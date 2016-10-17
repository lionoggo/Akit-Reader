package com.closedevice.fastapp.ui.gan.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.closedevice.fastapp.R;
import com.closedevice.fastapp.base.adapter.BaseViewPagerAdapter;
import com.closedevice.fastapp.base.inter.IPagerFragment;
import com.closedevice.fastapp.ui.gan.adapter.MainTabPagerAdapter;
import com.closedevice.fastapp.view.tab.SlidingTabLayout;

/**
 * Created by God
 * on 2016/10/12.
 */

public class GanPagerFragment extends Fragment implements IPagerFragment {


    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;

    private BaseViewPagerAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewpager, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.pager_container);
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.stl_tabs);

        mSlidingTabLayout.setCustomTabView(R.layout.view_tab_gan_indicator, R.id.tv_media_name);
        Resources res = getResources();
        int color = res.getColor(R.color.main_red);
        mSlidingTabLayout.setSelectedIndicatorColors(color);
        mSlidingTabLayout.setDistributeEvenly(true);

        mViewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.view_pager_margin));

        if (mAdapter == null) {
            mAdapter = new MainTabPagerAdapter(getChildFragmentManager());

        }

        mViewPager.setOffscreenPageLimit(mAdapter.getCount());
        mViewPager.setAdapter(mAdapter);
        mSlidingTabLayout.setViewPager(mViewPager);
        return view;

    }

    @Override
    public BaseViewPagerAdapter getPagerAdapter() {
        return null;
    }

    @Override
    public ViewPager getViewPager() {
        return null;
    }

    @Override
    public Fragment getCurrent() {
        return null;
    }


}
