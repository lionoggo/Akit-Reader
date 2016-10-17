package com.closedevice.fastapp.base.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.closedevice.fastapp.AppContext;
import com.closedevice.fastapp.R;
import com.closedevice.fastapp.api.exception.ApiException;
import com.closedevice.fastapp.base.BaseObserver;
import com.closedevice.fastapp.base.adapter.ListBaseAdapter;
import com.closedevice.fastapp.model.base.Entity;
import com.closedevice.fastapp.model.base.ListEntity;
import com.closedevice.fastapp.model.response.BaseResult;
import com.closedevice.fastapp.view.empty.EmptyLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.adapter.rxjava.HttpException;

@SuppressLint("NewApi")
public abstract class BaseListFragment<T extends Entity> extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener, OnScrollListener {

    public static final String BUNDLE_KEY_CATALOG = "BUNDLE_KEY_CATALOG";


    @Bind(R.id.swiperefreshlayout)
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.listview)
    protected ListView mListView;

    protected ListBaseAdapter<T> mAdapter;

    @Bind(R.id.error_layout)
    protected EmptyLayout mErrorLayout;

    protected int mStoreEmptyState = -1;


    protected int mCurrentPage = 0;

    protected int mCatalog = 1;


    protected BaseObserver mSubscriber = new BaseObserver<BaseResult<?>>() {

        @Override
        public void onError(Throwable e) {
            if (e instanceof HttpException) {
                executeOnLoadDataError(null);
            } else if (e instanceof IOException) {
                executeOnLoadDataError(null);
            } else if (e instanceof ApiException) {
                ApiException exception = (ApiException) e;
                if (exception.isTokenExpried()) {
                    if (isAdded()) {
                        //handle ui
                    }
                } else {
                    AppContext.toast(exception.getMessage());
                }
            } else {
                executeOnLoadDataError(null);
            }

            executeOnLoadFinish();

        }

        @Override
        public void onNext(BaseResult<?> result) {
            if (!result.isOk()) {
                executeOnLoadDataError(null);
                return;
            }

            if (isAdded()) {
                if (mState == STATE_REFRESH) {
                    onRefreshNetworkSuccess();
                }
                List<T> list;
                if (result.getData() instanceof ListEntity<?>) {
                    ListEntity<T> data = (ListEntity<T>) result.getData();
                    list = data.getList();
                } else {
                    list = (List<T>) result.getData();
                }
                executeOnLoadDataSuccess(list);
            }

        }

        @Override
        public void onCompleted() {
            executeOnLoadFinish();
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pull_refresh_listview;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflater = inflater;
        View view = inflater.inflate(getLayoutId(), container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initView(view);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mCatalog = args.getInt(BUNDLE_KEY_CATALOG, 0);
        }
    }

    @Override
    public void initView(View view) {
        mSwipeRefreshLayout.setOnRefreshListener(this);
//        mSwipeRefreshLayout.setColorSchemeResources(
//                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
//                R.color.swiperefresh_color3, R.color.swiperefresh_color4);

        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentPage = 0;
                mState = STATE_REFRESH;
                mErrorLayout.setErrorType(EmptyLayout.STATE_NETWORK_LOADING);
                sendRequest();
            }
        });


        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);

        if (mAdapter != null) {
            mListView.setAdapter(mAdapter);
            mErrorLayout.setErrorType(EmptyLayout.STATE_HIDE_LAYOUT);
        } else {
            mAdapter = getListAdapter();
            mListView.setAdapter(mAdapter);

            if (requestDataIfViewCreated()) {
                mErrorLayout.setErrorType(EmptyLayout.STATE_NETWORK_LOADING);
                mState = STATE_NONE;
                sendRequest();
            } else {
                mErrorLayout.setErrorType(EmptyLayout.STATE_HIDE_LAYOUT);
            }

        }
        if (mStoreEmptyState != -1) {
            mErrorLayout.setErrorType(mStoreEmptyState);
        }
    }

    @Override
    public void onDestroyView() {
        mStoreEmptyState = mErrorLayout.getErrorState();
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onRefresh() {
        if (mState == STATE_REFRESH || mState == STATE_LOADMORE) {
            return;
        }
        mListView.setSelection(0);
        setRefreshLoadingState();
        mCurrentPage = 0;
        mState = STATE_REFRESH;
        sendRequest();
    }


    public void refresh() {
        if (mState == STATE_REFRESH || mState == STATE_LOADMORE) {
            return;
        }
        mListView.setSelection(0);
        mCurrentPage = 0;
        mState = STATE_NONE;
        mErrorLayout.setErrorType(EmptyLayout.STATE_NETWORK_LOADING);
        sendRequest();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
    }


    protected abstract ListBaseAdapter<T> getListAdapter();

    protected boolean requestDataIfViewCreated() {
        return true;
    }


    protected void sendRequest() {
    }

    protected boolean needShowEmptyNoData() {
        return true;
    }

    protected boolean compareTo(List<? extends Entity> data, Entity enity) {
        int s = data.size();
        if (enity != null) {
            for (int i = 0; i < s; i++) {

                if (enity.getId() != null && enity.getId().equals(data.get(i).getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    protected int getPageSize() {
        return 10;
    }

    protected void onRefreshNetworkSuccess() {
    }


    protected void executeOnLoadDataSuccess(List<T> data) {
        if (data == null) {
            data = new ArrayList<T>();
        }

        mErrorLayout.setErrorType(EmptyLayout.STATE_HIDE_LAYOUT);
        if (mCurrentPage == 0) {
            mAdapter.clear();
        }

        for (int i = 0; i < data.size(); i++) {
            if (compareTo(mAdapter.getData(), data.get(i))) {
                data.remove(i);
                i--;
            }
        }


        int adapterState = ListBaseAdapter.STATE_EMPTY_ITEM;

        if ((mAdapter.getCount() + data.size()) == 0) {
            adapterState = ListBaseAdapter.STATE_EMPTY_ITEM;
        } else if (data.size() == 0 || (data.size() < getPageSize() && mCurrentPage == 0)) {
            adapterState = ListBaseAdapter.STATE_NO_MORE;
            mAdapter.notifyDataSetChanged();
        } else {
            adapterState = ListBaseAdapter.STATE_LOAD_MORE;
        }
        mAdapter.setState(adapterState);
        mAdapter.addItems(data);
        if (mAdapter.getCount() == 1) {
            if (needShowEmptyNoData()) {
                mErrorLayout.setErrorType(EmptyLayout.STATE_NODATA);
            } else {
                mAdapter.setState(ListBaseAdapter.STATE_EMPTY_ITEM);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    protected void executeOnLoadDataError(String error) {
        if (mCurrentPage == 0) {

            mErrorLayout.setErrorType(EmptyLayout.STATE_NETWORK_ERROR);
        } else {
            mCurrentPage--;
            mErrorLayout.setErrorType(EmptyLayout.STATE_HIDE_LAYOUT);
            mAdapter.setState(ListBaseAdapter.STATE_NETWORK_ERROR);
            mAdapter.notifyDataSetChanged();
        }
    }

    protected void executeOnLoadFinish() {
        setRefreshLoadedState();
        mState = STATE_NONE;
    }

    protected void setRefreshLoadingState() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
            mSwipeRefreshLayout.setEnabled(false);
        }
    }


    protected void setRefreshLoadedState() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(true);
        }
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mAdapter == null || mAdapter.getCount() == 0) {
            return;
        }
        if (mState == STATE_LOADMORE || mState == STATE_REFRESH) {
            return;
        }
        // 判断是否滚动到底部
        boolean scrollEnd = false;
        try {
            if (view.getPositionForView(mAdapter.getFooterView()) == view
                    .getLastVisiblePosition())
                scrollEnd = true;
        } catch (Exception e) {
            scrollEnd = false;
        }

        if (mState == STATE_NONE && scrollEnd) {
            if (mAdapter.getState() == ListBaseAdapter.STATE_LOAD_MORE
                    || mAdapter.getState() == ListBaseAdapter.STATE_NETWORK_ERROR) {
                mCurrentPage++;
                mState = STATE_LOADMORE;
                sendRequest();
                mAdapter.setFooterViewLoading();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
    }


}
