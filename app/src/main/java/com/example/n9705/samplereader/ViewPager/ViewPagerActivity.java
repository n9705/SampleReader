package com.example.n9705.samplereader.ViewPager;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.n9705.samplereader.MainActivity;
import com.example.n9705.samplereader.R;
import com.example.n9705.samplereader.SharePreference;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends AppCompatActivity {

    private ViewPager mIn_vp;
    private LinearLayout mIn_ll;
    private List<View> mViewList;
    private ImageView mLight_dots;
    private int mDistance;
    private ImageView mOne_dot;
    private ImageView mTwo_dot;
    private ImageView mThree_dot;
    private ImageView mFour_dot;
    private ImageView mFive_dot;
    private TextView mBtn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
        setContentView(R.layout.viewpager_activity);
        initView();
        initData();
        mIn_vp.setAdapter(new ViewPagerAdatper(mViewList));
        addDots();
        moveDots();
        mIn_vp.setPageTransformer(true,new DepthPageTransformer());
    }

    private void moveDots() {
        mLight_dots.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //获得两个圆点之间的距离
                mDistance = mIn_ll.getChildAt(1).getLeft() - mIn_ll.getChildAt(0).getLeft();
                mLight_dots.getViewTreeObserver()
                        .removeGlobalOnLayoutListener(this);
            }
        });
        mIn_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //页面滚动时小白点移动的距离，并通过setLayoutParams(params)不断更新其位置
                float leftMargin = mDistance * (position + positionOffset);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLight_dots.getLayoutParams();
                params.leftMargin = (int) leftMargin;
                mLight_dots.setLayoutParams(params);
                if(position==4){
                    mBtn_next.setVisibility(View.VISIBLE);
                }
                if(position!=4&&mBtn_next.getVisibility()==View.VISIBLE){
                    mBtn_next.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageSelected(int position) {
                //页面跳转时，设置小圆点的margin
                float leftMargin = mDistance * position;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLight_dots.getLayoutParams();
                params.leftMargin = (int) leftMargin;
                mLight_dots.setLayoutParams(params);
                if(position==4){
                    mBtn_next.setVisibility(View.VISIBLE);
                }
                if(position!=4&&mBtn_next.getVisibility()==View.VISIBLE){
                    mBtn_next.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void addDots() {
        mOne_dot = new ImageView(this);
        mOne_dot.setImageResource(R.drawable.gray_dot);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 40, 0);
        mIn_ll.addView(mOne_dot, layoutParams);
        mTwo_dot = new ImageView(this);
        mTwo_dot.setImageResource(R.drawable.gray_dot);
        mIn_ll.addView(mTwo_dot, layoutParams);
        mThree_dot = new ImageView(this);
        mThree_dot.setImageResource(R.drawable.gray_dot);
        mIn_ll.addView(mThree_dot, layoutParams);
        mFour_dot = new ImageView(this);
        mFour_dot.setImageResource(R.drawable.gray_dot);
        mIn_ll.addView(mFour_dot, layoutParams);
        mFive_dot = new ImageView(this);
        mFive_dot.setImageResource(R.drawable.gray_dot);
        mIn_ll.addView(mFive_dot, layoutParams);
        setClickListener();
    }

    private void setClickListener() {
        mOne_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIn_vp.setCurrentItem(0);
            }
        });
        mTwo_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIn_vp.setCurrentItem(1);
            }
        });
        mThree_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIn_vp.setCurrentItem(2);
            }
        });
        mFour_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIn_vp.setCurrentItem(3);
            }
        });
        mFive_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIn_vp.setCurrentItem(4);
            }
        });
        mBtn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePreference sp=new SharePreference(ViewPagerActivity.this);
                sp.setState();
                Intent intent=new Intent(ViewPagerActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }


    private void initData() {
        mViewList = new ArrayList<View>();
        LayoutInflater lf = getLayoutInflater().from(ViewPagerActivity.this);
        View view1 = lf.inflate(R.layout.we_indicator1, null);
        View view2 = lf.inflate(R.layout.we_indicator2, null);
        View view3 = lf.inflate(R.layout.we_indicator3, null);
        View view4 = lf.inflate(R.layout.we_indicator4, null);
        View view5 = lf.inflate(R.layout.we_indicator5, null);
        mViewList.add(view1);
        mViewList.add(view2);
        mViewList.add(view3);
        mViewList.add(view4);
        mViewList.add(view5);
    }

    private void initView() {
        mIn_vp = findViewById(R.id.in_viewpager);
        mIn_ll = findViewById(R.id.in_ll);
        mLight_dots = findViewById(R.id.iv_light_dots);
        mBtn_next = findViewById(R.id.NextTo);
    }
}
