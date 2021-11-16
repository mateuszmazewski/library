package com.github.mateuszmazewski.library.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H3 logo = new H3("Zarządzanie biblioteką");
        logo.addClassNames("text-l", "m-s"); //margin-small
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);

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
        RouterLink booksView = new RouterLink("Książki", BooksView.class);

        authorsView.setHighlightCondition(HighlightConditions.sameLocation());
        categoriesView.setHighlightCondition(HighlightConditions.sameLocation());
        publishersView.setHighlightCondition(HighlightConditions.sameLocation());
        booksView.setHighlightCondition(HighlightConditions.sameLocation());

        VerticalLayout viewsList = new VerticalLayout(authorsView, categoriesView, publishersView, booksView);
        addToDrawer(viewsList);
    }
}
