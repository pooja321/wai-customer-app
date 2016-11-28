package customer.thewaiapp.com.Booking;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import customer.thewaiapp.com.Model.ResourceOnline;

import customer.thewaiapp.com.R;

public abstract class ListViewFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mManager;
    private FirebaseRecyclerAdapter<ResourceOnline, ResourceViewHolder> mAdapter;
    Query mResourceQuery;
    String mCallingFragment;
    ProgressDialog mProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("wai","ListViewFragment oncreate");
    }

    // callback interface to implement on item list click listener
    public interface OnResourceSelectedInterface{
        void onListResourceSelected(String key, String Name, String callingFragment);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v("wai","ListViewFragment onCreateView");
        View view = inflater.inflate(R.layout.fragment_main_listview, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.main_rv_list_view);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle("Loading..");
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.setCancelable(true);
//        mProgressDialog.show();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v("wai","ListViewFragment onActivityCreated");
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mManager);

        mResourceQuery = setQuery();
        mResourceQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("wai","ListViewFragment onDataChange");
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
        });

        mAdapter = new FirebaseRecyclerAdapter<ResourceOnline, ResourceViewHolder>(ResourceOnline.class,R.layout.list_resource_item,
                ResourceViewHolder.class, mResourceQuery) {
            @Override
            protected void populateViewHolder(ResourceViewHolder viewHolder, final ResourceOnline model, final int position) {

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnResourceSelectedInterface listener = (OnResourceSelectedInterface) getActivity();
                        mCallingFragment = getCallingFragmentName();
                        Log.v("wai", mCallingFragment);
                        listener.onListResourceSelected(model.getResourceId(),model.getName(), mCallingFragment);
                    }
                });
                viewHolder.bindView(model);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    public abstract Query setQuery();
    public abstract String getCallingFragmentName();

}
