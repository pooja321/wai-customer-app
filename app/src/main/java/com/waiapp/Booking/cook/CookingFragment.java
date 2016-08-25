package com.waiapp.Booking.cook;

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


public class CookingFragment extends Fragment {

    public CookingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("wai","CookingFragment onCreateView");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cooking, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.cook_viewPager);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0){
                    Log.v("wai","new CookMapChildFragment");
                    return new CookMapChildFragment();
                }else{
                    Log.v("wai","new CookListChildFragment");
                    return new CookListChildFragment();
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
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.cook_tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
}
