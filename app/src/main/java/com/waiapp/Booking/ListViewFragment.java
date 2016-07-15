package com.waiapp.Booking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.waiapp.Model.Resource;
import com.waiapp.R;

public class ListViewFragment extends Fragment {

    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    private LinearLayoutManager mManager;

    private FirebaseRecyclerAdapter<Resource, FirebaseViewHolder> mAdapter;

    // callback interface to implement on item list click listener
    public interface OnResourceSelectedInterface{
        void onListResourceSelected(int index);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        OnResourceSelectedInterface listener = (OnResourceSelectedInterface) getActivity();
        View view = inflater.inflate(R.layout.fragment_main_listview, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        recyclerView = (RecyclerView) view.findViewById(R.id.main_rv_list_view);

        ListViewAdapter listAdapter = new ListViewAdapter(listener);
        recyclerView.setAdapter(listAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mManager = new LinearLayoutManager(getActivity());
//        mManager.setReverseLayout(true);
//        mManager.setStackFromEnd(true);
//        recyclerView.setLayoutManager(mManager);

//        Query postsQuery = mDatabase.child(Constants.CHILD_RESOURCE).child(Constants.CHILD_COOK);

    }

//    public String getUid() {
//        return FirebaseAuth.getInstance().getCurrentUser().getUid();
//    }
}