package com.github.mateuszmazewski.library.views;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

@PageTitle("Logowanie | Biblioteka")
@Route(value = "login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    private final LoginForm loginForm;

    public LoginView() {
        loginForm = new LoginForm();
        loginForm.setForgotPasswordButtonVisible(false);

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.getForm().setTitle("Logowanie");
        i18n.getForm().setUsername("Nazwa użytkownika");
        i18n.getForm().setPassword("Hasło");
        i18n.getForm().setSubmit("Zaloguj się");
        i18n.getErrorMessage().setTitle("\u200e");
        i18n.getErrorMessage()
                .setMessage("Niepoprawny login lub hasło");
        loginForm.setI18n(i18n);

        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        loginForm.setAction("login"); //path, where loginForm should post to
        add(loginForm);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            loginForm.setError(true);
        }
    }
}
