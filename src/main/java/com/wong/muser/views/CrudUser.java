package com.wong.muser.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dnd.GridDragEndEvent;
import com.vaadin.flow.component.grid.dnd.GridDragStartEvent;
import com.vaadin.flow.component.grid.dnd.GridDropEvent;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Binder.Binding;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.wong.muser.entity.Application;
import com.wong.muser.entity.Company;
import com.wong.muser.entity.Department;
import com.wong.muser.entity.Employee;
import com.wong.muser.entity.SysRole;
import com.wong.muser.entity.SysUserApp;
import com.wong.muser.entity.SysUserCompany;
import com.wong.muser.entity.User;
import com.wong.muser.repository.ApplicationRepository;
import com.wong.muser.repository.CompanyDepartmentRepository;
import com.wong.muser.repository.CompanyRepository;
import com.wong.muser.repository.DepartmentRepository;
import com.wong.muser.repository.EmployeeRepository;
import com.wong.muser.repository.RoleRepository;
import com.wong.muser.repository.SysUserAppRepository;
import com.wong.muser.repository.SysUserCompanyRepository;
import com.wong.muser.repository.UserRepository;

@Route(value = "crudUser", layout = Main.class)
public class CrudUser extends VerticalLayout {
  private static final long serialVersionUID = -6096628320176534729L;

  private UserRepository repository;
  private RoleRepository roleRepo;
  private CompanyRepository companyRepo;
  private ApplicationRepository ApplicationRepo;
  private SysUserCompanyRepository userCompanyRepo;
  private SysUserAppRepository sysUserAppRepo;
  private EmployeeRepository employeeRepo;
  private DepartmentRepository departmentRepo;
  private CompanyDepartmentRepository companyDepartmentRepo;

  VaadinSession session;
  User user;
  Employee employee;
  Company company;

  Binder<Employee> employeeBinder = new Binder<>(Employee.class);
  Binder<User> userBinder = new Binder<>(User.class);

  Button save = new Button("Guardar");
  Button reset = new Button("Limpiar");
  Button back = new Button("Regresar", VaadinIcon.ARROW_LEFT.create());
  Button remove = new Button("Eliminar");

  private Collection<Company> draggedItems;
  private Grid<Company> dragSource;

  Grid<Company> gridOrigin = new Grid<>(Company.class);
  Grid<Company> gridDest = new Grid<>(Company.class);

  private Collection<Application> draggedItems2;
  private Grid<Application> dragSource2;

  Grid<Application> gridOrigin2 = new Grid<>(Application.class);
  Grid<Application> gridDest2 = new Grid<>(Application.class);

  String codeFunction = "FUNC";
  String codeFuncReq = "UREQ";

  List<Department> departmentList = new ArrayList<Department>();
  ComboBox<Department> cBoxDepartment = new ComboBox<Department>("Departamento");

  public CrudUser(UserRepository repository, RoleRepository roleRepo, CompanyRepository companyRepo,
      SysUserCompanyRepository userCompanyRepo, EmployeeRepository employeeRepo, SysUserAppRepository sysUserAppRepo,
      ApplicationRepository ApplicationRepo, DepartmentRepository departmentRepo,
      CompanyDepartmentRepository companyDepartmentRepo) {

    this.setSizeFull();
    this.repository = repository;
    this.roleRepo = roleRepo;
    this.companyRepo = companyRepo;
    this.userCompanyRepo = userCompanyRepo;
    this.employeeRepo = employeeRepo;
    this.sysUserAppRepo = sysUserAppRepo;
    this.ApplicationRepo = ApplicationRepo;
    this.departmentRepo = departmentRepo;
    this.companyDepartmentRepo = companyDepartmentRepo;

    session = VaadinSession.getCurrent();

    if (session != null) {
      company = (Company) session.getAttribute("company");
      user = (User) session.getAttribute("object");
      if (user != null) {
        reset.setEnabled(false);
        employee = user.getEmployee();
      } else {
        user = new User();
        employee = new Employee();
      }
      createForm();
    }
    this.getStyle().set("align-items", "center");
  }

