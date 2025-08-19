package events.tournament;

import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.utils.Strings;
import l2s.gameserver.utils.Util;

public class Tournament_register extends Functions
{
	private static boolean active;

	public void bypass_listOfTeams()
	{
		String out = "<table><tr><td width=80>\u041a\u0430\u0442\u0435\u0433\u043e\u0440\u0438\u044f</td><td width=80>\u041d\u0430\u0437\u0432\u0430\u043d\u0438\u0435</td><td width=60>\u0420\u0430\u0437\u043c\u0435\u0440</td></tr>";
		if(Tournament_data.getTeams().isEmpty())
			out += "<tr><td>\u041d\u0435\u0442 \u043a\u043e\u043c\u0430\u043d\u0434</td><td></td><td></td></tr>";
		else
			for(final Team team : Tournament_data.getTeams().values())
			{
				out = out + "<tr><td>" + team.getCategory() + "</td>";
				out = out + "<td><a action=\"bypass -h scripts_events.tournament.Tournament_register:bypass_teamInfo " + team.getId() + "\">" + team.getName() + "</a></td>";
				out = out + "<td>" + team.getCount() + "</td></tr>";
			}
		out += "</table>";
		show(out, getSelf());
	}

	public void bypass_listOfTeamsForRegister()
	{
		final Player player = getSelf();
		final Integer category = Tournament_data.getCategory((int) player.getLevel());
		String out = "\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u043a\u043e\u043c\u0430\u043d\u0434\u0443:<br><table><tr><td width=80>\u041d\u0430\u0437\u0432\u0430\u043d\u0438\u0435</td><td width=60>\u0420\u0430\u0437\u043c\u0435\u0440</td></tr>";
		if(Tournament_data.getTeams().isEmpty())
			out += "<tr><td>\u041d\u0435\u0442 \u043a\u043e\u043c\u0430\u043d\u0434</td><td></td><td></td></tr>";
		else
			for(final Team team : Tournament_data.getTeams().values())
				if(team.getCategory() == category && team.getCount() < 3)
				{
					out += "<tr>";
					out = out + "<td><a action=\"bypass -h scripts_events.tournament.Tournament_register:bypass_teamInfo " + team.getId() + "\">" + team.getName() + "</a></td>";
					out = out + "<td>" + team.getCount() + "</td></tr>";
				}
		out += "</table>";
		show(out, getSelf());
	}

	public static void bypass_tournamentInfo()
	{}

