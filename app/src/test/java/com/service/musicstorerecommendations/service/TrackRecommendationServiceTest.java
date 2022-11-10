package com.service.musicstorerecommendations.service;

import com.service.musicstorerecommendations.model.ArtistRecommendation;
import com.service.musicstorerecommendations.model.TrackRecommendation;
import com.service.musicstorerecommendations.repository.TrackRecommendationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sound.midi.Track;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
public class TrackRecommendationServiceTest {
    private TrackRecommendationRepository trackRepositoryMock;

    private TrackRecommendationService service;


    private TrackRecommendation trackSetUp;
    private TrackRecommendation actualTrack;
    private TrackRecommendation editedTrack;

    private List<TrackRecommendation> tracksList;

    @Before
    public void setUp() throws Exception {

        setUpTrackRepository();

        service = new TrackRecommendationService(trackRepositoryMock);


    }

    @Test
    public void shouldReturnTrackById() throws Exception {

//        ACT
        actualTrack = service.getTrackRecommendationById(trackSetUp.getId());

        assertEquals(actualTrack, trackSetUp);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNoIdFound() throws Exception {

//        ACT
        actualTrack = service.getTrackRecommendationById(10);

        assertEquals(actualTrack, trackSetUp);
    }

    @Test
    public void shouldReturnNewTrack() throws Exception {

//        ACT
        // get it back out of the database
        TrackRecommendation trackPersistentOnDatabase = service.getTrackRecommendationById(trackSetUp.getId());

//        ASSERT
        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(trackSetUp, trackPersistentOnDatabase);

    }

    @Test
    public void shouldReturnNewTrackOnPostRequest() throws Exception {

//        ACT
        // get it back out of the database
        TrackRecommendation trackPersistentOnDatabase = service.createTrackRecommendation(trackSetUp);

//        ASSERT
        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(trackSetUp, trackPersistentOnDatabase);

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenTrackIsNull() throws Exception {

//        ARRANGE
        trackSetUp = null;

//        ACT
        actualTrack = service.createTrackRecommendation(trackSetUp);

        assertEquals(actualTrack, trackSetUp);
    }

    @Test
    public void shouldReturnAllTracks() throws Exception {

//        ACT
        // get it back out of the database
        List<TrackRecommendation> tracksPersistentOnDatabase = service.getAllTracksRecommendations();

//        ASSERT
        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(tracksList, tracksPersistentOnDatabase);

    }

    @Test
    public void shouldReturnEmptyListOfTracks() throws Exception {

//        ARRANGE

        tracksList = new ArrayList<>();

        doReturn(tracksList).when(trackRepositoryMock).findAll();
//        ACT
        // get it back out of the database
        List<TrackRecommendation> trackPersistentOnDatabase = service.getAllTracksRecommendations();


//        ASSERT
        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(tracksList, trackPersistentOnDatabase);


    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenUpdatingTrackThatIsNull() throws Exception {

//        ARRANGE
        trackSetUp = null;
//        ACT
        service.updateTrackRecommendationById(trackSetUp);

        assertEquals(actualTrack, trackSetUp);
    }

    @Test
    public void shouldUpdateATrack() throws Exception {

//        ACT
        service.updateTrackRecommendationById(trackSetUp);

//        ACT
        // get it back out of the database
        TrackRecommendation trackPersistentOnDatabase = service.getTrackRecommendationById(editedTrack.getId());

        assertEquals(trackSetUp, trackPersistentOnDatabase);
    }

    public void setUpTrackRepository() {
        trackRepositoryMock = mock(TrackRecommendationRepository.class);
        trackSetUp = new TrackRecommendation();
        trackSetUp.setLiked(true);
        trackSetUp.setTrackId(1);
        trackSetUp.setUserId(1);
        trackSetUp.setId(1);

        editedTrack = new TrackRecommendation();
        editedTrack.setLiked(false);
        editedTrack.setTrackId(1);
        editedTrack.setUserId(1);
        editedTrack.setId(1);

        doReturn(trackSetUp).when(trackRepositoryMock).save(trackSetUp);
        doReturn(editedTrack).when(trackRepositoryMock).save(editedTrack);
        doReturn(Optional.of(trackSetUp)).when(trackRepositoryMock).findById(1L);

        //        GET ALL
        TrackRecommendation track1 = new TrackRecommendation();

        track1.setLiked(false);
        track1.setTrackId(1);
        track1.setUserId(1);
        track1.setId(2);

        tracksList = new ArrayList<>();
        tracksList.add(trackSetUp);
        tracksList.add(track1);
        doReturn(tracksList).when(trackRepositoryMock).findAll();
    }
}
