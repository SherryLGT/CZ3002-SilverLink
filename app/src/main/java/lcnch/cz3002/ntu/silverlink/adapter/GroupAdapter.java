package lcnch.cz3002.ntu.silverlink.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import lcnch.cz3002.ntu.silverlink.R;
import lcnch.cz3002.ntu.silverlink.model.Group;
import lcnch.cz3002.ntu.silverlink.model.UserItem;

/**
 *
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 21/02/2017
 */

public class GroupAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Group> groupItems;
    private ArrayList<Group> favouriteGroupList;

    private ImageView groupPic, star;
    private TextView groupName;
    private Bitmap bitmap;

    public GroupAdapter(Context context, ArrayList<Group> groupItems, ArrayList<Group> favouriteGroupList) {
        this.context = context;
        this.groupItems = groupItems;
        this.favouriteGroupList = favouriteGroupList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_list_item, null);
        }

        groupPic = (ImageView) convertView.findViewById(R.id.iv_group_pic);
        groupName = (TextView) convertView.findViewById(R.id.tv_name);
        star = (ImageView) convertView.findViewById(R.id.iv_star);

        if(groupItems.get(position).getImage() != null) {
            bitmap = BitmapFactory.decodeByteArray(groupItems.get(position).getImage(), 0, groupItems.get(position).getImage().length);
            groupPic.setImageBitmap(bitmap);
        }
        else {
            if(new Random().nextBoolean())
                groupPic.setImageResource(R.drawable.default_guy);
            else
                groupPic.setImageResource(R.drawable.default_girl);
        }
        groupName.setText(groupItems.get(position).getName());

        star.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_empty_star));
        for(Group grp : favouriteGroupList) {
            if(groupItems.get(position).getId() == grp.getId()) {
                star.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_filled_star));
            }
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return groupItems.size();
    }

    @Override
    public Object getItem(int position) {
        return groupItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
