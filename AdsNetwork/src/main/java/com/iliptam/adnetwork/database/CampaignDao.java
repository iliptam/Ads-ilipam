package com.iliptam.adnetwork.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iliptam.adnetwork.models.Campaign;

import java.util.List;

@Dao
public interface CampaignDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Campaign campaign);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Campaign> campaignList);

    @Query("SELECT * FROM campaigns LIMIT '1'")
    LiveData<Campaign> get();

    @Query("SELECT * FROM campaigns")
    LiveData<List<Campaign>> getAll();

    @Query("SELECT * FROM campaigns WHERE ad_type = :type")
    LiveData<List<Campaign>> getByType(String type);

    @Query("DELETE FROM campaigns WHERE campaignId = :campaignId")
    void deleteById(int campaignId);

    @Query("DELETE FROM campaigns")
    void deleteTable();

}
