package com.service.musicstorerecommendations.controller;

import com.service.musicstorerecommendations.model.LabelRecommendation;
import com.service.musicstorerecommendations.service.LabelRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value = "label/recommendation")
public class LabelRecommendationController {



    @Autowired
    private LabelRecommendationService labelService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LabelRecommendation createLabel(@RequestBody @Valid LabelRecommendation label) {
        return labelService.createLabel(label);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public LabelRecommendation getById(@PathVariable long id) {
        return labelService.getLabelRecommendationById(id);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<LabelRecommendation> getAllLabelRecommendations() {
        return labelService.getAllLabelRecommendations();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateLabel(@RequestBody @Valid LabelRecommendation label) {
        labelService.updateLabelRecommendationById(label);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLabel(@PathVariable("id") long id) {
        labelService.deleteLabelRecommendationById(id);
    }

}
