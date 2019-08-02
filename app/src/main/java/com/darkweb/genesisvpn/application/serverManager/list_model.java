package com.darkweb.genesisvpn.application.serverManager;

import java.util.ArrayList;

public class list_model
{
    /*Private Variables*/

    private ArrayList<list_row_model> list_model = new ArrayList<>();

    /*Initializations*/

    private static final list_model ourInstance = new list_model();
    public static list_model getInstance()
    {
        return ourInstance;
    }

    /*Helper Methods*/

    void setModel(ArrayList<list_row_model> list_model)
    {
        this.list_model = list_model;
    }
    ArrayList<list_row_model> getModel()
    {
        return list_model;
    }

}