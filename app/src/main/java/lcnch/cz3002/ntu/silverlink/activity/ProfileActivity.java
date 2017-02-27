package lcnch.cz3002.ntu.silverlink.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

import lcnch.cz3002.ntu.silverlink.R;
import lcnch.cz3002.ntu.silverlink.controller.GsonHelper;
import lcnch.cz3002.ntu.silverlink.controller.Utility;
import lcnch.cz3002.ntu.silverlink.model.ApplicationUser;

public class ProfileActivity extends AppCompatActivity {

    private ApplicationUser userProfile;
    private Gson gson = GsonHelper.customGson;
    ImageView ivProfilePic;
    EditText etName;
    EditText etPwd;
    EditText etPwd2;
    Button btnUpdateProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ivProfilePic = (ImageView) findViewById(R.id.iv_profile_pic);
        etName = (EditText) findViewById(R.id.et_name);
        etPwd = (EditText) findViewById(R.id.et_pwd);
        etPwd2 = (EditText) findViewById(R.id.et_pwd2);
        btnUpdateProfile = (Button) findViewById(R.id.btn_update_profile);


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
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri imgUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                // Log.d(TAG, String.valueOf(bitmap));

                ivProfilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private class GetProfile extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (userProfile.getProfilePicture() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(userProfile.getProfilePicture(), 0, userProfile.getProfilePicture().length);
                ivProfilePic.setImageBitmap(bitmap);
            }
            etName.setText(userProfile.getFullName());

        }

        @Override
        protected Void doInBackground(Void... params) {
            String response = Utility.getRequest("api/Account/UserInfo");
            userProfile = gson.fromJson(response, ApplicationUser.class);

            return null;
        }
    }

    private class SetProfile extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            ivProfilePic.setDrawingCacheEnabled(true);
            ivProfilePic.buildDrawingCache();
            Bitmap bm = ivProfilePic.getDrawingCache();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            byte[] byteArray = stream.toByteArray();
            userProfile.setProfilePicture(byteArray);

            userProfile.setFullName(etName.getText().toString());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            if (userProfile.getProfilePicture() != null) {
//                Bitmap bitmap = BitmapFactory.decodeByteArray(userProfile.getProfilePicture(), 0, userProfile.getProfilePicture().length);
//                ivProfilePic.setImageBitmap(bitmap);
//            }
            etName.setText(userProfile.getFullName());

        }

        @Override
        protected Void doInBackground(Void... params) {
            String response = Utility.postRequest("api/User", gson.toJson(userProfile));
            return null;
        }
    }
}
