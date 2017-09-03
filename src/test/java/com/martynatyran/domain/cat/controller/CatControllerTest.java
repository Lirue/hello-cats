package com.martynatyran.domain.cat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martynatyran.Application;
import com.martynatyran.domain.cat.bo.ICatBO;
import com.martynatyran.domain.cat.config.Constants;
import com.martynatyran.domain.cat.dto.CatSnapshot;
import com.martynatyran.domain.cat.entity.GenderType;
import com.martynatyran.domain.cat.finder.CatSnapshotFinder;
import com.martynatyran.domain.cat.repo.ICatRepository;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.util.StringUtils;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class CatControllerTest {
    @Autowired
    private CatSnapshotFinder catSnapshotFinder;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ICatRepository catRepository;

    @Autowired
    private ICatBO catBO;

    private static final String CLAZZ = CatController.class.getSimpleName();
    private static final int AGE = 12;
    private static final GenderType GENDER = GenderType.FEMALE;
    private static final int numberOfCats = 3;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .build();
    }

    @After
    public void tearDown() {
        catRepository.deleteAll();
        this.mockMvc = null;
    }

    /*----------------------------------------------------------------------------------*/
    /*------------------------------- GET CATS OPERATION -------------------------------*/
    /*----------------------------------------------------------------------------------*/
    @Test
    public void shouldReturnListOfCats() throws Exception{
        //given
        createCatNews();
        List<CatSnapshot> catSnapshots = catSnapshotFinder.findAll();

        MockHttpServletRequestBuilder request
                = get("/cats")
                .accept(MediaType.APPLICATION_JSON_UTF8);

        //when
        ResultActions result = this.mockMvc.perform(request);

        //then
        result.
                andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(equalTo(catSnapshots.size()))))
                .andExpect(jsonPath("$[*].firstName").exists())
                .andExpect(jsonPath("$[*].lastName").exists())
                .andExpect(jsonPath("$[*].age").exists())
                .andExpect(jsonPath("$[*].age", hasItem(isA(Integer.class))))
                .andExpect(jsonPath("$[*].gender").exists());
    }

    /*------------------------------------------------------------------------------------*/
    /*------------------------------- EDIT A CAT OPERATION -------------------------------*/
    /*------------------------------------------------------------------------------------*/
    @Test
    public void shouldEditCat() throws Exception{
        //given
        String newFirstName = "test";
        CatNew catNew = createCatNew();
        CatSnapshot catSnapshot = createCat(catNew);

        String jsonWithNewFirstName = createJSON(newFirstName, catSnapshot.getLastName(),
                Integer.toString(catSnapshot.getAge()), catSnapshot.getGender().toString());

        MockHttpServletRequestBuilder request
                = put("/cats/" + catSnapshot.getId())
                .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonWithNewFirstName);

        //when
        ResultActions result = this.mockMvc.perform(request);

        //then
        result.andExpect(status().isNoContent());
    }

    @Test
    public void shouldNotEditCatWhenIdDoesNotExist() throws Exception{
        //given
        String id = "1234";
        String newFirstName = "test";
        CatNew catNew = createCatNew();
        CatSnapshot catSnapshot = createCat(catNew);

        String jsonWithNewFirstName = createJSON(newFirstName, catSnapshot.getLastName(),
                Integer.toString(catSnapshot.getAge()), catSnapshot.getGender().toString());

        MockHttpServletRequestBuilder request
                = put("/cats/" + id)
                .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonWithNewFirstName);

        //when
        ResultActions result = this.mockMvc.perform(request);

        //then
        result.andExpect(status().isNotFound());
    }

    @Test
    public void shouldNotEditCatWhenFirstNameIsEmpty() throws Exception{
        //given
        String newFirstName = null;
        CatNew catNew = createCatNew();
        CatSnapshot catSnapshot = createCat(catNew);
        String jsonWithoutFirstName = createJSON(newFirstName, CLAZZ, Integer.toString(AGE), GENDER.toString());

        MockHttpServletRequestBuilder request
                = put("/cats/" + catSnapshot.getId())
                .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonWithoutFirstName);

        //when
        ResultActions result = this.mockMvc.perform(request);

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotEditCatWhenFirstNameIsTooShort() throws Exception{
        //given
        CatNew catNew = createCatNew();
        CatSnapshot catSnapshot = createCat(catNew);
        String newFirstName = StringUtils.repeat("a", Constants.MIN_NAME_LENGTH - 1);
        String jsonWithTooShortFirstName = createJSON(newFirstName, CLAZZ, Integer.toString(AGE), GENDER.toString());

        MockHttpServletRequestBuilder request
                = put("/cats/" + catSnapshot.getId())
                .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonWithTooShortFirstName);

        //when
        ResultActions result = this.mockMvc.perform(request);

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotEditCatWhenFirstNameIsTooLong() throws Exception{
        //given
        CatNew catNew = createCatNew();
        CatSnapshot catSnapshot = createCat(catNew);
        String newFirstName = StringUtils.repeat("a", Constants.MAX_NAME_LENGTH + 1);
        String jsonWithTooLongFirstName = createJSON(newFirstName, CLAZZ, Integer.toString(AGE), GENDER.toString());

        MockHttpServletRequestBuilder request
                = put("/cats/" + catSnapshot.getId())
                .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonWithTooLongFirstName);

        //when
        ResultActions result = this.mockMvc.perform(request);

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotEditCatWhenLastNameIsEmpty() throws Exception{
        //given
        String newLastName = null;
        CatNew catNew = createCatNew();
        CatSnapshot catSnapshot = createCat(catNew);
        String jsonWithoutLastName = createJSON(CLAZZ, newLastName, Integer.toString(AGE), GENDER.toString());

        MockHttpServletRequestBuilder request
                = put("/cats/" + catSnapshot.getId())
                .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonWithoutLastName);

        //when
        ResultActions result = this.mockMvc.perform(request);

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotEditCatWhenLastNameIsTooShort() throws Exception{
        //given
        CatNew catNew = createCatNew();
        CatSnapshot catSnapshot = createCat(catNew);
        String newLastName = StringUtils.repeat("a", Constants.MIN_NAME_LENGTH - 1);
        String jsonWithTooShortLastName = createJSON(newLastName, CLAZZ, Integer.toString(AGE), GENDER.toString());

        MockHttpServletRequestBuilder request
                = put("/cats/" + catSnapshot.getId())
                .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonWithTooShortLastName);

        //when
        ResultActions result = this.mockMvc.perform(request);

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotEditCatWhenLastNameIsTooLong() throws Exception{
        //given
        CatNew catNew = createCatNew();
        CatSnapshot catSnapshot = createCat(catNew);
        String newLastName = StringUtils.repeat("a", Constants.MAX_NAME_LENGTH + 1);
        String jsonWithTooLongLastName = createJSON(newLastName, CLAZZ, Integer.toString(AGE), GENDER.toString());

        MockHttpServletRequestBuilder request
                = put("/cats/" + catSnapshot.getId())
                .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonWithTooLongLastName);

        //when
        ResultActions result = this.mockMvc.perform(request);

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotEditCatWhenAgeIsNull() throws Exception{
        //given
        String newAge = null;
        CatNew catNew = createCatNew();
        CatSnapshot catSnapshot = createCat(catNew);
        String jsonWithoutAge = createJSON(CLAZZ, CLAZZ, newAge, GENDER.toString());

        MockHttpServletRequestBuilder request
                = put("/cats/" + catSnapshot.getId())
                .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonWithoutAge);

        //when
        ResultActions result = this.mockMvc.perform(request);

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotEditCatWhenAgeIsTooLow() throws Exception{
        //given
        String newAge = Integer.toString(Constants.MIN_AGE - 1);
        CatNew catNew = createCatNew();
        CatSnapshot catSnapshot = createCat(catNew);
        String jsonWithTooLowAge = createJSON(CLAZZ, CLAZZ, newAge, GENDER.toString());

        MockHttpServletRequestBuilder request
                = put("/cats/" + catSnapshot.getId())
                .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonWithTooLowAge);

        //when
        ResultActions result = this.mockMvc.perform(request);

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotEditCatWhenAgeIsTooHigh() throws Exception{
        //given
        String newAge = Integer.toString(Constants.MAX_AGE + 1);
        CatNew catNew = createCatNew();
        CatSnapshot catSnapshot = createCat(catNew);
        String jsonWithTooHighAge = createJSON(CLAZZ, CLAZZ, newAge, GENDER.toString());

        MockHttpServletRequestBuilder request
                = put("/cats/" + catSnapshot.getId())
                .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonWithTooHighAge);

        //when
        ResultActions result = this.mockMvc.perform(request);

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotEditCatWhenGenderIsNull() throws Exception{
        //given
        String newGender = null;
        CatNew catNew = createCatNew();
        CatSnapshot catSnapshot = createCat(catNew);
        String jsonWithoutGender = createJSON(CLAZZ, CLAZZ, Integer.toString(AGE), newGender);

        MockHttpServletRequestBuilder request
                = put("/cats/" + catSnapshot.getId())
                .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonWithoutGender);

        //when
        ResultActions result = this.mockMvc.perform(request);

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotEditCatWhenGenderIsIncorrect() throws Exception{
        //given
        String newGender = "test";
        CatNew catNew = createCatNew();
        CatSnapshot catSnapshot = createCat(catNew);
        String jsonWithIncorrectGender = createJSON(CLAZZ, CLAZZ, Integer.toString(AGE), newGender);

        MockHttpServletRequestBuilder request
                = put("/cats/" + catSnapshot.getId())
                .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonWithIncorrectGender);

        //when
        ResultActions result = this.mockMvc.perform(request);

        //then
        result.andExpect(status().isBadRequest());
    }

    /*--------------------------------------------------------------------------------------*/
    /*------------------------------- DELETE A CAT OPERATION -------------------------------*/
    /*--------------------------------------------------------------------------------------*/
    @Test
    public void shouldDeleteCat() throws Exception{
        //given
        CatNew catNew = createCatNew();
        CatSnapshot catSnapshot = createCat(catNew);

        String body = new ObjectMapper().writeValueAsString(catNew);

        MockHttpServletRequestBuilder request
                = delete("/cats/" + catSnapshot.getId())
                .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        //when
        ResultActions result = this.mockMvc.perform(request);

        //then
        result.andExpect(status().isNoContent());
    }

    /*--------------------------------------------------------------------------------------*/
    /*------------------------------- CREATE A CAT OPERATION -------------------------------*/
    /*--------------------------------------------------------------------------------------*/
    @Test
    public void shouldCreateCat() throws Exception{
        //given
        CatNew catNew = createCatNew();

        String body = new ObjectMapper().writeValueAsString(catNew);

        MockHttpServletRequestBuilder request
                = post("/cats")
                .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(body);

        //when
        ResultActions result = this.mockMvc.perform(request);

        //then
        result.andExpect(status().isCreated())
                .andExpect(header().string("Location", Matchers.containsString("/cats/")));
    }

    @Test
    public void shouldNotCreateCatWhenFirstNameIsEmpty() throws Exception{
        //given
        String newFirstName = "";
        CatNew catNew = createCatNew();
        CatSnapshot catSnapshot = createCat(catNew);
        String jsonWithoutGender = createJSON(newFirstName, CLAZZ, Integer.toString(AGE), GENDER.toString());

        MockHttpServletRequestBuilder request
                = post("/cats/")
                .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonWithoutGender);

        //when
        ResultActions result = this.mockMvc.perform(request);

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateCatWhenFirstNameIsTooShort() throws Exception{
        //given
        String newFirstName = StringUtils.repeat("a", Constants.MIN_NAME_LENGTH - 1);
        String jsonWithTooShortFirstName = createJSON(newFirstName, CLAZZ, Integer.toString(AGE), GENDER.toString());

        MockHttpServletRequestBuilder request
                = post("/cats/")
                .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonWithTooShortFirstName);

        //when
        ResultActions result = this.mockMvc.perform(request);

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateCatWhenFirstNameIsTooLong() throws Exception{
        //given
        String newFirstName = StringUtils.repeat("a", Constants.MAX_NAME_LENGTH + 1);
        String jsonWithTooLongFirstName = createJSON(newFirstName, CLAZZ, Integer.toString(AGE), GENDER.toString());

        MockHttpServletRequestBuilder request
                = post("/cats/")
                .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonWithTooLongFirstName);

        //when
        ResultActions result = this.mockMvc.perform(request);

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateCatWhenLastNameIsEmpty() throws Exception{
        //given
        String newLastName = "";
        String jsonWithoutLastName = createJSON(CLAZZ, newLastName, Integer.toString(AGE), GENDER.toString());

        MockHttpServletRequestBuilder request
                = post("/cats/")
                .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonWithoutLastName);

        //when
        ResultActions result = this.mockMvc.perform(request);

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateCatWhenLastNameIsTooShort() throws Exception{
        //given
        String newLastName = StringUtils.repeat("a", Constants.MIN_NAME_LENGTH - 1);
        String jsonWithTooShortLastName = createJSON(newLastName, CLAZZ, Integer.toString(AGE), GENDER.toString());

        MockHttpServletRequestBuilder request
                = post("/cats/")
                .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonWithTooShortLastName);

        //when
        ResultActions result = this.mockMvc.perform(request);

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateCatWhenLastNameIsTooLong() throws Exception{
        //given
        String newLastName = StringUtils.repeat("a", Constants.MAX_NAME_LENGTH + 1);
        String jsonWithTooLongLastName = createJSON(newLastName, CLAZZ, Integer.toString(AGE), GENDER.toString());

        MockHttpServletRequestBuilder request
                = post("/cats/")
                .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonWithTooLongLastName);

        //when
        ResultActions result = this.mockMvc.perform(request);

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateCatWhenAgeIsNull() throws Exception{
        //given
        String newAge = "";
        String jsonWithoutAge = createJSON(CLAZZ, CLAZZ, newAge, GENDER.toString());

        MockHttpServletRequestBuilder request
                = post("/cats/")
                .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonWithoutAge);

        //when
        ResultActions result = this.mockMvc.perform(request);

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateCatWhenAgeIsTooLow() throws Exception{
        //given
        String newAge = Integer.toString(Constants.MIN_AGE - 1);
        String jsonWithTooLowAge = createJSON(CLAZZ, CLAZZ, newAge, GENDER.toString());

        MockHttpServletRequestBuilder request
                = post("/cats/")
                .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonWithTooLowAge);

        //when
        ResultActions result = this.mockMvc.perform(request);

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateCatWhenAgeIsTooHigh() throws Exception{
        //given
        String newAge = Integer.toString(Constants.MAX_AGE + 1);
        String jsonWithTooHighAge = createJSON(CLAZZ, CLAZZ, newAge, GENDER.toString());

        MockHttpServletRequestBuilder request
                = post("/cats/")
                .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonWithTooHighAge);

        //when
        ResultActions result = this.mockMvc.perform(request);

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateCatWhenGenderIsNull() throws Exception{
        //given
        String newGender = "";
        String jsonWithoutGender = createJSON(CLAZZ, CLAZZ, Integer.toString(AGE), newGender);

        MockHttpServletRequestBuilder request
                = post("/cats/")
                .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonWithoutGender);

        //when
        ResultActions result = this.mockMvc.perform(request);

        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateCatWhenGenderIsIncorrect() throws Exception{
        //given
        String newGender = "test";
        String jsonWithIncorrectGender = createJSON(CLAZZ, CLAZZ, Integer.toString(AGE), newGender);

        MockHttpServletRequestBuilder request
                = post("/cats/")
                .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonWithIncorrectGender);

        //when
        ResultActions result = this.mockMvc.perform(request);

        //then
        result.andExpect(status().isBadRequest());
    }

    /*-------------------------------------------------------------------------------*/
    /*------------------------------- HELPFUL METHODS -------------------------------*/
    /*-------------------------------------------------------------------------------*/
    private void createCatNews(){
        for (int i = 0; i < numberOfCats; i++){
            catBO.add(new CatNew(CLAZZ, CLAZZ, AGE, GENDER));
        }
    }

    private CatNew createCatNew(){
        return new CatNew(CLAZZ, CLAZZ, AGE, GENDER);
    }

    private CatSnapshot createCat(CatNew catNew){
        return catSnapshotFinder.findById(catBO.add(catNew)).get();
    }

    private String createJSON(String firstName, String lastName, String age, String gender){
        return String.format("{\"firstName\": \""+ firstName +",\"" +
                "lastName\": \"" + lastName +"\",\"" +
                "age\": \"" + age +"\"" +
                "gender:\": \"" + gender +"\"}");
    }
}
