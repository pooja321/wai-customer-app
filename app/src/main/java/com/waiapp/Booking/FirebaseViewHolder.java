package com.waiapp.Booking;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.waiapp.Model.Resource;
import com.waiapp.R;

public class FirebaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView mTextViewName, mTextViewResourceRating;
    private ImageView mImageViewResourcePic, mImageViewResourceIcon;
//    private int mIndex;

    public FirebaseViewHolder(View itemView) {
        super(itemView);

        mTextViewName = (TextView) itemView.findViewById(R.id.list_item_name);
        mTextViewResourceRating = (TextView) itemView.findViewById(R.id.list_item_rating);
        mImageViewResourcePic = (ImageView) itemView.findViewById(R.id.list_item_profilePic);
        mImageViewResourceIcon = (ImageView) itemView.findViewById(R.id.list_item_resource_icon);
    }

    //set what views will display
    public void bindView(Resource resource){
//        mIndex = position;
        String _fullName = resource.getFirstName().concat(" ").concat(resource.getLastName());
        mTextViewName.setText(_fullName);
        mTextViewResourceRating.setText(resource.getRating());
    }

    @Override
    public void onClick(View v) {

    }
}
