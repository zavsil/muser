package com.wong.muser.views;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinSession;
import com.wong.muser.entity.Company;
import com.wong.muser.entity.User;

@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes, viewport-fit=cover")
@PWA(name = "Quiz", shortName = "Quiz", startPath = "login", backgroundColor = "#0066ff", themeColor = "#0066ff", offlinePath = "offline-page.html", offlineResources = {
    "images/offline-login-banner.jpg" })
public class Main extends AppLayout {

  private static final long serialVersionUID = -1315147161569149717L;

  private final Tabs menu;

  VaadinSession session;
  static User usuario;
  static Company company;

  static String mOrden = "mOrden";
  static String mProveedor = "mProveedor";
  static String mProyectos = "mProyectos";
  static String mUsuarios = "mUsuarios";
  static String mDepartamentos = "mDepartamentos";

  public Main() {

    session = VaadinSession.getCurrent();

    String image = "";

    if (session != null) {
      usuario = (User) session.getAttribute("user");
      company = (Company) session.getAttribute("company");

      if (company != null) {
        image = company.getImage();
      }
    }

    this.setPrimarySection(AppLayout.Section.DRAWER);

    Image img = new Image("frontend/images/logos/" + image, "logo");
    img.setHeight("44px");
    img.getStyle().set("margin-left", "10px");
    this.addToNavbar(img);

    menu = createMenuTabs();

    HorizontalLayout h = new HorizontalLayout(menu);
    h.setSizeFull();
    h.getStyle().set("justify-content", "center");
    this.addToNavbar(true, h);

  }

  private static Tabs createMenuTabs() {

    final Tabs tabs = new Tabs();
    tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
    tabs.add(getAvailableTabs());
    tabs.addThemeVariants(TabsVariant.LUMO_SMALL);
    return tabs;

  }

  private static Tab[] getAvailableTabs() {
    final List<Tab> tabs = new ArrayList<>(3);
    try {

      tabs.add(createTab(VaadinIcon.COMPILE, "Usuarios", UserView.class));

      tabs.add(createTab(VaadinIcon.SIGN_OUT, "Salir", LogOut.class));

    } catch (Exception ex) {
      UI.getCurrent().navigate(LogOut.class);
    }

    return tabs.toArray(new Tab[tabs.size()]);
  }

  private static Tab createTab(VaadinIcon icon, String title, Class<? extends Component> viewClass) {
    return createTab(populateLink(new RouterLink(null, viewClass), icon, title));
  }

  private static <T extends HasComponents> T populateLink(T a, VaadinIcon icon, String title) {
    Icon i = icon.create();
    i.setSize("20px");
    a.add(i);
    a.add(title);
    return a;
  }

  private static Tab createTab(Component content) {
    final Tab tab = new Tab();
    tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
    tab.add(content);
    return tab;
  }

}
