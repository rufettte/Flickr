package com.flickr.RequestParamsBuilder;

import android.content.Context;
import androidx.annotation.NonNull;
import com.flickr.flickr.R;


public class SearchParameterBuilder implements ParameterBuilder {
    private int page;
    private String query;

    public SearchParameterBuilder(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int pageNumber) {
        this.page = pageNumber;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @NonNull
    @Override
    public String[] getParams(@NonNull Context context) {
        return new String[]{
                context.getString(R.string.searchUrl) + //url
                getQuery() + // search_term
                context.getString(R.string.page) +
                getPage() // page number
        };
    }
}
