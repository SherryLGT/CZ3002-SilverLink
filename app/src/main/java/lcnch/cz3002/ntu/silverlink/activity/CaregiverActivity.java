package lcnch.cz3002.ntu.silverlink.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import lcnch.cz3002.ntu.silverlink.R;
import lcnch.cz3002.ntu.silverlink.controller.Utility;
import lcnch.cz3002.ntu.silverlink.model.ApplicationUser;
import lcnch.cz3002.ntu.silverlink.model.Location;

import static java.security.AccessController.getContext;
import static lcnch.cz3002.ntu.silverlink.activity.SplashActivity.sharedPreferences;

public class CaregiverActivity extends AppCompatActivity implements OnMapReadyCallback {

    ApplicationUser.SilverUser silverUser;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        new GetCareLocation().execute();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_caregiver, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                new GetCareLocation().execute();
                break;
            case R.id.action_profile:
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.action_logout:
                AlertDialog dialog = new AlertDialog.Builder(CaregiverActivity.this).setMessage("Confirm log out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utility.accessToken = "";
                                SharedPreferences.Editor editor = sharedPreferences.edit().putString("accesstoken", Utility.accessToken);
                                editor.commit();
                                Intent intent = new Intent(CaregiverActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finishAffinity();
                            }
                        }).setNegativeButton("No", null).show();
                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setTextSize(30);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetCareLocation extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(CaregiverActivity.this, "Location Updated", Toast.LENGTH_LONG).show();
            LatLng loc = new LatLng(silverUser.getLocation().getLatitude(), silverUser.getLocation().getLongitude());
            map.clear();
            Marker marker = map.addMarker(new MarkerOptions().position(loc)
                    .title(silverUser.getFullName()).snippet(silverUser.getLocation().getAcquiredAt().toString()));
            marker.showInfoWindow();
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 18));

        }

        @Override
        protected Void doInBackground(Void... params) {
            silverUser = Utility.customGson.fromJson(Utility.getRequest("api/User/Care"), ApplicationUser.SilverUser.class);
            return null;
        }
    }


}
