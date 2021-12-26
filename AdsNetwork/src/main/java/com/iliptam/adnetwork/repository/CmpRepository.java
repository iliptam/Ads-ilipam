package com.iliptam.adnetwork.repository;

import static com.iliptam.adnetwork.utils.Global.CAT_ID;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
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
import java.util.Locale;

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

                    UpdateData updateData = new UpdateData(response.body(), prefManager, application);
                    updateData.execute();
                    if (completeListener!=null) {
                        completeListener.onInitializationComplete(true);
                    }
                    prefManager.setBoolean("INIT", true);
                } else {
                    if (completeListener!=null) {
                        completeListener.onInitializationComplete(false);
                    }
                    prefManager.setBoolean("INIT", false);
                }
            }

            @Override
            public void onFailure(Call<List<AdCampaign>> call, Throwable t) {
                if (completeListener!=null) {
                    completeListener.onInitializationComplete(false);
                }
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
                    mprefManager.setImpration("imp" + adCampaign.camName,
                            mprefManager.getImpration("imp" + adCampaign.camName));
                    mprefManager.setClick("clc" + adCampaign.camName,
                            mprefManager.getClick("clc" + adCampaign.camName));
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
        Application mapplication;

        public UpdateData(List<AdCampaign> campaignList, PrefManager mprefManager, Application application) {
            this.campaignList = campaignList;
            this.mprefManager = mprefManager;
            this.mapplication = application;
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

            String locale = mapplication.getResources().getConfiguration().locale.toString();
            String deviceName = getDeviceName();

//            Log.i("Adsiliptam", "Local: " + getUserCountry(mapplication) + " " + locale + " " +
//                    deviceName);

            if (adImp != 0) {
                Retrofit retrofit = apiClient.getClient();
                apiRest service = retrofit.create(apiRest.class);
                Call<ApiResponse> call = service.updateCampaign(id, adImp, adClick, getDeviceName(),
                        getUserCountry(mapplication), locale);
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

    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            /*if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            }
            else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { */// device is not 3G (would be unreliable)
            String networkCountry = tm.getNetworkCountryIso();
            if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                return networkCountry.toLowerCase(Locale.US);
            }

        } catch (Exception e) {
        }
        return null;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }
}
