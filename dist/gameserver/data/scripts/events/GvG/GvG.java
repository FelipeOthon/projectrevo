package events.GvG;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.gameserver.Announcements;
import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.database.mysql;
import l2s.gameserver.handler.IVoicedCommandHandler;
import l2s.gameserver.handler.VoicedCommandHandler;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.instancemanager.ZoneManager;
import l2s.gameserver.listener.ZoneEnterLeaveListener;
import l2s.gameserver.model.CommandChannel;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Party;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Servitor;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.entity.olympiad.Olympiad;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.components.ChatType;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.NpcHtmlMessage;
import l2s.gameserver.network.l2.s2c.Revive;
import l2s.gameserver.network.l2.s2c.Say2;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.DoorTable;
import l2s.gameserver.tables.ItemTable;
import l2s.gameserver.tables.SkillTable;
import l2s.gameserver.utils.Location;
import l2s.gameserver.utils.Util;

public class GvG extends Functions implements ScriptFile, IVoicedCommandHandler
{
	private static Logger _log;
	private String[] _commandList;
	private static final int _maxBid = 1000000000;
	private static int _order;
	private static int _count;
	private static int _leader1;
	private static int _leader2;
	private static final Skill revengeSkill;
	private static List<Integer> players_list1;
	private static List<Integer> players_list2;
	private static List<Integer> live_list1;
	private static List<Integer> live_list2;
	private static int count1;
	private static int count2;
	private static int winner;
	private static int _status;
	private static ScheduledFuture<?> _endTask;
	private static boolean _clearing;
	private static Zone _zone;
	private static ZoneListener _zoneListener;
	private static final Location BlueTeamLoc;
	private static final Location RedTeamLoc;
	private static final Location ClearLoc;
	private static boolean _active;

	public GvG()
	{
		_commandList = new String[] { "gvg" };
	}

