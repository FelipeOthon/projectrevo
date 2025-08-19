package commands.admin;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import l2s.commons.dao.JdbcEntityState;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.cache.Msg;
import l2s.gameserver.data.xml.holder.ResidenceHolder;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.events.impl.SiegeEvent;
import l2s.gameserver.model.entity.events.objects.SiegeClanObject;
import l2s.gameserver.model.entity.residence.Castle;
import l2s.gameserver.model.entity.residence.ClanHall;
import l2s.gameserver.model.entity.residence.Residence;
import l2s.gameserver.model.entity.residence.ResidenceType;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.c2s.RequestJoinSiege;
import l2s.gameserver.network.l2.s2c.CastleSiegeAttackerList;
import l2s.gameserver.network.l2.s2c.CastleSiegeDefenderList;
import l2s.gameserver.network.l2.s2c.NpcHtmlMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.ClanTable;
import l2s.gameserver.utils.HtmlUtils;
import l2s.gameserver.utils.Log;

public class AdminSiege implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().CanEditNPC)
			return false;
		final StringTokenizer st = new StringTokenizer(command, " ");
		st.nextToken();
		if(command.equalsIgnoreCase("admin_residence_list"))
		{
			final NpcHtmlMessage msg = new NpcHtmlMessage(5);
			msg.setFile("admin/residence_list.htm");
			final StringBuilder replyMSG = new StringBuilder(200);
			for(final Residence residence : ResidenceHolder.getInstance().getResidences())
				if(residence != null)
				{
					replyMSG.append("<tr><td>");
					replyMSG.append("<a action=\"bypass -h admin_residence ").append(residence.getId()).append("\">").append(HtmlUtils.htmlResidenceName(residence.getId())).append("</a>");
					replyMSG.append("</td><td>");
					final Clan owner = residence.getOwner();
					if(owner == null)
						replyMSG.append("NPC");
					else
						replyMSG.append(owner.getName());
					replyMSG.append("</td></tr>");
				}
			msg.replace("%residence_list%", replyMSG.toString());
			activeChar.sendPacket(msg);
		}
		else if(command.startsWith("admin_residence") && st.hasMoreTokens())
		{
			final Residence r = ResidenceHolder.getInstance().getResidence(Integer.parseInt(st.nextToken()));
			if(r == null)
				return false;
			final SiegeEvent<?, ?> event = r.getSiegeEvent();
			if(event == null)
				return false;
			final NpcHtmlMessage msg = new NpcHtmlMessage(5);
			msg.setFile("admin/siege_info.htm");
			msg.replace("%residence%", HtmlUtils.htmlResidenceName(r.getId()));
			msg.replace("%id%", String.valueOf(r.getId()));
			msg.replace("%owner%", r.getOwner() == null ? "NPC" : r.getOwner().getName());
			msg.replace("%cycle%", String.valueOf(r.getCycle()));
			msg.replace("%paid_cycle%", String.valueOf(r.getPaidCycle()));
			msg.replace("%reward_count%", String.valueOf(r.getRewardCount()));
			msg.replace("%left_time%", String.valueOf(r.getCycleDelay()));
			final StringBuilder clans = new StringBuilder(100);
			for(final Map.Entry<Object, List<Object>> entry : event.getObjects().entrySet())
				for(final Object o : entry.getValue())
					if(o instanceof SiegeClanObject)
					{
						final SiegeClanObject siegeClanObject = (SiegeClanObject) o;
						clans.append("<tr>").append("<td>").append(siegeClanObject.getClan().getName()).append("</td>").append("<td>").append(siegeClanObject.getClan().getLeaderName()).append("</td>").append("<td>").append(siegeClanObject.getType()).append("</td>").append("</tr>");
					}
			msg.replace("%clans%", clans.toString());
			msg.replace("%hour%", String.valueOf(r.getSiegeDate().get(11)));
			msg.replace("%minute%", String.valueOf(r.getSiegeDate().get(12)));
			msg.replace("%day%", String.valueOf(r.getSiegeDate().get(5)));
			msg.replace("%month%", String.valueOf(r.getSiegeDate().get(2) + 1));
			msg.replace("%year%", String.valueOf(r.getSiegeDate().get(1)));
			activeChar.sendPacket(msg);
		}
		else if(command.startsWith("admin_set_owner") && st.hasMoreTokens())
		{
			final Residence r = ResidenceHolder.getInstance().getResidence(Integer.parseInt(st.nextToken()));
			if(r == null)
				return false;
			final SiegeEvent<?, ?> event = r.getSiegeEvent();
			if(event == null)
				return false;
			Clan clan = null;
			String clanName = "";
			if(st.hasMoreTokens())
				clanName = st.nextToken();
			if(!clanName.equalsIgnoreCase("npc"))
			{
				clan = ClanTable.getInstance().getClanByName(clanName);
				if(clan == null)
				{
					activeChar.sendPacket(Msg.INCORRECT_NAME_PLEASE_TRY_AGAIN);
					useAdminCommand("admin_residence " + r.getId(), activeChar);
					return false;
				}
				clanName = clan.getName();
			}
			else
				clanName = "NPC";
			event.clearActions();
			r.getLastSiegeDate().setTimeInMillis(clan == null ? 0L : System.currentTimeMillis());
			r.getOwnDate().setTimeInMillis(clan == null ? 0L : System.currentTimeMillis());
			r.changeOwner(clan);
			event.reCalcNextTime(false);
			Log.addLog(activeChar.toString() + " set residence " + r.getName() + " to clan " + clanName, "gm_actions");
		}
		else if(command.startsWith("admin_set_siege_time") && st.hasMoreTokens())
		{
			final String[] wordList = command.split(" ");
			if(wordList.length < 3)
				return false;
			final Residence r = ResidenceHolder.getInstance().getResidence(Integer.parseInt(wordList[1]));
			if(r == null)
				return false;
			final SiegeEvent<?, ?> event = r.getSiegeEvent();
			if(event == null)
				return false;
			final Calendar calendar = (Calendar) r.getSiegeDate().clone();
			for(int i = 2; i < wordList.length; ++i)
			{
				int val;
				try
				{
					val = Integer.parseInt(wordList[i]);
				}
				catch(Exception e)
				{
					activeChar.sendMessage("\u041d\u0435\u0432\u0435\u0440\u043d\u044b\u0439 \u0444\u043e\u0440\u043c\u0430\u0442!");
					return false;
				}
				int type = 0;
				switch(i)
				{
					case 2:
					{
						type = 11;
						break;
					}
					case 3:
					{
						type = 5;
						break;
					}
					case 4:
					{
						type = 2;
						--val;
						break;
					}
					case 5:
					{
						type = 1;
						break;
					}
					case 6:
					{
						type = 12;
						break;
					}
					default:
					{
						continue;
					}
				}
				calendar.set(type, val);
			}
			event.clearActions();
			r.getSiegeDate().setTimeInMillis(calendar.getTimeInMillis());
			event.registerActions();
			r.setJdbcState(JdbcEntityState.UPDATED);
			r.update();
			useAdminCommand("admin_residence " + r.getId(), activeChar);
		}
		else if(command.startsWith("admin_quick_siege_start") && st.hasMoreTokens())
		{
			final Residence r = ResidenceHolder.getInstance().getResidence(Integer.parseInt(st.nextToken()));
			if(r == null)
				return false;
			final SiegeEvent<?, ?> event = r.getSiegeEvent();
			if(event == null)
				return false;
			final Calendar calendar = Calendar.getInstance();
			if(st.hasMoreTokens())
				calendar.add(13, Integer.parseInt(st.nextToken()));
			event.clearActions();
			r.getSiegeDate().setTimeInMillis(calendar.getTimeInMillis());
			event.registerActions();
			r.setJdbcState(JdbcEntityState.UPDATED);
			r.update();
			useAdminCommand("admin_residence " + r.getId(), activeChar);
		}
		else if(command.startsWith("admin_quick_siege_stop") && st.hasMoreTokens())
		{
			final Residence r = ResidenceHolder.getInstance().getResidence(Integer.parseInt(st.nextToken()));
			if(r == null)
				return false;
			final SiegeEvent<?, ?> event = r.getSiegeEvent();
			if(event == null)
				return false;
			event.clearActions();
			ThreadPoolManager.getInstance().execute(() -> event.stopEvent());
			useAdminCommand("admin_residence " + r.getId(), activeChar);
		}
		else if(command.startsWith("admin_add_attacker") && st.hasMoreTokens())
		{
			final Residence r = ResidenceHolder.getInstance().getResidence(Integer.parseInt(st.nextToken()));
			if(r == null)
				return false;
			final GameObject t = activeChar.getTarget();
			if(t == null || !t.isPlayer())
				return false;
			final Player player = (Player) t;
			if(player.getClan() == null)
			{
				activeChar.sendMessage(player.getName() + " \u043d\u0435 \u0441\u043e\u0441\u0442\u043e\u0438\u0442 \u0432 \u043a\u043b\u0430\u043d\u0435.");
				return false;
			}
			if(r.getType() == ResidenceType.Castle)
				RequestJoinSiege.registerAtCastle(player, (Castle) r, true, true, true);
			else if(r.getType() == ResidenceType.ClanHall)
				RequestJoinSiege.registerAtClanHall(player, (ClanHall) r, true, true);
			activeChar.sendMessage("\u041f\u043e\u043f\u044b\u0442\u043a\u0430 \u0434\u043e\u0431\u0430\u0432\u0438\u0442\u044c \u043a\u043b\u0430\u043d " + player.getClan().getName() + " \u043d\u0430 \u0441\u0442\u043e\u0440\u043e\u043d\u0443 \u0430\u0442\u0430\u043a\u0443\u044e\u0449\u0438\u0445 " + r.getName());
			activeChar.sendPacket(new CastleSiegeAttackerList(r));
			useAdminCommand("admin_residence " + r.getId(), activeChar);
		}
		else if(command.startsWith("admin_add_defender") && st.hasMoreTokens())
		{
			final Residence r = ResidenceHolder.getInstance().getResidence(Integer.parseInt(st.nextToken()));
			if(r == null)
				return false;
			final GameObject t = activeChar.getTarget();
			if(t == null || !t.isPlayer())
				return false;
			final Player player = (Player) t;
			if(player.getClan() == null)
			{
				activeChar.sendMessage(player.getName() + " \u043d\u0435 \u0441\u043e\u0441\u0442\u043e\u0438\u0442 \u0432 \u043a\u043b\u0430\u043d\u0435.");
				return false;
			}
			if(r.getType() != ResidenceType.Castle)
			{
				activeChar.sendMessage("\u0417\u0430\u0449\u0438\u0442\u043d\u0438\u043a\u0438 \u0434\u043e\u043f\u0443\u0441\u0442\u0438\u043c\u044b \u0442\u043e\u043b\u044c\u043a\u043e \u0432 \u043e\u0441\u0430\u0434\u0435 \u0437\u0430\u043c\u043a\u0430!");
				return false;
			}
			RequestJoinSiege.registerAtCastle(player, (Castle) r, false, true, true);
			activeChar.sendMessage("\u041f\u043e\u043f\u044b\u0442\u043a\u0430 \u0434\u043e\u0431\u0430\u0432\u0438\u0442\u044c \u043a\u043b\u0430\u043d " + player.getClan().getName() + " \u043d\u0430 \u0441\u0442\u043e\u0440\u043e\u043d\u0443 \u0437\u0430\u0449\u0438\u0449\u0430\u044e\u0449\u0438\u0445 " + r.getName());
			activeChar.sendPacket(new CastleSiegeDefenderList((Castle) r));
			useAdminCommand("admin_residence " + r.getId(), activeChar);
		}
		else if(command.startsWith("admin_del_siege_reg") && st.hasMoreTokens())
		{
			final Residence r = ResidenceHolder.getInstance().getResidence(Integer.parseInt(st.nextToken()));
			if(r == null)
				return false;
			final SiegeEvent<?, ?> siegeEvent = r.getSiegeEvent();
			if(siegeEvent == null)
				return false;
			final GameObject t = activeChar.getTarget();
			if(t == null || !t.isPlayer())
				return false;
			final Player player = (Player) t;
			if(player.getClan() == null)
			{
				activeChar.sendMessage(player.getName() + " \u043d\u0435 \u0441\u043e\u0441\u0442\u043e\u0438\u0442 \u0432 \u043a\u043b\u0430\u043d\u0435.");
				return false;
			}
			final Clan playerClan = player.getClan();
			final boolean defender = r.getType() == ResidenceType.Castle && siegeEvent.getSiegeClan("attackers", playerClan) == null;
			if(r.getType() == ResidenceType.Castle)
				RequestJoinSiege.registerAtCastle(player, (Castle) r, true, false, true);
			else if(r.getType() == ResidenceType.ClanHall)
				RequestJoinSiege.registerAtClanHall(player, (ClanHall) r, false, true);
			activeChar.sendMessage("\u041f\u043e\u043f\u044b\u0442\u043a\u0430 \u043e\u0442\u043c\u0435\u043d\u0438\u0442\u044c \u0443\u0447\u0430\u0441\u0442\u0438\u0435 \u043a\u043b\u0430\u043d\u0430 " + playerClan.getName() + " \u0432 \u043e\u0441\u0430\u0434\u0435 " + r.getName());
			if(defender)
				activeChar.sendPacket(new CastleSiegeDefenderList((Castle) r));
			else
				activeChar.sendPacket(new CastleSiegeAttackerList(r));
			useAdminCommand("admin_residence " + r.getId(), activeChar);
		}
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminSiege._adminCommands;
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
		AdminSiege._adminCommands = new String[] {
				"admin_residence_list",
				"admin_residence",
				"admin_set_owner",
				"admin_set_siege_time",
				"admin_quick_siege_start",
				"admin_quick_siege_stop",
				"admin_add_attacker",
				"admin_add_defender",
				"admin_del_siege_reg" };
	}
}
