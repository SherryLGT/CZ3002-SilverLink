package lcnch.cz3002.ntu.silverlink.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import lcnch.cz3002.ntu.silverlink.R;
import lcnch.cz3002.ntu.silverlink.model.ApplicationUser;


/**
 *
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 17/02/2017
 */

public class SignupFragment extends Fragment {

    private View rootView;
    private EditText et_name, et_pwd, et_pwd2;
    private CheckBox cb_role;
    private Button btn_cancel, btn_signup;

    private ApplicationUser user;

    /**
     * Default constructor for SignupFragment
     */
    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_signup, container, false);

        et_name = (EditText) rootView.findViewById(R.id.et_name);
        et_pwd = (EditText) rootView.findViewById(R.id.et_pwd);
        et_pwd2 = (EditText) rootView.findViewById(R.id.et_pwd2);
        cb_role = (CheckBox) rootView.findViewById(R.id.cb_role);
        btn_cancel = (Button) rootView.findViewById(R.id.btn_cancel);
        btn_signup = (Button) rootView.findViewById(R.id.btn_signup);

        et_pwd2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch(keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            btn_signup.performClick();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!et_pwd.getText().toString().equals(et_pwd2.getText().toString())) {
                    et_pwd.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border_text_box));
                    et_pwd2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border_text_box));
                }
                user = new ApplicationUser();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new LoginFragment()).commit();
            }
        });

        return rootView;
    }

}
