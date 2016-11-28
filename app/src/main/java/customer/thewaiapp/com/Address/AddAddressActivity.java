package customer.thewaiapp.com.Address;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import customer.thewaiapp.com.Model.Address;
import customer.thewaiapp.com.R;
import customer.thewaiapp.com.Utility.Constants;;

public class AddAddressActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private DatabaseReference mDatabase;

    private EditText mEditTextAddressName, mEditTextHouseNo, mEditTextAreaName, mEditTextLandMark, mEditTextCity, mEditTextPincode, mEditTextState;
    private Spinner mSpinnerAddressType;
    private Button mButtonSubmit;
    private Toolbar mtoolbar;
    private ProgressDialog mSaveProgressDialog;

    String addressName, addressid, addressType, houseNo, areaName, landmark, city, state, country, pincode, UID;
    TextView mTextviewDisplayMessage;

    public static final String selectAddressTypeLabel = "Select Address Type*";
    private String[] addressTypeList = new String[]{selectAddressTypeLabel, "Flat", "House"};

    boolean failFlag = false;

    TextInputLayout mTIL_Address_title, mTIL_Housenum, mTIL_Area_name, mTIL_Landmark, mTIL_City, mTIL_State, mTIL_Pincode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        mtoolbar = (Toolbar) findViewById(R.id.addaddress_toolbar);
        mtoolbar.setTitle("Add Address");
        mtoolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        setTitle("Add Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEditTextAddressName = (EditText) findViewById(R.id.addaddress_et_address_name);
        mEditTextHouseNo = (EditText) findViewById(R.id.addaddress_et_houseno);
        mEditTextAreaName = (EditText) findViewById(R.id.addaddress_et_areaname);
        mEditTextLandMark = (EditText) findViewById(R.id.addaddress_et_landmark);
        mEditTextCity = (EditText) findViewById(R.id.addaddress_et_city);
        mEditTextPincode = (EditText) findViewById(R.id.addaddress_et_pincode);
        mEditTextState = (EditText) findViewById(R.id.addaddress_et_state);
        mTextviewDisplayMessage = (TextView) findViewById(R.id.addaddress_textview_displaymessage);

        mTextviewDisplayMessage.setVisibility(TextView.GONE);

        mTIL_Address_title = (TextInputLayout) findViewById(R.id.til_address_title);
        mTIL_Housenum = (TextInputLayout) findViewById(R.id.til_house_no);
        mTIL_Area_name = (TextInputLayout) findViewById(R.id.til_area_name);
        mTIL_Landmark = (TextInputLayout) findViewById(R.id.til_landmark);
        mTIL_City = (TextInputLayout) findViewById(R.id.til_city);
        mTIL_State = (TextInputLayout) findViewById(R.id.til_State);
        mTIL_Pincode = (TextInputLayout) findViewById(R.id.til_pincode);

        mSpinnerAddressType = (Spinner) findViewById(R.id.addaddress_spinner_address_type);
        if (mSpinnerAddressType != null) {
            mSpinnerAddressType.setOnItemSelectedListener(this);
        }

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(AddAddressActivity.this, android.R.layout.simple_spinner_dropdown_item, addressTypeList);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerAddressType.setAdapter(genderAdapter);

        mButtonSubmit = (Button) findViewById(R.id.addaddress_button_submit);
        mButtonSubmit.setOnClickListener(this);

        mSaveProgressDialog = new ProgressDialog(this);
        mSaveProgressDialog.setMessage("Saving Your Address");
        mSaveProgressDialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case (R.id.addaddress_button_submit):
                failFlag = false;
                addAddress();
                break;
        }
    }

    private void addAddress() {
        addressName = mEditTextAddressName.getText().toString();
        houseNo = mEditTextHouseNo.getText().toString();
        areaName = mEditTextAreaName.getText().toString();
        landmark = mEditTextLandMark.getText().toString();
        city = mEditTextCity.getText().toString();
        pincode = mEditTextPincode.getText().toString();
        state = mEditTextState.getText().toString();
        country = "India";

        String EmptyString = getResources().getString(R.string.provide_necessary_details);
        String Provide_6_characters = getResources().getString(R.string.less_than_6_character);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        addressid = mDatabase.child(Constants.FIREBASE_CHILD_ADDRESS).push().getKey();

        if (addressName.equals("")) {
            failFlag = true;
            mTIL_Address_title.setError(EmptyString);
        } else {
            mTIL_Address_title.setErrorEnabled(false);
        }

        if (houseNo.equals("")) {
            failFlag = true;
            mTIL_Housenum.setError(EmptyString);
        } else {
            mTIL_Housenum.setErrorEnabled(false);
        }
        if (areaName.equals("")) {
            failFlag = true;
            mTIL_Area_name.setError(EmptyString);
        } else {
            mTIL_Area_name.setErrorEnabled(false);
        }

        if (city.equals("")) {
            failFlag = true;
            mTIL_City.setError(EmptyString);
        } else {
            mTIL_City.setErrorEnabled(false);
        }
        if (state.equals("")) {
            failFlag = true;
            mTIL_State.setError(EmptyString);
        } else {
            mTIL_State.setErrorEnabled(false);
        }
        if (pincode.length() < 6) {
            failFlag = true;
            mTIL_Pincode.setError(Provide_6_characters);
        } else {
            mTIL_Pincode.setErrorEnabled(false);
        }
        if (addressType.equals(selectAddressTypeLabel)) {
            failFlag = true;
            Toast.makeText(this, "Please select valid Address Type", Toast.LENGTH_SHORT).show();
        }

        if (!failFlag) {
            mSaveProgressDialog.show();

            Address address = new Address(addressid, addressName, addressType, houseNo, areaName, landmark, city, state, country, pincode);

            mDatabase.child(Constants.FIREBASE_CHILD_ADDRESS).child(UID).child(addressid).setValue(address)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(AddAddressActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddAddressActivity.this, "Addess Added", Toast.LENGTH_SHORT).show();
                        mButtonSubmit.setVisibility(Button.GONE);
                        mTextviewDisplayMessage.setVisibility(TextView.VISIBLE);
                    }
                    mSaveProgressDialog.dismiss();
                }
            });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner addressTypeSpinner = (Spinner) parent;
        if (addressTypeSpinner.getId() == R.id.addaddress_spinner_address_type) {
            addressType = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id== android.R.id.home) {
            Intent intent = NavUtils.getParentActivityIntent(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            NavUtils.navigateUpTo(this, intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
