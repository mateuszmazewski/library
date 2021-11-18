package com.github.mateuszmazewski.library.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Start | Biblioteka")
@Route(value = "")
public class StartView extends VerticalLayout {

    public StartView() {
        setSpacing(false);

        Button loginButton = new Button("Zaloguj się");
        loginButton.addClickListener(e -> UI.getCurrent().getPage().setLocation("/login"));

        add(
                new H1("Witaj"),
                new H2("Zaloguj się, aby rozpocząć"),
                loginButton
        );

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

}
