package com.service.musicstorerecommendations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.musicstorerecommendations.controller.LabelRecommendationController;
import com.service.musicstorerecommendations.model.LabelRecommendation;
import com.service.musicstorerecommendations.service.LabelRecommendationService;
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
@WebMvcTest(LabelRecommendationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class LabelRecommendationTest {

    @Autowired
    private MockMvc mockMvc;

    // The aim of this unit test is to test the controller and NOT the service layer.
    // Therefore mock the service layer.
    @MockBean
    private LabelRecommendationService service;

    @Autowired
    //used to move between Objects and JSON
    private ObjectMapper mapper;

    private LabelRecommendation labelInput;
    private LabelRecommendation labelOut;
    private LabelRecommendation labelOut2;
    private LabelRecommendation labelBadInput;

    private List<LabelRecommendation> labelList;

    @Before
    public void setUp() {
//      input
        labelInput = new LabelRecommendation();
        labelInput.setUserId(1);
        labelInput.setLiked(true);
        labelInput.setLabelId(1);

//output
        labelOut = new LabelRecommendation();
        labelOut.setUserId(1);
        labelOut.setLiked(true);
        labelOut.setLabelId(1);
        labelOut.setId(1);

        labelOut2 = new LabelRecommendation();
        labelOut2.setUserId(1);
        labelOut2.setLiked(true);
        labelOut2.setLabelId(1);
        labelOut2.setId(2);

//        list of label
        labelList = new ArrayList<>();
        labelList.add(labelOut);
        labelList.add(labelOut2);


//bad input label
        labelBadInput = new LabelRecommendation();
//        didnt include a user id for the label... validation will send an error back
        labelOut2.setLiked(true);
        labelOut2.setLabelId(1);


        when(service.createLabel(labelInput)).thenReturn(labelOut);
        when(service.getLabelRecommendationById(1)).thenReturn(labelOut);
        when(service.getLabelRecommendationById(15)).thenThrow(new IllegalArgumentException("Label not found"));
        when(service.getAllLabelRecommendations()).thenReturn(labelList);

    }

    @Test
    public void shouldReturnNewLabelRecommendationOnPostRequest() throws Exception {

        mockMvc.perform(
                        post("/label/recommendation")
                                .content(mapper.writeValueAsString(labelInput)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isCreated()) //Expected response status code.
                .andExpect(content().json(mapper.writeValueAsString(labelOut))); //matches the output of the Controller with the mock output.
    }

    @Test
    public void shouldReturn422StatusCodeOnMissingUserIdAttribute() throws Exception {

        mockMvc.perform(
                        post("/label/recommendation")
                                .content(mapper.writeValueAsString(labelBadInput)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.
    }

    @Test
    public void shouldReturnLabelRecommendationById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/label/recommendation/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isOk())
                // see https://www.baeldung.com/guide-to-jayway-jsonpath for more details on jsonPath
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void shouldReturn422OnLabelRecommendationIdThatDoesntExist() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/label/recommendation/{id}", 15)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldReturnAllLabelRecommendations() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/label/recommendation/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(labelList)));
    }

    @Test
    public void shouldReturnEmptyArrayOfLabelRecommendation() throws Exception {
//        create a new list, thus emptying the current list
        labelList = new ArrayList<>();
//        need to reset the mocking service to catch the emptying of the list
        when(service.getAllLabelRecommendations()).thenReturn(labelList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/label/recommendation/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn422StatusWithBadUserIdUpdateRequest() throws Exception {

//        ARRANGE
        labelInput.setUserId(0); //<--pretend this is a bad name, we are passing a null property
        doThrow(new IllegalArgumentException("label not found")).when(service).updateLabelRecommendationById(labelInput);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/label/recommendation")
                                .content(mapper.writeValueAsString(labelBadInput)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.
    }

    @Test
    public void shouldReturn204OnSuccessfulPutRequest() throws Exception {
//        ARRANGE
        labelOut.setUserId(1);
        doNothing().when(service).updateLabelRecommendationById(labelInput);

        mockMvc.perform(

                        MockMvcRequestBuilders.put("/label/recommendation")
                                .content(mapper.writeValueAsString(labelOut)) //converts object to JSON and places into RequestBody

                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the console below.
                .andExpect(status().isNoContent()); //Expected response status code.
    }

    @Test
    public void shouldDeleteLabelRecommendationReturnNoContent() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/label/recommendation/{id}", 1))
                .andDo(print())
                .andExpect(status().isNoContent()); //Expected response status code.
    }

    @Test
    public void shouldSendStatus404WhenDeletingALabelRecommendationThatDoesntExist() throws Exception {
        doThrow(new EmptyResultDataAccessException("not found", 1)).when(service).deleteLabelRecommendationById(15);

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/label/recommendation/{id}", 15))
                .andDo(print())
                .andExpect(status().isNotFound()); //Expected response status code.
    }

}