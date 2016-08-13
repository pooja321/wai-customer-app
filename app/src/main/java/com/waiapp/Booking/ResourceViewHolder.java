package com.waiapp.Booking;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.waiapp.Model.Resource;
import com.waiapp.R;

public class ResourceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView mTextViewName, mTextViewResourceRating;
    private ImageView mImageViewResourcePic, mImageViewGenderIcon;

    public ResourceViewHolder(View itemView) {
        super(itemView);

        mTextViewName = (TextView) itemView.findViewById(R.id.list_item_name);
        mTextViewResourceRating = (TextView) itemView.findViewById(R.id.list_item_rating);
        mImageViewResourcePic = (ImageView) itemView.findViewById(R.id.list_item_profilePic);
        mImageViewGenderIcon = (ImageView) itemView.findViewById(R.id.list_item_gender);
    }

    //set what views will display
    public void bindView(Resource resource){
        String _fullName = resource.getFirstName().concat(" ").concat(resource.getLastName());
        mTextViewName.setText(_fullName);
        mTextViewResourceRating.setText(String.valueOf(resource.getRating()));
        switch (resource.getGender()){
            case ("Male"):{
                mImageViewResourcePic.setImageResource(R.drawable.malechef);
                mImageViewGenderIcon.setImageResource(R.drawable.human_male);
                mImageViewGenderIcon.setColorFilter(Color.rgb(33,150,243));
                break; }
            case ("Female"):{
                mImageViewResourcePic.setImageResource(R.drawable.femalechef);
                mImageViewGenderIcon.setImageResource(R.drawable.human_female);
                mImageViewGenderIcon.setColorFilter(Color.rgb(233,30,99));
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {

    }
}
