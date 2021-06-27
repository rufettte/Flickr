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
import android.view.View;
import java.net.URL;


public abstract class RequestHandler extends AsyncTask<String, Void, String> {

    protected Context context;
    protected ContentLoadingProgressBar contentLoadingProgressBar;

    public RequestHandler(Context context) {
        this.context = context;
    }

    protected void showMessage(String message) {
        Snackbar.make(((Activity)context).findViewById(android.R.id.content), message,
                Snackbar.LENGTH_SHORT)
                .show();
    }

    protected String getResponseBody(HttpURLConnection httpURLConnection) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        for (String line = reader.readLine(); line != null; line=reader.readLine()) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    protected HttpURLConnection createRequestConnection(String[] requestParams) throws IOException{
        URL url = new URL(requestParams[0]);
        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.connect();
        return httpURLConnection;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        contentLoadingProgressBar = ((Activity) context).findViewById(R.id.progressbar);
        contentLoadingProgressBar.show();
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection httpURLConnection;
        String responseBody;
        try {
            httpURLConnection = createRequestConnection(params);
            responseBody  = getResponseBody(httpURLConnection);
            httpURLConnection.disconnect();
            return responseBody;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected abstract void onPostExecute(String result);
}