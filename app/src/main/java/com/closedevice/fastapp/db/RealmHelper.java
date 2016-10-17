package com.closedevice.fastapp.db;

import android.content.Context;

import com.closedevice.fastapp.AppConstant;
import com.closedevice.fastapp.model.LikeRecordRealm;
import com.closedevice.fastapp.model.ReadRecordRealm;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


public class RealmHelper {
    private static final String db_name = AppConstant.DB_NAME;
    private final Realm mRealm;

    public RealmHelper(Context context) {
        mRealm = Realm.getInstance(new RealmConfiguration.Builder(context).deleteRealmIfMigrationNeeded().name(db_name).build());

    }


    public void addReadRecord(ReadRecordRealm record) {
        mRealm.beginTransaction();
        mRealm.copyToRealm(record);
        mRealm.commitTransaction();

    }

    public ReadRecordRealm findReadRecord(String title) {
        ReadRecordRealm recordRealm = mRealm.where(ReadRecordRealm.class).equalTo("title", title).findFirst();
        return recordRealm;
    }

    public List<ReadRecordRealm> findAllReadRecord() {
        RealmResults<ReadRecordRealm> likeRecords = mRealm.where(ReadRecordRealm.class).findAllSorted("time");
        return mRealm.copyToRealm(likeRecords);
    }

    public void removeAllReadRecord() {
        mRealm.beginTransaction();
        mRealm.clear(ReadRecordRealm.class);
        mRealm.commitTransaction();
    }


    public void addLikeRecord(LikeRecordRealm record) {
        mRealm.beginTransaction();
        mRealm.copyToRealm(record);
        mRealm.commitTransaction();

    }

    public void removeLikeRecord(String title) {
        LikeRecordRealm record = mRealm.where(LikeRecordRealm.class).equalTo("title", title).findFirst();
        mRealm.beginTransaction();
        record.removeFromRealm();
        mRealm.commitTransaction();

    }

    public void removeAllLikeRecord() {
        mRealm.beginTransaction();
        mRealm.clear(LikeRecordRealm.class);
        mRealm.commitTransaction();
    }

    public LikeRecordRealm findLikeRecord(String title) {
        LikeRecordRealm record = mRealm.where(LikeRecordRealm.class).equalTo("title", title).findFirst();
        return record;
    }

    public List<LikeRecordRealm> findAllLikeRecord() {
        RealmResults<LikeRecordRealm> likeRecords = mRealm.where(LikeRecordRealm.class).findAllSorted("time");
        return mRealm.copyToRealm(likeRecords);
    }
}
