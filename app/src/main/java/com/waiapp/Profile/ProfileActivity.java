package com.waiapp.Profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.waiapp.BaseActivity;
import com.waiapp.Model.User;
import com.waiapp.R;

import io.realm.Realm;
import io.realm.RealmResults;

public class ProfileActivity extends BaseActivity implements View.OnClickListener {

    TextView mTextViewEmail, mTextViewGender, mTextViewMobileNum, mTextViewName, mTextViewUserId, mTextViewChangePassword;
    private ProgressDialog mAuthProgressDialog;

    Button mEditDetail;

    Realm mRealm;

    User user;

    public static final String UserKey= "user_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("My Profile");

        mRealm = Realm.getDefaultInstance();

        String UserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        mTextViewEmail = (TextView) findViewById(R.id.user_profile_email);
        mTextViewGender = (TextView) findViewById(R.id.user_profile_gender);
        mTextViewMobileNum = (TextView) findViewById(R.id.user_profile_mobilenum);
        mTextViewName = (TextView) findViewById(R.id.user_profile_name);
        mTextViewUserId = (TextView) findViewById(R.id.user_profile_id);
        mTextViewChangePassword = (TextView) findViewById(R.id.user_profile_changepassword);
        mEditDetail = (Button) findViewById(R.id.user_profile_editdetail);
        mEditDetail.setOnClickListener(this);
        RealmResults<User> UserResults = mRealm.where(User.class).equalTo("Email", UserEmail).findAll();
        if (UserResults.size() > 0) {
            user = UserResults.get(0);
            if (user != null) {
                String firstname = user.getFirstName();
                String lastname = user.getLastName();
                String Email = user.getEmail();
                String Gender = user.getGender();
                long Mobile = user.getMobileNumber();
                String UserId = user.getUserId();
                mTextViewEmail.setText(Email);
                mTextViewGender.setText(Gender);
                mTextViewMobileNum.setText(String.valueOf(Mobile));
                mTextViewName.setText(String.format("%s %s", firstname, lastname));
                mTextViewUserId.setText(UserId);
            }
        }

        mTextViewChangePassword.setOnClickListener(this);
//        mAuthProgressDialog = new ProgressDialog(this);
//        mAuthProgressDialog.setTitle(getString(R.string.progress_dialog_loading));
//        mAuthProgressDialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.user_profile_changepassword:
                startActivity(new Intent(ProfileActivity.this, EditUserProfileActivity.class).putExtra(UserKey,2));
                break;
            case R.id.user_profile_editdetail:
                startActivity(new Intent(ProfileActivity.this, EditUserProfileActivity.class).putExtra(UserKey,1));
                break;
//                String password = mEditTextNewPassword.getText().toString();
//                mAuthProgressDialog.show();
//                if (password.equals("")) {
//                    mEditTextNewPassword.setError(getString(R.string.error_cannot_be_empty));
//                    mAuthProgressDialog.dismiss();
//                }else{
//                    changePassword(password);
//                }
//                break;
//
//        }
        }

//    private void changePassword(String password) {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        if (user != null) {
//            user.updatePassword(password)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()) {
//                                Toast.makeText(ProfileActivity.this, "Password is updated!", Toast.LENGTH_SHORT).show();
//                                mEditTextNewPassword.setText("");
//                                mEditTextNewPassword.clearFocus();
//                            } else {
//                                mEditTextNewPassword.setError(task.getException().getMessage());
//                                Toast.makeText(ProfileActivity.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
//                            }
//                            mAuthProgressDialog.dismiss();
//                        }
//                    });
//        }
//    }
    }
}