package events.Christmas;

import java.util.Calendar;

import l2s.gameserver.Announcements;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.SkillTable;

public class NewYearTimer implements ScriptFile
{
	private static NewYearTimer instance;

	public static NewYearTimer getInstance()
	{
		if(NewYearTimer.instance == null)
			new NewYearTimer();
		return NewYearTimer.instance;
	}

	public NewYearTimer()
	{
		if(NewYearTimer.instance != null)
			return;
		NewYearTimer.instance = this;
		if(!isActive())
			return;
		final Calendar c = Calendar.getInstance();
		c.set(1, 2008);
		c.set(2, 0);
		c.set(5, 1);
		c.set(10, 0);
		c.set(12, 0);
		c.set(13, 0);
		c.set(14, 0);
		while(getDelay(c) < 0L)
			c.set(1, c.get(1) + 1);
		ThreadPoolManager.getInstance().schedule(new NewYearAnnouncer("\u0421 \u041d\u043e\u0432\u044b\u043c, " + c.get(1) + ", \u0413\u043e\u0434\u043e\u043c!!!"), getDelay(c));
		c.add(13, -1);
		ThreadPoolManager.getInstance().schedule(new NewYearAnnouncer("1"), getDelay(c));
		c.add(13, -1);
		ThreadPoolManager.getInstance().schedule(new NewYearAnnouncer("2"), getDelay(c));
		c.add(13, -1);
		ThreadPoolManager.getInstance().schedule(new NewYearAnnouncer("3"), getDelay(c));
		c.add(13, -1);
		ThreadPoolManager.getInstance().schedule(new NewYearAnnouncer("4"), getDelay(c));
		c.add(13, -1);
		ThreadPoolManager.getInstance().schedule(new NewYearAnnouncer("5"), getDelay(c));
	}

	private long getDelay(final Calendar c)
	{
		return c.getTime().getTime() - System.currentTimeMillis();
	}

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	private static boolean isActive()
	{
		return ServerVariables.getString("Christmas", "off").equalsIgnoreCase("on");
	}

	@Override
	public void onShutdown()
	{}

	private class NewYearAnnouncer implements Runnable
	{
		private final String message;

		private NewYearAnnouncer(final String message)
		{
			this.message = message;
		}

		@Override
		public void run()
		{
			Announcements.getInstance().announceToAll(message);
			if(message.length() == 1)
				return;
			for(final Player player : GameObjectsStorage.getPlayers())
				if(player != null)
				{
					final Skill skill = SkillTable.getInstance().getInfo(3266, 1);
					final MagicSkillUse msu = new MagicSkillUse(player, player, 3266, 1, skill.getHitTime(), 0L);
					player.broadcastPacket(new L2GameServerPacket[] { msu });
				}
			NewYearTimer.instance = null;
			new NewYearTimer();
		}
	}
}
