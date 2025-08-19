package services;

import l2s.gameserver.cache.Msg;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.SkillTable;

public class RemoveDeathPenalty extends Functions implements ScriptFile
{
	public void showdialog()
	{
		final Player player = getSelf();
		String htmltext;
		if(player.getDeathPenalty().getLevel() > 0)
		{
			htmltext = HtmCache.getInstance().getHtml("scripts/services/RemoveDeathPenalty-1.htm", player);
			htmltext = htmltext + "<a action=\"bypass -h scripts_services.RemoveDeathPenalty:remove\">Remove 1 level of Death Penalty (" + getPrice() + " adena).</a>";
		}
		else
			htmltext = HtmCache.getInstance().getHtml("scripts/services/RemoveDeathPenalty-0.htm", player);
		show(htmltext, getSelf());
	}

	public void remove()
	{
		final NpcInstance npc = getNpc();
		if(npc == null)
			return;
		final Player player = getSelf();
		if(player.getDeathPenalty().getLevel() > 0)
		{
			if(player.getAdena() >= getPrice())
			{
				player.reduceAdena(getPrice(), true);
				npc.doCast(SkillTable.getInstance().getInfo(5077, 1), player, false);
			}
			else
				player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
		}
		else
			show(HtmCache.getInstance().getHtml("scripts/services/RemoveDeathPenalty-0.htm", player), player);
	}

	public int getPrice()
	{
		final byte playerLvl = getSelf().getLevel();
		if(playerLvl <= 19)
			return 3600;
		if(playerLvl >= 20 && playerLvl <= 39)
			return 16400;
		if(playerLvl >= 40 && playerLvl <= 51)
			return 36200;
		if(playerLvl >= 52 && playerLvl <= 60)
			return 50400;
		if(playerLvl >= 61 && playerLvl <= 75)
			return 78200;
		return 102800;
	}

	@Override
	public void onLoad()
	{
		ScriptFile._log.info("Loaded Service: NPC RemoveDeathPenalty");
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}
}
