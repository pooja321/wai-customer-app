package customer.thewaiapp.com;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class FeedBackFormActivity extends AppCompatActivity {

    Spinner mSpinner_Feedback;
    List<String> list = new ArrayList<>();
    private Toolbar mtoolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_form);
        mtoolbar = (Toolbar) findViewById(R.id.feedbacktoolbar);
        mtoolbar.setTitle("Enter feedback details");
        list.add("Praise");
        list.add("Suggestion");
        list.add("Bug");
        mSpinner_Feedback = (Spinner) findViewById(R.id.SpinnerFeedbackType);
        SpinnerAdaptor adapter = new SpinnerAdaptor(list, FeedBackFormActivity.this);
        mSpinner_Feedback.setAdapter(adapter);
        mSpinner_Feedback.setDropDownVerticalOffset(120);
    }
}
