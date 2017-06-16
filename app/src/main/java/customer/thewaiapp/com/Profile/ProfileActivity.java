package customer.thewaiapp.com.Profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import customer.thewaiapp.com.BaseActivity;
import customer.thewaiapp.com.MainActivity;
import customer.thewaiapp.com.Model.User;
import customer.thewaiapp.com.R;
import customer.thewaiapp.com.Utility.Constants;
import io.realm.Realm;

public class ProfileActivity extends BaseActivity implements View.OnClickListener {

    TextView mTextViewEmail, mTextViewGender, mTextViewMobileNum, mTextViewName, mTextViewUserId, mTextViewChangePassword;
    private ProgressDialog mAuthProgressDialog;

    Button mEditDetail;

    Realm mRealm;

    User user;
    String UID;
    private DatabaseReference mDatabase;
    public static final String UserKey = "user_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("My Profile");

        mRealm = Realm.getDefaultInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String UserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        mTextViewEmail = (TextView) findViewById(R.id.user_profile_email);
        mTextViewGender = (TextView) findViewById(R.id.user_profile_gender);
        mTextViewMobileNum = (TextView) findViewById(R.id.user_profile_mobilenum);
        mTextViewName = (TextView) findViewById(R.id.user_profile_name);
        mTextViewUserId = (TextView) findViewById(R.id.user_profile_id);
        mTextViewChangePassword = (TextView) findViewById(R.id.user_profile_changepassword);
        mEditDetail = (Button) findViewById(R.id.user_profile_editdetail);
        mEditDetail.setOnClickListener(this);
        mDatabase.child(Constants.FIREBASE_CHILD_USERS).child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                mTextViewName.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
                mTextViewEmail.setText(user.getEmail());
                mTextViewGender.setText(user.getGender());
                mTextViewMobileNum.setText(String.valueOf(user.getMobileNumber()));
                mTextViewUserId.setText(user.getUserId());
                mRealm.beginTransaction();
                mRealm.copyToRealmOrUpdate(user);
                mRealm.commitTransaction();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mTextViewChangePassword.setOnClickListener(this);
        mTextViewEmail.setOnClickListener(this);
//        mAuthProgressDialog = new ProgressDialog(this);
//        mAuthProgressDialog.setTitle(getString(R.string.progress_dialog_loading));
//        mAuthProgressDialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.user_profile_changepassword:
                startActivity(new Intent(ProfileActivity.this, EditUserProfileActivity.class).putExtra(UserKey, 2));
                break;
            case R.id.user_profile_editdetail:
                startActivity(new Intent(ProfileActivity.this, EditUserProfileActivity.class).putExtra(UserKey, 1));
                break;
            case R.id.user_profile_email:
                startActivity(new Intent(ProfileActivity.this, EditUserProfileActivity.class).putExtra(UserKey, 3));
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
    }
}

