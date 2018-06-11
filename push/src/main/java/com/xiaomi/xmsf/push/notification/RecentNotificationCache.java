package com.xiaomi.xmsf.push.notification;

import android.app.Notification;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;

/**
 * @author zts
 */
public class RecentNotificationCache {


    private volatile static RecentNotificationCache cache = null;
    private LruCache<String, NotificationItem> cacheInstance;

    private RecentNotificationCache() {
        cacheInstance = new LruCache<>(10);
    }

    public static RecentNotificationCache getInstance() {
        if (cache == null) {
            synchronized (RecentNotificationCache.class) {
                if (cache == null) {
                    cache = new RecentNotificationCache();
                }
            }
        }
        return cache;
    }


    public void put(NotificationItem item) {
        cacheInstance.put(buildNotificationKey(item), item);
    }

    public boolean shouldFilter(Notification notification) {
        NotificationItem item = getNotificationItem("", notification);
        return cacheInstance.get(buildNotificationKey(item)) != null;
    }

    private static String buildNotificationKey(@NonNull NotificationItem item) {
        int maxTitleLen = item.getTitle().length() < 10 ? item.getTitle().length() : 10;
        int maxContentLen = item.getContent().length() < 20 ? item.getContent().length() : 20;
        String title = item.getTitle().subSequence(0, maxTitleLen).toString();
        String content = item.getContent().subSequence(0, maxContentLen).toString();
        return title + content;
    }


    @Nullable
    public static NotificationItem getNotificationItem(String packageName, Notification notification) {
        if (notification == null) {
            return null;
        }

        Bundle extras = notification.extras;
        if (extras == null || extras.size() == 0) {
            return null;
        }

        CharSequence titleText = notification.extras.getCharSequence(Notification.EXTRA_TITLE);
        if (titleText == null) {
            titleText = notification.extras.getCharSequence(Notification.EXTRA_TITLE_BIG);
        }

        CharSequence contentText = notification.extras.getCharSequence(Notification.EXTRA_TEXT);
        if (contentText == null) {
            contentText = notification.extras.getCharSequence(Notification.EXTRA_BIG_TEXT);
        }

        if (titleText == null || contentText == null) {
            return null;
        }

        return new NotificationItem(packageName, titleText, contentText);
    }


}