  private void createForm() {

    FormLayout providerForm = new FormLayout();
    providerForm.setResponsiveSteps(new ResponsiveStep("15em", 1), new ResponsiveStep("20em", 2),
        new ResponsiveStep("25em", 4));
    providerForm.setWidth("50%");

    TextField firstName = new TextField("Nombre");
    firstName.focus();
    TextField lastName = new TextField("Apellido");
    TextField phone = new TextField("Telefono");
    DatePicker startDate = new DatePicker("Alta");
    DatePicker endDate = new DatePicker("Baja");
    TextField email = new TextField("Correo");
    TextField userName = new TextField("Usuario");
    PasswordField password = new PasswordField("Contraseña");
    password.setVisible(user.getId() == null);

    List<SysRole> folioList = roleRepo.findAll();
    ComboBox<SysRole> role = new ComboBox<SysRole>("Rol");
    role.setItemLabelGenerator(SysRole::getName);
    role.setItems(folioList);
    role.setValue(folioList.get(0));

    List<Company> companyList = companyRepo.findAll();
    ComboBox<Company> cBoxCompany = new ComboBox<Company>("Empresa");
    cBoxCompany.setItemLabelGenerator(Company::getName);
    cBoxCompany.setItems(companyList);
    cBoxCompany.setValue(companyList.get(0));
    cBoxCompany.addValueChangeListener(event -> {
      departmentList = companyDepartmentRepo.findByCompanyOrderByDepartmentNameAsc(cBoxCompany.getValue());
      cBoxDepartment.setItems(departmentList);
      cBoxDepartment.setValue(departmentList.size() > 0 ? departmentList.get(0) : null);
    });

    departmentList = companyDepartmentRepo.findByCompanyOrderByDepartmentNameAsc(cBoxCompany.getValue());
    cBoxDepartment.setItems(departmentList);
    cBoxDepartment.setValue(departmentList.size() > 0 ? departmentList.get(0) : null);
    cBoxDepartment.setItemLabelGenerator(element -> {
      return element.getName();
    });

    Checkbox cheq = new Checkbox("Activo");
    cheq.getElement().setAttribute("theme", "small");

    Component compGrid = createCompanyGrid();
    Component compGridApp = createAplicationGrid();

    providerForm.add(firstName);
    providerForm.add(lastName);
    providerForm.add(phone);
    providerForm.add(email);
    providerForm.add(cBoxCompany);
    providerForm.add(startDate);
    providerForm.add(endDate);
    providerForm.add(cBoxDepartment);
    providerForm.add(role);
    providerForm.add(userName);
    providerForm.add(password);
    providerForm.add(cheq);
    providerForm.add(compGrid);
    providerForm.add(compGridApp);

    providerForm.setColspan(firstName, 2);
    providerForm.setColspan(lastName, 2);
    providerForm.setColspan(phone, 2);
    providerForm.setColspan(email, 2);
    providerForm.setColspan(cBoxCompany, 4);
    providerForm.setColspan(startDate, 2);
    providerForm.setColspan(endDate, 2);
    providerForm.setColspan(userName, 2);
    providerForm.setColspan(password, 2);
    providerForm.setColspan(role, 2);
    providerForm.setColspan(cBoxDepartment, 2);
    providerForm.setColspan(cheq, 2);
    providerForm.setColspan(compGrid, 4);
    providerForm.setColspan(compGridApp, 4);

    HorizontalLayout horizontal = new HorizontalLayout(providerForm);
    horizontal.getStyle().set("justify-content", "center");
    add(horizontal);

    applyStyle(phone);
    applyStyle(firstName);
    applyStyle(lastName);
    applyStyle(userName);
    applyStyle(password);
    applyStyle(email);
    applyStyle(role);
    applyStyle(startDate);
    applyStyle(endDate);
    applyStyle(cBoxDepartment);
    applyStyle(cBoxCompany);

    firstName.setRequiredIndicatorVisible(true);
    lastName.setRequiredIndicatorVisible(true);
    password.setRequiredIndicatorVisible(true);
    userName.setRequiredIndicatorVisible(true);
    email.setRequiredIndicatorVisible(true);
    cBoxCompany.setRequiredIndicatorVisible(true);
    cBoxDepartment.setRequiredIndicatorVisible(true);

    save.getStyle().set("marginRight", "10px");
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
    save.addClickListener(event -> {

      if (employeeBinder.writeBeanIfValid(employee)) {
        employee = employeeRepo.save(employee);
        user.setEmployee(employee);

        if (userBinder.writeBeanIfValid(user)) {
          User result = repository.save(user);
          if (result != null && result.getId() != null) {

            saveUserCompany(result);
            saveUserApplication(result);

            if (user.getId() == null) {
              clearAll();
            } else {
              UI.getCurrent().navigate(UserView.class);
            }
          }
        } else {
          userBinder.validate();
        }

      } else {
        employeeBinder.validate();
      }

    });

    reset.addThemeVariants(ButtonVariant.LUMO_SMALL);
    reset.addClickListener(event -> {
      clearAll();
    });

    remove.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
    remove.addClickListener(event -> {
      try {
        userCompanyRepo.deleteAll(userCompanyRepo.findByUser(user));
        sysUserAppRepo.deleteAll(sysUserAppRepo.findByUser(user));
        repository.delete(user);
        UI.getCurrent().navigate(UserView.class);
      } catch (DataIntegrityViolationException ex) {
        showAlert("No se pudo eliminar el usuario");
      }
    });

    back.addThemeVariants(ButtonVariant.LUMO_SMALL);
    back.addClickListener(event -> {
      UI.getCurrent().navigate(UserView.class);
    });

    // actions
    HorizontalLayout actions = new HorizontalLayout();
    actions.setWidth("45%");

    HorizontalLayout hLayout1 = new HorizontalLayout();
    hLayout1.getStyle().set("justify-content", "flex-start");
    hLayout1.setWidthFull();
    if (user.getId() != null) {
      hLayout1.add(remove);
    }

    HorizontalLayout hLayout2 = new HorizontalLayout();
    hLayout2.setWidthFull();
    hLayout2.getStyle().set("justify-content", "flex-end");
    hLayout2.getStyle().set("margin-right", "0px");
    hLayout2.add(back, reset, save);

    actions.add(hLayout1, hLayout2);

    this.add(actions);

    userBinder.forField(userName).withValidator(new StringLengthValidator("El usuario no puede estar vacío", 1, null))
        .bind(User::getUserName, User::setUserName);
    userBinder.forField(cheq).bind(User::getActive, User::setActive);

    employeeBinder.forField(firstName)
        .withValidator(new StringLengthValidator("El nombre no puede estar vacío", 1, null))
        .bind(Employee::getFirstName, Employee::setFirstName);
    employeeBinder.forField(lastName)
        .withValidator(new StringLengthValidator("El apellido no puede estar vacío", 1, null))
        .bind(Employee::getLastName, Employee::setLastName);
    employeeBinder.forField(email).withValidator(new StringLengthValidator("El correo no puede estar vacío", 1, null))
        .bind(Employee::getEmail, Employee::setEmail);
    employeeBinder.forField(startDate).bind(Employee::getInitialDate, Employee::setInitialDate);
    employeeBinder.forField(endDate).bind(Employee::getFinalDate, Employee::setFinalDate);
    employeeBinder.forField(cheq).bind(Employee::getActive, Employee::setActive);

    SerializablePredicate<String> phonePredicate = value -> !phone.getValue().trim().isEmpty();
    SerializablePredicate<Company> companyPredicate = value -> !(cBoxCompany.getValue() == null);
    SerializablePredicate<Department> departmentPredicate = value -> !(cBoxDepartment.getValue() == null);
    SerializablePredicate<SysRole> rolePredicate = value -> !(role.getValue() == null);

    Binding<Employee, String> phoneBinding = employeeBinder.forField(phone)
        .withValidator(phonePredicate, "El teléfono no puede estar vacío").bind(Employee::getPhone, Employee::setPhone);

    Binding<Employee, Company> companyBinding = employeeBinder.forField(cBoxCompany)
        .withValidator(companyPredicate, "La empresa no puede estar vacia")
        .bind(Employee::getCompany, Employee::setCompany);
    cBoxCompany.addValueChangeListener(event -> companyBinding.validate());

    Binding<Employee, Department> departmentBinding = employeeBinder.forField(cBoxDepartment)
        .withValidator(departmentPredicate, "El departamento no puede estar vacio")
        .bind(Employee::getDepartment, Employee::setDepartment);

    Binding<User, SysRole> roleBinding = userBinder.forField(role)
        .withValidator(rolePredicate, "El rol no puede estar vacio").bind(User::getRole, User::setRole);

    phone.addValueChangeListener(event -> phoneBinding.validate());
    cBoxCompany.addValueChangeListener(event -> departmentBinding.validate());
    role.addValueChangeListener(event -> roleBinding.validate());

    if (user.getId() != null) {
      userBinder.readBean(user);
      employeeBinder.readBean(user.getEmployee());
    } else {
      cheq.setValue(true);
      user.setChangePassword(true);
      userBinder.forField(password)
          .withValidator(new StringLengthValidator("La contraseña no puede estar vacía", 1, null))
          .bind(User::getUserPass, User::setUserPass);
    }

  }

