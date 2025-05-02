package dev.dmgiangi.k8.log.scraper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class K8LogScraperApplication {

    public static void main(String[] args) {
        SpringApplication.run(K8LogScraperApplication.class, args);
    }

}
