package com.darkweb.genesisvpn.application.homeManager;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;

import com.darkweb.genesisvpn.application.constants.enums;
import com.darkweb.genesisvpn.application.constants.strings;
import com.darkweb.genesisvpn.application.helperManager.helperMethods;
import com.darkweb.genesisvpn.application.proxyManager.proxy_controller;
import com.darkweb.genesisvpn.application.status.status;

class home_view_controller {

    /*LOCAL VIEW DECLARATION*/

    private Button connect_base;
    private Button connect_animator;
    private ImageView connect_loading;

    /*LOCAL VARIABLE DECLARATION*/

    private ObjectAnimator scale_connect_animator = null;
    private ObjectAnimator loading_connect_animator = null;

    /*INITIALIZATIONS*/

    home_view_controller(Button connect_base, Button connect_animator, ImageView connect_loading)
    {
        this.connect_base = connect_base;
        this.connect_animator = connect_animator;
        this.connect_loading = connect_loading;

        initializeConnectStyles();
    }

    /*HELPER METHODS*/

    private void initializeConnectStyles(){

        /*FONTS*/

        int size = helperMethods.screenWidth()*60/100;

        Typeface custom_font = Typeface.createFromAsset(home_model.getInstance().getHomeInstance().getAssets(),  "fonts/gotham_med.ttf");
        connect_base.setTypeface(custom_font);

        /*WIDTH WITH SCREEN*/

        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams)connect_base.getLayoutParams();
        lp.width = size;
        lp.height = size;

        connect_base.setLayoutParams(lp);
        connect_animator.setLayoutParams(lp);
        connect_loading.setLayoutParams(lp);

        home_animation.getInstance().beatAnimation(connect_animator);
        home_animation.getInstance().rotateAnimation(connect_loading);
        connect_base.setText(helperMethods.getScreenText(0.6f, strings.goText));
        ViewCompat.setTranslationZ(connect_loading, 15);
        connect_loading.setAlpha(0f);
    }

    /*EVENT TO VIEW HANDLERS*/

    void onStartView()
    {
        if(status.connection_status == enums.connection_status.connecting || status.connection_status == enums.connection_status.connected)
        {
            proxy_controller.getInstance().stopVPN();
            connect_base.setText(helperMethods.getScreenText(0.6f,strings.goText));
            connect_loading.animate().alpha(0);
            status.connection_status = enums.connection_status.unconnected;
        }
        else if(status.connection_status == enums.connection_status.unconnected)
        {
            proxy_controller.getInstance().startVPN();
            connect_base.setText(helperMethods.getScreenText(0.25f,strings.connectingText));
            connect_loading.animate().alpha(1);
            status.connection_status = enums.connection_status.connecting;
        }
    }

    void onConnected()
    {
        connect_base.setText(helperMethods.getScreenText(0.6f,strings.connectedText));
        connect_loading.animate().alpha(0);
        status.connection_status = enums.connection_status.connected;
    }

    void onDisConnected()
    {
        connect_base.setText(helperMethods.getScreenText(0.6f,strings.goText));
        connect_loading.animate().alpha(0);
        status.connection_status = enums.connection_status.unconnected;
    }

    /*ANIMATION VIEW REDIRECTIONS*/


}
