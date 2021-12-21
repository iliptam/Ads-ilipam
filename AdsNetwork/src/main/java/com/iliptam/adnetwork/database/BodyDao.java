package com.iliptam.adnetwork.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iliptam.adnetwork.models.CamBody;

import java.util.List;

@Dao
public interface BodyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CamBody camBody);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CamBody> camBodyList);

    @Query("SELECT * FROM cam_body LIMIT '1'")
    LiveData<CamBody> get();

    @Query("SELECT * FROM cam_body")
    LiveData<List<CamBody>> getAll();

    @Query("SELECT * FROM cam_body WHERE ad_id = :campaignId")
    LiveData<List<CamBody>> getById(int campaignId);

    @Query("DELETE FROM cam_body WHERE ad_id = :campaignId")
    void deleteById(int campaignId);

    @Query("DELETE FROM cam_body")
    void deleteTable();

}
