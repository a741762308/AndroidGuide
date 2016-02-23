# AndroidGuide
====
Android应用程序第一次安装启动时，我们希望给用户一些app的引导，实现引导页面也是有很多很多中方法，本例就用最常见的viewpage来实现，而且viewpage是支持切换动画的，因此可以做成比较酷炫的引导界面。
#效果图
![](/screenshot/shot.gif)
#布局文件
```
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#FFFFFF">


    <android.support.v4.view.ViewPager
        android:id="@+id/viewPage"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

    <FrameLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:layout_centerHorizontal="true"
        android:layout_marginBottom="22.0dip"
        android:gravity="center">


        <LinearLayout
            android:id="@+id/dotLayout"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:orientation="horizontal"/>

        <View
            android:id="@+id/cur_dot"
            android:layout_width="10dp"
            android:layout_height="10dp"
            android:background="@drawable/dot_focused" />
    </FrameLayout>

    <Button
        android:id="@+id/startBtn"
        android:layout_width="120dp"
        android:layout_height="40dp"
        android:layout_alignParentBottom="true"
        android:layout_centerHorizontal="true"
        android:layout_gravity="center_vertical"
        android:layout_marginBottom="40dp"
        android:layout_marginLeft="8dp"
        android:layout_marginRight="8dp"
        android:background="@drawable/rectangular_while_bule_line_bg"
        android:text="开始体验"
        android:visibility="gone"
        android:textColor="@color/top_tab_color"
        android:textSize="18sp" />
</RelativeLayout>
```
#代码
```java
public class GuideActivity extends BaseActivity {
    // 到达最后一张
    private static final int TO_THE_END = 0;
    // 离开最后一张
    private static final int LEAVE_FROM_END = 1;
    private LinearLayout dotContain; // 存储点的容器
    private int offset;              // 位移量
    private int curPos = 0;          // 记录当前的位置
    private ViewPager mViewPage;
    private List<View> dots = new ArrayList<View>();
    private List<View> guides = new ArrayList<View>();
    private  GuidePagerAdapter adapter;
    private View curDot;
    private Button startBtn;
    int[] ids = new int[]{R.mipmap.guide01, R.mipmap.guide02, R.mipmap.guide03, R.mipmap.guide04};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
    }

    private void initView() {
        mViewPage = (ViewPager) findViewById(R.id.viewPage);
        dotContain = (LinearLayout) findViewById(R.id.dotLayout);
        curDot = findViewById(R.id.cur_dot);
        startBtn= (Button) findViewById(R.id.startBtn);
        ImageView imageView = null;
        View dot = null;
        for (int i = 0; i < ids.length; i++) {
            imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setBackgroundResource(ids[i]);
            imageView.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            guides.add(imageView);

            dot=new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10),1.0f);
            params.setMargins(0, 0,DensityUtil.dip2px(this, 5), 0);
            dot.setLayoutParams(params);
            dot.setBackgroundResource(R.drawable.dot_normal);
            dotContain.addView(dot);
            dots.add(dot);
        }

        // 当curDot的所在的树形层次将要被绘出时此方法被调用
        curDot.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    public boolean onPreDraw() {
                        // 获取ImageView的宽度也就是点图片的宽度
                        offset = curDot.getWidth()+DensityUtil.dip2px(GuideActivity.this, 5);
                        return true;
                    }
                });
        adapter = new GuidePagerAdapter();
        mViewPage.setAdapter(adapter);
        //切换动画
        mViewPage.setPageTransformer(true, new TabletTransformer());
        mViewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int pos = position % ids.length;

                moveCursorTo(pos);

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
                aCache.put("guide",1+"");
                finish();
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
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


    /**
     * 移动指针到相邻的位置 动画
     *
     * @param position 指针的索引值
     */
    private void moveCursorTo(int position) {
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation tAnim =
                new TranslateAnimation(offset * curPos, offset * position, 0, 0);
        animationSet.addAnimation(tAnim);
        animationSet.setDuration(300);
        animationSet.setFillAfter(true);
        curDot.startAnimation(animationSet);
    }

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
```
#感谢
[动画ViewPagerTransforms](https://github.com/ToxicBakery/ViewPagerTransforms)

#License
    Copyright 2015 a741762308

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
