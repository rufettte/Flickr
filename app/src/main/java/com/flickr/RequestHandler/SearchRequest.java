package com.flickr.RequestHandler;


import android.content.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flickr.Activities.SearchableActivity;
import com.flickr.Objects.Image;
import com.flickr.flickr.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;


public class SearchRequest extends RequestHandler{

    public SearchRequest(Context context) {
        super(context);
    }

    @Override
    protected void onPostExecute(String result) {
        contentLoadingProgressBar.hide();
        if (result == null) {
            showMessage(context.getString(R.string.SearchRequest_internal));
            return;
        }

        try {
            List<Image> images = mapJSONResponseBody(result);
            if (images.isEmpty())
                showMessage(context.getString(R.string.SearchRequest_no_data_loaded));
            else
                ((SearchableActivity) context).setChanges(images);
        }
        catch (JsonProcessingException | JSONException e ) {
            showMessage(context.getString(R.string.SearchRequest_external));
        }
    }

    public List<Image> mapJSONResponseBody(String result) throws JsonProcessingException, JSONException {
        JSONObject jsonObject = new JSONObject(result);
        JSONArray jsonArray = jsonObject.getJSONObject(context.getString(R.string.photos)).getJSONArray(context.getString(R.string.photo));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonArray.toString(), mapper.getTypeFactory().constructCollectionType(List.class, Image.class));
    }
}
