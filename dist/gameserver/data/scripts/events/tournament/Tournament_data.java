package events.tournament;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import l2s.commons.dbcp.DbUtils;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.database.mysql;
import l2s.gameserver.idfactory.IdFactory;
import l2s.gameserver.model.Player;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;

public class Tournament_data extends Functions implements ScriptFile
{
	private static Map<Integer, Team> teams;

	public static Team getTeamById(final Integer id)
	{
		return Tournament_data.teams.get(id);
	}

	public static Team getTeamByName(final String name)
	{
		for(final Team team : Tournament_data.teams.values())
			if(name.equalsIgnoreCase(team.getName()))
				return team;
		return null;
	}

	public static Map<Integer, Team> getTeams()
	{
		return Tournament_data.teams;
	}

	@Override
	public void onLoad()
	{
		loadTeams();
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public static void loadTeams()
	{
		Tournament_data.teams = new HashMap<Integer, Team>();
		Connection con = null;
		PreparedStatement offline = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			offline = con.prepareStatement("SELECT * FROM tournament_teams");
			rs = offline.executeQuery();
			while(rs.next())
			{
				final Integer team_id = rs.getInt("team_id");
				Team team = Tournament_data.teams.get(team_id);
				if(team == null)
				{
					team = new Team();
					Tournament_data.teams.put(team_id, team);
				}
				team.setName(rs.getString("team_name"));
				team.setId(team_id);
				team.setCategory(rs.getInt("category"));
				final Integer leader = rs.getInt("leader");
				if(leader == 1)
					team.setLeader(rs.getInt("obj_id"));
				else
					team.addMember(rs.getInt("obj_id"));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, offline, rs);
		}
	}

	public static Boolean createTeam(final String name, final Player leader)
	{
		final Team team = new Team();
		team.setName(name);
		team.setId(IdFactory.getInstance().getNextId());
		team.setLeader(leader.getObjectId());
		team.setCategory(getCategory((int) leader.getLevel()));
		final Integer type = getPlayerType(leader.getObjectId());
		Connection con = null;
		PreparedStatement insertion = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			insertion = con.prepareStatement("INSERT INTO tournament_teams (obj_id, type, team_id, team_name, leader, category) VALUES (?,?,?,?,1,?) ");
			insertion.setInt(1, leader.getObjectId());
			insertion.setInt(2, type);
			insertion.setInt(3, team.getId());
			insertion.setString(4, name);
			insertion.setInt(5, team.getCategory());
			insertion.executeUpdate();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		finally
		{
			DbUtils.closeQuietly(con, insertion);
		}
		Tournament_data.teams.put(team.getId(), team);
		return true;
	}

	public static boolean register(final Player player, final Team team)
	{
		Connection con = null;
		PreparedStatement insertion = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			insertion = con.prepareStatement("INSERT INTO tournament_teams (obj_id, type, team_id, team_name, leader, category) VALUES (?,?,?,?,0,?) ");
			insertion.setInt(1, player.getObjectId());
			insertion.setInt(2, getPlayerType(player.getObjectId()));
			insertion.setInt(3, team.getId());
			insertion.setString(4, team.getName());
			insertion.setInt(5, team.getCategory());
			insertion.executeUpdate();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		finally
		{
			DbUtils.closeQuietly(con, insertion);
		}
		team.addMember(player.getObjectId());
		return true;
	}

	public static Boolean deleteTeam(final Integer team_id)
	{
		Tournament_data.teams.remove(team_id);
		return mysql.set("DELETE FROM tournament_teams WHERE team_id = " + team_id);
	}

	public static Boolean deleteMember(final Integer team_id, final Integer obj_id)
	{
		Tournament_data.teams.get(team_id).removeMember(obj_id);
		return mysql.set("DELETE FROM tournament_teams WHERE obj_id = " + obj_id);
	}

