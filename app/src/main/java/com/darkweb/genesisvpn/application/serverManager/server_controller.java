package com.darkweb.genesisvpn.application.serverManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.darkweb.genesisvpn.R;

import java.util.ArrayList;

public class server_controller extends AppCompatActivity {

    /*LOCAL VARIABLE DECLARATION*/

    private RecyclerView listView;

    /*INITIALIZATION*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_view);
        initializeModel();
        initializeViews();
        initializeList();
    }

    public void initializeModel(){
        server_model.getInstance().setServerInstance(this);


        ArrayList<list_row_model> list_items = new ArrayList<>();
        list_items.add(new list_row_model("Russia","192.215.123","as"));
        list_items.add(new list_row_model("China","192.215.123","a3"));
        list_items.add(new list_row_model("Russia","192.215.123","a3"));
        list_items.add(new list_row_model("China","192.215.123","a3"));
        list_items.add(new list_row_model("Russia","192.215.123","a3"));
        list_items.add(new list_row_model("China","192.215.123","a3"));
        list_items.add(new list_row_model("Russia","192.215.123","a3"));
        list_items.add(new list_row_model("China","192.215.123","a3"));
        list_model.getInstance().setModel(list_items);
    }

    public void initializeViews(){
        listView = findViewById(R.id.listview);
    }

    public void initializeList(){
        list_adapter adapter = new list_adapter();
        listView.setAdapter(adapter);
        listView.setLayoutManager(new LinearLayoutManager(this));
    }


    /*EVENT HANDLER*/

    public void onBackPressed(View view)
    {
        server_ehandler.getInstance().quit();
    }
}
