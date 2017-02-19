package lcnch.cz3002.ntu.silverlink;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import controller.Utility;
import model.ApplicationUser;
import model.LoginParameters;

/**
 *
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 17/02/2017
 */
public class LoginFragment extends Fragment {

    private View rootView;
    private EditText et_phone_no, et_pwd;
    private Button btn_login;
    private TextView tv_error_msg, tv_signup, tv_forget_pwd;

    private ProgressDialog dialog;
    private Gson gson;
    private LoginParameters parameters;
    private String response;
    private ApplicationUser user;

    /**
     * Default constructor for LoginFragment
     */
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_login, container, false);

        et_phone_no = (EditText) rootView.findViewById(R.id.et_phone_no);
        et_pwd = (EditText) rootView.findViewById(R.id.et_pwd);
        btn_login = (Button) rootView.findViewById(R.id.btn_login);
        tv_error_msg = (TextView) rootView.findViewById(R.id.tv_error_msg);
        tv_signup = (TextView) rootView.findViewById(R.id.tv_signup);
        tv_forget_pwd = (TextView) rootView.findViewById(R.id.tv_forget_pwd);

        tv_error_msg.setText(this.getString(R.string.invalid_login));

        et_pwd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch(keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            btn_login.performClick();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_phone_no.getText().length() == 8 && et_phone_no.getText().length() > 0) {
                    tv_error_msg.setVisibility(View.GONE);
                    et_phone_no.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border_text_box));
                    et_pwd.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border_text_box));

                    dialog = new ProgressDialog(getContext());
                    dialog.setMessage("Loading...");
                    dialog.setCancelable(false);

                    parameters = new LoginParameters(et_phone_no.getText().toString(), et_pwd.getText().toString());
                    new getToken().execute();
                }
                if(et_phone_no.getText().length() <  8) {
                    tv_error_msg.setVisibility(View.VISIBLE);
                    et_phone_no.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.redborder_text_box));
                }
                if(et_pwd.getText().length() == 0) {
                    tv_error_msg.setVisibility(View.VISIBLE);
                    et_pwd.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.redborder_text_box));
                }
            }
        });

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new SignupFragment()).commit();
            }
        });

        tv_forget_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return rootView;
    }

    private class getToken extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            dialog.show();
            gson = new Gson();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            response = Utility.postRequest("Token", parameters.getGrantType() + "&username=" + parameters.getPhoneNumber() + "&password=" + parameters.getPassword());
            return null;
        }
    }
}
