package com.laterna.xaxathonprime.discipline;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/disciplines")
@RequiredArgsConstructor
public class DisciplineController {
    private final DisciplineService disciplineService;

    @GetMapping
    public List<Discipline> getDisciplines() {
        return disciplineService.getDisciplines();
    }
}
