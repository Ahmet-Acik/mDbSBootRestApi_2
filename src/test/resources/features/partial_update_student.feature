Feature: Partially update a student

  As an administrator
  I want to partially update student information
  So that I can keep student records up to date

  Background:
    Given the following students exist:
      | id   | name        | age | email                   |
      | 1    | John Doe    | 20  | john.doe@example.com    |
      | 2    | Jane Doe    | 22  | jane.doe@example.com    |
      | 999  | Test User   | 30  | test.user@example.com   |
      | 1000 | Sample User | 25  | sample.user@example.com |

  Scenario Outline: Successfully partially update a student by ID
    When I send a PATCH request to "/students/<id>" with the following data:
      | field | value      |
      | name  | <newName>  |
      | age   | <newAge>   |
      | email | <newEmail> |
    Then the response status should be 200
    And the response should contain "<message>"

    Examples:
      | id | newName  | newAge | newEmail             | message                                           |
      | 1  | Jane Doe | 22     | jane.doe@example.com | Student partially updated successfully with ID: 1 |
      | 2  | Alicia   | 26     | alicia@example.com   | Student partially updated successfully with ID: 2 |

  Scenario Outline: Fail to partially update a non-existent student
    When I send a PATCH request to "/students/<id>" with the following data:
      | field | value      |
      | name  | <newName>  |
      | age   | <newAge>   |
      | email | <newEmail> |
    Then the response status should be 404
    And the response should contain "<message>"

    Examples:
      | id   | newName | newAge | newEmail          | message           |
      | 1999  | Jack    | 21     | Jack@example.com  | Student not found |
      | 11000 | Jones   | 23     | Jones@example.com | Student not found |