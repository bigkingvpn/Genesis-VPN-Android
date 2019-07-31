package com.darkweb.genesisvpn.application.homeManager;

import android.content.Context;

public class home_model {

    /*INSTANCE DECLARATIONS*/

    private static final home_model ourInstance = new home_model();
    public static home_model getInstance() {
        return ourInstance;
    }

    /*INITIALIZATIONS*/

    private home_controller homeInstance;

    /*INSTANCE GETTERS SETTERS*/
    public home_controller getHomeInstance()
    {
        return homeInstance;
    }
    void setHomeInstance(home_controller homeInstance)
    {
        this.homeInstance = homeInstance;
    }

}
