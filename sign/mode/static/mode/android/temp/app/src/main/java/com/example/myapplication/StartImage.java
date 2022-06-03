package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.tencent.smtt.sdk.QbSdk;

public class StartImage extends AppCompatActivity {

    private static final String TAG = "StartImage";
    private Handler mHandler = new Handler();
    private Button mBtnSkip;
    //倒计时
    private int count = 3;
    private boolean loadX5 = false;
    private com.tencent.smtt.sdk.WebView mWebView;
    public static String test;


    private Runnable mRunnableToMain = new Runnable() {
        @Override
        public void run() {
//            if(count == -1){
//                if(loadX5 == true){
//                    toLoginActivity();
//                }else {
//                    test = "加载中";
//                    mBtnSkip.setText(test);
//                    mHandler.postDelayed(mRunnableToMain, 1000);
//                }
//            }else {
//                test = "跳过 " + count;
//                mBtnSkip.setText(test);
//                mHandler.postDelayed(mRunnableToMain, 1000);
//                count = count - 1;
//            }

            if(count == -1){
                toLoginActivity();
            }else {
                if(count == 0){
                    test = "加载中";
                }else {
                    test = "跳过 " + count;
                }
                mBtnSkip.setText(test);
                mHandler.postDelayed(mRunnableToMain, 1000);
                count = count - 1;
            }

        }
    };
    private void toLoginActivity() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }
    private void init(){
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        Log.d(TAG, "启动");
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is x5内核 " + arg0);
//                if(count <= 2){
//                    mWebView =findViewById(R.id.x5);
//                    mWebView.loadUrl("https://www.baidu.com");
//                }

                loadX5 = true;

                //Intent intent = new Intent(Skip.this, MainActivity.class);
                //startActivity(intent);
            };

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);

        //x5.loadUrl("https://www.baidu.com");
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(mRunnableToMain);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        mBtnSkip = (Button) findViewById(R.id.startImage);
        mHandler.postDelayed(mRunnableToMain, 1000);

        mBtnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if(loadX5 == true){
//                    mHandler.removeCallbacks(mRunnableToMain);
//                    toLoginActivity();
//                //销毁mHandler
                    mHandler.removeCallbacks(mRunnableToMain);
                    toLoginActivity();
            }
        });
    }
}