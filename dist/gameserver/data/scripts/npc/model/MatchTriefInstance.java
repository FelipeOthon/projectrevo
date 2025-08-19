package npc.model;

import ai.MatchTrief;
import l2s.commons.util.Rnd;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.CTBBossInstance;
import l2s.gameserver.templates.npc.NpcTemplate;

public class MatchTriefInstance extends CTBBossInstance
{
	private long _massiveDamage;

	public MatchTriefInstance(final int objectId, final NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void reduceCurrentHp(double damage, final Creature attacker, final Skill skill, final int poleHitCount, final boolean crit, final boolean awake, final boolean standUp, final boolean directHp, final boolean canReflect, final boolean transferDamage, final boolean isDot, final boolean sendMessage)
	{
		if(_massiveDamage > System.currentTimeMillis())
		{
			damage = 10000.0;
			if(Rnd.chance(10))
				((MatchTrief) getAI()).hold();
		}
		else if(getCurrentHpPercents() > 50.0)
		{
			if(attacker.isPlayer())
				damage = damage / getMaxHp() / 0.05 * 100.0;
			else
				damage = damage / getMaxHp() / 0.05 * 10.0;
		}
		else if(getCurrentHpPercents() > 30.0)
		{
			if(Rnd.chance(90))
			{
				if(attacker.isPlayer())
					damage = damage / getMaxHp() / 0.05 * 100.0;
				else
					damage = damage / getMaxHp() / 0.05 * 10.0;
			}
			else
				_massiveDamage = System.currentTimeMillis() + 5000L;
		}
		else
			_massiveDamage = System.currentTimeMillis() + 5000L;
		super.reduceCurrentHp(damage, attacker, skill, poleHitCount, crit, awake, standUp, directHp, canReflect, transferDamage, isDot, sendMessage);
	}
}
