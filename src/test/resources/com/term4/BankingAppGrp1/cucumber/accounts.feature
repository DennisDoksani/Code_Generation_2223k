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

  Scenario: when trying to search customer name with name
    Given I log in as an "employee"
    When I send a GET request to "accounts/searchByCustomerName?limit=3&offset=0&customerName=Customer Seed"
    Then the response status code should be 200
    And the response should be an array of objects
    And the response should have 3 object

  Scenario: when trying to search customer name with name that does not exist
    Given I log in as an "customer"
    When I send a GET request to "accounts/searchByCustomerName?limit=3&offset=0&customerName=Invalid_Test"
    Then the response status code should be 404
    And the response should be error message "No accounts found by this name Invalid_Test!"

  Scenario: when customer tries to search their accounts by email they get all their accounts
    Given I log in as an "customer"
    When I send a GET request to "accounts/user/customer@seed.com"
    Then the response status code should be 200
    And The response should be an UserAccountsDTO List

  Scenario: when customer tries to load other accounts by providing other's email should give forbidden
    Given I log in as an "customer"
    When I send a GET request to "accounts/user/employeeCustomer@seed.com"
    Then the response status code should be 403
    And the response should be error message "You are not allowed to access others Accounts Details!"

  Scenario: when employee tries to load other accounts by providing other's email should give accounts
    Given I log in as an "employee"
    When I send a GET request to "accounts/user/customer@seed.com"
    Then the response status code should be 200
    And The response should be an UserAccountsDTO List

  Scenario: when customer tries to post new account with valid Account Creating DTO
    Given I log in as an "customer"
    When I send a POST request to "accounts"
    Then the response status code should be 403
    And the response should be error message "Access Denied"

  Scenario: when employee tries to post new account with valid Account Creating DTO but limit reached
    Given I log in as an "employee"
    When I send a POST request to "accounts" with a valid CreatingAccountDTO
    Then the response status code should be 409
    And the response should be error message "The user has reached the maximum limit for SAVINGS accounts."

  Scenario: when employee tries to post new account with valid Account Creating DTO and limit is ok
    Given I log in as an "customerWithoutAc"
    When I send a POST Request to "accounts" with Valid CreatingAccountDTO
    Then the response status code should be 201
    And the response should be an Account object with Iban

  Scenario: when employee tries to update account active status with valid Iban
    Given I log in as an "employee"
    When I send a POST request to "accounts/accountStatus/NL72INHO0579639781" with Valid RequestBody
    Then the response status code should be 204
    And  the account status of "NL72INHO0579639781" should be updated

  Scenario: when employee tries to update account active status with invalid Iban
    Given I log in as an "employee"
    When I send a POST request to "accounts/accountStatus/NL72INHO0579639782" with Valid RequestBody
    Then the response status code should be 404
    And the response should be error message "The updating account with IBAN: NL72INHO0579639782 was not found"

  Scenario: when customer tries to update account active status with invalid RequestBody
    Given I log in as an "customer"
    When I send a POST request to "accounts/accountStatus/NL72INHO0579639782" with Valid RequestBody
    Then the response status code should be 403
    And the response should be error message "Access Denied"










