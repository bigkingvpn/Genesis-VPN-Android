package com.darkweb.genesisvpn.application.serverManager;

public class list_row_model
{
    /*Private Variables*/

    private String flag;
    private String header;
    private String description;

    /*Initializations*/

    public list_row_model(String header, String description,String flag) {
        this.flag = flag;
        this.header = header;
        this.description = description;
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
}
