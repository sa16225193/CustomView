package com.example.youku;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.level;

public class MainActivity extends AppCompatActivity {

    private ImageView icon_home;
    private ImageView icon_menu;
    private RelativeLayout level1;
    private RelativeLayout level2;
    private RelativeLayout level3;
    private boolean isShowLevel3 = true;//是否显示最外层菜单
    private boolean isShowLevel2 = true;//是否显示中间层菜单
    private boolean isShowLevel1 = true;//是否显示最内层菜单

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        icon_home = (ImageView) findViewById(R.id.icon_home);
        icon_menu = (ImageView) findViewById(R.id.icon_menu);
        level1 = (RelativeLayout) findViewById(R.id.level1);
        level2 = (RelativeLayout) findViewById(R.id.level2);
        level3 = (RelativeLayout) findViewById(R.id.level3);

        MyOnClickListener myOnClickListener = new MyOnClickListener();
        //设置点击事件
        icon_home.setOnClickListener(myOnClickListener);
        icon_menu.setOnClickListener(myOnClickListener);
        level1.setOnClickListener(myOnClickListener);
        level2.setOnClickListener(myOnClickListener);
        level3.setOnClickListener(myOnClickListener);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_MENU){
            //如果一级、二级、三级菜单是显示的，就全部隐藏
            if(isShowLevel1){
                isShowLevel1 = false;
                Tools.hideView(level1);
                if(isShowLevel2){
                    isShowLevel2 = false;
                    Tools.hideView(level2,200);//使用补间动画
                    if(isShowLevel3){
                        isShowLevel3 = false;
                        Tools.hideView(level3,400);//使用补间动画
                    }
                }
            }else{
                //如果一级、二级菜单隐藏，就显示
                isShowLevel1 = true;
                Tools.showView(level1);
                isShowLevel2 = true;
                Tools.showView(level2,200);//使用补间动画
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.level1:
                    Toast.makeText(MainActivity.this,"level1",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.level2:
                    Toast.makeText(MainActivity.this,"level2",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.level3:
                    Toast.makeText(MainActivity.this,"level3",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.icon_home://home
                    //如果三级菜单和二级菜单都显示，则都隐藏
                    if(isShowLevel2){
                        isShowLevel2 = false;
                        Tools.hideView(level2);
                        if(isShowLevel3){
                            isShowLevel3 = false;
                            Tools.hideView(level3,200);//使用补间动画
                        }
                    }else {
                        isShowLevel2 = true;
                        Tools.showView(level2);
                    }
                    //如果都隐藏，显示二级菜单

                    break;
                case R.id.icon_menu://菜单
                    if(isShowLevel3){
                        isShowLevel3 = false;//隐藏
                        Tools.hideView(level3);//隐藏level3
                    }else {
                        //显示
                        isShowLevel3 = true;
                        Tools.showView(level3);//显示
                    }
                    break;
            }
        }
    }
}
