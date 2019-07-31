package com.darkweb.genesisvpn.application.homeManager;

import android.os.Bundle;
import com.darkweb.genesisvpn.R;
import android.view.View;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;

public class home_controller extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /*LOCAL VARIABLE DECLARATION*/

    Button connect_base;
    Button connect_animator;
    home_view_controller viewController;

    /*INITIALIZATIONS*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);


        initializateViews();
        initializeModel();
        initializeLayout();
    }

    public void initializeModel(){
        home_model.getInstance().setHomeInstance(this);
    }

    public void initializeLayout(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void initializateViews(){
        connect_base = findViewById(R.id.connect_base);
        connect_animator = findViewById(R.id.connect_animator);

        viewController = new home_view_controller(connect_base,connect_animator);
    }

    /*EVENT HANDLERS DEFAULTS*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_about)
        {
            home_ehandler.getInstance().aboutUS();
        }
        if (id == R.id.nav_share)
        {
            home_ehandler.getInstance().onShare();
        }
        else if (id == R.id.nav_help)
        {
            home_ehandler.getInstance().contactUS();
        }
        else if (id == R.id.nav_rate)
        {
            home_ehandler.getInstance().onRateUs();
        }
        else if (id == R.id.nav_quit)
        {
            home_ehandler.getInstance().onQuit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*EVENT HANDLERS OVERRIDE*/

    public void onStart(View view)
    {
        home_ehandler.getInstance().onStart();
    }

    /*EVENT VIEW REDIRECTIONS*/

    public void onStartView()
    {
        viewController.onStartView();
    }
}
