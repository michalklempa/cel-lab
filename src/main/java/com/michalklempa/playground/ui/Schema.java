package com.michalklempa.playground.ui;

import com.michalklempa.playground.MessageService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

class Schema extends VerticalLayout {

    Schema(MessageService messageService) {
        setWidthFull();
        setPadding(false);
        TextArea schema = new TextArea();

        var toggle = new Button();
        toggle.setIcon(VaadinIcon.CHEVRON_RIGHT.create());

        var bar = new HorizontalLayout(new Span("Schema"), toggle);
        bar.setAlignItems(FlexComponent.Alignment.CENTER);

        schema.setSizeFull();
        schema.setReadOnly(true);
        schema.getStyle().set("font-family", "monospace");

        var box = new Div(schema);
        box.setWidthFull();
        box.setVisible(false);
        box.getStyle()
                .set("height", "300px")
                .set("min-height", "100px")
                .set("resize", "vertical")
                .set("overflow", "auto");

        toggle.addClickListener(e -> {
            if (box.isVisible()) {
                box.setVisible(false);
                toggle.setIcon(VaadinIcon.CHEVRON_RIGHT.create());
            } else {
                box.setVisible(true);
                toggle.setIcon(VaadinIcon.CHEVRON_DOWN.create());
            }
        });
        add(bar);

        messageService.onSelectListeners.add(select -> {
            if (select.success) {
                schema.setValue(messageService.schema());
            } else {
                schema.clear();
            }
        });

        add(box);
        setFlexGrow(1, box);
    }
}
