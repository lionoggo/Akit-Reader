package com.closedevice.fastapp.ui.gan.fragment;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;

import com.closedevice.fastapp.api.remote.ApiFactory;
import com.closedevice.fastapp.base.adapter.ListBaseAdapter;
import com.closedevice.fastapp.base.ui.BaseListFragment;
import com.closedevice.fastapp.model.response.gan.GanItem;
import com.closedevice.fastapp.router.Router;
import com.closedevice.fastapp.ui.gan.adapter.GanAdapter;
import com.trello.rxlifecycle.FragmentEvent;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ArticleFragment extends BaseListFragment<GanItem> {

    @Override
    protected void sendRequest() {
        String keyword = getSearchKeyword();
        ApiFactory.getGanApi().getArticles(keyword, getPageSize(), mCurrentPage).compose(this.bindUntilEvent(FragmentEvent.STOP))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mSubscriber);

    }

    @NonNull
    private String getSearchKeyword() {
        String keyword = "";
        switch (mCatalog) {
            case 0:
                keyword = "Android";
                break;
            case 1:
                keyword = "iOS";
                break;
            case 2:
                keyword = "前端";
                break;
        }
        return keyword;
    }

    @Override
    protected ListBaseAdapter getListAdapter() {
        GanAdapter adapter = new GanAdapter();
        return adapter;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GanItem item = mAdapter.getItem(position);
        if (item != null)
            Router.showDetail(getActivity(), item.getDesc(), item.getUrl(), "", "", "");
    }
}
