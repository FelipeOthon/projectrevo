package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.SiegeGuardFighter;
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

public class AlfredVonHellmann extends SiegeGuardFighter
{
	public static final Skill DAMAGE_SKILL;
	public static final Skill DRAIN_SKILL;
	private static Zone ZONE_3;

	public AlfredVonHellmann(final NpcInstance actor)
	{
		super(actor);
	}

	@Override
	public void onEvtSpawn()
	{
		super.onEvtSpawn();
		AlfredVonHellmann.ZONE_3.setActive(true);
		Functions.npcShout(getActor(), "Heh Heh... I see that the feast has begun! Be wary! The curse of the Hellmann family has poisoned this land!", 0);
	}

	@Override
	public void onEvtDead(final Creature killer)
	{
		final NpcInstance actor = getActor();
		super.onEvtDead(killer);
		AlfredVonHellmann.ZONE_3.setActive(false);
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
			addTaskCast(attacker, AlfredVonHellmann.DRAIN_SKILL);
		final Creature target = actor.getAggroList().getMostHated();
		if(target == attacker && Rnd.chance(0.3))
			addTaskCast(attacker, AlfredVonHellmann.DAMAGE_SKILL);
	}

	static
	{
		DAMAGE_SKILL = SkillTable.getInstance().getInfo(5000, 1);
		DRAIN_SKILL = SkillTable.getInstance().getInfo(5001, 1);
		AlfredVonHellmann.ZONE_3 = ZoneManager.getInstance().getZoneById(Zone.ZoneType.poison, 503, false);
	}
}
