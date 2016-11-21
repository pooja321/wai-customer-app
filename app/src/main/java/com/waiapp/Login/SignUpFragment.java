package com.waiapp.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.waiapp.R;

/**
 * Created by keviv on 03/05/2016.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener {

    private static final String LOG_TAG = "wai";
    private String mUserEmail, mPassword;

    private ProgressDialog mAuthProgressDialog;
    private FirebaseAuth mAuth;
    private EditText mEditTextEmailCreate, mEditTextPasswordCreate;
    private Button mButtonSignUp;
    private TextView mTextViewSignInLink;


    public interface OnSignInButtonClickedInterface {
        public void onSignInFragmentSelected(Fragment fragment);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        mAuth = FirebaseAuth.getInstance();
        initializeScreen(view);
        return view;
    }

    private void initializeScreen(View view) {

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

        switch (v.getId()) {
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

        mUserEmail = mEditTextEmailCreate.getText().toString().toLowerCase();
        mPassword = mEditTextPasswordCreate.getText().toString();
        boolean validEmail = isEmailValid(mUserEmail);
        boolean validPassword = isPasswordValid(mPassword);

        if (!validEmail || !validPassword) return;
        /**
         * If everything was valid show the progress dialog to indicate that
         * account creation has started
         */
        mAuthProgressDialog.show();

        mAuth.createUserWithEmailAndPassword(mUserEmail, mPassword)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.v(LOG_TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.v(LOG_TAG, "createUserWithEmail:onComplete:" + task.getException().getMessage());
                            Log.v("wai","createUserWithEmail:Exception ", task.getException());
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthUserCollisionException e) {
                                mEditTextEmailCreate.setError(getString(R.string.error_email_in_use));
                            } catch(Exception e) {
                                Log.e(LOG_TAG, e.getMessage());
                            }
                        } else {
                            onAuthSuccess(task.getResult().getUser());
                            Toast.makeText(getActivity(), "User registered", Toast.LENGTH_SHORT).show();
                        }
                        mAuthProgressDialog.dismiss();
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        sendVerificationEmail(user);
        startActivity(new Intent(getActivity(), FillDetailsActivity.class).putExtra("user", new String[]{user.getUid(), user.getEmail()}));
    }

    private void sendVerificationEmail(FirebaseUser user) {
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Verification Email has been sent", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isEmailValid(String email) {

        boolean isGoodEmail = (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());

        if (!isGoodEmail) {
            mEditTextEmailCreate.setError(String.format(getString(R.string.error_invalid_email_not_valid),  email));
            return false;
        }
        return isGoodEmail;
    }

    private boolean isPasswordValid(String password) {
        if (password.length() < 6) {
            mEditTextPasswordCreate.setError(getResources().getString(R.string.error_invalid_password_not_valid));
            return false;
        }
        return true;
    }

}
