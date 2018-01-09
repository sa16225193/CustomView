package com.example.dropdownlist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import static android.R.id.list;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class MainActivity extends AppCompatActivity {

    private EditText et_input;
    private ImageView iv_down_arrow;
    private PopupWindow popupWindow;
    private ListView listview;
    private ArrayList<String> msgs;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_down_arrow = (ImageView)findViewById(R.id.iv_down_arrow);
        et_input = (EditText) findViewById(R.id.et_input);

        et_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(popupWindow==null){
                    popupWindow = new PopupWindow(MainActivity.this);
                    popupWindow.setWidth(et_input.getWidth());
                    //将200dp转换为像素，适配分辨率不同的机型
                    int height = DensityUtil.dip2px(MainActivity.this,200);
                    popupWindow.setHeight(height);

                    popupWindow.setContentView(listview);
                    popupWindow.setFocusable(true);//设置焦点
                }
                popupWindow.showAsDropDown(et_input,0,0);
            }
        });
        listview = new ListView(this);
        listview.setBackgroundResource(R.drawable.listview_background);
        //listview准备数据
        msgs = new ArrayList<String>();
        for (int i=0;i<500;i++){
            msgs.add(i+"--aaaaaaaaaaa--"+i);
        }
        myAdapter = new MyAdapter();
        listview.setAdapter(myAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //1.得到数据
                String msg = msgs.get(i);
                //2.设置输入框
                et_input.setText(msg);
                if(popupWindow!=null&&popupWindow.isShowing()){
                    popupWindow.dismiss();
                    popupWindow = null;
                }
            }
        });
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return msgs.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if(view==null){
                view = View.inflate(MainActivity.this,R.layout.item_main,null);
                viewHolder = new ViewHolder();
                viewHolder.iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
                viewHolder.tv_msg = (TextView) view.findViewById(R.id.tv_msg);
                view.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) view.getTag();
            }
            //根据位置得到数据
            final String msg = msgs.get(i);
            viewHolder.tv_msg.setText(msg);
            //设置删除
            viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //1.从集合中删除
                    msgs.remove(msg);
                    //2.刷新UI
                    myAdapter.notifyDataSetChanged();
                }
            });
            return view;
        }
    }

    static  class  ViewHolder{
        TextView tv_msg;
        ImageView iv_delete;
    }

}
