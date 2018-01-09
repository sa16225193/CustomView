package com.example.togglebutton;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import static android.R.attr.startX;

/**
 * Created by Administrator on 2017/3/18.
 * 1.构造方法实例化类
 * 2.测量-measure(int,int)-->onMeasure();
 * 如果当前View是一个ViewGroup，还有义务测量孩子
 * 孩子有建议权
 * 3.布局-layout-->onLayout();
 * 指定控件的位置，一般view不用写这个方法,ViewGroup时才需要
 * 4.绘制-draw()-->onDraw(canvas);
 * 根据上面两个方法的参数，进行绘制
 */

public class MyToggleButton extends View implements View.OnClickListener{

    private Bitmap backgroundBitmap;
    private Bitmap slidingBitmap;
    private int slideLeftMax;//距离左边最大距离
    private int slideLeft;
    private Paint paint;
    private boolean isOpen = false;//默认为关状态
    private boolean isEnableClick = true;//默认让点击事件生效
    private float startX;
    private float lastX;

    /**
     * 如果我们在布局文件中使用该类，将会用这个构造方法实例该类，如果没有就报错
     *
     * @param context
     * @param attrs
     */
    public MyToggleButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        paint = new Paint();
        paint.setAntiAlias(true);//消除锯齿
        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.switch_background);
        slidingBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.slide_button);
        slideLeftMax = backgroundBitmap.getWidth() - slidingBitmap.getWidth();
        setOnClickListener(this);

    }

    /**
     * 视图的测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //设置ToggleButton的宽高
        setMeasuredDimension(backgroundBitmap.getWidth(), backgroundBitmap.getHeight());
    }

    /**
     * 绘制
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        canvas.drawBitmap(backgroundBitmap, 0, 0, paint);
        canvas.drawBitmap(slidingBitmap,slideLeft,0,paint);
    }

    @Override
    public void onClick(View view) {
        if (isEnableClick){
            isOpen = !isOpen;
            flushView();
        }
    }

    private void flushView() {
        if(isOpen){
            slideLeft = slideLeftMax;
        }else {
            slideLeft = 0;
        }
        invalidate();//会调用onDraw()方法，强制重新绘制
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //1.记录按下的坐标
                lastX = event.getX();
                startX = event.getX();
                isEnableClick = true;
                break;
            case MotionEvent.ACTION_MOVE:
                //2.计算结束值
                float endX = event.getX();
                //3.计算偏移量
                float distance = endX - startX;
                slideLeft += distance;
                //4.屏蔽非法值
                if(slideLeft<0){
                    slideLeft = 0;
                }else if (slideLeft>slideLeftMax){
                    slideLeft = slideLeftMax;
                }
                //5.更新视图
                invalidate();
                //6.数据还原
                startX = event.getX();
                if(Math.abs(endX - lastX)>5){
                    isEnableClick = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if(!isEnableClick){
                    if(slideLeft>slideLeftMax/2){
                        //显示按钮开
                        isOpen = true;
                    }else {
                        isOpen = false;
                    }
                    flushView();
                }
                break;
        }
        return true;
    }

}
