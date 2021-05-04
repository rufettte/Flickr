package com.flickr.flickr;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.flickr.Activities.SearchableActivity;
import com.flickr.Objects.Image;
import com.flickr.RequestHandler.SearchRequest;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TestSearchResponse {

    String invalid =
            "{" +
                "'stat':'fail'," +
                "'code':100," +
                "'message':'Invalid API Key (Key not found)'" +
            "}";

    String valid =
            "{" +
                "'photos': {" +
                "'page':1, " +
                "'pages':4643, " +
                "'perpage':100, " +
                "'total':'464253', " +
                "'photo':[" +
                    "{" +
                        "'id':'51153940211'," +
                        "'owner':'191489695@N04'," +
                        "'secret':'2500c485a6','server':'65535'," +
                        "'farm':66," +
                        "'title':'what a blooming-dream !'," +
                        "'ispublic':1," +
                        "'isfriend':0," +
                        "'isfamily':0" +
                    "}," +
                "]" +
            "}," +
    "'stat':'ok'" +
    "}";

    String empty = "{" +
            "'photos': {" +
                    "'page':1," +
                    "'pages':4643," +
                    "'perpage':100," +
                    "'total':'464253'," +
                    "'photo':[]" +
            "}," +
    "'stat':'ok'}";


    /*response contains error codes; e.g., code=100 -> api_key error
    * (there is no photo/photos labels, therefore JSONException is expected.)
    * */
    @Test(expected = JSONException.class)
    public void check_if_responseBody_contains_Error_Codes() throws JsonProcessingException, JSONException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SearchRequest sr = new SearchRequest(appContext);
        String body = invalid;
        sr.mapJSONResponseBody(body);
    }


    /*response contains correct body with 2 search results*/
    @Test
    public void check_if_responseBody_contains_correct_context() throws JsonProcessingException, JSONException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SearchRequest sr = new SearchRequest(appContext);
        String body = valid;
        assertEquals(2, sr.mapJSONResponseBody(body).size());
    }

    /* response contains empty search result */
    @Test
    public void check_if_responseBody_contains_empty_search_result() throws JsonProcessingException, JSONException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SearchRequest sr = new SearchRequest(appContext);
        String body = empty;
        assertEquals(0, sr.mapJSONResponseBody(body).size());
    }
}