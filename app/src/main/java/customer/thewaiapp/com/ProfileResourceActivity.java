package customer.thewaiapp.com;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import customer.thewaiapp.com.Model.Resource;
import customer.thewaiapp.com.Utility.Constants;
import customer.thewaiapp.com.confirmation.BookingConfirmationActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;


public class ProfileResourceActivity extends AppCompatActivity {


    String name,key,callingFragment,Resource_id;
    TextView TextviewResourceName1,mTextviewResourceNameToolbar,mTextviewDescription,mTextviewAge,mTextviewExperience,
            mTextviewAdharNumber,mTextviewPoliceverify,mTextviewMaritalStatus,mTextviewChildren;
    Button btnbook;
    private Realm mRealm;
    private DatabaseReference mDatabase;
    Resource resource;
    ProgressBar mProgressBar;
    FirebaseStorage mStorage;
    StorageReference mStorageRef;
    CircleImageView mCircleImageview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_resourcedetails);
        name = getIntent().getStringExtra("resourceName");
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_profileuser_toolbar);
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);
        mCircleImageview = (CircleImageView) findViewById(R.id.profile_ci_user_id);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRealm = Realm.getDefaultInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Resource_id = getIntent().getStringExtra("resourceId");
        Log.v("Profile123","Resource_id: "+Resource_id);
        key = getIntent().getStringExtra("resourceId");
        mProgressBar  = (ProgressBar) findViewById(R.id.Resource_progressbar);
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();
        mStorageRef.child("Resources/Profile_Pictures/"+Resource_id+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ProfileResourceActivity.this)
                        .load(uri).listener(new RequestListener<Uri, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                        mProgressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        mProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).override(400, 400).diskCacheStrategy(DiskCacheStrategy.ALL).into(mCircleImageview);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        callingFragment = getIntent().getStringExtra("mCallingFragment");
        TextviewResourceName1 = (TextView) findViewById(R.id.profile_tv_upperusername);
        mTextviewResourceNameToolbar = (TextView) findViewById(R.id.profile_resource_resourcename);
        mTextviewDescription = (TextView) findViewById(R.id.profileresource_textview_description);
        mTextviewAge = (TextView) findViewById(R.id.profileresource_textview_age);
        mTextviewExperience = (TextView) findViewById(R.id.profileresource_textview_experience);
        mTextviewAdharNumber = (TextView) findViewById(R.id.profileresource_textview_adharnumber);
        mTextviewChildren = (TextView) findViewById(R.id.profileresource_textview_children);
        mTextviewMaritalStatus = (TextView) findViewById(R.id.profileresource_textview_maritalstatus);
        mTextviewPoliceverify = (TextView) findViewById(R.id.profileresource_textview_policeverify);
        btnbook =(Button) findViewById(R.id.profile_btn_book);
        TextviewResourceName1.setText(name);

        mDatabase.child(Constants.FIREBASE_CHILD_RESOURCES).child(Resource_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resource = dataSnapshot.getValue(Resource.class);
                mTextviewExperience.setText(String.valueOf(resource.getExperience()));
                mTextviewPoliceverify.setText(resource.getPoliceverification());
                mTextviewMaritalStatus.setText(resource.getMaritalstatus());
                mTextviewChildren.setText(String.valueOf(resource.getChildren()));
                String adhar = String.valueOf(resource.getAdharnumber());
               String adhar1 =  adhar.replace(adhar.substring(3,adhar.length()-3),"*********");
                mTextviewAdharNumber.setText(adhar1);
                mTextviewDescription.setText(resource.getDescription());
                mTextviewAge.setText(String.valueOf(resource.getAge()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btnbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProfileResourceActivity.this, BookingConfirmationActivity.class);
                intent.putExtra("resourceKey",key);
                intent.putExtra("resourceName",name);
                intent.putExtra("resourceMobile",resource.getMobileNumber());
                intent.putExtra("fragment_name",callingFragment);
                startActivity(intent);
            }
        });
    }
}
