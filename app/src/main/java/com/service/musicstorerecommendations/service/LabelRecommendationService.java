package com.service.musicstorerecommendations.service;

import com.service.musicstorerecommendations.model.LabelRecommendation;
import com.service.musicstorerecommendations.repository.LabelRecommendationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelRecommendationService {

    private LabelRecommendationRepository labelRecommendationRepository;

    public LabelRecommendationService(LabelRecommendationRepository labelRecommendationRepository) {
        this.labelRecommendationRepository = labelRecommendationRepository;
    }

    public LabelRecommendation createLabel(LabelRecommendation label) {
        if (label == null) throw new IllegalArgumentException("No label is passed! label object is null!");
        return labelRecommendationRepository.save(label);
    }

    public LabelRecommendation getLabelRecommendationById(long id) {
        return labelRecommendationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No Recommendation with the id: " + id));

    }

    public List<LabelRecommendation> getAllLabelRecommendations() {
        return labelRecommendationRepository.findAll();
    }

    public void updateLabelRecommendationById(LabelRecommendation label) {

//        code gather from SMU Challenge 5
        if (label == null) {
            throw new IllegalArgumentException("No data is passed! Label object is null!");
        }
        LabelRecommendation editedLabel = labelRecommendationRepository.findById(label.getId()).orElseThrow(() -> new IllegalArgumentException("No Label found!"));
        editedLabel.setLabelId(label.getLabelId());
        editedLabel.setLiked(label.isLiked());
        editedLabel.setUserId(label.getUserId());
        labelRecommendationRepository.save(editedLabel);
    }


    public void deleteLabelRecommendationById(long id) {
        labelRecommendationRepository.deleteById(id);
    }
}
