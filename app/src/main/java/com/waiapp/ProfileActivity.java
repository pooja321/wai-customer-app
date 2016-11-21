package com.waiapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ProfileActivity extends BaseActivity implements View.OnClickListener {

    private EditText mEditTextNewPassword;
    private Button mButtonChangePassword;
    private ProgressDialog mAuthProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Settings");
//        mEditTextNewPassword = (EditText) findViewById(R.id.profile_new_password);
//        mButtonChangePassword = (Button) findViewById(R.id.profile_bt_changePassword);
//        mButtonChangePassword.setOnClickListener(this);

//        mAuthProgressDialog = new ProgressDialog(this);
//        mAuthProgressDialog.setTitle(getString(R.string.progress_dialog_loading));
//        mAuthProgressDialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
//        int id = v.getId();
//        switch (id){
//            case R.id.profile_bt_changePassword:
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
