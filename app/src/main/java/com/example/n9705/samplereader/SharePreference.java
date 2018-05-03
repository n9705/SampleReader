//存用户数据

package com.example.n9705.samplereader;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 86758 on 2017/11/14 0014.
 */


public class SharePreference {

    public Context context;
    public SharePreference(Context context)
    {
        this.context = context;
    }

    /****设置是否首次登录状态  false为安装后第一次登录，true为已经登录过****/
    public void setState()
    {
        SharedPreferences sp = context.getSharedPreferences("first_login_save", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin", true);
        editor.commit();
    }
    /***获取是否首次登录状态***/
    public boolean getState()
    {
        SharedPreferences sp = context.getSharedPreferences("first_login_save", Context.MODE_PRIVATE);
        boolean b = sp.getBoolean("isLogin", false);
        return b;
    }

    //设置背景颜色的单选框为真
    public void setWhiteTrue(){
        SharedPreferences sp = context.getSharedPreferences("tags_save", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("WhiteChecked", true);
        editor.commit();
    }
    public void setGreenTrue(){
        SharedPreferences sp = context.getSharedPreferences("tags_save", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("GreenChecked", true);
        editor.commit();
    }
    public void setYellowTrue(){
        SharedPreferences sp = context.getSharedPreferences("tags_save", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("YellowChecked", true);
        editor.commit();
    }
    public void setPinkTrue(){
        SharedPreferences sp = context.getSharedPreferences("tags_save", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("PinkChecked", true);
        editor.commit();
    }
    //设置夜间模式开关为真
    public void setNightTrue(){
        SharedPreferences sp = context.getSharedPreferences("tags_save", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("NightChecked", true);
        editor.commit();
    }
    //设置字号
    public void setSize(int i){
        SharedPreferences sp = context.getSharedPreferences("tags_save", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("Size",i);
        editor.commit();
    }
    public void setLikeTrue(){
        SharedPreferences sp = context.getSharedPreferences("tags_save", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("LikeChecked", true);
        editor.commit();
    }
    
    //设置背景颜色的单选框为假
    public void setWhiteFalse(){
        SharedPreferences sp = context.getSharedPreferences("tags_save", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("WhiteChecked", false);
        editor.commit();
    }
    public void setGreenFalse(){
        SharedPreferences sp = context.getSharedPreferences("tags_save", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("GreenChecked", false);
        editor.commit();
    }
    public void setYellowFalse(){
        SharedPreferences sp = context.getSharedPreferences("tags_save", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("YellowChecked", false);
        editor.commit();
    }
    public void setPinkFalse(){
        SharedPreferences sp = context.getSharedPreferences("tags_save", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("PinkChecked", false);
        editor.commit();
    }
    //设置夜间模式开关为真
    public void setNightFalse(){
        SharedPreferences sp = context.getSharedPreferences("tags_save", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("NightChecked", false);
        editor.commit();
    }
    public void setLikeFlase(){
        SharedPreferences sp = context.getSharedPreferences("tags_save", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("LikeChecked", false);
        editor.commit();
    }

    /***获取状态***/
    //获取背景颜色
    public boolean getWhite()
    {
        SharedPreferences sp = context.getSharedPreferences("tags_save", Context.MODE_PRIVATE);
        boolean b = sp.getBoolean("WhiteChecked", false);
        return b;
    }
    public boolean getGreen()
    {
        SharedPreferences sp = context.getSharedPreferences("tags_save", Context.MODE_PRIVATE);
        boolean b = sp.getBoolean("GreenChecked", false);
        return b;
    }
    public boolean getYellow()
    {
        SharedPreferences sp = context.getSharedPreferences("tags_save", Context.MODE_PRIVATE);
        boolean b = sp.getBoolean("YellowChecked", false);
        return b;
    }
    public boolean getPink()
    {
        SharedPreferences sp = context.getSharedPreferences("tags_save", Context.MODE_PRIVATE);
        boolean b = sp.getBoolean("PinkChecked", false);
        return b;
    }
    //获取夜间模式开关状态
    public boolean getNight()
    {
        SharedPreferences sp = context.getSharedPreferences("tags_save", Context.MODE_PRIVATE);
        boolean b = sp.getBoolean("NightChecked", false);
        return b;
    }
    //获取字号大小
    public int getSize(){
        SharedPreferences sp = context.getSharedPreferences("tags_save", Context.MODE_PRIVATE);
        int b=sp.getInt("Size",1);
        return b;
    }
    public boolean getLike()
    {
        SharedPreferences sp = context.getSharedPreferences("tags_save", Context.MODE_PRIVATE);
        boolean b = sp.getBoolean("LikeChecked", false);
        return b;
    }

}
