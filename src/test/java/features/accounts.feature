Feature: Everything to do with Accounts endpoint
  *@Description: This is the Accounts endpoint test suite only employee and Customer are role

  Scenario: getting all Accounts from the database
    Given I log in as an "employee"
    When I send a GET request to "accounts"
    Then the response status code should be 200
    And the response should be an array of objects

  Scenario: getting a single account from the database
    Given I log in as an "employee"
    When I send a GET request to "accounts/1"
    Then the response status code should be 200
    And the response should be an object with an "id" property
