package com.tecknobit.nova.helpers.utils.ui;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.app.PendingIntent.getActivity;
import static android.content.Context.NOTIFICATION_SERVICE;
import static android.media.RingtoneManager.getDefaultUri;
import static com.tecknobit.nova.ui.activities.navigation.Splashscreen.DESTINATION_KEY;
import static com.tecknobit.novacore.records.NovaItem.IDENTIFIER_KEY;
import static com.tecknobit.novacore.records.NovaNotification.NOTIFICATIONS_KEY;
import static com.tecknobit.novacore.records.User.PROJECTS_KEY;
import static com.tecknobit.novacore.records.project.Project.PROJECT_KEY;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.RingtoneManager;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.tecknobit.nova.R;
import com.tecknobit.nova.helpers.storage.LocalSessionHelper;
import com.tecknobit.nova.ui.activities.navigation.Splashscreen;
import com.tecknobit.novacore.helpers.LocalSessionUtils;
import com.tecknobit.novacore.records.NovaNotification;
import com.tecknobit.novacore.records.User;
import com.tecknobit.novacore.records.release.Release.ReleaseStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class NotificationsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Objects.equals(intent.getAction(), Intent.ACTION_BOOT_COMPLETED)) {
            NotificationsHelper notificationsHelper = new NotificationsHelper(context);
            notificationsHelper.scheduleAndExec();
        }
    }

    public static class NotificationsHelper {

        private static final String WORKER_NAME = "notificationsChecker";

        private static final String NOVA_NOTIFICATIONS_CHANNEL_ID = NOTIFICATIONS_KEY;

        private static final OneTimeWorkRequest workerRequest = new OneTimeWorkRequest.Builder(
                NotificationsWorker.class)
                .setConstraints(new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build())
                .addTag(WORKER_NAME)
                .build();

        private final NotificationManager notificationManager;

        private final Context context;

        private final WorkManager workManager;

        public NotificationsHelper(Context context) {
            this.context = context;
            workManager = WorkManager.getInstance(context);
            notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            createNotificationsChannel();
        }

        private void createNotificationsChannel() {
            if(notificationManager.getNotificationChannel(NOVA_NOTIFICATIONS_CHANNEL_ID) == null) {
                NotificationChannel channel = new NotificationChannel(
                        NOVA_NOTIFICATIONS_CHANNEL_ID,
                        context.getString(R.string.release_events),
                        NotificationManager.IMPORTANCE_HIGH
                );
                AudioAttributes.Builder audioAttributes = new AudioAttributes.Builder();
                audioAttributes.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION);
                audioAttributes.setUsage(AudioAttributes.USAGE_NOTIFICATION);
                channel.setSound(getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), audioAttributes.build());
                channel.setDescription(context.getString(R.string.notifications_channel_description));
                notificationManager.createNotificationChannel(channel);
            }
        }

        public void scheduleAndExec() {
            scheduleRoutine();
            execCheckRoutine();
        }

        public void scheduleRoutine() {
            workManager.enqueue(workerRequest);
        }

        public void execCheckRoutine() {
            // TODO: TO REMOVE
            NovaNotification notificationt = null;
            try {
                notificationt = new NovaNotification(
                        "oor",
                        "https://www.vaielettrico.it/wp-content/uploads/2023/04/IMG_0118-1.jpg",
                        new User(new JSONObject().put("role", User.Role.Vendor.name())),
                        "gagag",
                        ReleaseStatus.Approved,
                        "v. 1.0.0",
                        false
                );
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            sendNotification(notificationt, getDestination(notificationt));
            try(LocalSessionHelper localSessionHelper = new LocalSessionHelper(context)) {
                for (LocalSessionUtils.NovaSession session : localSessionHelper.getSessions()) {
                    //TODO: MAKE THE REQUEST TO FETCH THE NOTIFICATIONS THEN
                    List<NovaNotification> notifications = List.of();
                    for(NovaNotification notification : notifications)
                        if(!notification.isSent())
                            sendNotification(notification, getDestination(notification));
                }
            }
        }

        private Intent getDestination(NovaNotification notification) {
            Intent destination = new Intent(context, Splashscreen.class);
            if(notification.getReleaseId() == null)
                destination.putExtra(DESTINATION_KEY, PROJECTS_KEY);
            else
                destination.putExtra(DESTINATION_KEY, PROJECT_KEY);
            destination.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            destination.putExtra(IDENTIFIER_KEY, notification.getUser().getId());
            return destination;
        }

        private void sendNotification(NovaNotification notification, Intent destination) {
            Notification.Builder builder = new Notification.Builder(context, NOVA_NOTIFICATIONS_CHANNEL_ID);
            // TODO: 02/04/2024 USE THE REAL APPLICATION ICON
            builder.setSmallIcon(R.drawable.ic_launcher_background);
            String releaseVersion = notification.getReleaseVersion();
            String text = context.getString(getContentText(notification.getReleaseId(),
                    notification.getStatus()));
            if(releaseVersion != null) {
                builder.setContentTitle(releaseVersion);
                builder.setContentText(text);
            } else
                builder.setContentTitle(text);
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(new URL(notification.getProjectLogo())
                        .openConnection().getInputStream());
                builder.setLargeIcon(bitmap);
            } catch (IOException ignored) {
            }
            builder.setContentIntent(getActivity(context, 0, destination, FLAG_UPDATE_CURRENT));
            builder.setAutoCancel(true);
            notificationManager.notify(new Random().nextInt(), builder.build());
        }

        private int getContentText(String releaseId, ReleaseStatus releaseStatus) {
            if(releaseStatus == null) {
                if(releaseId == null)
                    return R.string.the_project_has_been_deleted;
                else
                    return R.string.the_release_has_been_deleted;
            } else {
                return switch (releaseStatus) {
                    case New -> R.string.new_release_has_been_created;
                    case Verifying -> R.string.new_assets_are_ready_to_be_tested;
                    case Rejected -> R.string.the_release_has_been_rejected;
                    case Approved -> R.string.the_release_has_been_approved;
                    case Alpha -> R.string.the_release_has_been_promoted_to_alpha;
                    case Beta -> R.string.the_release_has_been_promoted_to_beta;
                    case Latest -> R.string.the_release_has_been_promoted_to_latest;
                    default -> -1;
                };
            }
        }
        
    }

    public static class NotificationsWorker extends Worker {

        private final NotificationsHelper notificationsHelper;

        public NotificationsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
            super(context, workerParams);
            notificationsHelper = new NotificationsHelper(context);
        }

        @NonNull
        @Override
        public Result doWork() {
            try {
                Thread.sleep(TimeUnit.MINUTES.toMillis(5));
                notificationsHelper.execCheckRoutine();
                doWork();
            } catch (InterruptedException e) {
                return Result.retry();
            }
            return Result.success();
        }

    }

}
