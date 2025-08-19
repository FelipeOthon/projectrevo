package services.villagemasters;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.instances.VillageMasterInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;

public class Ally extends Functions implements ScriptFile
{
	@Override
	public void onLoad()
	{
		ScriptFile._log.info("Loaded Service: Villagemasters [Alliance Operations]");
	}

	public void CheckCreateAlly()
	{
		final Player player = getSelf();
		final NpcInstance npc = getNpc();
		if(player == null || npc == null)
			return;
		String htmltext = "ally-01.htm";
		if(player.isClanLeader())
			htmltext = "ally-02.htm";
		((VillageMasterInstance) npc).showChatWindow(player, "villagemaster/" + htmltext, new Object[0]);
	}

	public void CheckDissolveAlly()
	{
		final Player player = getSelf();
		final NpcInstance npc = getNpc();
		if(player == null || npc == null)
			return;
		String htmltext = "ally-01.htm";
		if(player.isAllyLeader())
			htmltext = "ally-03.htm";
		((VillageMasterInstance) npc).showChatWindow(player, "villagemaster/" + htmltext, new Object[0]);
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}
}