	@Override
	public void onLoad()
	{
		_zone.getListenerEngine().addMethodInvokedListener(_zoneListener);
		_active = ServerVariables.getString("GvG", "off").equalsIgnoreCase("on");
		VoicedCommandHandler.getInstance().registerVoicedCommandHandler(this);
		_log.info("Loaded Event: GvG");
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{
		if(!GvG._clearing && _status > 0 && GvG._count > 0)
			clearFull(true);
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return _commandList;
	}

	@Override
	public boolean useVoicedCommand(final String command, final Player player, final String args)
	{
		if(command.equals("gvg"))
		{
			if(player == null)
				return false;
			if(!_active)
			{
				player.sendMessage("\u041e\u0442\u043a\u043b\u044e\u0447\u0435\u043d\u043e. \u0423\u043c\u043e\u043b\u044f\u0439\u0442\u0435 \u0413\u041c\u0430 \u0432\u043a\u043b\u044e\u0447\u0438\u0442\u044c.");
				return false;
			}
			final String content = HtmCache.getInstance().getHtml("scripts/events/GvG/gvg.htm", player);
			NpcHtmlMessage html = new NpcHtmlMessage(1);
			html.setHtml(content);
			String ms = "";
			String info = "";
			for(int i = 0; i < Config.GvG_ItemIds.length; ++i)
			{
				if(i > 0)
				{
					ms += ";";
					info += "<br1>";
				}
				ms += i;
				info = info + i + " - " + ItemTable.getInstance().getTemplate(Config.GvG_ItemIds[i]).getName();
			}
			html.replace("%i%", ms);
			html.replace("%info%", info);
			player.sendPacket(html);
			html = null;
		}
		return true;
	}

	public void activateEvent()
	{
		final Player player = getSelf();
		if(player == null || !player.getPlayerAccess().IsEventGm)
			return;
		if(!_active)
		{
			ServerVariables.set("GvG", "on");
			_log.info("GvG activated.");
			Announcements.getInstance().announceToAll("\u0410\u043a\u0442\u0438\u0432\u0438\u0440\u043e\u0432\u0430\u043d\u043e 'GvG'");
		}
		else
			player.sendMessage(player.isLangRus() ? "'GvG' \u0443\u0436\u0435 \u0430\u043a\u0442\u0438\u0432\u043d\u043e." : "'GvG' already active.");
		_active = true;
		show(HtmCache.getInstance().getHtml("admin/events.htm", player), player);
	}

	public void deactivateEvent()
	{
		final Player player = getSelf();
		if(player == null || !player.getPlayerAccess().IsEventGm)
			return;
		if(_active)
		{
			ServerVariables.unset("GvG");
			_log.info("'GvG' deactivated.");
			Announcements.getInstance().announceToAll("'GvG' \u0434\u0435\u0430\u043a\u0442\u0438\u0432\u0438\u0440\u043e\u0432\u0430\u043d\u043e");
		}
		else
			player.sendMessage("'GvG' not active.");
		_active = false;
		show(HtmCache.getInstance().getHtml("admin/events.htm", player), player);
	}

	public void clear()
	{
		final Player player = getSelf();
		if(player == null || !player.getPlayerAccess().IsEventGm)
			return;
		clearFull(true);
		player.sendMessage("'GvG' cleared.");
		show(HtmCache.getInstance().getHtml("admin/events.htm", player), player);
	}

	private static void clearFull(final boolean teleport)
	{
		if(_endTask != null)
		{
			_endTask.cancel(false);
			_endTask = null;
		}
		final boolean reset = _status > 0 && !GvG._clearing;
		GvG._clearing = true;
		_status = 0;
		if(reset)
		{
			giveItem(GvG._leader1, Config.GvG_ItemIds[GvG._order], GvG._count);
			giveItem(GvG._leader2, Config.GvG_ItemIds[GvG._order], GvG._count);
			removeAura();
		}
		GvG.count1 = 0;
		GvG.count2 = 0;
		GvG.winner = 0;
		GvG._order = 0;
		GvG._count = 0;
		GvG._leader1 = 0;
		GvG._leader2 = 0;
		if(reset && teleport)
			teleportPlayersToSavedCoords();
		GvG._clearing = false;
	}

	public static void start30()
	{
		sayToAllSc("30 seconds before the match begins!");
	}

	public static void start10()
	{
		sayToAllSc("10 seconds before the match begins!");
	}

	private static void sayToAll(final String text)
	{
		final Say2 cs = new Say2(0, ChatType.CRITICAL_ANNOUNCE, "GvG", text);
		for(final Player player : getPlayers(GvG.players_list1, GvG.players_list2))
			if(player != null)
				player.sendPacket(cs);
	}

	private static void sayToAllSc(final String text)
	{
		final ExShowScreenMessage e = new ExShowScreenMessage(text, 2000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, false);
		for(final Player player : getPlayers(GvG.players_list1, GvG.players_list2))
			if(player != null)
				player.sendPacket(e);
	}

	public void first(final String[] var)
	{
		if(!_active)
			return;
		if(var.length < 2)
			return;
		final Player player = getSelf();
		if(player == null)
			return;
		if(Config.GvG_ItemIds.length != Config.GvG_MinBids.length)
		{
			player.sendMessage("\u041d\u0435\u0432\u0435\u0440\u043d\u0430\u044f \u043d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0430 \u043a\u043e\u043d\u0444\u0438\u0433\u0443\u0440\u0430\u0446\u0438\u0438 \u044d\u0432\u0435\u043d\u0442\u0430. \u0421\u043e\u043e\u0431\u0449\u0438\u0442\u0435 \u0410\u0434\u043c\u0438\u043d\u0438\u0441\u0442\u0440\u0430\u0446\u0438\u0438.");
			return;
		}
		int order = 0;
		int bid = 0;
		try
		{
			order = Integer.parseInt(var[0]);
			bid = Integer.parseInt(var[1]);
		}
		catch(Exception e)
		{
			player.sendMessage("\u0412\u0432\u043e\u0434\u0438\u0442\u0435 \u0432\u0430\u043b\u0438\u0434\u043d\u044b\u0435 \u043f\u0430\u0440\u0430\u043c\u0435\u0442\u0440\u044b.");
			return;
		}
		if(Config.GvG_ItemIds.length < order + 1)
		{
			player.sendMessage("\u0417\u0430\u043f\u0440\u0435\u0449\u0435\u043d\u043d\u044b\u0439 \u043f\u0440\u0435\u0434\u043c\u0435\u0442 \u0441\u0442\u0430\u0432\u043a\u0438!");
			return;
		}
		Player receiver = null;
		if(var.length > 2 && !var[2].equals(""))
		{
			receiver = GameObjectsStorage.getPlayer(var[2]);
			if(receiver == null)
			{
				player.sendMessage("\u041f\u0435\u0440\u0441\u043e\u043d\u0430\u0436 " + var[2] + " \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d.");
				return;
			}
		}
		else if(player.getTarget() != null && player.getTarget().isPlayer())
			receiver = (Player) player.getTarget();
		if(receiver == null)
		{
			player.sendMessage("\u041d\u0443\u0436\u043d\u043e \u0432\u0432\u0435\u0441\u0442\u0438 \u0438\u043c\u044f \u043e\u043d\u043b\u0430\u0439\u043d \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436\u0430 \u0438\u043b\u0438 \u0432\u0437\u044f\u0442\u044c \u0435\u0433\u043e \u0432 \u0442\u0430\u0440\u0433\u0435\u0442.");
			return;
		}
		if(receiver.getObjectId() == player.getObjectId())
		{
			player.sendMessage("\u041d\u0435\u043b\u044c\u0437\u044f \u0432\u044b\u0437\u0432\u0430\u0442\u044c \u0441\u0435\u0431\u044f.");
			return;
		}
		if(_status > 0)
		{
			player.sendMessage("\u0410\u0440\u0435\u043d\u0430 \u0437\u0430\u043d\u044f\u0442\u0430. \u041f\u043e\u043f\u0440\u043e\u0431\u0443\u0439\u0442\u0435 \u043f\u043e\u0437\u0436\u0435.");
			return;
		}
		final Party playerParty = player.getParty();
		if(playerParty == null)
		{
			player.sendMessage("\u0423 \u0412\u0430\u0441 \u043d\u0435\u0442 \u0433\u0440\u0443\u043f\u043f\u044b.");
			return;
		}
		final CommandChannel playercc = playerParty.getCommandChannel();
		if(playercc != null)
		{
			if(playercc.getChannelLeader() != player)
			{
				player.sendMessage("\u0412\u044b \u043d\u0435 \u043b\u0438\u0434\u0435\u0440 \u043a\u043e\u043c\u0430\u043d\u0434\u043d\u043e\u0433\u043e \u043a\u0430\u043d\u0430\u043b\u0430.");
				return;
			}
			if(playercc.getMemberCount() > Config.GvG_Max_Members)
			{
				player.sendMessage("\u0412\u0430\u0448 \u043a\u043e\u043c\u0430\u043d\u0434\u043d\u044b\u0439 \u043a\u0430\u043d\u0430\u043b \u0434\u043e\u043b\u0436\u0435\u043d \u0441\u043e\u0441\u0442\u043e\u044f\u0442\u044c \u043d\u0435 \u0431\u043e\u043b\u044c\u0448\u0435 \u0447\u0435\u043c \u0441 " + Config.GvG_Max_Members + " \u0447\u0435\u043b\u043e\u0432\u0435\u043a.");
				return;
			}
			if(playercc.getMemberCount() < Config.GvG_Min_Members)
			{
				player.sendMessage("\u0412\u0430\u0448 \u043a\u043e\u043c\u0430\u043d\u0434\u043d\u044b\u0439 \u043a\u0430\u043d\u0430\u043b \u0434\u043e\u043b\u0436\u0435\u043d \u0441\u043e\u0441\u0442\u043e\u044f\u0442\u044c \u043d\u0435 \u043c\u0435\u043d\u0435\u0435 \u0447\u0435\u043c \u0441 " + Config.GvG_Min_Members + " \u0447\u0435\u043b\u043e\u0432\u0435\u043a.");
				return;
			}
		}
		else
		{
			if(!playerParty.isLeader(player))
			{
				player.sendMessage("\u0412\u044b \u043d\u0435 \u043b\u0438\u0434\u0435\u0440 \u0433\u0440\u0443\u043f\u043f\u044b.");
				return;
			}
			if(playerParty.getMemberCount() > Config.GvG_Max_Members)
			{
				player.sendMessage("\u0412\u0430\u0448\u0430 \u0433\u0440\u0443\u043f\u043f\u0430 \u0434\u043e\u043b\u0436\u043d\u0430 \u0441\u043e\u0441\u0442\u043e\u044f\u0442\u044c \u043d\u0435 \u0431\u043e\u043b\u044c\u0448\u0435 \u0447\u0435\u043c \u0441 " + Config.GvG_Max_Members + " \u0447\u0435\u043b\u043e\u0432\u0435\u043a.");
				return;
			}
			if(playerParty.getMemberCount() < Config.GvG_Min_Members)
			{
				player.sendMessage("\u0412\u0430\u0448\u0430 \u0433\u0440\u0443\u043f\u043f\u0430 \u0434\u043e\u043b\u0436\u043d\u0430 \u0441\u043e\u0441\u0442\u043e\u044f\u0442\u044c \u043d\u0435 \u043c\u0435\u043d\u0435\u0435 \u0447\u0435\u043c \u0441 " + Config.GvG_Min_Members + " \u0447\u0435\u043b\u043e\u0432\u0435\u043a.");
				return;
			}
		}
		final String itemName = ItemTable.getInstance().getTemplate(Config.GvG_ItemIds[order]).getName();
		if(bid < Config.GvG_MinBids[order])
		{
			player.sendMessage("\u041c\u0438\u043d\u0438\u043c\u0430\u043b\u044c\u043d\u0430\u044f \u0441\u0442\u0430\u0432\u043a\u0430 " + Config.GvG_MinBids[order] + " " + itemName + ".");
			return;
		}
		if(bid > 1000000000)
		{
			player.sendMessage("\u041c\u0430\u043a\u0441\u0438\u043c\u0430\u043b\u044c\u043d\u0430\u044f \u0441\u0442\u0430\u0432\u043a\u0430 1000000000 " + itemName + ".");
			return;
		}
		ItemInstance item = player.getInventory().getItemByItemId(Config.GvG_ItemIds[order]);
		if(item == null || item.getCount() < bid)
		{
			player.sendMessage("\u041d\u0435\u0434\u043e\u0441\u0442\u0430\u0442\u043e\u0447\u043d\u043e " + itemName + ".");
			return;
		}
		if(receiver.isInOlympiadMode())
		{
			player.sendMessage("\u041e\u043f\u043f\u043e\u043d\u0435\u043d\u0442 \u043d\u0435 \u043c\u043e\u0436\u0435\u0442 \u043f\u0440\u0438\u043d\u044f\u0442\u044c \u0432\u044b\u0437\u043e\u0432 \u0443\u0447\u0430\u0441\u0442\u0432\u0443\u044f \u0432 \u041e\u043b\u0438\u043c\u043f\u0438\u0430\u0434\u0435.");
			return;
		}
		if(receiver.inObserverMode())
		{
			player.sendMessage("\u041e\u043f\u043f\u043e\u043d\u0435\u043d\u0442 \u043d\u0435 \u043c\u043e\u0436\u0435\u0442 \u043f\u0440\u0438\u043d\u044f\u0442\u044c \u0432\u044b\u0437\u043e\u0432 \u043d\u0430\u0445\u043e\u0434\u044f\u0441\u044c \u0432 \u0440\u0435\u0436\u0438\u043c\u0435 \u043f\u0440\u043e\u0441\u043c\u043e\u0442\u0440\u0430.");
			return;
		}
		item = receiver.getInventory().getItemByItemId(Config.GvG_ItemIds[order]);
		if(item == null || item.getCount() < bid)
		{
			player.sendMessage("\u0423 \u0412\u0430\u0448\u0435\u0433\u043e \u043e\u043f\u043f\u043e\u043d\u0435\u043d\u0442\u0430 \u043d\u0435\u0434\u043e\u0441\u0442\u0430\u0442\u043e\u0447\u043d\u043e " + itemName + ".");
			return;
		}
		final Party receiverParty = receiver.getParty();
		if(receiverParty == null)
		{
			player.sendMessage("\u0412\u0430\u0448 \u043e\u043f\u043f\u043e\u043d\u0435\u043d\u0442 \u043d\u0435 \u0438\u043c\u0435\u0435\u0442 \u0433\u0440\u0443\u043f\u043f\u044b.");
			return;
		}
		if(playercc != null)
		{
			final CommandChannel receivercc = receiverParty.getCommandChannel();
			if(receivercc == null)
			{
				player.sendMessage("\u0412\u0430\u0448 \u043e\u043f\u043f\u043e\u043d\u0435\u043d\u0442 \u043d\u0435 \u0438\u043c\u0435\u0435\u0442 \u043a\u043e\u043c\u0430\u043d\u0434\u043d\u043e\u0433\u043e \u043a\u0430\u043d\u0430\u043b\u0430.");
				return;
			}
			if(receivercc.getChannelLeader() != receiver)
			{
				player.sendMessage("\u0412\u0430\u0448 \u043e\u043f\u043f\u043e\u043d\u0435\u043d\u0442 \u043d\u0435 \u043b\u0438\u0434\u0435\u0440 \u043a\u043e\u043c\u0430\u043d\u0434\u043d\u043e\u0433\u043e \u043a\u0430\u043d\u0430\u043b\u0430.");
				return;
			}
			if(receivercc.getMemberCount() != playercc.getMemberCount())
			{
				player.sendMessage("\u041a\u043e\u043b-\u0432\u043e \u0447\u0435\u043b\u043e\u0432\u0435\u043a \u0432 \u043a\u043e\u043c\u0430\u043d\u0434\u043d\u043e\u043c \u043a\u0430\u043d\u0430\u043b\u0435 \u043e\u043f\u043f\u043e\u043d\u0435\u043d\u0442\u0430 \u043d\u0435 \u0441\u043e\u043e\u0442\u0432\u0435\u0442\u0441\u0432\u0443\u0435\u0442 \u0412\u0430\u0448\u0435\u043c\u0443.");
				return;
			}
		}
		else
		{
			if(!receiverParty.isLeader(receiver))
			{
				player.sendMessage("\u0412\u0430\u0448 \u043e\u043f\u043f\u043e\u043d\u0435\u043d\u0442 \u043d\u0435 \u043b\u0438\u0434\u0435\u0440 \u0433\u0440\u0443\u043f\u043f\u044b.");
				return;
			}
			if(receiverParty.getMemberCount() != playerParty.getMemberCount())
			{
				player.sendMessage("\u041a\u043e\u043b-\u0432\u043e \u0447\u0435\u043b\u043e\u0432\u0435\u043a \u0432 \u0433\u0440\u0443\u043f\u043f\u0435 \u043e\u043f\u043f\u043e\u043d\u0435\u043d\u0442\u0430 \u043d\u0435 \u0441\u043e\u043e\u0442\u0432\u0435\u0442\u0441\u0432\u0443\u0435\u0442 \u0412\u0430\u0448\u0435\u043c\u0443.");
				return;
			}
		}
		player.setVar("gvgOrder", String.valueOf(order));
		player.setVar("gvgBid", String.valueOf(bid));
		player.GvG_ID = receiver.getObjectId();
		receiver.scriptRequest(player.getName() + " \u0432\u044b\u0437\u044b\u0432\u0430\u0435\u0442 \u0412\u0430\u0441 \u043d\u0430 \u0433\u0440\u0443\u043f\u043f\u043e\u0432\u0443\u044e \u0434\u0443\u044d\u043b\u044c (GvG). \u0421\u0442\u0430\u0432\u043a\u0430 " + bid + " " + itemName + ". \u041f\u0440\u0438\u043d\u044f\u0442\u044c \u0432\u044b\u0437\u043e\u0432?", "events.GvG.GvG:tryStart", new Object[] {
				receiver.getObjectId(),
				player.getObjectId() });
		player.sendMessage("\u0412\u044b\u0437\u043e\u0432 \u043e\u0442\u043f\u0440\u0430\u0432\u043b\u0435\u043d. \u041e\u0436\u0438\u0434\u0430\u0439\u0442\u0435 \u043e\u0442\u0432\u0435\u0442\u0430 \u043e\u043f\u043f\u043e\u043d\u0435\u043d\u0442\u0430.");
	}

	public static void tryStart(final int id1, final int id2)
	{
		if(!_active)
			return;
		final Player receiver = GameObjectsStorage.getPlayer(id1);
		final Player player = GameObjectsStorage.getPlayer(id2);
		if(receiver == null && player == null)
			return;
		if(receiver == null || !receiver.isConnected())
		{
			if(player != null)
				player.sendMessage("\u041e\u043f\u043f\u043e\u043d\u0435\u043d\u0442 \u0443\u0436\u0435 \u0432 \u043e\u0444\u0444\u043b\u0430\u0439\u043d\u0435.");
			return;
		}
		if(player == null || !player.isConnected())
		{
			receiver.sendMessage("\u041f\u0440\u043e\u0432\u043e\u043a\u0430\u0442\u043e\u0440 \u0443\u0436\u0435 \u0432 \u043e\u0444\u0444\u043b\u0430\u0439\u043d\u0435.");
			return;
		}
		if(player.GvG_ID != receiver.getObjectId())
		{
			player.sendMessage("\u041d\u0435\u0437\u0430\u043a\u043e\u043d\u043d\u0430\u044f \u0441\u0430\u043d\u043a\u0446\u0438\u044f \u043e\u0442 " + receiver.getName());
			receiver.sendMessage("\u041d\u0435\u0437\u0430\u043a\u043e\u043d\u043d\u0430\u044f \u0441\u0430\u043d\u043a\u0446\u0438\u044f!");
			return;
		}
		player.GvG_ID = 0;
		final Party playerParty = player.getParty();
		if(playerParty == null)
		{
			player.sendMessage("\u0423 \u0412\u0430\u0441 \u043d\u0435\u0442 \u0433\u0440\u0443\u043f\u043f\u044b.");
			receiver.sendMessage("\u0412\u0430\u0448 \u043e\u043f\u043f\u043e\u043d\u0435\u043d\u0442 \u0443\u0436\u0435 \u043d\u0435 \u0438\u043c\u0435\u0435\u0442 \u0433\u0440\u0443\u043f\u043f\u044b.");
			return;
		}
		final CommandChannel playercc = playerParty.getCommandChannel();
		if(playercc != null)
		{
			if(playercc.getChannelLeader() != player)
			{
				player.sendMessage("\u0412\u044b \u043d\u0435 \u043b\u0438\u0434\u0435\u0440 \u043a\u043e\u043c\u0430\u043d\u0434\u043d\u043e\u0433\u043e \u043a\u0430\u043d\u0430\u043b\u0430.");
				receiver.sendMessage("\u0412\u0430\u0448 \u043e\u043f\u043f\u043e\u043d\u0435\u043d\u0442 \u0443\u0436\u0435 \u043d\u0435 \u043b\u0438\u0434\u0435\u0440 \u043a\u043e\u043c\u0430\u043d\u0434\u043d\u043e\u0433\u043e \u043a\u0430\u043d\u0430\u043b\u0430.");
				return;
			}
			if(playercc.getMemberCount() > Config.GvG_Max_Members)
			{
				player.sendMessage("\u0412\u0430\u0448 \u043a\u043e\u043c\u0430\u043d\u0434\u043d\u044b\u0439 \u043a\u0430\u043d\u0430\u043b \u0434\u043e\u043b\u0436\u0435\u043d \u0441\u043e\u0441\u0442\u043e\u044f\u0442\u044c \u043d\u0435 \u0431\u043e\u043b\u044c\u0448\u0435 \u0447\u0435\u043c \u0441 " + Config.GvG_Max_Members + " \u0447\u0435\u043b\u043e\u0432\u0435\u043a.");
				receiver.sendMessage("\u0412 \u043a\u043e\u043c\u0430\u043d\u0434\u043d\u043e\u043c \u043a\u0430\u043d\u0430\u043b\u0435 \u043e\u043f\u043f\u043e\u043d\u0435\u043d\u0442\u0430 \u0443\u0436\u0435 \u0431\u043e\u043b\u044c\u0448\u0435 " + Config.GvG_Max_Members + " \u0447\u0435\u043b\u043e\u0432\u0435\u043a.");
				return;
			}
			if(playercc.getMemberCount() < Config.GvG_Min_Members)
			{
				player.sendMessage("\u0412\u0430\u0448 \u043a\u043e\u043c\u0430\u043d\u0434\u043d\u044b\u0439 \u043a\u0430\u043d\u0430\u043b \u0434\u043e\u043b\u0436\u0435\u043d \u0441\u043e\u0441\u0442\u043e\u044f\u0442\u044c \u043d\u0435 \u043c\u0435\u043d\u0435\u0435 \u0447\u0435\u043c \u0441 " + Config.GvG_Min_Members + " \u0447\u0435\u043b\u043e\u0432\u0435\u043a.");
				receiver.sendMessage("\u0412 \u043a\u043e\u043c\u0430\u043d\u0434\u043d\u043e\u043c \u043a\u0430\u043d\u0430\u043b\u0435 \u043e\u043f\u043f\u043e\u043d\u0435\u043d\u0442\u0430 \u0443\u0436\u0435 \u043d\u0435\u0442 " + Config.GvG_Min_Members + " \u0447\u0435\u043b\u043e\u0432\u0435\u043a.");
				return;
			}
		}
		else
		{
			if(!playerParty.isLeader(player))
			{
				player.sendMessage("\u0412\u044b \u043d\u0435 \u043b\u0438\u0434\u0435\u0440 \u0433\u0440\u0443\u043f\u043f\u044b.");
				receiver.sendMessage("\u0412\u0430\u0448 \u043e\u043f\u043f\u043e\u043d\u0435\u043d\u0442 \u0443\u0436\u0435 \u043d\u0435 \u043b\u0438\u0434\u0435\u0440 \u0433\u0440\u0443\u043f\u043f\u044b.");
				return;
			}
			if(playerParty.getMemberCount() > Config.GvG_Max_Members)
			{
				player.sendMessage("\u0412\u0430\u0448\u0430 \u0433\u0440\u0443\u043f\u043f\u0430 \u0434\u043e\u043b\u0436\u043d\u0430 \u0441\u043e\u0441\u0442\u043e\u044f\u0442\u044c \u043d\u0435 \u0431\u043e\u043b\u044c\u0448\u0435 \u0447\u0435\u043c \u0441 " + Config.GvG_Max_Members + " \u0447\u0435\u043b\u043e\u0432\u0435\u043a.");
				receiver.sendMessage("\u0412 \u0433\u0440\u0443\u043f\u043f\u0435 \u043e\u043f\u043f\u043e\u043d\u0435\u043d\u0442\u0430 \u0443\u0436\u0435 \u0431\u043e\u043b\u044c\u0448\u0435 " + Config.GvG_Max_Members + " \u0447\u0435\u043b\u043e\u0432\u0435\u043a.");
				return;
			}
			if(playerParty.getMemberCount() < Config.GvG_Min_Members)
			{
				player.sendMessage("\u0412\u0430\u0448\u0430 \u0433\u0440\u0443\u043f\u043f\u0430 \u0434\u043e\u043b\u0436\u043d\u0430 \u0441\u043e\u0441\u0442\u043e\u044f\u0442\u044c \u043d\u0435 \u043c\u0435\u043d\u0435\u0435 \u0447\u0435\u043c \u0441 " + Config.GvG_Min_Members + " \u0447\u0435\u043b\u043e\u0432\u0435\u043a.");
				receiver.sendMessage("\u0412 \u0433\u0440\u0443\u043f\u043f\u0435 \u043e\u043f\u043f\u043e\u043d\u0435\u043d\u0442\u0430 \u0443\u0436\u0435 \u043d\u0435\u0442 " + Config.GvG_Min_Members + " \u0447\u0435\u043b\u043e\u0432\u0435\u043a.");
				return;
			}
		}
		final Party receiverParty = receiver.getParty();
		if(receiverParty == null)
		{
			receiver.sendMessage("\u0423 \u0412\u0430\u0441 \u043d\u0435\u0442 \u0433\u0440\u0443\u043f\u043f\u044b.");
			player.sendMessage("\u0412\u0430\u0448 \u043e\u043f\u043f\u043e\u043d\u0435\u043d\u0442 \u0443\u0436\u0435 \u043d\u0435 \u0438\u043c\u0435\u0435\u0442 \u0433\u0440\u0443\u043f\u043f\u044b.");
			return;
		}
		if(playercc != null)
		{
			final CommandChannel receivercc = receiverParty.getCommandChannel();
			if(receivercc == null)
			{
				receiver.sendMessage("\u0412\u044b \u043d\u0435 \u0438\u043c\u0435\u0435\u0442\u0435 \u043a\u043e\u043c\u0430\u043d\u0434\u043d\u043e\u0433\u043e \u043a\u0430\u043d\u0430\u043b\u0430.");
				player.sendMessage("\u0412\u0430\u0448 \u043e\u043f\u043f\u043e\u043d\u0435\u043d\u0442 \u043d\u0435 \u0438\u043c\u0435\u0435\u0442 \u043a\u043e\u043c\u0430\u043d\u0434\u043d\u043e\u0433\u043e \u043a\u0430\u043d\u0430\u043b\u0430.");
				return;
			}
			if(receivercc.getChannelLeader() != receiver)
			{
				receiver.sendMessage("\u0412\u044b \u043d\u0435 \u043b\u0438\u0434\u0435\u0440 \u043a\u043e\u043c\u0430\u043d\u0434\u043d\u043e\u0433\u043e \u043a\u0430\u043d\u0430\u043b\u0430.");
				player.sendMessage("\u0412\u0430\u0448 \u043e\u043f\u043f\u043e\u043d\u0435\u043d\u0442 \u043d\u0435 \u043b\u0438\u0434\u0435\u0440 \u043a\u043e\u043c\u0430\u043d\u0434\u043d\u043e\u0433\u043e \u043a\u0430\u043d\u0430\u043b\u0430.");
				return;
			}
			if(receivercc.getMemberCount() != playercc.getMemberCount())
			{
				receiver.sendMessage("\u041a\u043e\u043b-\u0432\u043e \u0447\u0435\u043b\u043e\u0432\u0435\u043a \u0432 \u0412\u0430\u0448\u0435\u043c \u043a\u043e\u043c\u0430\u043d\u0434\u043d\u043e\u043c \u043a\u0430\u043d\u0430\u043b\u0435 \u043d\u0435 \u0441\u043e\u043e\u0442\u0432\u0435\u0442\u0441\u0432\u0443\u0435\u0442 \u043a\u043e\u043b-\u0432\u0443 \u0432 \u043a\u043e\u043c\u0430\u043d\u0434\u043d\u043e\u043c \u043a\u0430\u043d\u0430\u043b\u0435 \u043e\u043f\u043f\u043e\u043d\u0435\u043d\u0442\u0430.");
				player.sendMessage("\u041a\u043e\u043b-\u0432\u043e \u0447\u0435\u043b\u043e\u0432\u0435\u043a \u0432 \u043a\u043e\u043c\u0430\u043d\u0434\u043d\u043e\u043c \u043a\u0430\u043d\u0430\u043b\u0435 \u043e\u043f\u043f\u043e\u043d\u0435\u043d\u0442\u0430 \u043d\u0435 \u0441\u043e\u043e\u0442\u0432\u0435\u0442\u0441\u0432\u0443\u0435\u0442 \u0412\u0430\u0448\u0435\u043c\u0443.");
				return;
			}
		}
		else
		{
			if(!receiverParty.isLeader(receiver))
			{
				receiver.sendMessage("\u0412\u044b \u043d\u0435 \u043b\u0438\u0434\u0435\u0440 \u0433\u0440\u0443\u043f\u043f\u044b.");
				player.sendMessage("\u0412\u0430\u0448 \u043e\u043f\u043f\u043e\u043d\u0435\u043d\u0442 \u043d\u0435 \u043b\u0438\u0434\u0435\u0440 \u0433\u0440\u0443\u043f\u043f\u044b.");
				return;
			}
			if(receiverParty.getMemberCount() != playerParty.getMemberCount())
			{
				receiver.sendMessage("\u041a\u043e\u043b-\u0432\u043e \u0447\u0435\u043b\u043e\u0432\u0435\u043a \u0432 \u0412\u0430\u0448\u0435\u0439 \u0433\u0440\u0443\u043f\u043f\u0435 \u043d\u0435 \u0441\u043e\u043e\u0442\u0432\u0435\u0442\u0441\u0432\u0443\u0435\u0442 \u043a\u043e\u043b-\u0432\u0443 \u0432 \u0433\u0440\u0443\u043f\u043f\u0435 \u043e\u043f\u043f\u043e\u043d\u0435\u043d\u0442\u0430.");
				player.sendMessage("\u041a\u043e\u043b-\u0432\u043e \u0447\u0435\u043b\u043e\u0432\u0435\u043a \u0432 \u0433\u0440\u0443\u043f\u043f\u0435 \u043e\u043f\u043f\u043e\u043d\u0435\u043d\u0442\u0430 \u043d\u0435 \u0441\u043e\u043e\u0442\u0432\u0435\u0442\u0441\u0432\u0443\u0435\u0442 \u0412\u0430\u0448\u0435\u043c\u0443.");
				return;
			}
		}
		int order = 0;
		int bid = 0;
		try
		{
			order = Integer.parseInt(player.getVar("gvgOrder"));
			bid = Integer.parseInt(player.getVar("gvgBid"));
		}
		catch(Exception e)
		{
			player.sendMessage("\u041f\u0440\u0435\u0440\u0432\u0430\u043d\u043e \u0438\u0437-\u0437\u0430 \u043e\u0442\u0441\u0443\u0442\u0441\u0442\u0432\u0438\u044f \u0441\u0442\u0430\u0432\u043a\u0438.");
			receiver.sendMessage("\u041f\u0440\u0435\u0440\u0432\u0430\u043d\u043e \u0438\u0437-\u0437\u0430 \u043e\u0442\u0441\u0443\u0442\u0441\u0442\u0432\u0438\u044f \u0441\u0442\u0430\u0432\u043a\u0438.");
			return;
		}
		player.unsetVar("gvgOrder");
		player.unsetVar("gvgBid");
		if(_status > 0)
		{
			player.sendMessage("\u0410\u0440\u0435\u043d\u0430 \u0443\u0436\u0435 \u0437\u0430\u043d\u044f\u0442\u0430. \u041f\u043e\u043f\u0440\u043e\u0431\u0443\u0439\u0442\u0435 \u043f\u043e\u0437\u0436\u0435.");
			receiver.sendMessage("\u0410\u0440\u0435\u043d\u0430 \u0443\u0436\u0435 \u0437\u0430\u043d\u044f\u0442\u0430. \u041f\u043e\u043f\u0440\u043e\u0431\u0443\u0439\u0442\u0435 \u043f\u043e\u0437\u0436\u0435.");
			return;
		}
		if(GvG._clearing)
		{
			receiver.sendMessage("\u0414\u043e\u0436\u0434\u0438\u0442\u0435\u0441\u044c \u043e\u0447\u0438\u0441\u0442\u043a\u0438 \u0430\u0440\u0435\u043d\u044b.");
			player.sendMessage("\u0414\u043e\u0436\u0434\u0438\u0442\u0435\u0441\u044c \u043e\u0447\u0438\u0441\u0442\u043a\u0438 \u0430\u0440\u0435\u043d\u044b.");
			return;
		}
		final String itemName = ItemTable.getInstance().getTemplate(Config.GvG_ItemIds[order]).getName();
		ItemInstance item = player.getInventory().getItemByItemId(Config.GvG_ItemIds[order]);
		if(item == null || item.getCount() < bid)
		{
			player.sendMessage("\u0423 \u0412\u0430\u0441 \u043d\u0435\u0434\u043e\u0441\u0442\u0430\u0442\u043e\u0447\u043d\u043e " + itemName + ".");
			receiver.sendMessage("\u0423 \u043e\u043f\u043f\u043e\u043d\u0435\u043d\u0442\u0430 \u043d\u0435\u0434\u043e\u0441\u0442\u0430\u0442\u043e\u0447\u043d\u043e " + itemName + ".");
			return;
		}
		player.getInventory().destroyItem(item, bid, false);
		item = receiver.getInventory().getItemByItemId(Config.GvG_ItemIds[order]);
		if(item == null || item.getCount() < bid)
		{
			player.getInventory().addItem(Config.GvG_ItemIds[order], bid);
			receiver.sendMessage("\u0423 \u0412\u0430\u0441 \u043d\u0435\u0434\u043e\u0441\u0442\u0430\u0442\u043e\u0447\u043d\u043e " + itemName + ".");
			player.sendMessage("\u0423 \u043e\u043f\u043f\u043e\u043d\u0435\u043d\u0442\u0430 \u043d\u0435\u0434\u043e\u0441\u0442\u0430\u0442\u043e\u0447\u043d\u043e " + itemName + ".");
			return;
		}
		receiver.getInventory().destroyItem(item, bid, false);
		_status = 1;
		GvG._order = order;
		GvG._count = bid;
		GvG.players_list1 = new CopyOnWriteArrayList<Integer>();
		GvG.players_list2 = new CopyOnWriteArrayList<Integer>();
		live_list1 = new CopyOnWriteArrayList<Integer>();
		live_list2 = new CopyOnWriteArrayList<Integer>();
		GvG._leader1 = player.getObjectId();
		GvG._leader2 = receiver.getObjectId();
		List<Player> players1;
		List<Player> players2;
		if(playercc != null)
		{
			players1 = playercc.getMembers();
			players2 = receiverParty.getCommandChannel().getMembers();
		}
		else
		{
			players1 = playerParty.getPartyMembers();
			players2 = receiverParty.getPartyMembers();
		}
		for(final Player p : players1)
			if(p != null)
			{
				final String msg = checkPlayer(p);
				if(msg != null)
				{
					if(receiver != null)
						receiver.sendMessage("\u0427\u043b\u0435\u043d \u0433\u0440\u0443\u043f\u043f\u044b \u043e\u043f\u043f\u043e\u043d\u0435\u043d\u0442\u0430 " + msg);
					if(player != null)
						player.sendMessage(p.getName() + " " + msg);
					clearFull(false);
					return;
				}
				GvG.players_list1.add(p.getObjectId());
				live_list1.add(p.getObjectId());
			}
		for(final Player p : players2)
			if(p != null)
			{
				final String msg = checkPlayer(p);
				if(msg != null)
				{
					if(player != null)
						player.sendMessage("\u0427\u043b\u0435\u043d \u0433\u0440\u0443\u043f\u043f\u044b \u043e\u043f\u043f\u043e\u043d\u0435\u043d\u0442\u0430 " + msg);
					if(receiver != null)
						receiver.sendMessage(p.getName() + " " + msg);
					clearFull(false);
					return;
				}
				GvG.players_list2.add(p.getObjectId());
				live_list2.add(p.getObjectId());
			}
		players1 = null;
		players2 = null;
		sayToAll("\u0422\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0430\u0446\u0438\u044f \u0438\u0433\u0440\u043e\u043a\u043e\u0432...");
		executeTask("events.GvG.GvG", "prepare", new Object[0], 2000L);
	}

	private static String checkPlayer(final Player player)
	{
		if(!player.isConnected() || player.isInOfflineMode())
			return "\u0432 \u043e\u0444\u0444\u043b\u0430\u0439\u043d\u0435!";
		if(player.isMounted())
			return "\u0441\u0442\u0435\u0431\u0430\u0435\u0442\u0441\u044f \u043d\u0430\u0434 \u0436\u0438\u0432\u043e\u0442\u043d\u044b\u043c!";
		if(player.isInDuel())
			return "\u043d\u0430\u0445\u043e\u0434\u0438\u0442\u0441\u044f \u0432 \u0434\u0443\u044d\u043b\u0438!";
		if(player.getTeam() != 0)
			return "\u0443\u0447\u0430\u0441\u0442\u043d\u0438\u043a \u044d\u0432\u0435\u043d\u0442\u0430!";
		if(player.getOlympiadGameId() > 0 || player.isInOlympiadMode() || Olympiad.isRegistered(player))
			return "\u0443\u0447\u0430\u0441\u0442\u043d\u0438\u043a \u041e\u043b\u0438\u043c\u043f\u0438\u0430\u0434\u044b!";
		if(player.isInParty() && player.getParty().isInDimensionalRift())
			return "\u0432 \u0420\u0438\u0444\u0442\u0435!";
		if(player.isTeleporting())
			return "\u0432 \u043f\u0440\u043e\u0446\u0435\u0441\u0441\u0435 \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0430\u0446\u0438\u0438!";
		if(player.isCursedWeaponEquipped())
			return "\u0432\u043b\u0430\u0434\u0435\u043b\u0435\u0446 \u043f\u0440\u043e\u043a\u043b\u044f\u0442\u043e\u0433\u043e \u043e\u0440\u0443\u0436\u0438\u044f!";
		if(player.inObserverMode())
			return "\u043d\u0430\u0445\u043e\u0434\u0438\u0442\u0441\u044f \u0432 \u0440\u0435\u0436\u0438\u043c\u0435 \u043f\u0440\u043e\u0441\u043c\u043e\u0442\u0440\u0430!";
		if(player.getReflectionId() != 0)
			return "\u043d\u0430\u0445\u043e\u0434\u0438\u0442\u0441\u044f \u0432 \u0438\u043d\u0441\u0442\u0430\u043d\u0441\u0435!";
		return null;
	}

	public static void prepare()
	{
		DoorTable.getInstance().getDoor(25190013).closeMe();
		clearArena();
		ressurectPlayers();
		teleportPlayersToArena();
		executeTask("events.GvG.GvG", "prepareNext", new Object[0], Config.GvG_Time_Paralyze * 1000);
		sayToAll("\u041f\u043e\u0434\u0433\u043e\u0442\u043e\u0432\u043a\u0430 \u043d\u0430\u0447\u043d\u0435\u0442\u0441\u044f \u0447\u0435\u0440\u0435\u0437 " + Config.GvG_Time_Paralyze + " \u0441\u0435\u043a\u0443\u043d\u0434.");
	}

	public static void prepareNext()
	{
		for(final Player player : getPlayers(GvG.players_list1, GvG.players_list2))
			if(player != null)
			{
				player.getAbnormalList().stop(4515);
				if(player.getServitor() == null)
					continue;
				player.getServitor().getAbnormalList().stop(4515);
			}
		executeTask("events.GvG.GvG", "go", new Object[0], Config.GvG_Time_Prepare * 1000);
		if(Config.GvG_Time_Prepare > 30)
			executeTask("events.GvG.GvG", "start30", new Object[0], (Config.GvG_Time_Prepare - 30) * 1000);
		if(Config.GvG_Time_Prepare > 10)
			executeTask("events.GvG.GvG", "start10", new Object[0], (Config.GvG_Time_Prepare - 10) * 1000);
		sayToAll("\u0421\u0442\u0430\u0440\u0442 \u0447\u0435\u0440\u0435\u0437 " + Config.GvG_Time_Prepare + " \u0441\u0435\u043a\u0443\u043d\u0434 \u043f\u043e \u043a\u043e\u043c\u0430\u043d\u0434\u0435.");
		sayToAllSc(Config.GvG_Time_Prepare + " seconds before the match begins!");
	}

	public static void go()
	{
		DoorTable.getInstance().getDoor(25190013).openMe();
		_status = 2;
		checkLive();
		clearArena();
		sayToAll(">>> \u0421\u0420\u0410\u0416\u0410\u0419\u0422\u0415\u0421\u042c!!! <<<");
		sayToAllSc("Fight!");
		_endTask = executeTask("events.GvG.GvG", "endBattle", new Object[0], Config.GvG_Time_Battle * 60000);
	}

	public static void endBattle()
	{
		if(_endTask != null)
		{
			_endTask.cancel(false);
			_endTask = null;
		}
		GvG._clearing = true;
		_status = 0;
		removeAura();
		if(GvG.winner == 1)
		{
			sayToAll("\u041f\u043e\u0431\u0435\u0434\u0438\u043b\u0430 \u0433\u0440\u0443\u043f\u043f\u0430 \u043a\u0440\u0430\u0441\u043d\u044b\u0445: " + GvG.count1 + " / " + GvG.count2 + "");
			sayToAllSc("Red wins!");
		}
		else if(GvG.winner == 2)
		{
			sayToAll("\u041f\u043e\u0431\u0435\u0434\u0438\u043b\u0430 \u0433\u0440\u0443\u043f\u043f\u0430 \u0441\u0438\u043d\u0438\u0445: " + GvG.count2 + " / " + GvG.count1 + "");
			sayToAllSc("Blue wins!");
		}
		else if(live_list1.isEmpty() && live_list2.isEmpty())
		{
			sayToAll("\u041d\u0438\u0447\u044c\u044f.");
			sayToAllSc("Tie!");
			GvG.winner = 0;
		}
		else if(live_list1.isEmpty())
		{
			sayToAll("\u041f\u043e\u0431\u0435\u0434\u0438\u043b\u0430 \u0433\u0440\u0443\u043f\u043f\u0430 \u0441\u0438\u043d\u0438\u0445: " + GvG.count2 + " / " + GvG.count1 + "");
			sayToAllSc("Blue wins!");
			GvG.winner = 2;
		}
		else if(live_list2.isEmpty())
		{
			sayToAll("\u041f\u043e\u0431\u0435\u0434\u0438\u043b\u0430 \u0433\u0440\u0443\u043f\u043f\u0430 \u043a\u0440\u0430\u0441\u043d\u044b\u0445: " + GvG.count1 + " / " + GvG.count2 + "");
			sayToAllSc("Red wins!");
			GvG.winner = 1;
		}
		else if(GvG.count1 > GvG.count2)
		{
			sayToAll("\u041f\u043e\u0431\u0435\u0434\u0438\u043b\u0430 \u0433\u0440\u0443\u043f\u043f\u0430 \u043a\u0440\u0430\u0441\u043d\u044b\u0445: " + GvG.count1 + " / " + GvG.count2 + "");
			sayToAllSc("Red wins!");
			GvG.winner = 1;
		}
		else if(GvG.count2 > GvG.count1)
		{
			sayToAll("\u041f\u043e\u0431\u0435\u0434\u0438\u043b\u0430 \u0433\u0440\u0443\u043f\u043f\u0430 \u0441\u0438\u043d\u0438\u0445: " + GvG.count2 + " / " + GvG.count1 + "");
			sayToAllSc("Blue wins!");
			GvG.winner = 2;
		}
		else if(GvG.count1 == GvG.count2)
		{
			sayToAll("\u041d\u0438\u0447\u044c\u044f.");
			sayToAllSc("Tie!");
			GvG.winner = 0;
		}
		if(GvG.winner == 1)
			giveItem(GvG._leader1, Config.GvG_ItemIds[GvG._order], GvG._count * 2);
		else if(GvG.winner == 2)
			giveItem(GvG._leader2, Config.GvG_ItemIds[GvG._order], GvG._count * 2);
		else
		{
			giveItem(GvG._leader1, Config.GvG_ItemIds[GvG._order], GvG._count);
			giveItem(GvG._leader2, Config.GvG_ItemIds[GvG._order], GvG._count);
		}
		sayToAll("\u0427\u0435\u0440\u0435\u0437 20 \u0441\u0435\u043a\u0443\u043d\u0434 \u0432\u0441\u0435 \u0443\u0447\u0430\u0441\u0442\u043d\u0438\u043a\u0438 \u0431\u0443\u0434\u0443\u0442 \u0432\u044b\u043b\u0435\u0447\u0435\u043d\u044b \u0438 \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u043d\u044b \u043e\u0431\u0440\u0430\u0442\u043d\u043e.");
		executeTask("events.GvG.GvG", "end", new Object[0], 20000L);
		GvG.count1 = 0;
		GvG.count2 = 0;
		GvG.winner = 0;
		GvG._order = 0;
		GvG._count = 0;
		GvG._leader1 = 0;
		GvG._leader2 = 0;
	}

	public static void end()
	{
		ressurectPlayers();
		executeTask("events.GvG.GvG", "teleportPlayersToSavedCoords", new Object[0], 1000L);
	}

	private static void relax(final Player player)
	{
		player.setVar("GvG_backCoords", player.getX() + " " + player.getY() + " " + player.getZ());
		if(player.isCastingNow())
			player.abortCast(true, false);
		unRide(player);
		unSummonPet(player, true);
		player.getAbnormalList().stopAll();
		GvG.revengeSkill.getEffects(player, player, false, false);
		final Servitor pet = player.getServitor();
		if(pet != null)
		{
			pet.getAbnormalList().stopAll();
			GvG.revengeSkill.getEffects(player, pet, false, false);
		}
	}

	private static void teleportPlayersToArena()
	{
		for(final Player player : getPlayers(GvG.players_list1))
			if(player != null)
			{
				player.setTeam(1, true);
				relax(player);
				player.teleToLocation(Location.findAroundPosition(GvG.BlueTeamLoc, 0, 200, player.getGeoIndex()));
			}
		for(final Player player : getPlayers(GvG.players_list2))
			if(player != null)
			{
				player.setTeam(2, true);
				relax(player);
				player.teleToLocation(Location.findAroundPosition(GvG.RedTeamLoc, 0, 200, player.getGeoIndex()));
			}
	}

	public static void teleportPlayersToSavedCoords()
	{
		for(final Player player : getPlayers(GvG.players_list1, GvG.players_list2))
			backPlayer(player);
		unsetLastCoords();
		GvG._clearing = false;
	}

	private static void ressurectPlayers()
	{
		for(final Player player : getPlayers(GvG.players_list1, GvG.players_list2))
			if(player != null)
				if(player.isDead())
				{
					player.restoreExp();
					player.setCurrentHp(player.getMaxHp(), true);
					player.setCurrentMp(player.getMaxMp());
					player.setCurrentCp(player.getMaxCp());
					player.broadcastPacket(new L2GameServerPacket[] { new Revive(player) });
				}
				else
				{
					player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp(), false);
					player.setCurrentCp(player.getMaxCp());
				}
	}

