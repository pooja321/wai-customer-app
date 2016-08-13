package com.waiapp.Booking;

import android.app.ProgressDialog;
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
import com.waiapp.Model.Resource;
import com.waiapp.R;

public abstract class ListViewFragment extends Fragment {

    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    private LinearLayoutManager mManager;
    private FirebaseRecyclerAdapter<Resource, ResourceViewHolder> mAdapter;
    Query resourceQuery;
    String callingFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // callback interface to implement on item list click listener
    public interface OnResourceSelectedInterface{
        void onListResourceSelected(String key, Resource index, String callingFragment);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_listview, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerView = (RecyclerView) view.findViewById(R.id.main_rv_list_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

        private ProgressDialog progress = new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progress.setMessage("Fetching List of Resources for you");
            this.progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            this.progress.show();
        }

        @Override
        protected Void doInBackground(Query... params) {
            Log.v("wai","doinbackground");
            mAdapter = new FirebaseRecyclerAdapter<Resource, ResourceViewHolder>(Resource.class,R.layout.list_resource_item,
                    ResourceViewHolder.class,params[0]) {
                @Override
                protected void populateViewHolder(ResourceViewHolder viewHolder, final Resource model, final int position) {
                    final DatabaseReference resourceRef = getRef(position);
                    Log.v("wai", String.valueOf(position));
                    final OnResourceSelectedInterface listener = (OnResourceSelectedInterface) getActivity();
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callingFragment = getCallingFragmentName();
                            listener.onListResourceSelected(resourceRef.getKey(),model,callingFragment);
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
            recyclerView.setAdapter(mAdapter);
            if(progress.isShowing()){
                progress.dismiss();
            }
        }
    }
}
