package com.iliptam.adnetwork.repository;

import static com.iliptam.adnetwork.utils.Global.CAT_ID;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.iliptam.adnetwork.api.AdCampaign;
import com.iliptam.adnetwork.api.AdDescription;
import com.iliptam.adnetwork.api.AdTitle;
import com.iliptam.adnetwork.api.ApiResponse;
import com.iliptam.adnetwork.api.apiClient;
import com.iliptam.adnetwork.api.apiRest;
import com.iliptam.adnetwork.database.BodyDao;
import com.iliptam.adnetwork.database.CampaignDao;
import com.iliptam.adnetwork.database.CmpDatabase;
import com.iliptam.adnetwork.database.TitleDao;
import com.iliptam.adnetwork.interfaces.OnInitializationCompleteListener;
import com.iliptam.adnetwork.models.CamBody;
import com.iliptam.adnetwork.models.CamTitle;
import com.iliptam.adnetwork.models.Campaign;
import com.iliptam.adnetwork.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CmpRepository {

    Application application;

    CmpDatabase cmpDatabase;
    CampaignDao campaignDao;
    PrefManager prefManager;

    public CmpRepository(Application application) {

        this.application = application;
        cmpDatabase = CmpDatabase.getInstance(application);
        campaignDao = cmpDatabase.getCampaignDao();

        prefManager = new PrefManager(application.getApplicationContext());

    }

    public void loadFromServer(String key, OnInitializationCompleteListener completeListener) {
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<AdCampaign>> call = service.getCampaignsById(CAT_ID);
        call.enqueue(new Callback<List<AdCampaign>>() {
            @Override
            public void onResponse(Call<List<AdCampaign>> call, Response<List<AdCampaign>> response) {
                if (response != null && response.body() != null && response.body().size() > 0) {
                    SetData setData = new SetData(cmpDatabase, response.body(), prefManager);
                    setData.execute();

                    UpdateData updateData = new UpdateData(response.body(), prefManager);
                    updateData.execute();
                    completeListener.onInitializationComplete(true);
                    prefManager.setBoolean("INIT", true);
                }else {
                    completeListener.onInitializationComplete(false);
                    prefManager.setBoolean("INIT", false);
                }
            }

            @Override
            public void onFailure(Call<List<AdCampaign>> call, Throwable t) {
                completeListener.onInitializationComplete(false);
                prefManager.setBoolean("INIT", false);
            }
        });
    }

    public static class SetData extends AsyncTask<String, String, String> {

        public List<AdCampaign> campaignList;
        public CmpDatabase db;
        public TitleDao titleDao;
        public BodyDao bodyDao;
        public CampaignDao newCampaignDao;
        PrefManager mprefManager;

        public SetData(CmpDatabase db, List<AdCampaign> campaignList, PrefManager prefManager) {
            this.db = db;
            this.campaignList = campaignList;
            this.mprefManager = prefManager;

            titleDao = db.getTitleDao();
            bodyDao = db.getBodyDao();
            newCampaignDao = db.getCampaignDao();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                List<Campaign> clist = new ArrayList<>();

                Log.i("Adsiliptam", " " + campaignList.size());

                for (int i = 0; i < campaignList.size(); i++) {

                    AdCampaign adCampaign = campaignList.get(i);

                    if (adCampaign.adTitle != null && adCampaign.adTitle.size() > 0) {
                        List<CamTitle> camTitleList = new ArrayList<>();
                        for (int j = 0; j < adCampaign.adTitle.size(); j++) {
                            AdTitle stitle = adCampaign.adTitle.get(j);
                            camTitleList.add(new CamTitle(adCampaign.id, stitle.id, stitle.title));
                        }
                        db.runInTransaction(() -> {
                            titleDao.deleteById(adCampaign.id);
                            titleDao.insertAll(camTitleList);
                        });
                    }

                    if (adCampaign.adDescription != null && adCampaign.adDescription.size() > 0) {
                        List<CamBody> camBodyList = new ArrayList<>();
                        for (int j = 0; j < adCampaign.adDescription.size(); j++) {
                            AdDescription sbody = adCampaign.adDescription.get(j);
                            camBodyList.add(new CamBody(adCampaign.id, sbody.id, sbody.title));
                        }
                        db.runInTransaction(() -> {
                            bodyDao.deleteById(adCampaign.id);
                            bodyDao.insertAll(camBodyList);
                        });
                    }

                    clist.add(new Campaign(adCampaign.id, adCampaign.camName, adCampaign.adCategoryId, adCampaign.adCategoryName,
                            adCampaign.adIcon, adCampaign.adHeaderImage, adCampaign.adUrl, Float.parseFloat(adCampaign.adRating),
                            adCampaign.adCtaTxt, adCampaign.adBgColor, adCampaign.adTxtColor, adCampaign.adPrice,
                            adCampaign.adType));
                    mprefManager.setImpration("imp" + adCampaign.camName, mprefManager.getImpration("imp" + adCampaign.camName));
                    mprefManager.setClick("clc" + adCampaign.camName, mprefManager.getClick("clc" + adCampaign.camName));
                }

                db.runInTransaction(() -> {
                    newCampaignDao.deleteTable();
                    newCampaignDao.insertAll(clist);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

            return "1";
        }

    }

    public static class UpdateData extends AsyncTask<String, String, String> {

        public List<AdCampaign> campaignList;
        PrefManager mprefManager;

        public UpdateData(List<AdCampaign> campaignList, PrefManager mprefManager) {
            this.campaignList = campaignList;
            this.mprefManager = mprefManager;
        }

        @Override
        protected String doInBackground(String... strings) {

            for (int i = 0; i < campaignList.size(); i++) {
                uploadData(campaignList.get(i), mprefManager);
            }
            return "1";
        }

        private void uploadData(AdCampaign adCampaign, PrefManager prefManager) {
            int id = adCampaign.id;
            int adImp = prefManager.getImpration("imp" + adCampaign.camName);
            int adClick = prefManager.getClick("clc" + adCampaign.camName);

            if (adImp != 0) {
                Retrofit retrofit = apiClient.getClient();
                apiRest service = retrofit.create(apiRest.class);
                Call<ApiResponse> call = service.updateCampaign(id, adImp, adClick);
                call.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response != null && response.body() != null) {
                            if (response.body().success) {
                                prefManager.setImpration("imp" + adCampaign.camName, 0);
                                prefManager.setClick("clc" + adCampaign.camName, 0);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {

                    }
                });
            }
        }
    }


}
