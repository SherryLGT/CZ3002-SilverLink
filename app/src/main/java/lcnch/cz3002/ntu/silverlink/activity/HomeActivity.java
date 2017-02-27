package lcnch.cz3002.ntu.silverlink.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import lcnch.cz3002.ntu.silverlink.R;

/**
 *
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 20/02/2017
 */

public class HomeActivity extends AppCompatActivity {

    private ImageButton ibFriend, ibGroup, ibProfile, ibSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ibFriend = (ImageButton) findViewById(R.id.ib_friend);
        ibGroup = (ImageButton) findViewById(R.id.ib_group);
        ibProfile = (ImageButton) findViewById(R.id.ib_profile);
        ibSetting = (ImageButton) findViewById(R.id.ib_setting);

        ibFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FriendActivity.class);
                startActivity(intent);
            }
        });

        ibGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ibProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        ibSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // For testing purpose (remove when not needed)
//        Intent intent = new Intent(getApplicationContext(), FriendActivity.class);
//        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        AlertDialog dialog = new AlertDialog.Builder(this).setMessage("Are you sure you want to quit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("No", null).show();
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTextSize(30);
    }
}
