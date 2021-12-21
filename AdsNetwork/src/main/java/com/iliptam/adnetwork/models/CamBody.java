package com.iliptam.adnetwork.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cam_body")
public class CamBody {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "ad_id")
    public int adId;

    @ColumnInfo(name = "adBodyId")
    public String adBodyId;

    @ColumnInfo(name = "body")
    public String body;

    public CamBody(int adId, String adBodyId, String body) {
        this.adId = adId;
        this.adBodyId = adBodyId;
        this.body = body;
    }
}
