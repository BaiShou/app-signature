package com.arnold;

import com.intellij.notification.*;

public class NotificationUtil {

    public static void info(String msg) {
        Notification notification =  NotificationGroupManager.getInstance()
                .getNotificationGroup("arnold.notification.balloon")
                .createNotification(msg, NotificationType.INFORMATION);
        Notifications.Bus.notify(notification);
    }

    public static void warning(String msg) {
        Notification notification =  NotificationGroupManager.getInstance()
                .getNotificationGroup("arnold.notification.balloon")
                .createNotification(msg, NotificationType.WARNING);
        Notifications.Bus.notify(notification);
    }

    public static void error(String msg) {
        Notification notification =  NotificationGroupManager.getInstance()
                .getNotificationGroup("arnold.notification.balloon")
                .createNotification(msg, NotificationType.ERROR);
        Notifications.Bus.notify(notification);
    }
}
