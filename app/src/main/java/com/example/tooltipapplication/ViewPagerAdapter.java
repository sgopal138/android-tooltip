package com.example.tooltipapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return appropriate fragment based on tab position
        switch (position) {
            case 0:
//                return new XmlComposeFragment(); // This contains the Compose dialog
            case 1:
                return new OtherFragment();      // Placeholder for another tab
            default:
                return new Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Number of tabs
    }
}

