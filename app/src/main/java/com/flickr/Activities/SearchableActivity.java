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
import com.flickr.RequestHandler.SearchRequest;
import com.flickr.RequestParamsBuilder.SearchParameterBuilder;
import com.flickr.Utils.SuggestionProvider;
import com.flickr.flickr.R;
import java.util.ArrayList;
import java.util.List;


public class SearchableActivity extends Activity {

    private Context context;
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private SearchParameterBuilder parameterBuilder;


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
                /*"Should load more or not" is defined by the following condition.*/
                if(!recyclerView.canScrollVertically(1) && dy != 0 && parameterBuilder.getPage() > 1) {
                    loader(parameterBuilder.getParams(context));
                }
            }
        });
    }


    private void init(){
        context = this;
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        imageAdapter = new ImageAdapter(new ArrayList<>(), context);
        recyclerView.setAdapter(imageAdapter);
        parameterBuilder = new SearchParameterBuilder(1);
    }


    public void setChanges(List<Image> newImages){
        imageAdapter.addAll(newImages);
        parameterBuilder.setPage(parameterBuilder.getPage() + 1);
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
            parameterBuilder.setQuery(query.trim());
            loader(parameterBuilder.getParams(context));
        }
    }

    private void loader(String[] params){
        SearchRequest requestHandler = new SearchRequest(context);
        requestHandler.execute(params);
    }
}