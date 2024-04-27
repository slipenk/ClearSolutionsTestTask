package com.slipenk.clearsolutionstesttask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slipenk.clearsolutionstesttask.controller.UserController;
import com.slipenk.clearsolutionstesttask.entity.User;
import com.slipenk.clearsolutionstesttask.exceptions.RestExceptionHandler;
import com.slipenk.clearsolutionstesttask.exceptions.UserAgeValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDate;
import java.util.Objects;

import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.BIRTH_DATE_IS_MANDATORY;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.EMAIL_IS_MANDATORY;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.EMAIL_IS_NOT_VALID;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.EMPTY_STRING;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.FIRST_NAME_IS_MANDATORY;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.ID_PATH;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.INVALID_LOCATION_HEADER;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.IT_ALLOWS_TO_REGISTER;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.LAST_NAME_IS_MANDATORY;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.LOCATION;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.SLASH;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_ADDRESS_FIELD;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_BIRTH_DATE_FIELD;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_EMAIL_FIELD;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_FIRST_NAME_FIELD;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_ID_FIELD;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_LAST_NAME_FIELD;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_NOT_VALID_EMAIL_WITHOUT_AT;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_NOT_VALID_EMAIL_WITHOUT_DOT;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_PHONE_NUMBER_FIELD;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_USER_ADDRESS_1;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_USER_ADDRESS_2;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_USER_BIRTH_DATE_1;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_USER_BIRTH_DATE_2;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_USER_EMAIL_1;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_USER_EMAIL_2;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_USER_FIRST_NAME_1;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_USER_FIRST_NAME_2;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_USER_LAST_NAME_1;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_USER_LAST_NAME_2;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_USER_PHONE_NUMBER_1;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.TEST_USER_PHONE_NUMBER_2;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.USERS_PATH;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.USER_AGE_PROPS;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.VALUE_MUST_BE_EARLIER_THAN_CURRENT_DATE;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.YEARS_OLD;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class ClearSolutionsTestTaskApplicationTests {

    private final ObjectMapper objectMapper;
    private final UserController userController;
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
    void getUserById() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(get(USERS_PATH + ID_PATH, 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(TEST_ID_FIELD, notNullValue()))
                .andExpect(jsonPath(TEST_EMAIL_FIELD, is(TEST_USER_EMAIL_1)))
                .andExpect(jsonPath(TEST_FIRST_NAME_FIELD, is(TEST_USER_FIRST_NAME_1)))
                .andExpect(jsonPath(TEST_LAST_NAME_FIELD, is(TEST_USER_LAST_NAME_1)))
                .andExpect(jsonPath(TEST_BIRTH_DATE_FIELD, is(TEST_USER_BIRTH_DATE_1)))
                .andExpect(jsonPath(TEST_ADDRESS_FIELD, is(TEST_USER_ADDRESS_1)))
                .andExpect(jsonPath(TEST_PHONE_NUMBER_FIELD, is(TEST_USER_PHONE_NUMBER_1)));

        String locationHeader = resultActions.andReturn().getResponse().getHeader(LOCATION);
        int generatedId = 0;
        if (locationHeader != null) {
            generatedId = extractIdFromLocation(locationHeader);
        }
        String expectedLocation = USERS_PATH + SLASH + generatedId;
        assertThat(locationHeader).isEqualTo(expectedLocation);
    }

    @Test
    void createUser() throws Exception {
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

        String locationHeader = resultActions.andReturn().getResponse().getHeader(LOCATION);
        int generatedId = 0;
        if (locationHeader != null) {
            generatedId = extractIdFromLocation(locationHeader);
        }
        String expectedLocation = USERS_PATH + SLASH + generatedId;
        assertThat(locationHeader).isEqualTo(expectedLocation);
    }

    @Test
    void addUserWithoutEmail() throws Exception {
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
    void addUserWithBlankEmail() throws Exception {
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
    void addUserWithNotValidEmailWithoutAt() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        User user = new User(0, TEST_NOT_VALID_EMAIL_WITHOUT_AT, TEST_USER_FIRST_NAME_2, TEST_USER_LAST_NAME_2,
                LocalDate.of(1999, 6, 24), TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        mockMvc.perform(post(USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_EMAIL_FIELD, is(EMAIL_IS_NOT_VALID)));
    }

    @Test
    void addUserWithNotValidEmailWithoutDot() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        User user = new User(0, TEST_NOT_VALID_EMAIL_WITHOUT_DOT, TEST_USER_FIRST_NAME_2, TEST_USER_LAST_NAME_2,
                LocalDate.of(1999, 6, 24), TEST_USER_ADDRESS_2, TEST_USER_PHONE_NUMBER_2);

        mockMvc.perform(post(USERS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath(TEST_EMAIL_FIELD, is(EMAIL_IS_NOT_VALID)));
    }

    @Test
    void addUserWithoutFirstName() throws Exception {
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
    void addUserWithBlankFirstName() throws Exception {
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
    void addUserWithoutLastName() throws Exception {
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
    void addUserWithBlankLastName() throws Exception {
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
    void addUserWithoutBirthDate() throws Exception {
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
    void addUserWithTodayDate() throws Exception {
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
    void addUserWithFutureDate() throws Exception {
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
    void createUserWithoutAddressAndPhoneNumber() throws Exception {
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

        String locationHeader = resultActions.andReturn().getResponse().getHeader(LOCATION);
        int generatedId = 0;
        if (locationHeader != null) {
            generatedId = extractIdFromLocation(locationHeader);
        }
        String expectedLocation = USERS_PATH + SLASH + generatedId;
        assertThat(locationHeader).isEqualTo(expectedLocation);
    }

    @Test
    void createUserUnder18() throws Exception {
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
