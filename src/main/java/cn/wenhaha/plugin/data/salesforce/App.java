package cn.wenhaha.plugin.data.salesforce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * --------
 *
 * @author ：wyndem
 * @Date ：Created in 2022-07-16 16:43
 */
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        new EventListenImp().onStart("1");
        SpringApplication.run(App.class, args);
    }
}
