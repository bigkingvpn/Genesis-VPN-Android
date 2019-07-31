package com.darkweb.genesisvpn.application.homeManager;

import android.content.Context;

public class app_model {

    /*INSTANCE DECLARATIONS*/

    private static final app_model ourInstance = new app_model();
    public static app_model getInstance() {
        return ourInstance;
    }

    /*INITIALIZATIONS*/

    private Context appContext;
    private home_controller appInstance;

    /*INSTANCE GETTERS SETTERS*/

    void setAppContext(Context appContext)
    {
        this.appContext = appContext;
    }
    public Context getAppContext()
    {
        return appContext;
    }

    public home_controller getAppInstance()
    {
        return appInstance;
    }
    void setAppInstance(home_controller appInstance)
    {
        this.appInstance = appInstance;
    }

}
