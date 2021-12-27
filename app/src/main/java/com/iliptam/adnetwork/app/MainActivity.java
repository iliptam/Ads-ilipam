package com.iliptam.adnetwork.app;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iliptam.adnetwork.Adsiliptam;
import com.iliptam.adnetwork.BannerAd;
import com.iliptam.adnetwork.InterstitialAd;
import com.iliptam.adnetwork.NativeAd;
import com.iliptam.adnetwork.NativeAdsManager;
import com.iliptam.adnetwork.SingleNativeAd;
import com.iliptam.adnetwork.interfaces.AdListener;
import com.iliptam.adnetwork.interfaces.OnInitializationCompleteListener;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import adnetwork.myapplicationads.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements SingleNativeAd.Listener {


    BannerAd bannerAd;
    InterstitialAd interstitialAd;
    FrameLayout fl_native;
    SingleNativeAd nativeAdsManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Adsiliptam.initialize(MainActivity.this, 2, "https://abc.com/", 50,
                new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(boolean success) {
                        if (success) {
                            Toast.makeText(getApplicationContext(), "SUCCESS", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "FAILURE", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        bannerAd = new BannerAd(MainActivity.this, "Banner");

        LinearLayout container = findViewById(R.id.banner_container);
        bannerAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoadFailed(String var1) {
                Log.i("Adsiliptam", var1);

            }

            @Override
            public void onAdLoaded() {
                container.removeAllViews();
                container.addView(bannerAd);
            }

            @Override
            public void onAdClosed() {

            }

            @Override
            public void onAdShown() {

            }

        });
        bannerAd.loadAds();
        bannerAd.refreshAds(10, 0);

        fl_native = findViewById(R.id.native_ad_l);

        interstitialAd = new InterstitialAd(MainActivity.this, "Interstitial Ads");
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoadFailed(String var1) {

            }

            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdClosed() {
                Toast.makeText(MainActivity.this, "Close", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, MainActivity2.class));
            }

            @Override
            public void onAdShown() {

            }
        });
        interstitialAd.loadAds();

        loadNativwe();

        Button btn_show = findViewById(R.id.btn_show);
        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interstitialAd.isLoaded) {
                    interstitialAd.show(MainActivity.this);
                } else {
                    startActivity(new Intent(MainActivity.this, MainActivity2.class));
                }
            }
        });

        ReadLanguageTask readLanguageTask = new ReadLanguageTask();
        readLanguageTask.execute(new String[]{"https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=gu&dt=t&ie=UTF-8&oe=UTF-8&q=good"});
    }

    private void loadNativwe() {
        nativeAdsManager = new SingleNativeAd(MainActivity.this, "Native Ads");
        nativeAdsManager.setListener(this);
        nativeAdsManager.loadAds();
    }

    @Override
    public void onAdsLoaded() {
        fl_native.removeAllViews();
        if (fl_native.getChildCount() == 0) {
            LinearLayout ll_fb_native = (LinearLayout) (this).getLayoutInflater()
                    .inflate(R.layout.native_ad2, null);
            ImageView mvAdMedia;
            ImageView ivAdIcon;
            TextView tvAdTitle;
            TextView tvAdBody;
            RatingBar ratingBar;
            Button btnAdCallToAction;

            mvAdMedia = ll_fb_native.findViewById(R.id.native_ad_media);
            tvAdTitle = ll_fb_native.findViewById(R.id.native_ad_title);
            tvAdBody = ll_fb_native.findViewById(R.id.native_ad_body);
            ratingBar = ll_fb_native.findViewById(R.id.stars);
            btnAdCallToAction = ll_fb_native.findViewById(R.id.native_ad_call_to_action);
            ivAdIcon = ll_fb_native.findViewById(R.id.native_ad_icon);

            NativeAd ad;

            ad = nativeAdsManager.getnativeAd();

            if (ad != null) {

                tvAdTitle.setText(ad.adTitle);
                tvAdBody.setText(ad.adBody);
                btnAdCallToAction.setText(ad.adCtaText);
                ratingBar.setRating(ad.adRating);

                if (ad.adRating == 0) {
                    ratingBar.setVisibility(View.GONE);
                }
                nativeAdsManager.registerViewForInteraction(this, ad, ivAdIcon,
                        mvAdMedia, btnAdCallToAction);

                fl_native.addView(ll_fb_native);

            }
        }
    }

    @Override
    public void onAdError(String var1) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private class ReadLanguageTask extends AsyncTask<String, Void, String> {
        private ReadLanguageTask() {
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... strArr) {
            return readJSON(strArr[0]);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String str) {

            Log.e("STR", str);
            if (!str.equals("[\"ERROR\"]")) {
                try {
                    JSONArray jSONArray = new JSONArray(str);
                    String str2 = "";
                    for (int i = 0; i < jSONArray.getJSONArray(0).length(); i++) {
                        str2 = str2 + jSONArray.getJSONArray(0).getJSONArray(i).getString(0);
                    }

//                    Log.e("STR", str2);

                } catch (Exception unused) {
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                m3442a("en", "Good");
            }
        }
    }

    public String readJSON(String str) {
        Log.e("JSON", "Failed to download file" + str);
        StringBuilder sb = new StringBuilder();

//            HttpResponse execute = new DefaultHttpClient().execute(new HttpGet(str));
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .writeTimeout(60000, TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder()
                .url(str)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
            /*if (execute == null) {

            } else if (execute.getStatusLine().getStatusCode() == 200) {
                InputStream content = execute.getEntity().getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(content));
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    sb.append(readLine);
                }
                content.close();
            } else {
                Toast.makeText(NewTranslaterActivity.this, "Some thing went wrong.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.d("readJSON", e.getLocalizedMessage());
            sb.append("[\"ERROR\"]");
        }
        return sb.toString();*/
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!interstitialAd.isLoaded) {
            interstitialAd.loadAds();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static byte[] m3442a(String str, String str2) {
        byte[] bArr = new byte[0];
        try {
            String format = String.format(m3440a(m3441a("aHR0cHM6Ly90cmFuc2xhdGUuZ29vZ2xlLmNvbS90cmFuc2xhdGVfdHRzP3E9JXMmdGw9JXMmdG90YWw9MSZpZHg9MCZ0ZXh0bGVuPTEzJnRrPTI4MzgwMCZjbGllbnQ9dHctb2I=")), new Object[]{URLEncoder.encode(str2, "utf-8"), str});

            Log.e("SGSHGS", format);

            OkHttpClient client = new OkHttpClient.Builder()
                    .readTimeout(60000, TimeUnit.MILLISECONDS)
                    .writeTimeout(60000, TimeUnit.MILLISECONDS)
                    .build();

            Request request = new Request.Builder()
                    .url(format)
                    .addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.0; .NET CLR 1.1.4322; .NET CLR 2.0.50215;)")
                    .build();

            try {
//                Response response = client.newCall(request).execute();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.e("SGSHGS", "" + e.getMessage());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                        Log.e("SGSHGS", response.body().string());
                    }
                });

//                Log.e("SGSHGS", response.body().string());
            } catch (Exception e) {
                e.printStackTrace();
            }

           /* DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(format);
            httpGet.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.0; .NET CLR 1.1.4322; .NET CLR 2.0.50215;)");
            HttpResponse execute = defaultHttpClient.execute(httpGet);
            if (execute.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toByteArray(execute.getEntity());
            }*/
            return bArr;
        } catch (Exception e) {
            e.printStackTrace();
            return bArr;
        }
    }

    private static byte[] m3441a(String str) {
        return str.getBytes();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static String m3440a(byte[] bArr) {
        return new String(Base64.getDecoder().decode(bArr));
    }

}