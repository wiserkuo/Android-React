package com.clicq.react;

import java.util.ArrayList;
import java.util.List;

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
    public NumView(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);

        this.setOnTouchListener(this);
        setBackgroundColor(Color.WHITE);
        paint.setColor(Color.BLACK);
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
        Log.d(TAG,"==========================");
    }
    @Override
    public void onDraw(Canvas canvas) {
        for(int i=0;i<6;i++){
              canvas.drawLine(100+i*100, 100,100+i*100,600, paint);
              canvas.drawLine(100, 100+i*100,600,100+i*100, paint);           
        }
        for(int i=0;i<25;i++){
            canvas.drawText(""+seq[i],140+(i%5)*100,160+(i/5)*100,paint);
        }
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
        invalidate();
        Log.d(TAG, "point: " + point);
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