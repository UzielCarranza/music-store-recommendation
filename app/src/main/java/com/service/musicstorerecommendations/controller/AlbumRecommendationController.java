package com.service.musicstorerecommendations.controller;

import com.service.musicstorerecommendations.model.AlbumRecommendation;
import com.service.musicstorerecommendations.service.AlbumRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value = "album/recommendation")
public class AlbumRecommendationController {
    @Autowired
    private AlbumRecommendationService albumRecommendationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AlbumRecommendation create(@RequestBody @Valid AlbumRecommendation album) {
        return albumRecommendationService.createAlbum(album);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public AlbumRecommendation getById(@PathVariable long id) {
        return albumRecommendationService.getAlbumById(id);
    }



    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<AlbumRecommendation> getAllConsoles() {
        return albumRecommendationService.getAllAlbums();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAlbum(@RequestBody @Valid AlbumRecommendation album) {
        albumRecommendationService.updateAlbumById(album);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAlbum(@PathVariable("id") long id) {
        albumRecommendationService.deleteAlbumById(id);
    }

}
