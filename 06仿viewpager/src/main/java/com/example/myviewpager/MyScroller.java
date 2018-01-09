package com.example.myviewpager;

import android.os.SystemClock;

/**
 * Created by Administrator on 2017/3/20.
 */

public class MyScroller {
    /**
     * 起始坐标
     */
    private float startX;
    private float startY;
    /**
     * X轴和Y轴移动距离
     */
    private int distanceX;
    private int distanceY;
    private long startTime;//开始的时间
    private long totalTime = 500;
    private boolean isFinish;//移动是否完成
    private float currentX;

    public float getCurrentX() {
        return currentX;
    }

    public void setCurrentX(float currentX) {
        this.currentX = currentX;
    }


    public void startScroll(float startX,float startY,int distanceX,int distanceY){
        this.startX = startX;
        this.startY = startY;
        this.distanceX = distanceX;
        this.distanceY = distanceY;
        this.startTime = SystemClock.uptimeMillis();//系统开机时间
        this.isFinish = false;
    }

    /**
     *
     * @return true正在移动  false移动结束
     */
    public boolean computeScrollOffset(){
        if(isFinish){
            return false;
        }
        long endTime = SystemClock.uptimeMillis();//结束时间
        //移动一小段的时间
        long passTime = endTime - startTime;
        if(passTime<totalTime){
            //还没有移动结束
            //计算平均速度
//            float voleCity = distanceX/totalTime;//平均移动速度
            float subDistance = passTime * distanceX/totalTime;
            currentX = startX + subDistance;
        }else {
            //移动结束
            isFinish = true;
            currentX = startX + distanceX;
        }
        return true;
    }
}
