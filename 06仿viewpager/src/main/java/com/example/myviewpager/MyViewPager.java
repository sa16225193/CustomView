package com.example.myviewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import static android.R.attr.endX;

/**
 * 仿ViewPager效果
 * Created by Administrator on 2017/3/19.
 */

public class MyViewPager extends ViewGroup {

    /**
     * 手势识别器
     * 1.定义出来
     * 2.实例化，把想要的方法给重新定义
     * 3.在onTouchEvent()把事件传递给手势识别器
     */
    //1.定义手势识别器
    private GestureDetector detector;
    private int currentIndex;//当前页面的索引
    private Scroller scorller;
    private float startX;

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        scorller = new Scroller(context);
        //2.实例化手势识别器
        detector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){

            //长按事件
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
            }

            /**
             * 滑动
             * @param e1  按下时手指的位置
             * @param e2  滑动后手指的位置
             * @param distanceX  X轴滑动的距离
             * @param distanceY   Y轴滑动的距离
             * @return
             */
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                /**
                 * x:要在X轴移动的距离
                 * y:要在Y轴移动的距离
                 */
                scrollBy((int)distanceX,0);
                return true;
            }

            //双击
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return super.onDoubleTap(e);
            }
        });
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        //遍历孩子，给每个孩子指定在屏幕的位置
        for(int j=0;j<getChildCount();j++){
            View childView = getChildAt(j);
            childView.layout(j*getWidth(),0,(j+1)*getWidth(),getHeight());
        }
    }

    //使测试页面的子视图显示
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for(int i=0;i<getChildCount();i++){
            View childView = getChildAt(i);
            /**
             * widthMeasureSpec父视图的宽
             * heightMeasureSpec父视图的高
             */
            childView.measure(widthMeasureSpec,heightMeasureSpec);
        }
    }

    private float downX;
    private float downY;

    /**
     * 如果当前方法，返回true，拦截事件，将会触发当前控件的onTouchEvent()方法
     * 此时测试页面的scrollView将无法上下滑动
     * 如果当前方法，返回false，不拦截事件，事件将会继续传递给孩子
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        detector.onTouchEvent(ev);//解决移动scrollView鼠标闪动的bug，原因：ACTION_DOWN未执行
        boolean result = false;//默认 传递给孩子
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                //1.记录坐标
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = ev.getX();


                float endY = ev.getY();
                //计算绝对值
                float discX = Math.abs(endX - downX);
                float discY = Math.abs(endY - downY);
                if(discX>discY&&discX>5){
                    result = true;
                }else{
                    if((startX - endX)>getWidth()/2){
                        currentIndex++;
                    }else if((endX - startX)>getWidth()/2) {
                        currentIndex--;
                    }
                    scrollToPager(currentIndex);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        //3.把事件传递给手势识别器
        detector.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //1.记录坐标
                startX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                //2.计算新坐标
                float endX = event.getX();
                //3.计算偏移量
                int tempIndex = currentIndex;
                if((startX - endX)>getWidth()/2){
                    tempIndex++;
                }else if((endX - startX)>getWidth()/2){
                    tempIndex--;
                }
                //根据下标位置移动到指定的页面
                scrollToPager(tempIndex);
                break;
        }
        return true;
    }

    /**
     * 屏蔽非法值，根据索引移动到指定页面
     * @param tempIndex
     */
    public void scrollToPager(int tempIndex) {
        if (tempIndex<0){
            tempIndex = 0;
        }
        if(tempIndex>getChildCount()-1){
            tempIndex = getChildCount() - 1;
        }
        //当前页面的下标位置
        currentIndex = tempIndex;
        if (mOnPagerChangedListener!=null){
            mOnPagerChangedListener.onScrollToPager(currentIndex);
        }
        int disc = currentIndex*getWidth() - getScrollX();
        //getScrollY()起始Y轴坐标
//        scrollTo(currentIndex*getWidth(),getScrollY());
        scorller.startScroll(getScrollX(),getScrollY(),disc,0,Math.abs(disc));
        invalidate();//会调用onDraw()、computeScroll()
    }

    @Override
    public void computeScroll() {
//        super.computeScroll();
        if(scorller.computeScrollOffset()){
            float currentX = scorller.getCurrX();
            scrollTo((int)currentX,0);
            invalidate();
        }
    }

    public interface OnPagerChangedListener{
        /**
         * 当页面改变的时候回调这个方法
         * @param position  //当前页面的下标
         */
        void onScrollToPager(int position);
    }

    private OnPagerChangedListener mOnPagerChangedListener;

    /**
     * 设置页面改变的监听
     * @param listener
     */
    public void setOnPagerChangeListener(OnPagerChangedListener listener){
        mOnPagerChangedListener = listener;
    }
}
