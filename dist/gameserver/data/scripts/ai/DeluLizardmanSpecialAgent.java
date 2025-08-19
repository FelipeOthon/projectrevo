package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.Ranger;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;

public class DeluLizardmanSpecialAgent extends Ranger
{
	private boolean _firstTimeAttacked;

	public DeluLizardmanSpecialAgent(final NpcInstance actor)
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
			if(Rnd.chance(25))
				Functions.npcShout(actor, "How dare you interrupt our fight! Hey guys, help!", 2000);
		}
		else if(Rnd.chance(10))
			Functions.npcShout(actor, "Hey! Were having a duel here!", 2000);
		super.onEvtAttacked(attacker, skill, damage);
	}

	@Override
	protected void onEvtSpawn()
	{
		_firstTimeAttacked = true;
		super.onEvtSpawn();
	}
}
