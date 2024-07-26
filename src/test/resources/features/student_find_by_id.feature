Feature: Find student by ID

  Background:
    Given the student service is running

  Scenario Outline: Successfully find a student by ID
    Given a student with the following data exists:
      | id  | name   | age | email            |
      | <id> | <name> | <age> | <email> |
    When I send a GET request to "/students/<id>"
    Then the response status should be 200
    And the response should contain the student with ID "<id>"

    Examples:
      | id  | name   | age | email            |
      | 123 | John   | 20  | john@example.com |
      | 999 | Jane   | 22  | jane@example.com |

  Scenario Outline: Student not found by ID
    Given no student with ID "<id>" exists
    When I send a GET request to "/students/<id>"
    Then the response status should be 404

    Examples:
      | id  |
      | 123 |
      | 999 |