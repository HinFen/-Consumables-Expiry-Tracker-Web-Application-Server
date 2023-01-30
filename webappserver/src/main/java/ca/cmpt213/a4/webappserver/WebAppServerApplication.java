package ca.cmpt213.a4.webappserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Class that Starts up the WebAppServer
 *
 * @author hfk10
 */
@SpringBootApplication
public class WebAppServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebAppServerApplication.class, args);
    }

}