  private void saveUserCompany(User user) {

    // elimina los existente
    userCompanyRepo.deleteAll(userCompanyRepo.findByUser(user));

    // guarda las empresas asignadas
    @SuppressWarnings("unchecked")
    ListDataProvider<Company> listDataProvider = (ListDataProvider<Company>) gridDest.getDataProvider();
    List<Company> listCompany = new ArrayList<>(listDataProvider.getItems());

    // guarda la relacion usuario compañia
    List<SysUserCompany> listUserCompany = new ArrayList<SysUserCompany>();
    for (Company c : listCompany) {
      SysUserCompany useCompany = new SysUserCompany();
      useCompany.setCompany(c);
      useCompany.setUser(user);
      listUserCompany.add(useCompany);
    }

    userCompanyRepo.saveAll(listUserCompany);

  }

  private void saveUserApplication(User user) {

    // elimina los existente
    sysUserAppRepo.deleteAll(sysUserAppRepo.findByUser(user));

    // guarda las empresas asignadas
    @SuppressWarnings("unchecked")
    ListDataProvider<Application> listDataProvider = (ListDataProvider<Application>) gridDest2.getDataProvider();
    List<Application> listApplication = new ArrayList<>(listDataProvider.getItems());

    // guarda la relacion usuario aplicacion
    List<SysUserApp> listUserApplication = new ArrayList<SysUserApp>();
    for (Application c : listApplication) {
      SysUserApp useCompany = new SysUserApp();
      useCompany.setApplication(c);
      useCompany.setUser(user);
      listUserApplication.add(useCompany);
    }

    sysUserAppRepo.saveAll(listUserApplication);

  }

