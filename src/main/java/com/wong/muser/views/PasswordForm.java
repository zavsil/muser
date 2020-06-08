package com.wong.muser.views;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveEvent.ContinueNavigationAction;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.wong.muser.backend.authentication.PasswordTools;
import com.wong.muser.entity.Company;
import com.wong.muser.entity.User;
import com.wong.muser.repository.UserRepository;

@Route(value = "password", layout = Main.class)
public class PasswordForm extends VerticalLayout implements BeforeLeaveObserver {
  private static final long serialVersionUID = 5367497090837581272L;

  private UserRepository repository;

  VaadinSession session;

  Company company;

  private Binder<User> binder = new BeanValidationBinder<>(User.class);

  private User user;
  private User userSession;

  private PasswordField passFieldCurrentPassword;
  private PasswordField passFieldtxtNewPassword;
  private PasswordField passFieldtxtConfirmNewPassword;
  private Checkbox checkChangePassword;

  private Label lblStatus;
  private Button save;
  private Button cancel;

  private boolean newPasword = false;
  private boolean hasChanges = false;
  private boolean validateChange = false;

  Notification notificationError;

  private boolean visibleToAdmin = true;

  @Autowired
  public PasswordForm(UserRepository repository) {
    this.setSizeFull();
    this.repository = repository;

    session = VaadinSession.getCurrent();

    if (session != null) {
      Object obj = session.getAttribute("object");
      user = (User) obj;
      company = (Company) session.getAttribute("company");
      userSession = (User) session.getAttribute("userSession");

      if (user != null) {
        binder.setBean(user);

        if (user.getChangePassword() && user.getId() == userSession.getId()) {
          // validar la salida
          validateChange = true;
        }

      } else {
        user = new User();
      }

      if (userSession != null) {
        if ("ADMIN".equals(userSession.getRole().getCode())) {
          visibleToAdmin = false;
        }
      }

      add(createForm());
      add(buildButtons());

      // preparar los componentes de la forma
      Label content = new Label("La contraseña ingresada es incorrecta");
      content.getStyle().set("color", "red");
      notificationError = new Notification(content);
      notificationError.setDuration(3000);

      if (user.getId() == userSession.getId() && !visibleToAdmin) {
        checkChangePassword.setVisible(false);
        checkChangePassword.setValue(false);
      }

    }
  }

  private Component createForm() {
    VerticalLayout vForm = new VerticalLayout();
    vForm.getStyle().set("padding-right", "0px");
    vForm.getStyle().set("padding-left", "0px");

    passFieldCurrentPassword = new PasswordField("Contraseña Actual");
    passFieldCurrentPassword.focus();
    passFieldCurrentPassword.setWidthFull();
    passFieldCurrentPassword.setRequired(true);
    passFieldCurrentPassword.setValueChangeMode(ValueChangeMode.EAGER);
    passFieldCurrentPassword.setVisible(visibleToAdmin);
    passFieldCurrentPassword.addThemeVariants(TextFieldVariant.LUMO_SMALL);

    passFieldtxtNewPassword = new PasswordField("Contraseña Nueva");
    passFieldtxtNewPassword.setWidthFull();
    passFieldtxtNewPassword.setRequired(true);
    passFieldtxtNewPassword.setValueChangeMode(ValueChangeMode.EAGER);
    passFieldtxtNewPassword.addThemeVariants(TextFieldVariant.LUMO_SMALL);

    passFieldtxtConfirmNewPassword = new PasswordField("Confirmar Contraseña Nueva");
    passFieldtxtConfirmNewPassword.setWidthFull();
    passFieldtxtConfirmNewPassword.setRequired(true);
    passFieldtxtConfirmNewPassword.setValueChangeMode(ValueChangeMode.EAGER);
    passFieldtxtConfirmNewPassword.addThemeVariants(TextFieldVariant.LUMO_SMALL);

    passFieldtxtNewPassword.addValueChangeListener(event -> compareNewPassword());
    passFieldtxtConfirmNewPassword.addValueChangeListener(event -> compareNewPassword());

    checkChangePassword = new Checkbox("Cambiar contraseña");
    checkChangePassword.setVisible(!visibleToAdmin);
    checkChangePassword.setValue(true);
    checkChangePassword.getElement().setAttribute("theme", "small");

    lblStatus = new Label("");

    vForm.add(passFieldCurrentPassword);
    vForm.add(passFieldtxtNewPassword);
    vForm.add(passFieldtxtConfirmNewPassword);
    vForm.add(checkChangePassword);
    vForm.add(lblStatus);

    return vForm;
  }

