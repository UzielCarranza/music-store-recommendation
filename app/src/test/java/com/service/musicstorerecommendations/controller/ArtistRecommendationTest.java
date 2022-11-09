package com.service.musicstorerecommendations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.musicstorerecommendations.controller.ArtistRecommendationController;
import com.service.musicstorerecommendations.model.ArtistRecommendation;
import com.service.musicstorerecommendations.service.ArtistRecommendationService;
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
@WebMvcTest(ArtistRecommendationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ArtistRecommendationTest {


    @Autowired
    private MockMvc mockMvc;

    // The aim of this unit test is to test the controller and NOT the service layer.
    // Therefore mock the service layer.
    @MockBean
    private ArtistRecommendationService service;

    @Autowired
    //used to move between Objects and JSON
    private ObjectMapper mapper;

    private ArtistRecommendation artistInput;
    private ArtistRecommendation artistOut;
    private ArtistRecommendation artistOut2;
    private ArtistRecommendation artistBadInput;

    private List<ArtistRecommendation> artistList;

    @Before
    public void setUp() {
//      input album
        artistInput = new ArtistRecommendation();
        artistInput.setUserId(1);
        artistInput.setLiked(true);
        artistInput.setArtistId(1);

//output artist
        artistOut = new ArtistRecommendation();
        artistInput.setUserId(1);
        artistInput.setLiked(true);
        artistInput.setArtistId(1);
        artistOut.setId(1);

        artistOut2 = new ArtistRecommendation();
        artistInput.setUserId(1);
        artistInput.setLiked(true);
        artistInput.setArtistId(1);
        artistOut2.setId(2);

//        list of artist
        artistList = new ArrayList<>();
        artistList.add(artistOut);
        artistList.add(artistOut2);


//bad input artist
//        does not include user id
        artistBadInput = new ArtistRecommendation();
        artistBadInput.setLiked(false);
        artistBadInput.setArtistId(1);

        when(service.createArtistRecommendation(artistInput)).thenReturn(artistOut);
        when(service.getArtistRecommendationById(1)).thenReturn(artistOut);
        when(service.getArtistRecommendationById(15)).thenThrow(new IllegalArgumentException("Artist not found"));
        when(service.getAllArtistsRecommendations()).thenReturn(artistList);

    }

    @Test
    public void shouldReturnNewArtistRecommendationOnPostRequest() throws Exception {

        mockMvc.perform(
                        post("/artist/recommendation")
                                .content(mapper.writeValueAsString(artistInput)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isCreated()) //Expected response status code.
                .andExpect(content().json(mapper.writeValueAsString(artistOut))); //matches the output of the Controller with the mock output.
    }

    @Test
    public void shouldReturn422StatusCodeOnMissingIdAttribute() throws Exception {

        mockMvc.perform(
                        post("/artist/recommendation")
                                .content(mapper.writeValueAsString(artistBadInput)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.
    }

    @Test
    public void shouldReturnArtistRecommendationById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/artist/recommendation/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isOk())
                // see https://www.baeldung.com/guide-to-jayway-jsonpath for more details on jsonPath
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void shouldReturn422OnArtistRecommendationIdThatDoesntExist() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/artist/recommendation/{id}", 15)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldReturnAllArtistRecommendations() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/artist/recommendation/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(artistList)));
    }

    @Test
    public void shouldReturnEmptyArrayOfArtistRecommendations() throws Exception {
//        create a new list, thus emptying the current list
        artistList = new ArrayList<>();
//        need to reset the mocking service to catch the emptying of the list
        when(service.getAllArtistsRecommendations()).thenReturn(artistList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/artist/recommendation/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn422StatusWithBadArtistRecommendationIdUpdateRequest() throws Exception {

//        ARRANGE
        artistInput.setArtistId(0); //<--pretend this is a bad title, we are passing a null property
        doThrow(new IllegalArgumentException("Artist not found")).when(service).updateArtistRecommendationById(artistInput);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/artist/recommendation")
                                .content(mapper.writeValueAsString(artistBadInput)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.
    }

    @Test
    public void shouldReturn204OnSuccessfulPutRequest() throws Exception {
//        ARRANGE
        artistOut.setArtistId(1);
        artistOut.setUserId(1);
        doNothing().when(service).updateArtistRecommendationById(artistInput);

        mockMvc.perform(

                        MockMvcRequestBuilders.put("/artist/recommendation")
                                .content(mapper.writeValueAsString(artistOut)) //converts object to JSON and places into RequestBody

                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isNoContent()); //Expected response status code.
    }

    @Test
    public void shouldDeleteArtistRecommendationReturnNoContent() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/artist/recommendation/{id}", 1))
                .andDo(print())
                .andExpect(status().isNoContent()); //Expected response status code.
    }

    @Test
    public void shouldSendStatus404WhenDeletingAnArtistRecommendationThatDoesntExist() throws Exception {
        doThrow(new EmptyResultDataAccessException("not found", 1)).when(service).deleteArtistRecommendationById(15);

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/artist/recommendation/{id}", 15))
                .andDo(print())
                .andExpect(status().isNotFound()); //Expected response status code.
    }
}