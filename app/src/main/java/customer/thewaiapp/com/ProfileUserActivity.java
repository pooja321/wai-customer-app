package customer.thewaiapp.com;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import customer.thewaiapp.com.Booking.ListViewFragment;
import customer.thewaiapp.com.confirmation.BookingConfirmationActivity;


public class ProfileUserActivity extends AppCompatActivity {


    String name,rating,key,callingFragment;
    TextView TextviewResourceName,TextviewResourceRate,TextviewResourceName1;
    Button btnbook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        name = getIntent().getStringExtra("resourceName");
        rating = getIntent().getStringExtra("resourceRate");
        key = getIntent().getStringExtra("resourceId");
        callingFragment = getIntent().getStringExtra("mCallingFragment");
        TextviewResourceName = (TextView) findViewById(R.id.profile_tv_firstName_id);
        TextviewResourceRate = (TextView) findViewById(R.id.profile_tv_rating);
        TextviewResourceName1 = (TextView) findViewById(R.id.profile_tv_upperusername);
        btnbook =(Button) findViewById(R.id.profile_btn_book);
        TextviewResourceName.setText(name);
        TextviewResourceName1.setText(name);
        TextviewResourceRate.setText(rating);
        btnbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileUserActivity.this, BookingConfirmationActivity.class);
                intent.putExtra("resourceKey",key);
                intent.putExtra("resourceName",name);
                intent.putExtra("fragment_name",callingFragment);
                startActivity(intent);
            }
        });
    }
}
