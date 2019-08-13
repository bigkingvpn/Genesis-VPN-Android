package com.darkweb.genesisvpn.application.proxyManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import com.anchorfree.hydrasdk.HydraSDKConfig;
import com.anchorfree.hydrasdk.HydraSdk;
import com.anchorfree.hydrasdk.SessionConfig;
import com.anchorfree.hydrasdk.SessionInfo;
import com.anchorfree.hydrasdk.api.AuthMethod;
import com.anchorfree.hydrasdk.api.ClientInfo;
import com.anchorfree.hydrasdk.api.data.Country;
import com.anchorfree.hydrasdk.api.data.ServerCredentials;
import com.anchorfree.hydrasdk.api.response.User;
import com.anchorfree.hydrasdk.callbacks.Callback;
import com.anchorfree.hydrasdk.callbacks.CompletableCallback;
import com.anchorfree.hydrasdk.callbacks.VpnStateListener;
import com.anchorfree.hydrasdk.compat.CredentialsCompat;
import com.anchorfree.hydrasdk.dns.DnsRule;
import com.anchorfree.hydrasdk.exceptions.HydraException;
import com.anchorfree.hydrasdk.exceptions.VPNException;
import com.anchorfree.hydrasdk.vpnservice.VPNState;
import com.anchorfree.hydrasdk.vpnservice.connectivity.NotificationConfig;
import com.anchorfree.reporting.TrackingConstants;
import com.darkweb.genesisvpn.BuildConfig;
import com.darkweb.genesisvpn.R;
import com.darkweb.genesisvpn.application.constants.enums;
import com.darkweb.genesisvpn.application.constants.keys;
import com.darkweb.genesisvpn.application.constants.strings;
import com.darkweb.genesisvpn.application.homeManager.home_model;
import com.darkweb.genesisvpn.application.pluginManager.preference_manager;
import com.darkweb.genesisvpn.application.serverManager.list_model;
import com.darkweb.genesisvpn.application.serverManager.logs;
import com.darkweb.genesisvpn.application.status.status;

import java.util.LinkedList;
import java.util.List;

import static com.darkweb.genesisvpn.application.serverManager.logs.log;


public class proxy_controller {

    /*INITIALIZATIONS*/

    private static final proxy_controller ourInstance = new proxy_controller();
    public static proxy_controller getInstance() {
        return ourInstance;
    }

    /*LOCAL VARIABLE DECLARATIONS*/

    private boolean isLoading = false;
    private static final String CHANNEL_ID = "vpn";
    private static VPNState currentVpnState = VPNState.IDLE;

    private String server_name = strings.emptySTR;


    /*HELPER METHODS*/

    public void autoStart()
    {
        //  if(!preference_manager.getInstance().getBool(keys.app_initialized_key,false))
        //  {
        //      home_model.getInstance().getHomeInstance().onStartView();
        //  }

        stateManager();
        stateUIUpdater();
    }

    private void stateUIUpdater()
    {
        HydraSdk.addVpnListener(new VpnStateListener() {
            @Override
            public void vpnStateChanged(VPNState vpnState) {
                currentVpnState = vpnState;
                if(vpnState.name().equals("IDLE"))
                {
                    if(status.connection_status != enums.connection_status.connected && status.connection_status != enums.connection_status.reconnecting && status.connection_status != enums.connection_status.restarting)
                    {
                        home_model.getInstance().getHomeInstance().onDisConnected();
                        status.connection_status = enums.connection_status.no_status;
                        isLoading = false;
                    }
                }
                else if(vpnState.name().equals("CONNECTED") && status.connection_status != enums.connection_status.unconnected)
                {
                    home_model.getInstance().getHomeInstance().onConnected();
                    status.connection_status = enums.connection_status.no_status;
                    onUpdateFlag();
                    isLoading = false;
                }
                else if(vpnState.name().equals("PAUSED") || vpnState.name().equals("CONNECTING_VPN") || vpnState.name().equals("CONNECTING_PERMISSIONS"))
                {
                    home_model.getInstance().getHomeInstance().onConnecting();
                    isLoading = false;
                }
                else if(vpnState.name().equals("DISCONNECTINGN"))
                {
                    home_model.getInstance().getHomeInstance().onStopping();
                    isLoading = false;
                }
            }

            @Override
            public void vpnError(@NonNull HydraException e) {

            }
        });
    }

