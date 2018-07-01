package com.nexdev.enyason.journalapp;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by enyason on 5/28/18.
 */

//its an interface that has the DOA annotation

@Dao
public interface JournalDataAccessObject {

    @Query("SELECT * FROM Journal ORDER BY id")
    LiveData<List<Journal>> loadAllJournal(); // returns a list of live journal object


    @Query("SELECT * FROM Journal ORDER BY id")
    List<Journal> loadAlll(); // returns a list of journal object


    @Insert
    void insertJournal(Journal journal);


    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateJournal(Journal journal);

    @Delete
    void deleteJournal(Journal journal);

   @Query("SELECT * FROM Journal WHERE isFavourite = 1")
   LiveData<List<Journal>> getJournalByFav();

}
