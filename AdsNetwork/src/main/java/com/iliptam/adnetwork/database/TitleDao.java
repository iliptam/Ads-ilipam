package com.iliptam.adnetwork.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iliptam.adnetwork.models.CamTitle;

import java.util.List;

@Dao
public interface TitleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CamTitle camTitle);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CamTitle> camTitleList);

    @Query("SELECT * FROM cam_title LIMIT '1'")
    LiveData<CamTitle> get();

    @Query("SELECT * FROM cam_title")
    LiveData<List<CamTitle>> getAll();

    @Query("SELECT * FROM cam_title WHERE ad_id = :campaignId")
    LiveData<List<CamTitle>> getById(int campaignId);

    @Query("DELETE FROM cam_title WHERE ad_id = :campaignId")
    void deleteById(int campaignId);

    @Query("DELETE FROM cam_title")
    void deleteTable();

}
