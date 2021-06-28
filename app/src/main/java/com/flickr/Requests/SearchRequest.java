package com.flickr.Requests;

import android.os.Handler;
import android.os.Looper;
import com.flickr.Views.SearchView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchRequest {

    private String getResponseBody(HttpURLConnection httpURLConnection) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    private HttpURLConnection createRequestConnection(String[] requestParams) throws IOException {
        URL url = new URL(requestParams[0]);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.connect();
        return httpURLConnection;
    }

    public void loadSearchResult(SearchView searchView, String[] params) {
        Handler handler = new Handler(Looper.getMainLooper());
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute (() -> {
            HttpURLConnection httpURLConnection;
            String response;
            try {
                httpURLConnection = createRequestConnection(params);
                response  = getResponseBody(httpURLConnection);
                httpURLConnection.disconnect();
            } catch (IOException e) {
                response = null;
            }
            final String responseBody = response;
            handler.post(() -> searchView.onResponse(responseBody));
        });
    }
}
