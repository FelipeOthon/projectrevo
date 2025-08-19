package npc.model;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.CtrlEvent;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Spawn;
import l2s.gameserver.model.instances.MonsterInstance;
import l2s.gameserver.tables.NpcTable;
import l2s.gameserver.templates.npc.NpcTemplate;

public class FOGInstance extends MonsterInstance
{
	public boolean _canDuplicate;

	public FOGInstance(final int objectId, final NpcTemplate template)
	{
		super(objectId, template);
		_canDuplicate = true;
	}

	@Override
	public void onDeath(final Creature killer)
	{
		super.onDeath(killer);
		if(killer == null)
			return;
		if(_canDuplicate && Rnd.chance(40))
			for(int c = Rnd.get(2, 5), i = 0; i < c; ++i)
				try
				{
					final Spawn spawn = new Spawn(NpcTable.getTemplate(getNpcId()));
					spawn.setLoc(getLoc());
					final FOGInstance npc = (FOGInstance) spawn.doSpawn(true);
					npc._canDuplicate = false;
					npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, killer, Rnd.get(1, 100));
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
	}
}
