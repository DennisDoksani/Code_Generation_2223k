Feature: Everything to do with login/auth

  Scenario: Login with valid credentials
    Given I have a valid login object with valid email and valid password
    When I call the login endpoint
    Then I get http status 200
    And I receive a login token response

  Scenario: Login with invalid email and invalid password
    Given I have an invalid email and invalid password
    When I call the login endpoint
    Then I get http status 401
    And I receive an error message: "Invalid username/password"

  Scenario: Login with invalid email and valid password
    Given I have an invalid email and valid password
    When I call the login endpoint
    Then I get http status 401
    And I receive an error message: "Invalid username/password"

  Scenario: Login with valid email but invalid password
    Given I have a valid email but invalid password
    When I call the login endpoint
    Then I get http status 401
    And I receive an error message: "Invalid username/password"

  Scenario: Login with valid email but empty password
    Given I have a valid email but empty password
    When I call the login endpoint
    Then I get http status 400
    And I receive an error message: "Password is required."

  Scenario: Login with empty email but valid password
    Given I have an empty email but valid password
    When I call the login endpoint
    Then I get http status 400
    And I receive an error message: "Email is required."

  Scenario: Login with invalid email format but valid password
    Given I have an invalid email format but valid password
    When I call the login endpoint
    Then I get http status 400
    And I receive an error message: "Email is invalid."