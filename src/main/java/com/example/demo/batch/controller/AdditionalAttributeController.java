package com.example.demo.batch.controller;

import com.example.demo.batch.mapper.AdditionalAttributeMapper;
import com.example.demo.batch.dto.AdditionalAttributeDTO;
import com.example.demo.batch.model.AdditionalAttribute;
import com.example.demo.batch.repository.AdditionalAttributeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/enrichissement")
@AllArgsConstructor
public class AdditionalAttributeController {

    @Autowired
    private AdditionalAttributeRepository repository;


    private AdditionalAttributeMapper mapper;

    @PostMapping
    public void addAttribute(@RequestBody AdditionalAttributeDTO dto){
        AdditionalAttribute attribute = mapper.toEntity(dto);
        this.repository.save(attribute);
    }
}
