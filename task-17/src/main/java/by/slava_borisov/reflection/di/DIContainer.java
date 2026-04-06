package by.slava_borisov.reflection.di;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class DIContainer {

    private final Map<Class<?>, Object> beans = new HashMap<>();

    public <T> void register(Class<T> clazz, T bean) {
        beans.put(clazz, bean);
    }

    public <T> T get(Class<T> clazz) {
        return (T) beans.get(clazz);
    }

    public void injectDependencies(Object target) throws IllegalAccessException {
        for (Field field : target.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                Class<?> type = field.getType();
                Object dependency = beans.get(type);
                if (dependency != null) {
                    field.set(target, dependency);
                } else {
                    throw new IllegalStateException("No bean registered for type: " + type);
                }
            }
        }
    }
}
