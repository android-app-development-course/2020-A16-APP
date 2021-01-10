package com.xxx.justice.java;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.xxx.justice.R;
import com.xxx.justice.UTIL;
import com.xxx.justice.view.EditActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class UniversityFragment extends Fragment {
    private static final float MIN_SCALE = 0.75f;
    private static final float MIN_ALPHA = 0.75f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_university, null);
        final com.xxx.justice.java.VerticalViewPager verticalViewPager = layout.findViewById(R.id.verticalviewpager);
        final VerticalTabLayout tablayout = layout.findViewById(R.id.tablayout);
        layout.findViewById(R.id.toEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), EditActivity.class));
            }
        });
        tablayout.setTabAdapter(new MyTabAdapter());
        tablayout.addOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabView tab, int position) {
                verticalViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselected(TabView tab, int position) {
            }
        });
        verticalViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tablayout.setTabSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        verticalViewPager.setAdapter(new DummyAdapter(getFragmentManager()));
        verticalViewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.pagemargin));
        verticalViewPager.setPageMarginDrawable(new ColorDrawable(getResources().getColor(android.R.color.holo_green_dark)));
        verticalViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NotNull View view, float position) {
                int pageWidth = view.getWidth();
                int pageHeight = view.getHeight();
                if (position < -1) { // [-Infinity,-1)
                    view.setAlpha(0);
                } else if (position <= 1) { // [-1,1]
                    float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                    float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                    float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                    if (position < 0) {
                        view.setTranslationY(vertMargin - horzMargin / 2);
                    } else {
                        view.setTranslationY(-vertMargin + horzMargin / 2);
                    }
                    view.setScaleX(scaleFactor);
                    view.setScaleY(scaleFactor);
                    view.setAlpha(MIN_ALPHA +
                            (scaleFactor - MIN_SCALE) /
                                    (1 - MIN_SCALE) * (1 - MIN_ALPHA));
                } else { // (1,+Infinity]
                    view.setAlpha(0);
                }
            }
        });
        return layout;
    }

    public static class DummyAdapter extends FragmentPagerAdapter {
        List<PlaceholderFragment> fragments = new ArrayList<>();

        public DummyAdapter(FragmentManager fm) {
            super(fm);
            for (int i = 0; i < 34; i++) {
                fragments.add(PlaceholderFragment.newInstance(i));
            }
        }

        @NotNull
        @Override
        public Fragment getItem(int position) {
            //return PlaceholderFragment.newInstance(position + 1);
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return 34;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "PAGE 0";
                case 1:
                    return "PAGE 1";
                case 2:
                    return "PAGE 2";
                case 3:
                    return "PAGE 3";
                case 4:
                    return "PAGE 4";
            }
            return null;
        }
    }

    public static class PlaceholderFragment extends Fragment {
        String[] array = new String[]{"university 1", "university 2", "university 3",
                "university 4", "university 5"};
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_layout, container, false);
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 0:
                    break;
                case 1:
//                    rootView.setBackgroundColor(Color.BLACK);
                    break;
                case 2:
//                    rootView.setBackgroundColor(Color.BLUE);
                    break;
                case 3:
//                    rootView.setBackgroundColor(Color.GREEN);
                    break;
                case 4:
                    array = UTIL.INSTANCE.getUniversity();
                    break;
                default:
                    break;
            }
            final ListView listView = rootView.findViewById(R.id.listView);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getContext(), com.xxx.justice.view.PostActivity.class);
                    intent.putExtra("university", array[position]);
                    Objects.requireNonNull(getContext()).startActivity(intent);
                }
            });
            listView.setAdapter(new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.list_item, R.id.universityName, array));
            return rootView;
        }
    }

    class MyTabAdapter implements TabAdapter {
        List<String> titles;

        {
            titles = new ArrayList<>();
            Collections.addAll(titles, "北京", "安徽", "澳门", "重庆", "广东", "福建", "甘肃",
                    "广西", "贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南", "吉林",
                    "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西", "陕西", "上海",
                    "四川", "台湾", "天津", "西藏", "香港", "新疆", "云南", "浙江");
        }

        @Override
        public int getCount() {
            return 34;
        }

        @Override
        public int getBadge(int position) {
            if (position == 34) return position;
            return 0;
        }

        @Override
        public QTabView.TabIcon getIcon(int position) {
            return null;
        }

        @Override
        public QTabView.TabTitle getTitle(int position) {
            return new QTabView.TabTitle.Builder(getContext())
                    .setContent(titles.get(position))
                    .setTextColor(Color.BLUE, Color.BLACK)
                    .build();
        }

        @Override
        public int getBackground(int position) {
            return 0;
        }
    }
}
