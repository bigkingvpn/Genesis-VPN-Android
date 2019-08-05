package com.darkweb.genesisvpn.application.aboutManager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.darkweb.genesisvpn.R;
import com.darkweb.genesisvpn.application.pluginManager.admanager;

public class about_controller extends AppCompatActivity {

    /*INITIALIZATION*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_view);
        initializeModel();
    }

    public void initializeModel(){
        about_model.getInstance().setAboutInstance(this);
    }

    public void adsDisabler(View view)
    {
        admanager.getInstance().adsDisabler();
    }

    /*EVENT HANDLER*/

    public void onBackPressed(View view)
    {
        about_ehandler.getInstance().quit();
    }
}
