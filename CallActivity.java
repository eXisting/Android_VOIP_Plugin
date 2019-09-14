package io.CareAR.connect;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class CallActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WakeUpPhone();

        setContentView(R.layout.activity_call);
    }

    public void AcceptCall(View view) {
        Log.i("CallActivity", "AcceptCall");
    }

    public void DeclineCall(View view) {
        Log.i("CallActivity", "DeclineCall");
        finish();
    }

    private void WakeUpPhone() {
        final Window win = getWindow();
        win.addFlags( WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON );
    }
}
