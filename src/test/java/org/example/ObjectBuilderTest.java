package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObjectBuilderTest {
    @Test
    void create1() {
        A a = ObjectBuilder.create(A.class);
        assertNotNull(a.getFirstName());        // Поле firstName, помеченное @Autowired, будет проинициализировано конструктором по умолчанию типа поля
        assertNull(a.getLastName());            // Поле lastName, не помеченное @Autowired, будет null.
    }

    @Test
    void create2() {
        B b = ObjectBuilder.create(B.class, "LastName", 28);
        assertNotNull(b.getFirstName());            // Поле firstName, помеченное @Autowired, будет проинициализировано конструктором по умолчанию типа поля
        assertEquals(b.getLastName(), "LastName");  // Поле lastName инициализируется через конструктор
        assertEquals(b.getAge(), 28);               // Поле age инициализируется через конструктор
    }
}