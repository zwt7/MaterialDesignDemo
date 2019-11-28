package com.example.materialdesigndemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.materialdesigndemo.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button RecycleView,TabLayout,Navigation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar=findViewById(R.id.tool_bar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Material Design控件使用");


        RecycleView=findViewById(R.id.bt_recycle);
        TabLayout=findViewById(R.id.bt_TabLayout);
        Navigation=findViewById(R.id.bt_Navigation);
        RecycleView.setOnClickListener(this);
        TabLayout.setOnClickListener(this);
        Navigation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.bt_recycle:
                intent=new Intent(MainActivity.this,RecyclerViewActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_TabLayout:
                intent=new Intent(MainActivity.this,TabLayoutActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_Navigation:
                intent=new Intent(MainActivity.this,DrawerNavigationActivity.class);
                startActivity(intent);
                break;
        }
    }
}
