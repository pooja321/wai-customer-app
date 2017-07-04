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

    private TextView mTextViewName, mTextViewResourceRating,mTextViewAge,mTextviewPlaceofwork,mTextviewExperience,mTextviewAdhar,mTextviewPoliceVerify;
    private ImageView mImageViewResourcePic, mImageViewGenderIcon;
    private Uri profilePicUri;

    public ResourceViewHolder(View itemView) {
        super(itemView);

        mTextViewName = (TextView) itemView.findViewById(R.id.list_item_name);
        mTextViewResourceRating = (TextView) itemView.findViewById(R.id.list_item_rating);
        mImageViewResourcePic = (ImageView) itemView.findViewById(R.id.list_item_profilePic);
        mImageViewGenderIcon = (ImageView) itemView.findViewById(R.id.list_item_gender);
        mTextViewAge = (TextView) itemView.findViewById(R.id.textview_age);
        mTextviewPlaceofwork = (TextView) itemView.findViewById(R.id.textview_placeofwork);
        mTextviewExperience = (TextView) itemView.findViewById(R.id.textview_experience);
        mTextviewAdhar = (TextView) itemView.findViewById(R.id.textview_adhar);
        mTextviewPoliceVerify = (TextView) itemView.findViewById(R.id.textview_policeverification);
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
        if (resource.getAge()!=0)
        {
            mTextViewAge.setText(String.valueOf(resource.getAge()));
        }
        if (resource.getPlaceofwork()!=null)
        {
            mTextviewPlaceofwork.setText(resource.getPlaceofwork());
        }
        if (resource.getExperience()!=0)
        {
            mTextviewExperience.setText(String.valueOf(resource.getExperience()));
        }
        if (resource.getAdhar()!=null)
        {
            mTextviewAdhar.setText(resource.getAdhar());
        }
        if (resource.getPolice_verification()!=null)
        {
            mTextviewPoliceVerify.setText(resource.getPolice_verification());
        }
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
