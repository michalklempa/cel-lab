package com.michalklempa.playground;

import com.google.protobuf.DescriptorProtos.FileDescriptorSet;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.TypeRegistry;
import com.vaadin.flow.spring.annotation.UIScope;
import dev.cel.common.CelDescriptorUtil;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Consumer;

@Component
@UIScope
public class MessageService {

    public static class LoadResult {
        public boolean success;
        public String message;
        public List<String> names;

        public static LoadResult failure(String message) {
            return new LoadResult(false, message, null);
        }

        public static LoadResult success(List<String> names) {
            return new LoadResult(true, "", names);
        }

        public LoadResult(boolean success, String message, List<String> names) {
            this.success = success;
            this.message = message;
            this.names = names;
        }
    }

    public static class SelectResult  {
        public boolean success;
        public Descriptor descriptor;
        public TypeRegistry typeRegistry;

        public SelectResult(boolean success, Descriptor descriptor, TypeRegistry typeRegistry) {
            this.success = success;
            this.descriptor = descriptor;
            this.typeRegistry = typeRegistry;
        }
    }
    private final Map<String, Descriptor> messageTypes = new LinkedHashMap<>();
    private TypeRegistry typeRegistry = TypeRegistry.getEmptyTypeRegistry();
    private Descriptor selected;
    public final List<Consumer<SelectResult>> onSelectListeners = new ArrayList<>();
    public final List<Consumer<LoadResult>> onLoadListeners = new ArrayList<>();

    MessageService() {
    }

    public void load(byte[] bytes) {
        messageTypes.clear();
        selected = null;

        FileDescriptorSet set;
        LoadResult loadResult;
        try {
            set = FileDescriptorSet.parseFrom(bytes);
            var files = CelDescriptorUtil.getFileDescriptorsFromFileDescriptorSet(set);
            for (FileDescriptor file : files) {
                for (Descriptor message : file.getMessageTypes()) {
                    collectMessages(message);
                }
            }
            if (messageTypes.isEmpty()) {
                loadResult = LoadResult.failure("No message defition found.");
            } else {
                typeRegistry = TypeRegistry.newBuilder().add(messageTypes.values()).build();
                List<String> names = new ArrayList<>(messageTypes.keySet());
                Collections.sort(names);
                loadResult = LoadResult.success(names);
            }
        } catch (InvalidProtocolBufferException e) {
            loadResult = LoadResult.failure(e.getMessage());
        }

        for (Consumer<LoadResult> consumer : onLoadListeners) {
            consumer.accept(loadResult);
        }
    }

    private void collectMessages(Descriptor message) {
        messageTypes.put(message.getFullName(), message);
        for (Descriptor nested : message.getNestedTypes()) {
            collectMessages(nested);
        }
    }

    public void select(String fullName) {
        SelectResult selectResult;
        if (fullName == null) {
             selectResult = new SelectResult(false, null, null);
        } else {
            selected = messageTypes.get(fullName);
            selectResult = new SelectResult(true, selected, typeRegistry);
        }
        for (Consumer<SelectResult> consumer : onSelectListeners) {
            consumer.accept(selectResult);
        }
    }
}
