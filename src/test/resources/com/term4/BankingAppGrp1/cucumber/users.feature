Feature: Everything to do with users

  Scenario:
    Given I am a visitor without credentials
    And I have filled in the registration form with valid information
    When I call the registration endpoint
    Then I receive http status 201
    And a new user is returned

  Scenario:
    Given I am a visitor without credentials
    And I have filled in the registration form with valid information
    When I call the registration endpoint
    Then I receive http status 409
    And I receive error message: "This e-mail is already in use."

  Scenario:
    Given I am a visitor without credentials
    And I have filled in the registration form with an invalid BSN
    When I call the registration endpoint
    Then I receive http status 400
    And I receive error message: "BSN is invalid."

  Scenario:
    Given I am a visitor without credentials
    And I have filled in the registration form with an invalid email
    When I call the registration endpoint
    Then I receive http status 400
    And I receive error message: "Email is invalid."

  Scenario:
    Given I am a visitor without credentials
    And I have filled in the registration form with an invalid email
    When I call the registration endpoint
    Then I receive http status 400
    And I receive error message: "Email is invalid."

  Scenario:
    Given I am a visitor without credentials
    And I have filled in the registration form with an invalid phone number
    When I call the registration endpoint
    Then I receive http status 400
    And I receive error message: "Phone number is invalid."

  Scenario:
    Given I am a visitor without credentials
    And I have filled in the registration form but I am younger than 18 years old
    When I call the registration endpoint
    Then I receive http status 400
    And I receive error message: "User must be at least 18 years old."