	private static void checkLive()
	{
		final List<Integer> new_live_list1 = new CopyOnWriteArrayList<Integer>();
		final List<Integer> new_live_list2 = new CopyOnWriteArrayList<Integer>();
		for(final Integer objId : live_list1)
		{
			final Player player = GameObjectsStorage.getPlayer(objId);
			if(player != null)
				new_live_list1.add(player.getObjectId());
		}
		for(final Integer objId : live_list2)
		{
			final Player player = GameObjectsStorage.getPlayer(objId);
			if(player != null)
				new_live_list2.add(player.getObjectId());
		}
		live_list1 = new_live_list1;
		live_list2 = new_live_list2;
		int c1 = 0;
		int c2 = 0;
		for(final Player player2 : getPlayers(live_list1))
			if(player2.isDead())
				++c1;
		for(final Player player2 : getPlayers(live_list2))
			if(player2.isDead())
				++c2;
		if(live_list1.size() > 0 && live_list2.size() > 0)
			GvG.winner = c1 >= live_list1.size() ? 2 : c2 >= live_list2.size() ? 1 : 0;
		if(GvG.winner > 0 || live_list1.size() < 1 || live_list2.size() < 1)
			endBattle();
	}

	private static void removeAura()
	{
		for(final Player player : getPlayers(live_list1))
			if(player != null)
				player.setTeam(0, true);
		for(final Player player : getPlayers(live_list2))
			if(player != null)
				player.setTeam(0, true);
	}

