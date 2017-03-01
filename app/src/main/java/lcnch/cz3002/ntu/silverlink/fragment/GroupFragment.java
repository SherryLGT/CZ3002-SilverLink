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
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import lcnch.cz3002.ntu.silverlink.R;
import lcnch.cz3002.ntu.silverlink.adapter.GroupAdapter;
import lcnch.cz3002.ntu.silverlink.adapter.UserAdapter;
import lcnch.cz3002.ntu.silverlink.controller.Utility;
import lcnch.cz3002.ntu.silverlink.model.Friend;
import lcnch.cz3002.ntu.silverlink.model.Group;
import lcnch.cz3002.ntu.silverlink.model.UserItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFragment extends Fragment {

    private View rootView;
    private ListView lvGroupList;

    private ArrayList<Group> groupList;
    private ArrayList<Group> favouriteGroupList;
    private int selectedGroup;

    /**
     * Default constructor for FriendFragment
     */
    public GroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_group, container, false);

        lvGroupList = (ListView) rootView.findViewById(R.id.lv_group_list);

        new GetGroups().execute();

        lvGroupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedGroup = groupList.get(position).getId();
                boolean starred = false;

                for(Group grp : favouriteGroupList) {
                    if(selectedGroup == grp.getId()) {
                        starred = true;
                    }
                }

                if(!starred) {
                    new FavouriteGroup().execute();
                }
                else {
                    new UnfavouriteGroup().execute();
                }

                new GetGroups().execute();
            }
        });

        return rootView;
    }

    private class GetGroups extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            groupList = new ArrayList<>();
            favouriteGroupList = new ArrayList<>();
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            int index = lvGroupList.getFirstVisiblePosition();
            View v = lvGroupList.getChildAt(0);
            int top = (v == null) ? 0 : v.getTop();

            lvGroupList.setAdapter(new GroupAdapter(getContext(), groupList, favouriteGroupList));

            lvGroupList.setSelectionFromTop(index, top);

        }

        @Override
        protected Void doInBackground(Void... params) {
            String response = Utility.getRequest("api/Groups");
            groupList = Utility.customGson.fromJson(response, new TypeToken<List<Group>>(){}.getType());

            response = Utility.getRequest("api/Groups/Starred");
            favouriteGroupList = Utility.customGson.fromJson(response, new TypeToken<List<Group>>(){}.getType());

            return null;
        }
    }

    private class FavouriteGroup extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getContext(), "Favourite group added", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Utility.postRequest("api/Groups/" + Integer.toString(selectedGroup) + "/Star", "");

            return null;
        }
    }

    private class UnfavouriteGroup extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getContext(), "Favourite group removed", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Utility.postRequest("api/Groups/" + Integer.toString(selectedGroup) + "/Unstar", "");

            return null;
        }
    }
}
