Feature: Student Creation

  Background:
    Given the student service is running

  Scenario Outline: Create a new student with valid data
    Given the following student data:
      | name   | age   | email   |
      | <name> | <age> | <email> |
    When I send a POST request to "/students" with the student data
    Then the response status should be 201
    And the response should contain a message "A new student is successfully created with ID: <id>"

    Examples:
      | name | age | email            |
      | John | 20  | john@example.com |
      | Jane | 22  | jane@example.com |

  Scenario Outline: Fail to create a new student with invalid data
    Given the following student data:
      | name   | age   | email   |
      | <name> | <age> | <email> |
    When I send a POST request to "/students" with the student data
    Then the response status should be 400
    And the response should contain the following error messages:
      | error_message   |
      | <error_message> |

    Examples:
      | name | age | email            | error_message                                         |
      |      | 20  | john@example.com | Missing required field: Name is required              |
      | John | -1  | john@example.com | Missing required field: Age must be a positive number |
      | John | 20  | invalid-email    | Missing required field: Email is not valid            |