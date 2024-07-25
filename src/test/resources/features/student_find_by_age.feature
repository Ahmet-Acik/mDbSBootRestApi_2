Feature: Find students by age range

  Background:
    Given the student service is running

  Scenario Outline: Students found by age range
    Given students with the following data exist:
      | id   | name   | age   | email   |
      | <id> | <name> | <age> | <email> |
    When I send a GET request to find by age range "/students/age?minAge=<minAge>&maxAge=<maxAge>"
    Then the response status should be 200
    And the response should contain the following students:
      | id   | name   | age   | email   |
      | <id> | <name> | <age> | <email> |

    Examples:
      | id | name   | age | email              | minAge | maxAge |
      | 1  | John   | 20  | john@example.com   | 18     | 22     |
      | 2  | Johnny | 22  | johnny@example.com | 20     | 25     |
      | 3  | Jane   | 21  | jane@example.com   | 20     | 22     |

  Scenario Outline: No students found by age range
    Given no students exist within age range <minAge> to <maxAge>
    When I send a GET request to find by age range "/students/age?minAge=<minAge>&maxAge=<maxAge>"
    Then the response status should be 204

    Examples:
      | minAge | maxAge |
      | 30     | 35     |
      | 40     | 45     |
      | 50     | 55     |