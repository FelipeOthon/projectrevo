package commands.admin;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import l2s.commons.dbcp.DbUtils;
import l2s.gameserver.Config;
import l2s.gameserver.ai.CharacterAI;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.database.mysql;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.instancemanager.RaidBossSpawnManager;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Spawn;
import l2s.gameserver.model.World;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.scripts.Scripts;
import l2s.gameserver.tables.GmListTable;
import l2s.gameserver.tables.NpcTable;
import l2s.gameserver.tables.SpawnTable;
import l2s.gameserver.templates.npc.NpcTemplate;
import l2s.gameserver.utils.TimeUtils;

public class AdminSpawn implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().CanEditNPC)
			return false;
		if(command.equals("admin_show_spawns"))
			AdminHelpPage.showHelpPage(activeChar, "spawns.htm");
		else if(command.startsWith("admin_spawn_index"))
			try
			{
				final String val = command.substring(18);
				AdminHelpPage.showHelpPage(activeChar, "spawns/" + val + ".htm");
			}
			catch(StringIndexOutOfBoundsException ex)
			{}
		else if(command.startsWith("admin_spawn1"))
		{
			final StringTokenizer st = new StringTokenizer(command, " ");
			try
			{
				st.nextToken();
				final String id = st.nextToken();
				int mobCount = 1;
				if(st.hasMoreTokens())
					mobCount = Integer.parseInt(st.nextToken());
				spawnMonster(activeChar, id, 0, mobCount);
			}
			catch(Exception ex2)
			{}
		}
		else if(command.startsWith("admin_spawn") || command.startsWith("admin_spawn_monster"))
		{
			final StringTokenizer st = new StringTokenizer(command, " ");
			try
			{
				st.nextToken();
				final String id = st.nextToken();
				int respawnTime = 30;
				int mobCount2 = 1;
				if(st.hasMoreTokens())
					mobCount2 = Integer.parseInt(st.nextToken());
				if(st.hasMoreTokens())
					respawnTime = Integer.parseInt(st.nextToken());
				spawnMonster(activeChar, id, respawnTime, mobCount2);
			}
			catch(Exception ex3)
			{}
		}
		else if(command.startsWith("admin_unspawnall"))
		{
			for(final Player player : GameObjectsStorage.getPlayers())
				player.sendPacket(new SystemMessage(1278));
			World.deleteVisibleNpcSpawns();
			GmListTable.broadcastMessageToGMs("NPC Unspawn completed!");
		}
		else if(command.startsWith("admin_setai"))
		{
			if(activeChar.getTarget() == null || !activeChar.getTarget().isNpc())
			{
				activeChar.sendMessage("Please select target NPC or mob.");
				return false;
			}
			final StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken();
			if(!st.hasMoreTokens())
			{
				activeChar.sendMessage("Please specify AI name.");
				return false;
			}
			final String aiName = st.nextToken();
			final NpcInstance target = (NpcInstance) activeChar.getTarget();
			Constructor<?> aiConstructor = null;
			try
			{
				if(!aiName.equalsIgnoreCase("npc"))
					aiConstructor = Class.forName("l2s.gameserver.ai." + aiName).getConstructors()[0];
			}
			catch(Exception e)
			{
				try
				{
					aiConstructor = Scripts.getInstance().getClasses().get("ai." + aiName).getConstructors()[0];
				}
				catch(Exception e2)
				{
					activeChar.sendMessage("This type AI not found.");
					return false;
				}
			}
			if(aiConstructor != null)
			{
				try
				{
					target.setAI((CharacterAI) aiConstructor.newInstance(target));
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				target.getAI().startAITask();
			}
		}
		else if(command.startsWith("admin_setheading"))
		{
			final GameObject obj = activeChar.getTarget();
			if(!obj.isNpc())
			{
				activeChar.sendMessage("Target is incorrect!");
				return false;
			}
			final NpcInstance npc = (NpcInstance) obj;
			npc.setHeading(activeChar.getHeading());
			npc.decayMe();
			npc.spawnMe();
			activeChar.sendMessage("New heading : " + activeChar.getHeading());
			final Spawn spawn = npc.getSpawn();
			if(spawn == null)
			{
				activeChar.sendMessage("Spawn for this npc == null!");
				return false;
			}
			if(!mysql.set("update spawnlist set heading = " + activeChar.getHeading() + " where npc_templateid = " + npc.getNpcId() + " and locx = " + spawn.getLocx() + " and locy = " + spawn.getLocy() + " and locz = " + spawn.getLocz() + " and loc_id = " + spawn.getLocation()))
			{
				activeChar.sendMessage("Error in mysql query!");
				return false;
			}
		}
		else if(command.startsWith("admin_resp"))
		{
			final StringTokenizer st = new StringTokenizer(command, " ");
			try
			{
				st.nextToken();
				final int id2 = Integer.parseInt(st.nextToken());
				final int state = mysql.simple_get_int("`state`", "epic_boss_spawn", "bossId=" + id2);
				long respawnTime2 = state > 1 ? mysql.simple_get_int("respawnDate", "epic_boss_spawn", "bossId=" + id2) * 1000L : 0L;
				if(respawnTime2 <= 0L)
					for(final Spawn sp : SpawnTable.getInstance().getSpawnTable())
						if(sp.getNpcId() == id2)
						{
							respawnTime2 = sp.getRespawnTime() * 1000L;
							break;
						}
				if(respawnTime2 > 0L)
					activeChar.sendMessage("Respawn for npcId " + id2 + ": " + TimeUtils.toSimpleFormat(respawnTime2));
				else
					activeChar.sendMessage("Not found respawn for npcId: " + id2);
			}
			catch(Exception e3)
			{
				activeChar.sendMessage("USAGE: //resp npcId");
			}
		}
		else if(command.startsWith("admin_npcvis"))
		{
			final StringTokenizer st = new StringTokenizer(command, " ");
			try
			{
				st.nextToken();
				final int id2 = Integer.parseInt(st.nextToken());
				for(final NpcInstance npc2 : GameObjectsStorage.getNpcs())
					if(npc2.getNpcId() == id2)
						if(npc2.isVisible())
							npc2.decayMe();
						else
							npc2.spawnMe();
				activeChar.sendMessage("Visibility for npcId: " + id2);
			}
			catch(Exception e3)
			{
				activeChar.sendMessage("USAGE: //npcvis npcId");
			}
		}
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminSpawn._adminCommands;
	}

	private void spawnMonster(final Player activeChar, String monsterId, final int respawnTime, final int mobCount)
	{
		GameObject target = activeChar.getTarget();
		if(target == null)
			target = activeChar;
		final Pattern pattern = Pattern.compile("[0-9]*");
		final Matcher regexp = pattern.matcher(monsterId);
		NpcTemplate template;
		if(regexp.matches())
		{
			final int monsterTemplate = Integer.parseInt(monsterId);
			template = NpcTable.getTemplate(monsterTemplate);
		}
		else
		{
			monsterId = monsterId.replace('_', ' ');
			template = NpcTable.getTemplateByName(monsterId);
		}
		if(template == null)
		{
			activeChar.sendMessage("Incorrect monster template.");
			return;
		}
		try
		{
			final Spawn spawn = new Spawn(template);
			spawn.setLoc(target.getLoc());
			spawn.setLocation(0);
			spawn.setAmount(mobCount);
			spawn.setHeading(activeChar.getHeading());
			spawn.setRespawnDelay(respawnTime);
			if(Config.RAID_ONE_SPAWN && RaidBossSpawnManager.getInstance().isDefined(spawn.getNpcId()))
			{
				activeChar.sendMessage("Raid Boss " + template.name + " already spawned.");
				return;
			}
			spawn.init();
			if(respawnTime == 0)
				spawn.stopRespawn();
			activeChar.sendMessage("Created " + template.name + " on " + target.getObjectId() + ".");
			addNewSpawn(spawn);
		}
		catch(Exception e)
		{
			activeChar.sendMessage("Target is not ingame.");
		}
	}

	public void addNewSpawn(final Spawn spawn)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("INSERT INTO `spawnlist` (location,count,npc_templateid,locx,locy,locz,heading,respawn_delay,loc_id) values(?,?,?,?,?,?,?,?,?)");
			statement.setString(1, "");
			statement.setInt(2, spawn.getAmount());
			statement.setInt(3, spawn.getNpcId());
			statement.setInt(4, spawn.getLocx());
			statement.setInt(5, spawn.getLocy());
			statement.setInt(6, spawn.getLocz());
			statement.setInt(7, spawn.getHeading());
			statement.setInt(8, spawn.getRespawnDelay());
			statement.setInt(9, spawn.getLocation());
			statement.execute();
		}
		catch(Exception e1)
		{
			ScriptFile._log.error("spawn couldnt be stored in db:", e1);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	@Override
	public void onLoad()
	{
		AdminCommandHandler.getInstance().registerAdminCommandHandler(this);
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	static
	{
		AdminSpawn._adminCommands = new String[] {
				"admin_show_spawns",
				"admin_spawn",
				"admin_spawn_monster",
				"admin_spawn_index",
				"admin_unspawnall",
				"admin_spawn1",
				"admin_setheading",
				"admin_setai",
				"admin_resp",
				"admin_npcvis" };
	}
}
