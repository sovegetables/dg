package com.sovegetables.dns;

import com.sovegetables.android.logger.AppLogger;
import okhttp3.Dns;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class AppDns implements Dns {
    private static final String TAG = "AppDns";
    @Override
    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        List<InetAddress> inetAddresses = null;
        try {
            inetAddresses = DnsServiceWrapper.INSTANCE.getAddrsByName(hostname);
        } catch (Exception e) {
            AppLogger.e(TAG, e);
        }
        if(inetAddresses == null || inetAddresses.isEmpty()){
            return Dns.SYSTEM.lookup(hostname);
        }
        return inetAddresses;
    }
}
