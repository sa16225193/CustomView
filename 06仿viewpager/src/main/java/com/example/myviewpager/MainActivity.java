package com.example.myviewpager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends Activity {

    private MyViewPager myViewPager;
    private RadioGroup rg_main;
    private int[] ids = {
            R.drawable.a1,
            R.drawable.a2,
            R.drawable.a3,
            R.drawable.a4,
            R.drawable.a5,
            R.drawable.a6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//必须在setContentView之前设置
        setContentView(R.layout.activity_main);
        myViewPager = (MyViewPager) findViewById(R.id.myviewpager);
        rg_main = (RadioGroup) findViewById(R.id.rg_main);

        //添加页面
        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(ids[i]);
            //添加到MyViewPager这个view中
            myViewPager.addView(imageView);
            //添加测试页面
        }
        View testView = View.inflate(this, R.layout.test, null);
        myViewPager.addView(testView,2);

        for (int i = 0; i < myViewPager.getChildCount(); i++) {
            RadioButton button = new RadioButton(this);
            button.setId(i);//0-5的id
            if (i == 0) {
                button.setChecked(true);
            }
            //添加到RadioGroup
            rg_main.addView(button);
        }

        //设置RadioGroup选中状态的变化
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            /**
             *
             * @param group
             * @param checkedId  :0-5
             */
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                myViewPager.scrollToPager(checkedId);
            }
        });

        //设置监听页面的改变
        myViewPager.setOnPagerChangeListener(new MyViewPager.OnPagerChangedListener() {
            @Override
            public void onScrollToPager(int position) {
                rg_main.check(position);
            }
        });

    }

}
