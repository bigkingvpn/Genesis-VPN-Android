package com.darkweb.genesisvpn.application.aboutManager;

import com.darkweb.genesisvpn.application.helperManager.helperMethods;

class about_ehandler
{

    /*INITIALIZATION*/

    private static final about_ehandler ourInstance = new about_ehandler();

    static about_ehandler getInstance() {
        return ourInstance;
    }

    /*HANDLERS*/

    void quit()
    {
        helperMethods.quit(about_model.getInstance().getAboutInstance());
    }
}
