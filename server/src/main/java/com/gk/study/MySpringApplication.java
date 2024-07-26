package com.gk.study;

import com.gk.study.framework.EnableSgridServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lengqin1024(微信)
 * @email net936@163.com
 */
@SpringBootApplication
@EnableSgridServer
public class MySpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySpringApplication.class, args);
    }
}
