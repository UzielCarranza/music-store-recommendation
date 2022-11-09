package com.service.musicstorerecommendations.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "album_recommendation")
public class AlbumRecommendation extends UserPreferences {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_recommendation_id")
    private long id;


    @NotNull(message = "Album ID is required")
    @Min(value = 1, message = "Please enter a valid album ID")
    @Column(name = "album_id")
    private long albumId;

    public AlbumRecommendation() {
    }

    public AlbumRecommendation(long id, long albumId, long userId, boolean liked) {
        super(userId, liked);
        this.id = id;
        this.albumId = albumId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AlbumRecommendation that = (AlbumRecommendation) o;
        return id == that.id && albumId == that.albumId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }


    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, albumId);
    }

    @Override
    public String toString() {
        return "AlbumRecommendation{" +
                "id=" + id +
                ", albumId=" + albumId +
                "} " + super.toString();
    }
}
