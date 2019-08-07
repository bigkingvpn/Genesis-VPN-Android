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
import com.darkweb.genesisvpn.application.serverManager.server_model;
import com.darkweb.genesisvpn.application.status.status;

class home_ehandler
{
    /*INITIALIZATION*/

    private static final home_ehandler ourInstance = new home_ehandler();
    private boolean isUIBlocked = false;

    static home_ehandler getInstance() {
        return ourInstance;
    }

    /*HANDLERS*/

    void onShare()
    {
        if(!isUIBlocked)
        {
            resetUIBlock();
            isUIBlocked = true;
            helperMethods.shareApp();
        }
    }

    void privacyPolicy()
    {
        home_model.getInstance().getHomeInstance().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://infogamesolstudios.blogspot.com/p/privacy-policy-function-var-html5.html")));
    }

    void onRateUs(){
        home_model.getInstance().getHomeInstance().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.darkweb.genesisvpn")));
    }

    void onQuit(){
        helperMethods.quit(home_model.getInstance().getHomeInstance());
    }

    void contactUS()
    {
        helperMethods.sendEmail();
    }

    void aboutUS(){
        if(!isUIBlocked)
        {
            resetUIBlock();
            isUIBlocked = true;
            new Handler().postDelayed(() -> helperMethods.openActivity(about_controller.class), 400);
        }
    }

    void onServer(long delay){
        if(!isUIBlocked)
        {
            resetUIBlock();
            isUIBlocked = true;
            if(status.servers_loaded == enums.connection_servers.loaded)
            {
                new Handler().postDelayed(() -> helperMethods.openActivity(server_controller.class), delay);
            }
            else
            {
                message_manager.getInstance().serverLoading();
            }
        }
    }

    private void resetUIBlock()
    {
        new Thread()
        {
            public void run()
            {
                try
                {
                    sleep(1000);
                    isUIBlocked = false;
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    void onStart()
    {
        home_model.getInstance().getHomeInstance().onStartView();
    }

}
