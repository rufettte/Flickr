package com.flickr.Views;

import android.app.Activity;
import android.content.Context;
import androidx.core.widget.ContentLoadingProgressBar;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flickr.Activities.SearchActivity;
import com.flickr.Objects.Image;
import com.flickr.UIMassages.MassageHandler;
import com.flickr.Utils.ViewInterface;
import com.flickr.flickr.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

public class SearchView implements ViewInterface<String> {
    private final ContentLoadingProgressBar pb;
    private final Context context;

    public SearchView(Context context) {
        this.context = context;
        this.pb = ((Activity) this.context).findViewById(R.id.progressbar);
        this.pb.show();
    }

    public List<Image> mapJSONResponseBody(String result) throws JSONException, JsonProcessingException {
        JSONObject jsonObject = new JSONObject(result);
        JSONArray jsonArray = jsonObject.getJSONObject(context.getString(R.string.photos)).getJSONArray(context.getString(R.string.photo));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonArray.toString(), mapper.getTypeFactory().constructCollectionType(List.class, Image.class));
    }


    @Override
    public void onResponse(String response) {
        if (response==null) onExternalError();
        else {
            try {
                List<Image> images = mapJSONResponseBody(response);
                onSuccess(images);
            } catch (JsonProcessingException | JSONException e) {
                onInternalError();
            }
        }
        onComplete();
    }

    @Override
    public void onSuccess(List<Image> images) {
        if (images.isEmpty())
            MassageHandler.showMessage(context, R.string.SearchRequest_no_data_loaded);
        else ((SearchActivity) context).setChanges(images);
    }

    @Override
    public void onInternalError() {
        MassageHandler.showMessage(context, R.string.SearchRequest_internal);
    }

    @Override
    public void onExternalError() {
        MassageHandler.showMessage(context, R.string.SearchRequest_external);
    }

    @Override
    public void onComplete() {
        this.pb.hide();
    }
}
