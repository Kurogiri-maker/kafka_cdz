package com.example.demo.mapper;

import com.example.demo.dto.AdditionalAttributeDTO;
import com.example.demo.model.AdditionalAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AdditionalAttributeMapper {

    AdditionalAttributeDTO toDto(AdditionalAttribute t);

    AdditionalAttribute toEntity(AdditionalAttributeDTO dto);

    AdditionalAttribute mapNonNullFields(AdditionalAttributeDTO dto, @MappingTarget AdditionalAttribute t);
}
