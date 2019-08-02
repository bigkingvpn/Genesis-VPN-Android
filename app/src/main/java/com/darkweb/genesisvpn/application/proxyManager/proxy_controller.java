package com.darkweb.genesisvpn.application.proxyManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import com.anchorfree.hydrasdk.HydraSDKConfig;
import com.anchorfree.hydrasdk.HydraSdk;
import com.anchorfree.hydrasdk.SessionConfig;
import com.anchorfree.hydrasdk.api.ClientInfo;
import com.anchorfree.hydrasdk.api.data.ServerCredentials;
import com.anchorfree.hydrasdk.callbacks.Callback;
import com.anchorfree.hydrasdk.callbacks.CompletableCallback;
import com.anchorfree.hydrasdk.exceptions.HydraException;
import com.anchorfree.hydrasdk.vpnservice.connectivity.NotificationConfig;
import com.anchorfree.reporting.TrackingConstants;
import com.darkweb.genesisvpn.BuildConfig;
import com.darkweb.genesisvpn.R;
import com.darkweb.genesisvpn.application.constants.enums;
import com.darkweb.genesisvpn.application.homeManager.home_model;
import com.darkweb.genesisvpn.application.status.status;


public class proxy_controller {

    /*INITIALIZATIONS*/

    private static final proxy_controller ourInstance = new proxy_controller();
    public static proxy_controller getInstance() {
        return ourInstance;
    }

    /*LOCAL VARIABLE DECLARATIONS*/

    private static final String CHANNEL_ID = "vpn";
    private boolean isLoading = false;

    /*HELPER METHODS*/

    public void startVPN()
    {
        if(!isLoading)
        {
            isLoading = true;
            HydraSdk.startVPN(new SessionConfig.Builder()
                    .withVirtualLocation(HydraSdk.COUNTRY_OPTIMAL)
                    .withReason(TrackingConstants.GprReasons.M_UI)
                    .build(), new Callback<ServerCredentials>() {
                @Override
                public void success(@NonNull ServerCredentials serverCredentials) {
                    status.connection_status = enums.connection_status.connected;
                    isLoading = false;
                    home_model.getInstance().getHomeInstance().onConnected();
                }

                @Override
                public void failure(@NonNull HydraException e) {
                    home_model.getInstance().getHomeInstance().onDisConnected();
                    status.connection_status = enums.connection_status.unconnected;
                    isLoading = false;
                }
            });
        }
    }

    public void stopVPN()
    {
        HydraSdk.stopVPN(TrackingConstants.GprReasons.M_UI, new CompletableCallback() {
            @Override
            public void complete()
            {
                status.connection_status = enums.connection_status.unconnected;
            }

            @Override
            public void error(HydraException e)
            {
                status.connection_status = enums.connection_status.unconnected;
            }
        });
    }

    /*VPN INITIALIZATIONS*/

    public void initialization()
    {
        vpnIntialization();
    }

    public void vpnIntialization()
    {
        createNotificationChannel();
        SharedPreferences prefs = getPrefs();
        ClientInfo clientInfo = ClientInfo.newBuilder()
                .baseUrl(prefs.getString(BuildConfig.STORED_HOST_URL_KEY, BuildConfig.BASE_HOST))
                .carrierId(prefs.getString(BuildConfig.STORED_CARRIER_ID_KEY, BuildConfig.BASE_CARRIER_ID))
                .build();

        NotificationConfig notificationConfig = NotificationConfig.newBuilder()
                .title(home_model.getInstance().getHomeInstance().getResources().getString(R.string.app_name))
                .channelId(CHANNEL_ID)
                .build();

        HydraSdk.setLoggingLevel(Log.VERBOSE);

        HydraSDKConfig config = HydraSDKConfig.newBuilder()
                .observeNetworkChanges(true)
                .captivePortal(true)
                .moveToIdleOnPause(false)
                .build();
        HydraSdk.init(home_model.getInstance().getHomeInstance(), clientInfo, notificationConfig, config);
    }

    private SharedPreferences getPrefs() {
        return home_model.getInstance().getHomeInstance().getSharedPreferences(BuildConfig.SHARED_PREFS, Context.MODE_PRIVATE);
    }

    private void createNotificationChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "Sample VPN";
            String description = "VPN notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = home_model.getInstance().getHomeInstance().getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }

}
