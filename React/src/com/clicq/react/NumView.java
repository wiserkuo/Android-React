package com.clicq.react;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.os.Message;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.view.Display;
import android.graphics.Point;
public class NumView extends View implements OnTouchListener {
    private static final String TAG = "NumView";

    List<P> points = new ArrayList<P>();
    Paint paint = new Paint();
    int[] seq=new int[25]; 
    int numTouched = 0;
    boolean turnOn = false;
    int width,height,grid;
    Handler mHandler;
    int nextNum =1;
    int lastTouched;
    boolean startFlag=false;
    public NumView(Context context, AttributeSet attrs) {
        super(context,attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size;
        size = new Point();
        display.getSize(size);
        width=size.x;
        height=size.y;
        grid=size.x/7;
        mHandler = new Handler(){
            int i = 0;
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case 1:
                        invalidate();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        this.setOnTouchListener(this);
        setBackgroundColor(Color.WHITE);
        
        paint.setAntiAlias(true);
        for(int i=1;i<=25;i++)
            seq[i-1]=i;
        randomSeq();
        paint.setTextSize(grid/2);
        invalidate();
    }
    void randomSeq(){
        int temp,index1,index2;
        for(int i=1;i<50;i++){
            index1=(int)(Math.random()*25);
            index2=(int)(Math.random()*25);            
            temp=seq[index1];
            seq[index1]=seq[index2];
            seq[index2]=temp;
        }
        for(int i=1;i<=25;i++){
            Log.d(TAG,"i="+i+" value="+seq[i-1]);
        }
    }
    public void initialize(){
        nextNum=1;
        lastTouched=0;
        randomSeq();
        invalidate();
    }
    public void stop(){

        startFlag=false;
    }
    public void start(){
        startFlag=true;
    }
    public boolean isFinished(){

        return !startFlag;
    }
    @Override
    public void onDraw(Canvas canvas) {
        Log.d(TAG,"onDraw");


        //draw GREEN touched mark
        if(numTouched>=1&&numTouched<=25&&!turnOn){
            Log.d(TAG,"onDraw touched nextNum="+nextNum+" numTouched-1="+(numTouched-1));
            numTouched-=1;
            if(seq[numTouched]==nextNum)
                paint.setColor(Color.GREEN);
            else
                paint.setColor(Color.RED);
            canvas.drawRect(grid+(numTouched%5)*grid,grid+(numTouched/5)*grid,grid+(numTouched%5)*grid+grid,grid+(numTouched/5)*grid+grid,paint);
        }
        paint.setColor(Color.BLACK);
        //draw 5x5 
        for(int i=0;i<6;i++){
              canvas.drawLine(grid+i*grid, grid,grid+i*grid,grid*6, paint);
              canvas.drawLine(grid, grid+i*grid,grid*6,grid+i*grid, paint);
        }
        //draw 1-25 text
        for(int i=0;i<25;i++){
            if(seq[i]<10)
                canvas.drawText(""+seq[i], (float)(grid*1.4+(i%5)*grid), (float)(grid*1.7+(i/5)*grid) , paint);
            else
                canvas.drawText(""+seq[i], (float)(grid*1.2+(i%5)*grid), (float)(grid*1.7+(i/5)*grid) , paint);
        }
        //draw touched point
        for (P point : points) {
            canvas.drawCircle(point.x, point.y, 3, paint);
            // Log.d(TAG, "Painting: "+point);
        }
    }

    public boolean onTouch(View view, MotionEvent event) {
        // if(event.getAction() != MotionEvent.ACTION_DOWN)
        // return super.onTouchEvent(event);
        if (!startFlag)return true;
        P point = new P();
        point.x = event.getX();
        point.y = event.getY();
        points.add(point);
        if(point.x>=grid&&point.x<=grid*6&&point.y>=grid&&point.y<=grid*6){
            numTouched=1+((int)point.y-grid)/grid*5+(int)(point.x-grid)/grid;
            if(lastTouched==numTouched);
            new Thread(new Runnable(){
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    try{
                        turnOn=false;
                        Message msg = new Message();
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                        Thread.sleep(300);
                        turnOn=true;
                        Message msg2 = new Message();
                        msg2.what = 1;
                        mHandler.sendMessage(msg2);
                        if(seq[numTouched]==nextNum)nextNum++;
                        if(nextNum==26){
                            nextNum=1;
                            stop();
                        }

                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }

                }
            }).start();
            lastTouched=numTouched;
        }
        Log.d(TAG, "point: " + point+" numTouched="+numTouched);
        
        return true;
    }
}

class P {
    float x, y;

    @Override
    public String toString() {
        return x + "," + y;
    }
}