package lcnch.cz3002.ntu.silverlink.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import lcnch.cz3002.ntu.silverlink.R;
import lcnch.cz3002.ntu.silverlink.adapter.UserAdapter;
import lcnch.cz3002.ntu.silverlink.controller.Utility;
import lcnch.cz3002.ntu.silverlink.model.FCMType;
import lcnch.cz3002.ntu.silverlink.model.Friend;
import lcnch.cz3002.ntu.silverlink.model.UserItem;

/**
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 27/02/2017
 */

public class FriendFragment extends Fragment {

    private View rootView;
    private Spinner spnSort;
    private ListView lvFriendList;
    private Button btnAddFriend;

    private boolean recent;
    private String response;
    public static ArrayList<Friend> friendList;
    private ArrayList<UserItem> userItems;
    public static Friend friend;

    /**
     * Default constructor for FriendFragment
     */
    public FriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_friend, container, false);

        spnSort = (Spinner) rootView.findViewById(R.id.spn_sort);
        lvFriendList = (ListView) rootView.findViewById(R.id.lv_friend_list);
        btnAddFriend = (Button) rootView.findViewById(R.id.btn_add_friend);

        spnSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // alphabetical
                        recent = false;
                        new GetFriends().execute();
                        break;
                    case 1: // recent messages
                        recent = true;
                        new GetFriends().execute();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lvFriendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                friend = friendList.get(position);
                MessageFragment.messageType = FCMType.FRIEND_MESSAGE;
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new MessageFragment(), "MessageFragment").commit();
            }
        });

        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new AddFriendFragment(), "AddFriendFragment").commit();
            }
        });

        return rootView;
    }

    private class GetFriends extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            friendList = new ArrayList<Friend>();
            userItems = new ArrayList<UserItem>();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            lvFriendList.setAdapter(new UserAdapter(getContext(), userItems));
        }

        @Override
        protected Void doInBackground(Void... params) {
            response = Utility.getRequest("api/Friends?recent=" + recent); // true - recent messages, false - alphabetical
            friendList = Utility.customGson.fromJson(response, new TypeToken<List<Friend>>() {
            }.getType());
            for (Friend friend : friendList) {
                userItems.add(new UserItem(friend.getUser().getProfilePicture(), friend.getUser().getFullName()));
            }

            return null;
        }
    }
}
