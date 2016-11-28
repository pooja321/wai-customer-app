package customer.thewaiapp.com.Login;

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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import customer.thewaiapp.com.MainActivity;
import customer.thewaiapp.com.Model.User;
import customer.thewaiapp.com.Utility.Constants;

import customer.thewaiapp.com.R;

import io.realm.Realm;

public class LoginFragment extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String LOG_TAG = "wai";
    private EditText mEditTextEmailInput, mEditTextPasswordInput;
    private Button mButtonSignIn;
    TextView mTextViewSignUp, mTextViewForgotPassword;
    /* A dialog that is presented until the Firebase authentication finished. */
    private ProgressDialog mAuthProgressDialog;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference mDatabase;

    Realm mrealm;

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

        mrealm = Realm.getDefaultInstance();

        mTextViewSignUp = (TextView) view.findViewById(R.id.login_tv_sign_up);
        mTextViewForgotPassword = (TextView) view.findViewById(R.id.login_tv_forgot_password);
        mTextViewSignUp.setOnClickListener(this);
        mTextViewForgotPassword.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

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
        if (mGoogleApiClient == null) {
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
        mEditTextEmailInput = (EditText) view.findViewById(R.id.login_et_email);
        mEditTextPasswordInput = (EditText) view.findViewById(R.id.login_et_password);
        mButtonSignIn = (Button) view.findViewById(R.id.login_btn_signin);
        LinearLayout linearLayoutLoginActivity = (LinearLayout) view.findViewById(R.id.linear_layout_login_activity);
        initializeBackground(linearLayoutLoginActivity);

        /* Setup the progress dialog that is displayed later when authenticating with Firebase */
        mAuthProgressDialog = new ProgressDialog(getActivity());
        mAuthProgressDialog.setTitle(getString(R.string.progress_dialog_signingin));
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
            case R.id.login_tv_sign_up:
                SignUpFragment signUpFragment = new SignUpFragment();
                OnSignUpButtonClickedInterface listener = (OnSignUpButtonClickedInterface) getActivity();
                listener.onSignUpFragmentSelected(signUpFragment);
                break;
            case R.id.login_tv_forgot_password:
                startActivity(new Intent(getActivity(), ForgotPasswordActivity.class));
                break;
            case R.id.login_btn_signin:
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

        boolean validEmail = isEmailValid(email);

        if (!validEmail) {
            return;
        }

        if (password.equals("")) {
            mEditTextPasswordInput.requestFocus();
            mEditTextPasswordInput.setError(getString(R.string.error_cannot_be_empty));
            return;
        }
        mAuthProgressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(LOG_TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                if (!task.isSuccessful()) {
                    Log.v("wai", "exception ", task.getException());
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        mEditTextPasswordInput.requestFocus();
                        mEditTextPasswordInput.setError(getString(R.string.error_email_password_notmatch));
                    } catch (FirebaseAuthInvalidUserException e) {
                        mEditTextEmailInput.requestFocus();
                        mEditTextEmailInput.setError(getString(R.string.error_user_doesnt_exists));
                    } catch (Exception e) {
                        Log.e(LOG_TAG, e.getMessage());
                    }
                    mAuthProgressDialog.dismiss();
                } else {
                    FirebaseUser user = task.getResult().getUser();
                    getUserData(user);
                    Toast.makeText(getActivity(), "Authentication Successful.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void getUserData(final FirebaseUser user) {
        String UserKey = user.getUid();
        Log.v("wai", "Userkey: " + UserKey);
        mDatabase.child(Constants.FIREBASE_CHILD_USERS).child(UserKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                Log.v("wai", "Value from ondata change" + dataSnapshot.getValue().toString());

                mrealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        User user = dataSnapshot.getValue(User.class);
                        realm.copyToRealmOrUpdate(user);
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        mAuthProgressDialog.dismiss();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mAuthProgressDialog.dismiss();
            }
        });
    }

    private boolean isEmailValid(String email) {

        boolean isGoodEmail = (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());

        if (!isGoodEmail) {
            mEditTextEmailInput.requestFocus();
            mEditTextEmailInput.setError(String.format(getString(R.string.error_invalid_email_not_valid), email));
            return false;
        }
        return isGoodEmail;
    }

    /***
     * Sign in with Google plus when user clicks "Sign in with Google" textView (button)
     */
    public void onSignInGooglePressed(View view) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GOOGLE_LOGIN);

    }

    /***
     * This callback is triggered when any startActivityForResult finishes. The requestCode maps to
     * the value passed into startActivityForResult.
     */
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
                            } else {
                                startActivity(new Intent(getActivity(), MainActivity.class));
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
        if (mGoogleApiClient != null) {
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
