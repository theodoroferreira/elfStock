package br.com.pb.mshistorico;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Ms-History",
				description = "O MS History tem a responsabilidade de armazenar o histórico dos pedidos",
				version = "1.0.0",
				contact = @Contact(
						name = "Theodoro Ferreira",
						email = "theogferreira@outlook.com",
						url = "https://github.com/theodoroferreira"
				),
				license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html"),
				termsOfService = "https://example.com/terms/"
		),
		servers = @Server(url = "http://localhost:8082/api", description = "Base URL to access the entire application"),
		externalDocs = @ExternalDocumentation(
				description = "Repository on GitHub",
				url = "https://github.com/theodoroferreira/avaliacao-individual"
		)
)
public class MsHistoricoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsHistoricoApplication.class, args);
	}

}
