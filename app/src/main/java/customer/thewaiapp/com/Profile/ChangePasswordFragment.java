package customer.thewaiapp.com.Profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import customer.thewaiapp.com.R;


public class ChangePasswordFragment extends Fragment {
    EditText mEditTextNewPassword,mEditTextConfirmPassword;
    Button mSubmit;
    String mNewPassword,mConfirmPassword;
    public static ChangePasswordFragment newInstance() {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_change_password, container, false);
        getActivity().setTitle("Change Password");
        mEditTextNewPassword = (EditText) view.findViewById(R.id.fragment_changepassword_new);
        mEditTextConfirmPassword= (EditText) view.findViewById(R.id.fragment_changepassword_confirm);
        mSubmit= (Button) view.findViewById(R.id.button_changepassword_submit);
        mNewPassword = mEditTextNewPassword.getText().toString();
        mConfirmPassword=mEditTextConfirmPassword.getText().toString();
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNewPassword.equals(mConfirmPassword))
                {
                    Toast.makeText(getActivity(), "hello", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return  view;
    }


}
