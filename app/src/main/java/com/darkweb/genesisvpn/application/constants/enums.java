package com.darkweb.genesisvpn.application.constants;

public class enums {

    public static enum connection_status
    {
        unconnected, connecting, connected, stoping, no_status, restarting,reconnecting;
    }

    public static enum connection_servers
    {
        loaded,not_loaded;
    }

    public static enum error_handler
    {
        disconnect_fallback,connected_fallback;
    }

    public static enum app_status
    {
        paused,resumed;
    }
}


