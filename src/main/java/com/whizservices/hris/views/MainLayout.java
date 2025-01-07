package com.whizservices.hris.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;

import com.whizservices.hris.dtos.admin.UserDTO;
import com.whizservices.hris.services.admin.UserService;
import com.whizservices.hris.services.attendance.LeaveFilingService;
import com.whizservices.hris.utils.SecurityUtil;
import com.whizservices.hris.views.admin.DepartmentListView;
import com.whizservices.hris.views.admin.PositionListView;
import com.whizservices.hris.views.admin.UserListView;
import com.whizservices.hris.views.attendance.LeaveApprovalsListView;
import com.whizservices.hris.views.common.DashboardView;
import com.whizservices.hris.views.common.LeaveFilingView;
import com.whizservices.hris.views.compenben.*;
import com.whizservices.hris.views.common.EmployeeInfoView;
import com.whizservices.hris.views.attendance.TimesheetListView;
import com.whizservices.hris.views.profile.EmployeeDepartmentListView;
import com.whizservices.hris.views.profile.EmployeeListView;
import com.whizservices.hris.views.profile.EmployeePositionListView;

import jakarta.annotation.Resource;

import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {
    @Resource private final LeaveFilingService leaveFilingService;

    private UserDTO userDTO;
    private H1 viewTitle;

    public MainLayout(UserService userService,
                      LeaveFilingService leaveFilingService) {
        this.leaveFilingService = leaveFilingService;

        // Gets the user data transfer object based from the logged in user.
        if (SecurityUtil.getAuthenticatedUser() != null) {
            userDTO = userService.getByUsername(SecurityUtil.getAuthenticatedUser().getUsername());
        }

        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.Margin.NONE);
        viewTitle.getStyle().setWidth("50%");

        // This will show the logout button if the user is logged in the application.
        if (userDTO != null) {
            Button logoutButton = new Button("Logout", click -> SecurityUtil.logout());
            logoutButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            Span loggedUserSpan = new Span("Welcome " + userDTO.getEmployeeDTO().getFirstName().concat("!"));
            loggedUserSpan.getStyle().set("padding-right", "10px");

            Div logInfoDiv = new Div(loggedUserSpan, logoutButton);

            VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.add(logInfoDiv);
            verticalLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.END, logInfoDiv);

            addToNavbar(true, toggle, viewTitle, verticalLayout);
        } else {
            addToNavbar(true, toggle, viewTitle);
        }
    }

    private void addDrawerContent() {
        Span appName = new Span("WHIZ Services HRIS");
        appName.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(this.createNavigationLayout());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createProfileNavigation() {
        SideNav nav = new SideNav();

        nav.addItem(new SideNavItem("My Dashboard", DashboardView.class, LineAwesomeIcon.CHART_BAR_SOLID.create()));
        nav.addItem(new SideNavItem("My Profile", EmployeeInfoView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
        nav.addItem(new SideNavItem("My Leave Filings", LeaveFilingView.class, LineAwesomeIcon.DOOR_OPEN_SOLID.create()));

        return nav;
    }

    private SideNav createEmployeeNavigation() {
        SideNav nav = new SideNav();
        if (userDTO.getRole().equals("ROLE_ADMIN") ||
                userDTO.getRole().equals("ROLE_HR_MANAGER") ||
                userDTO.getRole().equals("ROLE_HR_SUPERVISOR") ||
                userDTO.getRole().equals("ROLE_HR_EMPLOYEE")) {
            nav.setLabel("Employee Details");

            nav.addItem(new SideNavItem("Employees", EmployeeListView.class, LineAwesomeIcon.ID_BADGE_SOLID.create()));
            nav.addItem(new SideNavItem("Employee Position", EmployeePositionListView.class, LineAwesomeIcon.USER_CHECK_SOLID.create()));
            nav.addItem(new SideNavItem("Employee Department", EmployeeDepartmentListView.class, LineAwesomeIcon.USER_CIRCLE_SOLID.create()));
        }

        return nav;
    }

    private SideNav createAttendanceNavigation() {
        SideNav nav = new SideNav();

        if (!userDTO.getRole().equals("ROLE_EMPLOYEE")) {
            nav.setLabel("Attendance");
        }

        if (userDTO.getRole().equals("ROLE_ADMIN") ||
                userDTO.getRole().equals("ROLE_HR_MANAGER") ||
                userDTO.getRole().equals("ROLE_HR_SUPERVISOR") ||
                userDTO.getRole().equals("ROLE_HR_EMPLOYEE")) {
            nav.addItem(new SideNavItem("Timesheets", TimesheetListView.class, LineAwesomeIcon.CALENDAR_WEEK_SOLID.create()));
        }

        if (userDTO.getRole().equals("ROLE_ADMIN") ||
                userDTO.getRole().equals("ROLE_HR_MANAGER") ||
                userDTO.getRole().equals("ROLE_HR_SUPERVISOR") ||
                userDTO.getRole().equals("ROLE_MANAGER") ||
                userDTO.getRole().equals("ROLE_SUPERVISOR")) {
            // Get the count of pending leaves to approved. Check every 5 seconds.
            int pendingLeaveCounts = leaveFilingService.getByLeaveStatusAndAssignedApproverEmployeeDTO("PENDING", userDTO.getEmployeeDTO()).size();

            // Show a notification badge that displays the count of leaves to be approved.
            Span counter = new Span(String.valueOf(pendingLeaveCounts));
            counter.getElement().getThemeList().add("badge pill medium error primary");
            counter.getStyle().set("margin-inline-start", "var(--lumo-space-s)");

            // Create the navigation item for leave approvals.
            SideNavItem leaveApprovalNavItem = new SideNavItem("Leave Approvals", LeaveApprovalsListView.class, LineAwesomeIcon.CALENDAR_CHECK.create());
            if (pendingLeaveCounts > 0) leaveApprovalNavItem.setSuffixComponent(counter);

            nav.addItem(leaveApprovalNavItem);
        }

        return nav;
    }

    private SideNav createCompenbenNavigation() {
        SideNav nav = new SideNav();

        if (userDTO.getRole().equals("ROLE_ADMIN") ||
                userDTO.getRole().equals("ROLE_HR_MANAGER") ||
                userDTO.getRole().equals("ROLE_HR_SUPERVISOR")) {
            nav.setLabel("Compensation and Benefits");
            nav.addItem(new SideNavItem("Rates", RatesListView.class, LineAwesomeIcon.MONEY_CHECK_SOLID.create()));
            nav.addItem(new SideNavItem("Allowances", AllowanceListView.class, LineAwesomeIcon.COINS_SOLID.create()));
            nav.addItem(new SideNavItem("Government Contributions", GovernmentContributionsListView.class, LineAwesomeIcon.HAND_HOLDING_USD_SOLID.create()));
            nav.addItem(new SideNavItem("Loan Deductions", LoanDeductionListView.class, LineAwesomeIcon.MONEY_BILL_WAVE_SOLID.create()));
            nav.addItem(new SideNavItem("Leave Benefits", LeaveBenefitsListView.class, LineAwesomeIcon.DOOR_OPEN_SOLID.create()));
        }

        return nav;
    }

    private SideNav createAdminNavigation() {
        SideNav nav = new SideNav();

        if (userDTO.getRole().equals("ROLE_ADMIN") ||
                userDTO.getRole().equals("ROLE_HR_MANAGER") ||
                userDTO.getRole().equals("ROLE_HR_SUPERVISOR")) {
            nav.setLabel("Administration");

            nav.addItem(new SideNavItem("Positions", PositionListView.class, LineAwesomeIcon.SITEMAP_SOLID.create()));
            nav.addItem(new SideNavItem("Departments", DepartmentListView.class, LineAwesomeIcon.BUILDING_SOLID.create()));
        }

        if (userDTO.getRole().equals("ROLE_ADMIN") ||
                userDTO.getRole().equals("ROLE_HR_MANAGER")) {
            nav.addItem(new SideNavItem("Users", UserListView.class, LineAwesomeIcon.USER_LOCK_SOLID.create()));
        }

        return nav;
    }

    private VerticalLayout createNavigationLayout() {
        VerticalLayout navigationLayout = new VerticalLayout();

        navigationLayout.add(this.createProfileNavigation(),
                             this.createEmployeeNavigation(),
                             this.createAttendanceNavigation(),
                             this.createCompenbenNavigation(),
                             this.createAdminNavigation());
        navigationLayout.setSpacing(true);
        navigationLayout.setSizeUndefined();

        this.createProfileNavigation().setWidthFull();
        this.createEmployeeNavigation().setWidthFull();
        this.createAttendanceNavigation().setWidthFull();
        this.createCompenbenNavigation().setWidthFull();
        this.createAdminNavigation().setWidthFull();

        return navigationLayout;
    }

    private Footer createFooter() {
        return new Footer();
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
