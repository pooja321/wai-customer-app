package com.waiapp.Booking;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.waiapp.Model.ResourceOnline;
import com.waiapp.R;

public abstract class ListViewFragment extends Fragment {

    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    private LinearLayoutManager mManager;
    private FirebaseRecyclerAdapter<ResourceOnline, ResourceViewHolder> mAdapter;
    Query resourceQuery;
    String callingFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("wai","ListViewFragment oncreate");
    }

    // callback interface to implement on item list click listener
    public interface OnResourceSelectedInterface{
//        void onListResourceSelected(String key, ResourceOnline index, String callingFragment);
        void onListResourceSelected(String key, String Name, String callingFragment);
        void onResourceListdownloadcomplete(Boolean iscomplete);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v("wai","ListViewFragment onCreateView");
        View view = inflater.inflate(R.layout.fragment_main_listview, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerView = (RecyclerView) view.findViewById(R.id.main_rv_list_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v("wai","ListViewFragment onActivityCreated");
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mManager);
        resourceQuery = setQuery();
        try {
            new loadResourceListTask().execute(resourceQuery);
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
    }
    public abstract Query setQuery();
    public abstract String getCallingFragmentName();

    class loadResourceListTask extends AsyncTask<Query, Void, Void>{

        final OnResourceSelectedInterface listener = (OnResourceSelectedInterface) getActivity();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.v("wai", "ListViewFragment onPreExecute");

        }

        @Override
        protected Void doInBackground(Query... params) {
            Log.v("wai", "ListViewFragment doInBackground");
            mAdapter = new FirebaseRecyclerAdapter<ResourceOnline, ResourceViewHolder>(ResourceOnline.class,R.layout.list_resource_item,
                    ResourceViewHolder.class,params[0]) {
                @Override
                protected void populateViewHolder(ResourceViewHolder viewHolder, final ResourceOnline model, final int position) {
                    final DatabaseReference resourceRef = getRef(position);
                    Log.v("wai", String.valueOf(position));

                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callingFragment = getCallingFragmentName();
//                            listener.onListResourceSelected(resourceRef.getKey(),model,callingFragment);
                            listener.onListResourceSelected(resourceRef.getKey(),model.getName(),callingFragment);
                        }
                    });
                    viewHolder.bindView(model);
                }
            };
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.v("wai", "ListViewFragment onPostExecute");
            recyclerView.setAdapter(mAdapter);
            listener.onResourceListdownloadcomplete(true);
        }
    }
}
