package com.example.ad_banner;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ViewPager viewpager;
    private TextView tv_title;
    private LinearLayout ll_point_group;
    private ArrayList<ImageView> imageViews;
    private final int[] imageIds = {
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d,
            R.drawable.e
    };
    private int prePosition = 0;//选中的点的位置索引
    private final String[] imageDescriptions = {
            "尚硅谷拔河争霸赛",
            "凝聚你我，放飞梦想",
            "抱歉，没座位了！",
            "7月就业名单全部曝光",
            "平均起薪11345元"
    };
    //使用Handler实现自动切换页面
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int item = viewpager.getCurrentItem()+1;
            viewpager.setCurrentItem(item);
            //延迟发消息
            handler.sendEmptyMessageDelayed(0,4000);
        }
    };
    private boolean isDragging = false;//是否正在拖拽

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tv_title = (TextView) findViewById(R.id.tv_title);
        ll_point_group = (LinearLayout) findViewById(R.id.ll_point_group);

        //ViewPager的使用
        //1.在布局文件中定义ViewPager
        //2.在代码中实例化ViewPager
        //3.准备数据
        imageViews = new ArrayList<>();
        for (int i = 0; i < imageIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imageIds[i]);
            //添加到集合中
            imageViews.add(imageView);
            //添加点
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(8,8);
            params.bottomMargin = 4;
            if(i==0){
                point.setEnabled(true);//默认选中第一个点
            }else {
                point.setEnabled(false);
                params.leftMargin = 8;
            }
            point.setLayoutParams(params);
            //将点添加到线性布局
            ll_point_group.addView(point);
        }
        //4.设置适配器(PagerAdapter)-item布局-绑定数据
        viewpager.setAdapter(new MyPagerAdapter());
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
        tv_title.setText(imageDescriptions[prePosition]);
        //设置ViewPager中间位置
        //item必须为imageView.size()的整数倍
        int item = Integer.MAX_VALUE/2 - Integer.MAX_VALUE/2%imageViews.size();
        viewpager.setCurrentItem(item);
        handler.sendEmptyMessageDelayed(0,3000);
    }

    class  MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         *当页面滚动了的时候回调这个方法
         * @param position   当前页面的位置
         * @param positionOffset   滑动页面的百分比
         * @param positionOffsetPixels   在屏幕上滑动的像素
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 当某个页面被选中的时候回调
         * @param position  被选中页面的位置
         */
        @Override
        public void onPageSelected(int position) {
            int realPosition = position%imageViews.size();
            //更新文本
            tv_title.setText(imageDescriptions[realPosition]);
            //把上一个选中的点，设置为未选中
            ll_point_group.getChildAt(prePosition).setEnabled(false);
            //将当前页面的点设置为选中
            ll_point_group.getChildAt(realPosition).setEnabled(true);
            prePosition = realPosition;
        }

        /**
         * 状态变化时回调这个方法：
         * 静止-》滑动、滑动-》静止、静止-》拖拽
         * @param state
         */
        @Override
        public void onPageScrollStateChanged(int state) {

            if(state == ViewPager.SCROLL_STATE_DRAGGING){//拖拽状态
                isDragging = true;
                handler.removeCallbacksAndMessages(null);
                Log.e("TAG","SCROLL_STATE_DRAGGING-----------");
            }else if (state == ViewPager.SCROLL_STATE_SETTLING){//滑动状态，页面切换
                Log.e("TAG","SCROLL_STATE_SETTLING-----------");
            }else if(state == ViewPager.SCROLL_STATE_IDLE&&isDragging){//静止状态
                Log.e("TAG","SCROLL_STATE_IDLE-----------");
                isDragging = false;
                handler.removeCallbacksAndMessages(null);
                handler.sendEmptyMessageDelayed(0,4000);
            }
        }
    }

    class MyPagerAdapter extends PagerAdapter {

        /**
         * 得到图片的总数
         *
         * @return
         */
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        /**
         * 比较view和object是否为同一个实例
         *
         * @param view  页面
         * @param object   instantiateItem方法返回的结果
         * @return
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 相当于ListView的getView方法
         *
         * @param container ViewPager自身
         * @param position  当前实例化页面的位置
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {//先初始化3个Item

            final int realPosition = position%imageViews.size();
            ImageView imageView = imageViews.get(realPosition);
            container.addView(imageView);//添加到ViewPager中
            //Log.e("TAG","instantiateItem()="+position+"---imageView="+imageView);

            //设置触摸监听，手指按下时，停止自动切换图片
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN://手指按下
                            Log.e("TAG","onTouch==手指按下");
                            handler.removeCallbacksAndMessages(null);
                            break;
                        case MotionEvent.ACTION_MOVE://手指在这个控件上移动
                            break;
                        case MotionEvent.ACTION_CANCEL://手指在这个控件上移动
                            Log.e("TAG","onTouch==事件取消");
//                            handler.removeCallbacksAndMessages(null);
//                            handler.sendEmptyMessageDelayed(0,4000);
                            break;
                        case MotionEvent.ACTION_UP://手指离开
                            Log.e("TAG","onTouch==手指离开");
                            handler.removeCallbacksAndMessages(null);
                            handler.sendEmptyMessageDelayed(0,4000);
                            break;
                    }
                    return false;
                }
            });

            imageView.setTag(realPosition);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("TAG","点击事件");
                    int position = (int) view.getTag();
                    String text = imageDescriptions[realPosition];
                    Toast.makeText(MainActivity.this,text,Toast.LENGTH_SHORT).show();
                }
            });

            return imageView;
        }

        /**
         * 释放资源
         * @param container  viewPager
         * @param position   要释放的位置
         * @param object   要释放的页面
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
           // super.destroyItem(container, position, object);
          //  Log.e("TAG","destroyItem()="+position+"---object="+object);
            container.removeView((View) object);
        }
    }


}
