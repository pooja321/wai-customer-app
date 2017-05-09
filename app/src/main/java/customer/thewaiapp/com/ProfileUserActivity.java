package customer.thewaiapp.com;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import customer.thewaiapp.com.Booking.ListViewFragment;

public class ProfileUserActivity extends AppCompatActivity {
    String name,rating;
    TextView TextviewResourceName,TextviewResourceRate;
    Button btnbook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        name = getIntent().getStringExtra("resourceName");
        rating = getIntent().getStringExtra("resourceRate");
        TextviewResourceName = (TextView) findViewById(R.id.profile_tv_firstName_id);
        TextviewResourceRate = (TextView) findViewById(R.id.profile_tv_rating);
//        btnbook =(Button) findViewById(R.id.profile_btn_book);
//        TextviewResourceName.setText(name);
//        TextviewResourceRate.setText(rating);
//        btnbook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ListViewFragment.OnResourceSelectedInterface listener = (ListViewFragment.OnResourceSelectedInterface) getActivity();
//                mCallingFragment = getCallingFragmentName();
//                Log.v("wai", mCallingFragment);
//                listener.onListResourceSelected(model.getResourceId(), model.getName(), mCallingFragment);
//            }
//        });
    }
}
