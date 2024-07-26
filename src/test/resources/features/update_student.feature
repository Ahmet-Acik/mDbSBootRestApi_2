##Feature: Update a student by ID
##
##  Background:
##    Given the student service is running
##    Given the following students exist:
##      | id | name       | age | email               |
##      | 1  | John Doe   | 20  | john.doe@example.com|
##      | 2  | Jane Smith | 22  | jane.smith@example.com|
##
##  Scenario Outline: Successfully update a student by ID
##    When I send a PUT request to "/students/<id>" with the following data:
##      | field | value          |
##      | name  | Updated Name   |
##      | age   | 25             |
##      | email | updated.email@example.com |
##    Then the response status should be 200
##    And the response should contain "Student updated successfully with ID: <id>"
##
##    Examples:
##      | id |
##      | 1  |
##      | 2  |
##
##  Scenario Outline: Fail to update a student with invalid data
##    When I send a PUT request to "/students/<id>" with the following data:
##      | field | value          |
##      | name  |                |
##      | age   | -1             |
##      | email | invalid-email  |
##    Then the response status should be 400
##    And the response should contain "Bad Request"
##
##    Examples:
##      | id |
##      | 1  |
##      | 2  |

Feature: Update a student by ID

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
##
#
#Feature: Update a student by ID
#
#  Background:
#    Given the student service is running
#    Given the following students exist:
#      | id | name       | age | email               | address.street | address.city | address.postcode | courses          | fullTime | gpa | graduationDate       | registerDate         |
#      | 1  | John Doe   | 20  | john.doe@example.com| 123 Main St    | Sample City  | 12345            | ["Math", "Science"] | true     | 3.5 | 2024-05-13T00:00:00 | 2023-10-01T00:00:00 |
#      | 2  | Jane Smith | 22  | jane.smith@example.com| 456 Elm St   | Another City | 67890            | ["History", "Art"] | false    | 3.8 | 2025-06-15T00:00:00 | 2022-09-01T00:00:00 |
#
#  Scenario Outline: Successfully update a student by ID
#    When I send a PUT request to "/students/<id>" with the following data:
#      | field          | value                      |
#      | name           | Updated Name               |
#      | age            | 25                         |
#      | email          | updated.email@example.com  |
#      | address.street | 456 Updated St             |
#      | address.city   | Updated City               |
#      | address.postcode | 67890                    |
#      | courses        | ["Math", "Science"]        |
#      | fullTime       | true                       |
#      | gpa            | 3.8                        |
#      | graduationDate | 2024-05-13T00:00:00        |
#      | registerDate   | 2023-10-01T00:00:00        |
#    Then the response status should be 200
#    And the response should contain "Student updated successfully with ID: <id>"
#
#    Examples:
#      | id |
#      | 1  |
#      | 2  |
#
#  Scenario Outline: Fail to update a student with invalid data
#    When I send a PUT request to "/students/<id>" with the following data:
#      | field | value          |
#      | name  |                |
#      | age   | -1             |
#      | email | invalid-email  |
#    Then the response status should be 400
#    And the response should contain "Bad Request"
#
#    Examples:
#      | id |
#      | 1  |
#      | 2  |
#
#  Scenario Outline: Fail to update a non-existent student
#    When I send a PUT request to "/students/<id>" with the following data:
#      | field          | value                      |
#      | name           | New Name                   |
#      | age            | 30                         |
#      | email          | new.email@example.com      |
#      | address.street | 789 New St                 |
#      | address.city   | New City                   |
#      | address.postcode | 12345                    |
#      | courses        | ["History", "Art"]         |
#      | fullTime       | false                      |
#      | gpa            | 3.2                        |
#      | graduationDate | 2025-06-15T00:00:00        |
#      | registerDate   | 2022-09-01T00:00:00        |
#    Then the response status should be 404
#    And the response should contain "Student not found"
#
#    Examples:
#      | id  |
#      | 999 |