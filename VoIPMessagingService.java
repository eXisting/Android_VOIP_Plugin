package io.CareAR.connect;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.MessagingUnityPlayerActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class VoIPMessagingService extends FirebaseMessagingService {
    private static String TITLE_KEY = "title";
    private static String MESSAGE_KEY = "body";
    public static String SESSION_ID_KEY = "sessionRoomID";
    public static String CALLER_KEY = "callerName";

    private static final String NOTIFICATION_ID_KEY = "NOTIFICATION_ID";
    private static final String VOICE_CHANNEL = "default";

    public class CallInvite {
        public String title;
        public String roomID;
        public String callerName;
        public String message;

        public CallInvite(Map<String, String> body) {
            message = body.get(MESSAGE_KEY);
            roomID = body.get(SESSION_ID_KEY);
            callerName = body.get(CALLER_KEY);
            title = body.get(TITLE_KEY);
        }
    }

    private NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }


    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("VoIPMessagingService", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("VoIPMessagingService", "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("VoIPMessagingService", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        Map<String, String> data = remoteMessage.getData();
        CallInvite invite = new CallInvite(data);

        Intent callIntent = new Intent(this, CallActivity.class);
        callIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        callIntent.putExtra(SESSION_ID_KEY, invite.roomID);
        callIntent.putExtra(CALLER_KEY, invite.callerName);

        startActivity(callIntent);

        //showBasicNotification(invite);
    }
    // [END receive_message]


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("FCM", "Token: " + s);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param invite FCM message body received.
     */
    private void showBasicNotification(CallInvite invite) {
        Intent intent = new Intent(this, MessagingUnityPlayerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.fcm_fallback_notification_channel_label);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.app_icon)
                        .setContentTitle(invite.title)
                        .setContentText(invite.message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
