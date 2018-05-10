package com.changxiao.downloadservicedemo.io;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * @author olamy
 * @version $Id: EasyX509TrustManager.java 765355 2009-04-15 20:59:07Z evenisse
 *          $
 * @since 1.2.3
 */
public class ZREasyX509TrustManager implements X509TrustManager {

    private X509TrustManager mStandardTrustManager = null;

    /**
     * Constructor for EasyX509TrustManager.
     */
    public ZREasyX509TrustManager(KeyStore keystore)
            throws NoSuchAlgorithmException, KeyStoreException {
        super();
        TrustManagerFactory factory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        factory.init(keystore);
        TrustManager[] trustmanagers = factory.getTrustManagers();
        if (trustmanagers.length == 0) {
            throw new NoSuchAlgorithmException("no trust manager found");
        }
        mStandardTrustManager = (X509TrustManager) trustmanagers[0];
    }

    /**
     * @see X509TrustManager#checkClientTrusted(X509Certificate[],
     *      String authType)
     */
    @Override
    public void checkClientTrusted(X509Certificate[] certificates,
            String authType) throws CertificateException {
        mStandardTrustManager.checkClientTrusted(certificates, authType);
    }

    /**
     * @see X509TrustManager#checkServerTrusted(X509Certificate[],
     *      String authType)
     */
    @Override
    public void checkServerTrusted(X509Certificate[] certificates,
            String authType) throws CertificateException {
        if ((certificates != null) && (certificates.length == 1)) {
            certificates[0].checkValidity();
        } else {
            mStandardTrustManager.checkServerTrusted(certificates, authType);
        }
    }

    /**
     * @see X509TrustManager#getAcceptedIssuers()
     */
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return mStandardTrustManager.getAcceptedIssuers();
    }

}
