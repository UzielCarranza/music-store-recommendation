package com.service.musicstorerecommendations.service;

import com.service.musicstorerecommendations.model.LabelRecommendation;
import com.service.musicstorerecommendations.model.TrackRecommendation;
import com.service.musicstorerecommendations.repository.LabelRecommendationRepository;
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
public class LabelRecommendationServiceTest {

    private LabelRecommendationRepository labelRepository;

    private LabelRecommendationService service;


    private LabelRecommendation labelSetUp;
    private LabelRecommendation actualLabel;
    private LabelRecommendation editedLabel;

    private List<LabelRecommendation> labelsList;

    @Before
    public void setUp() throws Exception {

        setUpLabelRepository();

        service = new LabelRecommendationService(labelRepository);


    }

    @Test
    public void shouldReturnLabelById() throws Exception {

//        ACT
        actualLabel = service.getLabelRecommendationById(labelSetUp.getId());

        assertEquals(actualLabel, labelSetUp);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNoIdFound() throws Exception {

//        ACT
        actualLabel = service.getLabelRecommendationById(10);

        assertEquals(actualLabel, labelSetUp);
    }

    @Test
    public void shouldReturnNewLabel() throws Exception {

//        ACT
        // get it back out of the database
        LabelRecommendation labelPersistentOnDatabase = service.getLabelRecommendationById(labelSetUp.getId());

//        ASSERT
        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(labelSetUp, labelPersistentOnDatabase);

    }

    @Test
    public void shouldReturnNewLabelOnPostRequest() throws Exception {

//        ACT
        // get it back out of the database
        LabelRecommendation labelPersistentOnDatabase = service.createLabel(labelSetUp);

//        ASSERT
        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(labelSetUp, labelPersistentOnDatabase);

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenLabelIsNull() throws Exception {

//        ARRANGE
        labelSetUp = null;

//        ACT
        actualLabel = service.createLabel(labelSetUp);

        assertEquals(actualLabel, labelSetUp);
    }

    @Test
    public void shouldReturnAllLabels() throws Exception {

//        ACT
        // get it back out of the database
        List<LabelRecommendation> labelsPersistentOnDatabase = service.getAllLabelRecommendations();

//        ASSERT
        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(labelsList, labelsPersistentOnDatabase);

    }

    @Test
    public void shouldReturnEmptyListOfLabels() throws Exception {

//        ARRANGE

        labelsList = new ArrayList<>();

        doReturn(labelsList).when(labelRepository).findAll();
//        ACT
        // get it back out of the database
        List<LabelRecommendation> labelPersistentOnDatabase = service.getAllLabelRecommendations();


//        ASSERT
        // confirm that the thing I got back from the database is the thing I wrote the database
        assertEquals(labelsList, labelPersistentOnDatabase);


    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenUpdatingALabelThatIsNull() throws Exception {

//        ARRANGE
        labelSetUp = null;
//        ACT
        service.updateLabelRecommendationById(labelSetUp);

        assertEquals(actualLabel, labelSetUp);
    }

    @Test
    public void shouldUpdateALabel() throws Exception {

//        ACT
        service.updateLabelRecommendationById(labelSetUp);

//        ACT
        // get it back out of the database
        LabelRecommendation labelPersistentOnDatabase = service.getLabelRecommendationById(editedLabel.getId());

        assertEquals(labelSetUp, labelPersistentOnDatabase);
    }

    public void setUpLabelRepository() {
        labelRepository = mock(LabelRecommendationRepository.class);
        labelSetUp = new LabelRecommendation();
        labelSetUp.setUserId(1);
        labelSetUp.setLabelId(1);
        labelSetUp.setLiked(true);
        labelSetUp.setId(1);

        editedLabel = new LabelRecommendation();
        editedLabel.setLiked(false);
        editedLabel.setLabelId(1);
        editedLabel.setUserId(1);
        editedLabel.setId(1);

        doReturn(labelSetUp).when(labelRepository).save(labelSetUp);
        doReturn(editedLabel).when(labelRepository).save(editedLabel);
        doReturn(Optional.of(labelSetUp)).when(labelRepository).findById(1L);

        //        GET ALL
        LabelRecommendation label1 = new LabelRecommendation();

        label1.setUserId(1);
        label1.setLabelId(1);
        label1.setLiked(true);
        label1.setId(2);

        labelsList = new ArrayList<>();
        labelsList.add(labelSetUp);
        labelsList.add(label1);
        doReturn(labelsList).when(labelRepository).findAll();
    }
}
