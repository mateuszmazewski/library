package com.github.mateuszmazewski.library.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Start | Biblioteka")
@Route(value = "")
public class StartView extends VerticalLayout {

    public StartView() {
        UI.getCurrent().getPage().setLocation("/books");
    }

}
