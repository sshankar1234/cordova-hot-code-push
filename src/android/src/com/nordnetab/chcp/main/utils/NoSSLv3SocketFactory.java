/*Copyright 2015 Bhavit Singh Sengar
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package com.nordnetab.chcp.main.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;


public class NoSSLv3SocketFactory extends SSLSocketFactory{
    private final SSLSocketFactory delegate;

public NoSSLv3SocketFactory() {
    this.delegate = HttpsURLConnection.getDefaultSSLSocketFactory();
}

public NoSSLv3SocketFactory(SSLSocketFactory delegate) {
    this.delegate = delegate;
}

@Override
public String[] getDefaultCipherSuites() {
    return delegate.getDefaultCipherSuites();
}

@Override
public String[] getSupportedCipherSuites() {
    return delegate.getSupportedCipherSuites();
}

private Socket makeSocketSafe(Socket socket) {
    if (socket instanceof SSLSocket) {
        socket = new NoSSLv3SSLSocket((SSLSocket) socket);
    }
    return socket;
}

@Override
public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
    return makeSocketSafe(delegate.createSocket(s, host, port, autoClose));
}

@Override
public Socket createSocket(String host, int port) throws IOException {
    return makeSocketSafe(delegate.createSocket(host, port));
}

@Override
public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
    return makeSocketSafe(delegate.createSocket(host, port, localHost, localPort));
}

@Override
public Socket createSocket(InetAddress host, int port) throws IOException {
    return makeSocketSafe(delegate.createSocket(host, port));
}

@Override
public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
    return makeSocketSafe(delegate.createSocket(address, port, localAddress, localPort));
}