package npc.model;

import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.MonsterInstance;
import l2s.gameserver.templates.npc.NpcTemplate;

public class QueenAntLarvaInstance extends MonsterInstance
{
	public QueenAntLarvaInstance(final int objectId, final NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void reduceCurrentHp(final double i, final Creature attacker, final Skill skill, final int poleHitCount, final boolean crit, final boolean awake, final boolean standUp, final boolean directHp, final boolean canReflect, final boolean transferDamage, final boolean isDot, final boolean sendMessage)
	{
		final double damage = getCurrentHp() - i > 1.0 ? i : getCurrentHp() - 1.0;
		super.reduceCurrentHp(damage, attacker, skill, poleHitCount, crit, awake, standUp, directHp, canReflect, transferDamage, isDot, sendMessage);
	}

	@Override
	public boolean canChampion()
	{
		return false;
	}

	@Override
	public boolean isImmobilized()
	{
		return true;
	}
}
