package services;

import java.util.List;

import bosses.ValakasManager;
import l2s.gameserver.Config;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.DoorInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.DoorTable;
import l2s.gameserver.utils.Location;

public class TeleToValakas extends Functions implements ScriptFile
{
	private static final int FLOATING_STONE = 7267;
	private static final Location TELEPORT_POSITION1;
	private static final Location TELEPORT_POSITION2;

	public void teleToCorridor()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		final NpcInstance npc = getNpc();
		if(!NpcInstance.canBypassCheck(player, npc) || npc.getNpcId() != 31540)
			return;
		final boolean canenter = player.getVarBoolean("ValakasEnter");
		if(ValakasManager.isEnableEnterToLair() == 1)
		{
			if(getItemCount(player, 7267) > 0L || canenter)
			{
				if(!canenter)
				{
					player.setVar("ValakasEnter", "1");
					removeItem(player, 7267, 1L);
				}
				player.teleToLocation(TeleToValakas.TELEPORT_POSITION1);
			}
			else
				show("<html><body>Klein:<br>You do not have the Floating Stone. Go get one and then come back to me.</body></html>", player);
		}
		else if(ValakasManager.isEnableEnterToLair() == 2)
			show("<html><body>Klein:<br>Valakas is already awake!<br>You may not enter the Lair of Valakas.</body></html>", player);
		else
			show("<html><body>Klein:<br>Valakas is now reborning and there's no way to enter the hall now.</body></html>", player);
	}

	public void teleToValakas()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		final NpcInstance npc = getNpc();
		if(!NpcInstance.canBypassCheck(player, npc) || npc.getNpcId() != 31385)
			return;
		if(Config.VALAKAS_IXION_KILL)
		{
			List<NpcInstance> ixion = GameObjectsStorage.getNpcs(true, 29040);
			if(!ixion.isEmpty())
			{
				if(player.isLangRus())
					show("<html><body>Heart of Volcano:<br>\u041f\u0440\u043e\u0445\u043e\u0434 \u0437\u0430\u043a\u0440\u044b\u0442 \u043f\u043e\u043a\u0430 Ixion \u0434\u044b\u0448\u0438\u0442!</body></html>", player);
				else
					show("<html><body>Heart of Volcano:<br>The passage is closed while Ixion alive!</body></html>", player);
				return;
			}
		}
		if(player.getVarBoolean("ValakasEnter"))
		{
			if(ValakasManager.isEnableEnterToLair() == 1)
			{
				ValakasManager.setValakasSpawnTask();
				player.teleToLocation(TeleToValakas.TELEPORT_POSITION2);
				player.unsetVar("ValakasEnter");
			}
			else if(ValakasManager.isEnableEnterToLair() == 0)
				show("<html><body>Heart of Volcano:<br>Valakas is now reborning and there's no way to enter the hall now.</body></html>", player);
			else
				show("<html><body>Heart of Volcano:<br>Valakas is already awake!<br>You may not enter the Lair of Valakas.</body></html>", player);
		}
		else
			show("<html><body>Heart of Volcano:<br>Conditions are not right to enter to Lair of Valakas.</body></html>", player);
	}

	public void open()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		final NpcInstance npc = getNpc();
		if(!NpcInstance.canBypassCheck(player, npc))
			return;
		if(npc.getNpcId() == 31384)
		{
			if(Config.VALAKAS_WOLVES_KILL)
			{
				List<NpcInstance> kerinne = GameObjectsStorage.getNpcs(true, 29030);
				List<NpcInstance> freki = GameObjectsStorage.getNpcs(true, 29033);
				if(!kerinne.isEmpty() || !freki.isEmpty())
				{
					if(player.isLangRus())
						show("<html><body>Gatekeeper of Valakas:<br>\u0414\u043b\u044f \u043f\u0440\u043e\u0445\u043e\u0434\u0430 \u043d\u0443\u0436\u043d\u043e \u0443\u0431\u0438\u0442\u044c Kerinne \u0438 Freki!</body></html>", player);
					else
						show("<html><body>Gatekeeper of Valakas:<br>If you want to pass, Kerinne and Freki must be dead!</body></html>", player);
					return;
				}
			}
			final DoorInstance door = DoorTable.getInstance().getDoor(24210004);
			if(door != null && !door.isOpen())
			{
				door.openMe();
			}
		}
		else if(npc.getNpcId() == 31686 || npc.getNpcId() == 31687)
		{
			if(Config.VALAKAS_WOLVES_KILL)
			{
				List<NpcInstance> uruz = GameObjectsStorage.getNpcs(true, 29036);
				List<NpcInstance> kinaz = GameObjectsStorage.getNpcs(true, 29037);
				if(!uruz.isEmpty() || !kinaz.isEmpty())
				{
					if(player.isLangRus())
						show("<html><body>Gatekeeper of Valakas:<br>\u0414\u043b\u044f \u043f\u0440\u043e\u0445\u043e\u0434\u0430 \u043d\u0443\u0436\u043d\u043e \u0443\u0431\u0438\u0442\u044c Uruz \u0438 Kinaz!</body></html>", player);
					else
						show("<html><body>Gatekeeper of Valakas:<br>If you want to pass, Uruz and Kinaz must be dead!</body></html>", player);
					return;
				}
			}
			final DoorInstance door = DoorTable.getInstance().getDoor(npc.getNpcId() == 31686 ? 24210005 : 24210006);
			if(door != null && !door.isOpen())
			{
				door.openMe();
			}
		}
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
		TELEPORT_POSITION1 = new Location(183831, -115457, -3296);
		TELEPORT_POSITION2 = new Location(203940, -111840, 66);
	}
}
