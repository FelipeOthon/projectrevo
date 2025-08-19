package services;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.DoorInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.DoorTable;

public class BorderOutpostDoormans extends Functions implements ScriptFile
{
	private static int DoorId;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public void openDoor()
	{
		final Player player = getSelf();
		if(player == null || !NpcInstance.canBypassCheck(player, player.getLastNpc()))
			return;
		final DoorInstance door = DoorTable.getInstance().getDoor(BorderOutpostDoormans.DoorId);
		door.openMe();
	}

	public void closeDoor()
	{
		final Player player = getSelf();
		if(player == null || !NpcInstance.canBypassCheck(player, player.getLastNpc()))
			return;
		final DoorInstance door = DoorTable.getInstance().getDoor(BorderOutpostDoormans.DoorId);
		door.closeMe();
	}

	static
	{
		BorderOutpostDoormans.DoorId = 24170001;
	}
}
