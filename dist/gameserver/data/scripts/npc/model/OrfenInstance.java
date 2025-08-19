package npc.model;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.CtrlIntention;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.BossInstance;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.PlaySound;
import l2s.gameserver.templates.npc.NpcTemplate;
import l2s.gameserver.utils.Location;

public class OrfenInstance extends BossInstance
{
	public static final Location nest;
	public static final Location[] locs;

	public OrfenInstance(final int objectId, final NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void setTeleported(final boolean flag)
	{
		super.setTeleported(flag);
		final Location loc = flag ? OrfenInstance.nest : OrfenInstance.locs[Rnd.get(OrfenInstance.locs.length)];
		setSpawnedLoc(loc);
		getAggroList().clear(true);
		getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE, null, null);
		this.teleToLocation(loc);
	}

	@Override
	public void spawnMe()
	{
		super.spawnMe();
		setTeleported(false);
		this.broadcastPacketToOthers(new L2GameServerPacket[] { new PlaySound(1, "BS01_A", 1, 0, getLoc()) });
	}

	@Override
	public void onDeath(final Creature killer)
	{
		this.broadcastPacketToOthers(new L2GameServerPacket[] { new PlaySound(1, "BS02_D", 1, 0, getLoc()) });
		super.onDeath(killer);
	}

	@Override
	public void reduceCurrentHp(final double damage, final Creature attacker, final Skill skill, final int poleHitCount, final boolean crit, final boolean awake, final boolean standUp, final boolean directHp, final boolean canReflect, final boolean transferDamage, final boolean isDot, final boolean sendMessage)
	{
		super.reduceCurrentHp(damage, attacker, skill, poleHitCount, crit, awake, standUp, directHp, canReflect, transferDamage, isDot, sendMessage);
		if(!isTeleported() && getCurrentHpPercents() <= 50.0)
			setTeleported(true);
	}

	static
	{
		nest = new Location(43728, 17220, -4342);
		locs = new Location[] { new Location(55024, 17368, -5412), new Location(53504, 21248, -5496), new Location(53248, 24576, -5272) };
	}
}
