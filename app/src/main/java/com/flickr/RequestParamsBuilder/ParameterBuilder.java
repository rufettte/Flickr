package com.flickr.RequestParamsBuilder;

import android.content.Context;

/* This is the interface that can help to build parameters for the GET method requests.
* */
public interface ParameterBuilder{
    String[] getParams(Context context) ;
}
