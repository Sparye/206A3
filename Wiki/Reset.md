# The Reset Button

This button resets both the Games & Practice modules. They should behave as they do when the program is launched for the first time.

This is achieved by simply erasing the contents of these modules' state files, and setting their variables to the launch default.

Saved settings are not reset by this button, as we reasoned users would not want to change the TTS speed when restarting a quiz.

An interesting fact is that the saved practice question is not erased by this button - It just sets the remaining attempts to zero so the question cannot be accessed!

