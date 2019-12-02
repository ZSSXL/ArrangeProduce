package cn.edu.jxust.arrangeproduce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author ZSS
 * @date 2019-11-29 15:15
 * @description 启动类
 */
@EnableJpaAuditing
@SpringBootApplication
public class ArrangeProduceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArrangeProduceApplication.class, args);
    }

}