	public void bypass_teamInfo(final String[] var)
	{
		final Player player = getSelf();
		if(var.length != 1)
		{
			show("\u041d\u0435\u043a\u043e\u0440\u0440\u0435\u043a\u0442\u043d\u044b\u0435 \u0434\u0430\u043d\u043d\u044b\u0435.", player);
			return;
		}
		Integer team_id;
		try
		{
			team_id = Integer.valueOf(var[0]);
		}
		catch(Exception e)
		{
			show("\u041d\u0435\u043a\u043e\u0440\u0440\u0435\u043a\u0442\u043d\u044b\u0435 \u0434\u0430\u043d\u043d\u044b\u0435.", player);
			return;
		}
		Team team;
		if(team_id > 0)
		{
			team = Tournament_data.getTeamById(team_id);
			if(team == null)
			{
				show("\u0422\u0430\u043a\u043e\u0439 \u043a\u043e\u043c\u0430\u043d\u0434\u044b \u043d\u0435 \u0441\u0443\u0449\u0435\u0441\u0442\u0432\u0443\u0435\u0442.", player);
				return;
			}
		}
		else
		{
			team = getTeamByPlayer(player);
			if(team == null)
			{
				show("\u0412\u044b \u043d\u0435 \u0437\u0430\u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0438\u0440\u043e\u0432\u0430\u043d\u044b.", player);
				return;
			}
		}
		String out = "\u0418\u043d\u0444\u043e\u0440\u043c\u0430\u0446\u0438\u044f \u043e \u043a\u043e\u043c\u0430\u043d\u0434\u0435:<br>";
		out = out + "<br>\u041d\u0430\u0437\u0432\u0430\u043d\u0438\u0435: " + team.getName();
		out = out + "<br>\u041b\u0438\u0434\u0435\u0440: " + Tournament_data.getPlayerName(team.getLeader());
		out = out + "<br>\u041a\u0430\u0442\u0435\u0433\u043e\u0440\u0438\u044f: " + team.getCategory();
		out += "<br>\u0421\u043e\u0441\u0442\u0430\u0432:<br>";
		out += "<table><tr><td width=80>\u0418\u043c\u044f</td><td width=60>\u0423\u0440\u043e\u0432\u0435\u043d\u044c</td><td width=100>\u041a\u043b\u0430\u0441\u0441</td><td width=60></td></tr>";
		for(final int member : team.getMembers())
		{
			final MemberInfo info = Tournament_data.getMemberInfo(member);
			out = out + "<tr><td>" + info.name + "</td>";
			out = out + "<td>" + info.level + "</td>";
			out = out + "<td>" + ClassId.VALUES[info.class_id].getName(player) + "</td>";
			if(team.getLeader() == player.getObjectId() && player.getObjectId() != member)
				out = out + "<td><a action=\"bypass -h scripts_events.tournament.Tournament_register:bypass_deleteMemberByLeader " + member + "\">\u0412\u044b\u0433\u043d\u0430\u0442\u044c</a></td></tr>";
			else
				out += "<td></td></tr>";
		}
		out += "</table>";
		if(!isRegistered(player))
			out = out + "<br><a action=\"bypass -h scripts_events.tournament.Tournament_register:bypass_register " + team.getId() + "\">\u0417\u0430\u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0438\u0440\u043e\u0432\u0430\u0442\u044c\u0441\u044f</a>";
		else if(team.getLeader() == player.getObjectId())
			out += "<br><a action=\"bypass -h scripts_events.tournament.Tournament_register:bypass_deleteTeam\">\u0423\u0434\u0430\u043b\u0438\u0442\u044c \u043a\u043e\u043c\u0430\u043d\u0434\u0443</a>";
		else
			out += "<br><a action=\"bypass -h scripts_events.tournament.Tournament_register:bypass_leave\">\u0412\u044b\u0439\u0442\u0438 \u0438\u0437 \u043a\u043e\u043c\u0430\u043d\u0434\u044b</a>";
		show(out, player);
	}

	public void bypass_createTeam()
	{
		if(!Tournament_register.active)
		{
			show("\u0418\u0437\u0432\u0438\u043d\u0438\u0442\u0435, \u043d\u043e \u043f\u0435\u0440\u0438\u0443\u0434 \u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0430\u0446\u0438\u0438 \u043e\u043a\u043e\u043d\u0447\u0435\u043d. \u0414\u043e\u0436\u0434\u0438\u0442\u0435\u0441\u044c \u043e\u043a\u043e\u043d\u0447\u0430\u043d\u0438\u044f \u0442\u0443\u0440\u043d\u0438\u0440\u0430.", getSelf());
			return;
		}
		show("scripts/events/tournament/32130-1.html", getSelf());
	}

