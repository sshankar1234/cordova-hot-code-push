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
        //Custom Code added to support kitkat https calls
        SSLContext sslcontext = SSLContext.getInstance("TLSv1");
        sslcontext.init(null, null, null);
        //SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(sslcontext.getSocketFactory());
        //HttpsURLConnection.setDefaultSSLSocketFactory(NoSSLv3Factory);
        //l_connection = (HttpsURLConnection) l_url.openConnection();
        //l_connection.connect();
        final URLConnection urlConnection = connectionURL.openConnection();
        //final HttpsURLConnection urlConnection = (HttpsURLConnection)connectionURL.openConnection();
        Log.d("Printing UrlConnection =" , urlConnection.toString());
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
