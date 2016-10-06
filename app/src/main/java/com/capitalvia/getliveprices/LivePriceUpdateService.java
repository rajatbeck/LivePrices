package com.capitalvia.getliveprices;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Rajat on 10/5/2016.
 */
public class LivePriceUpdateService extends IntentService implements ResponseInterface {

    private static final String TAG = "PRICE_UPDATE_SERVICE";
    private IBinder mBinder = new LocalBinder();
    CallBack activity;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public LivePriceUpdateService(String name) {
        super(name);
    }

    public LivePriceUpdateService() {
        super("LivePriceUpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, " Handle Intent called");


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "on Start Command Called");
        String scripts = intent.getStringExtra("script_name");
        new APIManager(getApplicationContext(), LivePriceUpdateService.this).callLivePricesAPI(scripts);
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "on Create Called");

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "on Bind Called");
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(TAG, "on Rebind Called");

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "on Start Called");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "on Destroy Called");

    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "on Unbind Called");
        return super.onUnbind(intent);
    }

    public void registerClient(Activity activity) {
        this.activity = (CallBack) activity;
    }

    @Override
    public Void parseResponse(String response) {
        activity.getResponse(response);
        return null;
    }

    public class LocalBinder extends Binder {
        LivePriceUpdateService getService() {
            return LivePriceUpdateService.this;
        }
    }

    public interface CallBack {
        void getResponse(String response);
    }
}
