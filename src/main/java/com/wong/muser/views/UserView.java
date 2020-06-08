package com.wong.muser.views;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.wong.muser.entity.Company;
import com.wong.muser.entity.User;
import com.wong.muser.repository.UserRepository;

@Route(value = "user", layout = Main.class)
public class UserView extends Div {
  private static final long serialVersionUID = -3547432450960213298L;

  TextField textBusqueda;
  Grid<User> grid;
  Label totalElemtns = new Label();

  Company company;
  User userSession;

  UserRepository repository;

  VaadinSession session;

  @Autowired
  public UserView(UserRepository repository) {
    this.repository = repository;
    session = VaadinSession.getCurrent();
    if (session != null) {
      userSession = (User) session.getAttribute("user");
      company = (Company) session.getAttribute("company");
    }

    this.add(createBar());
    this.add(creteGrid());
    this.setHeight("90%");
  }

  public Component createBar() {

    textBusqueda = new TextField();
    textBusqueda.setPlaceholder("Filtro por...");
    textBusqueda.addThemeVariants(TextFieldVariant.LUMO_SMALL);
    textBusqueda.setAutoselect(true);
    textBusqueda.setClearButtonVisible(true);
    textBusqueda.setWidth("50%");
    textBusqueda.addValueChangeListener(event -> {
      applyFilter();
    });

    HorizontalLayout hBar1 = new HorizontalLayout();
    hBar1.setWidthFull();
    hBar1.getStyle().set("justify-content", "flex-start");
    hBar1.getStyle().set("padding", "10px");
    hBar1.add(textBusqueda);

    Button bAdd = new Button(VaadinIcon.PLUS.create());
    bAdd.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
    bAdd.addClickListener(click -> {
      session.setAttribute("object", null);
      UI.getCurrent().navigate(CrudUser.class);
    });

    HorizontalLayout hBar2 = new HorizontalLayout();
    hBar2.setWidthFull();
    hBar2.getStyle().set("justify-content", "flex-end");
    hBar2.getStyle().set("padding", "10px");
    hBar2.add(bAdd);

    return new HorizontalLayout(hBar1, hBar2);
  }

  public Component creteGrid() {

    ListDataProvider<User> dataProvider = new ListDataProvider<>(repository.findAllOrderByUserNameAsc());
    totalElemtns.setText("Total: " + dataProvider.getItems().size() + " Usuarios");

    grid = new Grid<>();
    grid.setHeightFull();
    grid.setHeightByRows(false);
    grid.setDataProvider(dataProvider);
    grid.addThemeVariants(GridVariant.LUMO_COMPACT);

    grid.addComponentColumn(User -> {
      Button boton = new Button(User.getUserName());
      boton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
      boton.addClickListener(click -> {
        editElement(User);
      });

      return boton;
    }).setHeader("Usuario").setFooter(totalElemtns).setFlexGrow(4);

    grid.addColumn(User -> {
      return User.getRole().getName();
    }, "Rol").setHeader("Rol").setFlexGrow(2);

    grid.addComponentColumn(User -> {

      Button boton = new Button(VaadinIcon.KEY_O.create());
      boton.addThemeVariants(ButtonVariant.LUMO_SMALL);
      boton.getStyle().set("paddin-left", "0px");
      boton.addClickListener(click -> {
        editPassword(User);
      });

      return boton;

    }).setFlexGrow(0).setWidth("80px").setTextAlign(ColumnTextAlign.CENTER);

    grid.addComponentColumn(User -> {
      Icon i = new Icon(VaadinIcon.CHECK_CIRCLE);
      i.getStyle().set("color", "#0580FB");

      if (!User.getActive()) {
        i = new Icon(VaadinIcon.CHECK_CIRCLE_O);
      }
      return i;

    }).setHeader("Activo").setFlexGrow(0).setWidth("80px").setTextAlign(ColumnTextAlign.CENTER);

    return grid;
  }

  public void editElement(User user) {
    session.setAttribute("object", user);
    session.setAttribute("company", company);
    UI.getCurrent().navigate(CrudUser.class);
  }

  public void editPassword(User user) {
    session.setAttribute("object", user);
    session.setAttribute("company", company);
    session.setAttribute("userSession", userSession);
    UI.getCurrent().navigate(PasswordForm.class);
  }

  private void applyFilter() {
    List<User> result = repository.findAllByFilter(textBusqueda.getValue().trim().toLowerCase());
    ListDataProvider<User> listProvider = new ListDataProvider<>(result);
    grid.setDataProvider(listProvider);

    totalElemtns.setText("Total: " + result.size() + " Proveedores");
  }

}
