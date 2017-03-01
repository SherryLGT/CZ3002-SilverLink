package lcnch.cz3002.ntu.silverlink.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import lcnch.cz3002.ntu.silverlink.R;
import lcnch.cz3002.ntu.silverlink.controller.Utility;
import lcnch.cz3002.ntu.silverlink.model.ApplicationUser;

import static lcnch.cz3002.ntu.silverlink.activity.SplashActivity.loggedInUser;

public class ProfileActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    private Gson gson = Utility.customGson;
    ImageView ivProfilePic;
    EditText etName;
    EditText etPwd;
    EditText etPwd2;
    Button btnUpdateProfile;
    private JsonObject jsonObject;
    Bitmap profileBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ivProfilePic = (ImageView) findViewById(R.id.iv_profile_pic);
        etName = (EditText) findViewById(R.id.et_name);
        etPwd = (EditText) findViewById(R.id.et_pwd);
        etPwd2 = (EditText) findViewById(R.id.et_pwd2);
        btnUpdateProfile = (Button) findViewById(R.id.btn_update_profile);
        ivProfilePic.setImageResource(R.drawable.default_guy);
        dialog = Utility.SetupLoadingDialog(this, dialog);

        new GetProfile().execute();

        ivProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
// Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SetProfile().execute();

                if (!etPwd.getText().toString().equals("") && !etPwd2.getText().toString().equals("")) {
                    jsonObject = new JsonObject();
                    jsonObject.addProperty("oldPassword", etPwd.getText().toString());
                    jsonObject.addProperty("newPassword", etPwd2.getText().toString());
                    new UpdatePassword().execute();
                }

                new GetProfile().execute();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri imgUri = data.getData();

            try {
                profileBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                // Log.d(TAG, String.valueOf(bitmap));
                int size = profileBitmap.getWidth();
                if (size > profileBitmap.getHeight())
                    size = profileBitmap.getHeight();
                profileBitmap = Bitmap.createBitmap(profileBitmap, (profileBitmap.getWidth() - size) / 2, (profileBitmap.getHeight() - size) / 2, size, size);
                profileBitmap = Bitmap.createScaledBitmap(profileBitmap, 128, 128, false);
                ivProfilePic.setImageBitmap(profileBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private class GetProfile extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            dialog.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (loggedInUser.getProfilePicture() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(loggedInUser.getProfilePicture(), 0, loggedInUser.getProfilePicture().length);
                ivProfilePic.setImageBitmap(bitmap);
            }
            etName.setText(loggedInUser.getFullName());
            dialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String response = Utility.getRequest("api/Account/UserInfo");
            loggedInUser = gson.fromJson(response, ApplicationUser.class);

            return null;
        }
    }

    private class SetProfile extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            dialog.show();

            if (profileBitmap != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                profileBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                byte[] byteArray = stream.toByteArray();
                loggedInUser.setProfilePicture(byteArray);
            }
            loggedInUser.setFullName(etName.getText().toString());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            Toast.makeText(ProfileActivity.this, "Profile Updated!", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String response = Utility.postRequest("api/User", gson.toJson(loggedInUser));
            return null;
        }
    }


    private class UpdatePassword extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            Toast.makeText(ProfileActivity.this, "Password Updated!", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String response = Utility.postRequest("api/Account/ChangePassword", jsonObject.toString());
            return null;
        }
    }
}
