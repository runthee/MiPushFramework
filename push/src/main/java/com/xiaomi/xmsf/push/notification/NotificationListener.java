package com.xiaomi.xmsf.push.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.xiaomi.xmsf.BuildConfig;

import me.pqpo.librarylog4a.Log4a;

/**
 * @author zts
 */
@TargetApi(19)
public class NotificationListener extends NotificationListenerService {
    private static final String TAG = NotificationListener.class.getSimpleName();

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        String packageName = sbn.getPackageName();

        Log4a.i(TAG, String.format("StatusBarNotification %s ", sbn.toString()));

        if (BuildConfig.APPLICATION_ID.equals(packageName)) {
            return;
        }

        Notification notification = sbn.getNotification();
        NotificationItem notificationItem = RecentNotificationCache.getNotificationItem(packageName, notification);
        if (notificationItem == null) {
            return;
        }


        Log4a.i(TAG, String.format("onNotificationPosted %s ", notificationItem));


        RecentNotificationCache.getInstance().put(notificationItem);

    }


}
