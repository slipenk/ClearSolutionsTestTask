package com.slipenk.clearsolutionstesttask.service;

import com.slipenk.clearsolutionstesttask.entity.User;
import com.slipenk.clearsolutionstesttask.exceptions.BadDateException;
import com.slipenk.clearsolutionstesttask.exceptions.UserAgeValidationException;
import com.slipenk.clearsolutionstesttask.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.DATE_MUST_BE_EARLIER;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.IT_ALLOWS_TO_REGISTER;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.USER_AGE_PROPS;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.YEARS_OLD;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Value(USER_AGE_PROPS)
    private int userAge;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

}
