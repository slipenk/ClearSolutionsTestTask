package com.slipenk.clearsolutionstesttask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slipenk.clearsolutionstesttask.controller.UserController;
import com.slipenk.clearsolutionstesttask.entity.User;
import com.slipenk.clearsolutionstesttask.exceptions.BadDateException;
import com.slipenk.clearsolutionstesttask.exceptions.RestExceptionHandler;
import com.slipenk.clearsolutionstesttask.exceptions.UserAgeValidationException;
import com.slipenk.clearsolutionstesttask.exceptions.UserNotFoundException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.BIRTH_DATE_IS_MANDATORY;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.DATE_MUST_BE_EARLIER;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.DELETED_USER;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.DOLLAR_SIGN;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.DOT;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.EMAIL_IS_MANDATORY;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.EMAIL_IS_NOT_VALID;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.EMPTY_STRING;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.FIRST_NAME_IS_MANDATORY;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.FROM_DATE;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.ID_PATH;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.INVALID_LOCATION_HEADER;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.IT_ALLOWS_TO_REGISTER;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.LAST_NAME_IS_MANDATORY;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.LOCATION;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.PATCH_JSON;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.SLASH;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_ADDRESS_FIELD;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_ADDRESS_LIST_1_FIELD;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_ADDRESS_LIST_2_FIELD;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_BIRTH_DATE_FIELD;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_BIRTH_DATE_LIST_1_FIELD;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_BIRTH_DATE_LIST_2_FIELD;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_EMAIL_FIELD;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_EMAIL_LIST_1_FIELD;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_EMAIL_LIST_2_FIELD;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_FIRST_NAME_FIELD;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_FIRST_NAME_LIST_1_FIELD;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_FIRST_NAME_LIST_2_FIELD;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_ID_FIELD;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_ID_LIST_1_FIELD;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_ID_LIST_2_FIELD;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_LAST_NAME_FIELD;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_LAST_NAME_LIST_1_FIELD;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_LAST_NAME_LIST_2_FIELD;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_NOT_VALID_EMAIL_WITHOUT_AT_2;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_NOT_VALID_EMAIL_WITHOUT_DOT_2;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_PHONE_NUMBER_FIELD;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_PHONE_NUMBER_LIST_1_FIELD;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_PHONE_NUMBER_LIST_2_FIELD;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_UPDATED_EMAIL_1;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_UPDATED_WRONG_EMAIL_WITHOUT_AT_1;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_UPDATED_WRONG_EMAIL_WITHOUT_DOT_1;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_USER_ADDRESS_1;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_USER_ADDRESS_2;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_USER_ADDRESS_3;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_USER_BIRTH_DATE_1;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_USER_BIRTH_DATE_2;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_USER_BIRTH_DATE_3;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_USER_EMAIL_1;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_USER_EMAIL_2;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_USER_EMAIL_3;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_USER_FIRST_NAME_1;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_USER_FIRST_NAME_2;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_USER_FIRST_NAME_3;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_USER_LAST_NAME_1;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_USER_LAST_NAME_2;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_USER_LAST_NAME_3;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_USER_PHONE_NUMBER_1;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_USER_PHONE_NUMBER_2;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_USER_PHONE_NUMBER_3;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TO_DATE;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.USERS_NOT_FOUND;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.USERS_PATH;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.USER_AGE_PROPS;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.USER_ID_NOT_FOUND;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.VALUE_MUST_BE_EARLIER_THAN_CURRENT_DATE;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.YEARS_OLD;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClearSolutionsTestTaskApplicationTests {

    private final ObjectMapper objectMapper;
    private final UserController userController;
    private final Long idForUpdateAndDelete = 1L;
    private final Long idForNonExistenceUser = 10L;
    private MockMvc mockMvc;

    @Value(USER_AGE_PROPS)
    private int userAge;

    @Autowired
    public ClearSolutionsTestTaskApplicationTests(MockMvc mockMvc, ObjectMapper objectMapper, UserController userController) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.userController = userController;
    }

    @Test
    @Order(1)
    void testGetUserById() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(get(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(TEST_ID_FIELD, notNullValue()))
                .andExpect(jsonPath(TEST_EMAIL_FIELD, is(TEST_USER_EMAIL_1)))
                .andExpect(jsonPath(TEST_FIRST_NAME_FIELD, is(TEST_USER_FIRST_NAME_1)))
                .andExpect(jsonPath(TEST_LAST_NAME_FIELD, is(TEST_USER_LAST_NAME_1)))
                .andExpect(jsonPath(TEST_BIRTH_DATE_FIELD, is(TEST_USER_BIRTH_DATE_1)))
                .andExpect(jsonPath(TEST_ADDRESS_FIELD, is(TEST_USER_ADDRESS_1)))
                .andExpect(jsonPath(TEST_PHONE_NUMBER_FIELD, is(TEST_USER_PHONE_NUMBER_1)));

        checkLocationHeader(resultActions);
    }

    @Test
    @Order(2)
    void testGetNonExistenceUserById() throws Exception {
        mockMvc.perform(get(USERS_PATH + ID_PATH, idForNonExistenceUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertInstanceOf(UserNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals(USER_ID_NOT_FOUND + idForNonExistenceUser + DOT, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Order(3)
    void testGetUsersByBirthDateRange() throws Exception {
        mockMvc.perform(get(USERS_PATH)
                        .param(FROM_DATE, LocalDate.of(1992, 1, 1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .param(TO_DATE, LocalDate.of(1995, 1, 1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(DOLLAR_SIGN, hasSize(2)))
                .andExpect(jsonPath(TEST_ID_LIST_1_FIELD, is(1)))
                .andExpect(jsonPath(TEST_EMAIL_LIST_1_FIELD, is(TEST_USER_EMAIL_1)))
                .andExpect(jsonPath(TEST_FIRST_NAME_LIST_1_FIELD, is(TEST_USER_FIRST_NAME_1)))
                .andExpect(jsonPath(TEST_LAST_NAME_LIST_1_FIELD, is(TEST_USER_LAST_NAME_1)))
                .andExpect(jsonPath(TEST_BIRTH_DATE_LIST_1_FIELD, is(TEST_USER_BIRTH_DATE_1)))
                .andExpect(jsonPath(TEST_ADDRESS_LIST_1_FIELD, is(TEST_USER_ADDRESS_1)))
                .andExpect(jsonPath(TEST_PHONE_NUMBER_LIST_1_FIELD, is(TEST_USER_PHONE_NUMBER_1)))
                .andExpect(jsonPath(TEST_ID_LIST_2_FIELD, is(5)))
                .andExpect(jsonPath(TEST_EMAIL_LIST_2_FIELD, is(TEST_USER_EMAIL_3)))
                .andExpect(jsonPath(TEST_FIRST_NAME_LIST_2_FIELD, is(TEST_USER_FIRST_NAME_3)))
                .andExpect(jsonPath(TEST_LAST_NAME_LIST_2_FIELD, is(TEST_USER_LAST_NAME_3)))
                .andExpect(jsonPath(TEST_BIRTH_DATE_LIST_2_FIELD, is(TEST_USER_BIRTH_DATE_3)))
                .andExpect(jsonPath(TEST_ADDRESS_LIST_2_FIELD, is(TEST_USER_ADDRESS_3)))
                .andExpect(jsonPath(TEST_PHONE_NUMBER_LIST_2_FIELD, is(TEST_USER_PHONE_NUMBER_3)));
    }

    @Test
    @Order(4)
    void testGetUsersByBirthDateRangeCheckToDateBiggerThatFromDate() throws Exception {
        mockMvc.perform(get(USERS_PATH)
                        .param(FROM_DATE, LocalDate.of(1995, 1, 1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .param(TO_DATE, LocalDate.of(1992, 1, 1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertInstanceOf(BadDateException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals(DATE_MUST_BE_EARLIER, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Order(5)
    void testGetUsersByBirthDateRangeSameDate() throws Exception {
        mockMvc.perform(get(USERS_PATH)
                        .param(FROM_DATE, LocalDate.of(1992, 6, 15).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .param(TO_DATE, LocalDate.of(1992, 6, 15).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(DOLLAR_SIGN, hasSize(1)))
                .andExpect(jsonPath(TEST_ID_LIST_1_FIELD, is(1)))
                .andExpect(jsonPath(TEST_EMAIL_LIST_1_FIELD, is(TEST_USER_EMAIL_1)))
                .andExpect(jsonPath(TEST_FIRST_NAME_LIST_1_FIELD, is(TEST_USER_FIRST_NAME_1)))
                .andExpect(jsonPath(TEST_LAST_NAME_LIST_1_FIELD, is(TEST_USER_LAST_NAME_1)))
                .andExpect(jsonPath(TEST_BIRTH_DATE_LIST_1_FIELD, is(TEST_USER_BIRTH_DATE_1)))
                .andExpect(jsonPath(TEST_ADDRESS_LIST_1_FIELD, is(TEST_USER_ADDRESS_1)))
                .andExpect(jsonPath(TEST_PHONE_NUMBER_LIST_1_FIELD, is(TEST_USER_PHONE_NUMBER_1)));
    }

    @Test
    @Order(6)
    void testGetNonExistenceUsersByBirthDateRange() throws Exception {
        mockMvc.perform(get(USERS_PATH)
                        .param(FROM_DATE, LocalDate.of(2010, 1, 1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .param(TO_DATE, LocalDate.of(2011, 1, 1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertInstanceOf(UserNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals(USERS_NOT_FOUND, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Order(7)
    void testCreateUser() throws Exception {
        User user = new User(0, TEST_USER_EMAIL_2, TEST_USER_FIRST_NAME_2, TEST_USER_LAST_NAME_2,
                LocalDate.of(1999, 6, 24), TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        ResultActions resultActions = mockMvc.perform(post(USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(TEST_ID_FIELD, notNullValue()))
                .andExpect(jsonPath(TEST_EMAIL_FIELD, is(TEST_USER_EMAIL_2)))
                .andExpect(jsonPath(TEST_FIRST_NAME_FIELD, is(TEST_USER_FIRST_NAME_2)))
                .andExpect(jsonPath(TEST_LAST_NAME_FIELD, is(TEST_USER_LAST_NAME_2)))
                .andExpect(jsonPath(TEST_BIRTH_DATE_FIELD, is(TEST_USER_BIRTH_DATE_2)))
                .andExpect(jsonPath(TEST_ADDRESS_FIELD, is(TEST_USER_ADDRESS_2)))
                .andExpect(jsonPath(TEST_PHONE_NUMBER_FIELD, is(TEST_USER_PHONE_NUMBER_2)));

        checkLocationHeader(resultActions);
    }

    @Test
    @Order(8)
    void testCreateUserWithoutEmail() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        User user = new User(0, null, TEST_USER_FIRST_NAME_2, TEST_USER_LAST_NAME_2,
                LocalDate.of(1999, 6, 24), TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        mockMvc.perform(post(USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_EMAIL_FIELD, is(EMAIL_IS_MANDATORY)));
    }

    @Test
    @Order(9)
    void testCreateUserWithBlankEmail() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        User user = new User(0, EMPTY_STRING, TEST_USER_FIRST_NAME_2, TEST_USER_LAST_NAME_2,
                LocalDate.of(1999, 6, 24), TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        mockMvc.perform(post(USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_EMAIL_FIELD, is(EMAIL_IS_NOT_VALID)));
    }

    @Test
    @Order(10)
    void testCreateUserWithNotValidEmailWithoutAt() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        User user = new User(0, TEST_NOT_VALID_EMAIL_WITHOUT_AT_2, TEST_USER_FIRST_NAME_2, TEST_USER_LAST_NAME_2,
                LocalDate.of(1999, 6, 24), TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        mockMvc.perform(post(USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_EMAIL_FIELD, is(EMAIL_IS_NOT_VALID)));
    }

    @Test
    @Order(11)
    void testCreateUserWithNotValidEmailWithoutDot() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        User user = new User(0, TEST_NOT_VALID_EMAIL_WITHOUT_DOT_2, TEST_USER_FIRST_NAME_2, TEST_USER_LAST_NAME_2,
                LocalDate.of(1999, 6, 24), TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        mockMvc.perform(post(USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_EMAIL_FIELD, is(EMAIL_IS_NOT_VALID)));
    }

    @Test
    @Order(12)
    void testCreateUserWithoutFirstName() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        User user = new User(0, TEST_USER_EMAIL_2, null, TEST_USER_LAST_NAME_2,
                LocalDate.of(1999, 6, 24), TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        mockMvc.perform(post(USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_FIRST_NAME_FIELD, is(FIRST_NAME_IS_MANDATORY)));
    }

    @Test
    @Order(13)
    void testCreateUserWithBlankFirstName() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        User user = new User(0, TEST_USER_EMAIL_2, EMPTY_STRING, TEST_USER_LAST_NAME_2,
                LocalDate.of(1999, 6, 24), TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        mockMvc.perform(post(USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_FIRST_NAME_FIELD, is(FIRST_NAME_IS_MANDATORY)));
    }

    @Test
    @Order(14)
    void testCreateUserWithoutLastName() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        User user = new User(0, TEST_USER_EMAIL_2, TEST_USER_FIRST_NAME_2, null,
                LocalDate.of(1999, 6, 24), TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        mockMvc.perform(post(USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_LAST_NAME_FIELD, is(LAST_NAME_IS_MANDATORY)));
    }

    @Test
    @Order(15)
    void testCreateUserWithBlankLastName() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        User user = new User(0, TEST_USER_EMAIL_2, TEST_USER_FIRST_NAME_2, EMPTY_STRING,
                LocalDate.of(1999, 6, 24), TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        mockMvc.perform(post(USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_LAST_NAME_FIELD, is(LAST_NAME_IS_MANDATORY)));
    }

    @Test
    @Order(16)
    void testCreateUserWithoutBirthDate() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        User user = new User(0, TEST_USER_EMAIL_2, TEST_USER_FIRST_NAME_2, TEST_USER_LAST_NAME_2,
                null, TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        mockMvc.perform(post(USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_BIRTH_DATE_FIELD, is(BIRTH_DATE_IS_MANDATORY)));
    }

    @Test
    @Order(17)
    void testCreateUserWithTodayDate() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        User user = new User(0, TEST_USER_EMAIL_2, TEST_USER_FIRST_NAME_2, TEST_USER_LAST_NAME_2,
                LocalDate.now(), TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        mockMvc.perform(post(USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_BIRTH_DATE_FIELD, is(VALUE_MUST_BE_EARLIER_THAN_CURRENT_DATE)));
    }

    @Test
    @Order(18)
    void testCreateUserWithFutureDate() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        User user = new User(0, TEST_USER_EMAIL_2, TEST_USER_FIRST_NAME_2, TEST_USER_LAST_NAME_2,
                LocalDate.of(2066, 6, 24), TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        mockMvc.perform(post(USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_BIRTH_DATE_FIELD, is(VALUE_MUST_BE_EARLIER_THAN_CURRENT_DATE)));
    }

    @Test
    @Order(19)
    void testCreateUserWithTodayDateAndNotValidEmail() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        User user = new User(0, TEST_NOT_VALID_EMAIL_WITHOUT_AT_2, TEST_USER_FIRST_NAME_2, TEST_USER_LAST_NAME_2,
                LocalDate.now(), TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        mockMvc.perform(post(USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_BIRTH_DATE_FIELD, is(VALUE_MUST_BE_EARLIER_THAN_CURRENT_DATE)))
                .andExpect(jsonPath(TEST_EMAIL_FIELD, is(EMAIL_IS_NOT_VALID)));
    }

    @Test
    @Order(20)
    void testCreateUserWithoutAddressAndPhoneNumber() throws Exception {
        User user = new User(0, TEST_USER_EMAIL_2, TEST_USER_FIRST_NAME_2, TEST_USER_LAST_NAME_2,
                LocalDate.of(1999, 6, 24), null, null);

        ResultActions resultActions = mockMvc.perform(post(USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(TEST_ID_FIELD, notNullValue()))
                .andExpect(jsonPath(TEST_EMAIL_FIELD, is(TEST_USER_EMAIL_2)))
                .andExpect(jsonPath(TEST_FIRST_NAME_FIELD, is(TEST_USER_FIRST_NAME_2)))
                .andExpect(jsonPath(TEST_LAST_NAME_FIELD, is(TEST_USER_LAST_NAME_2)))
                .andExpect(jsonPath(TEST_BIRTH_DATE_FIELD, is(TEST_USER_BIRTH_DATE_2)))
                .andExpect(jsonPath(TEST_ADDRESS_FIELD, is(nullValue())))
                .andExpect(jsonPath(TEST_PHONE_NUMBER_FIELD, is(nullValue())));

        checkLocationHeader(resultActions);
    }

    @Test
    @Order(21)
    void testCreateUserUnderDefinedAge() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        User user = new User(0, TEST_USER_EMAIL_2, TEST_USER_FIRST_NAME_2, TEST_USER_LAST_NAME_2,
                LocalDate.of(2020, 6, 24), TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        mockMvc.perform(post(USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(UserAgeValidationException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals(IT_ALLOWS_TO_REGISTER + userAge + YEARS_OLD, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Order(22)
    void testUpdatePartialUserEmail() throws Exception {
        String body = "[{" +
                "\"op\": \"replace\"," +
                "\"path\": \"/email\"," +
                "\"value\": \"" + TEST_UPDATED_EMAIL_1 + "\"" +
                "}]";

        ResultActions resultActions = mockMvc.perform(patch(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON_VALUE + PATCH_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(TEST_ID_FIELD, notNullValue()))
                .andExpect(jsonPath(TEST_EMAIL_FIELD, is(TEST_UPDATED_EMAIL_1)))
                .andExpect(jsonPath(TEST_FIRST_NAME_FIELD, is(TEST_USER_FIRST_NAME_1)))
                .andExpect(jsonPath(TEST_LAST_NAME_FIELD, is(TEST_USER_LAST_NAME_1)))
                .andExpect(jsonPath(TEST_BIRTH_DATE_FIELD, is(TEST_USER_BIRTH_DATE_1)))
                .andExpect(jsonPath(TEST_ADDRESS_FIELD, is(TEST_USER_ADDRESS_1)))
                .andExpect(jsonPath(TEST_PHONE_NUMBER_FIELD, is(TEST_USER_PHONE_NUMBER_1)));

        checkLocationHeader(resultActions);
    }

    @Test
    @Order(23)
    void testUpdatePartialNonExistenceUser() throws Exception {
        String body = "[{" +
                "\"op\": \"replace\"," +
                "\"path\": \"/email\"," +
                "\"value\": \"" + TEST_UPDATED_EMAIL_1 + "\"" +
                "}]";

        mockMvc.perform(patch(USERS_PATH + ID_PATH, idForNonExistenceUser)
                        .contentType(MediaType.APPLICATION_JSON_VALUE + PATCH_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertInstanceOf(UserNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals(USER_ID_NOT_FOUND + idForNonExistenceUser + DOT, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Order(24)
    void testUpdatePartialUserAllFields() throws Exception {
        String body = "[{" +
                "\"op\": \"replace\"," +
                "\"path\": \"/email\"," +
                "\"value\": \"" + TEST_USER_EMAIL_2 + "\"" +
                "}," +
                "{" +
                "\"op\": \"replace\"," +
                "\"path\": \"/firstName\"," +
                "\"value\": \"" + TEST_USER_FIRST_NAME_2 + "\"" +
                "}," +
                "{" +
                "\"op\": \"replace\"," +
                "\"path\": \"/lastName\"," +
                "\"value\": \"" + TEST_USER_LAST_NAME_2 + "\"" +
                "}," +
                "{" +
                "\"op\": \"replace\"," +
                "\"path\": \"/birthDate\"," +
                "\"value\": \"" + TEST_USER_BIRTH_DATE_2 + "\"" +
                "}," +
                "{" +
                "\"op\": \"replace\"," +
                "\"path\": \"/address\"," +
                "\"value\": \"" + TEST_USER_ADDRESS_2 + "\"" +
                "}," +
                "{" +
                "\"op\": \"replace\"," +
                "\"path\": \"/phoneNumber\"," +
                "\"value\": \"" + TEST_USER_PHONE_NUMBER_2 + "\"" +
                "}]";

        ResultActions resultActions = mockMvc.perform(patch(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON_VALUE + PATCH_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(TEST_ID_FIELD, notNullValue()))
                .andExpect(jsonPath(TEST_EMAIL_FIELD, is(TEST_USER_EMAIL_2)))
                .andExpect(jsonPath(TEST_FIRST_NAME_FIELD, is(TEST_USER_FIRST_NAME_2)))
                .andExpect(jsonPath(TEST_LAST_NAME_FIELD, is(TEST_USER_LAST_NAME_2)))
                .andExpect(jsonPath(TEST_BIRTH_DATE_FIELD, is(TEST_USER_BIRTH_DATE_2)))
                .andExpect(jsonPath(TEST_ADDRESS_FIELD, is(TEST_USER_ADDRESS_2)))
                .andExpect(jsonPath(TEST_PHONE_NUMBER_FIELD, is(TEST_USER_PHONE_NUMBER_2)));

        checkLocationHeader(resultActions);
    }

    @Test
    @Order(25)
    void testUpdatePartialUserWithoutEmail() throws Exception {
        String body = "[{" +
                "\"op\": \"replace\"," +
                "\"path\": \"/email\"," +
                "\"value\": null" +
                "}]";

        mockMvc.perform(patch(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON_VALUE + PATCH_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_EMAIL_FIELD, is(EMAIL_IS_MANDATORY)));
    }

    @Test
    @Order(26)
    void testUpdatePartialUserWithBlankEmail() throws Exception {
        String body = "[{" +
                "\"op\": \"replace\"," +
                "\"path\": \"/email\"," +
                "\"value\": \"" + EMPTY_STRING + "\"" +
                "}]";

        mockMvc.perform(patch(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON_VALUE + PATCH_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_EMAIL_FIELD, is(EMAIL_IS_NOT_VALID)));
    }

    @Test
    @Order(27)
    void testUpdatePartialUserNotValidEmailWithoutAt() throws Exception {
        String body = "[{" +
                "\"op\": \"replace\"," +
                "\"path\": \"/email\"," +
                "\"value\": \"" + TEST_UPDATED_WRONG_EMAIL_WITHOUT_AT_1 + "\"" +
                "}]";

        mockMvc.perform(patch(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON_VALUE + PATCH_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_EMAIL_FIELD, is(EMAIL_IS_NOT_VALID)));
    }

    @Test
    @Order(28)
    void testUpdatePartialUserNotValidEmailWithoutDot() throws Exception {
        String body = "[{" +
                "\"op\": \"replace\"," +
                "\"path\": \"/email\"," +
                "\"value\": \"" + TEST_UPDATED_WRONG_EMAIL_WITHOUT_DOT_1 + "\"" +
                "}]";

        mockMvc.perform(patch(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON_VALUE + PATCH_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_EMAIL_FIELD, is(EMAIL_IS_NOT_VALID)));
    }

    @Test
    @Order(29)
    void testUpdatePartialUserWithoutFirstName() throws Exception {
        String body = "[{" +
                "\"op\": \"replace\"," +
                "\"path\": \"/firstName\"," +
                "\"value\": null" +
                "}]";

        mockMvc.perform(patch(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON_VALUE + PATCH_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_FIRST_NAME_FIELD, is(FIRST_NAME_IS_MANDATORY)));
    }

    @Test
    @Order(30)
    void testUpdatePartialUserWithBlankFirstName() throws Exception {
        String body = "[{" +
                "\"op\": \"replace\"," +
                "\"path\": \"/firstName\"," +
                "\"value\": \"" + EMPTY_STRING + "\"" +
                "}]";

        mockMvc.perform(patch(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON_VALUE + PATCH_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_FIRST_NAME_FIELD, is(FIRST_NAME_IS_MANDATORY)));
    }

    @Test
    @Order(31)
    void testUpdatePartialUserWithoutLastName() throws Exception {
        String body = "[{" +
                "\"op\": \"replace\"," +
                "\"path\": \"/lastName\"," +
                "\"value\": null" +
                "}]";

        mockMvc.perform(patch(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON_VALUE + PATCH_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_LAST_NAME_FIELD, is(LAST_NAME_IS_MANDATORY)));
    }

    @Test
    @Order(32)
    void testUpdatePartialUserWithBlankLastName() throws Exception {
        String body = "[{" +
                "\"op\": \"replace\"," +
                "\"path\": \"/lastName\"," +
                "\"value\": \"" + EMPTY_STRING + "\"" +
                "}]";

        mockMvc.perform(patch(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON_VALUE + PATCH_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_LAST_NAME_FIELD, is(LAST_NAME_IS_MANDATORY)));
    }

    @Test
    @Order(33)
    void testUpdatePartialUserWithoutBirthDate() throws Exception {
        String body = "[{" +
                "\"op\": \"replace\"," +
                "\"path\": \"/birthDate\"," +
                "\"value\": null" +
                "}]";

        mockMvc.perform(patch(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON_VALUE + PATCH_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_BIRTH_DATE_FIELD, is(BIRTH_DATE_IS_MANDATORY)));
    }

    @Test
    @Order(34)
    void testUpdatePartialUserWithNowBirthDate() throws Exception {
        String body = "[{" +
                "\"op\": \"replace\"," +
                "\"path\": \"/birthDate\"," +
                "\"value\": \"" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "\"" +
                "}]";

        mockMvc.perform(patch(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON_VALUE + PATCH_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_BIRTH_DATE_FIELD, is(VALUE_MUST_BE_EARLIER_THAN_CURRENT_DATE)));
    }

    @Test
    @Order(35)
    void testUpdatePartialUserWithFutureDate() throws Exception {
        String body = "[{" +
                "\"op\": \"replace\"," +
                "\"path\": \"/birthDate\"," +
                "\"value\": \"" + LocalDate.of(2066, 6, 24).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "\"" +
                "}]";

        mockMvc.perform(patch(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON_VALUE + PATCH_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_BIRTH_DATE_FIELD, is(VALUE_MUST_BE_EARLIER_THAN_CURRENT_DATE)));
    }

    @Test
    @Order(36)
    void testUpdatePartialUserWithoutAddressAndPhoneNumber() throws Exception {
        String body = "[{" +
                "\"op\": \"replace\"," +
                "\"path\": \"/address\"," +
                "\"value\": null" +
                "}," +
                "{" +
                "\"op\": \"replace\"," +
                "\"path\": \"/phoneNumber\"," +
                "\"value\": null" +
                "}]";

        ResultActions resultActions = mockMvc.perform(patch(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON_VALUE + PATCH_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(TEST_ID_FIELD, notNullValue()))
                .andExpect(jsonPath(TEST_EMAIL_FIELD, is(TEST_USER_EMAIL_1)))
                .andExpect(jsonPath(TEST_FIRST_NAME_FIELD, is(TEST_USER_FIRST_NAME_1)))
                .andExpect(jsonPath(TEST_LAST_NAME_FIELD, is(TEST_USER_LAST_NAME_1)))
                .andExpect(jsonPath(TEST_BIRTH_DATE_FIELD, is(TEST_USER_BIRTH_DATE_1)))
                .andExpect(jsonPath(TEST_ADDRESS_FIELD, is(nullValue())))
                .andExpect(jsonPath(TEST_PHONE_NUMBER_FIELD, is(nullValue())));

        checkLocationHeader(resultActions);
    }

    @Test
    @Order(37)
    void testUpdatePartialUserUnderSpecificAge() throws Exception {
        String body = "[{" +
                "\"op\": \"replace\"," +
                "\"path\": \"/birthDate\"," +
                "\"value\": \"" + LocalDate.of(2020, 6, 24).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "\"" +
                "}]";

        mockMvc.perform(patch(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON_VALUE + PATCH_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(UserAgeValidationException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals(IT_ALLOWS_TO_REGISTER + userAge + YEARS_OLD, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Order(38)
    void testUpdateUser() throws Exception {
        User user = new User(idForUpdateAndDelete, TEST_USER_EMAIL_2, TEST_USER_FIRST_NAME_2, TEST_USER_LAST_NAME_2,
                LocalDate.of(1999, 6, 24), TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        ResultActions resultActions = mockMvc.perform(put(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(TEST_ID_FIELD, notNullValue()))
                .andExpect(jsonPath(TEST_EMAIL_FIELD, is(TEST_USER_EMAIL_2)))
                .andExpect(jsonPath(TEST_FIRST_NAME_FIELD, is(TEST_USER_FIRST_NAME_2)))
                .andExpect(jsonPath(TEST_LAST_NAME_FIELD, is(TEST_USER_LAST_NAME_2)))
                .andExpect(jsonPath(TEST_BIRTH_DATE_FIELD, is(TEST_USER_BIRTH_DATE_2)))
                .andExpect(jsonPath(TEST_ADDRESS_FIELD, is(TEST_USER_ADDRESS_2)))
                .andExpect(jsonPath(TEST_PHONE_NUMBER_FIELD, is(TEST_USER_PHONE_NUMBER_2)));

        checkLocationHeader(resultActions);
    }

    @Test
    @Order(39)
    void testUpdateUserWithoutEmail() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        User user = new User(idForUpdateAndDelete, null, TEST_USER_FIRST_NAME_2, TEST_USER_LAST_NAME_2,
                LocalDate.of(1999, 6, 24), TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        mockMvc.perform(put(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_EMAIL_FIELD, is(EMAIL_IS_MANDATORY)));
    }

    @Test
    @Order(40)
    void testUpdateUserWithBlankEmail() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        User user = new User(idForUpdateAndDelete, EMPTY_STRING, TEST_USER_FIRST_NAME_2, TEST_USER_LAST_NAME_2,
                LocalDate.of(1999, 6, 24), TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        mockMvc.perform(put(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_EMAIL_FIELD, is(EMAIL_IS_NOT_VALID)));
    }

    @Test
    @Order(41)
    void testUpdateUserWithNotValidEmailWithoutAt() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        User user = new User(idForUpdateAndDelete, TEST_NOT_VALID_EMAIL_WITHOUT_AT_2, TEST_USER_FIRST_NAME_2, TEST_USER_LAST_NAME_2,
                LocalDate.of(1999, 6, 24), TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        mockMvc.perform(put(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_EMAIL_FIELD, is(EMAIL_IS_NOT_VALID)));
    }

    @Test
    @Order(42)
    void testUpdateUserWithNotValidEmailWithoutDot() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        User user = new User(idForUpdateAndDelete, TEST_NOT_VALID_EMAIL_WITHOUT_DOT_2, TEST_USER_FIRST_NAME_2, TEST_USER_LAST_NAME_2,
                LocalDate.of(1999, 6, 24), TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        mockMvc.perform(put(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_EMAIL_FIELD, is(EMAIL_IS_NOT_VALID)));
    }

    @Test
    @Order(43)
    void testUpdateUserWithoutFirstName() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        User user = new User(idForUpdateAndDelete, TEST_USER_EMAIL_2, null, TEST_USER_LAST_NAME_2,
                LocalDate.of(1999, 6, 24), TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        mockMvc.perform(put(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_FIRST_NAME_FIELD, is(FIRST_NAME_IS_MANDATORY)));
    }

    @Test
    @Order(44)
    void testUpdateUserWithBlankFirstName() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        User user = new User(idForUpdateAndDelete, TEST_USER_EMAIL_2, EMPTY_STRING, TEST_USER_LAST_NAME_2,
                LocalDate.of(1999, 6, 24), TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        mockMvc.perform(put(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_FIRST_NAME_FIELD, is(FIRST_NAME_IS_MANDATORY)));
    }

    @Test
    @Order(45)
    void testUpdateUserWithoutLastName() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        User user = new User(idForUpdateAndDelete, TEST_USER_EMAIL_2, TEST_USER_FIRST_NAME_2, null,
                LocalDate.of(1999, 6, 24), TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        mockMvc.perform(put(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_LAST_NAME_FIELD, is(LAST_NAME_IS_MANDATORY)));
    }

    @Test
    @Order(46)
    void testUpdateUserWithBlankLastName() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        User user = new User(idForUpdateAndDelete, TEST_USER_EMAIL_2, TEST_USER_FIRST_NAME_2, EMPTY_STRING,
                LocalDate.of(1999, 6, 24), TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        mockMvc.perform(put(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_LAST_NAME_FIELD, is(LAST_NAME_IS_MANDATORY)));
    }

    @Test
    @Order(47)
    void testUpdateUserWithoutBirthDate() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        User user = new User(idForUpdateAndDelete, TEST_USER_EMAIL_2, TEST_USER_FIRST_NAME_2, TEST_USER_LAST_NAME_2,
                null, TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        mockMvc.perform(put(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_BIRTH_DATE_FIELD, is(BIRTH_DATE_IS_MANDATORY)));
    }

    @Test
    @Order(48)
    void testUpdateUserWithTodayDate() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        User user = new User(idForUpdateAndDelete, TEST_USER_EMAIL_2, TEST_USER_FIRST_NAME_2, TEST_USER_LAST_NAME_2,
                LocalDate.now(), TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        mockMvc.perform(put(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_BIRTH_DATE_FIELD, is(VALUE_MUST_BE_EARLIER_THAN_CURRENT_DATE)));
    }

    @Test
    @Order(49)
    void testUpdateUserWithFutureDate() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        User user = new User(idForUpdateAndDelete, TEST_USER_EMAIL_2, TEST_USER_FIRST_NAME_2, TEST_USER_LAST_NAME_2,
                LocalDate.of(2066, 6, 24), TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        mockMvc.perform(put(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_BIRTH_DATE_FIELD, is(VALUE_MUST_BE_EARLIER_THAN_CURRENT_DATE)));
    }

    @Test
    @Order(50)
    void testUpdateUserWithTodayDateAndNotValidEmail() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        User user = new User(idForUpdateAndDelete, TEST_NOT_VALID_EMAIL_WITHOUT_AT_2, TEST_USER_FIRST_NAME_2, TEST_USER_LAST_NAME_2,
                LocalDate.now(), TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        mockMvc.perform(put(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_BIRTH_DATE_FIELD, is(VALUE_MUST_BE_EARLIER_THAN_CURRENT_DATE)))
                .andExpect(jsonPath(TEST_EMAIL_FIELD, is(EMAIL_IS_NOT_VALID)));
    }

    @Test
    @Order(51)
    void testUpdateUserWithoutAddressAndPhoneNumber() throws Exception {
        User user = new User(idForUpdateAndDelete, TEST_USER_EMAIL_2, TEST_USER_FIRST_NAME_2, TEST_USER_LAST_NAME_2,
                LocalDate.of(1999, 6, 24), null, null);

        ResultActions resultActions = mockMvc.perform(put(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(TEST_ID_FIELD, notNullValue()))
                .andExpect(jsonPath(TEST_EMAIL_FIELD, is(TEST_USER_EMAIL_2)))
                .andExpect(jsonPath(TEST_FIRST_NAME_FIELD, is(TEST_USER_FIRST_NAME_2)))
                .andExpect(jsonPath(TEST_LAST_NAME_FIELD, is(TEST_USER_LAST_NAME_2)))
                .andExpect(jsonPath(TEST_BIRTH_DATE_FIELD, is(TEST_USER_BIRTH_DATE_2)))
                .andExpect(jsonPath(TEST_ADDRESS_FIELD, is(nullValue())))
                .andExpect(jsonPath(TEST_PHONE_NUMBER_FIELD, is(nullValue())));

        checkLocationHeader(resultActions);
    }

    @Test
    @Order(52)
    void testUpdateUserUnderDefinedAge() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        User user = new User(idForUpdateAndDelete, TEST_USER_EMAIL_2, TEST_USER_FIRST_NAME_2, TEST_USER_LAST_NAME_2,
                LocalDate.of(2020, 6, 24), TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        mockMvc.perform(put(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(UserAgeValidationException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals(IT_ALLOWS_TO_REGISTER + userAge + YEARS_OLD, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Order(53)
    void testUpdateNonExistenceUser() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        User user = new User(idForUpdateAndDelete, TEST_USER_EMAIL_2, TEST_USER_FIRST_NAME_2, TEST_USER_LAST_NAME_2,
                LocalDate.of(1991, 6, 24), TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        mockMvc.perform(put(USERS_PATH + ID_PATH, idForNonExistenceUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertInstanceOf(UserNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals(USER_ID_NOT_FOUND + idForNonExistenceUser + DOT, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Order(54)
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().string(containsString(DELETED_USER + idForUpdateAndDelete + DOT)));

        mockMvc.perform(get(USERS_PATH + ID_PATH, idForUpdateAndDelete)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertInstanceOf(UserNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals(USER_ID_NOT_FOUND + idForUpdateAndDelete + DOT, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Order(55)
    void testDeleteNonExistenceUser() throws Exception {
        mockMvc.perform(delete(USERS_PATH + ID_PATH, idForNonExistenceUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertInstanceOf(UserNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals(USER_ID_NOT_FOUND + idForNonExistenceUser + DOT, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    private void checkLocationHeader(ResultActions resultActions) {
        String locationHeader = resultActions.andReturn().getResponse().getHeader(LOCATION);
        int generatedId = 0;
        if (locationHeader != null) {
            generatedId = extractIdFromLocation(locationHeader);
        }
        String expectedLocation = USERS_PATH + SLASH + generatedId;
        assertThat(locationHeader).isEqualTo(expectedLocation);
    }

    private int extractIdFromLocation(String locationHeader) {
        String[] parts = locationHeader.split(SLASH);
        if (parts.length > 0) {
            String lastPart = parts[parts.length - 1];
            try {
                return Integer.parseInt(lastPart);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(INVALID_LOCATION_HEADER + locationHeader);
            }
        } else {
            throw new IllegalArgumentException(INVALID_LOCATION_HEADER + locationHeader);
        }
    }
}
