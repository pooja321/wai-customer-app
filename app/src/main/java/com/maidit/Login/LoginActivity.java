package com.maidit.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.maidit.MainActivity;
import com.maidit.R;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnSignUpButtonClickedInterface {

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
            fragmentTransaction.add(R.id.placeHolder, loginFragment, LOGIN_FRAGMENT);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onSignUpFragmentSelected(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.placeHolder, fragment, SIGNUP_FRAGMENT);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void onSkipLoginButtonClick(View view){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
