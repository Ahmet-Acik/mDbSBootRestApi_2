Feature: Update a student by ID

  As an administrator
  I want to update student information by ID
  So that I can keep student records up to date and handle cases
  where the student does not exist or the data is invalid

  Background:
    Given the student service is running
    Given the following students exist:
      | id | name       | age | email               |
      | 1  | John Doe   | 20  | john.doe@example.com|
      | 2  | Jane Smith | 22  | jane.smith@example.com|

  Scenario Outline: Successfully update a student by ID
    When I send a PUT request to "/students/<id>" with the following data:
      | field | value          |
      | name  | Updated Name   |
      | age   | 25             |
      | email | updated.email@example.com |
    Then the response status should be 200
    And the response should contain "Student updated successfully with ID: <id>"

    Examples:
      | id |
      | 1  |
      | 2  |

  Scenario Outline: Fail to update a student with invalid data
    When I send a PUT request to "/students/<id>" with the following data:
      | field | value          |
      | name  |                |
      | age   | -1             |
      | email | invalid-email  |
    Then the response status should be 400
    And the response should contain "Bad Request"

    Examples:
      | id |
      | 1  |
      | 2  |

  Scenario Outline: Fail to update a non-existent student
    When I send a PUT request to "/students/<id>" with the following data:
      | field | value          |
      | name  | New Name       |
      | age   | 30             |
      | email | new.email@example.com |
    Then the response status should be 404
    And the response should contain "Student not found"

    Examples:
      | id  |
      | 999 |