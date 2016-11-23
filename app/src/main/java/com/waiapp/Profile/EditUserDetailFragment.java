package com.waiapp.Profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.waiapp.Model.User;
import com.waiapp.R;
import com.waiapp.Utility.Constants;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class EditUserDetailFragment extends Fragment {

    EditText mEditTextFirstName,mEditTextLastName, mEditTextMobile;
    Button mButtonSubmit;
    Toolbar mActionBarToolbar;
    Realm mRealm;
    User user;
    String UID;
    String UpdateFirstName,UpdateLastName;
    Long UpdateMobile;
    private DatabaseReference mDatabase;

    public static EditUserDetailFragment newInstance() {
        EditUserDetailFragment fragment = new EditUserDetailFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRealm = Realm.getDefaultInstance();

        String UserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        View view = inflater.inflate(R.layout.fragment_edit_user_detail, container, false);
        mEditTextFirstName= (EditText) view.findViewById(R.id.edit_userdetail_firstname);
        mEditTextLastName= (EditText) view.findViewById(R.id.edit_userdetail_lastname);
        mEditTextMobile = (EditText) view.findViewById(R.id.edit_userdetail_mobile);
        mButtonSubmit = (Button) view.findViewById(R.id.button_userdetail_submit);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        RealmResults<User> UserResults = mRealm.where(User.class).equalTo("Email", UserEmail).findAll();
        if (UserResults.size() > 0) {
            user = UserResults.get(0);
            if (user != null) {
                String firstname = user.getFirstName();
                String lastname = user.getLastName();
                long Mobile = user.getMobileNumber();
                mEditTextMobile.setText(String.valueOf(Mobile));
                mEditTextFirstName.setText(firstname);
                mEditTextLastName.setText(lastname);
            }
        }
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserData();
            }

        });
        return view;
    }

    private void updateUserData() {

        UpdateFirstName = mEditTextFirstName.getText().toString();
        UpdateLastName = mEditTextLastName.getText().toString();
        UpdateMobile = Long.valueOf(mEditTextMobile.getText().toString());

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("firstName", UpdateFirstName);
        childUpdates.put("lastName", UpdateLastName);
        childUpdates.put("mobileNumber", UpdateMobile);

        String UserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        mDatabase.child(Constants.FIREBASE_CHILD_USERS).child(UID).updateChildren(childUpdates);

        User UserResults = mRealm.where(User.class).equalTo("Email", UserEmail).findFirst();
        mRealm.beginTransaction();
        UserResults.setFirstName(UpdateFirstName);
        UserResults.setLastName(UpdateLastName);
        UserResults.setMobileNumber(UpdateMobile);
        mRealm.copyToRealmOrUpdate(user);
        mRealm.commitTransaction();

    }
}