	private static void clearArena()
	{
		for(final GameObject obj : _zone.getObjects())
			if(obj != null)
			{
				final Player player = obj.getPlayer();
				if(player != null && !live_list1.contains(player.getObjectId()) && !live_list2.contains(player.getObjectId()) && !player.isGM())
					player.teleToLocation(ClearLoc);
			}
	}

	public static void OnDie(final Creature self, final Creature killer)
	{
		if(_status != 2 || self == null)
			return;
		final Player p = self.getPlayer();
		if(p != null && p.getTeam() > 0 && (live_list1.contains(self.getObjectId()) || live_list2.contains(self.getObjectId())))
		{
			final Player pk = killer.getPlayer();
			if(pk != null)
				if(live_list1.contains(pk.getObjectId()) && live_list2.contains(p.getObjectId()))
					++GvG.count1;
				else if(live_list2.contains(pk.getObjectId()) && live_list1.contains(p.getObjectId()))
					++GvG.count2;
			checkLive();
		}
	}

	public static Location OnEscape(final Player player)
	{
		if(_status == 2 && player != null && player.getTeam() > 0 && (live_list1.contains(player.getObjectId()) || live_list2.contains(player.getObjectId())))
		{
			removePlayer(player);
			checkLive();
		}
		return null;
	}

