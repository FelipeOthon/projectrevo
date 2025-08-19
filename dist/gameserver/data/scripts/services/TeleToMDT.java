package services;

import org.apache.commons.lang3.ArrayUtils;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;

public class TeleToMDT extends Functions implements ScriptFile
{
	private static int[] npcIds;

	@Override
	public void onLoad()
	{
		ScriptFile._log.info("Loaded Service: Teleport to Race Track");
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public void toMDT()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		final NpcInstance npc = getNpc();
		if(!NpcInstance.canBypassCheck(player, npc) || !ArrayUtils.contains(TeleToMDT.npcIds, npc.getNpcId()))
			return;
		player.setVar("backCoords", player.getX() + " " + player.getY() + " " + player.getZ());
		player.teleToLocation(12661, 181687, -3560);
	}

	public void fromMDT()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		final NpcInstance npc = getNpc();
		if(!NpcInstance.canBypassCheck(player, npc) || npc.getNpcId() != 30995)
			return;
		final String var = player.getVar("backCoords");
		if(var == null || var.equals(""))
		{
			teleOut(player);
			return;
		}
		final String[] coords = var.split(" ");
		if(coords.length < 3)
		{
			teleOut(player);
			return;
		}
		player.teleToLocation(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]));
	}

	private static void teleOut(final Player player)
	{
		player.teleToLocation(12902, 181011, -3563);
		if(!player.isLangRus())
			show("I don't know from where you came here, but I can teleport you the another border side.", player);
		else
			show("\u042f \u043d\u0435 \u0437\u043d\u0430\u044e, \u043a\u0430\u043a \u0412\u044b \u043f\u043e\u043f\u0430\u043b\u0438 \u0441\u044e\u0434\u0430, \u043d\u043e \u044f \u043c\u043e\u0433\u0443 \u0412\u0430\u0441 \u043e\u0442\u043f\u0440\u0430\u0432\u0438\u0442\u044c \u0437\u0430 \u043e\u0433\u0440\u0430\u0436\u0434\u0435\u043d\u0438\u0435.", player);
	}

	static
	{
		TeleToMDT.npcIds = new int[] {
				30059,
				30080,
				30177,
				30233,
				30256,
				30320,
				30848,
				30878,
				30899,
				31210,
				31275,
				31320,
				31964,
				30006,
				30134,
				30146,
				32163,
				30576,
				30540 };
	}
}
