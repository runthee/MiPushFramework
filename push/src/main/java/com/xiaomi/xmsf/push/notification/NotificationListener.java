package com.xiaomi.xmsf.push.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.xiaomi.xmsf.BuildConfig;

/**
 * @author zts
 */
@TargetApi(19)
public class NotificationListener extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        String packageName = sbn.getPackageName();

        if (BuildConfig.APPLICATION_ID.equals(packageName)) {
            return;
        }

        Notification notification = sbn.getNotification();
        NotificationItem notificationItem = RecentNotificationCache.getNotificationItem(packageName, notification);
        if (notificationItem == null) {
            return;
        }

        RecentNotificationCache.getInstance().put(notificationItem);

    }


}
