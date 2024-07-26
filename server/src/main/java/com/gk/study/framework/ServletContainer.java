package com.gk.study.framework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;

public class ServletContainer implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

    @Autowired
    private SgridConf sgridConf;

    @Override
    public void customize(ConfigurableServletWebServerFactory factory) {
        System.out.println("[Sgrid-Java] [info] start Init Sgrid servletContainer ");
        System.out.println("[Sgrid-Java] [info] get sgrid remote configuration " + sgridConf);
        int port = 8080;
        if (sgridConf.server.port != null) {
            port = sgridConf.server.port;
        }
        factory.setPort(port);
        System.out.println("[Sgrid-Java] [info] End Init Sgrid servletContainer ");
//        String host = null;
//        if (sgridConf.server.host != null) {
//            host = sgridConf.server.host;
//        }
//        try {
//            factory.setAddress(InetAddress.getByName(host));
//        } catch (UnknownHostException e) {
//            System.err.println("[Sgrid-Java] [customize] " + e.getMessage());
//            e.printStackTrace();
//        }
    }

}
