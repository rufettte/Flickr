package com.flickr.RequestHandler;


import android.content.Context;
import android.view.View;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flickr.Activities.SearchableActivity;
import com.flickr.Objects.Image;
import com.flickr.flickr.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;


/*
* This class has its own method (mapJSONResponseBody) and additionally, it inherits
* onPostExecutes method from RequestHandler class to implement it w.r.t its needs.
* */
public class SearchRequest extends RequestHandler{

    public SearchRequest(Context context) {
        super(context);
    }

    /* After the response body is received from the server, this function
    1) either shows error messages
        Note that three types of error messages are defined by us:
        A) internal - resulting from internet connection, etc.
        B) external - resulting from error codes (e.g., code=100: incorrect api key), incorrect url, etc.
        C) No data loaded - when no data is available to present to the user
    2) or adds the search results to the list.
    * */
    @Override
    protected void onPostExecute(String result) {
        /*stop showing the progressbar*/
        contentLoadingProgressBar.setVisibility(View.GONE);
        /* Internal error: e.g., No internet connection*/
        if (result == null) {
            showMessage(context.getString(R.string.SearchRequest_internal));
            return;
        }

        try {
            List<Image> images = mapJSONResponseBody(result);
            if (images.isEmpty()) /* Empty search result ...*/
                showMessage(context.getString(R.string.SearchRequest_no_data_loaded));
            else /* Add search results to the list*/
                ((SearchableActivity) context).setChanges(images);
        }
        /*Server side errors; e.g., code=100 -> Wrong Api key, etc.*/
        catch (JsonProcessingException | JSONException e ) {
            showMessage(context.getString(R.string.SearchRequest_external));
        }
    }

    /*Maps the JSON array "photos" into the list of Image objects.*/
    public List<Image> mapJSONResponseBody(String result) throws JsonProcessingException, JSONException {
        JSONObject jsonObject = new JSONObject(result);
        JSONArray jsonArray = jsonObject.getJSONObject(context.getString(R.string.photos)).getJSONArray(context.getString(R.string.photo));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonArray.toString(), mapper.getTypeFactory().constructCollectionType(List.class, Image.class));
    }
}
