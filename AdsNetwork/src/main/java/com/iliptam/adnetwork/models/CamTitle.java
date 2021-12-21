package com.iliptam.adnetwork.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "cam_title")
public class CamTitle {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "ad_id")
    public int ad_Id;

    @ColumnInfo(name = "adTitleId")
    public String adTitleID;

    @ColumnInfo(name = "title")
    public String title;

    public CamTitle(int ad_Id, String adTitleID, String title) {
        this.ad_Id = ad_Id;
        this.adTitleID = adTitleID;
        this.title = title;
    }
}
