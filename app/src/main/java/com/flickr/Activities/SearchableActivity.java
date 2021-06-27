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

/*
* This activity class performs the search operation, shows the search results and is capable of
* doing endless scroll until there is no more relevant item available to present to the user.
*
* Note 1: per_page is set to 100 by default, but the user can not see all of them at once. Therefore,
*         to avoid overloading, per_page is set to 20, which means 20 items are pulled at each request of the user.
*
*
* Note 2: "page" variable of the SearchParameterBuilder object is incremented by 1 at the end of each "load more" operation.
* */
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

    /* Updates the search list:
       * Adds newly loaded search results
       * Increments the page number by 1 */
    public void setChanges(List<Image> newImages){
        imageAdapter.addAll(newImages);
        parameterBuilder.setPage(parameterBuilder.getPage() + 1);
    }

    /*
    * This activity's launch mode is set to "singleTop". So, if it is re-launched again
    * its instance will not be created and its current state will be preserved.
    * At that time, the intent data will be received through the onNewIntent callback.
    * */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    /*
    * Gets the query written to the search dialog box,
    * Removes the left and right blanks around the search term (to be in safe mode).
    * Asks to apply loading the search results with page=1.
    * */
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

    /*
    * loads the search results.
    * */
    private void loader(String[] params){
        SearchRequest requestHandler = new SearchRequest(context);
        requestHandler.execute(params);
    }
}