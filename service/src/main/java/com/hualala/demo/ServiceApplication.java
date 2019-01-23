package com.hualala.demo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ServiceApplication {

//    static {
//        System.setProperty("java.security.auth.login.config","classpath:kafka/kafka_client_jaas_dohko.conf");
//    }


    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = SpringApplication.run(ServiceApplication.class, args);


    }

}
