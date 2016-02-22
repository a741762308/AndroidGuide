package com.jsqix.splash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jsqix.utils.ACache;

/**
 * Created by dq on 2016/2/22.
 */
public class BaseActivity extends AppCompatActivity{
    ACache aCache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aCache=ACache.get(this);
    }
}
