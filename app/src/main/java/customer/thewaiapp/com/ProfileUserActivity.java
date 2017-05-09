package customer.thewaiapp.com;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ProfileUserActivity extends AppCompatActivity {
    String name,rating;
    TextView TextviewResourceName,TextviewResourceRate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        name = getIntent().getStringExtra("resourceName");
        rating = getIntent().getStringExtra("resourceRate");
        TextviewResourceName = (TextView) findViewById(R.id.profile_tv_firstName_id);
        TextviewResourceRate = (TextView) findViewById(R.id.profile_tv_rating);
        TextviewResourceName.setText(name);
        TextviewResourceRate.setText(rating);
    }
}
