package com.darkweb.genesisvpn.application.aboutManager;

public class about_model
{
    /*INSTANCE DECLARATIONS*/

    private about_controller aboutInstance;

    /*INITIALIZATIONS*/

    public static final about_model ourInstance = new about_model();

    public static about_model getInstance() {
        return ourInstance;
    }

    /*INSTANCE GETTERS SETTERS*/

    public about_controller getAboutInstance()
    {
        return aboutInstance;
    }
    void setAboutInstance(about_controller aboutInstance)
    {
        this.aboutInstance = aboutInstance;
    }

}
