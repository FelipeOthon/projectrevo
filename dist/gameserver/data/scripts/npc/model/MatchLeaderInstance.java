package npc.model;

import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.templates.npc.NpcTemplate;

public class MatchLeaderInstance extends MatchBerserkerInstance
{
	public MatchLeaderInstance(final int objectId, final NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void reduceCurrentHp(double damage, final Creature attacker, final Skill skill, final int poleHitCount, final boolean crit, final boolean awake, final boolean standUp, final boolean directHp, final boolean canReflect, final boolean transferDamage, final boolean isDot, final boolean sendMessage)
	{
		if(attacker.isPlayer())
			damage = damage / getMaxHp() / 0.05 * 100.0;
		else
			damage = damage / getMaxHp() / 0.05 * 10.0;
		super.reduceCurrentHp(damage, attacker, skill, poleHitCount, crit, awake, standUp, directHp, canReflect, transferDamage, isDot, sendMessage);
	}
}
