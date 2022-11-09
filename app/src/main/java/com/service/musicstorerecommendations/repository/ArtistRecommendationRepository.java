package com.service.musicstorerecommendations.repository;

import com.service.musicstorerecommendations.model.ArtistRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;


@Service
public interface ArtistRecommendationRepository extends JpaRepository<ArtistRecommendation, Long> {
}
