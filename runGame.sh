#!/bin/bash



if [ ! -d "./GameData" ]
then
	mkdir -p GameData/{Game/categories/,Practice/,Setting/}
	touch GameData/{Game/.Score,Practice/Attempt,Practice/Question,Setting/TTS}

fi


 
