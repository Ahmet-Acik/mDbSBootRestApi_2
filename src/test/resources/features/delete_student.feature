Feature: Delete a student by ID

  As an administrator
  I want to delete student records
  So that I can remove outdated or incorrect information and handle cases
  where the student does not exist

  Background:
    Given the student service is running
    And the following students exist:
      | id | name       | age | email               |
      | 1  | John Doe   | 20  | john.doe@example.com|
      | 2  | Jane Smith | 22  | jane.smith@example.com|

  Scenario Outline: Successfully delete a student by ID
    When I send a DELETE request to "/students/<id>"
    Then the response status should be 204

    Examples:
      | id |
      | 1  |
      | 2  |

  Scenario Outline: Fail to delete a non-existent student by ID
    When I send a DELETE request to "/students/<id>"
    Then the response status should be 404
    And the response should contain "Student not found"

    Examples:
      | id  |
      | 999 |