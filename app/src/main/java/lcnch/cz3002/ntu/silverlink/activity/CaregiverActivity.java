package lcnch.cz3002.ntu.silverlink.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import lcnch.cz3002.ntu.silverlink.R;
import lcnch.cz3002.ntu.silverlink.controller.Utility;
import lcnch.cz3002.ntu.silverlink.model.ApplicationUser;
import lcnch.cz3002.ntu.silverlink.model.Location;

import static java.security.AccessController.getContext;

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

    private class GetCareLocation extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(CaregiverActivity.this, "Location Updated", Toast.LENGTH_LONG).show();
            LatLng loc = new LatLng(silverUser.getLocation().getLatitude(), silverUser.getLocation().getLongitude());
            map.addMarker(new MarkerOptions().position(loc)
                    .title(silverUser.getFullName() + " @ " + silverUser.getLocation().getAcquiredAt()));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,18));

        }

        @Override
        protected Void doInBackground(Void... params) {
            silverUser = Utility.customGson.fromJson(Utility.getRequest("api/User/Care"),ApplicationUser.SilverUser.class);
            return null;
        }
    }



}
