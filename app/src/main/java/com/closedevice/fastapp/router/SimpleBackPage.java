package com.closedevice.fastapp.router;

import com.closedevice.fastapp.ui.common.DetailFragment;
import com.closedevice.fastapp.ui.setting.fragment.AboutFragment;
import com.closedevice.fastapp.ui.setting.fragment.LikeReadFragment;
import com.closedevice.fastapp.ui.setting.fragment.RecentReadFragment;
import com.closedevice.fastapp.ui.setting.fragment.SettingFragment;


public enum SimpleBackPage {
    ABOUT(0, "详情", AboutFragment.class),
    SETTING(1, "设置", SettingFragment.class),
    DETAIL(2, "详情", DetailFragment.class),
    RECENT_READ(3, "最近阅读", RecentReadFragment.class),
    LIKE_READ(4, "我的收藏", LikeReadFragment.class),;

    private int id;
    private String title;
    private Class<?> clazz;

    SimpleBackPage(int id, String title, Class<?> clazz) {
        this.id = id;
        this.title = title;
        this.clazz = clazz;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public static SimpleBackPage getPage(int id) {
        for (SimpleBackPage page : values()) {
            if (page.getId() == id) {
                return page;
            }

        }
        return null;
    }
}
