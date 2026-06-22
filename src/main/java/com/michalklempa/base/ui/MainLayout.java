package com.michalklempa.base.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.page.ColorScheme;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;

@Layout
public final class MainLayout extends AppLayout {

    private boolean darkMode = false;

    MainLayout() {
        setPrimarySection(Section.DRAWER);
        addToDrawer(createApplicationHeader(), createApplicationDrawer(), createApplicationFooter());
    }

    private Component createApplicationHeader() {
        var themeToggle = new Button(VaadinIcon.MOON.create()) {
            public void update(boolean darkMode) {
                this.setIcon(darkMode ? VaadinIcon.SUN_O.create() : VaadinIcon.MOON.create());
                UI.getCurrent().getPage().setColorScheme(darkMode ? ColorScheme.Value.DARK : ColorScheme.Value.LIGHT);
            }
        };
        themeToggle.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        themeToggle.setAriaLabel("Toggle dark mode");
        themeToggle.addClickListener(e -> {
            darkMode = !darkMode;
            themeToggle.update(darkMode);
            WebStorage.setItem("dark-mode", String.valueOf(darkMode));
        });

        WebStorage.getItem("dark-mode", value -> {
            darkMode = "true".equals(value);
            themeToggle.update(darkMode);
        });

        var appLogo = new Avatar("Cel Lab");
        appLogo.addClassName("app-logo");
        appLogo.addThemeVariants(AvatarVariant.AURA_FILLED, AvatarVariant.XSMALL);

        var appName = new Span("Cel Lab");
        appName.addClassName("app-name");

        var header = new HorizontalLayout(themeToggle, appLogo, appName);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setPadding(true);
        return header;
    }

    private Component createApplicationDrawer() {
        var scroller = new Scroller(createSideNav());
        scroller.addThemeVariants(ScrollerVariant.OVERFLOW_INDICATORS);
        return scroller;
    }

    private Component createApplicationFooter() {
        var footer = new VerticalLayout(new Span("Made with ❤️ with Vaadin"));
        footer.setAlignItems(FlexComponent.Alignment.CENTER);
        footer.addClassName("app-footer");
        return footer;
    }

    private SideNav createSideNav() {
        var nav = new SideNav();
        nav.setMinWidth(200, Unit.PIXELS);
        MenuConfiguration.getMenuEntries().forEach(entry -> nav.addItem(createSideNavItem(entry)));
        return nav;
    }

    private SideNavItem createSideNavItem(MenuEntry menuEntry) {
        if (menuEntry.icon() != null) {
            Component icon = menuEntry.icon().contains(".svg")
                    ? new SvgIcon(menuEntry.icon())
                    : new Icon(menuEntry.icon());
            return new SideNavItem(menuEntry.title(), menuEntry.path(), icon);
        }
        return new SideNavItem(menuEntry.title(), menuEntry.path());
    }
}
