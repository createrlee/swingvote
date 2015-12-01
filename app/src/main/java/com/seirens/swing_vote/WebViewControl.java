package com.seirens.swing_vote;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.parse.ParseUser;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebViewControl {
     private WebView webView;

     public WebViewControl (WebView w) {
         webView = w;
     }

     public void openURL(String url){
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {

                System.out.println("load url is ::::::: " + url);

                String[] array = url.split("/");
                System.out.println(array[0]);
                if (array[0].equals("js:"))
                    return;
                super.onLoadResource(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                System.out.println("hello :::::: " + url);

                String[] array = url.split("/");
                System.out.println(array[0]);
                if (array[0].equals("js:"))
                {

                    return true;
                }
                else
                    return false;
                /*
                if (Uri.parse(url).getHost().equals("www.example.com")) {
                    // This is my web site, so do not override; let my WebView load the page
                    return false;
                }
                // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                //startActivity(intent);
                return true;*/
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                System.out.println("the url is ::::::: " + url);

                String[] array = url.split("/");
                System.out.println(array[0]);
                if (array[0].equals("js:"))
                    return;
                else
                    super.onPageStarted(view, url, favicon);
//                String qp = Uri.parse(url).getQueryParameter("funcname");
                //Log.v("@@@@@@@", url);
            }
        });


        String entity = "current_userId_mobile=" + ParseUser.getCurrentUser().getObjectId();
         webView.postUrl(url,entity.getBytes());
         //webView.loadUrl(url);
    }

     public void httpPostUser(String urlString, ParseUser user) throws IOException {
         String entity = "current_userId_mobile=" + user.getObjectId();

         new ProcessPostTask().execute(urlString,entity);


         /*
         HttpPost request = new HttpPost(url);
         Vector<NameValuePair> nameValue = new Vector<>();
         nameValue.add( new BasicNameValuePair("current_userId_mobile", user.getObjectId()));
         HttpEntity entity = null ;
         try {
             entity = new UrlEncodedFormEntity( nameValue ) ;
         } catch (UnsupportedEncodingException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
         request.setEntity(entity);*/
     }

     public boolean goBack(){
         if(webView.canGoBack()) {
             webView.goBack();
             return true;
         }
         else {
             return false;
         }
     }

    public void reload() {
        webView.reload();
    }

    /** AsynkTask < url, post ,outputstream > */
    private class ProcessPostTask extends AsyncTask<String, String, OutputStream> {
        private Exception exception;
        @Override
        protected OutputStream doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                String entity = params[1];
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(10000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                OutputStream out = urlConnection.getOutputStream();
                out.write(entity.getBytes("UTF-8"));
                out.flush();
                out.close();
                return out;
            }
            catch (Exception e) {
                this.exception = e;
                return null;
            }
        }
    }

}