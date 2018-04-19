package control;

import java.util.Scanner;

import characters.Character;
import maps.Map;
import resources.Constants;
import characters.Pos;

public class GameController {
	private static Scanner reader = new Scanner(System.in);
	private static int numAlly = 1;
	private static int numEnemy = 1;
	private static char command;
	private static int unit, x, y;
	private static char c;
	
	// initialize everything
	private static Map newMap = new Map();
	private static Character[] charList = new Character[8];
	
	private static void printMap(char[][] map) {
		for(int i = 0; i < 8; ++i) {
			System.out.print(i + " ");
			for(int j = 0; j < 6; ++j) {
				System.out.print(map[i][j]);
			}
			System.out.println("");
		}
		System.out.println("/012345");
	}
	
	private static void checkHealth(Character character) {
		if(character.getHealth() == 0) {
			System.out.println(character.getName() + " has fallen!");
			newMap.reset(character.getPos().gety(), character.getPos().getx());
		}
	}
	
	private static boolean moveCommand() {
		unit = reader.nextInt();
		if(charList[unit-1] != null) {
			y = reader.nextInt();
			x = reader.nextInt();
			
			if(charList[unit-1].canMove(y,x) && (newMap.getMap()[y][x] == '.')) {
				newMap.setMap(charList[unit-1].getPos().gety(), charList[unit-1].getPos().getx(), '.'); 
				charList[unit-1].setPos(y, x);
				newMap.setMap(charList[unit-1].getPos().gety(), charList[unit-1].getPos().getx(), (char) (unit+48)); 
				reader.nextLine();
				return true;
			} else {
				System.out.println("Unit cannot move there!");
				return false;
			}
		}
		return false;
	}
	
	private static void attackCommand(final int attacker) {
		System.out.println("Which unit do you want to attack?");
		c = 'y';
		while (c == 'y') {
			unit = reader.nextInt();
			if(!(charList[unit-1] == null) && (charList[attacker-1].canAttack(charList[unit-1]))) {
				charList[attacker-1].initiateAttack(charList[unit-1]);
				
				// outputs results
				System.out.println(charList[attacker-1].getName() + " has " + charList[attacker-1].getHealth() + " health left!" );
				System.out.println(charList[unit-1].getName() + " has " + charList[unit-1].getHealth() + " health left!" );
				
				checkHealth(charList[attacker-1]);
				checkHealth(charList[unit-1]);
				
				c = 'n';
			} else {
				System.out.println("Please enter a valid unit! Try again?");
				c = reader.next().charAt(0);
			}
		}
	}
	
	private static void checkAttackCommand(final int unit) {
		if(charList[unit-1] != null) {
			printMap(newMap.checkMove(charList[unit-1].getPos().gety(), 
					charList[unit-1].getPos().getx(), 
					charList[unit-1].getMovement(),
					charList[unit-1].getRngType()));
			reader.nextLine();
		}
	}
	
	private static void checkStatsCommand(final int unit) {
		if(charList[unit-1] != null) {
			String colorType, damageType, moveType, rangeType;
			
			switch(charList[unit-1].getType()) {
				case 'r': colorType = "Red"; break;
				case 'b': colorType = "Blue"; break;
				case 'g': colorType = "Green"; break;
				case 'c': colorType = "Colorless"; break;
				default: colorType = "No color type found"; break;
			}
			switch(charList[unit-1].getDmgType()) {
				case 'p': damageType = "Physical"; break;
				case 'm': damageType = "Magic"; break;
				default: damageType = "No damage type found"; break;
			}
			switch(charList[unit-1].getMovement()) {
				case 'i': moveType = "Infantry"; break;
				case 'f': moveType = "Flying"; break;
				case 'h': moveType = "Horse"; break;
				case 'a': moveType = "Armor"; break;
				default: moveType = "No movement type found"; break;
			}
			switch(charList[unit-1].getRngType()) {
				case 'm': rangeType = "Melee"; break;
				case 'r': rangeType = "Ranged"; break;
				default: rangeType = "No range type found"; break;
			}
			System.out.println("Name: " + charList[unit-1].getName() +
					"\nHP:  " + charList[unit-1].getHealth() + 
					"/" + charList[unit-1].getMaxHealth() + 
					"\nAtk: " + charList[unit-1].getAttack() +
					"\nSpd: " + charList[unit-1].getSpeed() +
					"\nDef: " + charList[unit-1].getDefense() +
					"\nRes: " + charList[unit-1].getResistance() +
					"\nCol: " + colorType +
					"\nDmg: " + damageType +
					"\nMov: " + moveType +
					"\nRng: " + rangeType);
		}
	}
	
