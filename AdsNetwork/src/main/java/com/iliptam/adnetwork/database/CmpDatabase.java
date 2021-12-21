package com.iliptam.adnetwork.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.iliptam.adnetwork.models.CamBody;
import com.iliptam.adnetwork.models.CamTitle;
import com.iliptam.adnetwork.models.Campaign;


@Database(entities = {
        Campaign.class,
        CamBody.class,
        CamTitle.class}, version = 8, exportSchema = false)
public abstract class CmpDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "adnetwork.db";

    public abstract CampaignDao getCampaignDao();

    public abstract TitleDao getTitleDao();

    public abstract BodyDao getBodyDao();

    public static volatile CmpDatabase INSTANCE;

    public static CmpDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (CmpDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, CmpDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