	public static void OnPlayerExit(final Player player)
	{
		if(player == null || player.getTeam() < 1)
			return;
		if(_status == 1 && (live_list1.contains(player.getObjectId()) || live_list2.contains(player.getObjectId())))
		{
			removePlayer(player);
			backPlayer(player);
			return;
		}
		OnEscape(player);
	}

	private static void backPlayer(final Player player)
	{
		if(player == null)
			return;
		try
		{
			final String var = player.getVar("GvG_backCoords");
			if(var == null)
				return;
			if(var.equals(""))
			{
				player.unsetVar("GvG_backCoords");
				return;
			}
			final String[] coords = var.split(" ");
			if(coords.length < 3)
				return;
			player.teleToLocation(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]));
			player.unsetVar("GvG_backCoords");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void unsetLastCoords()
	{
		mysql.set("DELETE FROM `character_variables` WHERE `name`='GvG_backCoords'");
	}

	private static void removePlayer(final Player player)
	{
		if(player != null)
		{
			live_list1.remove(player.getObjectId());
			live_list2.remove(player.getObjectId());
			GvG.players_list1.remove(player.getObjectId());
			GvG.players_list2.remove(player.getObjectId());
			player.setTeam(0, true);
		}
	}

	private static void giveItem(final int objId, final int itemId, final int count)
	{
		final Player player = GameObjectsStorage.getPlayer(GvG._leader1);
		if(player != null)
			addItem(player, itemId, count);
		else
			Util.giveItem(objId, itemId, count);
	}

