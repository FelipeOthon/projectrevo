package bosses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbcp.DbUtils;
import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.idfactory.IdFactory;
import l2s.gameserver.model.instances.DoorInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.DoorTable;
import l2s.gameserver.tables.NpcTable;
import l2s.gameserver.templates.npc.NpcTemplate;
import l2s.gameserver.utils.Location;
import l2s.gameserver.utils.Log;
import npc.model.SepulcherMonsterInstance;
import npc.model.SepulcherNpcInstance;
import npc.model.SepulcherRaidInstance;

public class FourSepulchersSpawn extends Functions implements ScriptFile
{
	private static Logger _log = LoggerFactory.getLogger(FourSepulchersSpawn.class);

	public static Map<Integer, Location> _shadowSpawns;
	public static Map<Integer, Location> _mysteriousBoxSpawns;
	public static Map<Integer, List<Location>> _dukeFinalMobs;
	public static Map<Integer, List<Location>> _emperorsGraveNpcs;
	public static Map<Integer, List<Location>> _magicalMonsters;
	public static Map<Integer, List<Location>> _physicalMonsters;
	public static Map<Integer, Location> _startHallSpawns;
	public static Map<Integer, Boolean> _hallInUse;
	public static List<GateKeeper> _GateKeepers;
	public static Map<Integer, Integer> _keyBoxNpc;
	public static Map<Integer, Integer> _victim;
	public static Map<Integer, Boolean> _archonSpawned;
	public static List<Integer> _shadows;
	public static Map<Integer, List<SepulcherMonsterInstance>> _dukeMobs;
	public static Map<Integer, List<SepulcherMonsterInstance>> _viscountMobs;
	private static List<Integer> _boxes;
	public static List<SepulcherNpcInstance> _managers;
	public static List<NpcInstance> _allMobs;
	private static Location[] _startHallSpawn;
	private static Location[][] _shadowSpawnLoc;

	public static void init()
	{
		initFixedInfo();
		loadMysteriousBox();
		loadPhysicalMonsters();
		loadMagicalMonsters();
		initLocationShadowSpawns();
		loadDukeMonsters();
		loadEmperorsGraveMonsters();
		spawnManagers();
	}

