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

public class NumView extends View implements OnTouchListener {
    private static final String TAG = "NumView";

    List<Point> points = new ArrayList<Point>();
    Paint paint = new Paint();
    int[] seq=new int[25]; 
    int numTouched = 0;
    boolean turnOn = false;
 
    public NumView(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);

        this.setOnTouchListener(this);
        setBackgroundColor(Color.WHITE);
        
        paint.setAntiAlias(true);
        for(int i=1;i<=25;i++)
            seq[i-1]=i;
        randomSeq();
        paint.setTextSize(30);
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
    @Override
    public void onDraw(Canvas canvas) {
        Log.d(TAG,"onDraw");
        paint.setColor(Color.GREEN);
        //draw GREEN touched mark
        if(numTouched>=1&&numTouched<=25){
            Log.d(TAG,"onDraw touched");
            numTouched-=1;
            canvas.drawRect(100+(numTouched%5)*100,100+(numTouched/5)*100,100+(numTouched%5)*100+100,100+(numTouched/5)*100+100,paint);
        }
        paint.setColor(Color.BLACK);
        //draw 5x5 
        for(int i=0;i<6;i++){
              canvas.drawLine(100+i*100, 100,100+i*100,600, paint);
              canvas.drawLine(100, 100+i*100,600,100+i*100, paint);           
        }
        //draw 1-25 text
        for(int i=0;i<25;i++){
            canvas.drawText(""+seq[i],140+(i%5)*100,160+(i/5)*100,paint);
        }
        //draw touched point
        for (Point point : points) {
            canvas.drawCircle(point.x, point.y, 5, paint);
            // Log.d(TAG, "Painting: "+point);
        }
    }

    public boolean onTouch(View view, MotionEvent event) {
        // if(event.getAction() != MotionEvent.ACTION_DOWN)
        // return super.onTouchEvent(event);
        Point point = new Point();
        point.x = event.getX();
        point.y = event.getY();
        points.add(point);
        if(point.x>=100&&point.x<=600&&point.y>=100&&point.y<=600&&!turnOn){
            numTouched=1+((int)point.y-100)/100*5+(int)(point.x-100)/100;
            invalidate();
        }
        Log.d(TAG, "point: " + point+" numTouched="+numTouched);
        
        return true;
    }
}

class Point {
    float x, y;

    @Override
    public String toString() {
        return x + "," + y;
    }
}