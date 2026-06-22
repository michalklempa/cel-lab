package com.michalklempa.playground.ui;

import com.michalklempa.base.ui.ViewTitle;
import com.michalklempa.playground.MessageService;
import com.michalklempa.playground.CelService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "")
@PageTitle("CEL Expression Evaluate Playground")
@Menu(order = 0, icon = "vaadin:code", title = "Playground")
class PlaygroundView extends VerticalLayout {

    PlaygroundView(MessageService messageService, CelService celService) {
        add(new ViewTitle("CEL Expression Evaluation Playground"));

        var message = new Message(messageService);
        var expression = new Expression(messageService, celService);
        var evaluation = new Evaluation(messageService, celService);

        var columns = new SplitLayout(expression, evaluation);
        columns.setOrientation(SplitLayout.Orientation.HORIZONTAL);
        columns.setWidthFull();

        var split = new VerticalLayout(message, columns);
        split.setWidthFull();
        split.setFlexGrow(1, columns);

        add(split);
        setFlexGrow(1, split);
    }
}
