package com.closedevice.fastapp.ui.setting.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.closedevice.fastapp.R;
import com.closedevice.fastapp.base.adapter.ListBaseAdapter;
import com.closedevice.fastapp.model.LikeRecord;
import com.closedevice.fastapp.util.ImageLoaderUtil;
import com.closedevice.fastapp.view.SquareImageView;

import butterknife.Bind;
import butterknife.ButterKnife;


public class LikeReadAdapter extends ListBaseAdapter<LikeRecord> {
    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.item_like_article, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        LikeRecord record = mDatas.get(position);
        vh.title.setText(record.getTitle());
        if (record.getImage() != null)
            ImageLoaderUtil.load(parent.getContext(), record.getImage(), vh.image);
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.iv_article_image)
        SquareImageView image;
        @Bind(R.id.tv_article_title)
        TextView title;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
