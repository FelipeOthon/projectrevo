package npc.model;

import l2s.gameserver.cache.Msg;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.instances.MonsterInstance;
import l2s.gameserver.templates.npc.NpcTemplate;

public class GuardAntInstance extends MonsterInstance
{
	public GuardAntInstance(final int objectId, final NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void reduceCurrentHp(final double i, final Creature attacker, final Skill skill, final int poleHitCount, final boolean crit, final boolean awake, final boolean standUp, final boolean directHp, final boolean canReflect, final boolean transferDamage, final boolean isDot, final boolean sendMessage)
	{
		if(attacker != null && !attacker.isInZone(Zone.ZoneType.epic))
		{
			attacker.sendPacket(Msg.THE_ATTACK_HAS_BEEN_BLOCKED);
			return;
		}
		super.reduceCurrentHp(i, attacker, skill, poleHitCount, crit, awake, standUp, directHp, canReflect, transferDamage, isDot, sendMessage);
	}

	@Override
	public boolean canChampion()
	{
		return false;
	}
}