  private Component createCompanyGrid() {

    ComponentEventListener<GridDragStartEvent<Company>> dragStartListener = event -> {
      draggedItems = event.getDraggedItems();
      dragSource = event.getSource();
      gridOrigin.setDropMode(GridDropMode.BETWEEN);
      gridDest.setDropMode(GridDropMode.BETWEEN);
    };

    ComponentEventListener<GridDragEndEvent<Company>> dragEndListener = event -> {
      draggedItems = null;
      dragSource = null;
      gridOrigin.setDropMode(null);
      gridDest.setDropMode(null);
    };

    ComponentEventListener<GridDropEvent<Company>> dropListener = event -> {
      Optional<Company> target = event.getDropTargetItem();
      if (target.isPresent() && draggedItems.contains(target.get())) {
        return;
      }

      // Remove the items from the source grid
      @SuppressWarnings("unchecked")
      ListDataProvider<Company> sourceDataProvider = (ListDataProvider<Company>) dragSource.getDataProvider();
      List<Company> sourceItems = new ArrayList<>(sourceDataProvider.getItems());
      sourceItems.removeAll(draggedItems);
      dragSource.setItems(sourceItems);

      // Add dragged items to the target Grid
      Grid<Company> targetGrid = event.getSource();

      @SuppressWarnings("unchecked")
      ListDataProvider<Company> targetDataProvider = (ListDataProvider<Company>) targetGrid.getDataProvider();
      List<Company> targetItems = new ArrayList<>(targetDataProvider.getItems());

      int index = target
          .map(person -> targetItems.indexOf(person) + (event.getDropLocation() == GridDropLocation.BELOW ? 1 : 0))
          .orElse(0);
      targetItems.addAll(index, draggedItems);
      targetGrid.setItems(targetItems);
    };

    List<Integer> listIds = new ArrayList<Integer>();
    listIds.add(0);

    List<Company> listActualCompany = new ArrayList<Company>();

    List<SysUserCompany> listSysUserCompany = new ArrayList<SysUserCompany>();
    if (user.getId() != null) {
      listSysUserCompany = userCompanyRepo.findByUser(user);

      for (SysUserCompany c : listSysUserCompany) {
        listIds.add(c.getCompany().getId());
        listActualCompany.add(c.getCompany());
      }
    }
    List<Company> notCompanyUser = companyRepo.findDistinctByIdNotIn(listIds);

    gridOrigin.setItems(notCompanyUser);
    gridOrigin.setSelectionMode(Grid.SelectionMode.MULTI);
    gridOrigin.addThemeVariants(GridVariant.LUMO_COMPACT);
    gridOrigin.addDropListener(dropListener);
    gridOrigin.addDragStartListener(dragStartListener);
    gridOrigin.addDragEndListener(dragEndListener);
    gridOrigin.setRowsDraggable(true);
    gridOrigin.setColumns("name");
    gridOrigin.setHeight("300px");

    gridDest.setItems(listActualCompany);
    gridDest.setSelectionMode(Grid.SelectionMode.MULTI);
    gridDest.addThemeVariants(GridVariant.LUMO_COMPACT);
    gridDest.addDropListener(dropListener);
    gridDest.addDragStartListener(dragStartListener);
    gridDest.addDragEndListener(dragEndListener);
    gridDest.setRowsDraggable(true);
    gridDest.setColumns("name");
    gridDest.setHeight("300px");

    HorizontalLayout hztalGrid = new HorizontalLayout(gridOrigin, gridDest);
    hztalGrid.setSizeFull();
    VerticalLayout vertLayout = new VerticalLayout();
    vertLayout.add(new Label("Empresa a ingresar"));
    vertLayout.add(hztalGrid);

    return vertLayout;
  }