	public synchronized void bypass_createTeam(final String[] var)
	{
		if(!Tournament_register.active)
		{
			show("\u0418\u0437\u0432\u0438\u043d\u0438\u0442\u0435, \u043d\u043e \u043f\u0435\u0440\u0438\u0443\u0434 \u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0430\u0446\u0438\u0438 \u043e\u043a\u043e\u043d\u0447\u0435\u043d. \u0414\u043e\u0436\u0434\u0438\u0442\u0435\u0441\u044c \u043e\u043a\u043e\u043d\u0447\u0430\u043d\u0438\u044f \u0442\u0443\u0440\u043d\u0438\u0440\u0430.", getSelf());
			return;
		}
		final Player player = getSelf();
		if(var.length != 1)
		{
			show("\u041d\u0435\u043a\u043e\u0440\u0440\u0435\u043a\u0442\u043d\u044b\u0435 \u0434\u0430\u043d\u043d\u044b\u0435.", player);
			return;
		}
		String team_name = var[0];
		if(!Util.isMatchingRegexp(team_name, Config.CLAN_NAME_TEMPLATE))
		{
			show("\u041d\u0435\u043a\u043e\u0440\u0440\u0435\u043a\u0442\u043d\u044b\u0439 \u0432\u0432\u043e\u0434.", player);
			return;
		}
		if(team_name.equals(""))
		{
			show("\u0417\u0430\u043f\u043e\u043b\u043d\u0438\u0442\u0435 \u043f\u043e\u043b\u0435 '\u043d\u0430\u0437\u0432\u0430\u043d\u0438\u0435 \u043a\u043e\u043c\u0430\u043d\u0434\u044b'.", player);
			return;
		}
		team_name = Strings.addSlashes(team_name);
		if(Tournament_data.getTeamByName(team_name) != null)
		{
			show("\u041a\u043e\u043c\u0430\u043d\u0434\u0430 \u0441 \u0442\u0430\u043a\u0438\u043c \u0438\u043c\u0435\u043d\u0435\u043c \u0443\u0436\u0435 \u0441\u0443\u0449\u0435\u0441\u0442\u0432\u0443\u0435\u0442.", player);
			return;
		}
		if(isRegistered(player))
		{
			show("\u0412\u044b \u0443\u0436\u0435 \u0437\u0430\u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0438\u0440\u043e\u0432\u0430\u043d\u044b.", player);
			return;
		}
		if(Tournament_data.createTeam(team_name, player))
			show("\u041a\u043e\u043c\u0430\u043d\u0434\u0430 \u0437\u0430\u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0438\u0440\u043e\u0432\u0430\u043d\u0430. \u0414\u043b\u044f \u0443\u0447\u0430\u0441\u0442\u0438\u044f \u0432 \u0442\u0443\u0440\u043d\u0438\u0440\u0435, \u0432\u0430\u043c \u043f\u043e\u0442\u0440\u0435\u0431\u0443\u0435\u0442\u0441\u044f \u0435\u0449\u0435 2 \u0438\u0433\u0440\u043e\u043a\u0430 \u0432 \u043a\u043e\u043c\u0430\u043d\u0434\u0443.", player);
		else
			show("\u041f\u0440\u043e\u0438\u0437\u043e\u0448\u043b\u0430 \u043e\u0448\u0438\u0431\u043a\u0430. \u041f\u043e\u043f\u0440\u043e\u0431\u0443\u0439\u0442\u0435 \u0435\u0449\u0435 \u0440\u0430\u0437.", player);
	}

	public synchronized void bypass_register(final String[] var)
	{
		if(!Tournament_register.active)
		{
			show("\u0418\u0437\u0432\u0438\u043d\u0438\u0442\u0435, \u043d\u043e \u043f\u0435\u0440\u0438\u0443\u0434 \u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0430\u0446\u0438\u0438 \u043e\u043a\u043e\u043d\u0447\u0435\u043d. \u0414\u043e\u0436\u0434\u0438\u0442\u0435\u0441\u044c \u043e\u043a\u043e\u043d\u0447\u0430\u043d\u0438\u044f \u0442\u0443\u0440\u043d\u0438\u0440\u0430.", getSelf());
			return;
		}
		final Player player = getSelf();
		if(var.length != 1)
		{
			show("\u041d\u0435\u043a\u043e\u0440\u0440\u0435\u043a\u0442\u043d\u044b\u0435 \u0434\u0430\u043d\u043d\u044b\u0435.", player);
			return;
		}
		Integer team_id;
		try
		{
			team_id = Integer.valueOf(var[0]);
		}
		catch(Exception e)
		{
			show("\u041d\u0435\u043a\u043e\u0440\u0440\u0435\u043a\u0442\u043d\u044b\u0435 \u0434\u0430\u043d\u043d\u044b\u0435.", player);
			return;
		}
		final Team team = Tournament_data.getTeamById(team_id);
		if(team == null)
		{
			show("\u0422\u0430\u043a\u043e\u0439 \u043a\u043e\u043c\u0430\u043d\u0434\u044b \u043d\u0435 \u0441\u0443\u0449\u0435\u0441\u0442\u0432\u0443\u0435\u0442.", player);
			return;
		}
		if(team.getCount() > 2)
		{
			show("\u041a\u043e\u043c\u0430\u043d\u0434\u0430 \u043f\u0435\u0440\u0435\u043f\u043e\u043b\u043d\u0435\u043d\u0430.", player);
			return;
		}
		if(isRegistered(player))
		{
			show("\u0412\u044b \u0443\u0436\u0435 \u0437\u0430\u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0438\u0440\u043e\u0432\u0430\u043d\u044b.", player);
			return;
		}
		if(!checkCategory(player, team))
		{
			show("\u0412\u044b \u043d\u0435 \u043f\u043e\u0434\u0445\u043e\u0434\u0438\u0442\u0435 \u043f\u043e \u0443\u0440\u043e\u0432\u043d\u044e.", player);
			return;
		}
		if(!checkType(player, team))
		{
			show("\u0412\u044b \u043d\u0435 \u043f\u043e\u0434\u0445\u043e\u0434\u0438\u0442\u0435 \u043f\u043e \u043f\u0440\u043e\u0444\u0435\u0441\u0441\u0438\u0438.", player);
			return;
		}
		if(Tournament_data.register(player, team))
			show("\u0412\u044b \u0437\u0430\u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0438\u0440\u043e\u0432\u0430\u043b\u0438\u0441\u044c \u0432 \u043a\u043e\u043c\u0430\u043d\u0434\u0435 " + team.getName(), player);
		else
			show("\u041f\u0440\u043e\u0438\u0437\u043e\u0448\u043b\u0430 \u043e\u0448\u0438\u0431\u043a\u0430. \u041f\u043e\u043f\u0440\u043e\u0431\u0443\u0439\u0442\u0435 \u0435\u0449\u0435 \u0440\u0430\u0437.", player);
	}

