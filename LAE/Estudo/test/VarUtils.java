package pt.isel.test;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class VarUtils {
    public static <T> Map<String, Method> getPublicVarGettersOf(Class<T> clazz) {
        Map<String, Method> getters = new HashMap<>();
        Method[] methods = clazz.getMethods();
        for (var method : methods) { // Filters only public methods
            if(method.isAnnotationPresent(DontRead.class)) continue; // Filters methods with DontRead annotation
            //Filters methods that are var (have a getter and a setter with the same name)
            final String getterName = method.getName();
            if(!getterName.startsWith("get")) continue;
            final String setterName = "set" + getterName.substring(3); // Removes the "get" and adds "set"
            if (Arrays.stream(methods).noneMatch(m -> m.getName().equals(setterName))) continue; // If it doesn't have a setter, it's not a var
            // If it doesn't throw an exception, that means the setter exists
            getters.put(getterName.substring(3), method);
        }
        return getters;
    }
}
