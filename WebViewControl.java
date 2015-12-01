package com.swingvote.jsbridgetest;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Karl on 2015. 11. 17..
 */
public class WebViewControl {
    public void openURL(WebView webView, String url) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                String qp = Uri.parse(url).getQueryParameter("funcname");
                Log.v("@@@@@@@", url);
            }
        });
        webView.loadUrl(url);
    }
}
