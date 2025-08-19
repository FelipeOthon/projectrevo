package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2scripts.sguard.api.base.IPlayer;
import com.l2scripts.sguard.core.manager.GuardSessionManager;
import com.l2scripts.sguard.core.manager.session.GuardSession;
import com.l2scripts.sguard.core.manager.session.HWID;

import l2s.commons.dbcp.DbUtils;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.GameClient;
import l2s.gameserver.network.authcomm.AuthServerCommunication;
import l2s.gameserver.scripts.ScriptFile;

/**
 * @author Iqman
 * @date 00:42/06.06.2012
 */
public class BotHuntDAO implements ScriptFile
{
	private static final Logger _log = LoggerFactory.getLogger(BotHuntDAO.class);

	public static CopyOnWriteArrayList<String> _known_detections = new CopyOnWriteArrayList<String>();
	public static Map<GuardSession, BotSession> _known_sessions = new HashMap<GuardSession, BotSession>();

	public void loadKnownDetections()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM sguard_auth_log");
			rset = statement.executeQuery();
			while(rset.next())
			{
				String hwid = rset.getString("hwid");
				int detection_id = rset.getInt("detections");
				if(detection_id != 2) //bot
					continue;
				if(_known_detections.contains(hwid)) //already inside
					continue;
				_known_detections.add(hwid);
			}

		}
		catch(Exception e)
		{
			_log.error("", e);
		}
		finally
		{

			DbUtils.closeQuietly(con, statement, rset);
			_log.info("BotHunter: Loaded with " + _known_detections.size() + " known bot detections!");
		}

	}

	public static synchronized GuardSession validateActiveSeasons(Player player)
	{
		String hwid = player.getHWID();
		GuardSession session = GuardSessionManager.getSession(HWID.fromString(hwid));

		if(session == null) //not possible
			return null;

		if(session.getPlayers() == null || session.getPlayers().size() < 2)
			return null;

		return session;
	}

	public static synchronized void proceed(Player actor, GuardSession sguard_session)
	{
		if(sguard_session == null || actor == null)
			return;
		if(!sguard_session.hwid().equals(actor.getHWID())) //not possible
			return;

		BotSession session = _known_sessions.get(sguard_session);
		if(session == null) //new stored session
		{
			session = new BotSession(sguard_session, 0, 0, System.currentTimeMillis()); //session, lastskill launched, points, created on
			session.addLastSkillLaunchedTime(actor);
			if(_known_detections.contains(sguard_session.hwid()))
				session.addPoints(3);
			_known_sessions.put(sguard_session, session);
			return;
		}

		//restart session
		if(session.getCreatedTime() + 900000L < System.currentTimeMillis()) //default 900000L
		{
			_known_sessions.remove(sguard_session);
			session = null;
			proceed(actor, sguard_session);
			return;
		}

		//update if same skill owner launched
		if(session.getLastSkillLaunchedOwner() == actor)
		{
			session.addLastSkillLaunchedTime(actor);
			return;
		}

		//if different check time
		if(System.currentTimeMillis() < session.getLastSkillLaunchedTime() + 700L) //default 700ms
		{
			session.addPoints(1);
			log(actor, session.getPoints());
		}

		session.addLastSkillLaunchedTime(actor);

		if(session.getPoints() < 6)
			return;

		//ban
		ban(sguard_session);
	}

	private static void log(Player actor, int points)
	{
		if(actor == null)
			return;
		//test
		_log.info("Check activated for" + actor.getName() + " total points: " + points);
	}

	private static void ban(GuardSession session)
	{
		if(session == null)
			return;

		for(IPlayer l_pl : session.getPlayers())
		{
			if(l_pl == null)
				continue;
			String account_n = l_pl.getAccountName();
			AuthServerCommunication.getInstance().sendPacket(new ChangeAccessLevel(account_n, -100, -1));
			GameClient client = AuthServerCommunication.getInstance().getAuthedClient(account_n);
			if(client != null)
			{
				Player player = client.getActiveChar();
				if(player != null)
					player.kick(true);
			}
		}
	}

	public static class BotSession
	{
		private final GuardSession _sguard_session;
		private long _last_skill_launched_time;
		private int _points;
		private long _session_creation_time;
		private Player _last_player = null;

		public BotSession(GuardSession sguard_session, long last_skill_launched_time, int points, long session_creation_time)
		{
			_sguard_session = sguard_session;
			_last_skill_launched_time = last_skill_launched_time;
			_points = points;
			_session_creation_time = session_creation_time;
		}

		public GuardSession getGuardSession()
		{
			return _sguard_session;
		}

		public long getLastSkillLaunchedTime()
		{
			return _last_skill_launched_time;
		}

		public int getPoints()
		{
			return _points;
		}

		public long getCreatedTime()
		{
			return _session_creation_time;
		}

		public void addPoints(int points_add)
		{
			_points += points_add;
		}

		public void addLastSkillLaunchedTime(Player player)
		{
			if(player == null)
				return;
			_last_skill_launched_time = System.currentTimeMillis();
			_last_player = player;
		}

		public Player getLastSkillLaunchedOwner()
		{
			return _last_player;
		}
	}

	@Override
	public void onLoad()
	{
		loadKnownDetections();
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

}
