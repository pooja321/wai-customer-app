package customer.thewaiapp.com.Profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import customer.thewaiapp.com.Model.User;
import customer.thewaiapp.com.R;
import customer.thewaiapp.com.Utility.Constants;
import io.realm.Realm;


public class ChangeEmailFragment extends Fragment {

    EditText mEditTextNewEmail;
    Button mButtonSubmit;
    boolean failFlag = false;
    String mNewEmail;
    String UID;
    User user =new User();
    Realm mRealm;
    private DatabaseReference mDatabase;

    public static ChangeEmailFragment newInstance() {
        ChangeEmailFragment fragment = new ChangeEmailFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_change_email, container, false);
        mRealm = Realm.getDefaultInstance();
        getActivity().setTitle("Change Email");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mEditTextNewEmail= (EditText) view.findViewById(R.id.fragment_changeemail_new);
        mButtonSubmit= (Button) view.findViewById(R.id.button_changeemail_submit);
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                failFlag = false;
                UpdateEmail();
                changeEmail();
            }


        });
        return  view;
    }

    private void UpdateEmail() {
        mNewEmail = mEditTextNewEmail.getText().toString();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("email", mNewEmail);

        String UserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        mDatabase.child(Constants.FIREBASE_CHILD_USERS).child(UID).updateChildren(childUpdates);
        Log.v("wai","<<<<<<<UserEmail " + UserEmail);

        mRealm.beginTransaction();
        User UserResults = mRealm.where(User.class).equalTo("Email", UserEmail).findFirst();
        UserResults.setEmail(mNewEmail);
        mRealm.commitTransaction();
    }

    private void changeEmail() {
        mNewEmail = mEditTextNewEmail.getText().toString();


        if (mNewEmail.equals("")) {
            failFlag = true;
            mEditTextNewEmail.setError("Please fill detail");

        }
        if (!failFlag) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                user.updateEmail(mNewEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Email is updated!", Toast.LENGTH_SHORT).show();
                            mEditTextNewEmail.setText("");
                            mEditTextNewEmail.clearFocus();
                        } else {
                            mEditTextNewEmail.setError(task.getException().getMessage());
                            Toast.makeText(getActivity(), "Failed to update Email!", Toast.LENGTH_SHORT).show();
                        }
//                            mAuthProgressDialog.dismiss();
                    }

                });
            }
        }

    }

}
