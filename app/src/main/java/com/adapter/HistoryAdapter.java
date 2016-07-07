package com.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.myapplication2.R;
import com.modle.History;

import java.util.LinkedList;

/**
 * Created by user on 2016/5/13.
 */
public class HistoryAdapter extends BaseAdapter{
    LinkedList<History> list;
    LayoutInflater inflater;
    Context context;

    public HistoryAdapter(LinkedList<History> list, Context context) {
        this.list = list;
        inflater=LayoutInflater.from(context);
        this.context=context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        History history=list.get(position);
        ViewHolde vh=null;
        if (convertView==null){
            convertView=inflater.inflate(R.layout.history_lv_view,null);
            vh=new ViewHolde();
            vh.style= (TextView) convertView.findViewById(R.id.style);
            vh.money= (TextView) convertView.findViewById(R.id.money);
            vh.mtime= (TextView) convertView.findViewById(R.id.mtime);
            vh.trueorfalse= (TextView) convertView.findViewById(R.id.trueorfalse);
            convertView.setTag(vh);
        }else{
            vh= (ViewHolde) convertView.getTag();
        }
        if (history.getMoney()!=null && !history.getMoney().equals("")){
                     vh.money.setVisibility(View.VISIBLE);
                     vh.money.setText(history.getMoney());
        }else{
            vh.money.setVisibility(View.GONE);
        }
        if (history.isResult()){
            vh.trueorfalse.setText("成功");
            Drawable drawable= context.getResources().getDrawable(R.drawable.true_img);
/// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            vh.trueorfalse.setCompoundDrawables(drawable,null,null,null);
        }else{
            vh.trueorfalse.setText("失败");
            Drawable drawable= context.getResources().getDrawable(R.drawable.failure_img);
/// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            vh.trueorfalse.setCompoundDrawables(drawable,null,null,null);
        }
        if (history.isStyle()){
            vh.style.setText("转账");
            Drawable drawable= context.getResources().getDrawable(R.drawable.caution);
/// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            vh.style.setCompoundDrawables(drawable,null,null,null);
        }else{
            vh.style.setText("验证");
            Drawable drawable= context.getResources().getDrawable(R.drawable.money_img);
/// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            vh.style.setCompoundDrawables(drawable,null,null,null);
        }
        vh.mtime.setText(history.getTime());
        return convertView;
    }

    private class ViewHolde{
     TextView style,money,trueorfalse,mtime;
    }
}
