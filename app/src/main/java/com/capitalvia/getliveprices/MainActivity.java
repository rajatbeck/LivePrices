package com.capitalvia.getliveprices;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity implements ResponseInterface {

    private static final String TAG = "MAIN_ACTIVITY";
    ProgressBar mProgressBar;
    RecyclerView mRecyclerView;
    private String scripts = "NSE:20MICRONS,NSE:TATASTEEL";
    private List<LivePrice> mLivePriceList = new ArrayList<>();

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
                mRecyclerView.setAdapter(new LivePriceAdapter(getApplicationContext(), mLivePriceList));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void SetAlarm(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent i = new Intent(context, Alarm.class);
//        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
//        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 1, pi); // Millisec * Second * Minute
    }

    public void CancelAlarm(Context context) {
//        Intent intent = new Intent(context, Alarm.class);
//        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.cancel(sender);
    }
}
