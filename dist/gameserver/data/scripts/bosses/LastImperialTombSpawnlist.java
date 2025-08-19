package bosses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbcp.DbUtils;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.model.Spawn;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.NpcTable;
import l2s.gameserver.templates.npc.NpcTemplate;

public class LastImperialTombSpawnlist extends Functions implements ScriptFile
{
	private static final Logger _log = LoggerFactory.getLogger(LastImperialTombSpawnlist.class);

	private static List<Spawn> _Room1SpawnList1st;
	private static List<Spawn> _Room1SpawnList2nd;
	private static List<Spawn> _Room1SpawnList3rd;
	private static List<Spawn> _Room1SpawnList4th;
	private static List<Spawn> _Room2InsideSpawnList;
	private static List<Spawn> _Room2OutsideSpawnList;

	public static void fill()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM lastimperialtomb_spawnlist");
			rset = statement.executeQuery();
			while(rset.next())
			{
				final int npcTemplateId = rset.getInt("npc_templateid");
				final NpcTemplate npcTemplate = NpcTable.getTemplate(npcTemplateId);
				if(npcTemplate != null)
				{
					final Spawn spawnDat = new Spawn(npcTemplate);
					spawnDat.setAmount(rset.getInt("count"));
					spawnDat.setLocx(rset.getInt("locx"));
					spawnDat.setLocy(rset.getInt("locy"));
					spawnDat.setLocz(rset.getInt("locz"));
					spawnDat.setHeading(rset.getInt("heading"));
					spawnDat.setRespawnDelay(rset.getInt("respawn_delay"));
					switch(npcTemplateId)
					{
						case 18328:
						case 18330:
						case 18332:
						{
							LastImperialTombSpawnlist._Room1SpawnList1st.add(spawnDat);
							continue;
						}
						case 18329:
						{
							LastImperialTombSpawnlist._Room1SpawnList2nd.add(spawnDat);
							continue;
						}
						case 18333:
						{
							LastImperialTombSpawnlist._Room1SpawnList3rd.add(spawnDat);
							continue;
						}
						case 18331:
						{
							LastImperialTombSpawnlist._Room1SpawnList4th.add(spawnDat);
							continue;
						}
						case 18339:
						{
							LastImperialTombSpawnlist._Room2InsideSpawnList.add(spawnDat);
							continue;
						}
						case 18334:
						case 18335:
						case 18336:
						case 18337:
						case 18338:
						{
							LastImperialTombSpawnlist._Room2OutsideSpawnList.add(spawnDat);
							continue;
						}
					}
				}
				else
					_log.info("LastImperialTombSpawnlist: Data missing in NPC table for ID: " + npcTemplateId + ".");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		_log.info("LastImperialTombSpawnlist: Loaded " + LastImperialTombSpawnlist._Room1SpawnList1st.size() + " Room1 1st Npc Spawn Locations.");
		_log.info("LastImperialTombSpawnlist: Loaded " + LastImperialTombSpawnlist._Room1SpawnList2nd.size() + " Room1 2nd Npc Spawn Locations.");
		_log.info("LastImperialTombSpawnlist: Loaded " + LastImperialTombSpawnlist._Room1SpawnList3rd.size() + " Room1 3rd Npc Spawn Locations.");
		_log.info("LastImperialTombSpawnlist: Loaded " + LastImperialTombSpawnlist._Room1SpawnList4th.size() + " Room1 4th Npc Spawn Locations.");
		_log.info("LastImperialTombSpawnlist: Loaded " + LastImperialTombSpawnlist._Room2InsideSpawnList.size() + " Room2 Inside Npc Spawn Locations.");
		_log.info("LastImperialTombSpawnlist: Loaded " + LastImperialTombSpawnlist._Room2OutsideSpawnList.size() + " Room2 Outside Npc Spawn Locations.");
	}

	public static void clear()
	{
		LastImperialTombSpawnlist._Room1SpawnList1st.clear();
		LastImperialTombSpawnlist._Room1SpawnList2nd.clear();
		LastImperialTombSpawnlist._Room1SpawnList3rd.clear();
		LastImperialTombSpawnlist._Room1SpawnList4th.clear();
		LastImperialTombSpawnlist._Room2InsideSpawnList.clear();
		LastImperialTombSpawnlist._Room2OutsideSpawnList.clear();
	}

	public static List<Spawn> getRoom1SpawnList1st()
	{
		return LastImperialTombSpawnlist._Room1SpawnList1st;
	}

	public static List<Spawn> getRoom1SpawnList2nd()
	{
		return LastImperialTombSpawnlist._Room1SpawnList2nd;
	}

	public static List<Spawn> getRoom1SpawnList3rd()
	{
		return LastImperialTombSpawnlist._Room1SpawnList3rd;
	}

	public static List<Spawn> getRoom1SpawnList4th()
	{
		return LastImperialTombSpawnlist._Room1SpawnList4th;
	}

	public static List<Spawn> getRoom2InsideSpawnList()
	{
		return LastImperialTombSpawnlist._Room2InsideSpawnList;
	}

	public static List<Spawn> getRoom2OutsideSpawnList()
	{
		return LastImperialTombSpawnlist._Room2OutsideSpawnList;
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
		LastImperialTombSpawnlist._Room1SpawnList1st = new ArrayList<Spawn>();
		LastImperialTombSpawnlist._Room1SpawnList2nd = new ArrayList<Spawn>();
		LastImperialTombSpawnlist._Room1SpawnList3rd = new ArrayList<Spawn>();
		LastImperialTombSpawnlist._Room1SpawnList4th = new ArrayList<Spawn>();
		LastImperialTombSpawnlist._Room2InsideSpawnList = new ArrayList<Spawn>();
		LastImperialTombSpawnlist._Room2OutsideSpawnList = new ArrayList<Spawn>();
	}
}
