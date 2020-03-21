package com.example.accountingapp;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private static final String TAG = "MainActivity";

    private ViewPager viewPager;
    private MainViewPagerAdapter pagerAdapter;
    private TickerView amountText;
    private TextView dateText;
    private int curPagerPosition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GlobalUtil.getInstance().setContext(getApplicationContext());
        GlobalUtil.getInstance().mainActivity = this;
        getSupportActionBar().setElevation(0);
        amountText = (TickerView) findViewById(R.id.amount_text); // 一定要在viewpager调用onPageSelected之前初始化
        amountText.setCharacterLists(TickerUtils.provideNumberList());
        dateText = (TextView) findViewById(R.id.date_text);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        pagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(pagerAdapter);

        viewPager.setOnPageChangeListener(this);

        viewPager.setCurrentItem(pagerAdapter.getLastIndex());

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddRecordActivity.class);
                startActivityForResult(intent,1); //每次退出second activity，更新main activity view, requestCode 用于区分不同results
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { //每次退出second activity都会被执行
        super.onActivityResult(requestCode, resultCode, data);
        pagerAdapter.reload(); //reload需要自行填充adapter
        updateHeader();
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    public void updateHeader(){
        String amount = String.valueOf(pagerAdapter.getTotalCost(curPagerPosition)) + " ";
        amountText.setText(amount);
        String date = pagerAdapter.getDateStr(curPagerPosition);
        dateText.setText(DateUtil.getWeekDay(date));
    }

    @Override
    public void onPageSelected(int i) { //每次滑动pager被调用
        curPagerPosition = i;
        updateHeader();
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
