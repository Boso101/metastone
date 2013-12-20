package net.pferdimanzug.hearthstone.analyzer.game.spells;

import net.pferdimanzug.hearthstone.analyzer.game.GameContext;
import net.pferdimanzug.hearthstone.analyzer.game.Player;
import net.pferdimanzug.hearthstone.analyzer.game.actions.TargetSelection;
import net.pferdimanzug.hearthstone.analyzer.game.entities.Entity;

public class AreaDamageSpell extends AreaSpell {
	
	private int damage;

	public AreaDamageSpell(int damage, TargetSelection targetSelection) {
		super(targetSelection);
		this.damage = damage;
	}

	@Override
	protected void forEachTarget(GameContext context, Player player, Entity entity) {
		context.getLogic().damage(entity, damage);
	}

}
