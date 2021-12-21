package com.iliptam.adnetwork.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.iliptam.adnetwork.models.CamBody;
import com.iliptam.adnetwork.models.CamTitle;
import com.iliptam.adnetwork.models.Campaign;
import com.iliptam.adnetwork.repository.AdsRepository;

import java.util.List;

public class AdsViewModel extends AndroidViewModel {

    AdsRepository adRepository;

    public AdsViewModel(@NonNull Application application) {
        super(application);
        adRepository = new AdsRepository(application);
    }

    public LiveData<List<Campaign>> getCampaign(String type) {
        return adRepository.getCampaign(type);
    }

    public LiveData<List<CamTitle>> getAdTitle(int id) {
        return adRepository.getAdTitle(id);
    }

    public LiveData<List<CamBody>> getAdBody(int id) {
        return adRepository.getAdBody(id);
    }

}
