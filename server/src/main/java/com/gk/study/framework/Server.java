package com.gk.study.framework;


public class Server {
    public Integer port;
    public String name;
    public String host;
    public String protocol;
    public String language;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "[Sgrid-Java] Server{" +
                "port=" + port +
                ", name='" + name + '\'' +
                ", host='" + host + '\'' +
                ", protocol='" + protocol + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
