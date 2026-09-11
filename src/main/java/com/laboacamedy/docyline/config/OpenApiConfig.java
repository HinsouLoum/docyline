package com.laboacamedy.docyline.config;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * Configuration de la documentation Swagger/OpenAPI.
 * Declare le schema de securite JWT afin que le bouton "Authorize" apparaisse
 * dans Swagger UI et permette de tester les routes protegees avec un token.
 */
@Configuration
public class OpenApiConfig {

    // Nom de reference du schema de securite, reutilise plus bas
    private static final String SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI openApiConfiguration() {
        return new OpenAPI()
                .info(new Info()
                        .title("Labo Academy - API")
                        .description("API de vente de documents et de preparation aux concours en ligne")
                        .version("1.0.0"))
                // Applique le schema de securite a TOUTES les routes par defaut
                // (Swagger ajoutera quand meme le header Authorization uniquement quand on clique "Authorize")
                .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SCHEME_NAME, new SecurityScheme()
                                .name(SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                // Description affichee a cote du champ dans la popup "Authorize"
                                .description("Entrez uniquement le token JWT, sans le mot 'Bearer' devant.")
                        ));
    }
}
