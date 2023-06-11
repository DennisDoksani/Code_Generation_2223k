Feature: Everything to do with Accounts endpoint
#  *@Description: This is the Accounts endpoint test suite only employee and Customer are role

  Scenario: getting all Accounts from the database
    Given I log in as an "employee"
    When I send a GET request to "accounts"
    Then the response status code should be 200
    And the response should be an array of objects

  Scenario: getting a single account from the database
    Given I log in as an "employee"
    When I send a GET request to "accounts/NL72INHO0579629781"
    Then the response status code should be 200
    And the response should be an object with an "NL72INHO0579629781" property

  Scenario: trying to access the get all endpoint as an employee with Limit And Offset
    Given I log in as an "employee"
    When I send a GET request to "accounts?limit=1&offset=0"
    Then the response status code should be 200
    And the response should be an array of objects
    And the response should have 1 object

    Scenario: trying to access the get all endpoint as an employee with Invalid Account Type
    Given I log in as an "employee"
    When I send a GET request to "accounts?accountType=invalid"
    Then the response status code should be 400
    And the response should be error message "The account type is not valid"

    Scenario: trying to access the get all endpoint as an employee with Valid Account Type
    Given I log in as an "employee"
    When I send a GET request to "accounts?accountType=Savings&limit=2&offset=0"
    Then the response status code should be 200
    And the response should be an array of objects
    And the response should have 2 object


  Scenario: trying to access the get all endpoint as a customer
    Given I log in as an "customer"
    When I send a GET request to "accounts"
    Then the response status code should be 403
    And the response should be error message "Access Denied"

  Scenario: trying to access the get single endpoint as a customer when the account does not belong to them
    Given I log in as an "customer"
    When I send a GET request to "accounts/NL72INHO0579629781"
    Then the response status code should be 403
    And the response should be error message "You are not allowed to access this account!"

  Scenario: trying to access the get single endpoint as a customer when the account does belong to them
    Given I log in as an "customer"
    When I send a GET request to "accounts/NL72INHO0579639781"
    Then the response status code should be 200
    And the response should be an object with an "NL72INHO0579639781" property

  Scenario: trying to access the get one endpoint as an employee which iban does not exist
    Given I log in as an "employee"
    When I send a GET request to "accounts/NL72INHO0579629782"
    Then the response status code should be 404
    And the response should be error message "Account with IBAN: NL72INHO0579629782 was not found"

  Scenario: trying to access the get one endpoint as an employee which iban is not From bank
    Given I log in as an "employee"
    When I send a GET request to "accounts/NL72INH00579629783"
    Then the response status code should be 400
    And the response should be error message "Not a valid Iban for Inholland bank"


