package services;

import bosses.BaiumManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.Location;

public class TeleToBaium extends Functions implements ScriptFile
{
	private static final int AngelicVortex = 31862;
	private static final int BloodedFabric = 4295;
	private static final Location TELEPORT_POSITION;

	public void teleport_request()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		final NpcInstance npc = getNpc();
		if(!NpcInstance.canBypassCheck(player, npc))
		{
			show("<html><head><body>Angelic Vortex:<br>It's too far from me to work!</body></html>", player);
			return;
		}
		if(npc.getNpcId() != 31862)
		{
			show("<html><head><body><br>Your target must be on Angelic Vortex!</body></html>", player);
			return;
		}
		if(player.getZ() < 10030)
		{
			show("<html><head><body>Angelic Vortex:<br>Invalid height!</body></html>", player);
			return;
		}
		if(getItemCount(player, 4295) > 0L)
		{
			final int state = BaiumManager.isEnableEnterToLair();
			if(state == 1)
			{
				removeItem(player, 4295, 1L);
				player.setVar("BaiumEnter", "1");
				player.teleToLocation(TeleToBaium.TELEPORT_POSITION);
				show("<html><head><body>Angelic Vortex:<br>You are entered!</body></html>", player);
			}
			else if(state == 2)
				show("<html><head><body>Angelic Vortex:<br>Baium is already woken up! You can't enter!</body></html>", player);
			else
				show("<html><head><body>Angelic Vortex:<br>Baium now here is not present!</body></html>", player);
		}
		else
			show("<html><head><body>Angelic Vortex:<br>You do not have enough items!</body></html>", player);
	}

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	static
	{
		TELEPORT_POSITION = new Location(113100, 14500, 10077);
	}
}
