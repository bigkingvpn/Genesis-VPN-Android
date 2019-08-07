package com.darkweb.genesisvpn.application.proxyManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
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
import com.anchorfree.hydrasdk.compat.CredentialsCompat;
import com.anchorfree.hydrasdk.dns.DnsRule;
import com.anchorfree.hydrasdk.exceptions.HydraException;
import com.anchorfree.hydrasdk.vpnservice.VPNState;
import com.anchorfree.hydrasdk.vpnservice.connectivity.NotificationConfig;
import com.anchorfree.reporting.TrackingConstants;
import com.darkweb.genesisvpn.BuildConfig;
import com.darkweb.genesisvpn.R;
import com.darkweb.genesisvpn.application.constants.enums;
import com.darkweb.genesisvpn.application.constants.keys;
import com.darkweb.genesisvpn.application.constants.strings;
import com.darkweb.genesisvpn.application.homeManager.home_model;
import com.darkweb.genesisvpn.application.pluginManager.admanager;
import com.darkweb.genesisvpn.application.pluginManager.message_manager;
import com.darkweb.genesisvpn.application.pluginManager.preference_manager;
import com.darkweb.genesisvpn.application.serverManager.list_model;
import com.darkweb.genesisvpn.application.status.status;

import java.util.LinkedList;
import java.util.List;


public class proxy_controller {

    /*INITIALIZATIONS*/

    private static final proxy_controller ourInstance = new proxy_controller();
    public static proxy_controller getInstance() {
        return ourInstance;
    }

    /*LOCAL VARIABLE DECLARATIONS*/

    private static final String CHANNEL_ID = "vpn";
    private String server_name = strings.emptySTR;
    private boolean serverChanged = false;

    /*HELPER METHODS*/

    public void autoStart()
    {
        if(!preference_manager.getInstance().getBool(keys.app_initialized_key,false))
        {
            home_model.getInstance().getHomeInstance().onStartView();
        }
    }

    public void disConnect() {

        isConnected(new Callback<Boolean>() {
            @Override
            public void success(@NonNull Boolean aBoolean) {
                if (aBoolean) {
                    HydraSdk.stopVPN(TrackingConstants.GprReasons.M_UI, new CompletableCallback() {
                        @Override
                        public void complete() {
                            Log.i("SUPS1","S1 : " + serverChanged);
                            if(serverChanged)
                            {
                                Log.i("SUPS1","S2");
                                status.connection_status = enums.connection_status.unconnected;
                                home_model.getInstance().getHomeInstance().onStartView();
                            }
                            else
                            {
                                Log.i("SUPS1","S3");
                                status.connection_status = enums.connection_status.unconnected;
                                home_model.getInstance().getHomeInstance().onDisConnected();
                            }
                            Log.i("SUPS1","S4");
                            serverChanged = false;
                        }

                        @Override
                        public void error(HydraException e) {
                            if(status.connection_status != enums.connection_status.connected) {
                                status.connection_status = enums.connection_status.unconnected;
                                home_model.getInstance().getHomeInstance().onDisConnected();
                            }
                            Log.i("SUPS1","S10 : " + serverChanged);
                            serverChanged = false;
                        }
                    });
                }
                Log.i("SUPS1","S20 : " + serverChanged);
            }

            @Override
            public void failure(@NonNull HydraException e) {
                if(status.connection_status != enums.connection_status.connected) {
                    status.connection_status = enums.connection_status.unconnected;
                    home_model.getInstance().getHomeInstance().onDisConnected();
                }
                Log.i("SUPS1","S30 : " + serverChanged);
                serverChanged = false;
            }
        });
    }

