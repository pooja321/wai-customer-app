package customer.thewaiapp.com;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import customer.thewaiapp.com.BaseActivity;
import customer.thewaiapp.com.R;

public class TermsAndConditionsActivity extends BaseActivity {
    TextView mTextviewTermsandConditions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        setTitle("Terms and Conditions");
        mTextviewTermsandConditions= (TextView) findViewById(R.id.textview_termsandconditions);
        mTextviewTermsandConditions.setMovementMethod(new ScrollingMovementMethod());
    }
}
