package VeterinaryClinicBE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan({"fortech.team2.services", "fortech.team2.restcontrollers", "fortech.team2.validation", "fortech.team2.persistence", "fortech.team2.services.config"})
@EnableJpaRepositories("fortech.team2.persistence")
@EntityScan("fortech.team2.model")
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class VeterinaryClinicBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(VeterinaryClinicBeApplication.class, args);
	}

}
