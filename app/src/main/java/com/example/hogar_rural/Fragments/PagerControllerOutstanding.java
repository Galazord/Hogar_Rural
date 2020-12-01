package com.example.hogar_rural.Fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerControllerOutstanding extends FragmentPagerAdapter {

    //--> VARIABLES
    int numsOfTabs;
    String destine;

    //--> CONSTRUCTOR
    public PagerControllerOutstanding(@NonNull FragmentManager fm, int behavior, String destine) {
        super(fm, behavior);
        this.numsOfTabs = behavior;
        this.destine = destine;
    }

    //--> MÉTODOS
    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch(position){

            case 0:
                return new MyNewsFragment(destine);
            case 1:
                return new MyLowpricesFragment(destine);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numsOfTabs;
    }
}
