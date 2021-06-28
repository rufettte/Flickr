package com.flickr.Activities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.flickr.Requests.SearchRequest;
import com.flickr.Views.SearchView;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import java.util.ArrayList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchActivityTest {

    @Mock
    private SearchRequest searchRequest;

    @Mock
    private SearchView searchView;


    @Before
    public void setUp() {
        doCallRealMethod().when(searchView).onResponse(any());
    }

    @Test
    public void searchResult_ExternalError() {
        getParameterAsResponse(null);
        searchRequest.loadSearchResult(null, null);

        InOrder inOrder = Mockito.inOrder(searchView);
        inOrder.verify(searchView, times(1)).onExternalError();
        inOrder.verify(searchView, times(1)).onComplete();
        verify(searchView, never()).onInternalError();
        verify(searchView, never()).onSuccess(any());
    }

    @Test
    public void searchResult_InternalError() throws JsonProcessingException, JSONException {
        getParameterAsResponse("Internal error");
        when(searchView.mapJSONResponseBody(any())).thenThrow(JSONException.class);
        searchRequest.loadSearchResult(null, null);

        InOrder inOrder = Mockito.inOrder(searchView);
        inOrder.verify(searchView, times(1)).onInternalError();
        inOrder.verify(searchView, times(1)).onComplete();
        verify(searchView, never()).onExternalError();
        verify(searchView, never()).onSuccess(any());
    }

    @Test
    public void searchResult_SuccessfulResult() throws JsonProcessingException, JSONException {
        getParameterAsResponse("Successful Result");
        when(searchView.mapJSONResponseBody(notNull())).thenReturn(new ArrayList<>());
        searchRequest.loadSearchResult(null, null);

        InOrder inOrder = Mockito.inOrder(searchView);
        inOrder.verify(searchView, times(1)).onSuccess(any());
        inOrder.verify(searchView, times(1)).onComplete();
        verify(searchView, never()).onExternalError();
        verify(searchView, never()).onInternalError();
    }

    protected void getParameterAsResponse(String response) {
        doAnswer((Answer<Object>) invocation -> {
            searchView.onResponse(response);
            return null;
        }).when(searchRequest).loadSearchResult(any(), any());
    }
}