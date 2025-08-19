package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;

public class BrekaOrcOverlord extends DefaultAI
{
	private static boolean _firstTimeAttacked;

	public BrekaOrcOverlord(final NpcInstance actor)
	{
		super(actor);
		BrekaOrcOverlord._firstTimeAttacked = true;
	}

	@Override
	protected void onEvtAttacked(final Creature attacker, final Skill skill, final int damage)
	{
		final NpcInstance _thisActor = getActor();
		if(_thisActor == null)
			return;
		if(BrekaOrcOverlord._firstTimeAttacked)
		{
			if(Rnd.get(100) == 50)
				Functions.npcSay(_thisActor, "Extreme strength! ! ! !");
			else if(Rnd.get(100) == 50)
				Functions.npcSay(_thisActor, "Humph, wanted to win me to be also in tender!");
			else if(Rnd.get(100) == 50)
				Functions.npcSay(_thisActor, "Haven't thought to use this unique skill for this small thing!");
			BrekaOrcOverlord._firstTimeAttacked = false;
		}
		super.onEvtAttacked(attacker, skill, damage);
	}

	@Override
	protected void onEvtDead(final Creature killer)
	{
		BrekaOrcOverlord._firstTimeAttacked = true;
		super.onEvtDead(killer);
	}
}
