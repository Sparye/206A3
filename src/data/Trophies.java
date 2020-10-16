package data;

public class Trophies {
	public static String get() {
		int score = Score.getSumAndSave("0") / 1000;
		String award = "\n\nYour Award:\n\n";
		switch(score) {
		case 0:
			return award += "(◕‿◕✿)"
					+ "\nBeginner";
		case 1:
			return award += "(*^‿^*)"
					+ "\nBronze";
		case 2:
			return award += "\\(★ω★)/"
					+ "\nSilver";
		case 3:
			return award += "(ﾉ◕ヮ◕)ﾉ*:･ﾟ✧"
					+ "\nGold";
		case 4:
			return award += "♡＼(￣▽￣)／♡"
					+ "\nPlatinum";
		case 5:
			return award += "ʚ♡⃛ɞ(ू•ᴗ•ू❁)"
					+ "\nTrue Love";
		case 6:
			return award += "｡*:☆(°(°ω(°ω°(☆ω☆)°ω°)ω°)°)｡:゜☆｡"
					+ "\nCult Leader";
		case 7:
			return award += "ଘ(੭*ˊᵕˋ)੭* ̀ˋ* ੈ✩‧₊˚"
					+ "\nDivinity";
		default:
			return award+= "(இ﹏இ`｡)"
					+ "\nCheater!";
					
		}
	}
}
