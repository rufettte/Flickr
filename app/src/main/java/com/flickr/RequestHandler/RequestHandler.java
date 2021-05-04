package com.flickr.RequestHandler;


import androidx.core.widget.ContentLoadingProgressBar;
import com.google.android.material.snackbar.Snackbar;

import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import android.content.Context;
import java.io.BufferedReader;
import android.app.Activity;
import android.os.AsyncTask;
import com.flickr.flickr.R;
import java.io.IOException;

import android.util.Log;
import android.view.View;
import java.net.URL;

/* This class is common for the whole requests of this application. Currently,
   there is only Searching activity in this application. However, in the future,
   if more requests are required, this class can be inherited by them, just as
   how SearchRequest class does. So, all of the methods defined in this class
   have been implemented by considering all of the request futures. */
public abstract class RequestHandler extends AsyncTask<String, Void, String> {
    Context context;
    ContentLoadingProgressBar contentLoadingProgressBar;

    public RequestHandler(Context context) {
        this.context = context;
    }

    /*Shows error or success messages*/
    protected void showMessage(String message) {
        Snackbar.make(((Activity)context).findViewById(android.R.id.content), message,
                Snackbar.LENGTH_SHORT)
                .show();
    }

    /* Gets response body from the server as string*/
    protected String getResponseBody(HttpURLConnection httpURLConnection) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        for (String line = reader.readLine(); line != null; line=reader.readLine()) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    /*
    * This function creates request connection through GET method.
    *
    * Alternatively, POST method could be used - it is considered more secure, but
    * to keep the app as simple as possible, GET method was preferred.
    * */
    protected HttpURLConnection createRequestConnection(String[] requestParams) throws IOException{
        URL url = new URL(requestParams[0]);
        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.connect();
        return httpURLConnection;
    }

    /* Before starting to sending request, progressbar is started to notify the user that the request has been started to be performed.*/
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        contentLoadingProgressBar = (ContentLoadingProgressBar) ((Activity) context).findViewById(R.id.progressbar);
        contentLoadingProgressBar.setVisibility(View.VISIBLE);
    }

    /* In the following method, the request is sent and the response body is received
    accordingly. This process is done in other thread, rather than the main thread.*/
    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection httpURLConnection = null;
        String responseBody = null;
        try {
            httpURLConnection = createRequestConnection(params);
            responseBody  = getResponseBody(httpURLConnection);
            httpURLConnection.disconnect();
            return responseBody;
        } catch (IOException e) {
            return null;
        }
    }

    /* Response body is passed to this function after doInBackground method finishes its job.
    * Response body is implemented by the (child) classes inheriting this class, because
    * not all of the requests have the same requirements for the response.
    * */
    @Override
    protected abstract void onPostExecute(String result);
}