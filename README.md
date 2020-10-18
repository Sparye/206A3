## Before running the project:
Ensure that runGame.sh, Quinzical.jar, and the categories file are in the same directory. The project will create a new directory called GameData to store the game's state while the app is closed.

## Run the project:
Execute runGame.sh

To do this:
- Right-click inside the directory with runGame.sh, Quinzical.jar, and the categories file.
- Select "open in terminal"
- enter the following two commands:
- chmod +x runGame.sh
- ./runGame.sh

If the new Virtual Machine messes up the window size, change the system aspect ratio.

## How do I add questions?
Edit the "categories" text file.
- Categories should be separated from each other by an empty line.
- The category title goes on its own line. Do not put brackets in category titles.
- Questions go below it, with no blank lines between questions.
- Brackets are mandatory to separate the question, question prefix, and answer.
- A slash / can be used to allow multiple valid answers.

For example:

  Fauna
  
  This is New Zealand’s mountain Parrot ( What is) the Kea
  
  This is called a living dinosaur ( What is) the Tuatara / a Boomer
