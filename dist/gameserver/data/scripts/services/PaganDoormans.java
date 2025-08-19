package services;

import l2s.gameserver.cache.Msg;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.DoorInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.DoorTable;

public class PaganDoormans extends Functions implements ScriptFile
{
	private static final int MainDoorId = 19160001;
	private static final int SecondDoor1Id = 19160011;
	private static final int SecondDoor2Id = 19160010;
	public final int VISITOR_MARK = 8064;
	public final int FADED_MARK = 8065;
	public final int PERMANENT_MARK = 8067;

	@Override
	public void onLoad()
	{
		ScriptFile._log.info("Loaded Service: Pagan Doormans");
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public void openMainDoor()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		final NpcInstance npc = getNpc();
		if(!NpcInstance.canBypassCheck(player, npc) || npc.getNpcId() != 32034)
			return;
		final long visitorMarkCount = getItemCount(player, 8064);
		if(visitorMarkCount == 0L && getItemCount(player, 8067) == 0L && getItemCount(player, 8065) == 0L)
		{
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
			return;
		}
		if(visitorMarkCount > 0L)
		{
			removeItem(player, 8064, visitorMarkCount);
			addItem(player, 8065, 1L);
		}
		openDoor(19160001);
		show(HtmCache.getInstance().getHtml("default/32034-1.htm", player), player, npc);
	}

	public void openSecondDoor()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		final NpcInstance npc = getNpc();
		if(!NpcInstance.canBypassCheck(player, npc) || npc.getNpcId() != 32036)
			return;
		if(getItemCount(player, 8067) == 0L)
		{
			show(HtmCache.getInstance().getHtml("default/32036-2.htm", player), player, npc);
			return;
		}
		openDoor(19160011);
		openDoor(19160010);
		show(HtmCache.getInstance().getHtml("default/32036-1.htm", player), player, npc);
	}

	public void pressSkull()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		final NpcInstance npc = getNpc();
		if(!NpcInstance.canBypassCheck(player, npc) || npc.getNpcId() != 32035)
			return;
		openDoor(19160001);
		show(HtmCache.getInstance().getHtml("default/32035-1.htm", player), player, npc);
	}

	public void press2ndSkull()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		final NpcInstance npc = getNpc();
		if(!NpcInstance.canBypassCheck(player, npc) || npc.getNpcId() != 32037)
			return;
		openDoor(19160011);
		openDoor(19160010);
		show(HtmCache.getInstance().getHtml("default/32037-1.htm", player), player, npc);
	}

	private static void openDoor(final int doorId)
	{
		final int CLOSE_TIME = 10000;
		final DoorInstance door = DoorTable.getInstance().getDoor(doorId);
		if(!door.isOpen())
		{
			door.openMe();
		}
	}
}
