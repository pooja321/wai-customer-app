package customer.thewaiapp.com.Booking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import customer.thewaiapp.com.Model.ResourceOnline;
import customer.thewaiapp.com.ProfileResourceActivity;
import customer.thewaiapp.com.R;
import de.hdodenhof.circleimageview.CircleImageView;

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
    }

    // callback interface to implement on item list click listener
    public interface OnResourceSelectedInterface {
        void onListResourceSelected(String key, String Name, String callingFragment);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_listview, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.main_rv_list_view);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle("Loading..");
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mManager);

        mResourceQuery = setQuery();
        mResourceQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.v("wai","ListViewFragment onDataChange");
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

        mAdapter = new FirebaseRecyclerAdapter<ResourceOnline, ResourceViewHolder>(ResourceOnline.class, R.layout.list_resource_item,
                ResourceViewHolder.class, mResourceQuery) {
            @Override
            protected void populateViewHolder(ResourceViewHolder viewHolder, final ResourceOnline model, final int position) {
                mCallingFragment = getCallingFragmentName();
                CircleImageView cr = (CircleImageView) viewHolder.itemView.findViewById(R.id.list_item_profilePic);
                cr.setImageResource(R.drawable.blue);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), ProfileResourceActivity.class)
                                .putExtra("resourceName", model.getName())
                                .putExtra("resourceRate", model.getRating())
                                .putExtra("mCallingFragment", mCallingFragment)
                                .putExtra("resourceId",model.getResourceId()));

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
