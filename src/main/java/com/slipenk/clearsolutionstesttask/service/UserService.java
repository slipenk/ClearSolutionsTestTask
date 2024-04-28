package com.slipenk.clearsolutionstesttask.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.slipenk.clearsolutionstesttask.entity.User;
import com.slipenk.clearsolutionstesttask.exceptions.BadDateException;
import com.slipenk.clearsolutionstesttask.exceptions.UserAgeValidationException;
import com.slipenk.clearsolutionstesttask.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.DATE_MUST_BE_EARLIER;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.IT_ALLOWS_TO_REGISTER;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.USER;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.USER_AGE_PROPS;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.VALIDATE_USER;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.YEARS_OLD;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    @Value(USER_AGE_PROPS)
    private int userAge;

    @Autowired
    public UserService(UserRepository userRepository, ObjectMapper objectMapper, Validator validator) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    @Transactional(readOnly = true)
    public List<User> getAllUserWithinDateRange(LocalDate fromDate, LocalDate toDate) {
        checkTwoDates(fromDate, toDate);
        return userRepository.findByBirthDateBetween(fromDate, toDate);
    }

    private void checkTwoDates(LocalDate fromDate, LocalDate toDate) {
        if (!fromDate.isBefore(toDate)) {
            throw new BadDateException(DATE_MUST_BE_EARLIER);
        }
    }

    @Transactional
    public User addUser(User user) {
        return saveUser(user);
    }

    private User saveUser(User user) {
        if (checkUserAge(user)) {
            return userRepository.save(user);
        } else {
            throw new UserAgeValidationException(IT_ALLOWS_TO_REGISTER + userAge + YEARS_OLD);
        }
    }

    private boolean checkUserAge(User user) {
        LocalDate currentDate = LocalDate.now();
        Period age = Period.between(user.getBirthDate(), currentDate);
        return age.getYears() >= userAge;
    }

    @Transactional(readOnly = true)
    public User findById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    public User applyPatchToCustomer(JsonPatch patch, User userPatched) throws JsonPatchException, JsonProcessingException, MethodArgumentNotValidException, NoSuchMethodException {
        JsonNode patched = patch.apply(objectMapper.convertValue(userPatched, JsonNode.class));
        User user = objectMapper.treeToValue(patched, User.class);
        validateUser(user);
        return saveUser(user);
    }

    private void validateUser(User user) throws MethodArgumentNotValidException, NoSuchMethodException {
        BindingResult bindingResult = new BeanPropertyBindingResult(user, USER);
        validator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            MethodParameter parameter = new MethodParameter(this.getClass().getDeclaredMethod(VALIDATE_USER, User.class), 0);
            throw new MethodArgumentNotValidException(parameter, bindingResult);
        }
    }

}
