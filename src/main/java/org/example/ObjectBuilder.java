package org.example;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ObjectBuilder {
    /**
     * Создать объект любого типа.
     * <br/><br/>
     * У созданного объекта, поля, помеченные анотацией @Autowired, будут проинициализированы. Типы полей, к которым
     * применяется анотация @Autowired, должны обязательно иметь конструктор по умолчанию. В противном случае, инициализация поля
     * не будет осуществлена. Тип создаваемого объекта должен обязательно содержать сеттеры, поскольку нет другого
     * способа инициализировать приватные поля.
     *
     * @param type Тип объекта(описавается через объект типа Class)
     * @param args Опционально. Параметры конструктора создаваемого объекта
     * @return Новый объект заданного типа либо null
     * @param <T> Тип требуемого объекта
     */
    public static <T> T create(Class<T> type, Object...args) {

        try {

            //  Создаем новый объект заданного типа
            //  Следует помнить, что вызов конструктора без параметров и вызов конструктора с параметром(пусть даже с
            //  пустым массивом) - это разные конструкторы
            T obj;
            if (args.length == 0) {
                obj = type.getConstructor().newInstance();
            } else {

                //  Для создания объекта, нужно сначала получить его конструктор.
                //  Нужен именно тот конструктор, который соответствует по количеству и типу параметров, которые переданны через args
                //  Для этого нужно узнать РЕАЛЬНЫЙ тип каждого параметра, переданного в args

                Class<?>[] constructorParamType = new Class<?>[args.length];
                for (int i = 0; i <= args.length - 1; i++) {
                    constructorParamType[i] = args[i].getClass();
                    //  Параметры могут быть любого типа.
                    //  В массив же пападают объекты одного типа - Class. Хотя каждый из этих объектов может описывать разный тип аргументов.
                }

                //  Создаем объект, через вызов нужного конструктора
                obj = type.getConstructor(constructorParamType).newInstance(args);

            }

            //  Получаем массив всех полей из типа созданного объекта
            Field[] fields = type.getDeclaredFields();
            //  Ищем поля аннотированные @Autowired
            for (Field f: fields) {
                if (f.getAnnotation(Autowired.class) != null) {
                    //  Строим имя сеттера для поля
                    String setterName = "set" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
                    //  Получаем объект, описывающий сеттер
                    //  Второй параметр - это тип параметра, передаваемый в сеттер(описывает объект типа Class)
                    //  (Метод getMethod() может использоваться для получения не только сеттеров, но и абсолютно любых методов. А методы бывают
                    //  перегруженными. Соответственно, через второй параметр идет идентификация метода)
                    Method setter = type.getMethod(setterName, f.getType());
                    //  Запускаем сеттер, тем самым инициализируя поле
                    //  Второй параметр - создается новый объект, имеющий тип поля
                    setter.invoke(obj, f.getType().getConstructor().newInstance());
                }
            }
            return obj;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }

    }
}
