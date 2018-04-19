package maps;

import java.lang.Math;

import characters.Character;
import characters.Pos;
import resources.Constants;

public class Map {
	private char[][] map = new char[8][6];
	private char[][] baseMap = new char[8][6];
	
	// initialize map to empty
	public Map() {
		for(int i = 0; i < 8; ++i) {
			for(int j = 0; j < 6; ++j) {
				map[i][j] = '.';
				baseMap[i][j] = '.';
			}
		}
		
	}
	
	public void reset(final int y, final int x) {
		map[y][x] = baseMap[y][x];
	}
	
	public void setMap(final int y, final int x, final char c) {
		map[y][x] = c;
	}
	
	public char[][] getMap() {
		return map;
	}
	
	public char[][] checkMove(final int y, final int x, 
			final char movement, final char rngType) {
		int move, range;
		
		switch(movement) {
			case Constants.HORSE: move = 3; break;
			case Constants.ARMOR: move = 1; break;
			default: move = 2; break;
		}
		
		switch(rngType) {
			case Constants.RANGED: range = 2; break;
			case Constants.MELEE: range = 1; break;
			default: range = 0; break;
		}
		
		char[][] tempMap = new char[8][6];
		for(int i = 0; i < 8; ++i) {
			for(int j = 0; j < 6; ++j) {
				int dist = (Math.abs(y - i) + Math.abs(x - j));
				if((dist < move+1) && (map[i][j] == '.')) {
					tempMap[i][j] = '#';
				} else if ((dist <= move+range) && (map[i][j] == '.')) {
					tempMap[i][j] = '*';
				} else {
					tempMap[i][j] = map[i][j];
				}
			}
		}
		
		return tempMap;

	}
	
	public char mapAt(final int y, final int x) {
		return map[y][x];
	}

	private boolean nearRange(Pos pos, Pos pos2, int range) {
		if((Math.abs(pos.getx() - pos2.getx()) + Math.abs(pos.gety() - pos2.gety())) <= range) {
			return true;
		}
		return false;
	}
	
	public boolean nearOpponent(Character[] charList, int unit, int numAlly, int numEnemy) {
		int range;
		if (charList[unit-1].getRngType() == Constants.MELEE) {
			range = 1;
		} else if (charList[unit-1].getRngType() == Constants.RANGED) {
			range = 2;
		} else {
			range = 0;
		}
		for(int i = numAlly; i < numEnemy + numAlly; ++i) {
			if(nearRange(charList[unit-1].getPos(), charList[i].getPos(), range)) {
				return true;
			}
		}
		return false;
	}

}