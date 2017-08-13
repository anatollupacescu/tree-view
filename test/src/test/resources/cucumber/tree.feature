Feature: User can manipulate tree instances
  As a user
  I want to add and remove tree instances

 Scenario: User can create tree instance
    Given no trees have been created
    When I create a tree called products
    Then the tree names array should have 1 element

 Scenario: User can remove tree instance
    Given a tree instance called products is present
    When I remove the tree instance called products
    Then the tree names array should have 0 element
