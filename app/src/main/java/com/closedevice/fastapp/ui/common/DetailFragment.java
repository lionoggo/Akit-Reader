package com.closedevice.fastapp.ui.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.closedevice.fastapp.AppContext;
import com.closedevice.fastapp.R;
import com.closedevice.fastapp.base.ui.BaseActivity;
import com.closedevice.fastapp.base.ui.BaseFragment;
import com.closedevice.fastapp.db.RealmHelper;
import com.closedevice.fastapp.model.LikeRecordRealm;
import com.closedevice.fastapp.util.OSUtil;
import com.closedevice.fastapp.view.empty.EmptyLayout;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;


public class DetailFragment extends BaseFragment {


    @Bind(R.id.webview)
    WebView webview;
    @Bind(R.id.error_layout)
    EmptyLayout errorLayout;
    private String oldUrl;
    private String url;
    private String mTitle;
    private String mPicUrl;
    private String mCtime;
    private String mFrom;
    private Boolean isLiked;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);

        ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    @Override
    public void initView(View view) {

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);
        if (AppContext.getInstance().isNoImageMode()) {
            settings.setBlockNetworkImage(true);
        }

        if (AppContext.getInstance().isAutoCacheMode()) {
            settings.setAppCacheEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setDatabaseEnabled(true);
            if (OSUtil.hasInternet()) {
                settings.setCacheMode(WebSettings.LOAD_DEFAULT);
            } else {
                settings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
            }
        }

        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return false;
            }


        });

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {

                    errorLayout.setErrorType(EmptyLayout.STATE_HIDE_LAYOUT);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                setTitle(title);
            }
        });
        errorLayout.setErrorType(EmptyLayout.STATE_NETWORK_LOADING);
        webview.loadUrl(url);


    }


    private void setLikeState(final MenuItem item) {
        RealmHelper realmHelper = new RealmHelper(AppContext.context());
        Observable.just(realmHelper.findLikeRecord(mTitle)).map(new Func1<LikeRecordRealm, Boolean>() {
            @Override
            public Boolean call(LikeRecordRealm likeRecordRealm) {
                return likeRecordRealm != null;
            }
        }).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean liked) {
                isLiked = liked;
                if (liked) {
                    item.setIcon(R.drawable.ic_toolbar_like_p);
                } else {
                    item.setIcon(R.drawable.ic_toolbar_like_n);
                }
            }
        });

    }

    @Override
    public void initData() {
        Bundle args = getArguments();
        url = args.getString("url");
        oldUrl = url;
        mCtime = args.getString("ctime");
        mFrom = args.getString("from");
        mTitle = args.getString("title");
        mPicUrl = args.getString("picUrl");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        webview.destroy();
        ButterKnife.unbind(this);
    }

    private void setTitle(String title) {
        if (getActivity() == null) return;
        BaseActivity activity = (BaseActivity) getActivity();
        activity.setActionBarTitle(title);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.my_like, menu);
        MenuItem mLikeItem = menu.findItem(R.id.my_like);
        setLikeState(mLikeItem);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        RealmHelper realmHelper = new RealmHelper(AppContext.context());

        if (isLiked) {
            realmHelper.removeLikeRecord(mTitle);
        } else {
            if (realmHelper.findLikeRecord(mTitle) == null) {
                LikeRecordRealm record = new LikeRecordRealm();
                record.setId(url);
                record.setImage(mCtime);
                record.setImage(mPicUrl);
                record.setTitle(mTitle);
                realmHelper.addLikeRecord(record);
            }

        }

        setLikeState(item);


        return true;
    }


}