	public static MemberInfo getMemberInfo(final Integer obj_id)
	{
		final MemberInfo info = new MemberInfo();
		String sql = "SELECT A.char_name, A.online, B.level, B.class_id, C.ClassName FROM characters AS A ";
		sql += "LEFT JOIN character_subclasses AS B ON (A.obj_Id = B.char_obj_id) ";
		sql += "WHERE B.active = 1 AND obj_id = ?";
		Connection con = null;
		PreparedStatement offline = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			offline = con.prepareStatement(sql);
			offline.setInt(1, obj_id);
			rs = offline.executeQuery();
			if(rs.next())
			{
				if(rs.getInt("online") == 1)
					info.online = true;
				info.name = rs.getString("char_name");
				info.level = rs.getInt("level");
				info.class_id = rs.getInt("class_id");
				info.category = getCategory(info.level);
				return info;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, offline, rs);
		}
		return null;
	}

	public static Integer getPlayerType(final Integer obj_id)
	{
		Connection con = null;
		PreparedStatement offline = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			offline = con.prepareStatement("SELECT B.type FROM character_subclasses AS A LEFT JOIN tournament_class_list AS B ON (A.class_id = B.class_id) WHERE A.active = 1 AND A.char_obj_id = ?");
			offline.setInt(1, obj_id);
			rs = offline.executeQuery();
			if(rs.next())
				return rs.getInt("type");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, offline, rs);
		}
		return null;
	}

	public static String getPlayerName(final Integer obj_id)
	{
		Connection con = null;
		PreparedStatement offline = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			offline = con.prepareStatement("SELECT char_name FROM characters WHERE obj_Id = ?");
			offline.setInt(1, obj_id);
			rs = offline.executeQuery();
			if(rs.next())
				return rs.getString("char_name");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, offline, rs);
		}
		return null;
	}

	public static Integer getCategory(final Integer level)
	{
		if(level >= 20 && level <= 29)
			return 1;
		if(level >= 30 && level <= 39)
			return 2;
		if(level >= 40 && level <= 51)
			return 3;
		if(level >= 52 && level <= 61)
			return 4;
		if(level >= 62 && level <= 75)
			return 5;
		if(level >= 76)
			return 6;
		return 0;
	}

	public static boolean createTournamentTable(final int category)
	{
		mysql.set("DELETE FROM tournament_table");
		Connection con1 = null;
		Connection con2 = null;
		PreparedStatement statement1 = null;
		PreparedStatement statement2 = null;
		ResultSet rs = null;
		try
		{
			con1 = DatabaseFactory.getInstance().getConnection();
			con2 = DatabaseFactory.getInstance().getConnection();
			statement1 = con1.prepareStatement("select * from tournament_teams where leader = 1 and category = ? and status = 1");
			statement1.setInt(1, category);
			rs = statement1.executeQuery();
			int i = 0;
			while(rs.next())
				++i;
			rs.beforeFirst();
			if(i == 0)
				return true;
			if(i == 1)
			{
				rs.next();
				final int team_id = rs.getInt("team_id");
				final Team team = Tournament_data.teams.get(team_id);
				Tournament_battle.announce("\u041a\u043e\u043c\u0430\u043d\u0434\u0430 " + team.getName() + " \u0432\u044b\u0438\u0433\u0440\u0430\u043b\u0430 \u0442\u0443\u0440\u043d\u0438\u0440 \u0432 \u043a\u0430\u0442\u0435\u0433\u043e\u0440\u0438\u0438 " + category);
				Tournament_battle.giveItemsToWinner(team);
				Tournament_battle.endTournament();
				return true;
			}
			while(rs.next())
			{
				statement2 = con2.prepareStatement("insert into tournament_table (category, team1id, team1name, team2id, team2name) VALUES (?,?,?,?,?)");
				statement2.setInt(1, category);
				statement2.setInt(2, rs.getInt("team_id"));
				statement2.setString(3, rs.getString("team_name"));
				if(rs.next())
				{
					statement2.setInt(4, rs.getInt("team_id"));
					statement2.setString(5, rs.getString("team_name"));
				}
				else
				{
					statement2.setInt(4, 0);
					statement2.setString(5, "");
				}
				statement2.executeUpdate();
				DbUtils.close(statement2);
			}
			return false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Tournament_battle.announce("\u041f\u0440\u043e\u0438\u0437\u043e\u0448\u043b\u0430 \u043e\u0448\u0438\u0431\u043a\u0430, \u0442\u0443\u0440\u043d\u0438\u0440 \u0437\u0430\u0432\u0435\u0440\u0448\u0435\u043d.");
			return true;
		}
		finally
		{
			DbUtils.closeQuietly(con1, statement1, rs);
			DbUtils.closeQuietly(con2);
		}
	}

	public static boolean fillNextTeams(final int category)
	{
		Connection con = null;
		PreparedStatement offline = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			offline = con.prepareStatement("SELECT * FROM tournament_table WHERE category = ?");
			offline.setInt(1, category);
			rs = offline.executeQuery();
			while(rs.next())
			{
				final Integer team1id = rs.getInt("team1id");
				final Integer team2id = rs.getInt("team2id");
				if(team2id == 0)
				{
					teamWin(team1id);
					removeRecordFromTournamentTable(team1id);
				}
				else
				{
					final Team team1 = Tournament_data.teams.get(team1id);
					final Team team2 = Tournament_data.teams.get(team2id);
					if(team1 == null)
						continue;
					if(team2 == null)
					{
						teamWin(team1id);
						removeRecordFromTournamentTable(team1id);
					}
					else if(team1.getOnlineCount() == 0 && team2.getOnlineCount() > 0)
					{
						disqualifyTeam(team1id);
						teamWin(team2id);
						removeRecordFromTournamentTable(team1id);
					}
					else if(team1.getOnlineCount() > 0 && team2.getOnlineCount() == 0)
					{
						disqualifyTeam(team2id);
						teamWin(team1id);
						removeRecordFromTournamentTable(team1id);
					}
					else
					{
						if(team1.getOnlineCount() <= 0 || team2.getOnlineCount() != 0)
						{
							Tournament_battle.team1 = team1;
							Tournament_battle.team2 = team2;
							return true;
						}
						disqualifyTeam(team1id);
						disqualifyTeam(team2id);
						removeRecordFromTournamentTable(team1id);
					}
				}
			}
			return false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, offline, rs);
		}
		return false;
	}

	public static void removeRecordFromTournamentTable(final int team1id)
	{
		mysql.set("DELETE FROM tournament_table WHERE team1id = " + team1id);
	}

	public static void disqualifyTeam(final int teamId)
	{
		if(teamId > 0)
		{
			mysql.set("UPDATE tournament_teams SET status = 0 WHERE team_id = " + teamId);
			mysql.set("UPDATE tournament_teams SET losts = losts + 1 WHERE team_id = " + teamId);
			final Team team = Tournament_data.teams.get(teamId);
			if(team != null)
				Tournament_battle.announce("\u041a\u043e\u043c\u0430\u043d\u0434\u0430 " + team.getName() + " \u0434\u0438\u0441\u043a\u0432\u0430\u043b\u0438\u0444\u0438\u0446\u0438\u0440\u043e\u0432\u0430\u043d\u0430.");
		}
	}

	public static void teamWin(final int teamId)
	{
		if(teamId > 0)
		{
			mysql.set("UPDATE tournament_teams SET wins = wins + 1 WHERE team_id = " + teamId);
			final Team team = Tournament_data.teams.get(teamId);
			if(team != null)
				Tournament_battle.announce("\u041a\u043e\u043c\u0430\u043d\u0434\u0430 " + team.getName() + " \u0432\u044b\u0438\u0433\u0440\u0430\u043b\u0430 \u0431\u043e\u0439.");
		}
	}

	public static void teamLost(final int teamId)
	{
		if(teamId > 0)
		{
			mysql.set("UPDATE tournament_teams SET status = 0 WHERE team_id = " + teamId);
			mysql.set("UPDATE tournament_teams SET losts = losts + 1 WHERE team_id = " + teamId);
		}
	}

	static
	{
		Tournament_data.teams = new HashMap<Integer, Team>();
	}
}
