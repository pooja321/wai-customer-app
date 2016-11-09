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
import com.google.firebase.database.ServerValue;
import com.waiapp.MainActivity;
import com.waiapp.Model.User;
import com.waiapp.R;
import com.waiapp.Utility.Constants;

import java.util.HashMap;

import io.realm.Realm;

import static com.waiapp.Utility.Utilities.generateCustomerId;

public class FillDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText mEditTextFirstName, mEditTextLastName, mEditTextMobile;
    Spinner mSpinnerGender;
    Button mButtonSubmit;
    Context context;
    private DatabaseReference mDatabase;
    Realm mRealm;

    public static final String selectGenderLabel = "Select Gender";
    private String[] _gender = new String[]{selectGenderLabel,"Male", "Female"};
    private String _genderSelected;
    private String[] values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_details);

        mRealm = Realm.getDefaultInstance();
        Toolbar mtoolbar = (Toolbar) findViewById(R.id.fill_detail_toolbar);
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

        String _firstName = mEditTextFirstName.getText().toString();
        String _lastName = mEditTextLastName.getText().toString();
        long _mobile = Long.parseLong(mEditTextMobile.getText().toString());
        String _userUID = values[0];
        String _Email = values[1];
        HashMap<String, Object> timestampJoined = new HashMap<>();
        timestampJoined.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        HashMap<String, Object> timestampChanged = new HashMap<>();
        timestampChanged.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);

        String userId = generateCustomerId();
        User user = new User(userId, _Email, _firstName, _genderSelected, _lastName, _mobile, timestampChanged, timestampJoined);

        mDatabase.child(Constants.FIREBASE_CHILD_USERS).child(_userUID).setValue(user);
        startActivity(new Intent(FillDetailsActivity.this, MainActivity.class));
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
