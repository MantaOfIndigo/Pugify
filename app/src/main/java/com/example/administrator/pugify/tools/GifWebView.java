package com.example.administrator.pugify.tools;

import android.content.Context;
import android.webkit.WebView;

/**
 * Created by Administrator on 01/12/2015.
 */
public class GifWebView extends WebView {

    public GifWebView(Context context, String path) {
        super(context);

        loadUrl(path);
    }
}