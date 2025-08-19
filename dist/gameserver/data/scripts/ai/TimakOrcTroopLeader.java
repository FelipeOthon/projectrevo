package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.CtrlEvent;
import l2s.gameserver.ai.Fighter;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.Spawn;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.tables.NpcTable;
import l2s.gameserver.utils.Location;

public class TimakOrcTroopLeader extends Fighter
{
	private boolean _firstTimeAttacked;
	private static final int[] BROTHERS;

	public TimakOrcTroopLeader(final NpcInstance actor)
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
			Functions.npcShout(actor, "Show yourselves!", 2000);
			for(final int bro : TimakOrcTroopLeader.BROTHERS)
				try
				{
					final Spawn spawn = new Spawn(NpcTable.getTemplate(bro));
					spawn.setLoc(Location.findPointToStay(actor.getLoc(), 100, 120, actor.getGeoIndex()));
					final NpcInstance npc = spawn.doSpawn(true);
					npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, Rnd.get(1, 100));
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
		}
		super.onEvtAttacked(attacker, skill, damage);
	}

	@Override
	protected void onEvtDead(final Creature killer)
	{
		_firstTimeAttacked = true;
		super.onEvtDead(killer);
	}

	static
	{
		BROTHERS = new int[] { 20768, 20769, 20770 };
	}
}
