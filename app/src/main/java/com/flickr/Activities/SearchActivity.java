package com.flickr.Activities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import com.flickr.Objects.Image;
import com.flickr.Adapters.ImageAdapter;
import com.flickr.Requests.SearchRequest;
import com.flickr.Utils.SuggestionProvider;
import com.flickr.Views.SearchView;
import com.flickr.flickr.R;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends Activity {

    private Context context;
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private int pageNumber = 1;
    private String search_term;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);
        init();
        handleIntent(getIntent());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!recyclerView.canScrollVertically(1) && dy != 0 && pageNumber > 1)
                    loader();
            }
        });
    }


    private void init(){
        context = this;
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        imageAdapter = new ImageAdapter(new ArrayList<>(), context);
        recyclerView.setAdapter(imageAdapter);
    }


    public void setChanges(List<Image> newImages){
        imageAdapter.addAll(newImages);
        incrementPageNumber();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }


    private void handleIntent(@NonNull Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    SuggestionProvider.AUTHORITY, SuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);
            search_term = query.trim();
            loader();
        }
    }

    private void loader(){
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.loadSearchResult(new SearchView(context), getUrl());
    }

    private void incrementPageNumber() {
        pageNumber++;
    }

    private String[] getUrl() {
        return new String[]{
                context.getString(R.string.searchUrl) + //url
                search_term + // search_term
                context.getString(R.string.page) +
                pageNumber // page number
        };
    }
}