package com.waiapp.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.waiapp.MainActivity;
import com.waiapp.R;

public class LoginFragment extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String LOG_TAG = LoginFragment.class.getSimpleName();
    private EditText mEditTextEmailInput, mEditTextPasswordInput;
    private Button mButtonSignIn;
    TextView SignUptextView;
    /* A dialog that is presented until the Firebase authentication finished. */
    private ProgressDialog mAuthProgressDialog;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    /* A flag indicating that a PendingIntent is in progress and prevents us from starting further intents. */
    private boolean mGoogleIntentInProgress;
    /* Request code used to invoke sign in user interactions for Google+ */
    public static final int RC_GOOGLE_LOGIN = 1;
    /* A Google account object that is populated if the user signs in with Google */
    GoogleSignInAccount mGoogleAccount;
    protected GoogleApiClient mGoogleApiClient;

    public interface OnSignUpButtonClickedInterface {
        public void onSignUpFragmentSelected(Fragment fragment);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        SignUptextView = (TextView) view.findViewById(R.id.tv_sign_up);
        SignUptextView.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(LOG_TAG, "onAuthStateChanged:signed_in");
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);

                } else {
                    // User is signed out
                    Log.d(LOG_TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        /* Setup the Google API object to allow Google logins */
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        /*** Build a GoogleApiClient with access to the Google Sign-In API and the options specified by gso.      */
        if(mGoogleApiClient == null){
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }

        /*** Link layout elements from XML and setup progress dialog    */
        initializeScreen(view);

        /*** Call signInPassword() when user taps "Done" keyboard action     */
        mEditTextPasswordInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    signInPassword();
                }
                return true;
            }
        });
        mButtonSignIn.setOnClickListener(this);
        return view;
    }

    private void initializeScreen(View view) {
        mEditTextEmailInput = (EditText) view.findViewById(R.id.edit_text_email);
        mEditTextPasswordInput = (EditText) view.findViewById(R.id.edit_text_password);
        mButtonSignIn = (Button) view.findViewById(R.id.login_with_password);
        LinearLayout linearLayoutLoginActivity = (LinearLayout) view.findViewById(R.id.linear_layout_login_activity);
        initializeBackground(linearLayoutLoginActivity);
        /* Setup the progress dialog that is displayed later when authenticating with Firebase */
        mAuthProgressDialog = new ProgressDialog(getActivity());
        mAuthProgressDialog.setTitle(getString(R.string.progress_dialog_loading));
        mAuthProgressDialog.setMessage(getString(R.string.progress_dialog_authenticating_with_firebase));
        mAuthProgressDialog.setCancelable(false);
        /* Setup Google Sign In */
//        SignInButton signInButton = (SignInButton)view.findViewById(R.id.login_with_google);
//        signInButton.setSize(SignInButton.SIZE_WIDE);
//        signInButton.setOnClickListener(this);
    }

    private void initializeBackground(LinearLayout linearLayoutLoginActivity) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sign_up:
                SignUpFragment signUpFragment = new SignUpFragment();
                OnSignUpButtonClickedInterface listener = (OnSignUpButtonClickedInterface) getActivity();
                listener.onSignUpFragmentSelected(signUpFragment);
                break;
            case R.id.login_with_password:
                signInPassword();
                break;
//            case R.id.login_with_google:
                //onSignInGooglePressed(v);
//                break;
        }
    }
    private void signInPassword() {
        String email = mEditTextEmailInput.getText().toString();
        String password = mEditTextPasswordInput.getText().toString();

        if (email.equals("")) {
            mEditTextEmailInput.setError(getString(R.string.error_cannot_be_empty));
            return;
        }

        if (password.equals("")) {
            mEditTextPasswordInput.setError(getString(R.string.error_cannot_be_empty));
            return;
        }
        mAuthProgressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener( getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(LOG_TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(LOG_TAG, "signInWithEmail", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), "Authentication Successful.",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(),MainActivity.class));
//                            WaiApplication app = (WaiApplication) getActivity().getApplication();
//                            if(!app.getOrderPending() && !(app.getOrderPending()== null)) {
//                                Toast.makeText(getActivity(), String.valueOf(app.getOrderPending()), Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(getActivity(), MainActivity.class));
//                            }else{
//                                Toast.makeText(getActivity(), String.valueOf(app.getOrderPending()), Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(getActivity(), AddressActivity.class));
//                            }
                        }
                        mAuthProgressDialog.dismiss();
                    }
                });
    }

    /*** Sign in with Google plus when user clicks "Sign in with Google" textView (button)  */
    public void onSignInGooglePressed(View view) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GOOGLE_LOGIN);

    }
    /*** This callback is triggered when any startActivityForResult finishes. The requestCode maps to
     * the value passed into startActivityForResult.  */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...); */
        if (requestCode == RC_GOOGLE_LOGIN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(LOG_TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            mGoogleAccount = result.getSignInAccount();
            AuthCredential credential = GoogleAuthProvider.getCredential(mGoogleAccount.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(LOG_TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                            if (!task.isSuccessful()) {
                                Log.w(LOG_TAG, "signInWithCredential", task.getException());
                                Toast.makeText(getActivity(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }else{
                                startActivity(new Intent(getActivity(),MainActivity.class));
//                                WaiApplication app = (WaiApplication) getActivity().getApplication();
//                                if(!app.getOrderPending() && !(app.getOrderPending()== null)) {
//                                    Toast.makeText(getActivity(), String.valueOf(app.getOrderPending()), Toast.LENGTH_SHORT).show();
//                                    startActivity(new Intent(getActivity(), MainActivity.class));
//                                }else{
//                                    Toast.makeText(getActivity(), String.valueOf(app.getOrderPending()), Toast.LENGTH_SHORT).show();
//                                    startActivity(new Intent(getActivity(), AddressActivity.class));
//                                }
                            }
                        }
                    });
        } else {
            if (result.getStatus().getStatusCode() == GoogleSignInStatusCodes.SIGN_IN_CANCELLED) {
                showErrorToast("The sign in was cancelled. Make sure you're connected to the internet and try again.");
            } else {
                showErrorToast("Error handling the sign in: " + result.getStatus().getStatusMessage());
            }
            mAuthProgressDialog.dismiss();
        }
    }

    private void showErrorToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {

        //An unresolvable error has occurred and Google APIs (including Sign-In) will not
        mAuthProgressDialog.dismiss();
        showErrorToast(result.toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null){
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }

    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