  public Component createAplicationGrid() {

    ComponentEventListener<GridDragStartEvent<Application>> dragStartListener = event -> {
      draggedItems2 = event.getDraggedItems();
      dragSource2 = event.getSource();
      gridOrigin2.setDropMode(GridDropMode.BETWEEN);
      gridDest2.setDropMode(GridDropMode.BETWEEN);
    };

    ComponentEventListener<GridDragEndEvent<Application>> dragEndListener = event -> {
      draggedItems2 = null;
      dragSource2 = null;
      gridOrigin2.setDropMode(null);
      gridDest2.setDropMode(null);
    };

    ComponentEventListener<GridDropEvent<Application>> dropListener = event -> {
      Optional<Application> target = event.getDropTargetItem();
      if (target.isPresent() && draggedItems2.contains(target.get())) {
        return;
      }

      // Remove the items from the source grid
      @SuppressWarnings("unchecked")
      ListDataProvider<Application> sourceDataProvider = (ListDataProvider<Application>) dragSource2.getDataProvider();
      List<Application> sourceItems = new ArrayList<>(sourceDataProvider.getItems());
      sourceItems.removeAll(draggedItems2);
      dragSource2.setItems(sourceItems);

      // Add dragged items to the target Grid
      Grid<Application> targetGrid = event.getSource();

      @SuppressWarnings("unchecked")
      ListDataProvider<Application> targetDataProvider = (ListDataProvider<Application>) targetGrid.getDataProvider();
      List<Application> targetItems = new ArrayList<>(targetDataProvider.getItems());

      int index = target
          .map(person -> targetItems.indexOf(person) + (event.getDropLocation() == GridDropLocation.BELOW ? 1 : 0))
          .orElse(0);
      targetItems.addAll(index, draggedItems2);
      targetGrid.setItems(targetItems);
    };

    List<Integer> listIds = new ArrayList<Integer>();
    listIds.add(0);

    List<Application> listActualApp = new ArrayList<Application>();

    List<SysUserApp> listSysUserCompany = new ArrayList<SysUserApp>();
    if (user.getId() != null) {
      listSysUserCompany = sysUserAppRepo.findByUser(user);

      for (SysUserApp c : listSysUserCompany) {
        listIds.add(c.getApplication().getId());
        listActualApp.add(c.getApplication());
      }
    }

    List<Application> notCompanyUser = ApplicationRepo.findDistinctByIdNotIn(listIds);

    gridOrigin2.setItems(notCompanyUser);
    gridOrigin2.setSelectionMode(Grid.SelectionMode.MULTI);
    gridOrigin2.addThemeVariants(GridVariant.LUMO_COMPACT);
    gridOrigin2.addDropListener(dropListener);
    gridOrigin2.addDragStartListener(dragStartListener);
    gridOrigin2.addDragEndListener(dragEndListener);
    gridOrigin2.setRowsDraggable(true);
    gridOrigin2.setColumns("name");
    gridOrigin2.setHeight("300px");

    gridDest2.setItems(listActualApp);
    gridDest2.setSelectionMode(Grid.SelectionMode.MULTI);
    gridDest2.addThemeVariants(GridVariant.LUMO_COMPACT);
    gridDest2.addDropListener(dropListener);
    gridDest2.addDragStartListener(dragStartListener);
    gridDest2.addDragEndListener(dragEndListener);
    gridDest2.setRowsDraggable(true);
    gridDest2.setColumns("name");
    gridDest2.setHeight("300px");

    HorizontalLayout hztalGrid = new HorizontalLayout(gridOrigin2, gridDest2);
    hztalGrid.setSizeFull();
    VerticalLayout vertLayout = new VerticalLayout();
    vertLayout.add(new Label("Aplicaciones a ingresar"));
    vertLayout.add(hztalGrid);

    return vertLayout;
  }

  private void clearAll() {
    userBinder.readBean(null);
    user = new User();
  }

  private void applyStyle(TextField textField) {
    textField.setValueChangeMode(ValueChangeMode.EAGER);
    textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
    textField.getStyle().set("margin", "0px");
    textField.setWidth("45%");

  }

  private void applyStyle(PasswordField passField) {
    passField.setValueChangeMode(ValueChangeMode.EAGER);
    passField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
    passField.getStyle().set("margin", "0px");
    passField.setWidth("45%");
  }

  private void applyStyle(ComboBox<?> cbox) {
    cbox.getStyle().set("margin", "0px");
    cbox.getElement().setAttribute("theme", "small");
    cbox.setWidth("45%");
  }

  private void applyStyle(DatePicker date) {
    date.getStyle().set("margin", "0px");
    date.getElement().setAttribute("theme", "small");
    date.setWidth("45%");
  }

  private void showAlert(String mensaje) {
    Notification notification = new Notification(mensaje);
    notification.setDuration(4000);
    notification.setPosition(Position.MIDDLE);
    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    notification.open();
  }
}
