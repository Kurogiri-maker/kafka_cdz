package com.example.demo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;

@Entity
@Data
public class Attribute {

    private Long id;
    private String name;
    private String value;

    @ManyToMany
    @JoinColumn(name = "document_id")
    private Document document;
}
