package com.service.musicstorerecommendations.repository;

import com.service.musicstorerecommendations.model.TrackRecommendation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TrackRecommendationRepositoryTest {

    @Autowired
    private TrackRecommendationRepository trackRepository;


    private TrackRecommendation track;
    private TrackRecommendation editedTrack;

    @Before
    public void setUp() throws Exception {
        trackRepository.deleteAll();
        setUpTrackRepository();
    }

    //    CODE that Dan Mueller wrote during class on NOV, 03, 2022
    @Test
    public void shouldAddFindUpdateDeleteTrack() {

        // get it back out of the database
        TrackRecommendation trackPersistentOnDatabase = trackRepository.findById(track.getId()).get();

        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(track, trackPersistentOnDatabase);

//        update
        trackPersistentOnDatabase.setUserId(2);
        trackRepository.save(trackPersistentOnDatabase);
        assertEquals(trackPersistentOnDatabase, editedTrack);

        // delete it
        trackRepository.deleteById(track.getId());

//        // go try to get it again
        Optional<TrackRecommendation> artist3 = trackRepository.findById(track.getId());
//
//        // confirm that it's gone
        assertEquals(false, artist3.isPresent());
    }

    public void setUpTrackRepository() {
        track = new TrackRecommendation();
        track.setUserId(1);
        track.setTrackId(1);
        track.setLiked(true);
        trackRepository.save(track);

        editedTrack = new TrackRecommendation();
        editedTrack.setUserId(2);
        editedTrack.setTrackId(1);
        editedTrack.setLiked(true);
        editedTrack.setId(1);

    }
}
