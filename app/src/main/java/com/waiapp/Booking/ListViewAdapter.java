package com.waiapp.Booking;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.waiapp.Model.Resources;
import com.waiapp.R;

/**
 * Created by keviv on 28/06/2016.
 */
public class ListViewAdapter extends RecyclerView.Adapter {

//    private final ListViewFragment.OnResourceSelectedInterface mListener;

//    public ListViewAdapter(ListViewFragment.OnResourceSelectedInterface listener) {
//        mListener = listener;
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_resource_item, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ListViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return Resources.names.length;
    }


    //View.OnClickListener is used to tap on list item click
    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTextViewName, mTextViewResourceRating;
        private ImageView mImageViewResourcePic, mImageViewResourceIcon;
        private int mIndex;

        //bind List item views here
        public ListViewHolder(View itemView) {
            super(itemView);
            mTextViewName = (TextView) itemView.findViewById(R.id.list_item_name);
            mTextViewResourceRating = (TextView) itemView.findViewById(R.id.list_item_rating);
            mImageViewResourcePic = (ImageView) itemView.findViewById(R.id.list_item_profilePic);
            mImageViewResourceIcon = (ImageView) itemView.findViewById(R.id.list_item_resource_icon);
            //set click listener on list item
            itemView.setOnClickListener(this);
        }
        //set what views will display
        public void bindView(int position){
            mIndex = position;
            mTextViewName.setText(Resources.names[position]);
        }

        @Override
        public void onClick(View v) {
//            mListener.onListResourceSelected(mIndex);
        }
    }
}
