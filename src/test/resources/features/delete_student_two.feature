Feature: Delete a student by ID

  As an administrator
  I want to delete student records
  So that I can remove outdated or incorrect information and handle cases
  where the student does not exist

  Background:
    Given the student service is running
    And the following students are created:
      | name       | age | email               |
      | John Doe   | 20  | john.doe@example.com|
      | Jane Smith | 22  | jane.smith@example.com|

  Scenario Outline: Successfully delete a student by ID
    Given the student ID is stored for "<name>"
    When I send one DELETE request to "/students/<id>"
    Then the response status should be 204

    Examples:
      | name       |
      | John Doe   |
      | Jane Smith |

  Scenario Outline: Fail to delete a non-existent student by ID
    When I send a DELETE request to "/students/<id>"
    Then the response status should be 404
    And the response should contain "Student not found"

    Examples:
      | id  |
      | 999 |
      | 1000 |