package com.iliptam.adnetwork;

import static com.iliptam.adnetwork.utils.Global.check;

import android.content.Context;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.iliptam.adnetwork.interfaces.OnInitializationCompleteListener;
import com.iliptam.adnetwork.utils.Global;
import com.iliptam.adnetwork.utils.PrefManager;
import com.iliptam.adnetwork.viewmodels.CmpViewModel;


public class Adsiliptam {

    public static OnInitializationCompleteListener completeListener;
    public static Context mcontext;
    public static CmpViewModel cmpViewModel;
    public static PrefManager prefManager;
    public static int LS;

    public static void initialize(Context context, int key, String url,
                                  int loadSeconds, OnInitializationCompleteListener listener) {
        completeListener = listener;
        mcontext = context;
        LS = loadSeconds;
        Global.CAT_ID = key;
        Global.API_URL = url;
        Global.IMAGE_URL = url + "uploads/";

        prefManager = new PrefManager(mcontext);

        cmpViewModel = new ViewModelProvider((ViewModelStoreOwner) mcontext).get(CmpViewModel.class);

        if (Global.isConnectedToInternet(mcontext)) {
            if (check(mcontext, loadSeconds)) {
                DataFromServer();
            } else {
                if (!prefManager.getBoolean("INIT")) {
                    DataFromServer();
                }
            }
        }
    }

    private static void DataFromServer() {
        cmpViewModel.LoadFromServer("", completeListener);
    }

}
