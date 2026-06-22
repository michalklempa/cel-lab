package com.michalklempa.playground.ui;

import com.michalklempa.playground.CelService;
import com.michalklempa.playground.MessageService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

class Evaluation extends VerticalLayout {

    Evaluation(MessageService messageService, CelService celService) {
        setWidthFull();
        setEnabled(false);

        TextArea result = new TextArea("Evaluation Result");
        result.setWidthFull();
        result.setReadOnly(true);
        celService.onEvaluateListeners.add(evalutionResult -> result.setValue(evalutionResult.message));

        TextArea protojson = new TextArea("Input ProtoJSON");
        protojson.setWidthFull();
        protojson.setValueChangeMode(ValueChangeMode.LAZY);
        protojson.addValueChangeListener(e -> celService.evaluate(e.getValue()));

        celService.onCompileListeners.add(compilationResult -> {setEnabled(compilationResult.success);
        if (compilationResult.success) {
            celService.evaluate(protojson.getValue());
        }
        });

        add(result);
        add(protojson);

        setFlexGrow(1, protojson);
    }
}
