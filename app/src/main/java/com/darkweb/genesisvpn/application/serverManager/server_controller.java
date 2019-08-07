package com.darkweb.genesisvpn.application.serverManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import com.darkweb.genesisvpn.R;
import com.darkweb.genesisvpn.application.aboutManager.about_model;

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
        server_model.getInstance().getServerInstance().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

    public void initializeModel(){
        server_model.getInstance().setServerInstance(this);
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
