Feature: Find all students

  Background:
    Given the student service is running

  Scenario: Successfully find all students
    When I send a GET request to "/students/all"
    Then the response status should be 200
    And the response should contain a list of students

  Scenario: No students found
    Given the student repository is empty
    When I send a GET request to "/students/all"
    Then the response status should be 204