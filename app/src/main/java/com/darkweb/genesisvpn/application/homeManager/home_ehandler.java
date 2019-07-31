package com.darkweb.genesisvpn.homeDirectory;

import android.content.Intent;
import android.net.Uri;

public class home_ehandler
{
    private static final home_ehandler ourInstance = new home_ehandler();

    public static home_ehandler getInstance() {
        return ourInstance;
    }

    private home_ehandler() {
    }

    public void onShare()
    {

    }

    public void onRateUs()
    {
        app_model.getInstance().getAppInstance().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.darkweb.genesissearchengine")));
    }

    public void contactUS()
    {

    }

    public void aboutUS()
    {

    }


}
