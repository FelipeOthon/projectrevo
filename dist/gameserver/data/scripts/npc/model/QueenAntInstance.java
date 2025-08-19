package npc.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Spawn;
import l2s.gameserver.model.instances.BossInstance;
import l2s.gameserver.model.instances.MonsterInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.PlaySound;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.tables.NpcTable;
import l2s.gameserver.templates.npc.NpcTemplate;
import l2s.gameserver.utils.Location;

public class QueenAntInstance extends BossInstance
{
	private static Logger _log = LoggerFactory.getLogger(QueenAntInstance.class);

	private static final int Queen_Ant_Larva = 29002;
	private List<Spawn> _spawns;
	private NpcInstance Larva;

	public QueenAntInstance(final int objectId, final NpcTemplate template)
	{
		super(objectId, template);
		_spawns = new ArrayList<Spawn>();
		Larva = null;
	}

	public NpcInstance getLarva()
	{
		if(Larva == null)
			Larva = SpawnNPC(29002, new Location(-21600, 179482, -5846, Rnd.get(0, 65535)));
		return Larva;
	}

	@Override
	protected int getKilledInterval(final MonsterInstance minion)
	{
		return minion.getNpcId() == 29003 ? Config.NURSE_ANT_RESP : 280000 + Rnd.get(40000);
	}

	@Override
	public void onDeath(final Creature killer)
	{
		this.broadcastPacketToOthers(new L2GameServerPacket[] { new PlaySound(1, "BS02_D", 1, 0, getLoc()) });
		Functions.deSpawnNPCs(_spawns);
		Larva = null;
		_spawns.clear();
		super.onDeath(killer);
	}

	@Override
	public void spawnMe()
	{
		super.spawnMe();
		getLarva();
		this.broadcastPacketToOthers(new L2GameServerPacket[] { new PlaySound(1, "BS01_A", 1, 0, getLoc()) });
	}

	private NpcInstance SpawnNPC(final int npcId, final Location loc)
	{
		final NpcTemplate template = NpcTable.getTemplate(npcId);
		if(template == null)
		{
			_log.warn("WARNING! template is null for npc: " + npcId);
			Thread.dumpStack();
			return null;
		}
		try
		{
			final Spawn sp = new Spawn(template);
			sp.setLoc(loc);
			sp.setAmount(1);
			sp.setRespawnDelay(5);
			sp.startRespawn();
			_spawns.add(sp);
			return sp.spawnOne();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
