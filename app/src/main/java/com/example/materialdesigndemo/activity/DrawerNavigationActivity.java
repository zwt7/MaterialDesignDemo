package com.example.materialdesigndemo.activity;

import android.os.Bundle;

import com.example.materialdesigndemo.R;
import com.example.materialdesigndemo.adapter.NewsFragmentAdapter;
import com.example.materialdesigndemo.fragment.NewsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;

import java.util.ArrayList;
import java.util.List;

public class DrawerNavigationActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private TabLayout tabs;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_navigation);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "正在发送邮件中...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //抽屉菜单及导航的设置
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        //DrawerLayout初始化，以及设置切换
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        toggle.syncState();;
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawerLayout)
                .build();


        tabs=findViewById(R.id.tabs_layout);
        viewPager=findViewById(R.id.view_pager);
        initViewPager();
    }

    private void initViewPager() {
        //组装tab的标题集合
        List<String> titles=new ArrayList<>();
        titles.add("精选");
        titles.add("新闻");
        titles.add("体育");
        titles.add("购物");
        titles.add("视频");
        titles.add("健康");

        //2.根据tab的个数初始化Fragment的集合
        List<Fragment> fragments=new ArrayList<>();
        for (String title:titles){
            tabs.addTab(tabs.newTab().setText(title));
            fragments.add(NewsFragment.newInstance());
        }

        //3.创建ViewPager的adapter
        NewsFragmentAdapter adapter=new NewsFragmentAdapter(
                getSupportFragmentManager(),fragments,titles
        );

        //4.viewPager设置adapter
        viewPager.setAdapter(adapter);

        //5.关联TabLayout和ViewPager实现联动，关键点:adapter必须重写
        //getPageTitle()方法，滑动fragment
        tabs.setupWithViewPager(viewPager);

    }

    //加载选项菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer_navigation, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
