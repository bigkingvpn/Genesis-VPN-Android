package com.darkweb.genesisvpn.application.serverManager;

public class server_model
{
    /*INSTANCE DECLARATIONS*/

    private server_controller serverInstance;

    /*INITIALIZATIONS*/

    private static final server_model ourInstance = new server_model();

    public static server_model getInstance() {
        return ourInstance;
    }

    /*INSTANCE GETTERS SETTERS*/

    server_controller getServerInstance(){
        return serverInstance;
    }
    void setServerInstance(server_controller serverInstance){
        this.serverInstance = serverInstance;
    }


}
