package com.service.musicstorerecommendations.service;

import com.service.musicstorerecommendations.model.AlbumRecommendation;
import com.service.musicstorerecommendations.model.ArtistRecommendation;
import com.service.musicstorerecommendations.repository.ArtistRecommendationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
public class ArtistRecommendationServiceTest {


    private ArtistRecommendationRepository artistRepositoryMock;

    private ArtistRecommendationService service;


    private ArtistRecommendation artistSetUp;
    private ArtistRecommendation actualArtist;
    private ArtistRecommendation editedArtist;

    private List<ArtistRecommendation> artistList;

    @Before
    public void setUp() throws Exception {

        setUpArtistRepository();

        service = new ArtistRecommendationService(artistRepositoryMock);


    }

    @Test
    public void shouldReturnArtistById() throws Exception {

//        ACT
        actualArtist = service.getArtistRecommendationById(artistSetUp.getId());

        assertEquals(actualArtist, artistSetUp);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNoIdFound() throws Exception {

//        ACT
        actualArtist = service.getArtistRecommendationById(10);

        assertEquals(actualArtist, artistSetUp);
    }

    @Test
    public void shouldReturnNewArtist() throws Exception {

//        ACT
        // get it back out of the database
        ArtistRecommendation artistPersistentOnDatabase = service.getArtistRecommendationById(artistSetUp.getId());

//        ASSERT
        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(artistSetUp, artistPersistentOnDatabase);

    }

    @Test
    public void shouldReturnNewArtistOnPostRequest() throws Exception {

//        ACT
        // get it back out of the database
        ArtistRecommendation artistPersistentOnDatabase = service.createArtistRecommendation(artistSetUp);

//        ASSERT
        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(artistSetUp, artistPersistentOnDatabase);

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenArtistIsNull() throws Exception {

//        ARRANGE
        artistSetUp = null;

//        ACT
        actualArtist = service.createArtistRecommendation(artistSetUp);

        assertEquals(actualArtist, artistSetUp);
    }

    @Test
    public void shouldReturnAllArtist() throws Exception {

//        ACT
        // get it back out of the database
        List<ArtistRecommendation> artistsPersistentOnDatabase = service.getAllArtistsRecommendations();

//        ASSERT
        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(artistList, artistsPersistentOnDatabase);

    }

    @Test
    public void shouldReturnEmptyListOfAlbums() throws Exception {

//        ARRANGE

        artistList = new ArrayList<>();

        doReturn(artistList).when(artistRepositoryMock).findAll();
//        ACT
        // get it back out of the database
        List<ArtistRecommendation> artistPersistentOnDatabase = service.getAllArtistsRecommendations();


//        ASSERT
        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(artistList, artistPersistentOnDatabase);


    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenUpdatingAnAlbumThatIsNull() throws Exception {

//        ARRANGE
        artistSetUp = null;
//        ACT
        service.updateArtistRecommendationById(artistSetUp);

        assertEquals(actualArtist, artistSetUp);
    }

    @Test
    public void shouldUpdateAnArtist() throws Exception {

//        ACT
        service.updateArtistRecommendationById(artistSetUp);

//        ACT
        // get it back out of the database
        ArtistRecommendation artistPersistentOnDatabase = service.getArtistRecommendationById(editedArtist.getId());

        assertEquals(artistSetUp, artistPersistentOnDatabase);
    }

    public void setUpArtistRepository() {
        artistRepositoryMock = mock(ArtistRecommendationRepository.class);
        artistSetUp = new ArtistRecommendation();
        artistSetUp.setLiked(true);
        artistSetUp.setArtistId(1);
        artistSetUp.setUserId(1);
        artistSetUp.setId(1);

        editedArtist = new ArtistRecommendation();
        editedArtist.setLiked(false);
        editedArtist.setArtistId(1);
        editedArtist.setUserId(1);
        editedArtist.setId(1);

        doReturn(artistSetUp).when(artistRepositoryMock).save(artistSetUp);
        doReturn(editedArtist).when(artistRepositoryMock).save(editedArtist);
        doReturn(Optional.of(artistSetUp)).when(artistRepositoryMock).findById(1L);

        //        GET ALL
        ArtistRecommendation artist1 = new ArtistRecommendation();

        artist1.setUserId(1);
        artist1.setArtistId(1);
        artist1.setLiked(true);
        artist1.setId(2);

        artistList = new ArrayList<>();
        artistList.add(artistSetUp);
        artistList.add(artist1);
        doReturn(artistList).when(artistRepositoryMock).findAll();
    }
}
