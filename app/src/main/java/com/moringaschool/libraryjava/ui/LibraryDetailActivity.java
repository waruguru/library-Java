package com.moringaschool.libraryjava.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.moringaschool.ebeautyparlor.R;
import com.moringaschool.ebeautyparlor.adapters.ParlorPagerAdapter;
import com.moringaschool.ebeautyparlor.models.BeautyParlor;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LibraryDetailActivity extends AppCompatActivity {
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    private ParlorPagerAdapter adapterViewPager;
    List<BeautyParlor> mParlor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parlor_detail);
        ButterKnife.bind(this);

        mParlor = Parcels.unwrap(getIntent().getParcelableExtra("parlors"));
        int startingPosition = getIntent().getIntExtra("position", 0);

        adapterViewPager = new ParlorPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mParlor);
        mViewPager.setAdapter(adapterViewPager);
        mViewPager.setCurrentItem(startingPosition);
    }

}