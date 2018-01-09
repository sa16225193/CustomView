package com.example.youku;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;

import static android.R.attr.animation;

/**
 * 显示和隐藏指定控件
 * Created by Administrator on 2017/3/17.
 */

public class Tools {

    public static void hideView(ViewGroup view) {
        hideView(view,0);//使用补间动画方式
    }

    public static void showView(ViewGroup view) {
        showView(view,0);//使用补间动画方式
     }

    /**
     * 使用补间动画9
     * @param view
     * @param startOffset
     */
    public static void hideView(ViewGroup view, int startOffset) {
        /*RotateAnimation animation = new RotateAnimation(0, 180, view.getWidth() / 2, view.getHeight());
        animation.setDuration(500);
        animation.setStartOffset(startOffset);//设置动画延迟播放
        view.startAnimation(animation);
        animation.setFillAfter(true);// 设置动画停留在结束时的状态
        for(int i=0;i<view.getChildCount();i++){
            view.getChildAt(i).setEnabled(false);
        }
        view.setEnabled(false);*/

        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"rotation",0,180);
        animator.setDuration(500);
        animator.setStartDelay(startOffset);
        animator.start();
        view.setPivotX(view.getWidth()/2);
        view.setPivotY(view.getHeight());

    }

    /**
     * 使用补间动画
     * @param view
     * @param startOffset
     */
    public static void showView(ViewGroup view, int startOffset) {
        /*RotateAnimation animation = new RotateAnimation(180, 360, view.getWidth() / 2, view.getHeight());
        animation.setDuration(500);
        animation.setFillAfter(true);// 设置动画停留在结束时的状态
        animation.setStartOffset(startOffset);
        view.startAnimation(animation);
        for(int i=0;i<view.getChildCount();i++){
            view.getChildAt(i).setEnabled(true);
        }
        view.setEnabled(true);*/

        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"rotation",180,360);
        animator.setDuration(500);
        animator.setStartDelay(startOffset);
        animator.start();
        view.setPivotX(view.getWidth()/2);
        view.setPivotY(view.getHeight());
    }

}
