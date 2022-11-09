package com.service.musicstorerecommendations.controller;

import com.service.musicstorerecommendations.model.TrackRecommendation;
import com.service.musicstorerecommendations.service.TrackRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value = "track/recommendation")
public class TrackRecommendationController {
    @Autowired
    private TrackRecommendationService trackService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TrackRecommendation createTrack(@RequestBody @Valid TrackRecommendation track) {
        return trackService.createTrackRecommendation(track);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public TrackRecommendation getById(@PathVariable long id) {
        return trackService.getTrackRecommendationById(id);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<TrackRecommendation> getAllTracks() {
        return trackService.getAllTracksRecommendations();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTrack(@RequestBody @Valid TrackRecommendation track) {
        trackService.updateTrackRecommendationById(track);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrack(@PathVariable("id") long id) {
        trackService.deleteTrackRecommendationById(id);
    }

}
