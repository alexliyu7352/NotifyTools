package org.github.yippee.notifytools;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.classic.clearprocesses.*;

import org.github.yippee.notifytools.bean.Heweather7bean;
import org.github.yippee.notifytools.service.HeWeatherService;
import org.github.yippee.notifytools.service.JumpService;
import org.github.yippee.notifytools.service.SimService;
import org.github.yippee.notifytools.service.VcardService;
import org.github.yippee.notifytools.utils.Logs;

/**
 * Created by sf on 2017/1/6.
 */

public class NotifyService extends Service{
    private Logs log = Logs.getLogger(this.getClass());
    NotificationManager noteMng;
    private static final int NOTIFICATION_FLAG = 1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        log.d("onStartCommand");
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        log.d("onCreate");
        super.onCreate();
        noteMng = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new
                NotificationCompat.Builder(this);


        builder.setTicker("Hello RemotesViews!");// 收到通知的时候用于显示于屏幕顶部通知栏的内容
        builder.setSmallIcon(R.drawable.ic_notify);// 设置通知小图标,在下拉之前显示的图标
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_notify));// 落下后显示的图标
        builder.setWhen(System.currentTimeMillis());
//        builder.setOngoing(true);// 不能被用户x掉，会一直显示，如音乐播放等
//        builder.setAutoCancel(true);// 自动取消
//        builder.setOnlyAlertOnce(true);// 只alert一次
//        builder.setDefaults(Notification.DEFAULT_ALL);

//        myNotify.icon=R.drawable.ic_notify;
//        myNotify.tickerText = "";
//        myNotify.when = System.currentTimeMillis();
//
        RemoteViews rv = new RemoteViews(getPackageName(),
                R.layout.notification);



        builder.setCustomBigContentView(rv);

//        myNotify.bigContentView=rv;
//        myNotify.contentView =rv;
//        rv.setOnClickPendingIntent();
        Notification myNotify = builder.build();
        myNotify.flags = Notification.FLAG_NO_CLEAR;// 不能够自动清除

        PendingIntent piWeather=PendingIntent.getService(this, 0, new Intent(this, HeWeatherService.class), PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.btWeather,piWeather);

        PendingIntent piVcard=PendingIntent.getService(this, 0, new Intent(this, VcardService.class), PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.btVcard,piVcard);

        PendingIntent piSimInfo=PendingIntent.getService(this, 0, new Intent(this, SimService.class), PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.btSimInfo,piSimInfo);

        Intent intent = new Intent(this,com.classic.clearprocesses.ForceStopActivity.class);//"com.classic.clearprocesses.ForceStopActivity");
      // intent.setClassName("com.classic.clearprocesses", "com.classic.clearprocesses.ForceStopActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//      this.startActivity(intent);
        PendingIntent psProcee=PendingIntent.getActivity(this,0,intent, Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        rv.setOnClickPendingIntent(R.id.btStop,psProcee);

        PendingIntent piJump=PendingIntent.getService(this, 0, new Intent(this, JumpService.class), PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.btJump,piJump);

        //设置进度条，最大值 为100,当前值为0，最后一个参数为true时显示条纹
        //        myNotify.contentView.setProgressBar(R.id.pb, 100,0, false);
//        myNotify.contentView.setProgressBar(R.id.pb, 100,50, false);
        noteMng.notify(NOTIFICATION_FLAG, myNotify);
    }

    @Override
    public void onDestroy() {
        log.d("onDestroy");
        super.onDestroy();
    }
}