  private Component buildButtons() {
    HorizontalLayout layout = new HorizontalLayout();
    layout.setWidthFull();
    layout.setPadding(false);
    layout.setJustifyContentMode(JustifyContentMode.END);

    cancel = new Button("Cancelar");
    cancel.addThemeVariants(ButtonVariant.LUMO_SMALL);
    cancel.setVisible(!visibleToAdmin);
    cancel.addClickListener(event -> doCancel());

    save = new Button("Aceptar");
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
    save.addClickListener(event -> doValidateSave());

    Div space = new Div();

    layout.add(space, cancel, save);

    layout.setFlexGrow(1, space);
    return layout;
  }

  private void compareNewPassword() {
    if (passFieldtxtNewPassword.getValue().trim().length() > 0
        && passFieldtxtConfirmNewPassword.getValue().trim().length() > 0) {
      if (passFieldtxtNewPassword.getValue().trim().equals(passFieldtxtConfirmNewPassword.getValue().trim())) {
        lblStatus.setText("contraseña nueva confirmada");
        lblStatus.getStyle().set("color", "green");
        newPasword = true;
      } else {
        lblStatus.setText("contraseña nueva no coincide con la confirmación");
        lblStatus.getStyle().set("color", "red");
        newPasword = false;
      }
    }
  }

  private void doValidateSave() {
    if (user != null && binder.writeBeanIfValid(user)) {
      if (passFieldCurrentPassword.isVisible()) {
        if (PasswordTools.verifyPassword(passFieldCurrentPassword.getValue().trim(), user.getUserPass())) {
          doSave();
        } else {
          notificationError.open();
          passFieldCurrentPassword.focus();
        }
      } else {
        doSave();
      }

    }
  }

  private void doSave() {
    if (newPasword) {
      hasChanges = true;
      Class<? extends Component> navigation = null;

      user.setChangePassword(checkChangePassword.getValue());
      navigation = UserView.class;

      user.setUserPass(passFieldtxtNewPassword.getValue().trim());
      repository.save(user);
      Notification.show("Registro Guardado", 3000, Position.BOTTOM_CENTER);
      closeForm(navigation);

    } else {
      passFieldtxtNewPassword.focus();
    }
  }

  private void doCancel() {
    Class<? extends Component> navigation = null;

    if (user.getId() == userSession.getId() && visibleToAdmin) {
      user.setChangePassword(false);
      // navigation = PurchaseView.class;
    } else {
      user.setChangePassword(checkChangePassword.getValue());
      navigation = UserView.class;
    }

    closeForm(navigation);
  }

  private void closeForm(Class<? extends Component> navigation) {
    UI.getCurrent().navigate(navigation);
  }

  @Override
  public void beforeLeave(BeforeLeaveEvent event) {
    ContinueNavigationAction action = event.postpone();

    if (validateChange) {
      if (!hasChanges) {

        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);

        Label message = new Label("Es necesario cambiar la contraseña.");

        Button btnOkay = new Button("Ok", ev -> {
          dialog.close();
        });
        btnOkay.addThemeVariants(ButtonVariant.LUMO_SMALL);
        btnOkay.setWidth("90px");

        HorizontalLayout hlyButtons = new HorizontalLayout(btnOkay);
        hlyButtons.setJustifyContentMode(JustifyContentMode.END);
        hlyButtons.setWidthFull();

        dialog.add(new VerticalLayout(message, hlyButtons));
        dialog.open();
      } else {
        action.proceed();
      }
    } else {
      action.proceed();

    }
  }

}
