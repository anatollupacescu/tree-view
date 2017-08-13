Feature: User can manipulate tree contents
  As a user
  I want to see the tree changing when I apply updates
  So that I can change its state

 Scenario: User can add nodes to the tree
   Given I have a tree called root1
   When I add the following nodes

     | location        | name       |
     | /               | node1      |
     | /               | node2      |
     | /node1          | subNode1   |
     | /node1/subNode1 | subSub     |

   Then the root has 2 nodes
   And the node at /node1 contains subNode1
   And the node at /node1/subNode1 contains subSub
   And the node at /node2 has no children

 Scenario: User can remove nodes from tree
   Given I have a tree called root2
   When I add the following nodes

     | location        | name       |
     | /               | node1      |
     | /               | node2      |
     | /node1          | subNode1   |
     | /node1/subNode1 | subSub     |
   And I remove node node1 at location in root
   Then the root has 1 node
