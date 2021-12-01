package com.github.mateuszmazewski.library;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Theme(value = "library")
public class LibraryApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

}
