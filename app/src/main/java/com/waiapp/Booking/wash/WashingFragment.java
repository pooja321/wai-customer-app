package com.waiapp.Booking.wash;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.waiapp.R;

public class WashingFragment extends Fragment {

    public WashingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_washing, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.washing_viewPager);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0){
                    return new washMapChildFragment();
                }else{
                    return new washListChildFragment();
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
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.washing_tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
}
