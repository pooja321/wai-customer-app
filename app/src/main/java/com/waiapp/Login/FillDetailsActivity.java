package com.waiapp.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.waiapp.Address.AddressActivity;
import com.waiapp.MainActivity;
import com.waiapp.Model.User;
import com.waiapp.R;
import com.waiapp.Utility.Constants;
import com.waiapp.WaiApplication;

public class FillDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText mEditTextFirstName, mEditTextLastName, mEditTextMobile;
    Spinner mSpinnerGender;
    Button mButtonSubmit;
    Context context;
    private DatabaseReference mDatabase;

    public static final String selectGenderLabel = "Select Gender";
    private String[] _gender = new String[]{selectGenderLabel,"Male", "Female"};
    private String _genderSelected, _firstName, _lastName, _Email;
    private int _rating;
    private long _mobile;
    private String[] values;
    private Toolbar mtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_details);
        mtoolbar = (Toolbar) findViewById(R.id.fill_detail_toolbar);
        mtoolbar.setTitleTextColor(getResources().getColor( R.color.white));
        setSupportActionBar(mtoolbar);

        values = getIntent().getExtras().getStringArray("user");
        context = this;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mEditTextFirstName = (EditText) findViewById(R.id.edit_text_firstname_create);
        mEditTextLastName = (EditText) findViewById(R.id.edit_text_lastname_create);
        mEditTextMobile = (EditText) findViewById(R.id.edit_text_mobile_create);

        mSpinnerGender = (Spinner) findViewById(R.id.spinner_gender);
        if (mSpinnerGender != null) {
            mSpinnerGender.setOnItemSelectedListener(this);
        }
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(FillDetailsActivity.this, android.R.layout.simple_spinner_dropdown_item, _gender);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerGender.setAdapter(genderAdapter);

        mButtonSubmit = (Button) findViewById(R.id.button_submit);
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitDetails();
            }
        });
    }
    private void submitDetails() {

        _firstName = mEditTextFirstName.getText().toString();
        _lastName = mEditTextLastName.getText().toString();
        _mobile = Long.parseLong(mEditTextMobile.getText().toString());
        _rating = 5;
        _Email = values[1];

        String userId = values[0];
        User user = new User(_firstName,_lastName,_Email,_genderSelected,_mobile);

        mDatabase.child(Constants.CHILD_USERS).child(userId).setValue(user);
        WaiApplication app = (WaiApplication) getApplication();
        if(!app.getOrderPending() && !(app.getOrderPending()== null)) {
            startActivity(new Intent(FillDetailsActivity.this, MainActivity.class));
        }else{
            startActivity(new Intent(FillDetailsActivity.this, AddressActivity.class));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner genderSpinner = (Spinner) parent;
        if(genderSpinner.getId() == R.id.spinner_gender)   {
            _genderSelected = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
