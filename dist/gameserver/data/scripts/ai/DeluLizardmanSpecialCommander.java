package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.Fighter;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;

public class DeluLizardmanSpecialCommander extends Fighter
{
	private boolean _firstTimeAttacked;

	public DeluLizardmanSpecialCommander(final NpcInstance actor)
	{
		super(actor);
		_firstTimeAttacked = true;
	}

	@Override
	protected void onEvtAttacked(final Creature attacker, final Skill skill, final int damage)
	{
		final NpcInstance actor = getActor();
		if(_firstTimeAttacked)
		{
			_firstTimeAttacked = false;
			if(Rnd.chance(40))
				Functions.npcShout(actor, "Come on, Ill take you on!", 2000);
		}
		else if(Rnd.chance(15))
			Functions.npcShout(actor, "How dare you interrupt a sacred duel! You must be taught a lesson!", 2000);
		super.onEvtAttacked(attacker, skill, damage);
	}

	@Override
	protected void onEvtSpawn()
	{
		_firstTimeAttacked = true;
		super.onEvtSpawn();
	}
}
