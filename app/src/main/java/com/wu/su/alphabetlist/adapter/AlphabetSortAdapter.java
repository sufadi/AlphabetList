package com.wu.su.alphabetlist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.wu.su.alphabetlist.R;
import com.wu.su.alphabetlist.domain.SortModel;

import java.util.List;

/**
 * 按照名称首字母进行排序的adapter
 */
public class AlphabetSortAdapter extends BaseAdapter implements SectionIndexer {

    private List<SortModel> list = null;
    private Context mContext;

    public AlphabetSortAdapter(Context mContext, List<SortModel> list) {
        this.mContext = mContext;
        this.list = list;
    }



    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    final static class ViewHolder {
        TextView tv_fistletters;
        TextView tv_info;
    }

    // 更新ListView
    public void updateListView(List<SortModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final SortModel mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.alphabet_list_item, null);
            viewHolder.tv_fistletters = (TextView) view.findViewById(R.id.tv_fistletters);
            viewHolder.tv_info = (TextView) view.findViewById(R.id.tv_info);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        // 根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.tv_fistletters.setVisibility(View.VISIBLE);
            viewHolder.tv_fistletters.setText(mContent.fistLetter);
        } else {
            viewHolder.tv_fistletters.setVisibility(View.GONE);
        }
        viewHolder.tv_info.setText(this.list.get(position).info);
        return view;

    }


    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return list.get(position).fistLetter.charAt(0);
    }

    /**
     * 获取第一次出现该首字母的List所在的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).fistLetter;
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 根据ListView的位置获取对应的首字母
     */
    public String getAlpha(int position) {
        return list.get(position).fistLetter;
    }

}
