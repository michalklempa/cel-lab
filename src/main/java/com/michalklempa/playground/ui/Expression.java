package com.michalklempa.playground.ui;

import com.michalklempa.playground.CelService;
import com.michalklempa.playground.MessageService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.value.ValueChangeMode;

class Expression extends VerticalLayout {


    Expression(MessageService messageService, CelService celService) {
        setSizeFull();
        setEnabled(false);

        messageService.onSelectListeners.add(selectResult -> setEnabled(selectResult.success));

        TextArea expression = new TextArea("Expression");
        expression.setWidthFull();
        expression.setValueChangeMode(ValueChangeMode.LAZY);
        expression.addValueChangeListener(e -> celService.compile(expression.getValue()));
        add(expression);

        TextArea compilation = new TextArea("Compilation result");
        compilation.setWidthFull();
        compilation.setReadOnly(true);
        celService.onCompileListeners.add(compilationResult -> {
            compilation.setValue(compilationResult.message);
        });
        add(compilation);

        setFlexGrow(1, expression);
    }
}
