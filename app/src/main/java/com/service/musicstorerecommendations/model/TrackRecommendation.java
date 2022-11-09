package com.service.musicstorerecommendations.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "track_recommendation")
public class TrackRecommendation extends UserPreferences{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "track_recommendation_id")
    private long id;


    @NotNull(message = "Label ID is required")
    @Min(value = 1, message = "Please enter a valid Track ID number")
    @Column(name = "track_id")
    private long trackId;

    public TrackRecommendation(long userId, boolean liked, long id, long trackId) {
        super(userId, liked);
        this.id = id;
        this.trackId = trackId;
    }

    public TrackRecommendation() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTrackId() {
        return trackId;
    }

    public void setTrackId(long trackId) {
        this.trackId = trackId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TrackRecommendation that = (TrackRecommendation) o;
        return id == that.id && trackId == that.trackId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, trackId);
    }

    @Override
    public String toString() {
        return "TrackRecommendation{" +
                "id=" + id +
                ", trackId=" + trackId +
                "} " + super.toString();
    }
}
