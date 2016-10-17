package com.closedevice.fastapp.ui.weixin.fragment;

import android.view.View;
import android.widget.AdapterView;

import com.closedevice.fastapp.AppConstant;
import com.closedevice.fastapp.AppContext;
import com.closedevice.fastapp.api.remote.ApiFactory;
import com.closedevice.fastapp.base.adapter.ListBaseAdapter;
import com.closedevice.fastapp.base.ui.BaseListFragment;
import com.closedevice.fastapp.db.RealmHelper;
import com.closedevice.fastapp.model.ReadRecordRealm;
import com.closedevice.fastapp.model.response.wx.WXItem;
import com.closedevice.fastapp.router.Router;
import com.closedevice.fastapp.ui.weixin.adapter.WXAdapter;
import com.trello.rxlifecycle.FragmentEvent;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by God
 * on 2016/10/7.
 */

public class WXFragment extends BaseListFragment<WXItem> {

    @Override
    protected void sendRequest() {
        ApiFactory.getWXApi().getWXHot(AppConstant.KEY_WX, getPageSize(), mCurrentPage + 1).compose(this.bindUntilEvent(FragmentEvent.STOP))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mSubscriber);
    }

    @Override
    protected ListBaseAdapter<WXItem> getListAdapter() {
        WXAdapter adapter = new WXAdapter();
        return adapter;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        WXItem wxItem = mAdapter.getItem(position);
        if (wxItem != null) {
            Router.showDetail(getActivity(), wxItem.getTitle(), wxItem.getUrl(), wxItem.getPicUrl(), wxItem.getDescription(), wxItem.getCtime());
            RealmHelper realmHelper = new RealmHelper(AppContext.context());
            if (realmHelper.findReadRecord(wxItem.getTitle()) != null) {
                return;
            }
            ReadRecordRealm recordRealm = new ReadRecordRealm();
            recordRealm.setId(wxItem.getId());
            recordRealm.setTitle(wxItem.getTitle());
            recordRealm.setTime(System.currentTimeMillis());
            recordRealm.setImage(wxItem.getPicUrl());
            realmHelper.addReadRecord(recordRealm);


        }
    }
}
