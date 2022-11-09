package com.service.musicstorerecommendations.service;

import com.service.musicstorerecommendations.model.ArtistRecommendation;
import com.service.musicstorerecommendations.repository.ArtistRecommendationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistRecommendationService {

    private ArtistRecommendationRepository artistRecommendationRepository;

    public ArtistRecommendationService(ArtistRecommendationRepository artistRecommendationRepository) {
        this.artistRecommendationRepository = artistRecommendationRepository;
    }

    public ArtistRecommendation createArtistRecommendation(ArtistRecommendation artist) {

        if (artist == null) throw new IllegalArgumentException("No Artist is passed! Artist object is null!");
        return artistRecommendationRepository.save(artist);
    }


    public ArtistRecommendation getArtistRecommendationById(long id) {
        return artistRecommendationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No Recommendation found with the id: " + id));
    }

    public List<ArtistRecommendation> getAllArtistsRecommendations() {
        return artistRecommendationRepository.findAll();
    }

    public void updateArtistRecommendationById(ArtistRecommendation artist) {

//        code gather from SMU Challenge 5
        if (artist == null) {
            throw new IllegalArgumentException("No data is passed! Artist object is null!");
        }
        ArtistRecommendation editedArtist = artistRecommendationRepository.findById(artist.getId()).orElseThrow(() -> new IllegalArgumentException("No Recommendation found!"));

        editedArtist.setArtistId(artist.getArtistId());
        editedArtist.setLiked(artist.isLiked());
        editedArtist.setUserId(artist.getUserId());
        artistRecommendationRepository.save(editedArtist);
    }


    public void deleteArtistRecommendationById(long id) {
        artistRecommendationRepository.deleteById(id);
    }
}
