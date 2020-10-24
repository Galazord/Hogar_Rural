package com.example.hogar_rural.Fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerControllerAccount extends FragmentPagerAdapter {

    //--> VARIABLES
    int numsOfTabs;
    String destine;
    //--> CONSTRUCTOR
    public PagerControllerAccount(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.numsOfTabs = behavior;
    } //--> CONSTRUCTOR
    public PagerControllerAccount(@NonNull FragmentManager fm, int behavior,String destine) {
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
                return new MyProfileFragment(destine);
            case 1:
                return new MyHousesFragment();
            case 2:
                return new MyBookingsFragment();
            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return numsOfTabs;
    }
}
