package com.app.tp.layerchatintegration;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        <iframe width="560" height="315" src="https://www.youtube.com/embed/hpgSf5ZEgUs" frameborder="0" allowfullscreen></iframe>

//        webview.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorAccent));

//        String frameVideo = "<div>Youtube video .. <br> <iframe width=\"100%\" height=\"315\" src=\"https://www.youtube.com/embed/hpgSf5ZEgUs\" frameborder=\"0\" allowfullscreen></iframe>></div>";
//        String frameVideo ="<iframe width=560 height=349 src=https://www.youtube.com/embed/3kudmpzWpB4 frameborder=0 allowfullscreen=>";


        String frameVideo ="<div id=\"aCont\"> <style> #aCont img { max-width:100%; } </style>\n<p>If you have had a C-section delivery under general anesthesia, you may be on a drip. Even then, the baby should be given to you for <a href=\"http://app.babychakra.com/collection/131\" target=\"_blank\">breastfeeding</a>&nbsp;after about 4 hours of the operation, when you would have recovered from the effects of anesthesia. You will need the assistance of a hospital attendant or a close relative to give your child the first breastfeed. As you lie on your back, the nurse may place the baby on a pillow raised to the level of your breast, so she can conveniently reach it.</p>\r\n\r\n<p><br></p>\r\n\r\n\r\n\r\n<p>If you have had an episiotomy, your doctor may prescribe drugs to relieve the pain of the stitches. Doctors have found that if the baby is given to the mother soon after delivery for skin-to-skin contact, the mother gets so engrossed in her baby that stitching is often done without taking recourse to drugs for suppressing pain. :-)&nbsp;Isn&acirc;&#128;&#153;t that wonderful?</p>\r\n\r\n\r\n\r\n<p>Enjoy those first few moments of breastfeeding, whichever way it comes to you!</p>\r\n\r\n<p>Also read more about: <a href=\"http://app.babychakra.com/article/42\" target=\"_blank\">Do I Need A 'C' Section Answered!</a></p>\r\n\r\n\r\n\r\n<p><br></p>\r\n\r\n\r\n\r\n<p>Source: Book - Guide to Child Care by Dr R K Anand</p>\r\n\r\n\r\n\r\n<p>To consult Dr R K Anand in person, <a href=\"http://app.babychakra.com/service/4591\" target=\"_blank\"></a><a href=\"http://app.babychakra.com/service/4591\" target=\"_blank\">click here</a></p>\r\n\r\n<p>Source for banner image:care.com</p>\n</div>";

        WebView displayVideo = (WebView)findViewById(R.id.webview);

        displayVideo.getSettings().setJavaScriptEnabled(true);
        displayVideo.getSettings().setBuiltInZoomControls(false);
        displayVideo.getSettings().setSupportZoom(false);
        displayVideo.getSettings().setLoadWithOverviewMode(true);

        //articleWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 4);

//        displayVideo.getSettings().setAppCachePath("data/data/" + Util.GetPackageName(this) + "/cache");
        displayVideo.getSettings().setAppCacheEnabled(true);
        displayVideo.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);


        displayVideo.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        WebSettings webSettings = displayVideo.getSettings();
        webSettings.setJavaScriptEnabled(true);
        displayVideo.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http://app.babychakra.com") ||url.startsWith("https://app.babychakra.com") ||url.startsWith("babychakra://")) {

                    //we have intercepted the desired url call
                    Toast.makeText(getApplicationContext(), "HELLOW THERE: " + url, Toast.LENGTH_LONG).show();;
                    return true;
                }

                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        displayVideo.loadData(frameVideo, "text/html", "utf-8");

    }
}
