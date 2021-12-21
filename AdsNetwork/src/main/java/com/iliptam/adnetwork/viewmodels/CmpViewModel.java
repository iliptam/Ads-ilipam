package com.iliptam.adnetwork.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.iliptam.adnetwork.interfaces.OnInitializationCompleteListener;
import com.iliptam.adnetwork.repository.CmpRepository;

public class CmpViewModel extends AndroidViewModel {

    CmpRepository cmpRepository;

    public CmpViewModel(@NonNull Application application) {
        super(application);

        cmpRepository = new CmpRepository(application);

    }
    public void LoadFromServer(String key, OnInitializationCompleteListener completeListener){
        cmpRepository.loadFromServer(key, completeListener);
    }
}
