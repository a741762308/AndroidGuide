package com.jsqix.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.ToxicBakery.viewpager.transforms.TabletTransformer;
import com.viewpagerindicator.PageIndicator;

import java.util.ArrayList;
import java.util.List;

public class TabPageIndicator extends BaseActivity {
    // 到达最后一张
    private static final int TO_THE_END = 0;
    // 离开最后一张
    private static final int LEAVE_FROM_END = 1;
    private int curPos = 0;          // 记录当前的位置
    private ViewPager mViewPage;
    private PageIndicator mIndicator;
    private Button startBtn;
    private GuidePagerAdapter adapter;
    private List<View> guides = new ArrayList<View>();
    int[] ids = new int[]{R.mipmap.guide01, R.mipmap.guide02, R.mipmap.guide03, R.mipmap.guide04};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_page_indicator);
        initView();
    }

    private void initView() {
        mViewPage = (ViewPager) findViewById(R.id.viewPage);
        mIndicator = (PageIndicator) findViewById(R.id.indicator);
        startBtn= (Button) findViewById(R.id.startBtn);
        ImageView imageView = null;
        for (int i = 0; i < ids.length; i++) {
            imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setBackgroundResource(ids[i]);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            guides.add(imageView);
        }
        adapter = new GuidePagerAdapter();
        mViewPage.setAdapter(adapter);
        mIndicator.setViewPager(mViewPage);
        //切换动画
        mViewPage.setPageTransformer(true, new TabletTransformer());
        mViewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int pos = position % ids.length;

                if (pos == ids.length - 1) {// 到最后一张了
                    handler.sendEmptyMessageDelayed(TO_THE_END, 250);

                } else if (curPos == ids.length - 1) {
                    handler.sendEmptyMessageDelayed(LEAVE_FROM_END, 100);
                }
                curPos = pos;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aCache.put("guide", 1 + "");
                finish();
                startActivity(new Intent(TabPageIndicator.this, MainActivity.class));
            }
        });

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == TO_THE_END)
                startBtn.setVisibility(View.VISIBLE);
            else if (msg.what == LEAVE_FROM_END)
                startBtn.setVisibility(View.GONE);
        }
    };


    class GuidePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return guides.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (object);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(guides.get(arg1 % guides.size()));
        }

        //初始化arg1位置的界面
        @Override
        public Object instantiateItem(View arg0, int arg1) {

            ((ViewPager) arg0).addView(guides.get(arg1), 0);

            return guides.get(arg1);
        }
    }
}
