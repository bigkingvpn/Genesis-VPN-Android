package com.darkweb.genesisvpn.application.serverManager;

import com.anchorfree.hydrasdk.api.data.Country;

public class list_row_model
{
    /*Private Variables*/

    private String flag;
    private String header;
    private String description;
    private Country server;

    /*Initializations*/

    public list_row_model(String header, String description,String flag,Country server) {
        this.flag = flag;
        this.header = header;
        this.description = description;
        this.server = server;
    }

    /*Variable Getters*/

    public String getHeader() {
        return header;
    }
    public String getDescription() {
        return description;
    }
    public String getFlag() {
        return flag;
    }
    public Country getCountryModel() {
        return server;
    }
}
