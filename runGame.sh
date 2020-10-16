#!/bin/bash



if [ ! -d "./GameData" ]
then
	mkdir -p GameData/{Game/,Practice/,Setting/}
	touch GameData/{Game/.Score,Game/Questions,Practice/Attempt,Practice/Question,Setting/TTS,Setting/Timer,Setting/.lock}
	echo "0" >> GameData/Game/.Score
	echo "160" >> GameData/Setting/TTS
	echo "15" >> GameData/Setting/Timer
	echo "0" >> GameData/Setting/.lock
fi


 
