package com.htgames.rxmvp.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.htgames.rxmvp.DemoApplication;
import com.htgames.rxmvp.MainActivity;
import com.htgames.rxmvp.R;


/**
 * Created by yudenghao on 2017/7/27.
 */

// TODO: 2017/9/17 图形化通知栏 BaseRecycleView  看书  BottomNavGavionBar  Retrofit

public class NotifiManager {
    private static final NotifiManager ourInstance = new NotifiManager();
    private static Context context;
    private static NotificationManager mNotificationManager;
    private static NotificationCompat.Builder mBuilder;
    private int requestCode = 0;
    public int notifyId = 100;


    public static NotifiManager getInstance(Context mContext) {
        context = mContext;
        setNotification();
        return ourInstance;
    }

    private NotifiManager() {
    }

    public static void setNotification() {
        Uri sound = Uri.parse("android.resource://com.htgames.rxmvp/raw/msg");
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle(context.getString(R.string.app_name))
                .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setSound(sound)
                .setSmallIcon(R.mipmap.ic_launcher);
    }


    /**
     * 显示通知栏点击跳转到指定Activity
     */
    public void showIntentActivityNotify(String message, int notifyId) {
        this.notifyId = notifyId;
        // Notification.FLAG_ONGOING_EVENT --设置常驻 Flag;Notification.FLAG_AUTO_CANCEL 通知栏上点击此通知后自动清除此通知
//        notification.flags = Notification.FLAG_AUTO_CANCEL; //在通知栏上点击此通知后自动清除此通知
        mBuilder.setAutoCancel(true)//点击后让通知将消失
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(message)
                .setTicker(message);
        //点击的意图ACTION是跳转到Intent
        Intent resultIntent;
        resultIntent = new Intent(context, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        mNotificationManager.notify(notifyId, mBuilder.build());
        requestCode = requestCode + 1;
    }


    public void  createCustomNotification() {
        Intent intent = new Intent(context, MainActivity.class);
        //如果第二次获取并且请求码相同,如果原来已解决创建了这个PendingIntent,则复用这个类,并更新intent
        int flag = PendingIntent.FLAG_UPDATE_CURRENT;
        PendingIntent contentIntent = PendingIntent.getActivity(context, requestCode, intent, flag);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("自定义布局通知")
                .setWhen(System.currentTimeMillis())
                .setContentTitle("歌曲名")
                .setContentText("歌手") //以上的设置是在为了兼容3.0之前的版本
                .setContentIntent(contentIntent)
                .setContent(getRemoteView())//自定义通知栏view的api是在3.0之后生效
                .setFullScreenIntent(contentIntent, false);
        Notification notification = builder.build();
        //打开通知
        mNotificationManager.notify(requestCode, notification);
    }


    public RemoteViews getRemoteView() {
        RemoteViews remoteViews = new RemoteViews(DemoApplication.getContext().getPackageName(), R.layout.custom_notification);
        remoteViews.setTextViewText(R.id.tv_content_title, "歌曲名");
        remoteViews.setTextViewText(R.id.tv_content_text, "歌手");
        //打开上一首
        remoteViews.setOnClickPendingIntent(R.id.btn_pre, null);
        //打开下一首
        remoteViews.setOnClickPendingIntent(R.id.btn_next, null);
        //点击整体布局时,打开播放器
        remoteViews.setOnClickPendingIntent(R.id.ll_root, null);
        return remoteViews;
    }

    /**
     * @获取默认的pendingIntent,为了防止2.3及以下版本报错
     * @flags属性: 在顶部常驻:Notification.FLAG_ONGOING_EVENT
     * 点击去除： Notification.FLAG_AUTO_CANCEL
     */
    public static PendingIntent getDefalutIntent(int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, new Intent(), flags);
        return pendingIntent;
    }




    public void clearAppNotify() {
        clearNotify(notifyId);
    }


    /**
     * 清除所有通知栏
     */
    public void clearAllNotify() {
        mNotificationManager.cancelAll();// 删除你发的所有通知
    }


    /**
     * 清除当前创建的通知栏
     */
    public void clearNotify(int notifyId) {
        mNotificationManager.cancel(notifyId);//删除一个特定的通知ID对应的通知
    }
}
