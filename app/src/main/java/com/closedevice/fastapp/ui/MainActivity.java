package com.closedevice.fastapp.ui;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.closedevice.fastapp.AppContext;
import com.closedevice.fastapp.R;
import com.closedevice.fastapp.base.ui.BaseActivity;

public class MainActivity extends BaseActivity implements TabHost.OnTabChangeListener {

    private LinearLayout mPagerWrapper;
    private int mTagHeight;
    private FragmentTabHost mTabHost;
    private long mLastExitTime;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mPagerWrapper = (LinearLayout) findViewById(R.id.pager_wrapper);
        mTagHeight = 0;
        mPagerWrapper.setPadding(0, getActionBarSize() + mTagHeight, 0, 0);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mTabHost.getTabWidget().setShowDividers(0);
        initTabs();
        mTabHost.setCurrentTab(0);
        mTabHost.setOnTabChangedListener(this);

    }

    private void initTabs() {
        MainTabs[] tabs = MainTabs.values();
        for (MainTabs tab : tabs) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(tab.getNameRes()));

            View indicator = inflateView(R.layout.view_tab_main_indicator);
            ImageView icon = (ImageView) indicator.findViewById(R.id.tab_icon);
            icon.setImageResource(tab.getIconRes());
            TextView title = (TextView) indicator.findViewById(R.id.tab_titile);
            title.setText(getString(tab.getNameRes()));

            tabSpec.setIndicator(indicator);
            tabSpec.setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String tag) {
                    return new View(MainActivity.this);
                }
            });

            mTabHost.addTab(tabSpec, tab.getClazz(), null);
        }
    }

    @Override
    public void onTabChanged(String tabId) {
        final int tabCount = mTabHost.getTabWidget().getTabCount();
        for (int i = 0; i < tabCount; i++) {
            View tab = mTabHost.getTabWidget().getChildAt(i);
            if (i == mTabHost.getCurrentTab()) {
                tab.findViewById(R.id.tab_icon).setSelected(true);
                tab.findViewById(R.id.tab_titile).setSelected(true);
                changeActionBarTitle(i);
            } else {
                tab.findViewById(R.id.tab_icon).setSelected(false);
                tab.findViewById(R.id.tab_titile).setSelected(false);

            }
        }

        supportInvalidateOptionsMenu();
    }

    private void changeActionBarTitle(int i) {
    }


    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() - mLastExitTime < 2000) {
            super.onBackPressed();
        } else {
            mLastExitTime = System.currentTimeMillis();

            AppContext.toastShort(R.string.tip_click_back_again_to_exist);
        }
    }
}
