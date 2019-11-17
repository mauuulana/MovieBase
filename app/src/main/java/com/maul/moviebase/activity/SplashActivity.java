package com.maul.moviebase.activity;

import android.content.Intent;
import android.view.WindowManager;

import com.daimajia.androidanimations.library.Techniques;
import com.maul.moviebase.MainActivity;
import com.maul.moviebase.R;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class SplashActivity extends AwesomeSplash {

    @Override
    public void initSplash(ConfigSplash configSplash){

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        configSplash.setBackgroundColor(R.color.colorPrimary);
        configSplash.setAnimCircularRevealDuration(3000);
        configSplash.setRevealFlagX(Flags.REVEAL_LEFT);
        configSplash.setRevealFlagX(Flags.REVEAL_BOTTOM);

        configSplash.setLogoSplash(R.drawable.ic_logo);
        configSplash.setAnimLogoSplashDuration(2000);
        configSplash.setAnimLogoSplashTechnique(Techniques.Bounce);

        configSplash.setTitleSplash("Movie Base");
        configSplash.setAnimTitleDuration(2000);
        configSplash.setAnimTitleTechnique(Techniques.Landing);
        configSplash.setTitleFont("fonts/bungee.ttf");
    }

    @Override
    public void animationsFinished(){
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
    }
}
