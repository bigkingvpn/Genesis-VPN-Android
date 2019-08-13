package com.darkweb.genesisvpn.application.helperManager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.darkweb.genesisvpn.application.constants.enums;
import com.darkweb.genesisvpn.application.proxyManager.proxy_controller;
import com.darkweb.genesisvpn.application.status.status;

public class OnClearFromRecentService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        proxy_controller.getInstance().disconnectConnection();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        stopSelf();
        proxy_controller.getInstance().disconnectConnection();
    }
}