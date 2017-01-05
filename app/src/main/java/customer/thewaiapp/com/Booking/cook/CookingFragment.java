package customer.thewaiapp.com.Booking.cook;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import customer.thewaiapp.com.R;


public class CookingFragment extends Fragment {

    public CookingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cooking, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.cook_viewPager);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0){
                    return new CookMapChildFragment();
                }else{
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
//        tabLayout.getTabAt(0).setIcon(Utilities.tabIcons[0]);
//        tabLayout.getTabAt(1).setIcon(Utilities.tabIcons[1]);
        return view;
    }
}
