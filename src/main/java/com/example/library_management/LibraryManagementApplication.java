package com.example.library_management;

import java.awt.Desktop;
import java.net.URI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class LibraryManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryManagementApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void openBrowserAfterNetBeansRun(ApplicationReadyEvent event) {
        if (!Boolean.parseBoolean(event.getApplicationContext().getEnvironment().getProperty("library.open-browser", "true"))) {
            return;
        }

        String port = event.getApplicationContext().getEnvironment().getProperty(
                "local.server.port",
                event.getApplicationContext().getEnvironment().getProperty("server.port", "8080")
        );
        String address = "http://localhost:" + port + "/";
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(URI.create(address));
            } else if (System.getProperty("os.name").startsWith("Windows")) {
                new ProcessBuilder("rundll32.exe", "url.dll,FileProtocolHandler", address).start();
            } else {
                System.err.println("Could not open the browser automatically on this system: " + address);
            }
        } catch (Exception ex) {
            System.err.println("Could not open " + address + " automatically: " + ex.getMessage());
        }
    }
}