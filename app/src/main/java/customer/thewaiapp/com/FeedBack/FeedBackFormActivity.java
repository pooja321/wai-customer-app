package customer.thewaiapp.com.FeedBack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import customer.thewaiapp.com.Model.User;
import customer.thewaiapp.com.R;
import io.realm.Realm;
import io.realm.RealmResults;

public class FeedBackFormActivity extends AppCompatActivity {

    Spinner mSpinner_Feedback;
    List<String> list = new ArrayList<>();
    private Toolbar mtoolbar;
    Realm mRealm;
    User user;

    EditText mEtName,mEtEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_form);
        mRealm = Realm.getDefaultInstance();

        String UserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        mtoolbar = (Toolbar) findViewById(R.id.feedbacktoolbar);
        mtoolbar.setTitle("Feedback");
        mEtName= (EditText) findViewById(R.id.Feedback_EditTextName);
        mEtEmail= (EditText) findViewById(R.id.Feedback_EditTextEmail);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String title = "Enter feedback type";
        list.add(title);
        list.add("Praise");
        list.add("Suggestion");
        list.add("Bug");
        mSpinner_Feedback = (Spinner) findViewById(R.id.SpinnerFeedbackType);
        SpinnerAdaptor adapter = new SpinnerAdaptor(list, FeedBackFormActivity.this);
        mSpinner_Feedback.setAdapter(adapter);
        mSpinner_Feedback.setDropDownVerticalOffset(8);

        RealmResults<User> UserResults = mRealm.where(User.class).equalTo("Email", UserEmail).findAll();
        if (UserResults.size() > 0) {
            user = UserResults.get(0);
            if (user != null) {
                String firstname = user.getFirstName();
                String lastname = user.getLastName();
                String Email = user.getEmail();
                mEtEmail.setText(Email);
                mEtName.setText(String.format("%s %s", firstname, lastname));
            }
        }

    }
}
