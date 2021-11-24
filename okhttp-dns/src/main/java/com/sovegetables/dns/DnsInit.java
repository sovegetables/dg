package com.sovegetables.dns;

import android.content.Context;
import com.tencent.msdk.dns.DnsConfig;
import com.tencent.msdk.dns.DnsService;

public class DnsInit {

    public static void init(Context context, String appId, String dnsId){
        DnsConfig it = new DnsConfig.Builder()
                .appId(appId)
                .initBuiltInReporters()
                .dnsId(dnsId)
                .timeoutMills(5000)
                .build();
        DnsService.init(context.getApplicationContext(), it);
    }
}
