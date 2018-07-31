//更多菜单，就是底栏第三个键点击出现的菜单
package com.example.n9705.samplereader;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

/**
 * Created by 86758 on 2017/12/13 0013.
 */

public class Dialog_more extends DialogFragment {
    /**
     * log tag for Bottom_Dialog
     */
    private static final String TAG = "Dialog_more";
    View dialogView;//底栏的对象
    public Button bt_suggest,bt_favorite;

    public static Dialog_more newInstance() {
        return new Dialog_more();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM; // 显示在底部
        params.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度填充满屏
        window.setAttributes(params);
        // 这里用透明颜色替换掉系统自带背景
        int color = ContextCompat.getColor(getActivity(), android.R.color.transparent);
        window.setBackgroundDrawable(new ColorDrawable(color));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE); // 不显示标题栏
        dialogView = inflater.inflate(R.layout.more_dialog, container, false);
        bt_suggest=dialogView.findViewById(R.id.suggest);
        bt_favorite=dialogView.findViewById(R.id.favorite);

        //点击“意见反馈”按钮
        bt_suggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDownAnimation(view);
                Intent intent=new Intent(Dialog_more.this.getActivity(),SuggestActivity.class);
                startActivity(intent);
            }
        });
        //点击“收藏夹”按钮
        bt_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDownAnimation(view);
                Intent intent=new Intent(getActivity(),FavoriteActivity.class);
                getActivity().startActivityForResult(intent,1);
            }
        });
        //开启动画
        startUpAnimation(dialogView);
        return dialogView;
    }
    //开启动画
    private void startUpAnimation(View view) {
        Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        slide.setDuration(400);//动画时间
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        view.startAnimation(slide);
    }
    //关闭动画
    private void startDownAnimation(View view) {
        Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        slide.setDuration(400);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        slide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        view.startAnimation(slide);
    }
}
