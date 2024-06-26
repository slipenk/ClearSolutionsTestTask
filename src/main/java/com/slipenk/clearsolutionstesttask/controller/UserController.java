package com.slipenk.clearsolutionstesttask.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.slipenk.clearsolutionstesttask.entity.User;
import com.slipenk.clearsolutionstesttask.exceptions.UserNotFoundException;
import com.slipenk.clearsolutionstesttask.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.DELETED_USER;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.DOT;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.ID_PATH;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.LOCATION;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.SLASH;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.USERS_NOT_FOUND;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.USERS_PATH;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.USER_ID_NOT_FOUND;

@RestController
@RequestMapping(USERS_PATH)
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(ID_PATH)
    public ResponseEntity<User> getUser(@PathVariable long id) {
        User ourUser = userService.findById(id);
        if (ourUser == null) {
            throw new UserNotFoundException(USER_ID_NOT_FOUND + id + DOT);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .header(LOCATION, USERS_PATH + SLASH + ourUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(ourUser);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUserWithinDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        List<User> users = userService.getAllUserWithinDateRange(fromDate, toDate);
        if (users.isEmpty()) {
            throw new UserNotFoundException(USERS_NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(users);
    }

    @PostMapping
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        user.setId(0);
        User newUser = userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(LOCATION, USERS_PATH + SLASH + newUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(newUser);
    }

    @PutMapping(ID_PATH)
    public ResponseEntity<User> updateUser(@PathVariable long id, @Valid @RequestBody User user) {
        checkUserPresence(id);
        user.setId(id);
        User updatedUser = userService.addUser(user);
        return ResponseEntity.status(HttpStatus.OK)
                .header(LOCATION, USERS_PATH + SLASH + updatedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(updatedUser);
    }

    @PatchMapping(ID_PATH)
    public ResponseEntity<User> updateUserSomeFields(@PathVariable long id, @RequestBody JsonPatch patch) throws JsonPatchException, JsonProcessingException, MethodArgumentNotValidException, NoSuchMethodException {
        User user = userService.findById(id);
        checkUserPresence(id);
        User patchedUser = userService.applyPatchToCustomer(patch, user);
        return ResponseEntity.status(HttpStatus.OK)
                .header(LOCATION, USERS_PATH + SLASH + patchedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(patchedUser);
    }

    @DeleteMapping(ID_PATH)
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        checkUserPresence(id);
        userService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(DELETED_USER + id + DOT);
    }

    private void checkUserPresence(long id) {
        User tempUser = userService.findById(id);
        if (tempUser == null) {
            throw new UserNotFoundException(USER_ID_NOT_FOUND + id + DOT);
        }
    }
}
