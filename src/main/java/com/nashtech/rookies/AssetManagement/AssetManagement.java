package com.nashtech.rookies.AssetManagement;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Time;
import java.util.TimeZone;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Java1",
                description = "" +
                        "",
                contact = @Contact(
                        name = "",
                        url = "",
                        email = ""
                ),
                license = @License(
                        name = "MIT Licence",
                        url = "https://github.com/thombergs/code-examples/blob/master/LICENSE")),
        servers = @Server(url = "https://assetmanagement-api.azurewebsites.net"),
        security = {@SecurityRequirement(name = "bearerToken")}

)
@SecuritySchemes({
        @SecurityScheme(
                name = "bearerToken",
                type = SecuritySchemeType.HTTP,
                scheme = "bearer",
                bearerFormat = "JWT"
        )
})
public class AssetManagement {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Monterrey"));
        SpringApplication.run(AssetManagement.class, args);
    }

}
