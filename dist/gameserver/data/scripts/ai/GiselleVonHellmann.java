package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.SiegeGuardMystic;
import l2s.gameserver.instancemanager.ZoneManager;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.entity.events.impl.ClanHallSiegeEvent;
import l2s.gameserver.model.entity.events.objects.SpawnExObject;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.tables.SkillTable;
import l2s.gameserver.utils.Util;

public class GiselleVonHellmann extends SiegeGuardMystic
{
	private static final Skill DAMAGE_SKILL;
	private static final Zone ZONE_1;
	private static final Zone ZONE_2;

	public GiselleVonHellmann(final NpcInstance actor)
	{
		super(actor);
	}

	@Override
	public void onEvtSpawn()
	{
		super.onEvtSpawn();
		GiselleVonHellmann.ZONE_1.setActive(true);
		GiselleVonHellmann.ZONE_2.setActive(true);
		Functions.npcShout(getActor(), "Arise, my faithful servants! You, my people who have inherited the blood.  It is the calling of my daughter.  The feast of blood will now begin!", 0);
	}

	@Override
	public void onEvtDead(final Creature killer)
	{
		final NpcInstance actor = getActor();
		super.onEvtDead(killer);
		GiselleVonHellmann.ZONE_1.setActive(false);
		GiselleVonHellmann.ZONE_2.setActive(false);
		Functions.npcShout(actor, "Aargh...!  If I die, then the magic force field of blood will...!", 0);
		final ClanHallSiegeEvent siegeEvent = actor.getEvent(ClanHallSiegeEvent.class);
		if(siegeEvent == null)
			return;
		final SpawnExObject spawnExObject = (SpawnExObject) siegeEvent.getFirstObject("boss");
		final NpcInstance lidiaNpc = spawnExObject.getFirstSpawned();
		if(lidiaNpc.getCurrentHpRatio() == 1.0)
			lidiaNpc.setCurrentHp(lidiaNpc.getMaxHp() / 2, false);
	}

	@Override
	public void onEvtAttacked(final Creature attacker, final Skill skill, final int damage)
	{
		final NpcInstance actor = getActor();
		super.onEvtAttacked(attacker, skill, damage);
		if(Util.calculateDistance(attacker, actor, false) > 300.0 && Rnd.chance(0.13))
			addTaskCast(attacker, GiselleVonHellmann.DAMAGE_SKILL);
	}

	static
	{
		DAMAGE_SKILL = SkillTable.getInstance().getInfo(5003, 1);
		ZONE_1 = ZoneManager.getInstance().getZoneById(Zone.ZoneType.poison, 501, false);
		ZONE_2 = ZoneManager.getInstance().getZoneById(Zone.ZoneType.poison, 502, false);
	}
}
