package com.darkweb.genesisvpn.application.serverManager;

class server_model
{
    /*INSTANCE DECLARATIONS*/

    private server_controller aboutInstance;

    /*INITIALIZATIONS*/

    private static final server_model ourInstance = new server_model();

    static server_model getInstance() {
        return ourInstance;
    }

    /*INSTANCE GETTERS SETTERS*/

    server_controller getAboutInstance()
    {
        return aboutInstance;
    }
    void setServerInstance(server_controller aboutInstance)
    {
        this.aboutInstance = aboutInstance;
    }

}
