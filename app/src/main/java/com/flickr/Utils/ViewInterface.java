package com.flickr.Utils;

import com.flickr.Objects.Image;

import java.util.List;

public interface ViewInterface<T> {
    void onResponse(T response);
    void onSuccess(List<Image> images);
    void onInternalError();
    void onExternalError();
    void onComplete();
}
