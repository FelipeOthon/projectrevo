package bosses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

import l2s.commons.time.cron.SchedulingPattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.dbcp.DbUtils;
import l2s.commons.util.Rnd;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.utils.Location;

public class EpicBossState
{
	private static final Logger _log;
	private int _bossId;
	private long _respawnDate;
	private State _state;
	private int _aliveId;
	private double _current_hp;
	private double _current_mp;
	private int _locX;
	private int _locY;
	private int _locZ;
	private int _locH;

	public int getBossId()
	{
		return _bossId;
	}

	public void setBossId(final int newId)
	{
		_bossId = newId;
	}

	public State getState()
	{
		return _state;
	}

	public void setState(final State newState)
	{
		_state = newState;
	}

	public long getRespawnDate()
	{
		return _respawnDate;
	}

	public void setRespawnDate(final long respawnDelay, final long respawnDelayRnd, SchedulingPattern cron)
	{
		if(cron != null)
			_respawnDate = cron.next(System.currentTimeMillis());
		else
			_respawnDate = respawnDelay + Rnd.get(0L, respawnDelayRnd) + System.currentTimeMillis();
	}

	public EpicBossState(final int bossId)
	{
		this(bossId, true);
	}

	public EpicBossState(final int bossId, final boolean isDoLoad)
	{
		_bossId = bossId;
		if(isDoLoad)
			load();
	}

	public void load()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM epic_boss_spawn WHERE bossId = ? LIMIT 1");
			statement.setInt(1, _bossId);
			rset = statement.executeQuery();
			if(rset.next())
			{
				_respawnDate = rset.getLong("respawnDate") * 1000L;
				if(_respawnDate - System.currentTimeMillis() <= 0L)
					_state = State.NOTSPAWN;
				else
				{
					final int tempState = rset.getInt("state");
					if(tempState == State.NOTSPAWN.ordinal())
						_state = State.NOTSPAWN;
					else if(tempState == State.INTERVAL.ordinal())
						_state = State.INTERVAL;
					else if(tempState == State.ALIVE.ordinal())
					{
						_state = State.ALIVE;
						_aliveId = rset.getInt("aliveId");
						_current_hp = rset.getDouble("current_hp");
						_current_mp = rset.getDouble("current_mp");
						_locX = rset.getInt("locX");
						_locY = rset.getInt("locY");
						_locZ = rset.getInt("locZ");
						_locH = rset.getInt("locH");
					}
					else if(tempState == State.DEAD.ordinal())
						_state = State.DEAD;
					else
						_state = State.NOTSPAWN;
				}
			}
		}
		catch(Exception e)
		{
			EpicBossState._log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}

	public void save()
	{
		if(_state != State.ALIVE)
			aliveClean();
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("REPLACE INTO epic_boss_spawn (bossId,respawnDate,state,aliveId,current_hp,current_mp,locX,locY,locZ,locH) VALUES(?,?,?,?,?,?,?,?,?,?)");
			statement.setInt(1, _bossId);
			statement.setInt(2, (int) (_respawnDate / 1000L));
			statement.setInt(3, _state.ordinal());
			statement.setInt(4, _aliveId);
			statement.setDouble(5, _current_hp);
			statement.setDouble(6, _current_mp);
			statement.setInt(7, _locX);
			statement.setInt(8, _locY);
			statement.setInt(9, _locZ);
			statement.setInt(10, _locH);
			statement.execute();
			statement.close();
		}
		catch(Exception e)
		{
			EpicBossState._log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public void update()
	{
		if(_state != State.ALIVE)
			aliveClean();
		Connection con = null;
		Statement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.createStatement();
			statement.executeUpdate("UPDATE epic_boss_spawn SET respawnDate=" + _respawnDate / 1000L + ", state=" + _state.ordinal() + ", aliveId=" + _aliveId + ", current_hp='" + _current_hp + "', current_mp='" + _current_mp + "', locX=" + _locX + ", locY=" + _locY + ", locZ=" + _locZ + ", locH=" + _locH + " WHERE bossId=" + _bossId);
			final Date dt = new Date(_respawnDate);
			EpicBossState._log.info("update EpicBossState: ID:" + _bossId + ", RespawnDate:" + dt + ", State:" + _state.toString());
		}
		catch(Exception e)
		{
			EpicBossState._log.error("Exeption on update EpicBossState: ID " + _bossId + ", RespawnDate:" + _respawnDate / 1000L + ", State:" + _state.toString(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}

	public void setNextRespawnDate(final long newRespawnDate)
	{
		_respawnDate = newRespawnDate;
	}

	public long getInterval()
	{
		final long interval = _respawnDate - System.currentTimeMillis();
		return interval > 0L ? interval : 0L;
	}

	public void aliveSave(final int id, final double hp, final double mp, final Location loc)
	{
		_aliveId = id;
		_current_hp = hp;
		_current_mp = mp;
		_locX = loc.x;
		_locY = loc.y;
		_locZ = loc.z;
		_locH = loc.h;
		save();
	}

	public int getAliveId()
	{
		return _aliveId;
	}

	public double getCurrentHp()
	{
		return _current_hp;
	}

	public double getCurrentMp()
	{
		return _current_mp;
	}

	public Location getLoc()
	{
		return new Location(_locX, _locY, _locZ, _locH);
	}

	private void aliveClean()
	{
		_aliveId = 0;
		_current_hp = 0.0;
		_current_mp = 0.0;
		_locX = 0;
		_locY = 0;
		_locZ = 0;
		_locH = 0;
	}

	static
	{
		_log = LoggerFactory.getLogger(EpicBossState.class);
	}

	public enum State
	{
		NOTSPAWN,
		ALIVE,
		DEAD,
		INTERVAL;
	}
}
