package com.example.wave;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2017/3/22.
 */

public class WaveView extends View {

    private Paint paint;//画笔用来绘制圆环
    private int radius;//记录圆环的半径
    private float downX;//记录点击屏幕的坐标X
    private float downY;//记录点击屏幕的坐标Y
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            radius += 5;
            int alpha = paint.getAlpha();
            alpha -= 5;//让透明度不断减小直到为0
            if(alpha<0){
                alpha = 0;
            }
            paint.setAlpha(alpha);
            paint.setStrokeWidth(radius/3);
            invalidate();
        }
    };

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        radius = 5;
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);//设置形状为圆环
        paint.setStrokeWidth(radius/3);//设置圆环的半径
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(paint.getAlpha()>0&&downX>0&&downY>0) {
            canvas.drawCircle(downX, downY, radius, paint);
            handler.sendEmptyMessageDelayed(0,50);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                initView();
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }
}

