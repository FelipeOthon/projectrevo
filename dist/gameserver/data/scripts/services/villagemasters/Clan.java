package services.villagemasters;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.instances.VillageMasterInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;

public class Clan extends Functions implements ScriptFile
{
	@Override
	public void onLoad()
	{
		ScriptFile._log.info("Loaded Service: Villagemasters [Clan Operations]");
	}

	public void CheckCreateClan()
	{
		final Player player = getSelf();
		final NpcInstance npc = getNpc();
		if(player == null || npc == null)
			return;
		String htmltext = "clan-02.htm";
		if(player.getLevel() <= 9)
			htmltext = "clan-06.htm";
		else if(player.isClanLeader())
			htmltext = "clan-07.htm";
		else if(player.getClan() != null)
			htmltext = "clan-09.htm";
		((VillageMasterInstance) npc).showChatWindow(player, "villagemaster/" + htmltext, new Object[0]);
	}

	public void CheckDissolveClan()
	{
		final Player player = getSelf();
		final NpcInstance npc = getNpc();
		if(player == null || npc == null)
			return;
		String htmltext = "clan-01.htm";
		if(player.isClanLeader())
			htmltext = "clan-04.htm";
		else if(player.getClan() != null)
			htmltext = "9000-08.htm";
		else
			htmltext = "9000-11.htm";
		((VillageMasterInstance) npc).showChatWindow(player, "villagemaster/" + htmltext, new Object[0]);
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}
}
