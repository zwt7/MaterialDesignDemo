package com.example.materialdesigndemo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.materialdesigndemo.R;
import com.example.materialdesigndemo.activity.NewsDetailActivity;
import com.example.materialdesigndemo.activity.RecyclerViewActivity;
import com.example.materialdesigndemo.adapter.NewsAdapter;
import com.example.materialdesigndemo.model.News;
import com.example.materialdesigndemo.utils.HttpsUtil;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;


public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    //常量:聚合数据的url网址和handle的消息
    private static final String URL_NEWS="https://v.juhe.cn/toutiao/index";
    private static final int GET_NEWS=1;
    private static final int GET_NEWS_ERROR=2;
    //数据处理的对象
    private List<News> newsList;
    private NewsHandler handler;
    private NewsAdapter adapter;

    private RecyclerView reNews;
    private SwipeRefreshLayout refreshLayout;

    public NewsFragment() {
        // Required empty public constructor
    }

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_news, container, false);

        handler=new NewsHandler(this);

        initView(view);
        initData();
        return view;
    }
    private void initView(View view) {

        //2.初始化RecyleView列表和自动刷新布局
        reNews=view.findViewById(R.id.rv_news);
        refreshLayout=view.findViewById(R.id.refresh);


        //设置SwipeRefreshLayout的事件监听和进度条颜色
        refreshLayout.setOnRefreshListener(this);
    }

    private void initData() {
        String appKey="70de812abe5826690299251ad3de77df";
        String url=URL_NEWS+"?key="+appKey+"&type=top";
        final Request request=new Request.Builder().url(url).build();
        HttpsUtil.getInstance().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("NewListActivity",e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){

                    String json=response.body().string();
                    JSONObject obj= JSON.parseObject(json);
                    JSONObject result=obj.getJSONObject("result");
                    if(result!=null){
                        JSONArray data=result.getJSONArray("data");
                        if(data!=null&&!data.isEmpty()){
                            Message msg=handler.obtainMessage();
                            msg.what=GET_NEWS;
                            msg.obj=data.toJSONString();//字符串
                            handler.sendMessage(msg);
                        }
                    }
                    else{
                        Message msg=handler.obtainMessage();
                        msg.what=GET_NEWS_ERROR;
                        msg.obj=obj.getString("reason");
                        handler.sendMessage(msg);
                    }
                }
                else{
                    Log.e("NewsFragment",response.message());
                }
            }

        });
    }
    @Override
    public void onRefresh() {
        initData();
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        },3000);

    }

    static class NewsHandler extends Handler {
        private WeakReference<Fragment> ref;

        public NewsHandler(Fragment fragment){
            this.ref=new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            final NewsFragment fragment=(NewsFragment) this.ref.get();
            if(msg.what==GET_NEWS){
                //获取数据
                String json= (String) msg.obj;
                fragment.newsList=JSON.parseArray(json, News.class);
                //设置RecycleItemView的分割线和布局
                fragment.reNews.setLayoutManager(new LinearLayoutManager(fragment.getContext()));

                //设置Adapter
                fragment.adapter=new NewsAdapter(fragment.newsList);
                fragment.reNews.setAdapter(fragment.adapter);

                //设置事件监听
                fragment.adapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {
                        //传值过去
                        Intent intent=new Intent(fragment.getContext(), NewsDetailActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("news",fragment.newsList.get(position));
                        intent.putExtras(bundle);
                        fragment.startActivity(intent);
                    }
                });
            }
            else if(msg.what==GET_NEWS_ERROR){
                String reason= (String) msg.obj;
                if(fragment.getView()!=null){
                    Snackbar.make(fragment.getView(),reason,Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }
}
