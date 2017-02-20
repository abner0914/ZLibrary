package com.zlibrary.base.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.orhanobut.logger.Logger;
import com.zlibrary.base.db.ZDBHelper;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class ZApplication extends Application {

    /**
     * 全局LApplication唯一实例
     */
    private static ZApplication instance;
    /**
     * 全局上下文对象
     */
    private Context mContext;
    /**
     * 将所有FragmentActivity存放在链表中，便于管理
     */
    private List<FragmentActivity> activityList;
    /**
     * 全局FragmentManager实例
     */
    private FragmentManager mFragmentManager;
    /**
     * 全局数据库对象
     */
    private ZDBHelper mDBHelper;

    /**
     * 全局数据库名称
     */
    private String appDBName;

    /**
     * 全局数据库版本
     */
    private int appDBVersion;

    /**
     * 全局数据库创建表的语句数组
     */
    private String[] appDBCreateTables;

    /**
     * 全局数据库删除表的语句数组
     */
    private String[] appDBDropTables;

    /**
     * 全局的是否打开DEBUG模式<br />
     * 默认为true
     */
    private boolean openDebugMode = true;

    /**
     * 全局APP标识名称
     */
    private String appName;

    /**
     * 全局APP服务器主路径
     */
    private String appServiceUrl;

    /**
     * 全局数据缓存数量<br />
     * 默认为20
     */
    private int cacheCount = 20;

    /**
     * 是否允许销毁所有Activity<br />
     * 默认为false
     */
    private boolean destroyActivitys = false;

    /**
     * 全局缓存文件存储文件对象
     */
    private File cacheFile;

    /**
     * 全局Session的Key值
     */
    private String SessionKey = ZConfig.SESSION_KEY;

    /**
     * 全局Session的Value值
     */
    private String SessionValue;

    /**
     * 全局屏幕宽度
     */
    private int mDiaplayWidth;

    /**
     * 全局屏幕高度
     */
    private int mDiaplayHeight;

    /**
     * 获取一个LApplication的实例
     *
     * @return
     */
    public static synchronized ZApplication getInstance() {
        if (instance == null) {
            instance = new ZApplication();
        }
        return instance;
    }

    /**
     * 设置一个LApplication的实例
     *
     * @param app
     */
    public static void setLApplication(ZApplication app) {
        instance = app;
    }

    /**
     * @return 获取上下文对象
     */
    public Context getContext() {
        if (mContext == null) {
            mContext = this;
        }
        return mContext;
    }

    /**
     * 设置上下文对象
     *
     * @param mContext
     */
    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 添加一个Activity到数组里
     *
     * @param activity
     */
    public void addActivity(FragmentActivity activity) {
        if (this.activityList == null) {
            activityList = new LinkedList<FragmentActivity>();
        }
        this.activityList.add(activity);
    }

    /**
     * 删除一个Activity在数组里
     *
     * @param activity
     */
    public void delActivity(FragmentActivity activity) {
        if (activityList != null) {
            activityList.remove(activity);
        }
    }

    public int getActivityCount() {
        if (activityList != null) {
            return activityList.size();
        }
        return 0;
    }

    public void clearActivity() {
        if (activityList != null) {
            for (Activity activity : activityList) {
                activity.finish();
            }
            activityList.clear();
        }
    }

    /**
     * @return 获取一个 FragmentManager 对象
     */
    public FragmentManager getFragmentManager() {
        return this.mFragmentManager;
    }

    /**
     * 设置一个 FragmentManager 对象
     *
     * @param fm
     */
    public void setFragmentManager(FragmentManager fm) {
        this.mFragmentManager = fm;
    }

    /**
     * 获取一个DBHelper实例并打开数据库
     *
     * @return
     */
    public ZDBHelper getDBHelper() {
        if (mDBHelper == null) {
            mDBHelper = ZDBHelper.Instance(getContext(), appDBName,
                    appDBVersion);
            mDBHelper.setTableCreateText(appDBCreateTables);
            mDBHelper.setTableDropText(appDBDropTables);
            mDBHelper.openDataBase();
        }
        return mDBHelper;
    }

    /**
     * 关闭数据库连接
     */
    public void closeDBHelper() {
        if (mDBHelper != null) {
            mDBHelper.closeDataBase();
        }
    }

    /**
     * 设置DBHelper实例信息
     *
     * @param dbName         ：数据库名称
     * @param dbVersion      ：数据库版本
     * @param dbCreateTables ：数据库创建表语句集合
     * @param dbDropTables   ：数据库删除表语句集合
     */
    public void setDBInfo(String dbName, int dbVersion,
                          String[] dbCreateTables, String[] dbDropTables) {
        this.appDBName = dbName;
        this.appDBVersion = dbVersion;
        this.appDBCreateTables = dbCreateTables;
        this.appDBDropTables = dbDropTables;
    }

    /**
     * 获取是否启用Debug模式
     *
     * @return
     */
    public boolean getIsOpenDebugMode() {
        return openDebugMode;
    }

    /**
     * 设置是否启用Debug模式
     *
     * @param openDebugMode
     */
    public void setOpenDebugMode(boolean openDebugMode) {
        this.openDebugMode = openDebugMode;
    }

    /**
     * 获取应用标识名称
     *
     * @return
     */
    public String getAppName() {
        return appName;
    }

    /**
     * 设置应用标识名称
     *
     * @param appName
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * 获取应用主服务器路径
     *
     * @return
     */
    public String getAppServiceUrl() {
        return appServiceUrl;
    }

    /**
     * 设置应用主服务器路径
     *
     * @param appServiceUrl
     */
    public void setAppServiceUrl(String appServiceUrl) {
        this.appServiceUrl = appServiceUrl;
    }

    /**
     * 获取应用内存缓存数量
     *
     * @return
     */
    public int getCacheCount() {
        return cacheCount;
    }

    /**
     * 设置应用内存缓存数量
     *
     * @param cacheCount
     */
    public void setCacheCount(int cacheCount) {
        this.cacheCount = cacheCount;
    }

    /**
     * 获取应用是否允许销毁所有Activity
     *
     * @return
     */
    public boolean getIsDestroyActivitys() {
        return destroyActivitys;
    }

    /**
     * 设置应用是否允许销毁所有Activity
     *
     * @param destroyActivitys
     */
    public void setDestroyActivitys(boolean destroyActivitys) {
        this.destroyActivitys = destroyActivitys;
    }

    /**
     * 获取应用文件缓存路径文件
     *
     * @return
     */
    public File getCacheFile() {
        if (cacheFile == null) {
            cacheFile = getCacheDir();
        }
        return cacheFile;
    }

    /**
     * 设置应用文件缓存路径文件
     *
     * @param cacheFile
     */
    public void setCacheFile(File cacheFile) {
        this.cacheFile = cacheFile;
    }

    /**
     * 获取Session的Key值
     *
     * @return
     */
    public String getSessionKey() {
        return this.SessionKey;
    }

    /**
     * 设置Session的Key值
     *
     * @param sessionKey
     */
    public void setSessionKey(String sessionKey) {
        this.SessionKey = sessionKey;
    }

    /**
     * 获取Session的Value值
     *
     * @return
     */
    public String getSessionValue() {
        return this.SessionValue;
    }

    /**
     * 设置Session的Value值
     *
     * @param sessionValue
     */
    public void setSessionValue(String sessionValue) {
        this.SessionValue = sessionValue;
    }


    /**
     * 完全退出应用<br />
     * 需要设置destroyActivitys为true
     */
    public void exitApp() {
        if (destroyActivitys) {
            for (FragmentActivity f : activityList) {
                f.finish();
            }
            System.exit(0);
        }
    }

    /**
     * 初始化日志参数
     *
     * @param tag
     */
    public void initLogger(String tag) {
        Logger.init(tag)                      // default PRETTYLOGGER or use just init()
                .methodCount(3)                 // default 2
                .hideThreadInfo()               // default shown
                .methodOffset(2);                // default 0
    }

}
