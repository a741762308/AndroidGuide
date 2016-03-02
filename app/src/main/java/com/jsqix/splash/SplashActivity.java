package com.jsqix.splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.jsqix.utils.StringUtils;

public class SplashActivity extends BaseActivity {
    private ImageView splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {
        splash = (ImageView) findViewById(R.id.splash);
        Animation anima = AnimationUtils.loadAnimation(this,
                R.anim.welcome_fade_in_scale);
        anima.setFillAfter(true);
        anima.setFillBefore(false);
        anima.setAnimationListener(new AnimationImpl());
        splash.startAnimation(anima);
    }

    private class AnimationImpl implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            skip(); // 动画结束后跳转到别的页面
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

    }

    private void skip() {
        finish();
        int cont = StringUtils.toInt(aCache.getAsString("guide"));
        if (cont == 0) {
            //通过pageindicator实现小圆点
            startActivity(new Intent(this, TabPageIndicator.class));
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
