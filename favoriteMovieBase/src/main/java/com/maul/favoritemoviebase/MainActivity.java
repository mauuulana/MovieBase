package com.maul.favoritemoviebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.maul.favoritemoviebase.adapter.ViewPagerAdapter;
import com.maul.favoritemoviebase.fragment.MovieFavBaseFragment;
import com.maul.favoritemoviebase.fragment.TVShowFavBaseFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout= findViewById(R.id.tab);
        ViewPager viewPager = findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.AddFragment(new MovieFavBaseFragment(), getString(R.string.movies));
        adapter.AddFragment(new TVShowFavBaseFragment(), getString(R.string.tvshows));

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
