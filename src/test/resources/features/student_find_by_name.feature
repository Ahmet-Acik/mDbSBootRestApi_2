Feature: Find students starting with a given name

  As an administrator
  I want to find students starting with a given name
  So that I can view the list of students with a specific name prefix and handle cases
  where no students are found

  Background:
    Given the student service is running

  Scenario Outline: Students found by name prefix
    And a student with the following data exists:
      | id   | name   | age   | email   |
      | <id> | <name> | <age> | <email> |
    When I send a GET request to find by name "/students?name=<prefix>"
    Then the response status for find by name should be 200
    And the response should contain the following students:
      | id   | name   | age   | email   |
      | <id> | <name> | <age> | <email> |

    Examples:
      | id | name   | age | email              | prefix |
      | 1  | John   | 20  | john@example.com   | John   |
      | 2  | Johnny | 22  | johnny@example.com | Johnny |
      | 3  | Jane   | 21  | jane@example.com   | Jane   |

  Scenario Outline: No students found by name prefix
    And no students exist with name prefix "<prefix>"
    When I send a GET request to "/students?name=<prefix>"
    Then the response status should be 204

    Examples:
      | prefix |
      | John   |
      | Johnny |
      | Jane   |