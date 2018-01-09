package com.example.slidemenu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;
import android.widget.Toast;

import static android.R.attr.flipInterval;
import static android.R.attr.start;
import static android.R.attr.x;

/**
 * Created by Administrator on 2017/3/22.
 * 侧滑菜单
 */

public class SlideLayout extends FrameLayout {

    private View contentView;
    private View menuView;
    private int contentWidth;
    private int menuWidth;
    private int viewHeight;//三个高度都是相同的
    private float startX;
    private float startY;
    private float downX;//只赋值一次
    private float downY;//只赋值一次
    /**
     * 滚动器
     */
    private Scroller scroller;

    public SlideLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
    }

    /**
     * 当布局文件加载好，将会回调这个方法
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);
        menuView = getChildAt(1);
    }

    /**
     * 在测量方法里，得到控件的宽和高
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        contentWidth = contentView.getMeasuredWidth();
        menuWidth = menuView.getMeasuredWidth();
        viewHeight = getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //指定菜单的位置
        menuView.layout(contentWidth,0,contentWidth+menuWidth,viewHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //1.按下记录坐标
                downX = startX = event.getX();
                downY = startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //2.记录结束值
                float endX = event.getX();
                float endY = event.getY();
                //3.计算偏移量
                float distanceX = endX - startX;
                int toScrollX = (int) (getScrollX() - distanceX);
                //Toast.makeText(getContext(),"ACTION_MOVE:getScrollX()=="+getScrollX(),Toast.LENGTH_SHORT).show();
                if(toScrollX<0){//屏蔽非法值
                    toScrollX = 0;
                }else if(toScrollX > menuWidth){
                    toScrollX = menuWidth;
                }
                scrollTo(toScrollX,getScrollY());
                startX = event.getX();
                startY = event.getY();
                //在X轴和Y轴滑动的距离
                float DX = Math.abs(endX - downX);
                float DY = Math.abs(endY - downY);
                if(DX>DY&&DX>3){//水平滑动，响应侧滑，反listview的拦截
                    //请求父视图不拦截onTouchEvent事件
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                //Toast.makeText(getContext(),"ACTION_UP:getScrollX()=="+getScrollX(),Toast.LENGTH_SHORT).show();
                int totalScrollX = getScrollX();//X轴的偏移量
                if(totalScrollX < menuWidth/2){
                    //关闭menu
                    closeMenu();
                }else {
                    //打开menu
                    openMenu();
                }
                break;
        }
        return true;
    }

    /**
     *
     * @param event
     * @return  true，拦截孩子的事件，会执行当前控件的onTouchEvent()事件
     * false 不拦截孩子的事件，继续传递onTouchEvent()给孩子
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        super.onInterceptTouchEvent(event);
        boolean intercept = false;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //1.按下记录坐标
                downX = startX = event.getX();
                if(onStageChangeListener!=null){
                    onStageChangeListener.onDown(this);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //2.记录结束值
                float endX = event.getX();
                //3.计算偏移量
                float distanceX = endX - startX;
                startX = event.getX();
                //在X轴和Y轴滑动的距离
                float DX = Math.abs(endX - downX);
                if(DX>3){//水平滑动，响应侧滑，反listview的拦截
                    //请求父视图不拦截onTouchEvent事件
                    intercept = true;
                }
                break;
        }
        return intercept;
    }

    /**
     * 打开menu
     */
    public void openMenu() {
        int distanceX = menuWidth - getScrollX();
        scroller.startScroll(getScrollX(),getScrollY(),distanceX,getScrollY());
        invalidate();
        if(onStageChangeListener!=null){
            onStageChangeListener.onOpen(this);
        }
    }

    /**
     * 关闭menu
     */
    public void closeMenu() {
        int distanceX = 0 - getScrollX();
        scroller.startScroll(getScrollX(),getScrollY(),distanceX,getScrollY());
        invalidate();
        if(onStageChangeListener!=null){
            onStageChangeListener.onClose(this);
        }
    }

    @Override

    public void computeScroll() {
        super.computeScroll();
        if(scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            invalidate();
        }
    }

    /**
     * 监听SlideLayout状态的改变
     */
    public interface OnStateChangeListener{
        void onClose(SlideLayout layout);
        void onDown(SlideLayout layout);
        void onOpen(SlideLayout layout);
    }

    private OnStateChangeListener onStageChangeListener;

    /**
     * 设置SlideLayout状态改变的监听
     * @param onStateChangeListener
     */
    public void senOnStateChangeListener(OnStateChangeListener onStateChangeListener){
        this.onStageChangeListener = onStateChangeListener;
    }
}
