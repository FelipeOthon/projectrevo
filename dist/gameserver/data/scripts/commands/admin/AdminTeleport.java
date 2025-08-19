package commands.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.StringTokenizer;

import l2s.commons.dbcp.DbUtils;
import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.ai.CtrlIntention;
import l2s.gameserver.cache.Msg;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.geodata.GeoEngine;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Party;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.DoorInstance;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.pledge.Clan;
import l2s.gameserver.network.l2.s2c.NpcHtmlMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.DoorTable;
import l2s.gameserver.utils.Location;

public class AdminTeleport implements IAdminCommandHandler, ScriptFile
{
	private static String[] _adminCommands;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().CanTeleport)
			return false;
		if(command.startsWith("admin_instant_move"))
		{
			if(command.equals("admin_instant_move"))
				activeChar.setTeleMode(1);
			else if(command.equals("admin_instant_move r"))
				activeChar.setTeleMode(2);
			else if(command.equals("admin_instant_move end"))
				activeChar.setTeleMode(0);
		}
		else if(command.equals("admin_show_moves"))
			AdminHelpPage.showHelpPage(activeChar, "teleports.htm");
		if(command.equals("admin_show_moves_other"))
			AdminHelpPage.showHelpPage(activeChar, "tele/other.htm");
		else if(command.equals("admin_show_teleport"))
			showTeleportCharWindow(activeChar);
		else if(command.equals("admin_recall_npc"))
		{
			final GameObject obj = activeChar.getTarget();
			if(obj != null && obj.isNpc())
				((NpcInstance) obj).teleToLocation(activeChar.getLoc());
			else
				activeChar.sendPacket(Msg.INCORRECT_TARGET);
		}
		else if(command.equals("admin_returntospawn"))
		{
			final GameObject obj = activeChar.getTarget();
			if(obj != null && obj.isNpc())
				((NpcInstance) obj).teleToLocation(((NpcInstance) obj).getSpawnedLoc());
			else
				activeChar.sendPacket(Msg.INCORRECT_TARGET);
		}
		else if(command.equals("admin_teleport_to_character"))
			teleportToCharacter(activeChar, activeChar.getTarget());
		else if(command.startsWith("admin_walk"))
			try
			{
				final String val = command.substring(11);
				final StringTokenizer st = new StringTokenizer(val);
				final String x1 = st.nextToken();
				final int x2 = Integer.parseInt(x1);
				final String y1 = st.nextToken();
				final int y2 = Integer.parseInt(y1);
				final String z1 = st.nextToken();
				final int z2 = Integer.parseInt(z1);
				activeChar.moveToLocation(x2, y2, z2, 0, true);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		else if(command.startsWith("admin_move_to "))
		{
			try
			{
				final String val = command.substring(14);
				teleportTo(activeChar, val);
			}
			catch(StringIndexOutOfBoundsException e2)
			{
				activeChar.sendMessage("Wrong or no Coordinates given.");
			}
		}
		else if(command.startsWith("admin_moveto "))
		{
			try
			{
				final String val = command.substring(13);
				teleportTo(activeChar, val);
			}
			catch(StringIndexOutOfBoundsException e2)
			{
				activeChar.sendMessage("Wrong or no Coordinates given.");
			}
		}
		else if(command.startsWith("admin_teleport "))
		{
			try
			{
				final String val = command.substring(15);
				teleportTo(activeChar, val);
			}
			catch(StringIndexOutOfBoundsException e2)
			{
				activeChar.sendMessage("Wrong or no Coordinates given.");
			}
		}
		else if(command.startsWith("admin_teleport_character"))
			try
			{
				final String val = command.substring(25);
				this.teleportCharacter(activeChar, val);
			}
			catch(StringIndexOutOfBoundsException e2)
			{
				activeChar.sendMessage("Wrong or no Coordinates given.");
				showTeleportCharWindow(activeChar);
			}
		else if(command.startsWith("admin_teleportto "))
		{
			try
			{
				final String targetName = command.substring(17);
				final Player player = GameObjectsStorage.getPlayer(targetName);
				teleportToCharacter(activeChar, player);
			}
			catch(StringIndexOutOfBoundsException ex)
			{}
		}
		else if(command.startsWith("admin_teleport_to "))
		{
			try
			{
				final String targetName = command.substring(18);
				final Player player = GameObjectsStorage.getPlayer(targetName);
				teleportToCharacter(activeChar, player);
			}
			catch(StringIndexOutOfBoundsException ex)
			{}
		}
		else if(command.startsWith("admin_recall "))
			try
			{
				final String targetName = command.substring(13);
				final Player player = GameObjectsStorage.getPlayer(targetName);
				if(player == null)
				{
					activeChar.sendMessage("->" + targetName + "<- not found. Trying offline recall.");
					teleportCharacter_offline(targetName, activeChar.getLoc());
				}
				else
					this.teleportCharacter(player, activeChar.getLoc());
			}
			catch(StringIndexOutOfBoundsException ex2)
			{}
		else if(command.startsWith("admin_precall "))
			try
			{
				final String targetName = command.substring(14);
				final Player player = GameObjectsStorage.getPlayer(targetName);
				if(player == null)
					activeChar.sendMessage("Player " + targetName + " not found.");
				else
				{
					final Party party = player.getParty();
					if(party != null)
					{
						final int x2 = activeChar.getX();
						final int y3 = activeChar.getY();
						final int z3 = activeChar.getZ();
						activeChar.sendMessage("Teleporting party of player " + player.getName());
						for(final Player p : party.getPartyMembers())
							if(p != null)
								p.teleToLocation(Location.findAroundPosition(x2, y3, z3, 20, 150, activeChar.getGeoIndex()), activeChar.getReflectionId());
					}
					else
						activeChar.sendMessage("Player " + player.getName() + " not in party.");
				}
			}
			catch(StringIndexOutOfBoundsException ex3)
			{}
		else if(command.startsWith("admin_crecall "))
			try
			{
				final String targetName = command.substring(14);
				final Player player = GameObjectsStorage.getPlayer(targetName);
				if(player == null)
					activeChar.sendMessage("Player " + targetName + " not found.");
				else
				{
					final Clan clan = player.getClan();
					if(clan != null)
					{
						final int x2 = activeChar.getX();
						final int y3 = activeChar.getY();
						final int z3 = activeChar.getZ();
						activeChar.sendMessage("Teleporting clan of player " + player.getName());
						for(final Player p2 : clan.getOnlineMembers(0))
							if(p2 != null)
								p2.teleToLocation(Location.findAroundPosition(x2, y3, z3, 20, 150, activeChar.getGeoIndex()), activeChar.getReflectionId());
					}
					else
						activeChar.sendMessage("Player " + player.getName() + " not in clan.");
				}
			}
			catch(StringIndexOutOfBoundsException ex4)
			{}
		else if(command.startsWith("admin_ccrecall "))
			try
			{
				final String targetName = command.substring(15);
				final Player player = GameObjectsStorage.getPlayer(targetName);
				if(player == null)
					activeChar.sendMessage("Player " + targetName + " not found.");
				else if(player.isInParty() && player.getParty().isInCommandChannel())
				{
					final int x3 = activeChar.getX();
					final int y4 = activeChar.getY();
					final int z4 = activeChar.getZ();
					activeChar.sendMessage("Teleporting cc of player " + player.getName());
					for(final Player member : player.getParty().getCommandChannel().getMembers())
						if(member != null)
							member.teleToLocation(Location.findAroundPosition(x3, y4, z4, 20, 150, activeChar.getGeoIndex()), activeChar.getReflectionId());
				}
				else
					activeChar.sendMessage("Player " + player.getName() + " not in cc.");
			}
			catch(StringIndexOutOfBoundsException ex5)
			{}
		else if(command.startsWith("admin_failed"))
		{
			activeChar.sendMessage("Trying ActionFailed...");
			activeChar.sendActionFailed();
		}
		else if(command.equals("admin_tele"))
			showTeleportWindow(activeChar);
		else if(command.equals("admin_goup"))
		{
			activeChar.teleToLocation(activeChar.getLoc().changeZ(150));
			showTeleportWindow(activeChar);
		}
		else if(command.startsWith("admin_goup"))
			try
			{
				final String val = command.substring(11);
				activeChar.teleToLocation(activeChar.getLoc().changeZ(Integer.parseInt(val)));
				showTeleportWindow(activeChar);
			}
			catch(StringIndexOutOfBoundsException ex6)
			{}
		else if(command.equals("admin_godown"))
		{
			activeChar.teleToLocation(activeChar.getLoc().changeZ(GeoEngine.getLowerHeight(activeChar.getX(), activeChar.getY(), activeChar.getZ() - Config.MAX_Z_DIFF, activeChar.getGeoIndex())));
			showTeleportWindow(activeChar);
		}
		else if(command.startsWith("admin_godown"))
		{
			activeChar.teleToLocation(activeChar.getLoc().changeZ(GeoEngine.getUpperHeight(activeChar.getX(), activeChar.getY(), activeChar.getZ(), activeChar.getGeoIndex())));
			showTeleportWindow(activeChar);
		}
		else if(command.equals("admin_goeast"))
		{
			final int x4 = activeChar.getX();
			final int y5 = activeChar.getY();
			final int z5 = activeChar.getZ();
			activeChar.teleToLocation(x4 + 150, y5, z5);
			showTeleportWindow(activeChar);
		}
		else if(command.startsWith("admin_goeast"))
			try
			{
				final String val = command.substring(13);
				final int intVal = Integer.parseInt(val);
				final int x3 = activeChar.getX() + intVal;
				final int y4 = activeChar.getY();
				final int z4 = activeChar.getZ();
				activeChar.teleToLocation(x3, y4, z4);
				showTeleportWindow(activeChar);
			}
			catch(StringIndexOutOfBoundsException ex8)
			{}
		else if(command.equals("admin_gowest"))
		{
			final int x4 = activeChar.getX();
			final int y5 = activeChar.getY();
			final int z5 = activeChar.getZ();
			activeChar.teleToLocation(x4 - 150, y5, z5);
			showTeleportWindow(activeChar);
		}
		else if(command.startsWith("admin_gowest"))
			try
			{
				final String val = command.substring(13);
				final int intVal = Integer.parseInt(val);
				final int x3 = activeChar.getX() - intVal;
				final int y4 = activeChar.getY();
				final int z4 = activeChar.getZ();
				activeChar.teleToLocation(x3, y4, z4);
				showTeleportWindow(activeChar);
			}
			catch(StringIndexOutOfBoundsException ex9)
			{}
		else if(command.equals("admin_gosouth"))
		{
			final int x4 = activeChar.getX();
			final int y5 = activeChar.getY() + 150;
			final int z5 = activeChar.getZ();
			activeChar.teleToLocation(x4, y5, z5);
			showTeleportWindow(activeChar);
		}
		else if(command.startsWith("admin_gosouth"))
			try
			{
				final String val = command.substring(14);
				final int intVal = Integer.parseInt(val);
				final int x3 = activeChar.getX();
				final int y4 = activeChar.getY() + intVal;
				final int z4 = activeChar.getZ();
				activeChar.teleToLocation(x3, y4, z4);
				showTeleportWindow(activeChar);
			}
			catch(StringIndexOutOfBoundsException ex10)
			{}
		else if(command.equals("admin_gonorth"))
		{
			final int x4 = activeChar.getX();
			final int y5 = activeChar.getY();
			final int z5 = activeChar.getZ();
			activeChar.teleToLocation(x4, y5 - 150, z5);
			showTeleportWindow(activeChar);
		}
		else if(command.startsWith("admin_gonorth"))
			try
			{
				final String val = command.substring(14);
				final int intVal = Integer.parseInt(val);
				final int x3 = activeChar.getX();
				final int y4 = activeChar.getY() - intVal;
				final int z4 = activeChar.getZ();
				activeChar.teleToLocation(x3, y4, z4);
				showTeleportWindow(activeChar);
			}
			catch(StringIndexOutOfBoundsException ex11)
			{}
		else if(command.startsWith("admin_tonpc"))
		{
			String npcName = command.substring(12);
			List<NpcInstance> npcs;
			try
			{
				npcs = GameObjectsStorage.getNpcs(true, Integer.parseInt(npcName));
				if(!npcs.isEmpty())
				{
					teleportToCharacter(activeChar, Rnd.get(npcs));
					return true;
				}
			}
			catch(Exception e)
			{
				//
			}
			npcs = GameObjectsStorage.getNpcs(true, npcName);
			if(!npcs.isEmpty())
			{
				teleportToCharacter(activeChar, Rnd.get(npcs));
				return true;
			}
			activeChar.sendMessage("Npc " + npcName + " not found");
		}
		else if(command.startsWith("admin_todoor"))
			try
			{
				final int id = Integer.parseInt(command.substring(13));
				final DoorInstance door = DoorTable.getInstance().getDoor(id);
				if(door != null)
					activeChar.teleToLocation(Location.findPointToStay(door.getLoc(), 100, 250, activeChar.getGeoIndex()));
				else
					activeChar.sendMessage("Door " + id + " not found.");
			}
			catch(Exception e)
			{
				activeChar.sendMessage("Usage: //todoor <door_id>");
			}
		else if(command.startsWith("admin_toitem"))
			try
			{
				final int id = Integer.parseInt(command.substring(13));
				final ItemInstance item = GameObjectsStorage.getItem(id);
				if(item != null)
				{
					final Player owner = item.getOwner();
					teleportToCharacter(activeChar, owner != null ? owner : item);
				}
				else
					activeChar.sendMessage("Item " + id + " not found.");
			}
			catch(Exception e)
			{
				activeChar.sendMessage("Usage: //toitem <item_id>");
			}
		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return AdminTeleport._adminCommands;
	}

	private void teleportTo(final Player activeChar, final String Cords)
	{
		StringTokenizer st = new StringTokenizer(Cords);
		if(st.countTokens() < 2)
		{
			activeChar.sendMessage("2 coordinates required to teleport.");
			return;
		}
		int x = Integer.parseInt(st.nextToken());
		int y = Integer.parseInt(st.nextToken());
		int z = st.hasMoreTokens() ? Integer.parseInt(st.nextToken()) : Config.MAP_MAX_Z;
		Location loc = new Location(x, y, z);
		loc.correctGeoZ(activeChar.getGeoIndex());
		activeChar.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
		activeChar.teleToLocation(loc);
		activeChar.sendMessage("You have been teleported to " + Cords);
	}

	private void showTeleportWindow(final Player activeChar)
	{
		final NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		final StringBuffer replyMSG = new StringBuffer("<html><title>Teleport Menu</title>");
		replyMSG.append("<body>");
		replyMSG.append("<br>");
		replyMSG.append("<center><table>");
		replyMSG.append("<tr><td><button value=\"  \" action=\"bypass -h admin_tele\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("<td><button value=\"North\" action=\"bypass -h admin_gonorth\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("<td><button value=\"Up\" action=\"bypass -h admin_goup\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
		replyMSG.append("<tr><td><button value=\"West\" action=\"bypass -h admin_gowest\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("<td><button value=\"  \" action=\"bypass -h admin_tele\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("<td><button value=\"East\" action=\"bypass -h admin_goeast\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
		replyMSG.append("<tr><td><button value=\"  \" action=\"bypass -h admin_tele\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("<td><button value=\"South\" action=\"bypass -h admin_gosouth\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>");
		replyMSG.append("<td><button value=\"Down\" action=\"bypass -h admin_godown\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");
		replyMSG.append("</table></center>");
		replyMSG.append("</body></html>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}

	private void showTeleportCharWindow(final Player activeChar)
	{
		final GameObject target = activeChar.getTarget();
		Player player = null;
		if(target.isPlayer())
		{
			player = (Player) target;
			final NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
			final StringBuffer replyMSG = new StringBuffer("<html><title>Teleport Character</title>");
			replyMSG.append("<body>");
			replyMSG.append("The character you will teleport is " + player.getName() + ".");
			replyMSG.append("<br>");
			replyMSG.append("Co-ordinate x");
			replyMSG.append("<edit var=\"char_cord_x\" width=110>");
			replyMSG.append("Co-ordinate y");
			replyMSG.append("<edit var=\"char_cord_y\" width=110>");
			replyMSG.append("Co-ordinate z");
			replyMSG.append("<edit var=\"char_cord_z\" width=110>");
			replyMSG.append("<button value=\"Teleport\" action=\"bypass -h admin_teleport_character $char_cord_x $char_cord_y $char_cord_z\" width=60 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
			replyMSG.append("<button value=\"Teleport near you\" action=\"bypass -h admin_teleport_character " + activeChar.getX() + " " + activeChar.getY() + " " + activeChar.getZ() + "\" width=115 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
			replyMSG.append("<center><button value=\"Back\" action=\"bypass -h admin_current_player\" width=40 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></center>");
			replyMSG.append("</body></html>");
			adminReply.setHtml(replyMSG.toString());
			activeChar.sendPacket(adminReply);
			return;
		}
		activeChar.sendPacket(Msg.INCORRECT_TARGET);
	}

	private void teleportCharacter(final Player activeChar, final String Cords)
	{
		final GameObject target = activeChar.getTarget();
		Player player = null;
		if(target.isPlayer())
		{
			player = (Player) target;
			if(player.getObjectId() == activeChar.getObjectId())
				player.sendMessage("You cannot teleport your character.");
			else
			{
				final StringTokenizer st = new StringTokenizer(Cords);
				final String x1 = st.nextToken();
				final int x2 = Integer.parseInt(x1);
				final String y1 = st.nextToken();
				final int y2 = Integer.parseInt(y1);
				final String z1 = st.nextToken();
				final int z2 = Integer.parseInt(z1);
				this.teleportCharacter(player, new Location(x2, y2, z2));
			}
			return;
		}
		activeChar.sendPacket(Msg.INCORRECT_TARGET);
	}

	private void teleportCharacter(final Player player, final Location loc)
	{
		if(player != null)
		{
			player.sendMessage("Admin is teleporting you.");
			player.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
			player.teleToLocation(loc);
		}
	}

	private void teleportCharacter_offline(final String _name, final Location loc)
	{
		if(_name == null)
			return;
		Connection con = null;
		PreparedStatement st = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			st = con.prepareStatement("UPDATE characters SET x=?,y=?,z=? WHERE char_name=? LIMIT 1");
			st.setInt(1, loc.x);
			st.setInt(2, loc.y);
			st.setInt(3, loc.z);
			st.setString(4, _name);
			st.executeUpdate();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, st);
		}
	}

	private void teleportToCharacter(final Player activeChar, final GameObject target)
	{
		if(target == null)
			return;
		activeChar.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
		activeChar.teleToLocation(target.getLoc());
		activeChar.sendMessage("You have teleported to " + target);
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
		AdminTeleport._adminCommands = new String[] {
				"admin_show_moves",
				"admin_show_moves_other",
				"admin_show_teleport",
				"admin_teleport_to_character",
				"admin_teleportto",
				"admin_teleport_to",
				"admin_move_to",
				"admin_moveto",
				"admin_teleport",
				"admin_teleport_character",
				"admin_recall",
				"admin_precall",
				"admin_crecall",
				"admin_ccrecall",
				"admin_walk",
				"admin_recall_npc",
				"admin_returntospawn",
				"admin_gonorth",
				"admin_gosouth",
				"admin_goeast",
				"admin_gowest",
				"admin_goup",
				"admin_godown",
				"admin_tele",
				"admin_instant_move",
				"admin_failed",
				"admin_tonpc",
				"admin_todoor",
				"admin_toitem" };
	}
}
