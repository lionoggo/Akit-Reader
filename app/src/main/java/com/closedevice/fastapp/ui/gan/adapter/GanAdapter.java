package com.closedevice.fastapp.ui.gan.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.closedevice.fastapp.R;
import com.closedevice.fastapp.base.adapter.ListBaseAdapter;
import com.closedevice.fastapp.model.response.gan.GanItem;

import butterknife.Bind;
import butterknife.ButterKnife;


public class GanAdapter extends ListBaseAdapter<GanItem> {

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.item_gan, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        GanItem item = mDatas.get(position);
        vh.title.setText(item.getDesc());
        vh.author.setText(item.getWho());

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_tech_title)
        TextView title;
        @Bind(R.id.tv_tech_author)
        TextView author;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
