package com.geek.rpg.game;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class ListOfUnits {
	private List<AbstractUnit> listOfUnits = new ArrayList<AbstractUnit>();
	private int currentUnitInd = -1;
	private Hero hero;

	public void newHero(float x, float y) {
		hero = new Hero();
		hero.setPosition(new Vector2(x, y));
		listOfUnits.add(hero);
	}

	public void newMonster(float x, float y) {
		Monster monster = new Monster();
		monster.setPosition(new Vector2(x, y));
		listOfUnits.add(monster);
	}


	public void render() {
		for (AbstractUnit unit:listOfUnits) {
			unit.render();
		}
	}

	public void update(float dt) {
		for (AbstractUnit unit:listOfUnits) {
			unit.update(dt);
		}
	}

	public AbstractUnit getNext() {
		++currentUnitInd;
		if (currentUnitInd >= listOfUnits.size()) currentUnitInd = 0;
		return listOfUnits.get(currentUnitInd);
	}

	public AbstractUnit getHero() {
		return hero;
	}

	public List<AbstractUnit> getListOfUnits() {
		for (AbstractUnit unit : listOfUnits) {
			if (unit.hp == 0) listOfUnits.remove(unit);
		}
		return listOfUnits;
	}
}
