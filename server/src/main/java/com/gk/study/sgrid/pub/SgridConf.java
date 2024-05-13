package com.gk.study.sgrid.pub;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;

@Component
public class SgridConf implements SgridConfInterface {
    // Static
    private final static String SGRID_TARGET_PORT = "SGRID_TARGET_PORT";
    private final static String SGRID_DEV_CONF = "sgrid.yml";


    private final static String SGRID_CONFIG = "SGRID_CONFIG";

    public Server server;
    public HashMap<String, String> config = new HashMap<>();

    private boolean isConfigured = false; // 标志位，表示配置是否已被设置

    @PostConstruct
    public void init() {
        if (!isConfigured) {
            this.SetSgridConf();
            this.SetDBProperty(config.get("mysql-addr"), config.get("mysql-username"), config.get("mysql-password"));
            isConfigured = true; // 设置标志位，表示配置已完成
        }
    }


    private SgridConf loadDevConf(Resource resource) throws IOException {
        Yaml yaml = new Yaml();
        return yaml.loadAs(resource.getInputStream(), SgridConf.class);
    }

    private SgridConf loadProdConf(String yamlContent) throws IOException {
        Yaml yaml = new Yaml();
        return yaml.loadAs(yamlContent, SgridConf.class);
    }

    @Override
    public void SetDBProperty(String url, String username, String password) {
        System.setProperty("spring.datasource.url", url);
        System.setProperty("spring.datasource.username", username);
        System.setProperty("spring.datasource.password", password);
    }

    @Override
    public void SetSgridConf() {
        try {
            String sgridProdConf = System.getenv(SGRID_CONFIG);
            String sgridTargetPort = System.getenv(SGRID_TARGET_PORT);
            if (sgridProdConf == null || sgridProdConf.isEmpty()) {
                System.out.println("run dev ::  " + SGRID_DEV_CONF);
                Resource resource = new ClassPathResource(SGRID_DEV_CONF);
                SgridConf sgridConf = loadDevConf(resource);
                setServer(sgridConf.server);
                setConfig(sgridConf.config);
                System.out.println("server :: " + server);
                System.out.println("config :: " + config);
            } else {
                System.out.println("run prod :: " + sgridProdConf);
                SgridConf sgridConf = loadProdConf(sgridProdConf);
                setServer(sgridConf.server);
                setConfig(sgridConf.config);
                server.setPort(Integer.valueOf(sgridTargetPort));
            }
        } catch (Exception e) {
            System.out.println("Init Sgrid Configuration Error :: " + e);
        }
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void setConfig(HashMap<String, String> config) {
        this.config = config;
    }


//    public static void main(String[] args) throws IOException {
//        String content = "server:\n" +
//                "  name: SgirdCloud\n" +
//                "  host: 127.0.0.1\n" +
//                "  port: 15411\n" +
//                "  protocol: http\n" +
//                "  language: node\n" +
//                "config:\n" +
//                "  db_master:  root:123456@tcp(127.0.0.1:3306)/sgrid?charset=utf8&parseTime=true\n" +
//                "  db_slave: root:123456@tcp(127.0.0.1:3306)/sgrid?charset=utf8&parseTime=true";
//
//        SgridConf sgridConf = new SgridConf();
//        Properties properties = new Properties();
//        SgridConf sgridConf1 = sgridConf.loadProdConf(content);
//        sgridConf.config = sgridConf1.config;
//        sgridConf.server = sgridConf1.server;
//        System.out.println("sgridConf.config || " + sgridConf.config);
//        System.out.println("sgridConf.server || " + sgridConf.server);
//    }
}
