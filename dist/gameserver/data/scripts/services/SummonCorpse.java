package services;

import java.util.List;

import l2s.commons.util.Rnd;
import l2s.gameserver.cache.Msg;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.Location;

public class SummonCorpse extends Functions implements ScriptFile
{
	private static int SUMMON_PRICE;

	@Override
	public void onLoad()
	{
		ScriptFile._log.info("Loaded Service: Summon a corpse");
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public void doSummon()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		final NpcInstance npc = getNpc();
		if(!NpcInstance.canBypassCheck(player, npc) || npc.getNpcId() != 32104)
			return;
		final String fail = HtmCache.getInstance().getHtml("default/32104-fail.htm", player);
		final String success = HtmCache.getInstance().getHtml("default/32104-success.htm", player);
		if(!player.isInParty())
		{
			show(fail, player, npc);
			return;
		}
		int counter = 0;
		final List<Player> partyMembers = player.getParty().getPartyMembers();
		for(final Player partyMember : partyMembers)
			if(partyMember != null && partyMember.isDead())
			{
				++counter;
				if(player.getAdena() < SummonCorpse.SUMMON_PRICE)
				{
					player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
					return;
				}
				player.reduceAdena(SummonCorpse.SUMMON_PRICE, true);
				final Location coords = new Location(11255 + Rnd.get(-20, 20), -23370 + Rnd.get(-20, 20), -3649);
				partyMember.summonCharacterRequest(player, coords, 0);
			}
		if(counter == 0)
			show(fail, player, npc);
		else
			show(success, player, npc);
	}

	static
	{
		SummonCorpse.SUMMON_PRICE = 200000;
	}
}
