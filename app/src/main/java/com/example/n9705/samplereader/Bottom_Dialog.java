//底栏布局,三个按键
package com.example.n9705.samplereader;

import android.app.DialogFragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.n9705.samplereader.DataBase.MyOpenHelper;


/**
 * Created by 86758 on 2017/12/12 0012.
 */

public class Bottom_Dialog extends DialogFragment {
    /**
     * log tag for Bottom_Dialog
     */
    private static final String TAG = "Bottom_Dialog";
    public static final String DIALOG_TAG_2 = "dialog2";
    View dialogView;//底栏的对象
    ImageButton bt_adjustBackground,bt_more;
    CheckBox bt_like;
    private Dialog_adjust dialog_adjust;
    private likelistener likelistener;
    public MyOpenHelper mOpenHelper;
    public SQLiteDatabase db;
    private String title;

    public static Bottom_Dialog newInstance() {
        return new Bottom_Dialog();
    }
    public void Init(Dialog_adjust dialog_adjust,String Title1){
        this.dialog_adjust = dialog_adjust;
        this.title=Title1;
    }

    public interface likelistener{
        void check(boolean i);
    }

    public void setlikelistener(Bottom_Dialog.likelistener likelistener){
        this.likelistener = likelistener;
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
        dialogView = inflater.inflate(R.layout.bottom_dialog, container, false);
        //绑定底栏按键
        bt_adjustBackground=dialogView.findViewById(R.id.bt_adjust_background);
        bt_more=dialogView.findViewById(R.id.bt_more);
        bt_like=dialogView.findViewById(R.id.bt_like);

        // 检查是否被收藏过
        // 创建MyOpenHelper实例
        mOpenHelper = new MyOpenHelper(Bottom_Dialog.this.getActivity());
        // 得到数据库
        db = mOpenHelper.getWritableDatabase();
        // 查询数据
        if(Query()){
            bt_like.setChecked(true);
            System.out.println("这篇文章已收藏");
        }

        //点击换背景按钮
        bt_adjustBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //关闭底栏
                startDownAnimation(dialogView);
                //唤醒设置背景字号对话框
                dialog_adjust.show(getFragmentManager(), DIALOG_TAG_2);
            }
        });
        //点击收藏按钮
        bt_like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Toast.makeText(Bottom_Dialog.this.getActivity(), "已收藏", Toast.LENGTH_SHORT).show();
                    likelistener.check(true);

                }
                else{
                    Toast.makeText(Bottom_Dialog.this.getActivity(), "取消收藏", Toast.LENGTH_SHORT).show();
                    likelistener.check(false);
                }
            }
        });
        //点击更多按钮
        bt_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //关闭底栏
                startDownAnimation(dialogView);
                //唤醒更多对话框
                Dialog_more.newInstance().show(getFragmentManager(), DIALOG_TAG_2);
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

    // 查询数据
    public boolean Query() {
        int count=0;
        Cursor cursor = db.query("Article", null, "Title=?", new String[]{title}, null, null, null);
        if(cursor.moveToFirst()){
            do{
               count++;
               System.out.print("这篇文章被收藏过");
            }while (cursor.moveToNext());
        }
        cursor.close();
        if(count!=0){
            return true;
        }
        else{
            return false;
        }
    }
}