	private static void initFixedInfo()
	{
		FourSepulchersSpawn._startHallSpawns.put(31921, FourSepulchersSpawn._startHallSpawn[0]);
		FourSepulchersSpawn._startHallSpawns.put(31922, FourSepulchersSpawn._startHallSpawn[1]);
		FourSepulchersSpawn._startHallSpawns.put(31923, FourSepulchersSpawn._startHallSpawn[2]);
		FourSepulchersSpawn._startHallSpawns.put(31924, FourSepulchersSpawn._startHallSpawn[3]);
		FourSepulchersSpawn._hallInUse.put(31921, false);
		FourSepulchersSpawn._hallInUse.put(31922, false);
		FourSepulchersSpawn._hallInUse.put(31923, false);
		FourSepulchersSpawn._hallInUse.put(31924, false);
		FourSepulchersSpawn._GateKeepers.add(new GateKeeper(31925, 182727, -85493, -7200, -32584, 25150012));
		FourSepulchersSpawn._GateKeepers.add(new GateKeeper(31926, 184547, -85479, -7200, -32584, 25150013));
		FourSepulchersSpawn._GateKeepers.add(new GateKeeper(31927, 186349, -85473, -7200, -32584, 25150014));
		FourSepulchersSpawn._GateKeepers.add(new GateKeeper(31928, 188154, -85463, -7200, -32584, 25150015));
		FourSepulchersSpawn._GateKeepers.add(new GateKeeper(31929, 189947, -85466, -7200, -32584, 25150016));
		FourSepulchersSpawn._GateKeepers.add(new GateKeeper(31930, 181030, -88868, -7200, -33272, 25150002));
		FourSepulchersSpawn._GateKeepers.add(new GateKeeper(31931, 182809, -88856, -7200, -33272, 25150003));
		FourSepulchersSpawn._GateKeepers.add(new GateKeeper(31932, 184626, -88859, -7200, -33272, 25150004));
		FourSepulchersSpawn._GateKeepers.add(new GateKeeper(31933, 186438, -88858, -7200, -33272, 25150005));
		FourSepulchersSpawn._GateKeepers.add(new GateKeeper(31934, 188236, -88854, -7200, -33272, 25150006));
		FourSepulchersSpawn._GateKeepers.add(new GateKeeper(31935, 173102, -85105, -7200, -16248, 25150032));
		FourSepulchersSpawn._GateKeepers.add(new GateKeeper(31936, 173101, -83280, -7200, -16248, 25150033));
		FourSepulchersSpawn._GateKeepers.add(new GateKeeper(31937, 173103, -81479, -7200, -16248, 25150034));
		FourSepulchersSpawn._GateKeepers.add(new GateKeeper(31938, 173086, -79698, -7200, -16248, 25150035));
		FourSepulchersSpawn._GateKeepers.add(new GateKeeper(31939, 173083, -77896, -7200, -16248, 25150036));
		FourSepulchersSpawn._GateKeepers.add(new GateKeeper(31940, 175497, -81265, -7200, -16248, 25150022));
		FourSepulchersSpawn._GateKeepers.add(new GateKeeper(31941, 175495, -79468, -7200, -16248, 25150023));
		FourSepulchersSpawn._GateKeepers.add(new GateKeeper(31942, 175488, -77652, -7200, -16248, 25150024));
		FourSepulchersSpawn._GateKeepers.add(new GateKeeper(31943, 175489, -75856, -7200, -16248, 25150025));
		FourSepulchersSpawn._GateKeepers.add(new GateKeeper(31944, 175478, -74049, -7200, -16248, 25150026));
		FourSepulchersSpawn._keyBoxNpc.put(18120, 31455);
		FourSepulchersSpawn._keyBoxNpc.put(18121, 31455);
		FourSepulchersSpawn._keyBoxNpc.put(18122, 31455);
		FourSepulchersSpawn._keyBoxNpc.put(18123, 31455);
		FourSepulchersSpawn._keyBoxNpc.put(18124, 31456);
		FourSepulchersSpawn._keyBoxNpc.put(18125, 31456);
		FourSepulchersSpawn._keyBoxNpc.put(18126, 31456);
		FourSepulchersSpawn._keyBoxNpc.put(18127, 31456);
		FourSepulchersSpawn._keyBoxNpc.put(18128, 31457);
		FourSepulchersSpawn._keyBoxNpc.put(18129, 31457);
		FourSepulchersSpawn._keyBoxNpc.put(18130, 31457);
		FourSepulchersSpawn._keyBoxNpc.put(18131, 31457);
		FourSepulchersSpawn._keyBoxNpc.put(18149, 31458);
		FourSepulchersSpawn._keyBoxNpc.put(18150, 31459);
		FourSepulchersSpawn._keyBoxNpc.put(18151, 31459);
		FourSepulchersSpawn._keyBoxNpc.put(18152, 31459);
		FourSepulchersSpawn._keyBoxNpc.put(18153, 31459);
		FourSepulchersSpawn._keyBoxNpc.put(18154, 31460);
		FourSepulchersSpawn._keyBoxNpc.put(18155, 31460);
		FourSepulchersSpawn._keyBoxNpc.put(18156, 31460);
		FourSepulchersSpawn._keyBoxNpc.put(18157, 31460);
		FourSepulchersSpawn._keyBoxNpc.put(18158, 31461);
		FourSepulchersSpawn._keyBoxNpc.put(18159, 31461);
		FourSepulchersSpawn._keyBoxNpc.put(18160, 31461);
		FourSepulchersSpawn._keyBoxNpc.put(18161, 31461);
		FourSepulchersSpawn._keyBoxNpc.put(18162, 31462);
		FourSepulchersSpawn._keyBoxNpc.put(18163, 31462);
		FourSepulchersSpawn._keyBoxNpc.put(18164, 31462);
		FourSepulchersSpawn._keyBoxNpc.put(18165, 31462);
		FourSepulchersSpawn._keyBoxNpc.put(18183, 31463);
		FourSepulchersSpawn._keyBoxNpc.put(18184, 31464);
		FourSepulchersSpawn._keyBoxNpc.put(18212, 31465);
		FourSepulchersSpawn._keyBoxNpc.put(18213, 31465);
		FourSepulchersSpawn._keyBoxNpc.put(18214, 31465);
		FourSepulchersSpawn._keyBoxNpc.put(18215, 31465);
		FourSepulchersSpawn._keyBoxNpc.put(18216, 31466);
		FourSepulchersSpawn._keyBoxNpc.put(18217, 31466);
		FourSepulchersSpawn._keyBoxNpc.put(18218, 31466);
		FourSepulchersSpawn._keyBoxNpc.put(18219, 31466);
		FourSepulchersSpawn._victim.put(18150, 18158);
		FourSepulchersSpawn._victim.put(18151, 18159);
		FourSepulchersSpawn._victim.put(18152, 18160);
		FourSepulchersSpawn._victim.put(18153, 18161);
		FourSepulchersSpawn._victim.put(18154, 18162);
		FourSepulchersSpawn._victim.put(18155, 18163);
		FourSepulchersSpawn._victim.put(18156, 18164);
		FourSepulchersSpawn._victim.put(18157, 18165);
	}

