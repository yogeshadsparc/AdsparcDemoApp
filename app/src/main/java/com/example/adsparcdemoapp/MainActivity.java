package com.example.adsparcdemoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

import org.prebid.mobile.BannerAdUnit;
import org.prebid.mobile.Host;
import org.prebid.mobile.OnCompleteListener;
import org.prebid.mobile.PrebidMobile;
import org.prebid.mobile.ResultCode;
import org.prebid.mobile.addendum.AdViewUtils;
import org.prebid.mobile.addendum.PbFindSizeError;

public class MainActivity extends AppCompatActivity {
    private PublisherAdView mPublisherAdView;
    private TextView mTextView;

    private void showText(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  mTextView = findViewById(R.id.textView2);

        Host host = Host.CUSTOM;
        host.setHostUrl("https://s2s.t13.io/openrtb2/auction");
        PrebidMobile.setPrebidServerHost(host);
        PrebidMobile.setPrebidServerAccountId("dff549d4-6d0b-41db-a0e3-c55b1fe43586");
        PrebidMobile.setApplicationContext(this.getApplicationContext());
        BannerAdUnit bannerAdUnit = new BannerAdUnit("01c7f25d-ee98-4e26-9ba5-a41328e02723", 300, 250);
        mPublisherAdView = findViewById(R.id.publisherAdView);
        mPublisherAdView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                AdViewUtils.findPrebidCreativeSize(mPublisherAdView, new AdViewUtils.PbFindSizeListener() {
                    @Override
                    public void success(int width, int height) {
                        mPublisherAdView.setAdSizes(new AdSize(width, height));
                    }

                    @Override
                    public void failure(PbFindSizeError error) {
                        Log.d("AdsparcPrebidMobile", "error: " + error);
                    }
                });
            }
        });
        final PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();

        bannerAdUnit.fetchDemand(adRequest, new OnCompleteListener() {
            @Override
            public void onComplete(ResultCode resultCode) {
                showText(resultCode.toString());
                Log.d("AdsparcPrebidMobile", "result code: " + resultCode);
                mPublisherAdView.loadAd(adRequest);
            }
        });
    }
}
