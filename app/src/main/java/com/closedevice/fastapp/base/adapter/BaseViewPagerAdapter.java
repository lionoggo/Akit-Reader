package com.closedevice.fastapp.base.adapter;

import android.support.v4.app.FragmentManager;

import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;


public abstract class BaseViewPagerAdapter extends CacheFragmentStatePagerAdapter {

    public BaseViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
}
