package com.example.demo.batch.repository;

import com.example.demo.batch.model.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {


    List<Document> findAllByType(String type);

}
