package customer.thewaiapp.com.Address;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import customer.thewaiapp.com.Utility.Constants;

import customer.thewaiapp.com.R;

public class EditAddressActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private Spinner mSpinnerAddressType;

    private EditText mEditTextAddressName, mEditTextHouseNo, mEditTextAreaName, mEditTextLandMark, mEditTextCity, mEditTextPincode, mEditTextState;
    private Button mButtonSubmit;
    Address mAddress;
    String addressName, addressType, houseNo, areaName, landmark, city, state, country, pincode, UID;
    TextView mTextviewDisplayMessage;

    private ProgressDialog mSaveProgressDialog;

    boolean failFlag = false;
    public static final String selectAddressTypeLabel = "Select Address Type*";
    
    Toolbar mtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        mAddress = (Address) getIntent().getSerializableExtra("Address");
        Log.v("wai", "EditAddressActivity onCreate maddress: " + mAddress);
        Log.v("wai", "EditAddressActivity onCreate addressid: " + mAddress.getAddressId());
        Log.v("wai", "EditAddressActivity onCreate addressName: " + mAddress.getAddressName());
        Log.v("wai", "EditAddressActivity onCreate selectAdresstype: " + selectAddressTypeLabel);
        mtoolbar = (Toolbar) findViewById(R.id.editaddress_toolbar);
        mtoolbar.setTitle("Edit Address");
        mtoolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String[] addressTypeList = new String[]{mAddress.getAddressType(), "Flat", "House"};
        Log.v("wai", "EditAddressActivity onCreate addressTypelist: " + addressTypeList);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Log.v("wai", "EditAddressActivity onCreate UID: " + UID);
        }

        mEditTextAddressName = (EditText) findViewById(R.id.editaddress_et_address_name);
        mEditTextHouseNo = (EditText) findViewById(R.id.editaddress_et_houseno);
        mEditTextAreaName = (EditText) findViewById(R.id.editaddress_et_areaname);
        mEditTextLandMark = (EditText) findViewById(R.id.editaddress_et_landmark);
        mEditTextCity = (EditText) findViewById(R.id.editaddress_et_city);
        mEditTextPincode = (EditText) findViewById(R.id.editaddress_et_pincode);
        mEditTextState = (EditText) findViewById(R.id.editaddress_et_state);
        mButtonSubmit = (Button) findViewById(R.id.editaddress_button_submit);
        mTextviewDisplayMessage = (TextView) findViewById(R.id.editaddress_textview_displaymessage);

        mTextviewDisplayMessage.setVisibility(TextView.GONE);

        mEditTextAddressName.setText(mAddress.getAddressName());
        mEditTextHouseNo.setText(mAddress.getHouseNo());
        mEditTextAreaName.setText(mAddress.getAreaName());
        mEditTextLandMark.setText(mAddress.getLandmark());
        mEditTextCity.setText(mAddress.getCity());
        mEditTextPincode.setText(mAddress.getPincode());
        mEditTextState.setText(mAddress.getState());

        mSpinnerAddressType = (Spinner) findViewById(R.id.editaddress_spinner_address_type);

        if (mSpinnerAddressType != null) {
            mSpinnerAddressType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Spinner addressTypeSpinner = (Spinner) parent;
                    if (addressTypeSpinner.getId() == R.id.editaddress_spinner_address_type) {
                        addressType = parent.getItemAtPosition(position).toString();

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(EditAddressActivity.this, android.R.layout.simple_spinner_dropdown_item, addressTypeList);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerAddressType.setAdapter(genderAdapter);

        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                failFlag = false;
                mSaveProgressDialog = new ProgressDialog(EditAddressActivity.this);
                mSaveProgressDialog.setMessage("Saving Your Address");
                mSaveProgressDialog.setCancelable(false);
                updateAddress();
            }
        });
    }

    private void updateAddress() {
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

        if (addressName.equals("")) {
            failFlag = true;
            mEditTextAddressName.setError(EmptyString);
            mEditTextAddressName.requestFocus();
        }
        if (houseNo.equals("")) {
            failFlag = true;
            mEditTextHouseNo.setError(EmptyString);
            mEditTextHouseNo.requestFocus();
        }
        if (areaName.equals("")) {
            failFlag = true;
            mEditTextAreaName.setError(EmptyString);
            mEditTextAreaName.requestFocus();
        }
        if (city.equals("")) {
            failFlag = true;
            mEditTextCity.setError(EmptyString);
            mEditTextCity.requestFocus();
        }
        if (state.equals("")) {
            failFlag = true;
            mEditTextState.setError(EmptyString);
            mEditTextState.requestFocus();
        }
        if (pincode.equals("")) {
            failFlag = true;
            mEditTextPincode.setError(EmptyString);
            mEditTextPincode.requestFocus();
        }
        if (pincode.length() < 6 && houseNo.length() > 0) {
            failFlag = true;
            mEditTextPincode.setError(Provide_6_characters);
            mEditTextPincode.requestFocus();
        }
        if (addressType.equals(selectAddressTypeLabel)) {
            failFlag = true;
            Toast.makeText(EditAddressActivity.this, "Please select valid Address Type", Toast.LENGTH_SHORT).show();
        }
        if (!failFlag) {
            mSaveProgressDialog.show();
            Address address = new Address(mAddress.getAddressId(), addressName, addressType, houseNo, areaName, landmark, city, state, country, pincode);

            mDatabase.child(Constants.FIREBASE_CHILD_ADDRESS).child(UID).child(mAddress.getAddressId()).setValue(address).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(EditAddressActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditAddressActivity.this, "Addess Updated", Toast.LENGTH_SHORT).show();
                        mButtonSubmit.setVisibility(Button.GONE);
                        mTextviewDisplayMessage.setVisibility(TextView.VISIBLE);
                    }
                    mSaveProgressDialog.dismiss();
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = NavUtils.getParentActivityIntent(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            NavUtils.navigateUpTo(this, intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

