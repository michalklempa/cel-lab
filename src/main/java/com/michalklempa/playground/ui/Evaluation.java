package com.michalklempa.playground.ui;

import com.michalklempa.playground.CelService;
import com.michalklempa.playground.MessageService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

class Evaluation extends VerticalLayout {

    Evaluation(MessageService messageService, CelService celService) {
        setSizeFull();
        setEnabled(false);

        celService.onCompileListeners.add(compilationResult -> setEnabled(compilationResult.success));

        TextArea protojson = new TextArea("Input ProtoJSON");
        protojson.setWidthFull();
        protojson.setValueChangeMode(ValueChangeMode.LAZY);
        protojson.addValueChangeListener(e -> celService.evaluate(e.getValue()));
        add(protojson);

        TextArea result = new TextArea("Evaluation Result");
        result.setWidthFull();
        result.setReadOnly(true);
        celService.onEvaluateListeners.add(evalutionResult -> result.setValue(evalutionResult.message));

        add(result);

        setFlexGrow(1, protojson);
    }
}
