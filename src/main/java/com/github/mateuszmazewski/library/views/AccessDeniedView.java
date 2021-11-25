package com.github.mateuszmazewski.library.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Odmowa dostępu | Biblioteka")
@Route(value = "access-denied", layout = MainLayout.class)
public class AccessDeniedView extends VerticalLayout {

    public AccessDeniedView() {
        setSizeFull();
        add(new H1("Nie masz uprawnień do wyświetlenia tej zawartości."));
    }
}
