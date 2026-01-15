package by.slava_borisov.di;

public class DIUtil {

    private static final DIContainer container = new DIContainer();

    public static <T> void register(Class<T> clazz, T bean) {
        container.register(clazz, bean);
    }

    public static <T> T get(Class<T> clazz) {
        return container.get(clazz);
    }

    public static void injectDependencies(Object target) throws IllegalAccessException {
        container.injectDependencies(target);
    }
}

