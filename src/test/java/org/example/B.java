package org.example;

import lombok.Data;

@Data
public class B {
    @Autowired
    private String firstName;

    private String lastName;
    private Integer age;

    public B(String lastName, Integer age) {
        this.lastName = lastName;
        this.age = age;
    }
}
