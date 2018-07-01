package com.nexdev.enyason.journalapp;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by enyason on 5/31/18.
 */

public class AppExecutor {

    //this is for singleton instantiation
    private static final Object LOCK = new Object();
    private static AppExecutor sInstance;


    private final Executor diskIO;


    private AppExecutor(Executor diskIO) {
        this.diskIO = diskIO;


    }


    public static AppExecutor getInstance(){

        if (sInstance == null) {

            synchronized (LOCK){

                sInstance = new AppExecutor(Executors.newSingleThreadExecutor());

            }
        }

        return sInstance;

    }



    public Executor diskIO() {
        return diskIO;
    }





}
