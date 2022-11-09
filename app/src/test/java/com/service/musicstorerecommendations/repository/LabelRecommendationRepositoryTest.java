package com.service.musicstorerecommendations.repository;

import com.service.musicstorerecommendations.model.LabelRecommendation;
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
public class LabelRecommendationRepositoryTest {
    @Autowired
    private LabelRecommendationRepository labelRepository;


    private LabelRecommendation label;
    private LabelRecommendation editedLabel;

    @Before
    public void setUp() throws Exception {
        labelRepository.deleteAll();
        setUpLabelRepository();
    }

    //    CODE that Dan Mueller wrote during class on NOV, 03, 2022
    @Test
    public void shouldAddFindUpdateDeleteLabel() {

        // get it back out of the database
        LabelRecommendation labelPersistentOnDatabase = labelRepository.findById(label.getId()).get();

        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(label, labelPersistentOnDatabase);

//        update
        labelPersistentOnDatabase.setUserId(2);
        labelRepository.save(labelPersistentOnDatabase);
        assertEquals(labelPersistentOnDatabase, editedLabel);

        // delete it
        labelRepository.deleteById(label.getId());

//        // go try to get it again
        Optional<LabelRecommendation> artist3 = labelRepository.findById(label.getId());
//
//        // confirm that it's gone
        assertEquals(false, artist3.isPresent());
    }

    public void setUpLabelRepository() {
        label = new LabelRecommendation();
        label.setUserId(1);
        label.setLabelId(1);
        label.setLiked(true);
        labelRepository.save(label);

        editedLabel = new LabelRecommendation();
        editedLabel.setLiked(true);
        editedLabel.setLabelId(1);
        editedLabel.setUserId(2);
        editedLabel.setId(1);

    }
}
