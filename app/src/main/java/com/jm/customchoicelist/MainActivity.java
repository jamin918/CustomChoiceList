package com.jm.customchoicelist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView mSingleListView;
    private ListView mMultipleListView;
    private ArrayList<String> mTestData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSingleListView = (ListView) findViewById(R.id.lv_singleChoice);
        mMultipleListView = (ListView) findViewById(R.id.lv_multipleChoice);

        mSingleListView.setAdapter(new ArrayAdapter<String>(this, R.layout.item_lv_single_choice, initData()));

        mMultipleListView.setAdapter(new CustomAdapter());

        mSingleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int checkedItemPosition = mSingleListView.getCheckedItemPosition();
                Toast.makeText(MainActivity.this, "you chose item " + checkedItemPosition, Toast.LENGTH_SHORT).show();
            }
        });


        mMultipleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SparseBooleanArray checkedItemPositions = mMultipleListView.getCheckedItemPositions();
                boolean isChecked = checkedItemPositions.get(position);
                Toast.makeText(MainActivity.this, "item " + position + " isChecked=" + isChecked, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private ArrayList<String> initData() {
        mTestData = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            mTestData.add("测试数据"+i);
        }

        return mTestData;
    }


    private class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTestData.size();
        }

        @Override
        public String getItem(int position) {
            return mTestData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mTestData.get(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_lv_multiple_choice, container, false);
            }

            ((TextView) convertView.findViewById(R.id.tv_content))
                    .setText(getItem(position));
            return convertView;
        }
    }
}
