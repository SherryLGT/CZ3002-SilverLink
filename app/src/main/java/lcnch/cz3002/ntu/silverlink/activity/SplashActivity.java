package lcnch.cz3002.ntu.silverlink.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import lcnch.cz3002.ntu.silverlink.R;
import lcnch.cz3002.ntu.silverlink.controller.Utility;
import lcnch.cz3002.ntu.silverlink.model.ApplicationUser;

/**
 *
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 17/02/2017
 */

public class SplashActivity extends AppCompatActivity {

    public static SharedPreferences sharedPreferences;
    private String response;
    public static ApplicationUser loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        Utility.accessToken = sharedPreferences.getString("accesstoken", "");
        new getUserInfo().execute();
    }

    private class getUserInfo extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            loggedInUser = new ApplicationUser();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(loggedInUser != null) {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startService(new Intent(this, LocationService.class));
                startActivity(intent);
                finish();
                Toast.makeText(SplashActivity.this, "Welcome back", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            response = Utility.getRequest("api/Account/UserInfo");
            if(response != null) {
                loggedInUser = Utility.customGson.fromJson(response, ApplicationUser.class);
            }
            else {
                Utility.accessToken = "";
            }

            return null;
        }
    }
}
