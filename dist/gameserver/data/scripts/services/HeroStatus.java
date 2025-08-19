package services;

import l2s.gameserver.Config;
import l2s.gameserver.cache.Msg;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.Hero;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.SkillList;
import l2s.gameserver.network.l2.s2c.SocialAction;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.ItemTable;
import l2s.gameserver.utils.Log;

public class HeroStatus extends Functions implements ScriptFile
{
	public void listHS()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(!Config.SERVICES_HERO_STATUS_ENABLE)
		{
			if(player.isLangRus())
				player.sendMessage("Сервис отключен.");
			else
				player.sendMessage("Service disabled.");
			return;
		}
		final String text = player.isLangRus() ? "Команда <font color=\"LEVEL\">.herostatus</font> показывает дату окончания" : "Command <font color=\"LEVEL\">.herostatus</font> show end time";
		String html = "<title>Hero Status</title><center><br>" + text + "<br><br><br>";
		int n = 0;
		for(int i = 0; i < Config.SERVICES_HERO_STATUS_PRICE.length; i += 3)
		{
			++n;
			html = html + "<a action=\"bypass -h scripts_services.HeroStatus:getHS " + n + "\">" + Config.SERVICES_HERO_STATUS_PRICE[i + 2] + " days - " + Config.SERVICES_HERO_STATUS_PRICE[i + 1] + " " + ItemTable.getInstance().getTemplate(Config.SERVICES_HERO_STATUS_PRICE[i]).getName() + "</a><br>";
		}
		show(html, player);
	}

	public void getHS(final String[] param)
	{
		if(param.length < 1)
			return;
		final Player player = getSelf();
		if(player == null)
			return;
		if(!Config.SERVICES_HERO_STATUS_ENABLE)
		{
			if(player.isLangRus())
				player.sendMessage("Сервис отключен.");
			else
				player.sendMessage("Service disabled.");
			return;
		}
		if(player.isInOlympiadMode())
		{
			if(player.isLangRus())
				player.sendMessage("Вы находитесь в Олимпиаде.");
			else
				player.sendMessage("You are in Olympiad.");
			return;
		}
		if(player.isSubClassActive())
		{
			if(!player.isLangRus())
				player.sendMessage("Only for base class!");
			else
				player.sendMessage("Только базовым классом!");
			return;
		}
		if(player.getLevel() <= 75)
		{
			if(!player.isLangRus())
				player.sendMessage("Only for 76+ level!");
			else
				player.sendMessage("Доступно только с 76-го уровня!");
			return;
		}
		if(player.getClassId().getLevel() != 4)
		{
			if(!player.isLangRus())
				player.sendMessage("Need 3rd profession!");
			else
				player.sendMessage("Необходимо 3-ю профессию!");
			return;
		}
		int i = 0;
		try
		{
			i = Integer.parseInt(param[0]);
		}
		catch(Exception e)
		{
			i = 1;
		}
		i = Math.min(Math.max(i * 3, 1), Config.SERVICES_HERO_STATUS_PRICE.length);
		final int id = Config.SERVICES_HERO_STATUS_PRICE[i - 3];
		final int count = Config.SERVICES_HERO_STATUS_PRICE[i - 2];
		final long days = Config.SERVICES_HERO_STATUS_PRICE[i - 1];
		if(deleteItem(player, id, count))
		{
			final String v = player.getVar("HeroStatus");
			if(v != null && Long.parseLong(v) > System.currentTimeMillis())
			{
				player.setVar("HeroStatus", String.valueOf(Long.parseLong(v) + 86400000L * days));
				if(player.isLangRus())
					player.sendMessage("Вы успешно продлили статус Героя на " + days + " дн.");
				else
					player.sendMessage("You have successfully extended Hero status on " + days + " days.");
				Log.addLog(player.toString() + " extended Hero status on " + days + " days.", "services");
			}
			else
			{
				player.setVar("HeroStatus", String.valueOf(System.currentTimeMillis() + 86400000L * days));
				if(player.isLangRus())
					player.sendMessage("Вы успешно приобрели статус Героя на " + days + " дн.");
				else
					player.sendMessage("You have successfully acquired Hero status on " + days + " days.");
				Log.addLog(player.toString() + " acquired Hero status on " + days + " days.", "services");
				if(!player.isHero())
				{
					player.setHero(true);
					Hero.addSkills(player);
					player.updatePledgeClass();
					player.sendPacket(new SkillList(player));
					player.broadcastPacket(new L2GameServerPacket[] { new SocialAction(player.getObjectId(), 16) });
					player.broadcastUserInfo(true);
				}
			}
			return;
		}
		if(id == 57)
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
		else
			player.sendPacket(Msg.INCORRECT_ITEM_COUNT);
	}

	@Override
	public void onLoad()
	{
		ScriptFile._log.info("Loaded Service: Hero Status");
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}
}
