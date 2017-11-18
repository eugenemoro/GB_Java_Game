package com.geek.rpg.game;

import com.geek.rpg.game.effects.Effect;
import com.geek.rpg.game.effects.RegenerationEffect;

import java.util.ArrayList;
import java.util.List;

public class ActiveEffectsSystem {
	private List<Effect> effects;

	public ActiveEffectsSystem() {
		this.effects = new ArrayList<Effect>();
	}

	public void add(Effect e) {
		effects.add(e);
	}

	public void effectsLifeTimeCheck() {
		for (int i = effects.size() - 1; i >= 0; i--) {
			effects.get(i).tick();
			if (effects.get(i).isEnded()) {
				effects.get(i).end();
				effects.remove(i);
			}
		}
	}
}
