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
    Then the tree names array should have 0 elements

 Scenario: User can remove a particular tree instance
    Given two trees, alpha and beta, are present
    When I remove the tree instance called beta
    Then the tree names array should have 1 element
    And the tree names array should contain the name alpha

 Scenario: User can remove multiple tree instances
    Given two trees, alpha and beta, are present
    When I remove the tree instance called beta
    And I remove the tree instance called alpha
    Then the tree names array should have 0 elements
