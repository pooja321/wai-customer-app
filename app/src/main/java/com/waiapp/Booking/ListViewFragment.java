package com.waiapp.Booking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.waiapp.R;

/**
 * Created by keviv on 27/06/2016.
 */
public class ListViewFragment extends Fragment {

    // callback interface to implement on item list click listener
    public interface OnResourceSelectedInterface{
        void onListResourceSelected(int index);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        OnResourceSelectedInterface listener = (OnResourceSelectedInterface) getActivity();
        View view = inflater.inflate(R.layout.fragment_main_listview, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.main_rv_list_view);
        ListViewAdapter listAdapter = new ListViewAdapter(listener);
        recyclerView.setAdapter(listAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }
}
