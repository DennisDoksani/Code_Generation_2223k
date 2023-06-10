Feature: Everything to do with Accounts enpoint
 * @Description: This is the Accounts endpoint test suite

  Scenario: getting all Accounts from the database
    Given The endpoint for "accounts" is available for method "GET"
    When I send a GET request to "accounts"
    Then the response status code should be 200
    And the response should be an array of objects
    And the response should be an array of objects containing all accounts
