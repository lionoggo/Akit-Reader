package com.closedevice.fastapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.closedevice.fastapp.base.ui.BaseActivity;
import com.closedevice.fastapp.base.ui.BaseFragment;
import com.closedevice.fastapp.router.SimpleBackPage;

import java.lang.ref.WeakReference;


public class SimpleBackActivity extends BaseActivity {

    public final static String BUNDLE_KEY_PAGE = "BUNDLE_KEY_PAGE";
    public final static String BUNDLE_KEY_ARGS = "BUNDLE_KEY_ARGS";
    private static final String TAG = "FLAG_TAG";
    private WeakReference<Fragment> mFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Intent data = getIntent();
        if (data == null) {
            throw new RuntimeException(
                    "you must provide a page info to display");
        }
        int pageValue = data.getIntExtra(BUNDLE_KEY_PAGE, 0);
        SimpleBackPage page = SimpleBackPage.getPage(pageValue);
        if (page == null) {
            throw new IllegalArgumentException("can not find page by value:"
                    + pageValue);
        }

        setActionBarTitle(page.getTitle());

        try {
            Fragment fragment = (Fragment) page.getClazz().newInstance();

            Bundle args = data.getBundleExtra(BUNDLE_KEY_ARGS);
            if (args != null) {
                fragment.setArguments(args);
            }

            FragmentTransaction trans = getSupportFragmentManager()
                    .beginTransaction();
            trans.replace(R.id.container, fragment, TAG);
            trans.commit();

            mFragment = new WeakReference<Fragment>(fragment);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(
                    "generate fragment error. by value:" + pageValue);
        }
    }

    @Override
    public void onBackPressed() {
        if (mFragment != null && mFragment.get() != null
                && mFragment.get() instanceof BaseFragment) {
            BaseFragment bf = (BaseFragment) mFragment.get();
            if (!bf.onBackPressed()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
    }


    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