	private static void initLocationShadowSpawns()
	{
		final int locNo = Config.FS_SPAWN >= 0 ? Config.FS_SPAWN : Rnd.get(4);
		final int[] gateKeeper = { 31929, 31934, 31939, 31944 };
		FourSepulchersSpawn._shadowSpawns.clear();
		for(int i = 0; i <= 3; ++i)
			FourSepulchersSpawn._shadowSpawns.put(gateKeeper[i], FourSepulchersSpawn._shadowSpawnLoc[locNo][i].clone());
	}

	private static void loadEmperorsGraveMonsters()
	{
		FourSepulchersSpawn._emperorsGraveNpcs.clear();
		final int count = loadSpawn(FourSepulchersSpawn._emperorsGraveNpcs, 6);
		FourSepulchersSpawn._log.info("FourSepulchersManager: loaded " + count + " Emperor's grave NPC spawns.");
	}

	private static void loadDukeMonsters()
	{
		FourSepulchersSpawn._dukeFinalMobs.clear();
		FourSepulchersSpawn._archonSpawned.clear();
		final int count = loadSpawn(FourSepulchersSpawn._dukeFinalMobs, 5);
		for(final Integer npcId : FourSepulchersSpawn._dukeFinalMobs.keySet())
			FourSepulchersSpawn._archonSpawned.put(npcId, false);
		FourSepulchersSpawn._log.info("FourSepulchersManager: loaded " + count + " Church of duke monsters spawns.");
	}

	private static void loadMagicalMonsters()
	{
		FourSepulchersSpawn._magicalMonsters.clear();
		final int count = loadSpawn(FourSepulchersSpawn._magicalMonsters, 2);
		FourSepulchersSpawn._log.info("FourSepulchersManager: loaded " + count + " magical monsters spawns.");
	}

	private static void loadPhysicalMonsters()
	{
		FourSepulchersSpawn._physicalMonsters.clear();
		final int count = loadSpawn(FourSepulchersSpawn._physicalMonsters, 1);
		FourSepulchersSpawn._log.info("FourSepulchersManager: loaded " + count + " physical monsters spawns.");
	}

