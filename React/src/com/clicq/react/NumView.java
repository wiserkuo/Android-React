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
    int averageCount=0;
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
        if(size.x<size.y)
            grid=size.x/5;
        else
            grid=size.y/6;
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

    }
    public void stop(){

        startFlag=false;
    }
    public void start(){
        startFlag=true;
        invalidate();
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
            canvas.drawRect((numTouched%5)*grid,(numTouched/5)*grid,(numTouched%5)*grid+grid,(numTouched/5)*grid+grid,paint);
        }
        paint.setColor(Color.BLACK);
        //draw 5x5 
        for(int i=0;i<6;i++){
              canvas.drawLine(i*grid, 0,i*grid,grid*5, paint);
              canvas.drawLine(0, i*grid,grid*5,i*grid, paint);
        }
        //draw 1-25 text
        for(int i=0;i<25;i++){
            if(seq[i]<10)
                canvas.drawText(""+seq[i], (float)(grid*0.4+(i%5)*grid), (float)(grid*0.7+(i/5)*grid) , paint);
            else
                canvas.drawText(""+seq[i], (float)(grid*0.2+(i%5)*grid), (float)(grid*0.7+(i/5)*grid) , paint);
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
        if (!startFlag&&!(event.getAction()==MotionEvent.ACTION_DOWN))return true;

        P point = new P();
        point.x = event.getX();
        point.y = event.getY();
        points.add(point);
        Log.d(TAG, "onTouched: "+event.getX()+" "+event.getY());
        /*averageCount++;
        if(averageCount==2){
            P ap=new P();
            for (P p : points) {
                //canvas.drawCircle(point.x, point.y, 3, paint);
            // Log.d(TAG, "Painting: "+point);
                ap.x+=p.x;
                ap.y+=p.y;

            }
            ap.x=ap.x/2;
            ap.y=ap.y/2;
            points.clear();
            averageCount=0;
            point.x=ap.x;
            point.y=ap.y;
        }
        else 
            return true;*/
        if(point.x>=0&&point.x<=grid*5&&point.y>=0&&point.y<=grid*5){
            numTouched=1+((int)point.y)/grid*5+(int)(point.x)/grid;
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