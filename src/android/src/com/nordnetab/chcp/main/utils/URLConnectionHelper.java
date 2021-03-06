package com.nordnetab.chcp.main.utils;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import android.util.Log;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import com.google.android.gms.security.ProviderInstaller;

/**
 * Created by Nikolay Demyankov on 03.06.16.
 * <p/>
 * Helper class to work with URLConnection
 */
public class URLConnectionHelper {

    // connection timeout in milliseconds
    private static final int CONNECTION_TIMEOUT = 30000;

    // data read timeout in milliseconds
    private static final int READ_TIMEOUT = 30000;

    /**
     * Create URLConnection instance.
     *
     * @param url            to what url
     * @param requestHeaders additional request headers
     * @return connection instance
     * @throws IOException when url is invalid or failed to establish connection
     */
    public static URLConnection createConnectionToURL(final String url, final Map<String, String> requestHeaders) throws IOException {
        final URL connectionURL = URLUtility.stringToUrl(url);
        if (connectionURL == null) {
            throw new IOException("Invalid url format: " + url);
        }
        HttpsURLConnection urlConnection = null;
        try {
            //Custom Code added to support kitkat https calls
            /*SSLContext sslcontext = SSLContext.getInstance("TLSv1");
            System.out.println("--------sslcontext = "+ sslcontext);
            sslcontext.init(null, null, null);
            //Not disabling SSLV3 to reduce impact
            SSLSocketFactory NoSSLv3Factory = new com.nordnetab.chcp.main.utils.NoSSLv3SocketFactory(sslcontext.getSocketFactory());
            HttpsURLConnection.setDefaultSSLSocketFactory(NoSSLv3Factory);
                //l_connection = (HttpsURLConnection) l_url.openConnection();
            //l_connection.connect();
            //final URLConnection urlConnection = connectionURL.openConnection();
            */
            ProviderInstaller.installIfNeeded(getApplicationContext());
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            SSLEngine engine = sslContext.createSSLEngine();
            urlConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            urlConnection = (HttpsURLConnection)connectionURL.openConnection();
            Log.d("Printing UrlConnection =" , urlConnection.toString());
        }catch(Exception e){
            e.printStackTrace();
        }
        
        urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
        urlConnection.setReadTimeout(READ_TIMEOUT);

        if (requestHeaders != null) {
            for (final Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        return urlConnection;
    }

}
