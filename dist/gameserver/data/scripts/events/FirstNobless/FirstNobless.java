package events.FirstNobless;

import l2s.gameserver.Announcements;
import l2s.gameserver.Config;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.model.Player;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;

public class FirstNobless extends Functions implements ScriptFile
{
	private static boolean ENABLED;

	public static void reward(final Player player)
	{
		if(FirstNobless.ENABLED)
		{
			FirstNobless.ENABLED = false;
			ServerVariables.set("FirstNobless", 1);
			for(int i = 0; i < Config.FIRST_NOBLESS_REWARD.length; i += 2)
				player.getInventory().addItem(Config.FIRST_NOBLESS_REWARD[i], Config.FIRST_NOBLESS_REWARD[i + 1]);
			final String[] param = { player.getName() };
			Announcements.getInstance().announceByCustomMessage("scripts.events.FirstNobless", param);
			ScriptFile._log.info("First Nobless: " + param[0] + " get reward!");
		}
	}

	@Override
	public void onLoad()
	{
		if(Config.FIRST_NOBLESS)
		{
			FirstNobless.ENABLED = ServerVariables.getInt("FirstNobless", 0) < 1;
			ScriptFile._log.info("Loaded Event: FirstNobless");
		}
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	static
	{
		FirstNobless.ENABLED = false;
	}
}
