package com.service.musicstorerecommendations.service;

import com.service.musicstorerecommendations.model.AlbumRecommendation;
import com.service.musicstorerecommendations.repository.AlbumRecommendationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbumRecommendationService {
    private AlbumRecommendationRepository albumRecommendationRepository;

    public AlbumRecommendationService(AlbumRecommendationRepository albumRecommendationRepository) {
        this.albumRecommendationRepository = albumRecommendationRepository;
    }

    public AlbumRecommendation createAlbum(AlbumRecommendation album) {

        AlbumRecommendation albumRecommendation = new AlbumRecommendation();
//        code gather from SMU Challenge 5
        if (album == null) throw new IllegalArgumentException("No Album is passed! Album object is null!");
        return albumRecommendationRepository.save(albumRecommendation);
    }

    public AlbumRecommendation getAlbumById(long id) {
        return albumRecommendationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No Album found with the id: " + id));
    }

    public List<AlbumRecommendation> getAllAlbums() {
        return albumRecommendationRepository.findAll();
    }

    public void updateAlbumById(AlbumRecommendation album) {
        //        code gather from SMU Challenge 5
        if (album == null) {
            throw new IllegalArgumentException("No Album data is passed! Album object is null!");
        }
        AlbumRecommendation editedAlbum = albumRecommendationRepository.findById(album.getId()).orElseThrow(() -> new IllegalArgumentException("No Album found!"));

        editedAlbum.setAlbumId(album.getAlbumId());
        editedAlbum.setLiked(album.isLiked());
        editedAlbum.setUserId(album.getUserId());
        albumRecommendationRepository.save(editedAlbum);
    }

    public void deleteAlbumById(long id) {
        albumRecommendationRepository.deleteById(id);
    }

}
