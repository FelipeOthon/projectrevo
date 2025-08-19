package services;

import l2s.gameserver.Config;
import l2s.gameserver.cache.Msg;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;

public class ManaRegen extends Functions implements ScriptFile
{
	private static final int ADENA = 57;
	private static final long PRICE = 5L;

	public void DoManaRegen()
	{
		if(!Config.SERVICES_GIRAN_HARBOR_ENABLED)
			return;
		final Player player = getSelf();
		if(player == null)
			return;
		final NpcInstance npc = getNpc();
		if(!NpcInstance.canBypassCheck(player, npc) || npc.getNpcId() != 30878)
			return;
		final long mp = (long) Math.floor(player.getMaxMp() - player.getCurrentMp());
		final long fullCost = mp * 5L;
		if(fullCost <= 0L)
		{
			player.sendPacket(Msg.NOTHING_HAPPENED);
			return;
		}
		if(getItemCount(player, 57) >= fullCost)
		{
			removeItem(player, 57, fullCost);
			player.sendPacket(new SystemMessage(1068).addNumber(mp));
			player.setCurrentMp(player.getMaxMp());
		}
		else
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
	}

	@Override
	public void onLoad()
	{
		ScriptFile._log.info("Loaded Service: Mana Regen");
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}
}
