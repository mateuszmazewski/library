package com.github.mateuszmazewski.library.views;

import com.github.mateuszmazewski.library.security.SecurityService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {
    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H3 logo = new H3("Zarządzanie biblioteką");
        String username = SecurityService.getAuthenticatedUserUsername();
        H5 loggedInUser;

        logo.addClassNames("text-l", "m-s"); //margin-small

        Button logoutButton = new Button("Wyloguj się", e -> securityService.logout());

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);
        if (username != null && !username.isEmpty()) {
            loggedInUser = new H5("Zalogowano jako: " + username);
            header.add(loggedInUser);
        }
        header.add(logoutButton);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames("py-0", "px-m"); //padding x/y axis

        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink authorsView = new RouterLink("Autorzy", AuthorsView.class);
        RouterLink categoriesView = new RouterLink("Gatunki literackie", CategoriesView.class);
        RouterLink publishersView = new RouterLink("Wydawnictwa", PublishersView.class);
        RouterLink bookDefinitionsView = new RouterLink("Definicje książek", BookDefinitionsView.class);
        RouterLink booksView = new RouterLink("Książki", BooksView.class);
        RouterLink readersView = new RouterLink("Czytelnicy", ReadersView.class);
        RouterLink employeesView = new RouterLink("Pracownicy", EmployeesView.class);
        RouterLink usersView = new RouterLink("Użytkownicy", UsersView.class);
        RouterLink borrowsView = new RouterLink("Wypożyczenia", BorrowsView.class);

        authorsView.setHighlightCondition(HighlightConditions.sameLocation());
        categoriesView.setHighlightCondition(HighlightConditions.sameLocation());
        publishersView.setHighlightCondition(HighlightConditions.sameLocation());
        bookDefinitionsView.setHighlightCondition(HighlightConditions.sameLocation());
        booksView.setHighlightCondition(HighlightConditions.sameLocation());
        readersView.setHighlightCondition(HighlightConditions.sameLocation());
        employeesView.setHighlightCondition(HighlightConditions.sameLocation());
        usersView.setHighlightCondition(HighlightConditions.sameLocation());
        borrowsView.setHighlightCondition(HighlightConditions.sameLocation());

        VerticalLayout viewsList = new VerticalLayout(
                authorsView,
                categoriesView,
                publishersView,
                bookDefinitionsView,
                booksView,
                readersView,
                employeesView,
                usersView,
                borrowsView);
        addToDrawer(viewsList);
    }
}