	private static int loadSpawn(final Map<Integer, List<Location>> table, final int type)
	{
		int count = 0;
		Connection con = null;
		PreparedStatement statement1 = null;
		ResultSet rset1 = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement1 = con.prepareStatement("SELECT DISTINCT key_npc_id FROM four_sepulchers_spawnlist WHERE spawntype = ? ORDER BY key_npc_id");
			statement1.setInt(1, type);
			rset1 = statement1.executeQuery();
			while(rset1.next())
			{
				final int keyNpcId = rset1.getInt("key_npc_id");
				PreparedStatement statement2 = null;
				ResultSet rset2 = null;
				try
				{
					statement2 = con.prepareStatement("SELECT id, count, npc_templateid, locx, locy, locz, heading, respawn_delay, key_npc_id FROM four_sepulchers_spawnlist WHERE key_npc_id = ? AND spawntype = ? ORDER BY id");
					statement2.setInt(1, keyNpcId);
					statement2.setInt(2, type);
					rset2 = statement2.executeQuery();
					final List<Location> locations = new ArrayList<Location>();
					while(rset2.next())
					{
						locations.add(new Location(rset2.getInt("locx"), rset2.getInt("locy"), rset2.getInt("locz"), rset2.getInt("heading"), rset2.getInt("npc_templateid")));
						++count;
					}
					table.put(keyNpcId, locations);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					DbUtils.closeQuietly(statement2, rset2);
				}
			}
		}
		catch(Exception e2)
		{
			e2.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement1, rset1);
		}
		return count;
	}

	private static void loadMysteriousBox()
	{
		FourSepulchersSpawn._mysteriousBoxSpawns.clear();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT id, count, npc_templateid, locx, locy, locz, heading, respawn_delay, key_npc_id FROM four_sepulchers_spawnlist WHERE spawntype = 0 ORDER BY id");
			rset = statement.executeQuery();
			while(rset.next())
				FourSepulchersSpawn._mysteriousBoxSpawns.put(rset.getInt("key_npc_id"), new Location(rset.getInt("locx"), rset.getInt("locy"), rset.getInt("locz"), rset.getInt("heading"), rset.getInt("npc_templateid")));
			FourSepulchersSpawn._log.info("FourSepulchersManager: Loaded " + FourSepulchersSpawn._mysteriousBoxSpawns.size() + " Mysterious-Box spawns.");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}

	private static void spawnManagers()
	{
		FourSepulchersSpawn._managers = new ArrayList<SepulcherNpcInstance>();
		for(int i = 31921; i <= 31924; ++i)
			try
			{
				final NpcTemplate template = NpcTable.getTemplate(i);
				Location loc = null;
				switch(i)
				{
					case 31921:
					{
						loc = new Location(181061, -85595, -7200, -32584);
						break;
					}
					case 31922:
					{
						loc = new Location(179292, -88981, -7200, -33272);
						break;
					}
					case 31923:
					{
						loc = new Location(173202, -87004, -7200, -16248);
						break;
					}
					case 31924:
					{
						loc = new Location(175606, -82853, -7200, -16248);
						break;
					}
				}
				final SepulcherNpcInstance npc = new SepulcherNpcInstance(IdFactory.getInstance().getNextId(), template);
				npc.setSpawnedLoc(loc);
				npc.spawnMe(loc);
				FourSepulchersSpawn._managers.add(npc);
				FourSepulchersSpawn._log.info("FourSepulchersManager: Spawned " + template.name);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
	}

	static void closeAllDoors()
	{
		for(final GateKeeper gk : FourSepulchersSpawn._GateKeepers)
			try
			{
				gk.door.closeMe();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
	}

	public static void deleteAllMobs()
	{
		for(final NpcInstance mob : FourSepulchersSpawn._allMobs)
			mob.deleteMe();
		FourSepulchersSpawn._allMobs.clear();
		FourSepulchersSpawn._boxes.clear();
	}

	public static void spawnShadow(final int npcId)
	{
		if(!FourSepulchersManager.isAttackTime())
			return;
		final Location loc = FourSepulchersSpawn._shadowSpawns.get(npcId);
		if(loc == null)
			return;
		final NpcTemplate template = NpcTable.getTemplate(loc.id);
		final SepulcherRaidInstance mob = new SepulcherRaidInstance(IdFactory.getInstance().getNextId(), template);
		mob.mysteriousBoxId = npcId;
		mob.setSpawnedLoc(loc);
		mob.spawnMe(loc);
		FourSepulchersSpawn._allMobs.add(mob);
	}

	public static void locationShadowSpawns()
	{
		final int locNo = Config.FS_SPAWN >= 0 ? Config.FS_SPAWN : Rnd.get(4);
		final int[] gateKeeper = { 31929, 31934, 31939, 31944 };
		for(int i = 0; i <= 3; ++i)
		{
			final Location loc = FourSepulchersSpawn._shadowSpawns.get(gateKeeper[i]);
			loc.x = FourSepulchersSpawn._shadowSpawnLoc[locNo][i].x;
			loc.y = FourSepulchersSpawn._shadowSpawnLoc[locNo][i].y;
			loc.z = FourSepulchersSpawn._shadowSpawnLoc[locNo][i].z;
			loc.h = FourSepulchersSpawn._shadowSpawnLoc[locNo][i].h;
		}
	}

	public static void spawnEmperorsGraveNpc(final int npcId)
	{
		if(!FourSepulchersManager.isAttackTime())
			return;
		final List<Location> monsterList = FourSepulchersSpawn._emperorsGraveNpcs.get(npcId);
		if(monsterList != null)
			for(final Location loc : monsterList)
			{
				final NpcTemplate template = NpcTable.getTemplate(loc.id);
				NpcInstance npc = null;
				if(template.isInstanceOf(SepulcherMonsterInstance.class))
					npc = new SepulcherMonsterInstance(IdFactory.getInstance().getNextId(), template);
				else
					npc = new SepulcherNpcInstance(IdFactory.getInstance().getNextId(), template);
				npc.setSpawnedLoc(loc);
				npc.spawnMe(loc);
				FourSepulchersSpawn._allMobs.add(npc);
			}
	}

	public static void spawnArchonOfHalisha(final int npcId)
	{
		if(!FourSepulchersManager.isAttackTime())
			return;
		if(FourSepulchersSpawn._archonSpawned.get(npcId))
			return;
		final List<Location> monsterList = FourSepulchersSpawn._dukeFinalMobs.get(npcId);
		if(monsterList == null)
			return;
		for(final Location loc : monsterList)
		{
			final NpcTemplate template = NpcTable.getTemplate(loc.id);
			final SepulcherMonsterInstance mob = new SepulcherMonsterInstance(IdFactory.getInstance().getNextId(), template);
			mob.mysteriousBoxId = npcId;
			mob.setSpawnedLoc(loc);
			mob.spawnMe(loc);
			FourSepulchersSpawn._allMobs.add(mob);
		}
		FourSepulchersSpawn._archonSpawned.put(npcId, true);
	}

	public static void spawnExecutionerOfHalisha(final NpcInstance npc)
	{
		if(!FourSepulchersManager.isAttackTime())
			return;
		final NpcTemplate template = NpcTable.getTemplate(FourSepulchersSpawn._victim.get(npc.getNpcId()));
		final SepulcherMonsterInstance npc2 = new SepulcherMonsterInstance(IdFactory.getInstance().getNextId(), template);
		npc2.setSpawnedLoc(npc.getLoc());
		npc2.spawnMe(npc.getLoc());
		FourSepulchersSpawn._allMobs.add(npc2);
	}

	public static void spawnKeyBox(final NpcInstance npc, final String name)
	{
		if(!FourSepulchersManager.isAttackTime())
			return;
		final NpcTemplate template = NpcTable.getTemplate(FourSepulchersSpawn._keyBoxNpc.get(npc.getNpcId()));
		final SepulcherNpcInstance npc2 = new SepulcherNpcInstance(IdFactory.getInstance().getNextId(), template);
		npc2.setSpawnedLoc(npc.getLoc());
		npc2.spawnMe(npc.getLoc());
		FourSepulchersSpawn._allMobs.add(npc2);
		Log.addLog("KeyBoxNpc " + npc2.getNpcId() + "(" + npc.getNpcId() + ") spawned on " + npc.getLoc().x + " " + npc.getLoc().y + " " + npc.getLoc().z + " | killer: " + name, "sepulchers");
	}

	public static void spawnMonster(final int npcId, final String name)
	{
		if(!FourSepulchersManager.isAttackTime() || FourSepulchersSpawn._boxes.contains(npcId))
			return;
		final List<SepulcherMonsterInstance> mobs = new ArrayList<SepulcherMonsterInstance>();
		final int type = Rnd.get(2);
		List<Location> monsterList;
		if(type == 0)
			monsterList = FourSepulchersSpawn._physicalMonsters.get(npcId);
		else
			monsterList = FourSepulchersSpawn._magicalMonsters.get(npcId);
		if(monsterList != null)
		{
			boolean spawnKeyBoxMob = false;
			boolean spawnedKeyBoxMob = false;
			Location lastLoc = null;
			for(final Location loc : monsterList)
			{
				if(spawnedKeyBoxMob)
					spawnKeyBoxMob = false;
				else
					switch(npcId)
					{
						case 31469:
						case 31474:
						case 31479:
						case 31484:
						{
							if(Rnd.chance(2))
							{
								spawnKeyBoxMob = true;
								spawnedKeyBoxMob = true;
								FourSepulchersSpawn._boxes.add(npcId);
								lastLoc = loc;
								break;
							}
							break;
						}
					}
				final NpcTemplate template = NpcTable.getTemplate(spawnKeyBoxMob ? 18149 : loc.id);
				final SepulcherMonsterInstance mob = new SepulcherMonsterInstance(IdFactory.getInstance().getNextId(), template);
				mob.mysteriousBoxId = npcId;
				mob.setSpawnedLoc(loc);
				mob.spawnMe(loc);
				switch(npcId)
				{
					case 31469:
					case 31472:
					case 31474:
					case 31477:
					case 31479:
					case 31482:
					case 31484:
					case 31487:
					{
						mobs.add(mob);
						break;
					}
				}
				FourSepulchersSpawn._allMobs.add(mob);
			}
			switch(npcId)
			{
				case 31469:
				case 31474:
				case 31479:
				case 31484:
				{
					FourSepulchersSpawn._viscountMobs.put(npcId, mobs);
					break;
				}
				case 31472:
				case 31477:
				case 31482:
				case 31487:
				{
					FourSepulchersSpawn._dukeMobs.put(npcId, mobs);
					break;
				}
			}
			if(spawnedKeyBoxMob)
				Log.addLog("KeyBoxMob spawned on " + lastLoc.x + " " + lastLoc.y + " " + lastLoc.z + " for type " + type + " npcId " + npcId + " | killer: " + name, "sepulchers");
		}
		else
			Log.addLog("Not found type " + type + " mobs for npcId " + npcId, "sepulchers");
	}

	public static void spawnMysteriousBox(final int npcId)
	{
		if(!FourSepulchersManager.isAttackTime())
			return;
		final NpcTemplate template = NpcTable.getTemplate(FourSepulchersSpawn._mysteriousBoxSpawns.get(npcId).id);
		final SepulcherNpcInstance npc = new SepulcherNpcInstance(IdFactory.getInstance().getNextId(), template);
		npc.setSpawnedLoc(FourSepulchersSpawn._mysteriousBoxSpawns.get(npcId));
		npc.spawnMe(npc.getSpawnedLoc());
		FourSepulchersSpawn._allMobs.add(npc);
	}

	public static synchronized boolean isDukeMobsAnnihilated(final int npcId)
	{
		final List<SepulcherMonsterInstance> mobs = FourSepulchersSpawn._dukeMobs.get(npcId);
		if(mobs == null)
			return true;
		for(final SepulcherMonsterInstance mob : mobs)
			if(!mob.isDead())
				return false;
		return true;
	}

	public static synchronized boolean isViscountMobsAnnihilated(final int npcId)
	{
		final List<SepulcherMonsterInstance> mobs = FourSepulchersSpawn._viscountMobs.get(npcId);
		if(mobs == null)
			return true;
		for(final SepulcherMonsterInstance mob : mobs)
			if(!mob.isDead())
				return false;
		return true;
	}

	public static boolean isShadowAlive(final int id)
	{
		final Location loc = FourSepulchersSpawn._shadowSpawns.get(id);
		if(loc == null)
			return true;
		for(final NpcInstance n : FourSepulchersSpawn._allMobs)
			if(n.getNpcId() == loc.id && !n.isDead())
				return true;
		return false;
	}

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	static
	{
		FourSepulchersSpawn._shadowSpawns = new HashMap<Integer, Location>();
		FourSepulchersSpawn._mysteriousBoxSpawns = new HashMap<Integer, Location>();
		FourSepulchersSpawn._dukeFinalMobs = new HashMap<Integer, List<Location>>();
		FourSepulchersSpawn._emperorsGraveNpcs = new HashMap<Integer, List<Location>>();
		FourSepulchersSpawn._magicalMonsters = new HashMap<Integer, List<Location>>();
		FourSepulchersSpawn._physicalMonsters = new HashMap<Integer, List<Location>>();
		FourSepulchersSpawn._startHallSpawns = new HashMap<Integer, Location>();
		FourSepulchersSpawn._hallInUse = new HashMap<Integer, Boolean>();
		FourSepulchersSpawn._GateKeepers = new ArrayList<GateKeeper>();
		FourSepulchersSpawn._keyBoxNpc = new HashMap<Integer, Integer>();
		FourSepulchersSpawn._victim = new HashMap<Integer, Integer>();
		FourSepulchersSpawn._archonSpawned = new HashMap<Integer, Boolean>();
		FourSepulchersSpawn._shadows = new ArrayList<Integer>(4);
		FourSepulchersSpawn._dukeMobs = new HashMap<Integer, List<SepulcherMonsterInstance>>();
		FourSepulchersSpawn._viscountMobs = new HashMap<Integer, List<SepulcherMonsterInstance>>();
		FourSepulchersSpawn._boxes = new ArrayList<Integer>();
		FourSepulchersSpawn._allMobs = new ArrayList<NpcInstance>();
		FourSepulchersSpawn._startHallSpawn = new Location[] {
				new Location(181632, -85587, -7218),
				new Location(179963, -88978, -7218),
				new Location(173217, -86132, -7218),
				new Location(175608, -82296, -7218) };
		FourSepulchersSpawn._shadowSpawnLoc = new Location[][] {
				{
						new Location(191231, -85574, -7216, 33380, 25339),
						new Location(189534, -88969, -7216, 32768, 25349),
						new Location(173195, -76560, -7215, 49277, 25346),
						new Location(175591, -72744, -7215, 49317, 25342) },
				{
						new Location(191231, -85574, -7216, 33380, 25342),
						new Location(189534, -88969, -7216, 32768, 25339),
						new Location(173195, -76560, -7215, 49277, 25349),
						new Location(175591, -72744, -7215, 49317, 25346) },
				{
						new Location(191231, -85574, -7216, 33380, 25346),
						new Location(189534, -88969, -7216, 32768, 25342),
						new Location(173195, -76560, -7215, 49277, 25339),
						new Location(175591, -72744, -7215, 49317, 25349) },
				{
						new Location(191231, -85574, -7216, 33380, 25349),
						new Location(189534, -88969, -7216, 32768, 25346),
						new Location(173195, -76560, -7215, 49277, 25342),
						new Location(175591, -72744, -7215, 49317, 25339) } };
	}

	public static class GateKeeper extends Location
	{
		public final DoorInstance door;
		public final NpcTemplate template;

		public GateKeeper(final int npcId, final int _x, final int _y, final int _z, final int _h, final int doorId)
		{
			super(_x, _y, _z, _h);
			door = DoorTable.getInstance().getDoor(doorId);
			template = NpcTable.getTemplate(npcId);
			if(template == null)
				_log.warn("FourGoblets::Sepulcher::RoomLock npc_template " + npcId + " undefined");
			if(door == null)
				_log.warn("FourGoblets::Sepulcher::RoomLock door id " + doorId + " undefined");
		}
	}
}
