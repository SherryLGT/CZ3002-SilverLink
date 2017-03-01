package lcnch.cz3002.ntu.silverlink.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import lcnch.cz3002.ntu.silverlink.R;
import lcnch.cz3002.ntu.silverlink.fragment.FriendFragment;
import lcnch.cz3002.ntu.silverlink.fragment.MessageFragment;

public class GroupMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_message);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new MessageFragment(), "MessageFragment").commit();
    }
}
