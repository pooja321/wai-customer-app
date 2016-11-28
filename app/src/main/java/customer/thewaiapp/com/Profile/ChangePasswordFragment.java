package customer.thewaiapp.com.Profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

import customer.thewaiapp.com.R;



public class ChangePasswordFragment extends Fragment {
    EditText mEditTextNewPassword, mEditTextConfirmPassword;
    Button mSubmit;
    String mNewPassword, mConfirmPassword;

    boolean failFlag = false;

    public static ChangePasswordFragment newInstance() {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        getActivity().setTitle("Change Password");
        mEditTextNewPassword = (EditText) view.findViewById(R.id.fragment_changepassword_new);
        mEditTextConfirmPassword = (EditText) view.findViewById(R.id.fragment_changepassword_confirm);
        mSubmit = (Button) view.findViewById(R.id.button_changepassword_submit);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    mAuthProgressDialog.show();
                failFlag = false;
                changePassword();
            }

        });
        return view;
    }

    private void changePassword() {
        mNewPassword = mEditTextNewPassword.getText().toString();
        mConfirmPassword = mEditTextConfirmPassword.getText().toString();

        if (mNewPassword.equals("")) {
            failFlag = true;
            mEditTextNewPassword.setError("Please fill detail");

        }
        if (mConfirmPassword.equals("")) {
            failFlag = true;
            mEditTextConfirmPassword.setError("Please fill detail");

        }
        if (!failFlag) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                user.updatePassword(mNewPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Password is updated!", Toast.LENGTH_SHORT).show();
                                mEditTextNewPassword.setText("");
                                mEditTextNewPassword.clearFocus();
                                } else {
                                mEditTextNewPassword.setError(task.getException().getMessage());
                                    Toast.makeText(getActivity(), "Failed to update password!", Toast.LENGTH_SHORT).show();
                                }
//                            mAuthProgressDialog.dismiss();
                            }

                        });
            }
        }
    }
}