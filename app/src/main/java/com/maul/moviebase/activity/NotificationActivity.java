package com.maul.moviebase.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.maul.moviebase.R;
import com.maul.moviebase.utils.DailyReminder;
import com.maul.moviebase.utils.PreferenceNotif;
import com.maul.moviebase.utils.ReleaseReminder;


import static com.maul.moviebase.R.id.back_notif;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {

    private DailyReminder dailyReminder;
    private ReleaseReminder releaseReminder;
    private PreferenceNotif preference;
    private SharedPreferences sReleased, sDaily;
    private SharedPreferences.Editor eReleased, eDaily;

    String TYPE_DAILY = "dailyRemind";
    String TYPE_RELEASE = "releasedRemind";
    String DAILY_REMINDER = "remindDaily";
    String RELEASE_REMINDER = "remindReleased";
    String KEY_RELEASE = "Released";
    String KEY_DAILY_REMINDER = "Daily";

    String timeDaily = "07:00";
    String timeReleased = "08:00";
    private SwitchCompat swDaily;
    private SwitchCompat swReleased;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        ImageView backNotif = findViewById(R.id.back_notif);
        backNotif.setOnClickListener(this);

        dailyReminder = new DailyReminder();
        releaseReminder = new ReleaseReminder();
        preference = new PreferenceNotif(this);
        swDaily = findViewById(R.id.daily_remind);
        swReleased = findViewById(R.id.released_remind);
        setPreference();
        setDaily();
        setReleased();
    }

    private void releasedOn() {
        String message = getResources().getString(R.string.released_remind);
        preference.setTimeRelease(timeReleased);
        preference.setReleaseMessage(message);
        releaseReminder.setAlarm(NotificationActivity.this, TYPE_RELEASE, timeReleased, message);
    }

    private void releaseOff() {
        releaseReminder.cancelNotification(NotificationActivity.this);
    }

    private void dailyOn() {
        String message = getResources().getString(R.string.daily_reminder);
        preference.setTimeDaily(timeDaily);
        preference.setDailyMessage(message);
        dailyReminder.setAlarm(NotificationActivity.this, TYPE_DAILY, timeDaily, message);
    }

    private void dailyOff() {
        dailyReminder.cancelNotif(NotificationActivity.this);
    }

    private void setPreference() {
        sDaily = getSharedPreferences(DAILY_REMINDER, MODE_PRIVATE);
        boolean checkDaily = sDaily.getBoolean(KEY_DAILY_REMINDER, false);
        swDaily.setChecked(checkDaily);
        sReleased = getSharedPreferences(RELEASE_REMINDER, MODE_PRIVATE);
        boolean checkReleased = sReleased.getBoolean(KEY_RELEASE, false);
        swReleased.setChecked(checkReleased);
    }

    public void setReleased() {
        eReleased = sReleased.edit();
        swReleased.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                eReleased.putBoolean(KEY_RELEASE, true);
                eReleased.apply();
                releasedOn();
            } else {
                eReleased.putBoolean(KEY_RELEASE, false);
                eReleased.commit();
                releaseOff();
            }
        });
    }

    public void setDaily() {
        eDaily = sDaily.edit();
        swDaily.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (isChecked) {
                eDaily.putBoolean(KEY_DAILY_REMINDER, true);
                eDaily.apply();
                dailyOn();
            } else {
                eDaily.putBoolean(KEY_DAILY_REMINDER, false);
                eDaily.commit();
                dailyOff();
            }
        }));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == back_notif) {
            onBackPressed();
            {
                finish();
            }
            this.overridePendingTransition(R.anim.no_anim, R.anim.slide_left);
        }
    }
}
