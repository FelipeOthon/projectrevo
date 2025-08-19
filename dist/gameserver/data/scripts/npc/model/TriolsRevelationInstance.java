package npc.model;

import l2s.gameserver.instancemanager.ZoneManager;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.instances.MonsterInstance;
import l2s.gameserver.templates.npc.NpcTemplate;

public class TriolsRevelationInstance extends MonsterInstance
{
	private static final int CODE = 31357;
	private boolean _active;

	public TriolsRevelationInstance(final int objectId, final NpcTemplate template)
	{
		super(objectId, template);
		setImmobilized(true);
	}

	@Override
	public void onSpawn()
	{
		ZoneManager.getInstance().getZoneById(Zone.ZoneType.poison, getNpcId() - 31357, false).setActive(true);
		_active = true;
		super.onSpawn();
	}

	@Override
	public void deleteMe()
	{
		if(_active)
			ZoneManager.getInstance().getZoneById(Zone.ZoneType.poison, getNpcId() - 31357, false).setActive(false);
		super.deleteMe();
	}

	@Override
	public void onDeath(final Creature killer)
	{
		super.onDeath(killer);
		if(_active)
		{
			_active = false;
			final Zone zone = ZoneManager.getInstance().getZoneById(Zone.ZoneType.poison, getNpcId() - 31357, false);
			zone.setActive(false);
			for(final Playable p : zone.getInsidePlayables())
				if(p != null)
					p.getAbnormalList().stop(4149);
		}
	}

	@Override
	public boolean canChampion()
	{
		return false;
	}
}
