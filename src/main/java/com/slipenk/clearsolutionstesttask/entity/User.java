package com.slipenk.clearsolutionstesttask.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.ADDRESS;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.BIRTH_DATE;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.BIRTH_DATE_IS_MANDATORY;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.EMAIL;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.EMAIL_IS_MANDATORY;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.EMAIL_IS_NOT_VALID;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.FIRST_NAME;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.FIRST_NAME_IS_MANDATORY;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.ID;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.LAST_NAME;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.LAST_NAME_IS_MANDATORY;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.PHONE_NUMBER;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.REGEX_FOR_EMAIL;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.USERS;
import static com.slipenk.clearsolutionstesttask.dictionary.Dictionary.VALUE_MUST_BE_EARLIER_THAN_CURRENT_DATE;

@Entity
@Table(name = USERS)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID, nullable = false, unique = true)
    private long id;

    @NotNull(message = EMAIL_IS_MANDATORY)
    @NotBlank(message = EMAIL_IS_MANDATORY)
    @Email(message = EMAIL_IS_NOT_VALID, regexp = REGEX_FOR_EMAIL)
    @Column(name = EMAIL, nullable = false)
    private String email;

    @NotNull(message = FIRST_NAME_IS_MANDATORY)
    @NotBlank(message = FIRST_NAME_IS_MANDATORY)
    @Column(name = FIRST_NAME, nullable = false)
    private String firstName;

    @NotNull(message = LAST_NAME_IS_MANDATORY)
    @NotBlank(message = LAST_NAME_IS_MANDATORY)
    @Column(name = LAST_NAME, nullable = false)
    private String lastName;

    @NotNull(message = BIRTH_DATE_IS_MANDATORY)
    @Past(message = VALUE_MUST_BE_EARLIER_THAN_CURRENT_DATE)
    @Column(name = BIRTH_DATE, nullable = false)
    private LocalDate birthDate;

    @Column(name = ADDRESS)
    private String address;

    @Column(name = PHONE_NUMBER)
    private String phoneNumber;
}
