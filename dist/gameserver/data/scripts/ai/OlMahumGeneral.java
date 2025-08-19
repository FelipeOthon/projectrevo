package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.Ranger;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;

public class OlMahumGeneral extends Ranger
{
	private boolean _firstTimeAttacked;

	public OlMahumGeneral(final NpcInstance actor)
	{
		super(actor);
		_firstTimeAttacked = true;
	}

	@Override
	protected void onEvtAttacked(final Creature attacker, final Skill skill, final int damage)
	{
		final NpcInstance _thisActor = getActor();
		if(_firstTimeAttacked)
		{
			_firstTimeAttacked = false;
			if(Rnd.chance(25))
				Functions.npcShout(_thisActor, "We shall see about that!", 2000);
		}
		else if(Rnd.chance(10))
			Functions.npcShout(_thisActor, "I will definitely repay this humiliation!", 2000);
		super.onEvtAttacked(attacker, skill, damage);
	}

	@Override
	protected void onEvtDead(final Creature killer)
	{
		_firstTimeAttacked = true;
		super.onEvtDead(killer);
	}
}
