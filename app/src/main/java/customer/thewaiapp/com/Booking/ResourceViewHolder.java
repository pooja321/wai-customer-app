package customer.thewaiapp.com.Booking;

import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import customer.thewaiapp.com.Model.ResourceOnline;

import customer.thewaiapp.com.R;

public class ResourceViewHolder extends RecyclerView.ViewHolder {

    private TextView mTextViewName, mTextViewResourceRating;
    private ImageView mImageViewResourcePic, mImageViewGenderIcon;
    Uri profilePicUri;

    public ResourceViewHolder(View itemView) {
        super(itemView);

        mTextViewName = (TextView) itemView.findViewById(R.id.list_item_name);
        mTextViewResourceRating = (TextView) itemView.findViewById(R.id.list_item_rating);
        mImageViewResourcePic = (ImageView) itemView.findViewById(R.id.list_item_profilePic);
        mImageViewGenderIcon = (ImageView) itemView.findViewById(R.id.list_item_gender);
    }

    //set what views will display
    public void bindView(ResourceOnline resource){
        String _fullName = resource.getName();
        if(resource.getPicture() != null){
            profilePicUri = Uri.parse(resource.getPicture());
            Glide.with(itemView.getContext()).load(profilePicUri).placeholder(R.drawable.beforeafter).into(mImageViewResourcePic);
        }else{
            Glide.with(itemView.getContext()).load(R.drawable.beforeafter).into(mImageViewResourcePic);
        }
        mTextViewName.setText(_fullName);
        mTextViewResourceRating.setText(String.valueOf(resource.getRating()));
        Glide.with(itemView.getContext()).load(profilePicUri).placeholder(R.drawable.beforeafter).into(mImageViewResourcePic);
        switch (resource.getGender()){
            case ("Male"):{
//                mImageViewResourcePic.setImageResource(R.drawable.malechef);
                mImageViewGenderIcon.setImageResource(R.drawable.human_male);
                mImageViewGenderIcon.setColorFilter(Color.rgb(33,150,243));
                break; }
            case ("Female"):{
//                mImageViewResourcePic.setImageResource(R.drawable.femalechef);
                mImageViewGenderIcon.setImageResource(R.drawable.human_female);
                mImageViewGenderIcon.setColorFilter(Color.rgb(233,30,99));
                break;
            }
        }
    }
}
