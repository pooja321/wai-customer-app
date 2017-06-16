package customer.thewaiapp.com.Login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import customer.thewaiapp.com.R;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnSignUpButtonClickedInterface,
        SignUpFragment.OnSignInButtonClickedInterface {

    public static final String LOGIN_FRAGMENT = "login_fragment";
    public static final String SIGNUP_FRAGMENT = "SignUp_fragment";
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginFragment savedLoginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag(LOGIN_FRAGMENT);
        if (savedLoginFragment == null) {
            LoginFragment loginFragment = new LoginFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.login_frame_placeholder, loginFragment, LOGIN_FRAGMENT);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
    }
    @Override
    public void onSignUpFragmentSelected(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.login_frame_placeholder, fragment, SIGNUP_FRAGMENT);
        fragmentTransaction.commit();
    }
    @Override
    public void onSignInFragmentSelected(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.login_frame_placeholder, fragment, LOGIN_FRAGMENT);
        fragmentTransaction.commit();
    }
}
