package com.capitalvia.getliveprices;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ResponseInterface, LivePriceUpdateService.CallBack {

    private static final String TAG = "MAIN_ACTIVITY";
    ProgressBar mProgressBar;
    RecyclerView mRecyclerView;
    private String scripts = "NSE:20MICRONS,NSE:TATASTEEL";
    private static List<LivePrice> mLivePriceList = new ArrayList<>();
    LivePriceUpdateService livePriceUpdateService;
    boolean livePriceUpdateServiceBound = false;
    private Intent intent;
    private PendingIntent pi;
    private LivePriceAdapter livePriceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.loader_live_price);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_live_prices);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        new APIManager(getApplicationContext(), MainActivity.this).callLivePricesAPI(scripts);

    }

    @Override
    public Void parseResponse(String response) {
        String replaceComment = response.replace("//", "");
        Log.d(TAG, replaceComment);
        try {

            JSONArray jsonArray = new JSONArray(replaceComment);
            if (jsonArray.length() != 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    LivePrice livePrice = new LivePrice();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    livePrice.setLive_price(jsonObject.getString("l"));
                    livePrice.setLast_updated_time(jsonObject.getString("lt"));
                    livePrice.setPrev_day_close(jsonObject.getString("pcls_fix"));
                    mLivePriceList.add(livePrice);
                }
                livePriceAdapter = new LivePriceAdapter(getApplicationContext(), mLivePriceList);
                mRecyclerView.setAdapter(livePriceAdapter);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            LivePriceUpdateService.LocalBinder binder = (LivePriceUpdateService.LocalBinder) service;
            livePriceUpdateService = binder.getService();
            livePriceUpdateService.registerClient(MainActivity.this);
            livePriceUpdateServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            livePriceUpdateServiceBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        SetAlarm(MainActivity.this);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (livePriceUpdateServiceBound) {
            unbindService(mServiceConnection);
            livePriceUpdateServiceBound = false;
            CancelAlarm(getApplicationContext());
        }
    }

    public void SetAlarm(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        intent = new Intent(context, LivePriceUpdateService.class);
        intent.putExtra("script_name", scripts);
        pi = PendingIntent.getService(context, 0, intent, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60, pi);
    }

    public void CancelAlarm(Context context) {
//        intent = new Intent(context, LivePriceUpdateService.class);
//        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pi);
    }

    @Override
    public void getResponse(String response) {
        String replaceComment = response.replace("//", "");
        Log.d(TAG, replaceComment);
        try {
            JSONArray jsonArray = new JSONArray(replaceComment);
            if (jsonArray.length() != 0) {
                mLivePriceList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    LivePrice livePrice = new LivePrice();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    livePrice.setLive_price(jsonObject.getString("l"));
                    livePrice.setLast_updated_time(jsonObject.getString("lt"));
                    livePrice.setPrev_day_close(jsonObject.getString("pcls_fix"));
                    mLivePriceList.add(livePrice);
                }
            }
            Log.d(TAG, mLivePriceList.get(0).getLast_updated_time());
            livePriceAdapter.refreshList(mLivePriceList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
