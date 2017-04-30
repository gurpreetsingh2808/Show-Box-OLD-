package com.popular_movies.service.interceptors;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

import com.google.gson.Gson;
import com.popular_movies.service.ApiErrorResponse;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;

import okhttp3.Protocol;
import okhttp3.Response;

/**
 * Created by Gurpreet on 4/10/2016.
 */
public class ErrorInterceptor implements okhttp3.Interceptor {

    // The name of the TAG
    public String TAG = ErrorInterceptor.class.getSimpleName();

    // The instance of activity
    private WeakReference<Activity> activity;

    // The public constructor of the class
    public ErrorInterceptor(Activity activity) {
        this.activity = new WeakReference<>(activity);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        try {

            Response response = chain.proceed(chain.request());

            if (!response.isSuccessful()) {
                Log.e(TAG, "There seems to be a error in the response, processing the error");

                final Gson gson = new Gson();
                final ApiErrorResponse errorData = gson.fromJson(response.body().charStream(), ApiErrorResponse.class);
                if (errorData == null) {
                    Log.d(TAG, "intercept: errordata is null");
                } else {
                    Log.d(TAG, "intercept: error data is not null");
                }

                if (activity != null && activity.get() != null && errorData != null) {

                    //  Handle any unknown error
                    activity.get().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(activity.get())
                                    .setTitle("Error " + errorData.getStatus_code())
                                    .setMessage("\n" + errorData.getStatus_message())
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setCancelable(false)
                                    .create().show();
                        }
                    });
                } else {
                    Log.e(TAG, "Cannot handle the error and display UI element as activity reference is not found");
                }
            }
            return response;
        }
        catch (SocketTimeoutException e) {
            e.printStackTrace();
            //SnackBarManager.renderNetworkIssueSnackBar(activity.get());
            return new Response.Builder().request(chain.request()).protocol(Protocol.HTTP_1_1).code(HttpURLConnection.HTTP_OK).build();
        }
        catch (IOException e) {
            e.printStackTrace();
            return new Response.Builder().request(chain.request()).protocol(Protocol.HTTP_1_1).code(HttpURLConnection.HTTP_OK).build();
        }
        catch (Exception e) {
            e.printStackTrace();
            return new Response.Builder().request(chain.request()).protocol(Protocol.HTTP_1_1).code(HttpURLConnection.HTTP_OK).build();
        }
    }
}
