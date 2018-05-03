package com.example.n9705.samplereader;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.security.GeneralSecurityException;

import javax.mail.MessagingException;


public class SuggestActivity extends AppCompatActivity {

    //控件声明
    public Toolbar toolbar1;
    public TextView SendButton,suggestBarText;
    public EditText editText;
    public ActionBar actionBar;
    public String message;
    private Handler handler=null;
    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        SendButton=findViewById(R.id.Send);
        editText=findViewById(R.id.SuggestText);
        progressBar=findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);
        suggestBarText=findViewById(R.id.SuggestBarTextview);
        toolbar1=findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar1);//toolbar绑定为actionbar
        actionBar = getSupportActionBar();//启用toolbar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//把返回键显示出来
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
            toolbar1.setTitleTextColor(this.getResources().getColor(R.color.toolbar_back));
        }
        suggestBarText.setText("意见反馈");
        //提交按钮
        SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message=editText.getText().toString();
                if(message.length()==0){//如果没写建议就发送
                    Toast.makeText(SuggestActivity.this, "反馈不能为空", Toast.LENGTH_SHORT).show();
                }
                else{ //把意见发送给服务器
                    //显示进度条
                    suggestBarText.setText("");
                    progressBar.setVisibility(View.VISIBLE);
                    //创建属于主线程的handler
                    handler=new Handler();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.print("发送邮件中....");
                            try {
                                MailTool.sendMail(message);
                            } catch (MessagingException e) {
                                e.printStackTrace();
                            } catch (GeneralSecurityException e) {
                                e.printStackTrace();
                            }
                            handler.post(runnableToast);
                            System.out.print("发送完毕");
                        }
                    }).start();
                }
            }
        });
        //返回键
        toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // 构建Runnable对象，在runnable中更新界面
    Runnable runnableToast=new Runnable(){
        @Override
        public void run() {
            //更新界面
            progressBar.setVisibility(View.INVISIBLE);
            suggestBarText.setText("意见反馈");
            Toast.makeText(SuggestActivity.this, "反馈成功!", Toast.LENGTH_SHORT).show();
            finish();
        }
    };

}
