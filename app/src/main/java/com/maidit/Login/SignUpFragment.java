package com.maidit.Login;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.maidit.R;
import com.maidit.Utility.Constants;

import java.util.Map;

/**
 * Created by keviv on 03/05/2016.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener {

    private static final String LOG_TAG = SignUpFragment.class.getSimpleName();
    private ProgressDialog mAuthProgressDialog;
    private Firebase mFirebaseRef;
    private EditText mEditTextUsernameCreate, mEditTextEmailCreate, mEditTextPasswordCreate;
    private Button mButtonSignUp;
    private String mUserName, mUserEmail, mPassword;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.fragment_signup,container,false);
        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);
        initializeScreen(view);
        return view;
    }

    private void initializeScreen(View view) {

        mEditTextUsernameCreate = (EditText) view.findViewById(R.id.edit_text_username_create);
        mEditTextEmailCreate = (EditText) view.findViewById(R.id.edit_text_email_create);
        mEditTextPasswordCreate = (EditText) view.findViewById(R.id.edit_text_password_create);
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

        mFirebaseRef.createUser(mUserEmail, mPassword, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                /* Dismiss the progress dialog */
                mAuthProgressDialog.dismiss();
                Log.i(LOG_TAG, getString(R.string.log_message_auth_successful));
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                /* Error occurred, log the error and dismiss the progress dialog */
                Log.d(LOG_TAG, getString(R.string.log_error_occurred) +
                        firebaseError);
                mAuthProgressDialog.dismiss();
                /* Display the appropriate error message */
                if (firebaseError.getCode() == FirebaseError.EMAIL_TAKEN) {
                    mEditTextEmailCreate.setError(getString(R.string.error_email_taken));
                } else {
                    showErrorToast(firebaseError.getMessage());
                }

            }
        });
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
    /**
     * Show error toast to users
     */
    private void showErrorToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}
