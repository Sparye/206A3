## Settings

The only setting in the current build of the game is the text-to-speech (TTS) speed slider, labeled "speaking speed".

This slider features a dynamic speed multiplier, where x1.0 indicates festival's default speed.

We initially implemented our TTS functions using espeak. Unfortunately, we found espeak had a bug where the virtual machine's audio output device would have to be reset every time a TTS function was called.

Following Catherine's festival lecture, we rewrote those functions to use Festival - which did not have the bug.

An interesting fact is that speed values are still saved to the game's state files in espeak's speed units. This is in case a fix for espeak's audio bug is found. Our TTS functions convert this speed to a duration stretch factor compatible with Festival. 
