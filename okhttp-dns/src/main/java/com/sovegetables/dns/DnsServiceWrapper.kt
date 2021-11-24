package com.sovegetables.dns

import com.tencent.msdk.dns.DnsService
import com.tencent.msdk.dns.core.IpSet
import java.net.InetAddress
import java.net.UnknownHostException

object DnsServiceWrapper {

    private val EMPTY_ADDRESSES = arrayListOf<InetAddress>()

    private val proxyHost by lazy { System.getProperty("http.proxyHost") }
    private val proxyPort by lazy { System.getProperty("http.proxyPort") }

    private val useHttpProxy by lazy {
        @Suppress("LocalVariableName")
        val _useHttpProxy = null != proxyHost && null != proxyPort
        com.sovegetables.android.logger.AppLogger.d("useHttpProxy:", _useHttpProxy)
        _useHttpProxy
    }

    val useHttpDns = true

    fun getAddrsByName(hostname: String): List<InetAddress> {
        // 客户端启用HTTP代理时, 不使用HTTPDNS
        if (useHttpProxy || !useHttpDns) {
            // LocalDNS只取第一个IP
            return getAddrByNameByLocal(hostname)?.let { arrayListOf(it) } ?: EMPTY_ADDRESSES
        }
        com.sovegetables.android.logger.AppLogger.d("DnsServiceWrapper","DnsServiceWrapper lookup by HttpDns")
        val ipSet = DnsService.getAddrsByName(hostname)
        if (IpSet.EMPTY == ipSet) {
            return EMPTY_ADDRESSES
        }
        // 当前v6环境质量较差, 优先选择v4 IP, 且只考虑使用第一个v6 IP
        return when {
            ipSet.v6Ips.isNotEmpty() && ipSet.v4Ips.isNotEmpty() ->
                arrayListOf(
                    *(ipSet.v4Ips.map { InetAddress.getByName(it) }.toTypedArray()),
                    InetAddress.getByName(ipSet.v6Ips[0])
                )
            ipSet.v6Ips.isNotEmpty() -> arrayListOf(InetAddress.getByName(ipSet.v6Ips[0]))
            ipSet.v4Ips.isNotEmpty() -> ipSet.v4Ips.map { InetAddress.getByName(it) }.toMutableList()
            else -> EMPTY_ADDRESSES
        }
    }

    private fun getAddrByNameByLocal(hostname: String) =
        try {
            InetAddress.getByName(hostname)
        } catch (e: UnknownHostException) {
            null
        }
}
