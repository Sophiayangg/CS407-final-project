package com.cs407.final_project;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class MyPagerAdapter extends FragmentStateAdapter {
    private List<Integer> imageList;

    public MyPagerAdapter(FragmentActivity fragmentActivity, List<Integer> imageList) {
        super(fragmentActivity);
        this.imageList = imageList;
    }

    @Override
    public Fragment createFragment(int position) {
        int imageResource = imageList.get(position);
        return ImageFragment.newInstance(imageResource);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
}

