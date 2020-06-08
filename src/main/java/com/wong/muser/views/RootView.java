package com.wong.muser.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route("")
@RouteAlias("/")
public class RootView extends Div {
  private static final long serialVersionUID = -8714936688757110014L;

  public RootView() {
    UI.getCurrent().navigate(Login.class);

  }

}
