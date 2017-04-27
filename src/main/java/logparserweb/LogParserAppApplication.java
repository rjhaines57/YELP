package logparserweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
//@ComponentScan({"controllers"})
public class LogParserAppApplication {

	
	
	public static void main(String[] args) {
		SpringApplication.run(LogParserAppApplication.class, args);
	}
}