    private void getCurrentServer() {
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
                        }
                    });
                } else {
                }
            }

            @Override
            public void failure(@NonNull HydraException e) {

            }
        });
    }

    public void forcedExit()
    {
            HydraSdk.stopVPN(TrackingConstants.GprReasons.M_UI, new CompletableCallback() {
            @Override
            public void complete() {
            }

            @Override
            public void error(HydraException e) {
            }
        });

        //home_model.getInstance().getHomeInstance().stopService(home_model.getInstance().getHomeInstance());
        //stopService(Intent intent)
    }

    public void chooseServer(Country server)
    {
        if(!server_name.equals(server.getCountry()))
        {
            server_name = server.getCountry();
            if(status.connection_status != enums.connection_status.connected)
            {
                HydraSdk.stopVPN(TrackingConstants.GprReasons.M_UI, new CompletableCallback() {
                    @Override
                    public void complete() {
                        status.connection_status = enums.connection_status.unconnected;
                        home_model.getInstance().getHomeInstance().onStartView();
                    }

                    @Override
                    public void error(HydraException e) {
                    }
                });

            }
            else if(status.connection_status == enums.connection_status.connected)
            {
                serverChanged = true;
                disConnect();
            }
            home_model.getInstance().getHomeInstance().onHideFlag();
        }
    }

    public void onOrientationChanged() {

        isConnected(new Callback<Boolean>()
        {
            @Override
            public void success(@NonNull Boolean aBoolean)
            {
                if(aBoolean)
                {
                    status.connection_status = enums.connection_status.connected;
                    home_model.getInstance().getHomeInstance().onConnected();
                    getCurrentServer();
                }
            }
            @Override
            public void failure(@NonNull HydraException e)
            {
            }
        });
    }

    private void setCurrentFlag()
    {
        home_model.getInstance().getHomeInstance().onSetFlag(server_name);
    }

    public void connect() {
        AuthMethod authMethod = AuthMethod.anonymous();
        if(HydraSdk.isLoggedIn())
        {
            connectToVpn();
        }
        else
        {
            HydraSdk.login(authMethod, new Callback<User>() {
                @Override
                public void success(User user) {
                    connectToVpn();
                }

                @Override
                public void failure(HydraException e) {
                }
            });
        }
    }


    public void startVPN() {
        initHydraSdk();
        loadServers();
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

    public void setNewHostAndCarrier(String hostUrl, String carrierId) {
        SharedPreferences prefs = getPrefs();
        if (TextUtils.isEmpty(hostUrl)) {
            prefs.edit().remove(BuildConfig.STORED_HOST_URL_KEY).apply();
        } else {
            prefs.edit().putString(BuildConfig.STORED_HOST_URL_KEY, hostUrl).apply();
        }

        if (TextUtils.isEmpty(carrierId)) {
            prefs.edit().remove(BuildConfig.STORED_CARRIER_ID_KEY).apply();
        } else {
            prefs.edit().putString(BuildConfig.STORED_CARRIER_ID_KEY, carrierId).apply();
        }
        initHydraSdk();
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

    private void connectTrigger()
    {
        isConnected(new Callback<Boolean>() {
            @Override
            public void success(@NonNull Boolean aBoolean) {
                if(!aBoolean)
                {
                    List<String> bypassDomains = new LinkedList<>();

                    bypassDomains.add("*facebook.com");
                    bypassDomains.add("*wtfismyip.com");

                    SessionConfig.Builder builder = new SessionConfig.Builder()
                            .withReason(TrackingConstants.GprReasons.M_UI)
                            .addDnsRule(DnsRule.Builder.bypass().fromDomains(bypassDomains));

                    if(!server_name.equals(strings.emptySTR))
                    {
                        //server_name = list_model.getInstance().getModel().get(0).getCountryModel().getCountry();
                        builder.withVirtualLocation(server_name);
                    }
                    SessionConfig build_res = builder.build();
                    HydraSdk.startVPN(build_res, new Callback<ServerCredentials>() {
                        @Override
                        public void success(ServerCredentials serverCredentials) {

                            preference_manager.getInstance().setBool(keys.app_initialized_key,true);
                            if(status.connection_status == enums.connection_status.unconnected || status.connection_status == enums.connection_status.stoping) {
                                status.connection_status = enums.connection_status.connected;
                                disConnect();
                            }
                            else
                            {
                                getCurrentServer();
                                admanager.getInstance().showAd();
                                status.connection_status = enums.connection_status.connected;
                                home_model.getInstance().getHomeInstance().onConnected();
                            }
                            serverChanged = false;
                        }

                        @Override
                        public void failure(HydraException e) {
                            if(status.connection_status != enums.connection_status.connected) {
                                status.connection_status = enums.connection_status.unconnected;
                                home_model.getInstance().getHomeInstance().onDisConnected();
                            }
                            else if(status.connection_status == enums.connection_status.connected)
                            {
                                getCurrentServer();
                                status.connection_status = enums.connection_status.connected;
                                home_model.getInstance().getHomeInstance().onConnected();
                                serverChanged = false;
                            }
                            serverChanged = false;
                        }
                    });
                }
                else
                {
                    getCurrentServer();
                    status.connection_status = enums.connection_status.connected;
                    home_model.getInstance().getHomeInstance().onConnected();
                    serverChanged = false;
                }
                Log.i("SUPS1","S40 : " + serverChanged);
            }

            @Override
            public void failure(@NonNull HydraException e) {
                if(status.connection_status != enums.connection_status.connected) {
                    status.connection_status = enums.connection_status.unconnected;
                    home_model.getInstance().getHomeInstance().onDisConnected();
                }
                else if(status.connection_status == enums.connection_status.connected)
                {
                    getCurrentServer();
                    status.connection_status = enums.connection_status.connected;
                    home_model.getInstance().getHomeInstance().onConnected();
                    serverChanged = false;
                }
                Log.i("SUPS1","S50 : " + serverChanged);
                serverChanged = false;
            }
        });
    }

    private void connectToVpn() {
        connectTrigger();
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

    public void getProxiesList()
    {
    }

}
