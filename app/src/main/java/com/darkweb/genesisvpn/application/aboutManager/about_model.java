package com.darkweb.genesisvpn.application.aboutManager;

class about_model
{
    /*INSTANCE DECLARATIONS*/

    private about_controller aboutInstance;

    /*INITIALIZATIONS*/

    private static final about_model ourInstance = new about_model();

    static about_model getInstance() {
        return ourInstance;
    }

    /*INSTANCE GETTERS SETTERS*/

    about_controller getAboutInstance()
    {
        return aboutInstance;
    }
    void setAboutInstance(about_controller aboutInstance)
    {
        this.aboutInstance = aboutInstance;
    }

}
