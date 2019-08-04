package com.darkweb.genesisvpn.application.serverManager;

import com.darkweb.genesisvpn.application.helperManager.helperMethods;

class server_ehandler
{

    /*INITIALIZATION*/

    private static final server_ehandler ourInstance = new server_ehandler();

    static server_ehandler getInstance() {
        return ourInstance;
    }

    /*HANDLERS*/

    void quit()
    {
        helperMethods.quit(server_model.getInstance().getServerInstance());
    }
}
