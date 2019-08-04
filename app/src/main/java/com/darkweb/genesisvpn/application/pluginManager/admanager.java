package com.darkweb.genesisvpn.application.pluginManager;

import com.darkweb.genesisvpn.application.homeManager.home_model;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class admanager
{

    /*Private Variables*/
    private static final admanager ourInstance = new admanager();
    private InterstitialAd mInterstitialHidden_base;

    /*Initializations*/

    public static admanager getInstance() {
        return ourInstance;
    }

    private admanager() {
    }

    public void initialize()
    {
        MobileAds.initialize(home_model.getInstance().getHomeInstance(), "ca-app-pub-5074525529134731~1412991199");
        mInterstitialHidden_base = initAd("ca-app-pub-5074525529134731/7636560716");
    }

    private InterstitialAd initAd(String id)
    {
        InterstitialAd adInstance = new InterstitialAd(home_model.getInstance().getHomeInstance());
        adInstance.setAdUnitId(id);
        adInstance.loadAd(new AdRequest.Builder().build());

        return adInstance;
    }

    /*Helper Methods*/

    public void showAd()
    {
        if(mInterstitialHidden_base.isLoaded())
        {
            mInterstitialHidden_base.show();
            mInterstitialHidden_base.loadAd(new AdRequest.Builder().build());
        }
        else if(!mInterstitialHidden_base.isLoading())
        {
            mInterstitialHidden_base.loadAd(new AdRequest.Builder().build());
        }
    }
}
