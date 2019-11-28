package com.example.materialdesigndemo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.materialdesigndemo.R;
import com.example.materialdesigndemo.adapter.NewsAdapter;
import com.example.materialdesigndemo.model.News;
import com.example.materialdesigndemo.utils.HttpsUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    //常量:聚合数据的url网址和handle的消息
    private static final String URL_NEWS="https://v.juhe.cn/toutiao/index";
    private static final int GET_NEWS=1;
    private static final int GET_NEWS_ERROR=2;
    //数据处理的对象
    private List<News> newsList;
    private NewsHandler handler;
    private NewsAdapter adapter;

    private Toolbar toolbar;
    private RecyclerView reNews;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        handler=new NewsHandler(this);

        initView();
        initData();
    }

    private void initView() {
        //初始化toolbar
        toolbar=findViewById(R.id.tool_bar);
        toolbar.setTitle("RecycleView示例");
        setSupportActionBar(toolbar);

        //2.初始化RecyleView列表和自动刷新布局
        reNews=findViewById(R.id.rv_news);
        refreshLayout=findViewById(R.id.refresh);


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
                else {
                    Log.e("RecyclerViewActivity",response.message());
                }
            }

        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category_layout,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_LinearLayout:
                reNews.setLayoutManager(new LinearLayoutManager(this));
                adapter=new NewsAdapter(newsList);
                reNews.setAdapter(adapter);
                break;
            case R.id.item_GridLayout:
                break;
            case R.id.item_StaggeredGridLayout:
                break;
        }
        return super.onOptionsItemSelected(item);
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

    static class NewsHandler extends Handler{
        private WeakReference<Activity> ref;

        public NewsHandler(Activity activity){
            this.ref=new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final RecyclerViewActivity activity=(RecyclerViewActivity) this.ref.get();
            if(msg.what==GET_NEWS){
                //获取数据
                String json= (String) msg.obj;
                activity.newsList=JSON.parseArray(json, News.class);
                //设置RecycleItemView的分割线和布局
                activity.reNews.setLayoutManager(new LinearLayoutManager(activity));

                //设置Adapter
                activity.adapter=new NewsAdapter(activity.newsList);
                activity.reNews.setAdapter(activity.adapter);

                //设置事件监听
                activity.adapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {
                        //传值过去
                        Intent intent=new Intent(activity,NewsDetailActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("news",activity.newsList.get(position));
                        intent.putExtras(bundle);
                        activity.startActivity(intent);
                    }
                });
            }
        }
    }

}
