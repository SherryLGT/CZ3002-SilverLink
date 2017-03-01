package lcnch.cz3002.ntu.silverlink.controller;

/**
 * This class is to handle Firebase Cloud Messaging (FCM) service.
 *
 * @author Calvin Che Zi Yi
 * @version 1.0
 * @since 28/02/2017
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import lcnch.cz3002.ntu.silverlink.R;
import lcnch.cz3002.ntu.silverlink.activity.SplashActivity;
import lcnch.cz3002.ntu.silverlink.model.FCMType;


public class FCMService extends FirebaseMessagingService {

    private static final String TAG = "FCMService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (Integer.parseInt(remoteMessage.getData().get("type")) == FCMType.LOCATION_REQUEST.getValue()) {
        }

        FCMType type = FCMType.values()[Integer.parseInt(remoteMessage.getData().get("type"))];
        switch (type) {
            case LOCATION_REQUEST:
                break;
            case FRIEND_MESSAGE:
                break;
            case GROUP_MESSAGE:
                break;
            case FRIEND_ADDED:
                sendNotification(remoteMessage.getNotification());
                break;
            default:
                break;
        }
        Intent intent = new Intent("messageUpdater");
        intent.putExtra("msgType", type.getValue());
        intent.putExtra("itemId", Integer.parseInt(remoteMessage.getData().get("id")));
        getApplication().sendBroadcast(intent);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param notification FCM notification received.
     */
    private void sendNotification(RemoteMessage.Notification notification) {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo_empty)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
