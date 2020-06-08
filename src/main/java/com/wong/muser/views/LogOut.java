package com.wong.muser.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.wong.muser.backend.authentication.AccessControl;
import com.wong.muser.backend.authentication.AccessControlFactory;

@Route(value = "logout")
public class LogOut extends VerticalLayout {

  private static final long serialVersionUID = -284353664154687429L;

  public LogOut() {

    AccessControl accessControl = AccessControlFactory.getInstance().createAccessControl();

    if (accessControl.isUserSignedIn()) {
      accessControl.signOut();
      UI.getCurrent().navigate(Login.class);

    }

  }

}
