package com.example.guoyao.myapplication;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class ListitemAdapter extends BaseAdapter {

    //ListItem对应的视图类
    public class ViewHolder{
        public TextView mTitle;
        public TextView mSubTitile;
        public CheckBox mCheckBox;
    };
    //ListItem对应的数据类
    public static class DataHolder{
        public String titleStr;
        public String subTitleStr;
        public boolean checked;

        public DataHolder(String title,String subTitle,boolean check){
            titleStr = title;
            subTitleStr = subTitle;
            checked=check;
        }
    }

    private List<DataHolder> mList;
    private Context mContext;
    private LayoutInflater mInflater;
    public ListitemAdapter(Context context,List<DataHolder> list){
        mList = list;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            holder=new ViewHolder();

            convertView = mInflater.inflate(R.layout.check_list_item, null);
            holder.mTitle = (TextView)convertView.findViewById(R.id.title);
            holder.mSubTitile = (TextView)convertView.findViewById(R.id.subtitle);
            holder.mCheckBox = (CheckBox)convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        }else {

            holder = (ViewHolder)convertView.getTag();
        }

        holder.mTitle.setText((String)mList.get(position).titleStr);
        holder.mSubTitile.setText((String)mList.get(position).subTitleStr);
        holder.mCheckBox.setChecked(mList.get(position).checked);
        return convertView;
    }
}