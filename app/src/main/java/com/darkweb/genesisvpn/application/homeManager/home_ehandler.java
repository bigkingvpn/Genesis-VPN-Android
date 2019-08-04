package com.darkweb.genesisvpn.application.homeManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.View;

import com.darkweb.genesisvpn.application.aboutManager.about_controller;
import com.darkweb.genesisvpn.application.constants.enums;
import com.darkweb.genesisvpn.application.helperManager.helperMethods;
import com.darkweb.genesisvpn.application.pluginManager.message_manager;
import com.darkweb.genesisvpn.application.serverManager.server_controller;
import com.darkweb.genesisvpn.application.status.status;

class home_ehandler
{
    /*INITIALIZATION*/

    private static final home_ehandler ourInstance = new home_ehandler();

    static home_ehandler getInstance() {
        return ourInstance;
    }

    /*HANDLERS*/

    void onShare()
    {
        helperMethods.shareApp();
    }

    void onRateUs(){
        home_model.getInstance().getHomeInstance().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.darkweb.genesissearchengine")));
    }

    void onQuit(){
        helperMethods.quit(home_model.getInstance().getHomeInstance());
    }

    void contactUS()
    {
        helperMethods.sendEmail();
    }

    void aboutUS(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                helperMethods.openActivity(about_controller.class);
            }
        }, 500);
    }

    void onServer(){
        if(status.servers_loaded == enums.connection_servers.loaded)
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    helperMethods.openActivity(server_controller.class);
                }
            }, 200);
        }
        else
        {
            message_manager.getInstance().serverLoading();
        }
    }

    void onStart()
    {
        home_model.getInstance().getHomeInstance().onStartView();
    }
}
