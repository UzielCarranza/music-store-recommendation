package com.service.musicstorerecommendations.service;

import com.service.musicstorerecommendations.model.TrackRecommendation;
import com.service.musicstorerecommendations.repository.TrackRecommendationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackRecommendationService {

    private TrackRecommendationRepository trackRecommendationRepository;

    public TrackRecommendationService(TrackRecommendationRepository trackRecommendationRepository) {
        this.trackRecommendationRepository = trackRecommendationRepository;
    }

    public TrackRecommendation createTrackRecommendation(TrackRecommendation track) {
        if (track == null) throw new IllegalArgumentException("No track is passed! track object is null!");
        return trackRecommendationRepository.save(track);
    }

    public TrackRecommendation getTrackRecommendationById(long id) {
        return trackRecommendationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No Recommendation with the id: " + id));
    }


    public List<TrackRecommendation> getAllTracksRecommendations() {
        return trackRecommendationRepository.findAll();
    }

    public void updateTrackRecommendationById(TrackRecommendation track) {

//        code gather from SMU Challenge 5
        if (track == null) {
            throw new IllegalArgumentException("No data is passed! Track object is null!");
        }
        TrackRecommendation editedtrack = trackRecommendationRepository.findById(track.getId()).orElseThrow(() -> new IllegalArgumentException("No Track found!"));
        editedtrack.setTrackId(track.getTrackId());
        editedtrack.setLiked(track.isLiked());
        editedtrack.setUserId(track.getUserId());
        trackRecommendationRepository.save(editedtrack);
    }

    public void deleteTrackRecommendationById(long id) {
        trackRecommendationRepository.deleteById(id);
    }
}
