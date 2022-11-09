package com.service.musicstorerecommendations.service;

import com.service.musicstorerecommendations.model.AlbumRecommendation;
import com.service.musicstorerecommendations.repository.AlbumRecommendationRepository;
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
public class AlbumRecommendationServiceTest {


    private AlbumRecommendationRepository albumRepositoryMock;


    private AlbumRecommendationService service;

    private AlbumRecommendation albumSetUp;
    private AlbumRecommendation actualAlbum;
    private AlbumRecommendation editedAlbum;

    private List<AlbumRecommendation> albumList;


    @Before
    public void setUp() throws Exception {

        setUpAlbumRepository();

        service = new AlbumRecommendationService(albumRepositoryMock);


    }

    @Test
    public void shouldReturnAlbumById() throws Exception {

//        ACT
        actualAlbum = service.getAlbumById(albumSetUp.getId());

        assertEquals(actualAlbum, albumSetUp);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNoIdFound() throws Exception {

//        ACT
        actualAlbum = service.getAlbumById(10);

        assertEquals(actualAlbum, albumSetUp);
    }

    @Test
    public void shouldReturnNewAlbum() throws Exception {

//        ACT
        // get it back out of the database
        AlbumRecommendation albumPersistentOnDatabase = service.getAlbumById(albumSetUp.getId());

//        ASSERT
        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(albumSetUp, albumPersistentOnDatabase);

    }
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenAlbumIsNull() throws Exception {

//        ARRANGE
        albumSetUp = null;

//        ACT
        actualAlbum = service.createAlbum(albumSetUp);

        assertEquals(actualAlbum, albumSetUp);
    }
    @Test
    public void shouldReturnAllAlbums() throws Exception {

//        ACT
        // get it back out of the database
        List<AlbumRecommendation> albumsPersistentOnDatabase = service.getAllAlbums();

//        ASSERT
        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(albumList, albumsPersistentOnDatabase);

    }
    @Test
    public void shouldReturnEmptyListOfAlbums() throws Exception {

//        ARRANGE

        albumList = new ArrayList<>();

        doReturn(albumList).when(albumRepositoryMock).findAll();
//        ACT
        // get it back out of the database
        List<AlbumRecommendation> albumsPersistentOnDatabase = service.getAllAlbums();


//        ASSERT
        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(albumList, albumsPersistentOnDatabase);


    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenUpdatingAnAlbumThatIsNull() throws Exception {

//        ARRANGE
        albumSetUp = null;
//        ACT
        service.updateAlbumById(albumSetUp);

        assertEquals(actualAlbum, albumSetUp);
    }

    @Test
    public void shouldUpdateAnAlbum() throws Exception {

//        ACT
        service.updateAlbumById(albumSetUp);

//        ACT
        // get it back out of the database
        AlbumRecommendation albumPersistentOnDatabase = service.getAlbumById(editedAlbum.getId());

        assertEquals(albumSetUp, albumPersistentOnDatabase);
    }


    public void setUpAlbumRepository() {
        albumRepositoryMock = mock(AlbumRecommendationRepository.class);

        albumSetUp = new AlbumRecommendation();
        albumSetUp.setAlbumId(1);
        albumSetUp.setUserId(1);
        albumSetUp.setLiked(true);
        albumSetUp.setId(1);


        editedAlbum = new AlbumRecommendation();
        editedAlbum.setAlbumId(1);
        editedAlbum.setUserId(1);
        editedAlbum.setLiked(false);
        editedAlbum.setId(1);

        doReturn(editedAlbum).when(albumRepositoryMock).save(editedAlbum);
        doReturn(albumSetUp).when(albumRepositoryMock).save(albumSetUp);
        doReturn(Optional.of(albumSetUp)).when(albumRepositoryMock).findById(1L);

//        GET ALL
        AlbumRecommendation album1 = new AlbumRecommendation();

        editedAlbum.setAlbumId(1);
        editedAlbum.setUserId(1);
        editedAlbum.setLiked(true);
        album1.setId(2);

        albumList = new ArrayList<>();
        albumList.add(albumSetUp);
        albumList.add(album1);
        doReturn(albumList).when(albumRepositoryMock).findAll();
    }
}
