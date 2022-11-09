package com.service.musicstorerecommendations.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;


@MappedSuperclass
 public abstract class UserPreferences {


    @NotNull(message = "User ID is required")
    @Min(value = 1, message = "Please enter a valid User ID number")
    @Column(name = "user_id")
    private long userId;


    @NotNull(message = "liked is required")
    private boolean liked;

    public UserPreferences(long userId, boolean liked) {
        this.userId = userId;
        this.liked = liked;
    }

    public UserPreferences() {
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPreferences that = (UserPreferences) o;
        return userId == that.userId && liked == that.liked;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, liked);
    }

    @Override
    public String toString() {
        return "UserPreferences{" +
                "userId=" + userId +
                ", liked=" + liked +
                '}';
    }
}
