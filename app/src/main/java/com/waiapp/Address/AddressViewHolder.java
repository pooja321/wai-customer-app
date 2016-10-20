package com.waiapp.Address;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.waiapp.Model.Address;
import com.waiapp.R;

/**
 * Created by keviv on 03/08/2016.
 */
public class AddressViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView mTextViewAddressName, mTextViewHouseNo, mTextViewAreaName, mTextViewLandMark, mTextViewCity, mTextViewState,
            mTextViewPincode;

    public AddressViewHolder(View itemView) {
        super(itemView);
        mTextViewAddressName = (TextView) itemView.findViewById(R.id.address_item_addressname);
        mTextViewHouseNo = (TextView) itemView.findViewById(R.id.address_item_houseno);
        mTextViewAreaName = (TextView) itemView.findViewById(R.id.address_item_areaname);
        mTextViewLandMark = (TextView) itemView.findViewById(R.id.address_item_landmark);
        mTextViewCity = (TextView) itemView.findViewById(R.id.address_item_cityname);
        mTextViewState = (TextView) itemView.findViewById(R.id.address_item_statename);
        mTextViewPincode = (TextView) itemView.findViewById(R.id.address_item_pincode);
    }

    @Override
    public void onClick(View v) {

    }

    public void bindView(Address address) {
        mTextViewAddressName.setText(String.valueOf(address.getAddressName()));
        mTextViewHouseNo.setText(String.valueOf(address.getHouseNo()));
        mTextViewAreaName.setText(String.valueOf(address.getAreaName()));
        mTextViewLandMark.setText(String.valueOf(address.getLandmark()));
        mTextViewCity.setText(String.valueOf(address.getCity()));
        mTextViewState.setText(String.valueOf(address.getState()));
        mTextViewPincode.setText(String.valueOf(address.getPincode()));
    }
}
