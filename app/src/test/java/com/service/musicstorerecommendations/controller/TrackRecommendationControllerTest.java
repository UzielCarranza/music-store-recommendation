package com.service.musicstorerecommendations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.musicstorerecommendations.model.TrackRecommendation;
import com.service.musicstorerecommendations.service.TrackRecommendationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TrackRecommendationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TrackRecommendationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    // The aim of this unit test is to test the controller and NOT the service layer.
    // Therefore mock the service layer.
    @MockBean
    private TrackRecommendationService service;

    @Autowired
    //used to move between Objects and JSON
    private ObjectMapper mapper;

    private TrackRecommendation trackInput;
    private TrackRecommendation trackOut;
    private TrackRecommendation trackOut2;
    private TrackRecommendation trackBadInput;

    private List<TrackRecommendation> trackList;

    @Before
    public void setUp() {
//      input album
        trackInput = new TrackRecommendation();
        trackInput.setUserId(1);
        trackInput.setLiked(true);
        trackInput.setTrackId(1);

//output album
        trackOut = new TrackRecommendation();
        trackOut.setUserId(1);
        trackOut.setLiked(true);
        trackOut.setTrackId(1);
        trackOut.setId(1);

        trackOut2 = new TrackRecommendation();

        trackOut2.setUserId(1);
        trackOut2.setLiked(true);
        trackOut2.setTrackId(1);
        trackOut2.setId(2);

//        list of albums
        trackList = new ArrayList<>();
        trackList.add(trackOut);
        trackList.add(trackOut2);


//bad input album
//        missing user id
        trackBadInput = new TrackRecommendation();
        trackOut2.setLiked(true);
        trackOut2.setTrackId(1);


        when(service.createTrackRecommendation(trackInput)).thenReturn(trackOut);
        when(service.getTrackRecommendationById(1)).thenReturn(trackOut);
        when(service.getTrackRecommendationById(15)).thenThrow(new IllegalArgumentException("Track not found"));
        when(service.getAllTracksRecommendations()).thenReturn(trackList);

    }

    @Test
    public void shouldReturnNewTrackRecommendationOnPostRequest() throws Exception {

        mockMvc.perform(
                        post("/track/recommendation")
                                .content(mapper.writeValueAsString(trackInput)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isCreated()) //Expected response status code.
                .andExpect(content().json(mapper.writeValueAsString(trackOut))); //matches the output of the Controller with the mock output.
    }

    @Test
    public void shouldReturn422StatusCodeOnMissingTitleAttribute() throws Exception {

        mockMvc.perform(
                        post("/track/recommendation")
                                .content(mapper.writeValueAsString(trackBadInput)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.
    }

    @Test
    public void shouldReturnTrackRecommendationById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/track/recommendation/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isOk())
                // see https://www.baeldung.com/guide-to-jayway-jsonpath for more details on jsonPath
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void shouldReturn422OnTrackRecommendationIdThatDoesntExist() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/track/recommendation/{id}", 15)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldReturnAllTrackRecommendations() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/track/recommendation/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(trackList)));
    }

    @Test
    public void shouldReturnEmptyArrayOfTrackRecommendations() throws Exception {
//        create a new list, thus emptying the current list
        trackList = new ArrayList<>();
//        need to reset the mocking service to catch the emptying of the list
        when(service.getAllTracksRecommendations()).thenReturn(trackList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/track/recommendation/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn422StatusWithBadUserIdUpdateRequest() throws Exception {

//        ARRANGE
        trackInput.setUserId(0); //<--pretend this is a bad name, we are passing a null property
        doThrow(new IllegalArgumentException("Track not found")).when(service).updateTrackRecommendationById(trackInput);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/track/recommendation")
                                .content(mapper.writeValueAsString(trackBadInput)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.
    }

    @Test
    public void shouldReturn204OnSuccessfulPutRequest() throws Exception {
//        ARRANGE
        trackOut.setUserId(1);
        doNothing().when(service).updateTrackRecommendationById(trackInput);

        mockMvc.perform(

                        MockMvcRequestBuilders.put("/track/recommendation")
                                .content(mapper.writeValueAsString(trackOut)) //converts object to JSON and places into RequestBody

                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isNoContent()); //Expected response status code.
    }

    @Test
    public void shouldDeleteTrackRecommendationReturnNoContent() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/track/recommendation/{id}", 1))
                .andDo(print())
                .andExpect(status().isNoContent()); //Expected response status code.
    }

    @Test
    public void shouldSendStatus404WhenDeletingATrackRecommendationThatDoesntExist() throws Exception {
        doThrow(new EmptyResultDataAccessException("not found", 1)).when(service).deleteTrackRecommendationById(15);

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/track/recommendation/{id}", 15))
                .andDo(print())
                .andExpect(status().isNotFound()); //Expected response status code.
    }
}