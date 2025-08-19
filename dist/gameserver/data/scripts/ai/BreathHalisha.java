package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.Fighter;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;
import l2s.gameserver.tables.SkillTable;
import l2s.gameserver.utils.Location;

public class BreathHalisha extends Fighter
{
	private static final int range = 1000;
	private long tele;

	public BreathHalisha(final NpcInstance actor)
	{
		super(actor);
		tele = 0L;
	}

	@Override
	protected boolean createNewTask()
	{
		final NpcInstance actor = getActor();
		if(actor == null || actor.isDead())
			return false;
		final Creature target;
		if(tele < System.currentTimeMillis() && (target = prepareTarget()) != null)
		{
			final int x = target.getX() + Rnd.get(1000) - 500;
			final int y = target.getY() + Rnd.get(1000) - 500;
			final int z = target.getZ();
			if(actor.getDistance(x, y) > 1000.0)
			{
				actor.broadcastPacket(new L2GameServerPacket[] { new MagicSkillUse(actor, target, 1086, 1, 0, 0L) });
				actor.teleToLocation(Location.findAroundPosition(x, y, z, 20, 70, actor.getGeoIndex()));
				tele = System.currentTimeMillis() + 30000L;
			}
		}
		return super.createNewTask();
	}

	@Override
	protected void onEvtAttacked(final Creature attacker, final Skill skill, final int damage)
	{
		final NpcInstance actor = getActor();
		if(actor.getCurrentHpPercents() <= 25.0)
		{
			actor.doCast(SkillTable.getInstance().getInfo(5011, 1), attacker, true);
			actor.doDie(attacker);
		}
		super.onEvtAttacked(attacker, skill, damage);
	}
}
