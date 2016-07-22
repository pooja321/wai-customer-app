package com.waiapp.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.waiapp.MainActivity;
import com.waiapp.R;

/**
 * Created by keviv on 03/05/2016.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener {

    private static final String LOG_TAG = SignUpFragment.class.getSimpleName();
    private ProgressDialog mAuthProgressDialog;
//    private Firebase mFirebaseRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText mEditTextUsernameCreate, mEditTextEmailCreate, mEditTextPasswordCreate;
    private Button mButtonSignUp;
    private TextView mTextViewSignInLink;
    private String mUserName, mUserEmail, mPassword;
    public interface OnSignInButtonClickedInterface {
        public void onSignInFragmentSelected(Fragment fragment);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.fragment_signup,container,false);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);

                } else {
                    // User is signed out
                    Log.d(LOG_TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        initializeScreen(view);
        return view;
    }

    private void initializeScreen(View view) {

        mEditTextUsernameCreate = (EditText) view.findViewById(R.id.edit_text_username_create);
        mEditTextEmailCreate = (EditText) view.findViewById(R.id.edit_text_email_create);
        mEditTextPasswordCreate = (EditText) view.findViewById(R.id.edit_text_password_create);

        mTextViewSignInLink = (TextView) view.findViewById(R.id.tv_sign_in);
        mTextViewSignInLink.setOnClickListener(this);
        mButtonSignUp = (Button) view.findViewById(R.id.btn_create_account_final);
        mButtonSignUp.setOnClickListener(this);
        /* Setup the progress dialog that is displayed later when authenticating with Firebase */
        mAuthProgressDialog = new ProgressDialog(getActivity());
        mAuthProgressDialog.setTitle(getResources().getString(R.string.progress_dialog_loading));
        mAuthProgressDialog.setMessage(getResources().getString(R.string.progress_dialog_creating_user_with_firebase));
        mAuthProgressDialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_create_account_final:
                onCreateAccountPressed();
                break;
            case R.id.tv_sign_in:
                LoginFragment loginFragment = new LoginFragment();
                OnSignInButtonClickedInterface listener = (OnSignInButtonClickedInterface) getActivity();
                listener.onSignInFragmentSelected(loginFragment);

        }
    }

    private void onCreateAccountPressed() {

        mUserName = mEditTextUsernameCreate.getText().toString();
        mUserEmail = mEditTextEmailCreate.getText().toString().toLowerCase();
        mPassword = mEditTextPasswordCreate.getText().toString();
        boolean validEmail = isEmailValid(mUserEmail);
        boolean validUserName = isUserNameValid(mUserName);
        boolean validPassword = isPasswordValid(mPassword);

        if (!validEmail || !validUserName || !validPassword) return;
        /**
         * If everything was valid show the progress dialog to indicate that
         * account creation has started
         */
        mAuthProgressDialog.show();

        mAuth.createUserWithEmailAndPassword(mUserEmail, mPassword)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(LOG_TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.d(LOG_TAG, "createUserWithEmail:onComplete:" + task.getException().getMessage());
                            Toast.makeText(getActivity(), "Authentication failed.",Toast.LENGTH_SHORT).show();
                        }else{
                            onAuthSuccess(task.getResult().getUser());
//                            startActivity(new Intent(getActivity(),MainActivity.class));
                            Toast.makeText(getActivity(),"User registered",Toast.LENGTH_SHORT).show();
                        }
                        mAuthProgressDialog.dismiss();
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        startActivity(new Intent(getActivity(),FillDetailsActivity.class).putExtra("user",new String[]{user.getUid(),user.getEmail()}));
    }

    private boolean isEmailValid(String email) {
        boolean isGoodEmail =
                (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            mEditTextEmailCreate.setError(String.format(getString(R.string.error_invalid_email_not_valid),
                    email));
            return false;
        }
        return isGoodEmail;
    }

    private boolean isUserNameValid(String userName) {
        if (userName.equals("")) {
            mEditTextUsernameCreate.setError(getResources().getString(R.string.error_cannot_be_empty));
            return false;
        }
        return true;
    }

    private boolean isPasswordValid(String password) {
        if (password.length() < 6) {
            mEditTextPasswordCreate.setError(getResources().getString(R.string.error_invalid_password_not_valid));
            return false;
        }
        return true;
    }
}
