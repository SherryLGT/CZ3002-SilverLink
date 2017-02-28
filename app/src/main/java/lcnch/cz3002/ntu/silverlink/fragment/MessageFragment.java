package lcnch.cz3002.ntu.silverlink.fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lcnch.cz3002.ntu.silverlink.R;
import lcnch.cz3002.ntu.silverlink.adapter.MessageAdapter;
import lcnch.cz3002.ntu.silverlink.controller.Utility;
import lcnch.cz3002.ntu.silverlink.model.Message;
import lcnch.cz3002.ntu.silverlink.model.MessageItem;

import static lcnch.cz3002.ntu.silverlink.fragment.FriendFragment.friend;

/**
 *
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 28/02/2017
 */

public class MessageFragment extends Fragment {

    private View rootView;
    private ImageView ivProfilePic;
    private TextView tvName;
    private ListView lvMessages;

    private Bitmap bitmap;
    private String response;
    private ArrayList<Message> messageList;
    private ArrayList<MessageItem> messageItems;

    /**
     * Default constructor for FriendFragment
     */
    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_message, container, false);

        ivProfilePic = (ImageView) rootView.findViewById(R.id.iv_profile_pic);
        tvName = (TextView) rootView.findViewById(R.id.tv_name);
        lvMessages = (ListView) rootView.findViewById(R.id.lv_messages);

        if(friend.getUser().getProfilePicture() != null) {
            bitmap = BitmapFactory.decodeByteArray(friend.getUser().getProfilePicture(), 0, friend.getUser().getProfilePicture().length);
            ivProfilePic.setImageBitmap(bitmap);
        }
        else {
            if(new Random().nextBoolean())
                ivProfilePic.setImageResource(R.drawable.default_guy);
            else
                ivProfilePic.setImageResource(R.drawable.default_girl);
        }

        tvName.setText(friend.getUser().getFullName());

        new getMessages().execute();
        lvMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return rootView;
    }

    private class getMessages extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            messageList = new ArrayList<Message>();
            messageItems = new ArrayList<MessageItem>();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            lvMessages.setAdapter(new MessageAdapter(getContext(), messageItems));
        }

        @Override
        protected Void doInBackground(Void... params) {
            response = Utility.getRequest("api/Friends/" + friend.getId() + "/Messages/1970-01-01");
            messageList = Utility.customGson.fromJson(response, new TypeToken<List<Message>>() {}.getType());
            for(Message message : messageList) {
                messageItems.add(new MessageItem(message.getSentAt(), message.getSentBy().getProfilePicture(), message.getSentBy().getFullName()));
            }

            return null;
        }
    }
}
