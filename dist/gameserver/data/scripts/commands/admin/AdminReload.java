package commands.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.gameserver.Announcements;
import l2s.gameserver.Config;
import l2s.gameserver.TradeController;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.data.string.StringsHolder;
import l2s.gameserver.data.xml.holder.MultiSellHolder;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.instancemanager.ZoneManager;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.s2c.NpcHtmlMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.DoorTable;
import l2s.gameserver.tables.FishTable;
import l2s.gameserver.tables.GmListTable;
import l2s.gameserver.tables.NpcTable;
import l2s.gameserver.tables.SkillTable;
import l2s.gameserver.tables.SpawnTable;
import l2s.gameserver.tables.TeleportLocTable;
import l2s.gameserver.tables.TeleportTable;
import l2s.gameserver.tables.TerritoryTable;
import l2s.gameserver.utils.AddonsConfig;
import l2s.gameserver.utils.Strings;
import l2s.gameserver.utils.velocity.VelocityUtils;

public class AdminReload implements IAdminCommandHandler, ScriptFile
{
	private static Logger _log = LoggerFactory.getLogger(AdminReload.class);

	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().CanReload)
			return false;
		if(command.equals("admin_reload"))
		{
			final String content = HtmCache.getInstance().getHtml("admin/reload.htm", activeChar);
			final NpcHtmlMessage msg = new NpcHtmlMessage(5);
			msg.setHtml(content);
			activeChar.sendPacket(msg);
		}
		else if(command.equals("admin_reload_multisell"))
		{
			try
			{
				MultiSellHolder.getInstance().reload();
			}
			catch(Exception e)
			{
				activeChar.sendMessage("Error. Multisell list NOT reloaded!");
				return false;
			}
			activeChar.sendMessage("Multisell list reloaded!");
		}
		else if(command.equals("admin_reload_gmaccess"))
		{
			try
			{
				Config.loadGMAccess();
				for(final Player player : GameObjectsStorage.getPlayers())
					if(!Config.EVERYBODY_HAS_ADMIN_RIGHTS)
						player.setPlayerAccess(Config.gmlist.get(player.getObjectId()));
					else
						player.setPlayerAccess(Config.gmlist.get(0));
			}
			catch(Exception e)
			{
				return false;
			}
			activeChar.sendMessage("GMAccess reloaded!");
		}
		else if(command.equals("admin_reload_htm"))
			switch(Config.HTM_CACHE_MODE)
			{
				case 2:
				{
					HtmCache.getInstance().reload();
					activeChar.sendMessage("HTML cache reloaded.");
					break;
				}
				case 1:
				{
					HtmCache.getInstance().clear();
					activeChar.sendMessage("HTML cache clearned.");
					break;
				}
				case 0:
				{
					activeChar.sendMessage("HTML cache disabled.");
					break;
				}
			}
		else if(command.equals("admin_reload_announcements"))
		{
			Announcements.getInstance().loadAnnouncements();
			Announcements.getInstance().listAnnouncements(activeChar);
		}
		if(command.equals("admin_reload_qs"))
		{
			if(command.endsWith("all"))
				for(final Player p : GameObjectsStorage.getPlayers())
					reloadQuestStates(p);
			else
			{
				final GameObject t = activeChar.getTarget();
				if(t != null && t.isPlayer())
				{
					final Player p = (Player) t;
					reloadQuestStates(p);
				}
				else
					reloadQuestStates(activeChar);
			}
			return true;
		}
		if(command.equals("admin_reload_qs_help"))
		{
			activeChar.sendMessage("");
			activeChar.sendMessage("Quest Help:");
			activeChar.sendMessage("reload_qs_help - This Message.");
			activeChar.sendMessage("reload_qs <selected target> - reload all quest states for target.");
			activeChar.sendMessage("reload_qs <no target or target is not player> - reload quests for self.");
			activeChar.sendMessage("reload_qs all - reload quests for all players in world.");
			activeChar.sendMessage("");
			return true;
		}
		if(command.equals("admin_reload_loc"))
		{
			TerritoryTable.getInstance().reloadData();
			ZoneManager.getInstance().reload();
			GmListTable.broadcastMessageToGMs("Locations and zones reloaded.");
		}
		else if(command.equals("admin_reload_skills"))
		{
			SkillTable.getInstance().reload();
			GmListTable.broadcastMessageToGMs("Skill table reloaded by " + activeChar.getName() + ".");
			_log.info("Skill table reloaded by " + activeChar.getName() + ".");
		}
		else if(command.equals("admin_reload_npc"))
		{
			NpcTable.getInstance().reloadAllNpc();
			GmListTable.broadcastMessageToGMs("Npc table reloaded.");
		}
		else if(command.equals("admin_reload_spawn"))
		{
			SpawnTable.getInstance().reloadAll();
			GmListTable.broadcastMessageToGMs("All npc respawned.");
		}
		else if(command.equals("admin_reload_npc_spawn"))
		{
			NpcTable.getInstance().reloadAllNpc();
			SpawnTable.getInstance().reloadAll();
			GmListTable.broadcastMessageToGMs("Npc reloaded and respawned.");
		}
		else if(command.equals("admin_reload_fish"))
		{
			FishTable.getInstance().reload();
			GmListTable.broadcastMessageToGMs("Fish table reloaded.");
		}
		else if(command.equals("admin_reload_abuse"))
		{
			Config.abuseLoad();
			GmListTable.broadcastMessageToGMs("Abuse reloaded.");
		}
		else if(command.equals("admin_reload_translit"))
		{
			Strings.reload();
			GmListTable.broadcastMessageToGMs("Translit reloaded.");
		}
		else if(command.equals("admin_reload_shops"))
		{
			TradeController.reload();
			GmListTable.broadcastMessageToGMs("Shops reloaded.");
		}
		else if(command.equals("admin_reload_locale"))
		{
			StringsHolder.getInstance().reload();
		}
		else if(command.equals("admin_reload_config"))
		{
			try
			{
				Config.load(true);
				VelocityUtils.reload();
			}
			catch(Exception e)
			{
				activeChar.sendMessage("Error: " + e.getMessage() + "!");
				return false;
			}
			GmListTable.broadcastMessageToGMs("Config reloaded!");
		}
		else if(command.equals("admin_reload_addonsconfig"))
		{
			AddonsConfig.reload();
			GmListTable.broadcastMessageToGMs("Addons config reloaded!");
		}
		else if(command.equals("admin_reload_doors"))
		{
			DoorTable.getInstance().reloadDoors();
			GmListTable.broadcastMessageToGMs("Doors reloaded.");
		}
		else if(command.equals("admin_reload_teleports"))
		{
			TeleportTable.reload();
			TeleportLocTable.getInstance().reloadAll();
			GmListTable.broadcastMessageToGMs("Teleports reloaded.");
		}
		return true;
	}

	private void reloadQuestStates(final Player p)
	{
		for(final QuestState qs : p.getAllQuestsStates())
			p.delQuestState(qs.getQuest().getId());
		Quest.playerEnter(p);
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminReload._adminCommands;
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
		AdminReload._adminCommands = new String[] {
				"admin_reload",
				"admin_reload_multisell",
				"admin_reload_announcements",
				"admin_reload_gmaccess",
				"admin_reload_htm",
				"admin_reload_qs",
				"admin_reload_qs_help",
				"admin_reload_loc",
				"admin_reload_skills",
				"admin_reload_npc",
				"admin_reload_spawn",
				"admin_reload_npc_spawn",
				"admin_reload_fish",
				"admin_reload_abuse",
				"admin_reload_translit",
				"admin_reload_shops",
				"admin_reload_locale",
				"admin_reload_config",
				"admin_reload_addonsconfig",
				"admin_reload_doors",
				"admin_reload_teleports" };
	}
}
