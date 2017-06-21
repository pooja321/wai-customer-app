package customer.thewaiapp.com;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import customer.thewaiapp.com.Model.Resource;
import customer.thewaiapp.com.Utility.Constants;
import customer.thewaiapp.com.confirmation.BookingConfirmationActivity;
import io.realm.Realm;


public class ProfileResourceActivity extends AppCompatActivity {


    String name,key,callingFragment,Resource_id;
    TextView TextviewResourceName1,mTextviewResourceNameToolbar;
    Button btnbook;
    private Realm mRealm;
    private DatabaseReference mDatabase;
    Resource resource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_resourcedetails);
        name = getIntent().getStringExtra("resourceName");
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_profileuser_toolbar);
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRealm = Realm.getDefaultInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Resource_id = getIntent().getStringExtra("resourceId");
        Log.v("Profile123","Resource_id: "+Resource_id);
        key = getIntent().getStringExtra("resourceId");
        callingFragment = getIntent().getStringExtra("mCallingFragment");
        TextviewResourceName1 = (TextView) findViewById(R.id.profile_tv_upperusername);
        mTextviewResourceNameToolbar = (TextView) findViewById(R.id.profile_resource_resourcename);
        btnbook =(Button) findViewById(R.id.profile_btn_book);
        TextviewResourceName1.setText(name);

        mDatabase.child(Constants.FIREBASE_CHILD_RESOURCES).child(Resource_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resource = dataSnapshot.getValue(Resource.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btnbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("Profile123","Resource_id: "+resource.getMobileNumber());
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
