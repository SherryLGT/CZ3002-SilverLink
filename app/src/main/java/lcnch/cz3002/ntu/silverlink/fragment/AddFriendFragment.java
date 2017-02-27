package lcnch.cz3002.ntu.silverlink.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Random;

import lcnch.cz3002.ntu.silverlink.R;
import lcnch.cz3002.ntu.silverlink.controller.GsonHelper;
import lcnch.cz3002.ntu.silverlink.controller.Utility;
import lcnch.cz3002.ntu.silverlink.model.ApplicationUser;
import lcnch.cz3002.ntu.silverlink.model.Friend;

import static lcnch.cz3002.ntu.silverlink.activity.SplashActivity.loggedInUser;
import static lcnch.cz3002.ntu.silverlink.fragment.FriendFragment.friendList;

/**
 *
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 27/02/2017
 */

public class AddFriendFragment extends Fragment {

    private View rootView;
    private EditText etPhoneNo;
    private ImageView ivProfilePic;
    private TextView tvName, tvNotFound;
    private Button btnAddFriend;

    private ProgressDialog dialog;
    private String phoneNo, response;
    private ApplicationUser user;
    private Gson gson = GsonHelper.customGson;
    /**
     * Default constructor for AddFriendFragment
     */
    public AddFriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_add_friend, container, false);

        etPhoneNo = (EditText) rootView.findViewById(R.id.et_phone_no);
        ivProfilePic = (ImageView) rootView.findViewById(R.id.iv_profile_pic);
        tvName = (TextView) rootView.findViewById(R.id.tv_name);
        tvNotFound = (TextView) rootView.findViewById(R.id.tv_not_found);
        btnAddFriend = (Button) rootView.findViewById(R.id.btn_add_friend);

        dialog = Utility.SetupLoadingDialog(getContext(), dialog);

        etPhoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPhoneNo.selectAll();
            }
        });

        etPhoneNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(etPhoneNo.getText().toString().length() == 8) {
                    phoneNo = etPhoneNo.getText().toString();
                    new findFriend().execute();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
                }
            }
        });

        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(getContext()).setMessage("Confirm add?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new addFriend().execute();
                            }
                        }).setNegativeButton("No", null).show();
                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setTextSize(30);
            }
        });

        return rootView;
    }

    private class findFriend extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(user != null) {
                if (user.getProfilePicture() != null && user.getProfilePicture().length > 0) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(user.getProfilePicture(), 0, user.getProfilePicture().length);
                    ivProfilePic.setImageBitmap(Bitmap.createScaledBitmap(bitmap, ivProfilePic.getWidth(), ivProfilePic.getHeight(), false));
                } else {
                    if (new Random().nextBoolean())
                        ivProfilePic.setImageResource(R.drawable.default_guy);
                    else
                        ivProfilePic.setImageResource(R.drawable.default_girl);
                }
                tvName.setText(user.getFullName());

                btnAddFriend.setVisibility(View.VISIBLE);
                tvNotFound.setVisibility(View.GONE);
                for(Friend friend : friendList) {
                   if(friend.equals(friendList)) {
                       btnAddFriend.setVisibility(View.GONE);
                       break;
                   }
                }
                if(loggedInUser.getId().equals(user.getId())) {
                    btnAddFriend.setVisibility(View.GONE);
                }
            }
            else {
                ivProfilePic.setImageBitmap(null);
                tvName.setText("");
                btnAddFriend.setVisibility(View.GONE);
                tvNotFound.setVisibility(View.VISIBLE);
            }

            dialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            response = Utility.getRequest("api/Users/" + phoneNo);
            user = gson.fromJson(response, ApplicationUser.class);

            return null;
        }
    }

    private class addFriend extends AsyncTask<Void, Void, Void> {
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
            Utility.postRequest("api/Friends/" + user.getId() + "/Add", "");

            return null;
        }
    }
}