    private void stateManager()
    {
        new Thread()
        {
            public void run()
            {
                while (true)
                {
                    try
                    {
                        sleep(1000);

                        if(status.connection_status == enums.connection_status.no_status || status.app_status == enums.app_status.paused || (isLoading && status.connection_status != enums.connection_status.unconnected)){
                            continue;
                        }

                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }


                    isConnected(new Callback<Boolean>() {

                        @Override
                        public void success(@NonNull Boolean aBoolean) {
                            if(aBoolean){

                                if(status.connection_status == enums.connection_status.restarting)
                                {
                                    HydraSdk.restartVpn(createConnectionRequest(), new Callback<Bundle>() {
                                        @Override
                                        public void success(@NonNull Bundle bundle) {
                                            if( status.connection_status != enums.connection_status.restarting)
                                            {
                                                status.connection_status = enums.connection_status.no_status;
                                            }
                                            new Thread()
                                            {
                                                public void run()
                                                {
                                                    if(status.connection_status == enums.connection_status.unconnected)
                                                    {
                                                        disconnectConnection();
                                                    }
                                                }
                                            }.start();
                                        }

                                        @Override
                                        public void failure(HydraException e) {
                                            failureHandler(enums.error_handler.disconnect_fallback);
                                        }
                                    });
                                }
                                if(status.connection_status == enums.connection_status.connected)
                                {
                                    status.connection_status = enums.connection_status.no_status;
                                }
                                else if(status.connection_status == enums.connection_status.unconnected)
                                {
                                    disconnectConnection();
                                }
                            }
                            else
                            {
                                if(status.connection_status == enums.connection_status.connected || status.connection_status == enums.connection_status.restarting || status.connection_status == enums.connection_status.reconnecting)
                                {
                                    isLoading = true;
                                    if(!HydraSdk.isLoggedIn())
                                    {
                                        connect();
                                    }
                                    else
                                    {
                                        HydraSdk.startVPN(createConnectionRequest(), new Callback<ServerCredentials>() {
                                            @Override
                                            public void success(ServerCredentials serverCredentials) {

                                                if(status.connection_status != enums.connection_status.restarting)
                                                {
                                                    status.connection_status = enums.connection_status.no_status;
                                                }
                                                new Thread()
                                                {
                                                    public void run()
                                                    {
                                                        if(status.connection_status == enums.connection_status.unconnected)
                                                        {
                                                            disconnectConnection();
                                                        }
                                                    }
                                                }.start();
                                            }

                                            @Override
                                            public void failure(HydraException e) {
                                                failureHandler(enums.error_handler.disconnect_fallback);
                                            }
                                        });
                                    }
                                }
                                else if(status.connection_status == enums.connection_status.unconnected)
                                {
                                    if(HydraSdk.isLoggedIn())
                                    {
                                        //home_model.getInstance().getHomeInstance().onStopping();
                                        disconnectConnection();
                                    }
                                    else
                                    {
                                        //home_model.getInstance().getHomeInstance().onDisConnected();
                                    }
                                }
                            }
                        }
                        @Override
                        public void failure(@NonNull HydraException e) {
                            failureHandler(enums.error_handler.disconnect_fallback);
                        }
                    });
                }
            }
        }.start();
    }

    private SessionConfig createConnectionRequest()
    {
        List<String> bypassDomains = new LinkedList<>();

        bypassDomains.add("*facebook.com");
        bypassDomains.add("*wtfismyip.com");

        SessionConfig.Builder builder = new SessionConfig.Builder()
                .withReason(TrackingConstants.GprReasons.M_UI)
                .addDnsRule(DnsRule.Builder.bypass().fromDomains(bypassDomains));

        if(!server_name.equals(strings.emptySTR))
        {
            builder.withVirtualLocation(server_name);
        }
        SessionConfig build_res = builder.build();
        return build_res;
    }

    public void resetLoading()
    {
        isLoading = false;
    }

