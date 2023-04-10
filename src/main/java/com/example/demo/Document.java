package com.example.demo;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Map;

@Entity
@Data
@RequiredArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*
    private String name;
    private String siren;
    private String refMandat;
    private Map<String, String> additionalAttributes;

     */

    @JoinColumn(name = "attribute_id")
    private Attribute newAttribute;
}
