package com.service.musicstorerecommendations.repository;

import com.service.musicstorerecommendations.model.ArtistRecommendation;
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
public class ArtistRecommendationReposirotyTest {

    @Autowired
    private ArtistRecommendationRepository artistRepository;


    private ArtistRecommendation artist;
    private ArtistRecommendation editedArtist;

    @Before
    public void setUp() throws Exception {
        artistRepository.deleteAll();
        setUpArtistRepository();
    }

    //    CODE that Dan Mueller wrote during class on NOV, 03, 2022
    @Test
    public void shouldAddFindUpdateDeleteArtist() {

        // get it back out of the database
        ArtistRecommendation artistPersistentOnDatabase = artistRepository.findById(artist.getId()).get();

        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(artist, artistPersistentOnDatabase);

//        update
        artistPersistentOnDatabase.setArtistId(2);
        artistRepository.save(artistPersistentOnDatabase);
        assertEquals(artistPersistentOnDatabase, editedArtist);

        // delete it
        artistRepository.deleteById(artist.getId());

//        // go try to get it again
        Optional<ArtistRecommendation> artist3 = artistRepository.findById(artist.getId());
//
//        // confirm that it's gone
        assertEquals(false, artist3.isPresent());
    }

    public void setUpArtistRepository() {
        artist = new ArtistRecommendation();
        artist.setUserId(1);
        artist.setArtistId(1);
        artist.setLiked(true);


        editedArtist = new ArtistRecommendation();
        editedArtist.setUserId(1);
        editedArtist.setArtistId(2);
        editedArtist.setLiked(true);
        editedArtist.setId(1);

        artistRepository.save(artist);
    }

}

