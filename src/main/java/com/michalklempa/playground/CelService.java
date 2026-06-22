package com.michalklempa.playground;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.TypeRegistry;
import com.google.protobuf.util.JsonFormat;
import com.vaadin.flow.spring.annotation.UIScope;
import dev.cel.common.CelAbstractSyntaxTree;
import dev.cel.common.CelValidationException;
import dev.cel.common.types.SimpleType;
import dev.cel.common.types.StructTypeReference;
import dev.cel.compiler.CelCompiler;
import dev.cel.compiler.CelCompilerFactory;
import dev.cel.extensions.CelExtensions;
import dev.cel.parser.CelStandardMacro;
import dev.cel.runtime.CelEvaluationException;
import dev.cel.runtime.CelRuntime;
import dev.cel.runtime.CelRuntimeFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Component
@UIScope
public class CelService {

    public static class CompilationResult {
        public boolean success;
        public String message;

        public CompilationResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }

    public static class EvaluationResult {
        public boolean success;
        public String message;

        public EvaluationResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }
    private CelCompiler compiler;
    private CelRuntime runtime;
    private String lastExpression = "";
    private CelAbstractSyntaxTree lastAst;
    private Descriptor lastDescriptor;
    private TypeRegistry lastTypeRegistry;
    public final List<Consumer<CompilationResult>> onCompileListeners = new ArrayList<>();
    public final List<Consumer<EvaluationResult>> onEvaluateListeners = new ArrayList<>();

    CelService(MessageService messageService) {
        messageService.onSelectListeners.add(selectResult -> {
            lastAst = null;
            lastDescriptor = null;
            lastTypeRegistry = null;
            if (selectResult.success) {
                lastDescriptor = selectResult.descriptor;
                lastTypeRegistry = selectResult.typeRegistry;

                compiler = CelCompilerFactory.standardCelCompilerBuilder()
                        .setStandardMacros(CelStandardMacro.STANDARD_MACROS)
                        .addLibraries(CelExtensions.strings())
                        .addMessageTypes(lastDescriptor)
                        .addVar("event", StructTypeReference.create(lastDescriptor.getFullName()))
                        .setResultType(SimpleType.BOOL)
                        .build();

                runtime = CelRuntimeFactory.standardCelRuntimeBuilder()
                        .addLibraries(CelExtensions.strings())
                        .addMessageTypes(lastDescriptor)
                        .build();


                compile(lastExpression);
            } else {
                compile(null);
            }
        });
    }

    public void compile(String expression) {
        CompilationResult result;
        lastExpression = expression;
        if (expression == null) {
            result = new CompilationResult(false, "");
        } else {
            try {
                lastAst = compiler.compile(expression).getAst();
                result = new CompilationResult(true, "Compilation Success, AST resultType: " + lastAst.getResultType());
            } catch (CelValidationException e) {
                lastAst = null;
                result = new CompilationResult(false, e.getMessage());
            }
        }
        for (Consumer<CompilationResult> consumer : onCompileListeners) {
            consumer.accept(result);
        }
    }

    public void evaluate(String protoJson) {
        EvaluationResult evaluationResult;
        try {
            DynamicMessage.Builder event = DynamicMessage.newBuilder(lastDescriptor);
            JsonFormat.parser().usingTypeRegistry(lastTypeRegistry).ignoringUnknownFields().merge(protoJson, event);
            var program = runtime.createProgram(lastAst);
            var eval = program.eval(Map.of("event", event.build()));
            evaluationResult = new EvaluationResult(true, String.valueOf(eval));
        } catch (CelEvaluationException | InvalidProtocolBufferException e) {
            evaluationResult = new EvaluationResult(false, e.getMessage());
        }
        for (Consumer<EvaluationResult> consumer : onEvaluateListeners) {
            consumer.accept(evaluationResult);
        }
    }
}
