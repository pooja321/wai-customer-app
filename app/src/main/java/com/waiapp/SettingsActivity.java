package com.waiapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.waiapp.Utility.Constants;

public class SettingsActivity extends BaseActivity {

    Button mOkButton;
    RadioGroup mRadioGroupSort;
    RadioButton mRadioButtonRating, mRadioButtonDistance;
    CheckBox mCheckBoxMale, mCheckBoxFemale, mCheckBoxStarFive, mCheckBoxStarFour,
            mCheckBoxStarThree, mCheckBoxStarTwo, mCheckBoxStarOne;

    String sortBy;
    SharedPreferences mPrefSortFilter;
    SharedPreferences.Editor mEditor;
    Context mContext;
    int PRIVATE_MODE = 0;

    private static final String PREF_RESOURCE_FILTER_SORT = "sortFilterPreferences";
    private static final String KEY_SORT = "sortPreferences";
    private static final String PREF_FILTER = "filterPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sortBy = Constants.SORTBY_RATING;
        mContext = getApplicationContext();
        mPrefSortFilter = mContext.getSharedPreferences(PREF_RESOURCE_FILTER_SORT, PRIVATE_MODE);
        mEditor = mPrefSortFilter.edit();

        mOkButton = (Button) findViewById(R.id.settings_bt_ok);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
                startActivity(new Intent(SettingsActivity.this,MainActivity.class));
            }
        });
        mRadioGroupSort = (RadioGroup) findViewById(R.id.settings_sort_rg);
        mRadioButtonRating = (RadioButton) findViewById(R.id.settings_rb_rating);
        mRadioButtonDistance = (RadioButton) findViewById(R.id.settings_rb_distance);

        mCheckBoxMale = (CheckBox) findViewById(R.id.settings_filter_cb_male);
        mCheckBoxFemale = (CheckBox) findViewById(R.id.settings_filter_cb_female);
        mCheckBoxStarFive = (CheckBox) findViewById(R.id.settings_filter_cb_5star);
        mCheckBoxStarFour = (CheckBox) findViewById(R.id.settings_filter_cb_4star);
        mCheckBoxStarThree = (CheckBox) findViewById(R.id.settings_filter_cb_3star);
        mCheckBoxStarTwo = (CheckBox) findViewById(R.id.settings_filter_cb_2star);
        mCheckBoxStarOne = (CheckBox) findViewById(R.id.settings_filter_cb_1star);
    }

    private void saveSettings() {
        int id = mRadioGroupSort.getCheckedRadioButtonId();
        switch (id){
            case R.id.settings_rb_rating:
                Toast.makeText(SettingsActivity.this, "You selected rating", Toast.LENGTH_SHORT).show();
                sortBy = Constants.SORTBY_RATING;
                break;
            case R.id.settings_rb_distance:
                Toast.makeText(SettingsActivity.this, "You selected distance", Toast.LENGTH_SHORT).show();
                sortBy = Constants.SORTBY_DISTANCE;
                break;
        }
        Log.v("wai settings activity",sortBy);
        mEditor.putString(KEY_SORT, sortBy);
        mEditor.commit();
    }
    public String getSortPreferences(){
        return mPrefSortFilter.getString(KEY_SORT, sortBy);
    }
}
