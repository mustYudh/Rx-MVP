package com.htgames.rxmvp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDexApplication;

import com.htgames.rxmvp.utils.CrashHandler;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by yudenghao on 2017/9/15.
 */

public class DemoApplication extends MultiDexApplication {
    private static Context context = null;

    //greendao相关
    private SQLiteDatabase db;
//    private DaoMaster mDaoMaster;
//    private static DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        initDao();
//        initConfig();
    }

    private void initDao() {
        //数据库的配置
//        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(getContext(), "demo_dao", null);
//        db = devOpenHelper.getWritableDatabase();
//        mDaoMaster = new DaoMaster(db);
//        mDaoSession = mDaoMaster.newSession();

    }

    private void initConfig() {
//        RetrofitService.init();
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag("My custom tag")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
        } else {
            CrashHandler.getInstance().init(context);
        }
    }

    public static Context getContext() {
        return context;
    }
}