    public void disconnectConnection() {

        HydraSdk.stopVPN(TrackingConstants.GprReasons.M_UI, new CompletableCallback() {
            @Override
            public void complete() {
                //home_model.getInstance().getHomeInstance().onDisConnected();
                isLoading = false;
                if(status.connection_status == enums.connection_status.restarting)
                {
                    status.connection_status = enums.connection_status.restarting;
                    isLoading = false;
                }
                else if(status.connection_status != enums.connection_status.reconnecting)
                {
                    status.connection_status = enums.connection_status.no_status;
                }
                else
                {
                    status.connection_status = enums.connection_status.connected;
                }
            }

            @Override
            public void error(HydraException e) {
                isLoading = false;
                status.connection_status = enums.connection_status.no_status;
            }
        });
    }

    private void failureHandler(enums.error_handler fallback)
    {
        if(enums.error_handler.disconnect_fallback == fallback){

            status.connection_status = enums.connection_status.unconnected;
            //home_model.getInstance().getHomeInstance().onDisConnected();
            status.connection_status = enums.connection_status.no_status;
            isLoading = false;
        }
    }

    private void onUpdateFlag()
    {
        Log.i("BREAKER TEXT","BREAKER TEXT");
        HydraSdk.getVpnState(new Callback<VPNState>() {
            @Override
            public void success(@NonNull VPNState state) {
                if (state == VPNState.CONNECTED) {
                    HydraSdk.getSessionInfo(new Callback<SessionInfo>() {
                        @Override
                        public void success(@NonNull SessionInfo sessionInfo) {

                            server_name = CredentialsCompat.getServerCountry(sessionInfo.getCredentials());
                            setCurrentFlag();
                        }

                        @Override
                        public void failure(@NonNull HydraException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }

            @Override
            public void failure(@NonNull HydraException e) {
                e.printStackTrace();
            }
        });
    }

    public void chooseServer(Country server)
    {
        if(!server_name.equals(server.getCountry()))
        {
            server_name = server.getCountry();
            isLoading = true;
            status.connection_status = enums.connection_status.restarting;
            disconnectConnection();
        }

    }

    private void setCurrentFlag()
    {
        home_model.getInstance().getHomeInstance().onSetFlag(server_name);
    }


    private void connectToVpn() {
        createConnectionRequest();
    }

    private void isConnected(Callback<Boolean> callback) {
        HydraSdk.getVpnState(new Callback<VPNState>() {
            @Override
            public void success(@NonNull VPNState vpnState) {
                callback.success(vpnState == VPNState.CONNECTED);
            }

            @Override
            public void failure(@NonNull HydraException e) {
                callback.success(false);
            }
        });
    }





















    /*First Time Installations*/

    public void connect() {
        AuthMethod authMethod = AuthMethod.anonymous();
        HydraSdk.login(authMethod, new Callback<User>() {
            @Override
            public void success(User user) {
                isLoading = false;
                if(status.connection_status != enums.connection_status.unconnected)
                {
                    status.connection_status = enums.connection_status.connected;
                }
            }

            @Override
            public void failure(HydraException e) {
                isLoading = false;
            }
        });
    }


    public void startVPN() {
        initHydraSdk();
        loadServers();
    }

    private void loadServers() {
        HydraSdk.countries(new Callback<List<Country>>() {
            @Override
            public void success(List<Country> countries) {
                list_model.getInstance().setModel(countries);
                status.servers_loaded = enums.connection_servers.loaded;
            }

            @Override
            public void failure(HydraException e) {
            }
        });
    }

    private void initHydraSdk() {
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
                .observeNetworkChanges(true) //sdk will handle network changes and start/stop vpn
                .captivePortal(true) //sdk will handle if user is behind captive portal wifi
                .moveToIdleOnPause(false)//sdk will report PAUSED state
                .build();
        HydraSdk.init(home_model.getInstance().getHomeInstance(), clientInfo, notificationConfig, config);
    }

    private SharedPreferences getPrefs() {
        return home_model.getInstance().getHomeInstance().getSharedPreferences(BuildConfig.SHARED_PREFS, Context.MODE_PRIVATE);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(status.connection_status == enums.connection_status.connected)
            {
                CharSequence name = "Sample VPN";
                String description = "VPN notification";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = home_model.getInstance().getHomeInstance().getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

}
