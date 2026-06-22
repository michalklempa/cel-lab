package com.michalklempa.playground.ui;

import com.michalklempa.playground.CelService;
import com.michalklempa.playground.MessageService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.value.ValueChangeMode;

class Expression extends VerticalLayout {


    Expression(MessageService messageService, CelService celService) {
        setWidthFull();
        setEnabled(false);

        messageService.onSelectListeners.add(selectResult -> setEnabled(selectResult.success));

        TextArea compilation = new TextArea("Compilation result");
        compilation.setWidthFull();
        compilation.setReadOnly(true);
        celService.onCompileListeners.add(compilationResult -> {
            compilation.setValue(compilationResult.message);
        });

        TextArea expression = new TextArea("Expression");
        expression.setWidthFull();
        expression.setValueChangeMode(ValueChangeMode.LAZY);
        expression.addValueChangeListener(e -> celService.compile(expression.getValue()));

        add(compilation);
        add(expression);

        setFlexGrow(1, expression);
    }
}
