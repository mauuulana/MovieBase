package com.maul.moviebase.utils;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.maul.moviebase.R;
import com.maul.moviebase.activity.DetailActivityMovie;
import com.maul.moviebase.modal.Movie;
import com.maul.moviebase.modal.MovieData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static com.maul.moviebase.activity.DetailActivityMovie.EXTRA_MOVIES;

public class ReleaseReminder extends BroadcastReceiver {

    int ID_NOTIF = 3;
    SimpleDateFormat date;
    MovieData data;
    private ArrayList<Movie> listMovie = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date dates = new Date();
        final String released = date.format(dates);
        getReleasedMovie(context, released, released);
    }

    private void getReleasedMovie(final Context context, String string1, String string2) {
        data = MovieData.getInstance();
        data.getReleased(string1, string2, new MovieData.OnGetReleasedMovie() {
            @Override
            public void onSuccess(ArrayList<Movie> movies) {
                String title = movies.get(0).getTitle();
                String message = movies.get(0).getOverview();
                int id = movies.get(0).getId();
                listMovie.addAll(movies);
                for (Movie movie : movies) {
                    if (movie.getReleaseDate().equals(string1)) {
                        showNotif(context, title, message, id);
                        Log.d("releasedReminder", "Success" + movies);
                    }
                }
            }

            @Override
            public void onError() {
                String toast = Integer.toString((R.string.toast_error));
                Log.d("releaseNotifReminder", toast);
            }
        });

    }

    public void showNotif(Context context, String title, String message, int id) {
        String CHANNEL_ID = "channel_2";
        String CHANNEL_NAME = "Released channel";

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(context, DetailActivityMovie.class);
        intent.putExtra(EXTRA_MOVIES, listMovie.get(0));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setSound(alarmSound)
                .setSmallIcon(R.drawable.ic_logo_mini)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setColorized(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);

            channel.enableVibration(true);
            channel.setLightColor(R.color.colorPrimary);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setChannelId(CHANNEL_ID);


            Objects.requireNonNull(notificationManagerCompat).createNotificationChannel(channel);
        }

        Objects.requireNonNull(notificationManagerCompat).notify(id, builder.build());

    }

    public void cancelNotification(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_NOTIF, intent, 0);
        Objects.requireNonNull(alarmManager).cancel(pendingIntent);
    }

    public void setAlarm(Context context, String type, String time, String message) {
        cancelNotification(context);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseReminder.class);
        intent.putExtra("message", message);
        intent.putExtra("type", type);
        String[] timeArray = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_NOTIF, intent, 0);
        Objects.requireNonNull(alarmManager).setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }
}
