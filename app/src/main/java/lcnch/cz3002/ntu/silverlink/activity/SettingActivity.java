package lcnch.cz3002.ntu.silverlink.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lcnch.cz3002.ntu.silverlink.R;
import lcnch.cz3002.ntu.silverlink.adapter.UserAdapter;
import lcnch.cz3002.ntu.silverlink.controller.Utility;
import lcnch.cz3002.ntu.silverlink.model.ApplicationUser;
import lcnch.cz3002.ntu.silverlink.model.UserItem;
import lcnch.cz3002.ntu.silverlink.model.UserRole;

import static lcnch.cz3002.ntu.silverlink.activity.SplashActivity.loggedInUser;

public class SettingActivity extends AppCompatActivity {

    private ListView lvCarerList;
    private RelativeLayout rlAddCarer;
    private EditText etPhoneNo;
    private ImageView ivProfilePic;
    private TextView tvName, tvNotFound;
    private Button btnAdd, btnAddCarer, btnCancel;

    private ProgressDialog dialog;
    private String response;
    private ArrayList<ApplicationUser> carerList;
    private ArrayList<UserItem> userItems;
    private ApplicationUser user;
    private String phoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        lvCarerList = (ListView) findViewById(R.id.lv_carer_list);
        rlAddCarer = (RelativeLayout) findViewById(R.id.rl_add_carer);
        etPhoneNo = (EditText) findViewById(R.id.et_phone_no);
        ivProfilePic = (ImageView) findViewById(R.id.iv_profile_pic);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvNotFound = (TextView) findViewById(R.id.tv_not_found);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnAddCarer = (Button) findViewById(R.id.btn_add_carer);
        btnCancel = (Button) findViewById(R.id.btn_cancel);

        dialog = Utility.SetupLoadingDialog(this, dialog);
        new loadCarers().execute();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPhoneNo.setText("");
                ivProfilePic.setImageBitmap(null);
                tvName.setText("");
                tvNotFound.setVisibility(View.GONE);
                btnAddCarer.setVisibility(View.GONE);
                rlAddCarer.setVisibility(View.GONE);
                btnAdd.setVisibility(View.VISIBLE);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlAddCarer.setVisibility(View.VISIBLE);
                btnAdd.setVisibility(View.GONE);
            }
        });

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
                    new findCarer().execute();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }
            }
        });

        btnAddCarer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(SettingActivity.this).setMessage("Confirm add?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new addCarer().execute();
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                        }).setNegativeButton("No", null).show();
                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setTextSize(30);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_setting, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                AlertDialog dialog = new AlertDialog.Builder(SettingActivity.this).setMessage("Confirm log out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utility.accessToken = "";
                                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("No", null).show();
                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setTextSize(30);
        }
        return super.onOptionsItemSelected(item);
    }

    private class loadCarers extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            carerList = new ArrayList<ApplicationUser>();
            userItems = new ArrayList<UserItem>();
            user = new ApplicationUser();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            lvCarerList.setAdapter(new UserAdapter(getApplicationContext(), userItems));
        }

        @Override
        protected Void doInBackground(Void... params) {
            response = Utility.getRequest("api/User/Carers");
            carerList = Utility.customGson.fromJson(response, new TypeToken<List<ApplicationUser>>() {}.getType());
            for(ApplicationUser carer : carerList) {
                userItems.add(new UserItem(carer.getProfilePicture(), carer.getFullName()));
            }

            return null;
        }
    }

    private class findCarer extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(user != null && user.getRole() == UserRole.CARER) {
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

                btnAddCarer.setVisibility(View.VISIBLE);
                tvNotFound.setVisibility(View.GONE);
                for (int i = 0; i < carerList.size(); i++) {
                    if (user.getId().equals(carerList.get(i).getId())) {
                        btnAddCarer.setVisibility(View.GONE);
                        break;
                    }
                }
                if (loggedInUser.getId().equals(user.getId())) {
                    btnAddCarer.setVisibility(View.GONE);
                }
            }
            else {
                ivProfilePic.setImageBitmap(null);
                tvName.setText("");
                btnAddCarer.setVisibility(View.GONE);
                tvNotFound.setVisibility(View.VISIBLE);
            }

            dialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            response = Utility.getRequest("api/Users/" + phoneNo);
            user = Utility.customGson.fromJson(response, ApplicationUser.class);

            return null;
        }
    }

    private class addCarer extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Utility.postRequest("api/User/Carers/" + user.getId() + "/Add", "");

            return null;
        }
    }
}
