package com.laterna.xaxathonprime.discipline;

import com.laterna.xaxathonprime.discipline.dto.DisciplineDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DisciplineMapper {
    DisciplineDto toDto(Discipline discipline);
    Discipline toEntity(DisciplineDto disciplineDto);
}
