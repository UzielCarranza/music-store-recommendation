package com.service.musicstorerecommendations.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "artist_recommendation")
public class ArtistRecommendation extends UserPreferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artist_recommendation_id")
    private long id;

    @NotNull(message = "Artist ID is required")
    @Min(value = 1, message = "Please enter a valid Artist ID number")
    @Column(name = "artist_id")
    private long artistId;

    public ArtistRecommendation() {
    }

    public ArtistRecommendation(long userId, boolean liked, long id, long artistId) {
        super(userId, liked);
        this.id = id;
        this.artistId = artistId;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ArtistRecommendation that = (ArtistRecommendation) o;
        return id == that.id && artistId == that.artistId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, artistId);
    }

    @Override
    public String toString() {
        return "ArtistRecommendation{" +
                "id=" + id +
                ", artistId=" + artistId +
                "} " + super.toString();
    }
}
