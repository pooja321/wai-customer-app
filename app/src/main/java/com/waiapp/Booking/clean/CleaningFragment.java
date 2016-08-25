package com.waiapp.Booking.clean;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.waiapp.R;


public class CleaningFragment extends Fragment {

    public CleaningFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("wai","CleaningFragment onCreateView");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cleaning, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.cleaning_viewPager);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0){
                    Log.v("wai","new cleanMapChildFragment");
                    return new cleanMapChildFragment();
                }else{
                    Log.v("wai","new cleanListChildFragment");
                    return new cleanListChildFragment();

                }
            }

            @Override
            public int getCount() {
                return 2;
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return position == 0 ? "Map" : "List";
            }
        });
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.cleaning_tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
