package art.aelaort;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(exclude = {
		HibernateJpaAutoConfiguration.class,
		KafkaAutoConfiguration.class,
		RabbitAutoConfiguration.class,
		BatchAutoConfiguration.class,
		MailSenderAutoConfiguration.class,
})
@EnableCaching
public class Main {
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}