	public synchronized void bypass_deleteTeam()
	{
		if(!Tournament_register.active)
		{
			show("\u0418\u0437\u0432\u0438\u043d\u0438\u0442\u0435, \u043d\u043e \u043f\u0435\u0440\u0438\u0443\u0434 \u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0430\u0446\u0438\u0438 \u043e\u043a\u043e\u043d\u0447\u0435\u043d. \u0414\u043e\u0436\u0434\u0438\u0442\u0435\u0441\u044c \u043e\u043a\u043e\u043d\u0447\u0430\u043d\u0438\u044f \u0442\u0443\u0440\u043d\u0438\u0440\u0430.", getSelf());
			return;
		}
		final Player player = getSelf();
		final Team team = getTeamByPlayer(player);
		if(team == null)
		{
			show("\u0412\u044b \u043d\u0435 \u0437\u0430\u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0438\u0440\u043e\u0432\u0430\u043d\u044b.", player);
			return;
		}
		if(team.getLeader() != player.getObjectId())
		{
			show("\u0422\u043e\u043b\u044c\u043a\u043e \u043b\u0438\u0434\u0435\u0440 \u043c\u043e\u0436\u0435\u0442 \u0443\u0434\u0430\u043b\u0438\u0442\u044c \u043a\u043e\u043c\u0430\u043d\u0434\u0443.", player);
			return;
		}
		if(Tournament_data.deleteTeam(team.getId()))
			show("\u041a\u043e\u043c\u0430\u043d\u0434\u0430 \u0443\u0434\u0430\u043b\u0435\u043d\u0430.", player);
		else
			show("\u041f\u0440\u043e\u0438\u0437\u043e\u0448\u043b\u0430 \u043e\u0448\u0438\u0431\u043a\u0430. \u041f\u043e\u043f\u0440\u043e\u0431\u0443\u0439\u0442\u0435 \u0435\u0449\u0435 \u0440\u0430\u0437.", player);
	}

