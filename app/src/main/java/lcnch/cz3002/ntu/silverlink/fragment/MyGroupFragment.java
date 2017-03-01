package lcnch.cz3002.ntu.silverlink.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import lcnch.cz3002.ntu.silverlink.R;
import lcnch.cz3002.ntu.silverlink.adapter.MyGroupAdaptor;
import lcnch.cz3002.ntu.silverlink.controller.Utility;
import lcnch.cz3002.ntu.silverlink.model.Friend;
import lcnch.cz3002.ntu.silverlink.model.Group;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyGroupFragment extends Fragment {

    private View rootView;

    private ListView lvGroupList;

    public static ArrayList<Group> groupList;
    public static Group group;

    /**
     * Default constructor for FriendFragment
     */
    public MyGroupFragment() {
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
                group = groupList.get(position);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new MessageFragment(), "MessageFragment").commit();
            }
        });


        return rootView;
    }

    public class GetGroups extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            groupList = new ArrayList<>();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            int index = lvGroupList.getFirstVisiblePosition();
            View v = lvGroupList.getChildAt(0);
            int top = (v == null) ? 0 : v.getTop();

            lvGroupList.setAdapter(new MyGroupAdaptor(getContext(), groupList));

            lvGroupList.setSelectionFromTop(index, top);
        }

        @Override
        protected Void doInBackground(Void... params) {
            String response = Utility.getRequest("api/Groups/Starred");
            groupList = Utility.customGson.fromJson(response, new TypeToken<List<Group>>() {}.getType());
            return null;
        }
    }
}
