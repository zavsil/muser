package com.wong.muser;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.wong.muser.backend.authentication.AccessControl;
import com.wong.muser.backend.authentication.AccessControlFactory;
import com.wong.muser.views.Login;

public class MainInitListener implements VaadinServiceInitListener {
  private static final long serialVersionUID = -7985090833092112279L;

  @Override
  public void serviceInit(ServiceInitEvent event) {
    final AccessControl accessControl = AccessControlFactory.getInstance().createAccessControl();

    event.getSource().addUIInitListener(uiInitEvent -> {
      uiInitEvent.getUI().addBeforeEnterListener(enterEvent -> {
        if (!accessControl.isUserSignedIn() && !Login.class.equals(enterEvent.getNavigationTarget())) {
          enterEvent.rerouteTo(Login.class);
        }
      });
    });
  }

}