package com.service.musicstorerecommendations.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.musicstorerecommendations.model.AlbumRecommendation;
import com.service.musicstorerecommendations.service.AlbumRecommendationService;
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
@WebMvcTest(AlbumRecommendationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AlbumRecommendationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    // The aim of this unit test is to test the controller and NOT the service layer.
    // Therefore mock the service layer.
    @MockBean
    private AlbumRecommendationService service;

    @Autowired
    //used to move between Objects and JSON
    private ObjectMapper mapper;

    private AlbumRecommendation albumRecommendationInput;
    private AlbumRecommendation albumRecommendationOut;
    private AlbumRecommendation albumRecommendationOut2;
    private AlbumRecommendation albumRecommendationBadInput;

    private List<AlbumRecommendation> albumRecommendationList;

    @Before
    public void setUp() {
//      input album
        albumRecommendationInput = new AlbumRecommendation();
        albumRecommendationInput.setAlbumId(1);
        albumRecommendationInput.setLiked(true);
        albumRecommendationInput.setUserId(1);

//output album
        albumRecommendationOut = new AlbumRecommendation();
        albumRecommendationOut.setAlbumId(1);
        albumRecommendationOut.setLiked(true);
        albumRecommendationOut.setUserId(1);
        albumRecommendationOut.setId(1);

        albumRecommendationOut2 = new AlbumRecommendation();

        albumRecommendationOut2.setAlbumId(1);
        albumRecommendationOut2.setLiked(true);
        albumRecommendationOut2.setUserId(1);
        albumRecommendationOut2.setId(2);

//        list of albums
        albumRecommendationList = new ArrayList<>();
        albumRecommendationList.add(albumRecommendationOut);
        albumRecommendationList.add(albumRecommendationOut2);


//bad input album
//        doesnt include album id
        albumRecommendationBadInput = new AlbumRecommendation();
        albumRecommendationOut2.setLiked(true);
        albumRecommendationOut2.setUserId(1);

        when(service.createAlbum(albumRecommendationInput)).thenReturn(albumRecommendationOut);
        when(service.getAlbumById(1)).thenReturn(albumRecommendationOut);
        when(service.getAlbumById(15)).thenThrow(new IllegalArgumentException("Recommendation not found"));
        when(service.getAllAlbums()).thenReturn(albumRecommendationList);

    }

    @Test
    public void shouldReturnNewAlbumRecommendationOnPostRequest() throws Exception {

        mockMvc.perform(
                        post("/album/recommendation")
                                .content(mapper.writeValueAsString(albumRecommendationInput)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isCreated()) //Expected response status code.
                .andExpect(content().json(mapper.writeValueAsString(albumRecommendationOut))); //matches the output of the Controller with the mock output.
    }

    @Test
    public void shouldReturn422StatusCodeOnMissingAlbumIdAttribute() throws Exception {

        mockMvc.perform(
                        post("/album/recommendation")
                                .content(mapper.writeValueAsString(albumRecommendationBadInput)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.
    }

    @Test
    public void shouldReturnAlbumRecommendationById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/album/recommendation/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isOk())
                // see https://www.baeldung.com/guide-to-jayway-jsonpath for more details on jsonPath
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void shouldReturn422OnAlbumRecommendationIdThatDoesntExist() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/album/recommendation/{id}", 15)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldReturnAllAlbums() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/album/recommendation/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(albumRecommendationList)));
    }

    @Test
    public void shouldReturnEmptyArrayOfRecommendations() throws Exception {
//        create a new list, thus emptying the current list
        albumRecommendationList = new ArrayList<>();
//        need to reset the mocking service to catch the emptying of the list
        when(service.getAllAlbums()).thenReturn(albumRecommendationList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/album/recommendation/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn422StatusWithBadAlbumIdUpdateRequest() throws Exception {

//        ARRANGE
        albumRecommendationInput.setAlbumId(0); //<--pretend this is a bad title, we are passing a null property
        doThrow(new IllegalArgumentException("Artist not found")).when(service).updateAlbumById(albumRecommendationInput);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/album/recommendation")
                                .content(mapper.writeValueAsString(albumRecommendationBadInput)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.
    }

    @Test
    public void shouldReturn204OnSuccessfulPutRequest() throws Exception {
//        ARRANGE
        albumRecommendationOut.setAlbumId(1);
        doNothing().when(service).updateAlbumById(albumRecommendationInput);

        mockMvc.perform(

                        MockMvcRequestBuilders.put("/album/recommendation")
                                .content(mapper.writeValueAsString(albumRecommendationOut)) //converts object to JSON and places into RequestBody

                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isNoContent()); //Expected response status code.
    }

    @Test
    public void shouldDeleteAlbumRecommendationReturnNoContent() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/album/recommendation/{id}", 1))
                .andDo(print())
                .andExpect(status().isNoContent()); //Expected response status code.
    }

    @Test
    public void shouldSendStatus404WhenDeletingAnAlbumRecommendationThatDoesntExist() throws Exception {
        doThrow(new EmptyResultDataAccessException("not found", 1)).when(service).deleteAlbumById(15);

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/album/recommendation/{id}", 15))
                .andDo(print())
                .andExpect(status().isNotFound()); //Expected response status code.
    }

}