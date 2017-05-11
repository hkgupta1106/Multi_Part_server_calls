package com.skeleton.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.skeleton.R;
import com.skeleton.fragment.SigninFragment;
import com.skeleton.fragment.SignupFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * main activity
 */
public class SignupAndSigninActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_and_signin);

        ViewPager vpPager = (ViewPager) findViewById(R.id.vp_pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new SignupFragment());
        fragments.add(new SigninFragment());

        PagerAdapter pagerAdapter = new com.skeleton.adapter.PagerAdapter(getSupportFragmentManager(), fragments);
        vpPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(vpPager);
    }
}
