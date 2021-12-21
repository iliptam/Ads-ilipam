package com.iliptam.adnetwork.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.iliptam.adnetwork.database.BodyDao;
import com.iliptam.adnetwork.database.CampaignDao;
import com.iliptam.adnetwork.database.CmpDatabase;
import com.iliptam.adnetwork.database.TitleDao;
import com.iliptam.adnetwork.models.CamBody;
import com.iliptam.adnetwork.models.CamTitle;
import com.iliptam.adnetwork.models.Campaign;

import java.util.List;

public class AdsRepository {

    CmpDatabase cmpDatabase;
    CampaignDao campaignDao;
    TitleDao titleDao;
    BodyDao bodyDao;

    public AdsRepository(Application application) {

        cmpDatabase = CmpDatabase.getInstance(application);
        campaignDao = cmpDatabase.getCampaignDao();
        titleDao = cmpDatabase.getTitleDao();
        bodyDao = cmpDatabase.getBodyDao();

    }

    public LiveData<List<Campaign>> getCampaign(String type) {
        return campaignDao.getByType(type);
    }

    public LiveData<List<CamTitle>> getAdTitle(int id) {
        return titleDao.getById(id);
    }

    public LiveData<List<CamBody>> getAdBody(int id) {
        return bodyDao.getById(id);
    }


    public class UpdateImpration extends AsyncTask<String, String, String> {

        int campaignId;
        CmpDatabase db;

        public UpdateImpration(int campaignId, CmpDatabase db) {
            this.campaignId = campaignId;
            this.db = db;
        }

        @Override
        protected String doInBackground(String... strings) {

           /* ClickImpDao clickImpDao1 = db.getClickImpDao();
            db.runInTransaction(() -> {
                clickImpDao1.updateImpById(campaignId);
            });
*/
            return "1";
        }
    }

    public class UpdateClick extends AsyncTask<String, String, String> {

        int campaignId;
        CmpDatabase db;

        public UpdateClick(int campaignId, CmpDatabase db) {
            this.campaignId = campaignId;
            this.db = db;
        }

        @Override
        protected String doInBackground(String... strings) {

            /*ClickImpDao clickImpDao1 = db.getClickImpDao();
            db.runInTransaction(() -> {
                clickImpDao1.updateClickById(campaignId);
            });*/

            return "1";
        }
    }
}
