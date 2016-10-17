package com.closedevice.fastapp.ui.gan;

import com.closedevice.fastapp.ui.gan.fragment.ArticleFragment;

/**
 * Created by God
 * on 2016/10/12.
 */

public enum GanMainTab {
    CATALOG_ANDROID(0, 0, "Android", ArticleFragment.class),
    CATALOG_IOS(1, 1, "ios", ArticleFragment.class),
    CATALOG_WEB(2, 2, "web", ArticleFragment.class),;

    private int id;
    private int catalog;
    private String title;
    private Class<?> clazz;

    GanMainTab(int id, int catalog, String title, Class<?> clazz) {
        this.id = id;
        this.catalog = catalog;
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

    public static GanMainTab getTab(int id) {
        for (GanMainTab tab : values()) {
            if (tab.getId() == id) {
                return tab;
            }
        }
        return CATALOG_ANDROID;
    }

    public int getCatalog() {
        return catalog;
    }

    public void setCatalog(int catalog) {
        this.catalog = catalog;
    }
}
