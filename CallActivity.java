package io.CareAR.connect;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.MessagingUnityPlayerActivity;
import com.unity3d.player.UnityPlayer;


public class CallActivity extends Activity {

    private MediaPlayer mMediaPlayer;
    private CallInfoModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new CallInfoModel(getIntent().getExtras());

        Log.e("CallActivity: ", "onCreate!");

        DisplayCallerInfo();
        WakeUpPhone();

        setContentView(R.layout.activity_call);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopDefaultNotificationRingtone();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // If the screen is off then the device has been locked
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        if (!powerManager.isInteractive()) {
            mMediaPlayer.pause();
        }
    }

    public void AcceptCall(View view) {
        Log.i("CallActivity", "AcceptCall");
        Intent mainUnityActivity = new Intent(this,MessagingUnityPlayerActivity.class);
        mainUnityActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (model.IsHasCallInfo())
        {
            mainUnityActivity.putExtras(model.GetInfoAsBundle());
            startActivity(mainUnityActivity);

            // TODO: gives "Native libraries not loaded - dropping message for" when there is no activity to use
            UnityPlayer.UnitySendMessage("NativeBridge", "ProcessNativeCallWith", model.BuildInfoString());
        }
        else
            Log.e("AcceptCall: ", "No call info from VOIP class!");

        model.Reset();
        finish();
    }

    public void DeclineCall(View view) {
        Log.i("CallActivity", "DeclineCall");
        finish();
    }

    private void DisplayCallerInfo() {
        // TODO
    }

    private void WakeUpPhone() {
        playDefaultNotificationRingtone();

        final Window win = getWindow();
        win.addFlags( WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON );
    }

    private void playDefaultNotificationRingtone() {
        try {
            Uri alert =  RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(this, alert);

            final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
                mMediaPlayer.setLooping(true);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopDefaultNotificationRingtone() {
        mMediaPlayer.stop();
        mMediaPlayer.release();
    }
}
