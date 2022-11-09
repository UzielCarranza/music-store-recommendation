package com.service.musicstorerecommendations.repository;

import com.service.musicstorerecommendations.model.AlbumRecommendation;
import com.service.musicstorerecommendations.repository.AlbumRecommendationRepository;
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
public class AlbumRepositoryRecommendationTest {

    @Autowired
    private AlbumRecommendationRepository albumRepository;


    private AlbumRecommendation album1;
    private AlbumRecommendation editedAlbum;

    @Before
    public void setUp() throws Exception {
        albumRepository.deleteAll();
        setUpAlbumRepository();
    }

    //    CODE that Dan Mueller wrote during class on NOV, 03, 2022
    @Test
    public void shouldAddFindUpdateDeleteAlbum() {

        // get it back out of the database
        AlbumRecommendation albumPersistentOnDatabase = albumRepository.findById(album1.getId()).get();

        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(album1, albumPersistentOnDatabase);

//        update
        albumPersistentOnDatabase.setAlbumId(2);
        albumRepository.save(albumPersistentOnDatabase);
        assertEquals(albumPersistentOnDatabase, editedAlbum);

        // delete it
        albumRepository.deleteById(album1.getId());

//        // go try to get it again
        Optional<AlbumRecommendation> album3 = albumRepository.findById(album1.getId());
//
//        // confirm that it's gone
        assertEquals(false, album3.isPresent());
    }



    public void setUpAlbumRepository() {
        album1 = new AlbumRecommendation();
        album1.setAlbumId(1);
        album1.setUserId(1);
        album1.setLiked(true);


        editedAlbum = new AlbumRecommendation();
        editedAlbum.setLiked(true);
        editedAlbum.setUserId(1);
        editedAlbum.setAlbumId(2);
        editedAlbum.setId(1);
        albumRepository.save(album1);
    }
}

