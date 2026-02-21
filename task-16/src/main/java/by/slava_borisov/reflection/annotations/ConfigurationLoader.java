package by.slava_borisov.reflection.annotations;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class ConfigurationLoader {

    public static void loadConfiguration(Object configObject) throws Exception {
        Class<?> clazz = configObject.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(ConfigProperty.class)) {
                continue;
            }

            ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);

            String fileName = annotation.configFileName();
            String propertyName = annotation.propertyName();
            PropertyType type = annotation.type();

            if (propertyName.isEmpty()) {
                propertyName = clazz.getSimpleName() + "." + field.getName();
            }

            if (type == PropertyType.AUTO) {
                type = determineType(field.getType());
            }

            String value = loadProperty(fileName, propertyName);
            if (value != null) {
                setFieldValue(configObject, field, value, type);
            }
        }
    }

    private static String loadProperty(String fileName, String propertyName) {
        try (InputStream input = ConfigurationLoader.class
                .getClassLoader()
                .getResourceAsStream(fileName)) {
            if (input == null) {
                return null;
            }
            Properties properties = new Properties();
            properties.load(input);
            return properties.getProperty(propertyName);
        } catch (Exception e) {
            return null;
        }
    }

    private static PropertyType determineType(Class<?> fieldType) {
        if (fieldType == int.class || fieldType == Integer.class) {
            return PropertyType.INTEGER;
        } else if (fieldType == boolean.class || fieldType == Boolean.class) {
            return PropertyType.BOOLEAN;
        } else if (fieldType == double.class || fieldType == Double.class) {
            return PropertyType.DOUBLE;
        } else if (fieldType == long.class || fieldType == Long.class) {
            return PropertyType.LONG;
        }
        return PropertyType.STRING;
    }

    private static void setFieldValue(Object obj, Field field, String value, PropertyType type)
            throws IllegalAccessException {

        field.setAccessible(true);
        Class<?> fieldType = field.getType();

        if (fieldType.isArray()) {
            setArrayField(obj, field, value);
            return;
        }
        if (Collection.class.isAssignableFrom(fieldType)) {
            setCollectionField(obj, field, value);
            return;
        }

        switch (type) {
            case INTEGER -> field.set(obj, Integer.parseInt(value));
            case BOOLEAN -> field.set(obj, Boolean.parseBoolean(value));
            case DOUBLE -> field.set(obj, Double.parseDouble(value));
            case LONG -> field.set(obj, Long.parseLong(value));
            case STRING, AUTO -> field.set(obj, value);
        }
    }

    private static void setArrayField(Object obj, Field field, String value)
            throws IllegalAccessException {

        String[] parts = Arrays.stream(value.split(","))
                .map(String::trim)
                .toArray(String[]::new);

        Class<?> componentType = field.getType().getComponentType();

        if (componentType == String.class) {
            field.set(obj, parts);
        } else if (componentType == int.class) {
            int[] arr = new int[parts.length];
            for (int i = 0; i < parts.length; i++) {
                arr[i] = Integer.parseInt(parts[i]);
            }
            field.set(obj, arr);
        } else if (componentType == Integer.class) {
            Integer[] arr = new Integer[parts.length];
            for (int i = 0; i < parts.length; i++) {
                arr[i] = Integer.parseInt(parts[i]);
            }
            field.set(obj, arr);
        } else if (componentType == boolean.class) {
            boolean[] arr = new boolean[parts.length];
            for (int i = 0; i < parts.length; i++) {
                arr[i] = Boolean.parseBoolean(parts[i]);
            }
            field.set(obj, arr);
        } else if (componentType == Boolean.class) {
            Boolean[] arr = new Boolean[parts.length];
            for (int i = 0; i < parts.length; i++) {
                arr[i] = Boolean.parseBoolean(parts[i]);
            }
            field.set(obj, arr);
        } else {
            field.set(obj, parts);
        }
    }

    private static void setCollectionField(Object obj, Field field, String value)
            throws IllegalAccessException {

        String[] parts = Arrays.stream(value.split(","))
                .map(String::trim)
                .toArray(String[]::new);

        Class<?> elementType = String.class;
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType pType) {
            Type actual = pType.getActualTypeArguments()[0];
            if (actual instanceof Class<?> c) {
                elementType = c;
            }
        }

        List<Object> list = new ArrayList<>();

        if (elementType == Integer.class) {
            for (String part : parts) {
                list.add(Integer.parseInt(part));
            }
        } else if (elementType == Boolean.class) {
            for (String part : parts) {
                list.add(Boolean.parseBoolean(part));
            }
        } else {
            list.addAll(Arrays.asList(parts));
        }

        field.set(obj, list);
    }
}