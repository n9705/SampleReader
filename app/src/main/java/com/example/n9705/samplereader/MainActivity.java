package com.example.n9705.samplereader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.listener.OnGuideChangedListener;
import com.app.hubert.guide.listener.OnLayoutInflatedListener;
import com.app.hubert.guide.listener.OnPageChangedListener;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.app.hubert.guide.model.HighlightOptions;
import com.app.hubert.guide.model.RelativeGuide;
import com.example.n9705.samplereader.AdvancedTextview.ActionMenu;
import com.example.n9705.samplereader.AdvancedTextview.CustomActionMenuCallBack;
import com.example.n9705.samplereader.AdvancedTextview.SelectableTextView;
import com.example.n9705.samplereader.DataBase.Articles_Dao;
import com.example.n9705.samplereader.DataBase.MyOpenHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Vector;

import es.dmoral.toasty.Toasty;

import static com.example.n9705.samplereader.Bottom_Dialog.DIALOG_TAG_2;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,CustomActionMenuCallBack {

    private static final String TAG ="";
    public Toolbar toolbar;
    private TextView textTitle, textAuthor, barTitle, textFinish;
    private SelectableTextView textView;
    private ScrollView scrollView;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;
    public String Text="",Title="",time="",Author="";
    private Handler handler=null;
    public int color=0;
    MyOpenHelper mOpenHelper;
    SQLiteDatabase db;
    Vector<Thread> vectors=new Vector<Thread>();

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
        //调用布局
        setContentView(R.layout.activity_main);
        initView();
        textView.setOnClickListener(this);
        toolbar.setOnClickListener(this);
        setSupportActionBar(toolbar);
        //初始化，包括读取保存的状态，获取新的文章
        handler=new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.print("检查是否有网络连接中...");
                if(!NetCheck.isNetworkAvailable(MainActivity.this)){
                    handler.post(runnableToast);
                }
                System.out.print("检查完毕");
            }
        }).start();
        init();
        SharePreference sp=new SharePreference(MainActivity.this);
        boolean isLogin = sp.getState();
        if(!isLogin){//第一次启动
            //显示引导页面
            guidePager();
            sp.setState();
        }
        // 创建MyOpenHelper实例
        mOpenHelper = new MyOpenHelper(this);
        // 得到数据库
        db = mOpenHelper.getWritableDatabase();
    }

    //绑定控件
    public void initView(){
        scrollView = findViewById(R.id.scrollView);
        linearLayout = findViewById(R.id.linearlayout);
        progressBar = findViewById(R.id.progressBar);
        barTitle = findViewById(R.id.barTitle);
        textTitle = findViewById(R.id.textTitle);
        textAuthor = findViewById(R.id.textAuthor);
        textFinish = findViewById(R.id.textFinish);
        textView = findViewById(R.id.textView);
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //创建属于主线程的handler
        handler=new Handler();
        //OnTouch监听器,可以自动隐藏和显示标题，酷的一批
        scrollView.setOnTouchListener(new PicOnTouchListener());
    }

    private void guidePager(){
        Animation enterAnimation = new AlphaAnimation(0f, 1f);
        enterAnimation.setDuration(400);
        enterAnimation.setFillAfter(true);

        Animation exitAnimation = new AlphaAnimation(1f, 0f);
        exitAnimation.setDuration(400);
        exitAnimation.setFillAfter(true);

        //新增多页模式，即一个引导层显示多页引导内容
        NewbieGuide.with(this)
                .setLabel("page")//设置引导层标示区分不同引导层，必传！否则报错
//                .anchor(anchor)
                .setOnGuideChangedListener(new OnGuideChangedListener() {
                    @Override
                    public void onShowed(Controller controller) {
                        Log.e(TAG, "NewbieGuide onShowed: ");
                        //引导层显示
                    }

                    @Override
                    public void onRemoved(Controller controller) {
                        Log.e(TAG, "NewbieGuide  onRemoved: ");
                        //引导层消失（多页切换不会触发）
                    }
                })
                .alwaysShow(false)//是否每次都显示引导层，默认false，只显示一次
                .addGuidePage(GuidePage.newInstance() //添加引导页
                        .addHighLight(toolbar,HighLight.Shape.CIRCLE,-550)
                        .setLayoutRes(R.layout.guide_view_1)
                        .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                            @Override
                            public void onLayoutInflated(View view, final Controller controller) {
                                view.findViewById(R.id.guideNext1).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        controller.showPage(1);
                                    }
                                });
                            }
                        })
                        .setEnterAnimation(enterAnimation)//进入动画
                        .setExitAnimation(exitAnimation)//退出动画
                )
                .addGuidePage(GuidePage.newInstance() //添加引导页
                        .addHighLight(textView,HighLight.Shape.CIRCLE,-1500)
                        .setLayoutRes(R.layout.guide_view_2)
                        .setEnterAnimation(enterAnimation)//进入动画
                        .setExitAnimation(exitAnimation)//退出动画
                )
                .addGuidePage(GuidePage.newInstance() //添加引导页
                        .addHighLight(textView,HighLight.Shape.CIRCLE,-1500)
                        .setLayoutRes(R.layout.guide_view_3)
                        .setEnterAnimation(enterAnimation)//进入动画
                        .setExitAnimation(exitAnimation)//退出动画
                )
                .show();//显示引导层(至少需要一页引导页才能显示)
    }

    //获取文章
    private int flag=0;//用来判断是不是打开后第一次刷新
    private String url="";
    private void getArticle() {
        //显示进度条
        progressBar.setVisibility(View.VISIBLE);
        //初始化
        Text=Author="";
        //开一个线程来获取文章
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //System.out.print("线程的名称是: "+Thread.currentThread().getName());
                    if(flag==0){//如果是打开后第一次刷新，获取最新的文章链接
                        url="https://meiriyiwen.com/";
                        flag=1;
                    }
                    else{//如果不是的话，那随机
                        url="https://meiriyiwen.com/random";
                    }
                    Log.i("URL", url);
                    Document doc= Jsoup.connect(url).get();
                   try {
                       System.out.println("开始解析文章");
                       Element element1=doc.getElementById("article_show").getElementsByTag("h1").get(0);
                       Title=element1.text();
                       Log.i("Title", Title);
                       Element element2=doc.getElementById("article_show").getElementsByTag("p").get(0).getElementsByTag("span").get(0);
                       Author=element2.text();
                       Log.i("Author", Author);
                       Elements elements=doc.getElementsByClass("article_text").get(0).getElementsByTag("p");
                       Text="\u3000\u3000";
                       for(int i=1;i<elements.size();i++){
                           Text=Text+elements.get(i).text();
                           if(i!=elements.size()-1){
                               Text=Text+"\n\u3000\u3000";
                           }
                           else{
                               Text=Text+"\0";//最后一段
                           }
                       }
                       Log.i("Text", Text);
                       //成功获取文章
                       //在子线程的run方法中向UI线程post，runnable对象来更新UI
                       handler.post(runnableUi);
                   }catch (Exception e){
                       Log.i("TAG", "文章解析错误");
                       e.printStackTrace();
                   }
                } catch (Exception e) {
                    Log.i("TAG", "无法访问url");
                    e.printStackTrace();
                    //handler.post(runnableToast);
                }
            }
        }).start();
    }

    // 构建Runnable对象，在runnable中更新界面
    Runnable runnableUi=new Runnable(){
        @Override
        public void run() {
            //更新界面
            barTitle.setText("");
            progressBar.setVisibility(View.INVISIBLE);
            textTitle.setText(Title);//显示正文标题
            textAuthor.setText(Author);//显示作者
            textView.setText(Text);//显示文章内容
            textView.clearFocus();
            textView.setTextJustify(true);// 是否启用两端对齐 默认启用
            textView.setForbiddenActionMenu(false);// 是否禁用自定义ActionMenu 默认启用
            textView.postInvalidate();
            textFinish.setText("全文完");
            SharePreference sp = new SharePreference(MainActivity.this);
            sp.setLikeFlase();
        }
    };
    // 构建Runnable对象，在runnable中更新界面
    Runnable runnableToast=new Runnable(){
        @Override
        public void run() {
            //更新界面
            progressBar.setVisibility(View.INVISIBLE);
            barTitle.setText(Title);
            Toast.makeText(MainActivity.this, "无网络连接", Toast.LENGTH_SHORT).show();
            if(Title==""){
                textTitle.setText("无网络连接");
            }
        }
    };

    //OnTouch监听器,监听scrollview的滑动，让标题选择显示
    private class PicOnTouchListener implements View.OnTouchListener {
        private int lastY = 0;
        private int touchEventId = -9983761;
        int[] position=new int[2];
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int eventAction = event.getAction();
            switch (eventAction) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    textAuthor.getLocationOnScreen(position);
                    if(position[1]<=150){//作者不在屏幕上
                        barTitle.setText(Title);
                    }
                    else{//作者在屏幕上
                        barTitle.setText("");
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    //惯性滑动，每隔1ms监听一次
                    handler.sendMessageDelayed(handler.obtainMessage(touchEventId, v), 1);
                    break;
                default:
                    break;
            }
            return false;
        }
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                View scroller = (View) msg.obj;
                if (msg.what == touchEventId) {
                    if (lastY != scroller.getScrollY()) {//窗口惯性滑动未停止
                        textAuthor.getLocationOnScreen(position);
                        if(position[1]<=150){//作者不在屏幕上
                            barTitle.setText(Title);
                        }
                        else{//作者在屏幕上
                            barTitle.setText("");
                        }
                        handler.sendMessageDelayed(handler.obtainMessage(touchEventId, scroller), 1);
                        lastY = scroller.getScrollY();
                    }
                }
            }
        };
    }

    //toolbar和textview点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                //获取文章
                barTitle.setText("");
                getArticle();
                //返回顶部
                scrollView.smoothScrollTo(0, 0);
                break;
            case R.id.textView:
                //正文点击事件，调出底栏
                Dialog_adjust dialog_adjust = Dialog_adjust.newInstance();
                dialog_adjust.setXxxlistener(new Dialog_adjust.xxxlistener() {
                    @Override
                    public void test(int i) {
                        setsize(i);
                    }
                    public void color(int i) {
                        setColor(i);
                    }
                });
                Bottom_Dialog bottom_dialog = Bottom_Dialog.newInstance();
                bottom_dialog.Init(dialog_adjust,Title);
                //收藏按钮监听
                bottom_dialog.setlikelistener(new Bottom_Dialog.likelistener() {
                    SharePreference sp = new SharePreference(MainActivity.this);
                    @Override
                    public void check(boolean i) {
                        if (i) {
                            //save article
                            sp.setLikeTrue();
                            Article article = new Article(Title,Author,Text);
                            Articles_Dao articles_dao = new Articles_Dao(MainActivity.this);
                            System.out.println("收藏，加入数据库");
                            articles_dao.addFavorite(article);
                        } else {
                            //delete article
                            sp.setLikeFlase();
                            Article article = new Article(Title,Author,Text);
                            Articles_Dao articles_dao = new Articles_Dao(MainActivity.this);
                            System.out.println("取消收藏");
                            articles_dao.deleteFavorite(article);
                        }
                    }
                });
                bottom_dialog.show(getFragmentManager(), DIALOG_TAG_2);
                break;
            default:
                break;
        }
    }


    //接收收藏夹返回的数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if (resultCode == RESULT_OK) {
                //返回顶部
                scrollView.smoothScrollTo(0, 0);
                Bundle bundle = data.getExtras();
                Article article = (Article) bundle.getSerializable("article");
                SharePreference sp = new SharePreference(MainActivity.this);
                sp.setLikeTrue();
                barTitle.setText("");
                Title=article.getTitle();
                Author=article.getAuthor();
                Text=article.getText();
                textTitle.setText(Title);//显示正文标题
                textAuthor.setText(Author);//显示作者
                textView.setText(Text);//显示文章内容
                int i=sp.getSize();//获取字号
                setsize(i);//设置字体大小
                textView.clearFocus();
                textView.setTextJustify(true);// 是否启用两端对齐 默认启用
                textView.setForbiddenActionMenu(false);// 是否禁用自定义ActionMenu 默认启用
                textView.postInvalidate();
            }
        }
    }

    @Override
    public boolean onCreateCustomActionMenu(ActionMenu menu) {
        menu.setActionMenuBgColor(0xff666666);                    // ActionMenu背景色
        menu.setMenuItemTextColor(0xffffffff);                   // ActionMenu文字颜色
        return false;                                            // 返回false，保留默认菜单(全选/复制)；返回true，移除默认菜单
    }

    @Override
    public void onCustomActionItemClicked(String itemTitle, String selectedContent) {
        Toasty.success(this, "ActionMenu: " + itemTitle, Toast.LENGTH_SHORT,true).show();
    }

    //初始化界面样式
    public void init(){
        textFinish.setText("");
        SharePreference sp = new SharePreference(MainActivity.this);
        //设置文字和背景颜色
        if(sp.getNight()){
            color=4;
        }
        else{
            if(sp.getWhite()){
                color=0;
            }
            if(sp.getGreen()){
                color=1;
            }
            if(sp.getYellow()){
                color=2;
            }
            if(sp.getPink()){
                color=3;
            }
        }
        setTextColor(color);
//        //设置toolbar刷新按钮样式
//        getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
//        invalidateOptionsMenu();
        //初始化的时候顺便获取文章
        getArticle();
        //初始化文字大小
        int i=sp.getSize();//获取字号
        setsize(i);
    }

    //设置字体背景颜色
    public void setTextColor(int t){
        if(t==0){
            textFinish.setBackgroundColor(this.getResources().getColor(R.color.whiteBack));
            textFinish.setTextColor(this.getResources().getColor(R.color.whiteFont));
            barTitle.setTextColor(this.getResources().getColor(R.color.whiteFont));
            textTitle.setBackgroundColor(this.getResources().getColor(R.color.whiteBack));
            textTitle.setTextColor(this.getResources().getColor(R.color.whiteFont));
            textAuthor.setBackgroundColor(this.getResources().getColor(R.color.whiteBack));
            textAuthor.setTextColor(this.getResources().getColor(R.color.whiteFont));
            textView.setBackgroundColor(this.getResources().getColor(R.color.whiteBack));
            textView.setTextColor(this.getResources().getColor(R.color.whiteFont));
            toolbar.setBackgroundColor(this.getResources().getColor(R.color.whiteBack));
            toolbar.setTitleTextColor(this.getResources().getColor(R.color.whiteFont));
            linearLayout.setBackgroundColor(this.getResources().getColor(R.color.whiteBack));
        }
        else if(t==1){
            barTitle.setTextColor(this.getResources().getColor(R.color.greenFont));
            textFinish.setBackgroundColor(this.getResources().getColor(R.color.greenBack));
            textFinish.setTextColor(this.getResources().getColor(R.color.greenFont));
            textTitle.setBackgroundColor(this.getResources().getColor(R.color.greenBack));
            textTitle.setTextColor(this.getResources().getColor(R.color.greenFont));
            textAuthor.setBackgroundColor(this.getResources().getColor(R.color.greenBack));
            textAuthor.setTextColor(this.getResources().getColor(R.color.greenFont));
            textView.setBackgroundColor(this.getResources().getColor(R.color.greenBack));
            textView.setTextColor(this.getResources().getColor(R.color.greenFont));
            toolbar.setBackgroundColor(this.getResources().getColor(R.color.greenBack));
            toolbar.setTitleTextColor(this.getResources().getColor(R.color.greenFont));
            linearLayout.setBackgroundColor(this.getResources().getColor(R.color.greenBack));
        }
        else if(t==2){
            barTitle.setTextColor(this.getResources().getColor(R.color.yellowFont));
            textFinish.setBackgroundColor(this.getResources().getColor(R.color.yellowBack));
            textFinish.setTextColor(this.getResources().getColor(R.color.yellowFont));
            textTitle.setBackgroundColor(this.getResources().getColor(R.color.yellowBack));
            textTitle.setTextColor(this.getResources().getColor(R.color.yellowFont));
            textAuthor.setBackgroundColor(this.getResources().getColor(R.color.yellowBack));
            textAuthor.setTextColor(this.getResources().getColor(R.color.yellowFont));
            textView.setBackgroundColor(this.getResources().getColor(R.color.yellowBack));
            textView.setTextColor(this.getResources().getColor(R.color.yellowFont));
            toolbar.setBackgroundColor(this.getResources().getColor(R.color.yellowBack));
            toolbar.setTitleTextColor(this.getResources().getColor(R.color.yellowFont));
            linearLayout.setBackgroundColor(this.getResources().getColor(R.color.yellowBack));
        }
        else if(t==3){
            barTitle.setTextColor(this.getResources().getColor(R.color.pinkFont));
            textFinish.setBackgroundColor(this.getResources().getColor(R.color.pinkBack));
            textFinish.setTextColor(this.getResources().getColor(R.color.pinkFont));
            textTitle.setBackgroundColor(this.getResources().getColor(R.color.pinkBack));
            textTitle.setTextColor(this.getResources().getColor(R.color.pinkFont));
            textAuthor.setBackgroundColor(this.getResources().getColor(R.color.pinkBack));
            textAuthor.setTextColor(this.getResources().getColor(R.color.pinkFont));
            textView.setBackgroundColor(this.getResources().getColor(R.color.pinkBack));
            textView.setTextColor(this.getResources().getColor(R.color.pinkFont));
            toolbar.setBackgroundColor(this.getResources().getColor(R.color.pinkBack));
            toolbar.setTitleTextColor(this.getResources().getColor(R.color.pinkFont));
            linearLayout.setBackgroundColor(this.getResources().getColor(R.color.pinkBack));
        }
        else if(t==4){
            textFinish.setBackgroundColor(this.getResources().getColor(R.color.nightBack));
            textFinish.setTextColor(this.getResources().getColor(R.color.nightFont));
            barTitle.setTextColor(this.getResources().getColor(R.color.nightFont));
            textTitle.setBackgroundColor(this.getResources().getColor(R.color.nightBack));
            textTitle.setTextColor(this.getResources().getColor(R.color.nightFont));
            textAuthor.setBackgroundColor(this.getResources().getColor(R.color.nightBack));
            textAuthor.setTextColor(this.getResources().getColor(R.color.nightFont));
            textView.setBackgroundColor(this.getResources().getColor(R.color.nightBack));
            textView.setTextColor(this.getResources().getColor(R.color.nightFont));
            toolbar.setBackgroundColor(this.getResources().getColor(R.color.nightBack));
            toolbar.setTitleTextColor(this.getResources().getColor(R.color.nightFont));
            linearLayout.setBackgroundColor(this.getResources().getColor(R.color.nightBack));
        }
    }

    // 设置字体大小
    public void setsize(int i){
        if(i == 0) {
            System.out.println("设置字体大小为“小");
            textView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, 53);
        }
        else if(i == 1) {
            System.out.println("设置字体大小为“中");
            textView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, 59);
        }
        else if(i == 2) {
            System.out.println("设置字体大小为“大");
            textView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, 67);
        }
    }

    //调用设置字体背景颜色
    public void setColor(int i) {
        color=i;
        if(i == 0) {                // white
            setTextColor(0);
        }
        else if(i == 1) {           // green
            setTextColor(1);
        }
        else if(i == 2) {           // yellow
            setTextColor(2);
        }
        else if(i == 3) {           // pink
            setTextColor(3);
        }
        else if(i == 4) {           // night
            setTextColor(4);
        }
    }


}
