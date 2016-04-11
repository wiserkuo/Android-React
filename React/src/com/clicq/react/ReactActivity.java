package com.clicq.react;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.util.Log;
import android.os.Handler;
public class ReactActivity extends Activity implements OnClickListener {
    private NumView numView;
    private Button startstop;
    private TextView timer;
    boolean startFlag=false;
    Handler mHandler;
    float csec=0;
    int countDown=3;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set full screen view
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                         WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

//        numView = new NumView(this);
        setContentView(R.layout.main);
//      numView.requestFocus();
        numView=(NumView)findViewById(R.id.numview);
        startstop=(Button)findViewById(R.id.startstop);
        startstop.setOnClickListener(this);
        timer=(TextView)findViewById(R.id.timer);
        mHandler = new Handler(){
            int i = 0;
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case 1:
                        csec+=0.1;
                        String test = String.format("%.01f", csec);
                        timer.setText(""+test);
                        break;
                    case 2:
                        timer.setText(""+countDown);
                        countDown--;
                        break;
                    case 3:
                        numView.start();
                        startFlag=true;
                        csec=0;
                        startstop.setText("Stop");
                        startstop.setEnabled(true);
                        new Thread(new Runnable(){
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                try{
                                    while(startFlag) {
                                        if(numView.isFinished()){
                                            startFlag=false;
                                            startstop.setText("Start");
                                        }
                                        Message msg = new Message();
                                        msg.what = 1;
                                        mHandler.sendMessage(msg);
                                        Thread.sleep(100);
                                    }
                                }
                                catch(Exception e){
                                    e.printStackTrace();
                                }

                            }
                        }).start();
                        break;
                }
                super.handleMessage(msg);
            }
        };

    }
   // private OnClickListener listener=new OnClickListener(){
        @Override
        public void onClick(View v){
            switch(v.getId()){
                case R.id.startstop:
                    Log.d("ReactActivity","startstop............");

                    if(startFlag) {
                        numView.stop();
                        startFlag=false;
                        startstop.setText("Start");
                    }
                    else {
                        numView.initialize();
                        countDown=3;
                        startstop.setEnabled(false);
                        new Thread(new Runnable(){
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                try{
                                    while(countDown!=0) {
                                        Message msg = new Message();
                                        msg.what = 2;
                                        mHandler.sendMessage(msg);
                                        Thread.sleep(1000);
                                    }
                                    Message msg = new Message();
                                    msg.what = 3;
                                    mHandler.sendMessage(msg);
                                }
                                catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }).start();


                    }
                        break;
            }
        }

    //};
}