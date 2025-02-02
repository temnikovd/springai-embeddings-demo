package dev.temnikov.aiembeddings;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AiEmbeddingsApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext run = new SpringApplicationBuilder(AiEmbeddingsApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
		run.getBean(EmbeddingsService.class).getSimilarProducts("5G Phone. IPS");
	}

}
