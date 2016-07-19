package com.waiapp.confirmation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.waiapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CookBookingConfirmationFragment extends Fragment {


    public CookBookingConfirmationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_cook_booking_confirmation, container, false);
        return view;
    }

}
