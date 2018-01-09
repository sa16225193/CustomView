package com.example.quickindex;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.quickindex.IndexView.OnIndexChangeListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends Activity {

    private ListView lv_main;
    private TextView tv_word;
    private IndexView iv_words;
    private Handler handler = new Handler();
    ArrayList<Person> persons = new ArrayList<>();
    private IndexAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv_main = (ListView) findViewById(R.id.lv_main);
        tv_word = (TextView) findViewById(R.id.tv_word);
        iv_words = (IndexView) findViewById(R.id.iv_words);
        //设置监听字母下标索引的变化
        iv_words.setOnIndexChangeListener(new MyOnIndexChangListener());
        //准备数据
        initData();
        //设置适配器
        adapter = new IndexAdapter();
        lv_main.setAdapter(adapter);
    }

    private void updateWord(String word) {
        tv_word.setVisibility(View.VISIBLE);
        tv_word.setText(word);
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                //也是允许在主线程
                System.out.println(Thread.currentThread().getName() + "-------------");
                tv_word.setVisibility(View.GONE);
            }
        }, 3000);
    }

    class MyOnIndexChangListener implements IndexView.OnIndexChangeListener {

        @Override
        public void onIndexChange(String word) {
            updateWord(word);
            updateListView(word);
        }
    }

    private void updateListView(String word) {
        for(int i=0;i<persons.size();i++){
            String listWord = persons.get(i).getPinyin().substring(0,1);
            if(word.equals(listWord)){
                //i刚好为listView中的位置
                lv_main.setSelection(i);//此时默认定位到最后一个
                return;//使定位到第一个
            }
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {

        persons.add(new Person("张晓飞"));
        persons.add(new Person("杨光福"));
        persons.add(new Person("胡继群"));
        persons.add(new Person("刘畅"));

        persons.add(new Person("钟泽兴"));
        persons.add(new Person("尹革新"));
        persons.add(new Person("安传鑫"));
        persons.add(new Person("张骞壬"));

        persons.add(new Person("温松"));
        persons.add(new Person("李凤秋"));
        persons.add(new Person("刘甫"));
        persons.add(new Person("娄全超"));
        persons.add(new Person("张猛"));

        persons.add(new Person("王英杰"));
        persons.add(new Person("李振南"));
        persons.add(new Person("孙仁政"));
        persons.add(new Person("唐春雷"));
        persons.add(new Person("牛鹏伟"));
        persons.add(new Person("姜宇航"));

        persons.add(new Person("刘挺"));
        persons.add(new Person("张洪瑞"));
        persons.add(new Person("张建忠"));
        persons.add(new Person("侯亚帅"));
        persons.add(new Person("刘帅"));

        persons.add(new Person("乔竞飞"));
        persons.add(new Person("徐雨健"));
        persons.add(new Person("吴亮"));
        persons.add(new Person("王兆霖"));

        persons.add(new Person("阿三"));
        persons.add(new Person("李博俊"));


        //排序
        Collections.sort(persons, new Comparator<Person>() {
            @Override
            public int compare(Person lhs, Person rhs) {
                return lhs.getPinyin().compareTo(rhs.getPinyin());
            }
        });
    }

    class IndexAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return persons.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.item_main, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_word = (TextView) convertView.findViewById(R.id.tv_word);
                viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String name = persons.get(position).getName();
            String word = persons.get(position).getPinyin().substring(0, 1);
            viewHolder.tv_word.setText(word);
            viewHolder.tv_name.setText(name);
            if (position == 0) {
                viewHolder.tv_word.setVisibility(View.VISIBLE);
            } else {
                //得到前一个位置对应的字母，如果当前的字母和上一个相同，隐藏；否则就显示
                String preWord = persons.get(position - 1).getPinyin().substring(0,1);
                if (word.equals(preWord)) {
                    viewHolder.tv_word.setVisibility(View.GONE);
                } else {
                    viewHolder.tv_word.setVisibility(View.VISIBLE);
                }

            }
            return convertView;
        }
    }

    static class ViewHolder {
        TextView tv_word;
        TextView tv_name;
    }
}
