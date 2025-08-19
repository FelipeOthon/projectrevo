package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.CtrlEvent;
import l2s.gameserver.ai.Fighter;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.Spawn;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.tables.NpcTable;
import l2s.gameserver.utils.Location;

public class Pronghorn extends Fighter
{
	private boolean _mobsNotSpawned;
	private static final int PRONGHORN = 22088;
	private static final int PRONGHORN_SPIRIT = 22087;
	private static final int LOST_BUFFALO = 22093;

	public Pronghorn(final NpcInstance actor)
	{
		super(actor);
		_mobsNotSpawned = true;
	}

	@Override
	protected void onEvtSeeSpell(final Skill skill, final Creature caster)
	{
		final NpcInstance actor = getActor();
		if(actor == null || skill.isMagic() || !skill.isOffensive() || skill.isHandler())
			return;
		if(_mobsNotSpawned)
		{
			_mobsNotSpawned = false;
			final int spawnId = actor.getNpcId() == 22088 ? 22087 : 22093;
			for(int i = 0; i < Rnd.get(6, 8); ++i)
				try
				{
					final Spawn sp = new Spawn(NpcTable.getTemplate(spawnId));
					sp.setLoc(Location.findAroundPosition(actor.getLoc(), 100, 120, actor.getGeoIndex()));
					final NpcInstance npc = sp.doSpawn(true);
					if(caster.isSummon())
						npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, caster, Rnd.get(2, 100));
					npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, caster.getPlayer(), Rnd.get(1, 100));
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
		}
	}

	@Override
	protected void onEvtSpawn()
	{
		_mobsNotSpawned = true;
		super.onEvtSpawn();
	}

	@Override
	protected boolean randomWalk()
	{
		return _mobsNotSpawned;
	}
}
