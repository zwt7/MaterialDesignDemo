package com.example.materialdesigndemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.materialdesigndemo.R;
import com.example.materialdesigndemo.model.News;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<News> newsList;
    private Context context;

    public NewsAdapter(List<News> newsList){
        this.newsList=newsList;
    }
    //3.将那个布局拿来
    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news,parent,false);
        return new NewsViewHolder(view);
        //把Item的布局拿来
    }
    //4
    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        //绑定数据，都是String类型的
         News news=newsList.get(position);
        holder.itemView.setTag(position);
        holder.tvTitle.setText(news.getTitle());
        holder.tvTime.setText(new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.CHINA).format(news.getDate()));
        holder.tvAuthor.setText(news.getAuthorName());
        Glide.with(context).load(news.getPic()).into(holder.ivPic);

        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.OnItemClick(v,(Integer) v.getTag());
                }
            }
        });
    }

    //2.先计算她的长度
    @Override
    public int getItemCount() {
        return newsList.size();
    }
    //3。
    static class NewsViewHolder extends  RecyclerView.ViewHolder{
        //
        TextView tvTitle,tvAuthor,tvTime;
        ImageView ivPic;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            //在这个里面Item的初始化
            tvTitle=itemView.findViewById(R.id.tv_title);
            tvTime=itemView.findViewById(R.id.tv_date);
            tvAuthor=itemView.findViewById(R.id.tv_author);
            ivPic= itemView.findViewById(R.id.img_pic);
        }
    }
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }

    public interface OnItemClickListener{
        void OnItemClick(View view,int position);
    }

}
