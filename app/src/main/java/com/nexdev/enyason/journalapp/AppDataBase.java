package com.nexdev.enyason.journalapp;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

/**
 * Created by enyason on 5/28/18.
 */

@Database(entities = {Journal.class},version = 1,exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDataBase extends RoomDatabase {

    public static final String LOG_TAG = AppDataBase.class.getSimpleName();

    public static final Object LOCK = new Object();

    public static final String DATABASE_NAME = "journal_list";

    private static AppDataBase sInstance;



    public static AppDataBase getsInstance(Context context){

        if (sInstance== null) {

            synchronized (LOCK){

                Log.d(LOG_TAG,"creating new database");
                Log.i("DB",DATABASE_NAME);

                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDataBase.class,AppDataBase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();



            }
        }

        Log.d(LOG_TAG,"getting the database instance");



        return sInstance;
    }

    public abstract JournalDataAccessObject taskDao();

}
