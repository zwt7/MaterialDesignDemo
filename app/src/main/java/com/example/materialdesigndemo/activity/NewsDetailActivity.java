package com.example.materialdesigndemo.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.materialdesigndemo.R;
import com.example.materialdesigndemo.model.News;

public class NewsDetailActivity extends AppCompatActivity {
 //主要就是toolbar的设置，返回的箭头

    private WebView wvNews;
    private WebSettings settings;

    private Toolbar toolbar;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        //设置标题栏回退按钮，及点击事件
        toolbar=findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsDetailActivity.this.finish();
            }
        });

        wvNews=findViewById(R.id.wv_news_detail);
        //接收到来自RecyclerViewActivity的数据
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null){
            final News news= (News) bundle.get("news");
            wvNews.loadUrl(news.getUrl());
            settings=wvNews.getSettings();
            settings.setBuiltInZoomControls(true);
            settings.setJavaScriptEnabled(true);
            settings.setBlockNetworkImage(false);//解决图片不能加载
            settings.setUseWideViewPort(true);//将图片调整到合适webView
            settings.setLoadWithOverviewMode(true);//缩放至屏幕的大小
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            //解决http和https混合问题

            //设置网页在webView打开
            wvNews.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    wvNews.loadUrl(news.getUrl());
                    return true;
                }
                //解决https图片不能加载

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                  handler.proceed();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        if(wvNews!=null){
            wvNews.loadDataWithBaseURL(null,"","text/html","utf-8",null);
            wvNews.clearHistory();
            ((ViewGroup)wvNews.getParent()).removeView(wvNews);
            wvNews.destroy();;
            wvNews=null;
        }
        super.onDestroy();
    }
}
