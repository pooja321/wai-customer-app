package com.waiapp.EditProfile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.waiapp.R;

public class EditUserDetail extends Fragment {

    EditText mEditTextName,mEditTextEmail,mEditTextMobile;
    Button mButtonSubmit;
    Toolbar mActionBarToolbar;

    public static EditUserDetail newInstance() {
        EditUserDetail fragment = new EditUserDetail();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_user_detail, container, false);
        mEditTextName= (EditText) view.findViewById(R.id.edit_userdetail_name);
        mEditTextEmail=(EditText) view.findViewById(R.id.edit_userdetail_email);
        mEditTextMobile=(EditText) view.findViewById(R.id.edit_userdetail_mobile);
        mButtonSubmit= (Button) view.findViewById(R.id.button_submit);
        return view;
    }


}
