package com.maul.moviebase.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.maul.moviebase.R;


public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        CardView languageMenu, notifMenu;

        languageMenu = findViewById(R.id.change_language);
        languageMenu.setOnClickListener(this);

        notifMenu = findViewById(R.id.notification);
        notifMenu.setOnClickListener(this);

        ImageView backSetting =  findViewById(R.id.back_setting);
        backSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_language:
                Intent lIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(lIntent);
                break;

            case R.id.back_setting:
                onBackPressed();
                {
                    finish();
                }
                this.overridePendingTransition(R.anim.no_anim, R.anim.slide_left);
                break;

            case R.id.notification:
                Intent nIntent = new Intent(this, NotificationActivity.class);
                startActivity(nIntent);
                this.overridePendingTransition(R.anim.slide_right, R.anim.no_anim);
                break;
        }
    }
}
