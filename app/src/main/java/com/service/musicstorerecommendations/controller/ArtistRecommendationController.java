package com.service.musicstorerecommendations.controller;

import com.service.musicstorerecommendations.model.ArtistRecommendation;
import com.service.musicstorerecommendations.service.ArtistRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value = "artist/recommendation")
public class ArtistRecommendationController {


    @Autowired
    private ArtistRecommendationService artistService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArtistRecommendation createArtistRecommendation(@RequestBody @Valid ArtistRecommendation artist) {
        return artistService.createArtistRecommendation(artist);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ArtistRecommendation getById(@PathVariable long id) {
        return artistService.getArtistRecommendationById(id);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ArtistRecommendation> getAllArtistsRecommendations() {
        return artistService.getAllArtistsRecommendations();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateArtistRecommendation(@RequestBody @Valid ArtistRecommendation artist) {
        artistService.updateArtistRecommendationById(artist);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArtist(@PathVariable("id") long id) {
        artistService.deleteArtistRecommendationById(id);
    }
}
