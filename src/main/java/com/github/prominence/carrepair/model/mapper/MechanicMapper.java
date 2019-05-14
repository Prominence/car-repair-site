package com.github.prominence.carrepair.model.mapper;

import com.github.prominence.carrepair.model.domain.Mechanic;
import com.github.prominence.carrepair.model.dto.MechanicDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MechanicMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
    })
    MechanicDto mechanicToMechanicDto(Mechanic mechanic);

    @InheritInverseConfiguration
    Mechanic mechanicDtoToMechanic(MechanicDto mechanicDto);

    List<MechanicDto> mechanicsToMechanicDtoList(List<Mechanic> mechanics);
}
