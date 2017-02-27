package lcnch.cz3002.ntu.silverlink.fragment;


import android.app.ProgressDialog;
import android.os.AsyncTask;
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

import com.google.gson.JsonObject;

import lcnch.cz3002.ntu.silverlink.R;
import lcnch.cz3002.ntu.silverlink.controller.Utility;
import lcnch.cz3002.ntu.silverlink.model.UserRole;


/**
 *
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 17/02/2017
 */

public class SignupFragment extends Fragment {

    private View rootView;
    private EditText etPhoneNo, etName, etPwd, etPwd2;
    private CheckBox cbRole;
    private Button btnCancel, btnSignup;

    private ProgressDialog dialog;
    private JsonObject jsonObject;
    private String response;

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

        etPhoneNo = (EditText) rootView.findViewById(R.id.et_phone_no);
        etName = (EditText) rootView.findViewById(R.id.et_name);
        etPwd = (EditText) rootView.findViewById(R.id.et_pwd);
        etPwd2 = (EditText) rootView.findViewById(R.id.et_pwd2);
        cbRole = (CheckBox) rootView.findViewById(R.id.cb_role);
        btnCancel = (Button) rootView.findViewById(R.id.btn_cancel);
        btnSignup = (Button) rootView.findViewById(R.id.btn_signup);

        dialog = Utility.SetupLoadingDialog(getContext(), dialog);

        etPwd2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch(keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            btnSignup.performClick();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPhoneNo.getText().toString().length() == 8 && !etName.getText().toString().isEmpty() && !etPwd.getText().toString().isEmpty() && etPwd.getText().toString().length() >= 6 && !etPwd2.getText().toString().isEmpty() && etPwd2.getText().toString().length() >= 6 && etPwd.getText().toString().equals(etPwd2.getText().toString())) {
                    etPhoneNo.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border_text_box));
                    etName.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border_text_box));
                    etPwd.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border_text_box));
                    etPwd2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border_text_box));

                    jsonObject = new JsonObject();
                    jsonObject.addProperty("phoneNumber", etPhoneNo.getText().toString());
                    jsonObject.addProperty("fullName", etName.getText().toString());
                    jsonObject.addProperty("password", etPwd.getText().toString());
                    if(!cbRole.isChecked()) {
                        jsonObject.addProperty("role", UserRole.SILVER.getValue());
                    }
                    else {
                        jsonObject.addProperty("role", UserRole.CARER.getValue());
                    }
                    new signUp().execute();
                }
                if(etPhoneNo.getText().toString().length() < 8) {
                    etPhoneNo.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.red_border_text_box));
                }
                if(etName.getText().toString().isEmpty()) {
                    etName.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.red_border_text_box));
                }
                if(etPwd.getText().toString().isEmpty() || etPwd.getText().toString().length() < 6) {
                    etPwd.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.red_border_text_box));
                }
                if(etPwd2.getText().toString().isEmpty() || etPwd2.getText().toString().length() < 6) {
                    etPwd2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.red_border_text_box));
                }
                if(!etPwd.getText().toString().equals(etPwd2.getText().toString())) {
                    etPwd.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.red_border_text_box));
                    etPwd2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.red_border_text_box));
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new LoginFragment()).commit();
            }
        });

        return rootView;
    }

    private class signUp extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            response = Utility.postRequest("api/Account/Register", jsonObject.toString());

            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new LoginFragment()).commit();

            return null;
        }
    }
}
