package com.github.kuweiguge.cleancodesweep.notifications;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

/**
 * 通知工具类
 */
public class Notifier {
    /**
     * 通知组
     */
    private static final String NOTIFICATION_GROUP = "Clean Code Sweep Notification Group";

    /**
     * 通知错误
     *
     * @param project 当前项目
     * @param content 内容
     */
    public static void notifyError(@Nullable Project project,
                                   String content) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup(NOTIFICATION_GROUP)
                .createNotification(content, NotificationType.ERROR)
                .notify(project);
    }

    /**
     * 通知信息
     *
     * @param project 当前项目
     * @param content 内容
     */
    public static void notifyInformation(@Nullable Project project,
                                         String content) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup(NOTIFICATION_GROUP)
                .createNotification(content, NotificationType.INFORMATION)
                .notify(project);
    }

    /**
     * 通知警告
     * @param project 当前项目
     * @param content 内容
     */
    public static void notifyWarning(@Nullable Project project,
                                         String content) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup(NOTIFICATION_GROUP)
                .createNotification(content, NotificationType.WARNING)
                .notify(project);
    }

}