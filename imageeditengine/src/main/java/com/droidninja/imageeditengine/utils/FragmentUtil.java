package com.droidninja.imageeditengine.utils;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.droidninja.imageeditengine.BaseFragment;

public class FragmentUtil {

    public static boolean hadFragment(AppCompatActivity activity) {
        return activity.getSupportFragmentManager().getBackStackEntryCount() != 0;
    }

    public static void replaceFragment(AppCompatActivity activity, int contentId, BaseFragment fragment) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

        transaction.replace(contentId, fragment, fragment.getClass().getSimpleName());

        Log.d("add tag", fragment.getClass().getSimpleName());
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public static void addFragment(AppCompatActivity activity, int contentId, BaseFragment fragment) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

        transaction.add(contentId, fragment, fragment.getClass().getSimpleName());
        Log.d("add tag", fragment.getClass().getSimpleName());
        transaction.commit();
    }

    public static void removeFragment(AppCompatActivity activity, String tag) {
        removeFragment(activity, getFragmentByTag(activity, tag));
        Log.d("remove tag", tag);
    }

    public static void removeFragment(AppCompatActivity activity, BaseFragment fragment) {
        activity.getSupportFragmentManager().getFragments().forEach(fragment1 -> Log.d("currents tag", fragment1.getClass().getSimpleName()));
        Log.d("currents tag", "---------------------------------------------------------------");
        activity.getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .commit();

        activity.getSupportFragmentManager().getFragments().forEach(fragment1 -> Log.d("currents tag", fragment1.getClass().getSimpleName()));
    }

    public static void showFragment(AppCompatActivity activity, BaseFragment fragment) {
        activity.getSupportFragmentManager().beginTransaction()
                .show(fragment)
                .commit();
    }

    public static void hideFragment(AppCompatActivity activity, BaseFragment fragment) {
        activity.getSupportFragmentManager().beginTransaction()
                .hide(fragment)
                .commit();
    }

    public static void attachFragment(AppCompatActivity activity, BaseFragment fragment) {
        activity.getSupportFragmentManager().beginTransaction()
                .attach(fragment)
                .commit();
    }

    public static void detachFragment(AppCompatActivity activity, BaseFragment fragment) {
        activity.getSupportFragmentManager().beginTransaction()
                .detach(fragment)
                .commit();
    }

    public static BaseFragment getFragmentByTag(AppCompatActivity appCompatActivity, String tag) {
        return (BaseFragment) appCompatActivity.getSupportFragmentManager().findFragmentByTag(tag);
    }

}