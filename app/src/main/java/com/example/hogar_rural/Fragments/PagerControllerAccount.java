package com.example.hogar_rural.Fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerControllerAccount extends FragmentPagerAdapter {

    //--> VARIABLES
    int numsOfTabs;

    //--> CONSTRUCTOR
    public PagerControllerAccount(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.numsOfTabs = behavior;
    }

    //--> MÉTODOS
    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch(position){

            case 0:
                return new MyProfileFragment();
            case 1:
                return new MyHousesFragment();
            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return numsOfTabs;
    }
}
