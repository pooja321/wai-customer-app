package customer.thewaiapp.com;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

public class AccountActivity extends AppCompatActivity {
    Button mButtonLogout;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();


        mAuth=FirebaseAuth.getInstance();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
               if (firebaseAuth.getCurrentUser()==null){
                   startActivity(new Intent(AccountActivity.this,GoogleSignInActivity.class));
               }

            }
        };

        mButtonLogout= (Button) findViewById(R.id.activity_account_btn_logout);
        mButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                //Auth.GoogleSignInApi.signOut(mGoogleApiClient);

               // Auth.GoogleSignInApi.signOut();
               // mAuth.signOut();
                //Intent intent=new Intent(AccountActivity.this,GoogleSignInActivity.class);
                //startActivity(intent);
                //Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
               // Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                //startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });
        // Configure Google Sign In

    }
//    private void signOut() {
//        // Firebase sign out
//        mAuth.signOut();
//
//        // Google sign out
//        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(@NonNull Status status) {
//                        updateUI(null);
//                    }
//                });
//    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
}
