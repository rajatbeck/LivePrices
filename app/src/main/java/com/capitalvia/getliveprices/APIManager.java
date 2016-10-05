package com.capitalvia.getliveprices;


import android.content.Context;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


/**
 * Created by rajat on 6/10/15.
 */
public class APIManager {

    private static final String TAG = "APIManager";
    RequestQueue mQueue;
    private Context context;
    private ResponseInterface responseInterface;
    StringRequest jsonRequest;
    StringRequest jsonRequest1;

    String url;
    String url1;
    private static final String ALLOWED_URI_CHARS = "utf-8";
    Response.Listener<String> defaultResponseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            responseInterface.parseResponse(response);
        }
    };
    Response.ErrorListener defaultErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            responseInterface.parseResponse("ERROR: " + error.toString());
        }


    };

    public APIManager(Context context, ResponseInterface responseInterface) {
        this.context = context;
        this.responseInterface = responseInterface;
    }

    private void callGetAPI(String params, Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        mQueue = Volley.newRequestQueue(context);
        url = "http://finance.google.com/finance/info?client=ig&q=" + params;

        jsonRequest = new StringRequest(Request.Method.GET, url,
                responseListener, errorListener);


        /******************Code to increase the timeout value of volley response**************/
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        /**********************************************************************************************/
        mQueue.add(jsonRequest);
    }

    private void callFrontdeskGetAPI(String params, Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        mQueue = Volley.newRequestQueue(context);
        url = "http://10.222.1.12/frontdeskweb/api/" + params;
        jsonRequest = new StringRequest(Request.Method.GET, url,
                responseListener, errorListener);


        /******************Code to increase the timeout value of volley response**************/
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        /**********************************************************************************************/
        mQueue.add(jsonRequest);
    }


    private void callFrontdeskGetAPI_(String params) {
        callFrontdeskGetAPI(params, defaultResponseListener, defaultErrorListener);
    }


    private void callGetAPI(String params) {
        callGetAPI(params, defaultResponseListener, defaultErrorListener);
    }

    public void callLivePricesAPI(String params) {
        String query = params;
        callGetAPI(query);
    }

}
