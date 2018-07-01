package com.nexdev.enyason.journalapp;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by enyason on 5/28/18.
 */

public class DateConverter {

    @TypeConverter
    public static Date toDate(Long timeStamp) {

        return timeStamp == null ? null : new Date(timeStamp);
    }

    @TypeConverter
    public static Long toTimeStamp(Date date) {

        return date == null ? null : date.getTime();
    }

}
