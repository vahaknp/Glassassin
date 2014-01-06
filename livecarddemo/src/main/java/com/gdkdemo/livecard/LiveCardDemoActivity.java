package com.gdkdemo.livecard;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.gdkdemo.livecard.service.LiveCardDemoLocalService;
import com.gdkdemo.livecard.R;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import java.util.ArrayList;


// The "main" activity...
public class LiveCardDemoActivity extends Activity
{
    // Service to handle liveCard publishing, etc...
    private boolean mIsBound = false;
    private LiveCardDemoLocalService liveCardDemoLocalService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d("onServiceConnected() called.");
            liveCardDemoLocalService = ((LiveCardDemoLocalService.LocalBinder)service).getService();
        }
        public void onServiceDisconnected(ComponentName className) {
            Log.d("onServiceDisconnected() called.");
            liveCardDemoLocalService = null;
        }
    };
    private void doBindService()
    {
        bindService(new Intent(this, LiveCardDemoLocalService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }
    private void doUnbindService() {
        if (mIsBound) {
            unbindService(serviceConnection);
            mIsBound = false;
        }
    }
    private void doStartService()
    {
        startService(new Intent(this, LiveCardDemoLocalService.class));
    }
    private void doStopService()
    {
        stopService(new Intent(this, LiveCardDemoLocalService.class));
    }


    @Override
    protected void onDestroy()
    {
        doUnbindService();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d("onCreate() called.");

        setContentView(R.layout.activity_livecarddemo);

        // Service to control the life cycle of a LiveCard.
        // bind() does not work. We need to call start() explicitly...
        // doBindService();
        doStartService();
        // TBD: We need to call doStopService() when the user "closes" the app....
        // ...
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d("onResume() called.");

        // For live card menu handling
        openOptionsMenu();
    }


    // Context menus
    // ...

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        Log.d("onCreateOptionsMenu() called.");

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_livecarddemo_livecard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Log.d("onOptionsItemSelected() called.");

        // Handle item selection.
        switch (item.getItemId()) {
            case R.id.menu_stop_livecarddemo:
                doStopService();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onOptionsMenuClosed(Menu menu)
    {
        Log.d("onOptionsItemSelected() called.");

        // Nothing else to do, closing the activity.
        finish();
    }


}
