## Game GUI

### Money Grid
In the game module, there is a money/clue grid. We implemented the grid by creating a GridPane and using a nested for loop to create category labels and buttons for each question. 

At the beginning, we used a nested for loop with fixed values to create buttons. But then we realized that counting the number of categories and number of questions and then create a nested for loop with those values is much better idea.

The reason is that it allows the number of question and the number of categories to be dynamic. Therefore, the program is more open for extension and less hard coding.

### Answering Scene
The answering scene was easy to create since it shared a lot of similarities with the answering scene for practice module. We made some little tweaks to the GUI and the logic for verifying answers.