	private static void createHeroes(final int unitIdStart, final int numHeroes) {
		for (int i = unitIdStart; i < numHeroes + unitIdStart; ++i) {
			if (i < numAlly) {
				System.out.print("Ally Character " + (i+1) + "\nName: ");
			} else {
				System.out.print("Enemy Character " + (i+1-numAlly) + "\nName: ");
			}
			String name = reader.nextLine();
			if(Character.isDefaultCharacter(name)) {
				charList[i] = Character.createDefaultCharacter(name);
			} else {
				System.out.print("Health: ");
				int hp = reader.nextInt();
				System.out.print("Attack: ");
				int atk = reader.nextInt();
				System.out.print("Speed: ");
				int spd = reader.nextInt();
				System.out.print("Defense: ");
				int def = reader.nextInt();
				System.out.print("Resistance: ");
				int res = reader.nextInt();
				System.out.print("Color Type (r,b,g,c): ");
				char type = reader.next().charAt(0);
				System.out.print("Damage Type (m,p): ");
				char dmgType = reader.next().charAt(0);
				System.out.print("Move Type (h,a,f,i): ");
				char movement = reader.next().charAt(0);
				System.out.print("Range (m,r): ");
				char rngType = reader.next().charAt(0);
				charList[i] = new Character(name, hp, hp, atk, spd, def, res, type, dmgType, movement, rngType);
				reader.nextLine();
			}
		}
	}
	
	private static boolean positionTaken(final int y, final int x, Character[] charList) {
		for(Character character : charList) {
			if(character != null && character.getPos().gety() == y && character.getPos().getx() == x) {
				return true;
			}
		}
		return false;
	}
	
	private static void setHeroes(final int unitIdStart, final int numHeroes) {
		for(int i = unitIdStart; i < numHeroes + unitIdStart; ++i) {
			System.out.println("What is the position for " + charList[i].getName() + " (y,x)?");
			y = reader.nextInt();
			x = reader.nextInt();
			while(positionTaken(y, x, charList)) {
				System.out.println("Position is already taken! Try again:");
				y = reader.nextInt();
				x = reader.nextInt();
			}
			charList[i].setPos(y, x);
			reader.nextLine();
		}
	}
	
	private static void setMap() {
		for(int i = 0; i < 8; ++i) {
			if (charList[i] != null) {
				newMap.setMap(charList[i].getPos().gety(), charList[i].getPos().getx(), (char) (i+49)); 
			}
		}
	}
	
	public static void main(String[] args) {
		
		boolean runGame = true;
		
		System.out.println("Welcome to Fire Emblem Heroes Emulator!\n"
				+ "Please initialize your characters!");
		
		// create user heroes
		createHeroes(0, numAlly);
		
		// create enemy heroes
		createHeroes(numAlly, numEnemy);
		
		// set positions for user heroes
		setHeroes(0, numAlly);
		
		// set positions for enemy heroes
		setHeroes(numAlly, numEnemy);
		
		// set all heroes onto the map
		setMap();
		
		printMap(newMap.getMap());
		
		while(runGame) {
			System.out.println("Input a command:\n"
					+ "move(m): unit, x, y\n"
					+ "check(c): unit\n"
					+ "attack(a): unit, unit\n"
					+ "end(e)\n"
					+ "quit(q)");
			command = reader.next().charAt(0);
			
			switch(command) {
				case 'm':
					boolean success = moveCommand();
					printMap(newMap.getMap());
					if (success && newMap.nearOpponent(charList, unit, numAlly, numEnemy)) {
						System.out.println("Do you wish to attack?");
						c = reader.next().charAt(0);
						if(c == 'y') {
							attackCommand(unit);
							printMap(newMap.getMap());
						}
						reader.nextLine();
					}
					break;
				case 'c': 
					unit = reader.nextInt();
					checkAttackCommand(unit);
					checkStatsCommand(unit);
					break;
				case 'a': 
					unit = reader.nextInt();
					attackCommand(unit);
					printMap(newMap.getMap());
					break;
				case 'e': 
					printMap(newMap.getMap());
					break;
				case 'q':
					runGame = false;
					break;
			}
		}
	}
}