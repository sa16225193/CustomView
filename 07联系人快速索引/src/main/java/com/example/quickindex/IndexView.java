package com.example.quickindex;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 联系人快速索引
 * 绘制快速索引的字母
 * 1.把26个字母放入数组中
 * 2.在onMeasure()计算每条的高itemHeight和宽itemWidth,
 * 3.和在onDraw()wordWidth,wordHeight
 * 4.按下字母，文字变色
 *  重写onTouchEvent(),返回true,在down/move的过程中计算
 *  int touchIndex = Y/itemHeight;
 *  强制绘制--》onDraw()
 *  在onDraw()方法对应的下标设置画笔变色
 *  在up的时候
 *  touchIndex = -1;强制绘制
 *
 * Created by Administrator on 2017/3/21.
 */

public class IndexView extends View {

    private String[] words = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    private int itemWidth;
    private int itemHeight;
    private Paint paint;

    public IndexView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.WHITE);//设置画笔颜色
        paint.setAntiAlias(true);//设置抗锯齿
        paint.setTypeface(Typeface.DEFAULT_BOLD);//设置粗体
        paint.setTextSize(50);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        itemHeight = getMeasuredHeight()/words.length;//获取每个item的高
        itemWidth = getMeasuredWidth();//获取每个item的宽
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(int i=0;i<words.length;i++){
            if (touchIndex==i){
                //设置灰色
                paint.setColor(Color.GRAY);
            }else {
                //设置白色
                paint.setColor(Color.WHITE);
            }
            String word = words[i];
            Rect rect = new Rect();
            //画笔
            //0,1表示取一个字母
            paint.getTextBounds(word,0,1,rect);
            //获取字母的宽和高
            int wordWidth = rect.width();
            int wordHeight = rect.height();
            //计算每个字母的左下角坐标
            float wordX = itemWidth/2 - wordWidth/2;
            float wordY = itemHeight/2 + wordHeight/2 + i*itemHeight;
            //绘制文本
            canvas.drawText(word,wordX,wordY,paint);
        }
    }

    private int touchIndex = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float Y = event.getY();
                int index = (int) (Y/itemHeight);//得到字母的索引
                if(index!=touchIndex){
                    touchIndex = index;
                    invalidate();//强制绘制
                    if(onIndexChangeListener!=null&&touchIndex<words.length&&touchIndex>-1){
                        onIndexChangeListener.onIndexChange(words[touchIndex]);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                touchIndex = -1;
                invalidate();
                break;
        }
        return true;
    }

    /**
     * 字母索引改变的监听器
     */
    public interface OnIndexChangeListener{
        /**
         * 当字母下标位置发生改变时回调
         * @param word  //改变后的字母
         */
        void onIndexChange(String word);
    }

    private OnIndexChangeListener onIndexChangeListener;

    public void setOnIndexChangeListener(OnIndexChangeListener onIndexChangeListener){
        this.onIndexChangeListener = onIndexChangeListener;
    }
}
