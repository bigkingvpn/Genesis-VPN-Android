package com.darkweb.genesisvpn.application.homeManager;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;

import com.anchorfree.hydrasdk.HydraSdk;
import com.anchorfree.hydrasdk.callbacks.Callback;
import com.anchorfree.hydrasdk.exceptions.HydraException;
import com.anchorfree.hydrasdk.vpnservice.VPNState;
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
        status.connection_status = enums.connection_status.unconnected;
        ViewCompat.setTranslationZ(connect_loading, 15);
        connect_base.setTextSize(TypedValue.COMPLEX_UNIT_SP, 65);
        connect_loading.setAlpha(0f);
    }

    /*EVENT TO VIEW HANDLERS*/

    void onStartView()
    {
        if(status.connection_status == enums.connection_status.connected)
        {
            connect_base.setText(helperMethods.getScreenText(0.001f,strings.goText));
            connect_base.setTextSize(TypedValue.COMPLEX_UNIT_SP, 65);
            connect_loading.animate().alpha(0);
            status.connection_status = enums.connection_status.unconnected;
            proxy_controller.getInstance().disConnect();
        }
        else if(status.connection_status == enums.connection_status.connecting)
        {
            connect_base.setText(helperMethods.getScreenText(0.001f,strings.stopingText));
            connect_base.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
            connect_loading.animate().alpha(1);
            status.connection_status = enums.connection_status.stoping;
            proxy_controller.getInstance().disConnect();
        }
        else if(status.connection_status == enums.connection_status.stoping)
        {
            connect_base.setText(helperMethods.getScreenText(0.001f,strings.connectingText));
            connect_base.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            connect_loading.animate().alpha(1);
            status.connection_status = enums.connection_status.connecting;
        }
        else if(status.connection_status == enums.connection_status.unconnected)
        {
            connect_base.setText(helperMethods.getScreenText(0.001f,strings.connectingText));
            connect_base.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            connect_loading.animate().alpha(1);
            status.connection_status = enums.connection_status.connecting;
            proxy_controller.getInstance().connect();
        }
    }

    void onConnected()
    {
        connect_base.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        connect_base.setText(helperMethods.getScreenText(0.6f,strings.connectedText));
        connect_loading.animate().alpha(0);
        status.connection_status = enums.connection_status.connected;
    }

    void onDisConnected()
    {
        if(status.connection_status == enums.connection_status.unconnected)
        {
            connect_base.setText(helperMethods.getScreenText(0.001f,strings.goText));
            connect_base.setTextSize(TypedValue.COMPLEX_UNIT_SP, 65);
            connect_loading.animate().alpha(0);
            status.connection_status = enums.connection_status.unconnected;
            proxy_controller.getInstance().disConnect();
        }
    }

    void onConnecting()
    {
        connect_base.setText(helperMethods.getScreenText(0.001f,strings.connectingText));
        connect_base.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        connect_loading.animate().alpha(1);
        status.connection_status = enums.connection_status.connecting;
        proxy_controller.getInstance().connect();
    }
    /*ANIMATION VIEW REDIRECTIONS*/


}
