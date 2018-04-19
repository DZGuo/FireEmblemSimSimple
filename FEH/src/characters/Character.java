package characters;

import characters.Pos;
import resources.Constants;

import java.lang.Math;
import java.lang.String;

public class Character {
	private String name;
	private int maxHp, hp, atk, spd, def, res;
	private char type, dmgType, movement, rngType;
	private Skill aSkill, bSkill, cSkill;
	private Pos pos = new Pos();
	private final static Character[] characterList = 
		{ new Character("Celica", 39, 39, 46, 33, 22, 22, Constants.RED, Constants.MAGIC, Constants.INFANTRY, Constants.RANGED),
		  new Character("Lucina", 43, 43, 50, 36, 25, 19, Constants.RED, Constants.PHYSICAL, Constants.INFANTRY, Constants.MELEE), 
		  new Character("Effie", 50, 50, 55, 22, 33, 23, Constants.BLUE, Constants.PHYSICAL, Constants.ARMOR, Constants.MELEE), 
		  new Character("Reinhardt", 38, 38, 41, 23, 27, 25, Constants.BLUE, Constants.MAGIC, Constants.HORSE, Constants.RANGED), 
		  new Character("Hector", 52, 52, 52, 24, 37, 19, Constants.GREEN, Constants.PHYSICAL, Constants.ARMOR, Constants.MELEE), 
		  new Character("Fae", 46, 46, 46, 28, 25, 30, Constants.GREEN, Constants.MAGIC, Constants.INFANTRY, Constants.MELEE), 
		  new Character("Kagero", 31, 31, 40, 32, 25, 31, Constants.COLORLESS, Constants.PHYSICAL, Constants.INFANTRY, Constants.RANGED), 
		  new Character("Innes", 35, 35, 47, 34, 14, 31, Constants.COLORLESS, Constants.PHYSICAL, Constants.INFANTRY, Constants.RANGED) };
	
	public Character(final String name, final int maxHp,final int hp, final int atk, 
			final int spd, final int def, final int res, final char type, 
			final char dmgType, final char movement, final char rngType) {
		this.maxHp = maxHp;
		this.name = name;
		this.hp = hp;
		this.atk = atk;
		this.spd = spd;
		this.def = def;
		this.res = res;
		this.type = type;
		this.dmgType = dmgType;
		this.movement = movement;
		this.rngType = rngType;
	}
	
	public void setPos(final int y, final int x) {
		this.pos.setx(x);
		this.pos.sety(y);
	}
	
	public String getName() { return this.name; }
	public int getMaxHealth() { return this.maxHp; }
	public int getHealth() { return this.hp; }
	public int getAttack() { return this.atk; }
	public int getSpeed() { return this.spd; }
	public int getDefense() { return this.def; }
	public int getResistance() { return this.res; }
	public char getType() { return this.type; }
	public char getDmgType() { return this.dmgType; }
	public char getMovement() { return this.movement; }
	public char getRngType() { return this.rngType; }
	public Pos getPos() { return this.pos; }
	
	public int hit(final int eAtk, final char eDmgType, final char eType) {
		int damage = 0;
		double modifier = 1;
		
		switch(this.type) {
			case Constants.RED: 
				switch(eType) {
					case Constants.BLUE: modifier = 1.2; break;
					case Constants.GREEN: modifier = 0.8; break;
				}
				break;
			case Constants.BLUE: 
				switch(eType) {
					case Constants.GREEN: modifier = 1.2; break;
					case Constants.RED: modifier = 0.8; break;
				}
				break;
			case Constants.GREEN: 
				switch(eType) {
					case Constants.RED: modifier = 1.2; break;
					case Constants.BLUE: modifier = 0.8; break;
				}
				break;
		}
		
		// Calculates damage based on damage type and 
		if (eDmgType == Constants.MAGIC) {
			damage = (int) Math.round((eAtk * modifier) - this.res);
		} else if (eDmgType == Constants.PHYSICAL) {
			damage = (int) Math.round((eAtk * modifier) - this.def);
		}
		
		this.hp -= damage;
		if(this.hp < 0) { this.hp = 0; }
		return damage;
	}
	
	private void attack(Character enemy, final int phase) {
		int damage = enemy.hit(this.atk, this.dmgType, this.type);
		switch(phase) {
			case 1: System.out.println(this.name + " attacks " + 
						enemy.getName() + " for " + damage + " damage!");
					break;
			case 2: System.out.println(this.getName() + " counters " + 
						enemy.name + " for " + damage + " damage!");
					break;
			case 3: System.out.println(this.getName() + " strikes " + 
						enemy.name + " for " + damage + " damage!");
					break;
			case 4: System.out.println(this.getName() + " stikes " + 
					enemy.name + " again for " + damage + " damage!");
					break;
			default: break;
		}
	}
	
	public void initiateAttack(Character enemy) {
		
		// initial attack
		this.attack(enemy, 1);
		
		// possible counter
		if((enemy.getRngType() == this.rngType) && (enemy.getHealth() > 0)) {
			enemy.attack(this, 2);
		}
		
		// second attack if applicable
		if((this.spd - enemy.getSpeed() >= 5) && (this.hp > 0) 
				&& (enemy.getHealth() > 0)) {
			this.attack(enemy, 3);
		} else if((enemy.getSpeed() - this.spd >= 5) && (enemy.getHealth() > 0) 
				&& (this.hp > 0)  && (enemy.getRngType() == this.rngType)) {
			enemy.attack(this, 3);
		}
	}

	public boolean canMove(final int y, final int x) {
		int move;
		switch (this.movement) {
			case Constants.ARMOR: move = 1; break;
			case Constants.HORSE: move = 3; break;
			default: move = 2;
		}
		
		if((Math.abs(this.pos.gety() - y) + Math.abs(this.pos.getx() - x)) <= move) {
			return true;
		} else {
			return false;
		}
	}

	public boolean canAttack(Character enemy) {
		int range;
		switch(this.rngType) {
			case Constants.MELEE: range = 1; break;
			case Constants.RANGED: range = 2; break;
			default: range = 0;
		}
		
		if((Math.abs(this.pos.gety() - enemy.getPos().gety()) + 
				Math.abs(this.pos.getx() - enemy.getPos().getx())) <= range) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isDefaultCharacter(final String name) {
		for(Character character : characterList) {
			if(name.equals(character.getName())) { 
				return true;
			}
		}
		return false;
	}

	public static Character createDefaultCharacter(String name) {
		for(Character character : characterList) {
			if(name.equals(character.getName())) { 
				return character;
			}
		}
		return null;
	}
}