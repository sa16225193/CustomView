package com.example.autoattr;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义属性
 * Created by Administrator on 2017/3/19.
 */

public class MyAtrributeView extends View {

    private int myAge;
    private String myName;
    private Bitmap myBg;

    public MyAtrributeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //获取属性的三种方式
        //1.用命名空间去获取
        String name = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","my_name");
        String age = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","my_age");
        String bg = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","my_background");
        System.out.println("age ="+age+",name="+name+",bg="+bg);
        //2.遍历属性集合
        for(int i=0;i<attrs.getAttributeCount();i++){
            System.out.println(attrs.getAttributeName(i)+attrs.getAttributeValue(i));
        }
        //3.使用系统工具，获取属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.MyAtrributeView);
        for(int i=0;i<typedArray.getIndexCount();i++){
            int index = typedArray.getIndex(i);
            switch (index){
                case R.styleable.MyAtrributeView_my_age:
                    myAge = typedArray.getInt(index,0);
                    break;
                case R.styleable.MyAtrributeView_my_name:
                    myName = typedArray.getString(index);
                    break;
                case R.styleable.MyAtrributeView_my_background:
                    Drawable drawable = typedArray.getDrawable(index);
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                    myBg = bitmapDrawable.getBitmap();
                    break;
            }
        }
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        canvas.drawText(myName+"---"+myAge,50,50,paint);
        canvas.drawBitmap(myBg,50,50,paint);
    }
}
