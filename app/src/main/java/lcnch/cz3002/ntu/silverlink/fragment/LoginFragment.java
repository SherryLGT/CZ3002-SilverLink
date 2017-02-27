package lcnch.cz3002.ntu.silverlink.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lcnch.cz3002.ntu.silverlink.R;
import lcnch.cz3002.ntu.silverlink.activity.HomeActivity;
import lcnch.cz3002.ntu.silverlink.controller.Utility;
import lcnch.cz3002.ntu.silverlink.model.ApplicationUser;

import static lcnch.cz3002.ntu.silverlink.activity.SplashActivity.loggedInUser;
import static lcnch.cz3002.ntu.silverlink.activity.SplashActivity.sharedPreferences;

/**
 *
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 17/02/2017
 */

public class LoginFragment extends Fragment {

    private View rootView;
    private EditText etPhoneNo, etPwd;
    private Button btnLogin;
    private TextView tvErrorMsg, tvSignUp, tvForgetPwd;

    private Gson gson;
    private ProgressDialog dialog;
    private String phoneNo, pwd, response;

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

        etPhoneNo = (EditText) rootView.findViewById(R.id.et_phone_no);
        etPwd = (EditText) rootView.findViewById(R.id.et_pwd);
        btnLogin = (Button) rootView.findViewById(R.id.btn_login);
        tvErrorMsg = (TextView) rootView.findViewById(R.id.tv_error_msg);
        tvSignUp = (TextView) rootView.findViewById(R.id.tv_signup);
        tvForgetPwd = (TextView) rootView.findViewById(R.id.tv_forget_pwd);

        dialog = Utility.SetupLoadingDialog(getContext(), dialog);
        tvErrorMsg.setText(this.getResources().getString(R.string.invalid_login));

        etPwd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch(keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            btnLogin.performClick();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPhoneNo.getText().length() == 8 && !etPhoneNo.getText().toString().isEmpty()) {
                    tvErrorMsg.setVisibility(View.GONE);
                    etPhoneNo.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border_text_box));
                    etPwd.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border_text_box));

                    phoneNo = etPhoneNo.getText().toString();
                    pwd = etPwd.getText().toString();
                    new getToken().execute();
                }
                if(etPhoneNo.getText().length() <  8) {
                    tvErrorMsg.setVisibility(View.VISIBLE);
                    etPhoneNo.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.red_border_text_box));
                }
                if(etPwd.getText().length() == 0) {
                    tvErrorMsg.setVisibility(View.VISIBLE);
                    etPwd.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.red_border_text_box));
                }
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new SignupFragment()).commit();
            }
        });

        tvForgetPwd.setOnClickListener(new View.OnClickListener() {
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
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();

            JsonObject obj = new JsonParser().parse(response).getAsJsonObject();
            if(obj.get("access_token") != null) {
                Utility.accessToken = obj.get("access_token").getAsString();
                SharedPreferences.Editor editor = sharedPreferences.edit().putString("accesstoken", Utility.accessToken);
                editor.commit();

                new getUserInfo().execute();
                Intent intent = new Intent(getContext(), HomeActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
            else {
                tvErrorMsg.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            response = Utility.postRequest("Token", "grant_type=password&username=" + phoneNo + "&password=" + pwd);

            return null;
        }
    }

    private class getUserInfo extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            gson = new Gson();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            response = Utility.getRequest("api/Account/UserInfo");
            loggedInUser = gson.fromJson(response, ApplicationUser.class);

            return null;
        }
    }
}
