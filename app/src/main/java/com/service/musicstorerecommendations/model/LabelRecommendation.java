package com.service.musicstorerecommendations.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "label_recommendation")
public class LabelRecommendation extends UserPreferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "label_recommendation_id")
    private long id;


    @NotNull(message = "Label ID is required")
    @Min(value = 1, message = "Please enter a valid Label ID number")
    @Column(name = "label_id")
    private long labelId;

    public LabelRecommendation(long userId, boolean liked, long id, long labelId) {
        super(userId, liked);
        this.id = id;
        this.labelId = labelId;
    }

    public LabelRecommendation() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLabelId() {
        return labelId;
    }

    public void setLabelId(long labelId) {
        this.labelId = labelId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LabelRecommendation that = (LabelRecommendation) o;
        return id == that.id && labelId == that.labelId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, labelId);
    }

    @Override
    public String toString() {
        return "LabelRecommendation{" +
                "id=" + id +
                ", labelId=" + labelId +
                "} " + super.toString();
    }
}
