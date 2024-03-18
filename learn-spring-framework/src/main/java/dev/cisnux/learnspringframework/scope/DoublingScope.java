package dev.cisnux.learnspringframework.scope;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.ArrayList;
import java.util.List;

public class DoublingScope implements Scope {
    private final List<Object> objects = new ArrayList<>();
    private long length = 0;


    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        if (objects.size() == 2) {
            final var index = (int) (length % 2);
            length++;
            return objects.get(index);
        }

        Object object = objectFactory.getObject();
        objects.add(object);
        length++;

        return object;
    }

    @Override
    public Object remove(String name) {
        return !objects.isEmpty() ? objects.removeFirst() : null;
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {

    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return null;
    }
}
