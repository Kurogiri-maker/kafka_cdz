package com.example.demo.repository;

import com.example.demo.model.AdditionalAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdditionalAttributeRepository extends JpaRepository<AdditionalAttribute,Long> {
    void deleteAllByAttributeAndType(String attribute, String type);
}
