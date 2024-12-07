package com.laterna.xaxathonprime.discipline;

import com.laterna.xaxathonprime.discipline.dto.DisciplineDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DisciplineService {
    private final DisciplineRepository disciplineRepository;
    private final DisciplineMapper disciplineMapper;

    public Set<DisciplineDto> findAllById(Iterable<Long> ids) {
        return disciplineRepository.findAllById(ids)
                .stream()
                .map(disciplineMapper::toDto)
                .collect(Collectors.toSet());
    }

    public List<Discipline> getDisciplines() {
        return disciplineRepository.findAll();
    }
}
