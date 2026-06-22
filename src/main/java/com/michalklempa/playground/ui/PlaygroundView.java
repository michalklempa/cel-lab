package com.michalklempa.playground.ui;

import com.michalklempa.base.ui.ViewTitle;
import com.michalklempa.playground.MessageService;
import com.michalklempa.playground.CelService;
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
        setSizeFull();
        add(new ViewTitle("CEL Expression Evaluation Playground"));

        var message = new Message(messageService);
        var exp = new Expression(messageService, celService);
        var evaluation = new Evaluation(messageService, celService);

        var columns = new SplitLayout(exp, evaluation);
        columns.setOrientation(SplitLayout.Orientation.HORIZONTAL);
        columns.setSizeFull();

        add(message, columns);
    }
}
