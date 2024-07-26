package com.gk.study.framework;

import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;

@Component
public class SgridConf {

    private boolean isConfigured = false; // 标志位，表示配置是否已被设置
    private static final String SGRID_TARGET_PORT = "SGRID_TARGET_PORT";
    private static final String SGRID_DEV_CONF = "sgrid.yml";
    private static final String SGRID_CONFIG = "SGRID_CONFIG";
    private static final String SGRID_PROCESS_INDEX = "SGRID_PROCESS_INDEX";

    @Setter
    public Server server;
    @Setter
    public HashMap<String, String> config = new HashMap<>();

    @PostConstruct
    public void init() {
        if (!isConfigured) {
            isConfigured = true; // 设置标志位，表示配置已完成
            // 这一步必须要执行
            this.SetSgridConf();
            // 这一步选执行，只是提供参考设置的样例代码 ！
            this.SetDBProperty(
                    get("mysql-addr"),
                    get("mysql-username"),
                    get("mysql-password")
            );
        }

    }

    @Bean
    public ServletContainer servletContainer() {
        System.out.println("[Sgrid-Java] [info] init servletContainer ");
        return new ServletContainer();
    }

    @Override
    public String toString() {
        return (
                "SgridConf [server=" +
                        server +
                        ", config=" +
                        config +
                        ", isConfigured=" +
                        isConfigured +
                        "]"
        );
    }

    private SgridConf loadDevConf(Resource resource) throws IOException {
        Yaml yaml = new Yaml();
        return yaml.loadAs(resource.getInputStream(), SgridConf.class);
    }

    private SgridConf loadProdConf(String yamlContent) throws IOException {
        Yaml yaml = new Yaml();
        return yaml.loadAs(yamlContent, SgridConf.class);
    }

    public void SetDBProperty(String url, String username, String password) {
        System.setProperty("spring.datasource.url", url);
        System.setProperty("spring.datasource.username", username);
        System.setProperty("spring.datasource.password", password);
    }

    public void SetSgridConf() {
        try {
            String sgridProdConf = System.getenv(SGRID_CONFIG);
            String sgridTargetPort = System.getenv(SGRID_TARGET_PORT);
            if (sgridProdConf == null || sgridProdConf.isEmpty()) { // 测试环境下
                Resource resource = new ClassPathResource(SGRID_DEV_CONF);
                SgridConf sgridConf = loadDevConf(resource);
                setServer(sgridConf.server);
                setConfig(sgridConf.config);
            } else { // 生产环境下 有配置 需要将 PORT 塞到 server.config中
                SgridConf sgridConf = loadProdConf(sgridProdConf);
                setServer(sgridConf.server);
                setConfig(sgridConf.config);
                server.setPort(Integer.valueOf(sgridTargetPort));
            }
        } catch (Exception e) {
            System.err.println(
                    "[Sgrid-Java] [error] Error Init SetSgridConf " + e
            );
        }
    }

    public boolean threadLock() {
        String sgridProdConf = System.getenv(SGRID_CONFIG);
        if (sgridProdConf.isEmpty()) {
            return System.getenv(SGRID_PROCESS_INDEX).equals("1");
        }
        return true;
    }

    public String get(String path) {
        return config.get(path);
    }

}
