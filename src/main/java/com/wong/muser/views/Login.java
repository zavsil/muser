package com.wong.muser.views;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.login.AbstractLogin.LoginEvent;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.VaadinSession;
import com.wong.muser.backend.authentication.AccessControl;
import com.wong.muser.backend.authentication.AccessControlFactory;
import com.wong.muser.backend.authentication.PasswordTools;
import com.wong.muser.entity.Company;
import com.wong.muser.entity.SysUserCompany;
import com.wong.muser.entity.User;
import com.wong.muser.repository.*;

@Route(value = "login")
public class Login extends VerticalLayout implements RouterLayout {
  private static final long serialVersionUID = 8296602279016116782L;

  @Autowired
  private CompanyRepository companyRepository;
  @Autowired
  private SysUserCompanyRepository SysUserCompanyRepository;

  private final LoginForm component;
  private AccessControl accessControl;

  VaadinSession session;

  User user;
  Company company;

  ComboBox<Company> companySelection;

  public Login(CompanyRepository companyRepository) {
    session = VaadinSession.getCurrent();

    this.companyRepository = companyRepository;

    accessControl = AccessControlFactory.getInstance().createAccessControl();

    getStyle().set("background", "url('frontend/images/desktop.jpg') no-repeat center");
    this.setSizeFull();
    this.getStyle().set("justify-content", "center");
    this.setAlignItems(Alignment.CENTER);

    component = new LoginForm();
    component.setI18n(createEspanolI18n());
    component.addLoginListener(e -> {

      boolean isAuthenticated = authenticate(e);
      if (isAuthenticated) {
        session.setAttribute("company", company);
        session.setAttribute("user", user);

        UI.getCurrent().navigate(UserView.class);

      } else {
        component.setError(true);
      }

    });

    component.setForgotPasswordButtonVisible(false);

    List<Company> lista = this.companyRepository.findByOrderByDisplayOrderAsc();

    companySelection = new ComboBox<Company>("Empresa");
    companySelection.setItemLabelGenerator(Company::getName);
    companySelection.setItems(lista);
    if (lista != null) {
      companySelection.setValue(lista.iterator().next());
    }
    companySelection.getElement().setAttribute("theme", "small");
    companySelection.getStyle().set("padding-right", "24px");
    companySelection.getStyle().set("padding-left", "24px");
    companySelection.getStyle().set("width", "-webkit-fill-available");

    companySelection.addValueChangeListener(event -> {
      if (event.getSource().isEmpty()) {
        companySelection.setValue(event.getOldValue());
      }
    });

    Label title = new Label("Users");
    title.getStyle().set("font-size", "larger");
    title.getStyle().set("font-family", "Arial");
    title.getStyle().set("color", "#808B96");

    VerticalLayout loginSection = new VerticalLayout();
    loginSection.getStyle().set("width", "");
    loginSection.getStyle().set("align-items", "center");

    loginSection.getStyle().set("background-color", "white");
    loginSection.getStyle().set("border-radius", "30px");

    loginSection.add(title);
    loginSection.add(companySelection);
    loginSection.add(component);

    add(loginSection);
  }

  private boolean authenticate(LoginEvent e) {
    boolean existe = false;
    company = companySelection.getValue();

    SysUserCompany result = SysUserCompanyRepository.findByUserUserNameAndUserUserPassAndUserActiveAndCompany(
        e.getUsername(), PasswordTools.generateSecurePassword(e.getPassword()), true, company);

    if (result != null) {
      user = result.getUser();

      if (user != null) {
        accessControl.signIn(e.getUsername(), e.getPassword());
        existe = true;
      }
    }
    return existe;
  }

  private LoginI18n createEspanolI18n() {
    final LoginI18n i18n = LoginI18n.createDefault();

    i18n.setHeader(new LoginI18n.Header());
    i18n.getForm().setUsername("Usuario");
    i18n.getForm().setSubmit("Iniciar sesión");
    i18n.getForm().setPassword("Contraseña");
    i18n.getErrorMessage().setMessage("Usuario o contraseña incorrecta.");
    return i18n;
  }

}