	static
	{
		_log = LoggerFactory.getLogger(GvG.class);
		GvG._order = 0;
		GvG._count = 0;
		GvG._leader1 = 0;
		GvG._leader2 = 0;
		revengeSkill = SkillTable.getInstance().getInfo(4515, 1);
		GvG.players_list1 = new CopyOnWriteArrayList<Integer>();
		GvG.players_list2 = new CopyOnWriteArrayList<Integer>();
		live_list1 = new CopyOnWriteArrayList<Integer>();
		live_list2 = new CopyOnWriteArrayList<Integer>();
		GvG.count1 = 0;
		GvG.count2 = 0;
		GvG.winner = 0;
		_status = 0;
		GvG._clearing = false;
		_zone = ZoneManager.getInstance().getZone(Config.GvG_Zone);
		_zoneListener = new ZoneListener();
		BlueTeamLoc = Location.parseLoc(Config.GvG_BlueTeamLoc);
		RedTeamLoc = Location.parseLoc(Config.GvG_RedTeamLoc);
		ClearLoc = Location.parseLoc(Config.GvG_ClearLoc);
		_active = false;
	}

	private static class ZoneListener extends ZoneEnterLeaveListener
	{
		@Override
		public void objectEntered(final Zone zone, final GameObject object)
		{
			if(object == null)
				return;
			final Player player = object.getPlayer();
			if(_status > 0 && player != null && !live_list1.contains(player.getObjectId()) && !live_list2.contains(player.getObjectId()) && !player.isGM())
				ThreadPoolManager.getInstance().schedule(new TeleportTask((Creature) object, ClearLoc), 3000L);
		}

		@Override
		public void objectLeaved(final Zone zone, final GameObject object)
		{
			if(object == null)
				return;
			final Player player = object.getPlayer();
			if(_status == 2 && player != null && player.getTeam() > 0 && (live_list1.contains(player.getObjectId()) || live_list2.contains(player.getObjectId())))
			{
				final double angle = Util.convertHeadingToDegree(object.getHeading());
				final double radian = Math.toRadians(angle - 90.0);
				final int x = (int) (object.getX() + 50.0 * Math.sin(radian));
				final int y = (int) (object.getY() - 50.0 * Math.cos(radian));
				final int z = object.getZ();
				ThreadPoolManager.getInstance().schedule(new TeleportTask((Creature) object, new Location(x, y, z)), 3000L);
			}
		}
	}

	private static class TeleportTask implements Runnable
	{
		Location loc;
		Creature target;

		public TeleportTask(final Creature target, final Location loc)
		{
			this.target = target;
			this.loc = loc;
			target.startStunning();
		}

		@Override
		public void run()
		{
			target.stopStunning();
			target.teleToLocation(loc);
		}
	}
}