	public synchronized void bypass_deleteMemberByLeader(final String[] var)
	{
		if(!Tournament_register.active)
		{
			show("\u0418\u0437\u0432\u0438\u043d\u0438\u0442\u0435, \u043d\u043e \u043f\u0435\u0440\u0438\u0443\u0434 \u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0430\u0446\u0438\u0438 \u043e\u043a\u043e\u043d\u0447\u0435\u043d. \u0414\u043e\u0436\u0434\u0438\u0442\u0435\u0441\u044c \u043e\u043a\u043e\u043d\u0447\u0430\u043d\u0438\u044f \u0442\u0443\u0440\u043d\u0438\u0440\u0430.", getSelf());
			return;
		}
		final Player player = getSelf();
		if(var.length != 1)
		{
			show("\u041d\u0435\u043a\u043e\u0440\u0440\u0435\u043a\u0442\u043d\u044b\u0435 \u0434\u0430\u043d\u043d\u044b\u0435.", player);
			return;
		}
		Integer obj_id;
		try
		{
			obj_id = Integer.valueOf(var[0]);
		}
		catch(Exception e)
		{
			show("\u041d\u0435\u043a\u043e\u0440\u0440\u0435\u043a\u0442\u043d\u044b\u0435 \u0434\u0430\u043d\u043d\u044b\u0435.", player);
			return;
		}
		final Team team = getTeamByPlayer(player);
		if(team == null)
		{
			show("\u0412\u044b \u043d\u0435 \u0437\u0430\u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0438\u0440\u043e\u0432\u0430\u043d\u044b.", player);
			return;
		}
		if(team.getLeader() != player.getObjectId())
		{
			show("\u0412\u044b \u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0435\u0441\u044c \u043b\u0438\u0434\u0435\u0440\u043e\u043c.", player);
			return;
		}
		if(Tournament_data.deleteMember(team.getId(), obj_id))
			show("\u0418\u0433\u0440\u043e\u043a \u0443\u0434\u0430\u043b\u0435\u043d \u0438\u0437 \u043a\u043e\u043c\u0430\u043d\u0434\u044b.", player);
		else
			show("\u041f\u0440\u043e\u0438\u0437\u043e\u0448\u043b\u0430 \u043e\u0448\u0438\u0431\u043a\u0430. \u041f\u043e\u043f\u0440\u043e\u0431\u0443\u0439\u0442\u0435 \u0435\u0449\u0435 \u0440\u0430\u0437.", player);
	}

	public void bypass_leave()
	{
		if(!Tournament_register.active)
		{
			show("\u0418\u0437\u0432\u0438\u043d\u0438\u0442\u0435, \u043d\u043e \u043f\u0435\u0440\u0438\u0443\u0434 \u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0430\u0446\u0438\u0438 \u043e\u043a\u043e\u043d\u0447\u0435\u043d. \u0414\u043e\u0436\u0434\u0438\u0442\u0435\u0441\u044c \u043e\u043a\u043e\u043d\u0447\u0430\u043d\u0438\u044f \u0442\u0443\u0440\u043d\u0438\u0440\u0430.", getSelf());
			return;
		}
		final Player player = getSelf();
		final Team team = getTeamByPlayer(player);
		if(team == null)
		{
			show("\u0412\u044b \u043d\u0435 \u0437\u0430\u0440\u0435\u0433\u0438\u0441\u0442\u0440\u0438\u0440\u043e\u0432\u0430\u043d\u044b.", player);
			return;
		}
		if(Tournament_data.deleteMember(team.getId(), player.getObjectId()))
			show("\u0412\u044b \u0432\u044b\u0448\u043b\u0438 \u0438\u0437 \u043a\u043e\u043c\u0430\u043d\u0434\u044b.", player);
		else
			show("\u041f\u0440\u043e\u0438\u0437\u043e\u0448\u043b\u0430 \u043e\u0448\u0438\u0431\u043a\u0430. \u041f\u043e\u043f\u0440\u043e\u0431\u0443\u0439\u0442\u0435 \u0435\u0449\u0435 \u0440\u0430\u0437.", player);
	}

	public static Boolean isRegistered(final Player player)
	{
		for(final Team team : Tournament_data.getTeams().values())
			for(final int obj_id : team.getMembers())
				if(player.getObjectId() == obj_id)
					return true;
		return false;
	}

	public static Team getTeamByPlayer(final Player player)
	{
		for(final Team team : Tournament_data.getTeams().values())
			for(final int obj_id : team.getMembers())
				if(player.getObjectId() == obj_id)
					return team;
		return null;
	}

	public static boolean checkCategory(final Player player, final Team team)
	{
		final Integer category = Tournament_data.getCategory((int) player.getLevel());
		return team.getCategory() == category;
	}

	public static boolean checkType(final Player player, final Team team)
	{
		final Integer type = Tournament_data.getPlayerType(player.getObjectId());
		for(final int obj_id : team.getMembers())
			if(type == Tournament_data.getPlayerType(obj_id))
				return false;
		return true;
	}

	public static void endRegistration()
	{
		Tournament_register.active = false;
	}

	public static void endTournament()
	{
		Tournament_register.active = true;
	}

	static
	{
		Tournament_register.active = true;
	}
}
