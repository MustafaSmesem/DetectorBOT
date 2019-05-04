package com.tensorflow.lite.examples.detection;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsAccessorAdapter extends FragmentPagerAdapter {

    public TabsAccessorAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                InfoFragment infoFragment = new InfoFragment();
                return infoFragment;
            case 1:
                SettingsFragment settingsFragment = new SettingsFragment();
                return settingsFragment;
            case 2:
                BluetoothFragment bluetoothFragment = new BluetoothFragment();
                return bluetoothFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }


}
