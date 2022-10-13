package org.example;

import lombok.Data;

@Data
public class A {
    @Autowired
    private String firstName;
    private String lastName;
}
