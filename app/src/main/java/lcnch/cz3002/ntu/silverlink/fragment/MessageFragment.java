package lcnch.cz3002.ntu.silverlink.fragment;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lcnch.cz3002.ntu.silverlink.R;
import lcnch.cz3002.ntu.silverlink.adapter.MessageAdapter;
import lcnch.cz3002.ntu.silverlink.controller.Utility;
import lcnch.cz3002.ntu.silverlink.model.FCMType;
import lcnch.cz3002.ntu.silverlink.model.Message;

import static lcnch.cz3002.ntu.silverlink.fragment.FriendFragment.friend;
import static lcnch.cz3002.ntu.silverlink.fragment.MyGroupFragment.group;

/**
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 28/02/2017
 */

public class MessageFragment extends Fragment {

    public static FCMType messageType;
    private View rootView;
    private ImageView ivProfilePic, ivPlay;
    private TextView tvName, tvPlay;
    private ListView lvMessages;
    private ImageButton ibRecord;

    private Bitmap bitmap;
    private ArrayList<Message> messageList;

    private Message tempMessage;
    private boolean recording = false;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private File audioFile = null;
    private File folder = new File(Environment.getExternalStorageDirectory() + "/SilverLink");

    /**
     * Default constructor for FriendFragment
     */
    public MessageFragment() {
        // Required empty public constructor
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getIntExtra("msgType",0) == FCMType.FRIEND_MESSAGE.getValue()){
                if(intent.getIntExtra("itemId",0) == friend.getId()) {
                    new GetMessages().execute();
                }
            }
           else if(intent.getIntExtra("msgType",0) == FCMType.GROUP_MESSAGE.getValue()){
                if(intent.getIntExtra("itemId",0) == group.getId()) {
                    new GetMessages().execute();
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_message, container, false);

        ivProfilePic = (ImageView) rootView.findViewById(R.id.iv_profile_pic);
        tvName = (TextView) rootView.findViewById(R.id.tv_name);
        lvMessages = (ListView) rootView.findViewById(R.id.lv_messages);
        ibRecord = (ImageButton) rootView.findViewById(R.id.ib_record);

        getActivity().registerReceiver(new MessageReceiver(), new IntentFilter("messageUpdater"));

        if (!folder.exists()) {
            folder.mkdir();
        }

        audioFile = new File(folder.getAbsolutePath(), "recording.3gp");

        if (messageType == FCMType.FRIEND_MESSAGE) {
            if (friend.getUser().getProfilePicture() != null) {
                bitmap = BitmapFactory.decodeByteArray(friend.getUser().getProfilePicture(), 0, friend.getUser().getProfilePicture().length);
                ivProfilePic.setImageBitmap(bitmap);
            } else {
                if (new Random().nextBoolean())
                    ivProfilePic.setImageResource(R.drawable.default_guy);
                else
                    ivProfilePic.setImageResource(R.drawable.default_girl);
            }
            tvName.setText(friend.getUser().getFullName());
        } else if (messageType == FCMType.GROUP_MESSAGE) {
            if (group.getImage() != null) {
                bitmap = BitmapFactory.decodeByteArray(group.getImage(), 0, group.getImage().length);
                ivProfilePic.setImageBitmap(bitmap);
            } else {
                if (new Random().nextBoolean())
                    ivProfilePic.setImageResource(R.drawable.default_guy);
                else
                    ivProfilePic.setImageResource(R.drawable.default_girl);
            }
            tvName.setText(group.getName());
        }

        new GetMessages().execute();

        lvMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tempMessage = messageList.get(position);
                ConvertBytesToFile(tempMessage.getMessageData());

                if (!CheckPermission()) {
                    RequestPermission();
                }

                StartPlaying();

                ivPlay = (ImageView) view.findViewById(R.id.iv_msg);
                tvPlay = (TextView) view.findViewById(R.id.tv_play);
                ivPlay.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_stop));
                tvPlay.setText(getString(R.string.tv_stop));
            }
        });

        ibRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!recording) {
                    ibRecord.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.record_button));
                    ibRecord.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_stop));

                    if (!CheckPermission()) {
                        RequestPermission();
                    }

                    StartRecording();
                    recording = true;
                } else {
                    ibRecord.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.flat_button));
                    ibRecord.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_record));

                    StopRecording();
                    recording = false;

                    new SendMessage().execute();
                }
            }
        });

        return rootView;
    }


    private class GetMessages extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            messageList = new ArrayList<Message>();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            lvMessages.setAdapter(new MessageAdapter(getContext(), messageList));
            lvMessages.setSelection(messageList.size() - 1);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (messageType == FCMType.FRIEND_MESSAGE) {
                String response = Utility.getRequest("api/Friends/" + friend.getId() + "/Messages/1970-01-01");
                messageList = Utility.customGson.fromJson(response, new TypeToken<List<Message>>() {
                }.getType());
            } else if (messageType == FCMType.GROUP_MESSAGE) {
                String response = Utility.getRequest("api/Groups/" + group.getId() + "/Messages/1970-01-01");
                messageList = Utility.customGson.fromJson(response, new TypeToken<List<Message>>() {
                }.getType());
            }


            return null;
        }
    }

    private void StartRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaRecorder.start();
    }

    private void StopRecording() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    private void StartPlaying() {
        if (mediaPlayer != null) {
            StopPlaying();
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioFile.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ivPlay.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_play));
                tvPlay.setText(getString(R.string.tv_play));
            }
        });
    }

    private void StopPlaying() {
        mediaPlayer.release();
        mediaPlayer = null;

        ivPlay.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_play));
        tvPlay.setText(getString(R.string.tv_play));
    }

    private boolean CheckPermission() {
        int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 0);
    }

    private void ConvertBytesToFile(byte[] bytearray) {
        try {
            FileOutputStream fileoutputstream = new FileOutputStream(audioFile);
            fileoutputstream.write(bytearray);
            fileoutputstream.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private byte[] ToByteArray(String filePath) {
        try {
            InputStream in;
            in = new FileInputStream(filePath);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int read = 0;
            byte[] buffer = new byte[1024];
            while (read != -1) {
                read = in.read(buffer);
                if (read != -1)
                    out.write(buffer, 0, read);
            }
            out.close();
            in.close();
            return out.toByteArray();
        } catch (Exception ex) {
            return new byte[0];
        }
    }

    private class SendMessage extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            new GetMessages().execute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            tempMessage = new Message();
            byte[] data = ToByteArray(audioFile.getAbsolutePath());
            tempMessage.setMessageData(data);
            if (messageType == FCMType.FRIEND_MESSAGE) {
                Utility.postRequest("api/Friends/" + friend.getId() + "/Messages", Utility.customGson.toJson(tempMessage));
            }
            else if (messageType == FCMType.GROUP_MESSAGE) {
                Utility.postRequest("api/Groups/" + group.getId() + "/Messages", Utility.customGson.toJson(tempMessage));
            }

            return null;
        }
    }

}
