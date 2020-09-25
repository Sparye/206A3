#!/bin/bash



if [ ! -d "./GameData" ]
then
	mkdir -p GameData/{Game/,Practice/,Setting/}
	touch GameData/{Game/.Score,Game/Questions,Practice/Attempt,Practice/Question,Setting/TTS}

fi


 
