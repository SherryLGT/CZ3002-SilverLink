package lcnch.cz3002.ntu.silverlink.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import lcnch.cz3002.ntu.silverlink.R;
import lcnch.cz3002.ntu.silverlink.adapter.FriendAdapter;
import lcnch.cz3002.ntu.silverlink.controller.Utility;
import lcnch.cz3002.ntu.silverlink.model.Friend;
import lcnch.cz3002.ntu.silverlink.model.FriendItem;

public class FriendActivity extends AppCompatActivity {

    private TextView tvTitle;
    private FrameLayout flContainer;
    private ListView lvFriendList;
    private Button btnAddFriend;

    private Gson gson;
    private String response;
    private ArrayList<Friend> friendList;
    private ArrayList<FriendItem> friendItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        flContainer = (FrameLayout) findViewById(R.id.fl_container);

        tvTitle.setText(getResources().getText(R.string.title_friend_list));

        new GetFriends().execute();
    }

    private class GetFriends extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
            friendList = new ArrayList<Friend>();
            friendItems = new ArrayList<FriendItem>();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            lvFriendList = new ListView(getApplicationContext());
            lvFriendList.setAdapter(new FriendAdapter(getApplicationContext(), friendItems));
            flContainer.addView(lvFriendList);

            btnAddFriend = new Button(getApplicationContext());
            btnAddFriend.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | Gravity.END));
            btnAddFriend.setBackground(getResources().getDrawable(R.drawable.round_button));
            btnAddFriend.setText(getResources().getString(R.string.btn_add_friend));
            flContainer.addView(btnAddFriend);

//            android:background="@drawable/round_button"
//            android:textAllCaps="true"
//            android:textColor="@color/blue"
//            android:textSize="@dimen/sml_font_size"
        }

        @Override
        protected Void doInBackground(Void... params) {
            response = Utility.getRequest("api/Friends");
            friendList = gson.fromJson(response, new TypeToken<List<Friend>>() {}.getType());
            for(Friend friend : friendList) {
                friendItems.add(new FriendItem(friend.getUser().getProfilePicture(), friend.getUser().getFullName()));
            }

            return null;
        }
    }
}
