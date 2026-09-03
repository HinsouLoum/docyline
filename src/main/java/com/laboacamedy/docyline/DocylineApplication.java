package com.laboacamedy.docyline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Point d'entree principal de l'application Labo Academy.
 * Cette application gere la vente de documents numeriques et la preparation
 * aux concours en ligne (cf. cahier des charges).
 */
@SpringBootApplication
public class DocylineApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocylineApplication.class, args);
	}

}
