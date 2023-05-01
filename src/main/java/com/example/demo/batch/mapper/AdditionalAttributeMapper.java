package com.example.demo.batch.mapper;

import com.example.demo.batch.dto.AdditionalAttributeDTO;
import com.example.demo.batch.model.AdditionalAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AdditionalAttributeMapper {

    AdditionalAttributeDTO toDto(AdditionalAttribute t);

    AdditionalAttribute toEntity(AdditionalAttributeDTO dto);

    AdditionalAttribute mapNonNullFields(AdditionalAttributeDTO dto, @MappingTarget AdditionalAttribute t);
}
