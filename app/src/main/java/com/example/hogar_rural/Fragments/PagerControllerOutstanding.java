package com.example.hogar_rural.Fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerControllerOutstanding extends FragmentPagerAdapter {

    //--> VARIABLES
    int numsOfTabs;

    //--> CONSTRUCTOR
    public PagerControllerOutstanding(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.numsOfTabs = behavior;
    }

    //--> MÉTODOS
    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch(position){

            case 0:
                return new MyNewsFragment();
            case 1:
                return new MyLowpricesFragment();
            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return numsOfTabs;
    }
}
