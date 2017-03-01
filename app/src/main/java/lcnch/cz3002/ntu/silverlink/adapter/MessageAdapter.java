package lcnch.cz3002.ntu.silverlink.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

import lcnch.cz3002.ntu.silverlink.R;
import lcnch.cz3002.ntu.silverlink.model.Message;

/**
 *
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 28/02/2017
 */

public class MessageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Message> messageList;

    private TextView sentAt, fullName;
    private ImageView profilePic;
    private Bitmap bitmap;

    public MessageAdapter(Context context, ArrayList<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.message_list_item, null);
        }

        sentAt = (TextView) convertView.findViewById(R.id.tv_date);
        profilePic = (ImageView) convertView.findViewById(R.id.iv_profile_pic);
        fullName = (TextView) convertView.findViewById(R.id.tv_name);

        SimpleDateFormat sdf = new SimpleDateFormat("d MMM, h:mm a");
        sentAt.setText(sdf.format(messageList.get(position).getSentAt()));
        if(messageList.get(position).getSentBy().getProfilePicture() != null) {
            bitmap = BitmapFactory.decodeByteArray(messageList.get(position).getSentBy().getProfilePicture(), 0, messageList.get(position).getSentBy().getProfilePicture().length);
            profilePic.setImageBitmap(bitmap);
        }
        else {
            if(new Random().nextBoolean())
                profilePic.setImageResource(R.drawable.default_guy);
            else
                profilePic.setImageResource(R.drawable.default_girl);
        }
        fullName.setText(messageList.get(position).getSentBy().getFullName());

        return convertView;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}