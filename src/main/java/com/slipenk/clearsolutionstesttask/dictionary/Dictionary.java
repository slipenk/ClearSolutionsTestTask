package com.slipenk.clearsolutionstesttask.dictionary;

public class Dictionary {

    //additional needed values
    public static final String SLASH = "/";
    public static final String DOT = ".";
    public static final String EMPTY_STRING = " ";
    public static final String PATCH_JSON = "-patch+json";
    public static final String USER = "user";
    public static final String LOCATION = "Location";
    public static final String RESULT = "result";
    public static final String FROM_DATE = "fromDate";
    public static final String TO_DATE = "toDate";

    //for validation
    public static final String VALIDATE_USER = "validateUser";

    //values from properties files
    public static final String USER_AGE_PROPS = "${user.age}";

    //regex
    public static final String REGEX_FOR_EMAIL = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    //table names
    public static final String USERS = "users";

    //attributes
    public static final String ID = "id";
    public static final String EMAIL = "email";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String BIRTH_DATE = "birth_date";
    public static final String ADDRESS = "address";
    public static final String PHONE_NUMBER = "phone_number";

    //messages
    public static final String EMAIL_IS_MANDATORY = "Email is mandatory.";
    public static final String EMAIL_IS_NOT_VALID = "Email is not valid.";
    public static final String FIRST_NAME_IS_MANDATORY = "First name is mandatory.";
    public static final String LAST_NAME_IS_MANDATORY = "Last name is mandatory.";
    public static final String BIRTH_DATE_IS_MANDATORY = "Birth date is mandatory.";
    public static final String VALUE_MUST_BE_EARLIER_THAN_CURRENT_DATE = "Value must be earlier than current date";
    public static final String IT_ALLOWS_TO_REGISTER = "It allows to register users who are more than ";
    public static final String YEARS_OLD = " years old.";
    public static final String USER_ID_NOT_FOUND = "User id not found - ";
    public static final String USERS_NOT_FOUND = "Users not found.";
    public static final String DELETED_USER = "Deleted user, id - ";
    public static final String DATE_MUST_BE_EARLIER = "The first date must be before the second date.";
    public static final String INVALID_LOCATION_HEADER = "Invalid location header: ";
    public static final String BEFORE_METHOD = "=====>> Calling method: ";
    public static final String ARGUMENT = "=====>> Argument: ";
    public static final String RETURN_FROM_METHOD = "=====>> Return from method: ";
    public static final String RESULT_MESSAGE = "=====>> Result: ";

    //paths
    public static final String USERS_PATH = SLASH + USERS;
    public static final String ID_PATH = SLASH + "{id}";

    //for tests
    public static final String TEST_ID_FIELD = "$.id";
    public static final String TEST_EMAIL_FIELD = "$.email";
    public static final String TEST_FIRST_NAME_FIELD = "$.firstName";
    public static final String TEST_LAST_NAME_FIELD = "$.lastName";
    public static final String TEST_BIRTH_DATE_FIELD = "$.birthDate";
    public static final String TEST_ADDRESS_FIELD = "$.address";
    public static final String TEST_PHONE_NUMBER_FIELD = "$.phoneNumber";
    public static final String TEST_ID_LIST_1_FIELD = "$[0].id";
    public static final String TEST_EMAIL_LIST_1_FIELD = "$[0].email";
    public static final String TEST_FIRST_NAME_LIST_1_FIELD = "$[0].firstName";
    public static final String TEST_LAST_NAME_LIST_1_FIELD = "$[0].lastName";
    public static final String TEST_BIRTH_DATE_LIST_1_FIELD = "$[0].birthDate";
    public static final String TEST_ADDRESS_LIST_1_FIELD = "$[0].address";
    public static final String TEST_PHONE_NUMBER_LIST_1_FIELD = "$[0].phoneNumber";
    public static final String TEST_ID_LIST_2_FIELD = "$[1].id";
    public static final String TEST_EMAIL_LIST_2_FIELD = "$[1].email";
    public static final String TEST_FIRST_NAME_LIST_2_FIELD = "$[1].firstName";
    public static final String TEST_LAST_NAME_LIST_2_FIELD = "$[1].lastName";
    public static final String TEST_BIRTH_DATE_LIST_2_FIELD = "$[1].birthDate";
    public static final String TEST_ADDRESS_LIST_2_FIELD = "$[1].address";
    public static final String TEST_PHONE_NUMBER_LIST_2_FIELD = "$[1].phoneNumber";
    public static final String DOLLAR_SIGN = "$";

    public static final String TEST_USER_EMAIL_1 = "salah@gmail.com";
    public static final String TEST_UPDATED_EMAIL_1 = "salah92@gmail.com";
    public static final String TEST_UPDATED_WRONG_EMAIL_WITHOUT_AT_1 = "salah92gmail.com";
    public static final String TEST_UPDATED_WRONG_EMAIL_WITHOUT_DOT_1 = "salah92@gmailcom";
    public static final String TEST_USER_FIRST_NAME_1 = "Mohamed";
    public static final String TEST_USER_LAST_NAME_1 = "Salah";
    public static final String TEST_USER_BIRTH_DATE_1 = "1992-06-15";
    public static final String TEST_USER_ADDRESS_1 = "Egypt";
    public static final String TEST_USER_PHONE_NUMBER_1 = "+380934578612";
    public static final String TEST_USER_EMAIL_2 = "nunez@gmail.com";
    public static final String TEST_USER_FIRST_NAME_2 = "Darwin";
    public static final String TEST_USER_LAST_NAME_2 = "Nunez";
    public static final String TEST_USER_BIRTH_DATE_2 = "1999-06-24";
    public static final String TEST_USER_ADDRESS_2 = "Uruguay";
    public static final String TEST_USER_PHONE_NUMBER_2 = "+380998778612";
    public static final String TEST_NOT_VALID_EMAIL_WITHOUT_AT_2 = "nunezgmail.com";
    public static final String TEST_NOT_VALID_EMAIL_WITHOUT_DOT_2 = "nunez@gmailcom";
    public static final String TEST_USER_EMAIL_3 = "robertson@gmail.com";
    public static final String TEST_USER_FIRST_NAME_3 = "Andrew";
    public static final String TEST_USER_LAST_NAME_3 = "Robertson";
    public static final String TEST_USER_BIRTH_DATE_3 = "1994-03-11";
    public static final String TEST_USER_ADDRESS_3 = "Scotland";
    public static final String TEST_USER_PHONE_NUMBER_3 = "+380934511114";

    private Dictionary() {
    }
}
