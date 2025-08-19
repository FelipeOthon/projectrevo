package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import l2s.commons.dbcp.DbUtils;
import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.cache.Msg;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.database.mysql;
import l2s.gameserver.instancemanager.PlayerManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Servitor;
import l2s.gameserver.model.SubClass;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.model.entity.Hero;
import l2s.gameserver.model.entity.events.impl.SiegeEvent;
import l2s.gameserver.model.entity.olympiad.Olympiad;
import l2s.gameserver.model.instances.HitmanInstance;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.instances.PetInstance;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.l2.s2c.PartySmallWindowUpdate;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.CharTemplateTable;
import l2s.gameserver.tables.ItemTable;
import l2s.gameserver.utils.Log;
import l2s.gameserver.utils.Util;

public class Rename extends Functions implements ScriptFile
{
	public void rename_page()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(!Config.SERVICES_CHANGE_NICK_ENABLED)
		{
			if(player.isLangRus())
				player.sendMessage("Сервис отключен.");
			else
				player.sendMessage("Service disabled.");
			return;
		}
		String append = "<br>";
		append = append + "<font color=\"LEVEL\">" + new CustomMessage("scripts.services.Rename.RenameFor").addString(Util.formatAdena(Config.SERVICES_CHANGE_NICK_PRICE)).addItemName(Config.SERVICES_CHANGE_NICK_ITEM).toString(player) + "</font>";
		if(!Config.SERVICES_CHANGE_NICK_SYMBOLS.isEmpty())
			append = append + "<br>" + (player.isLangRus() ? "Допустимые символы" : "Valid symbols") + ": " + Config.SERVICES_CHANGE_NICK_SYMBOLS;
		append += "<table>";
		append = append + "<tr><td>" + new CustomMessage("scripts.services.Rename.NewName").toString(player) + " <edit var=\"new_name\" width=120></td></tr>";
		append += "<tr><td></td></tr>";
		append = append + "<tr><td><button value=\"" + new CustomMessage("scripts.services.Rename.RenameButton").toString(player) + "\" action=\"bypass -h scripts_services.Rename:rename $new_name\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>";
		append += "</table>";
		show(append, player);
	}

	public void rename(final String[] args)
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(args.length != 1)
		{
			show(new CustomMessage("scripts.services.Rename.incorrectinput"), player);
			return;
		}
		if(Config.ENABLE_OLYMPIAD && (Olympiad.isRegistered(player) || player.getOlympiadGameId() > -1 || player.isInOlympiadMode()))
		{
			player.sendMessage(player.isLangRus() ? "\u041d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u043d\u043e \u0432 \u041e\u043b\u0438\u043c\u043f\u0438\u0430\u0434\u0435." : "You can't do it in Olympiad.");
			return;
		}
		if(player.getEvent(SiegeEvent.class) != null)
		{
			show(new CustomMessage("scripts.services.Rename.SiegeNow"), player);
			return;
		}
		final String name = args[0];
		if(!Util.isMatchingRegexp(name, Config.SERVICES_CHANGE_NICK_TEMPLATE) || StringUtils.containsIgnoreCase(name, "admin"))
		{
			show("<br>" + (player.isLangRus() ? "\u041d\u0435\u0434\u043e\u043f\u0443\u0441\u0442\u0438\u043c\u043e\u0435 \u0438\u043c\u044f" : "Invalid name") + ".<br><br><br><a action=\"bypass -h scripts_services.Rename:rename_page\">" + (player.isLangRus() ? "\u041d\u0430\u0437\u0430\u0434" : "Back") + "</a>", player);
			return;
		}
		if(getItemCount(player, Config.SERVICES_CHANGE_NICK_ITEM) < Config.SERVICES_CHANGE_NICK_PRICE)
		{
			if(Config.SERVICES_CHANGE_NICK_ITEM == 57)
				player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			else
				player.sendPacket(Msg.INCORRECT_ITEM_COUNT);
			return;
		}
		if(PlayerManager.getObjectIdByName(name) > 0)
		{
			show(new CustomMessage("scripts.services.Rename.Thisnamealreadyexists"), player);
			return;
		}
		removeItem(player, Config.SERVICES_CHANGE_NICK_ITEM, Config.SERVICES_CHANGE_NICK_PRICE);
		final String oldName = player.getName();
		player.reName(name, true);
		Log.addLog("Player " + oldName + " [" + player.getObjectId() + "] renamed to " + name, "renames");
		show(new CustomMessage("scripts.services.Rename.changedname").addString(oldName).addString(name), player);
		if(Config.HITMAN_ENABLE && Rnd.chance(Config.HITMAN_LOSS_CHANCE))
		{
			HitmanInstance.orderDelete(player.getObjectId());
			HitmanInstance.updateOrderPlayer();
		}
		if(player.isInParty())
			player.getParty().broadcastToPartyMembers(player, new PartySmallWindowUpdate(player));
	}

	public void showErasePetName()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(!Config.SERVICES_CHANGE_PET_NAME_ENABLED)
		{
			show("Сервис отключен.", player);
			return;
		}
		String out = "";
		out += "<html><body>\u0412\u044b \u043c\u043e\u0436\u0435\u0442\u0435 \u043e\u0431\u043d\u0443\u043b\u0438\u0442\u044c \u0438\u043c\u044f \u0443 \u043f\u0435\u0442\u0430, \u0434\u043b\u044f \u0442\u043e\u0433\u043e \u0447\u0442\u043e\u0431\u044b \u043d\u0430\u0437\u043d\u0430\u0447\u0438\u0442\u044c \u043d\u043e\u0432\u043e\u0435. \u041f\u0435\u0442 \u043f\u0440\u0438 \u044d\u0442\u043e\u043c \u0434\u043e\u043b\u0436\u0435\u043d \u0431\u044b\u0442\u044c \u0432\u044b\u0437\u0432\u0430\u043d.";
		out = out + "<br><font color=\"LEVEL\">\u0421\u0442\u043e\u0438\u043c\u043e\u0441\u0442\u044c \u043e\u0431\u043d\u0443\u043b\u0435\u043d\u0438\u044f: " + Util.formatAdena(Config.SERVICES_CHANGE_PET_NAME_PRICE) + " " + ItemTable.getInstance().getTemplate(Config.SERVICES_CHANGE_PET_NAME_ITEM).getName();
		out += "</font><br><button width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\" action=\"bypass -h scripts_services.Rename:erasePetName\" value=\"\u041e\u0431\u043d\u0443\u043b\u0438\u0442\u044c \u0438\u043c\u044f\">";
		out += "</body></html>";
		show(out, player);
	}

	public void erasePetName()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(!Config.SERVICES_CHANGE_PET_NAME_ENABLED)
		{
			show("Сервис отключен.", player);
			return;
		}
		final Servitor pet = player.getServitor();
		if(pet == null || !pet.isPet())
		{
			show("\u041f\u0438\u0442\u043e\u043c\u0435\u0446 \u0434\u043e\u043b\u0436\u0435\u043d \u0431\u044b\u0442\u044c \u0432\u044b\u0437\u0432\u0430\u043d.", player);
			return;
		}
		if(pet.getName() == null || pet.getName().isEmpty() || pet.getName().equalsIgnoreCase(pet.getTemplate().name))
		{
			show("\u041f\u0438\u0442\u043e\u043c\u0435\u0446 \u0431\u0435\u0437 \u0438\u043c\u0435\u043d\u0438.", player);
			return;
		}

		if(deleteItem(player, Config.SERVICES_CHANGE_PET_NAME_ITEM, Config.SERVICES_CHANGE_PET_NAME_PRICE))
		{
			pet.setName(pet.getTemplate().name);
			pet.broadcastPetInfo();
			final PetInstance _pet = (PetInstance) pet;
			final ItemInstance controlItem = _pet.getControlItem();
			if(controlItem != null)
			{
				controlItem.setCustomType2(1);
				controlItem.setPriceToSell(0);
				controlItem.updateDatabase();
				_pet.InventoryUpdateControlItem();
			}
			show("\u0418\u043c\u044f \u0441\u0442\u0435\u0440\u0442\u043e.", player);
		}
		else if(Config.SERVICES_CHANGE_PET_NAME_ITEM == 57)
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
		else
			player.sendPacket(Msg.INCORRECT_ITEM_COUNT);
	}

	public void changesex_page()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(!Config.SERVICES_CHANGE_SEX_ENABLED)
		{
			if(player.isLangRus())
				player.sendMessage("Сервис отключен.");
			else
				player.sendMessage("Service disabled.");
			return;
		}
		if(!player.isInPeaceZone())
		{
			show("You must be in peace zone to use this service.", player);
			return;
		}
		String append = "Sex changing";
		append += "<br>";
		append = append + "<font color=\"LEVEL\">" + new CustomMessage("scripts.services.SexChange.SexChangeFor").addString(Util.formatAdena(Config.SERVICES_CHANGE_SEX_PRICE)).addItemName(Config.SERVICES_CHANGE_SEX_ITEM).toString(player) + "</font>";
		append += "<table>";
		append = append + "<tr><td><button value=\"" + new CustomMessage("scripts.services.SexChange.Button").toString(player) + "\" action=\"bypass -h scripts_services.Rename:changesex\" width=80 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>";
		append += "</table>";
		show(append, player);
	}

	public void changesex()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(!Config.SERVICES_CHANGE_SEX_ENABLED)
		{
			if(player.isLangRus())
				player.sendMessage("Сервис отключен.");
			else
				player.sendMessage("Service disabled.");
			return;
		}
		if(!player.isInPeaceZone())
		{
			show("You must be in peace zone to use this service.", player);
			return;
		}
		if(getItemCount(player, Config.SERVICES_CHANGE_SEX_ITEM) < Config.SERVICES_CHANGE_SEX_PRICE)
		{
			if(Config.SERVICES_CHANGE_SEX_ITEM == 57)
				player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			else
				player.sendPacket(Msg.INCORRECT_ITEM_COUNT);
			return;
		}
		if(player.recording)
			player.writeBot(false);
		Connection con = null;
		PreparedStatement offline = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			offline = con.prepareStatement("UPDATE characters SET sex = ? WHERE obj_Id = ?");
			offline.setInt(1, player.getSex() != 1 ? 1 : 0);
			offline.setInt(2, player.getObjectId());
			offline.executeUpdate();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			show(new CustomMessage("common.Error"), player);
			return;
		}
		finally
		{
			DbUtils.closeQuietly(con, offline);
		}
		player.setHairColor(0);
		player.setHairStyle(0);
		player.setFace(0);
		removeItem(player, Config.SERVICES_CHANGE_SEX_ITEM, Config.SERVICES_CHANGE_SEX_PRICE);
		player.kick(true);
		Log.addLog("Character " + player + " sex changed to " + (player.getSex() == 1 ? "male" : "female"), "services");
	}

	public void changebase(final String[] param)
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(Config.SERVICES_CHANGE_BASE_ITEM < 1)
		{
			if(player.isLangRus())
				player.sendMessage("Сервис отключен.");
			else
				player.sendMessage("Service disabled.");
			return;
		}
		if(player.getLevel() < 76)
		{
			player.sendMessage(player.isLangRus() ? "\u0414\u043e\u0441\u0442\u0443\u043f\u043d\u043e \u0442\u043e\u043b\u044c\u043a\u043e \u0441 76-\u0433\u043e \u0443\u0440\u043e\u0432\u043d\u044f." : "Required 76+ level.");
			return;
		}
		if(!player.isInPeaceZone())
		{
			if(player.isLangRus())
				show("\u0414\u043e\u0441\u0442\u0443\u043f\u043d\u043e \u0442\u043e\u043b\u044c\u043a\u043e \u0432 \u043c\u0438\u0440\u043d\u043e\u0439 \u0442\u0435\u0440\u0440\u0438\u0442\u043e\u0440\u0438\u0438.", player);
			else
				show("You must be in peace zone to use this service.", player);
			return;
		}
		if(player.isSubClassActive())
		{
			if(player.isLangRus())
				show("\u0412\u044b \u0434\u043e\u043b\u0436\u043d\u044b \u0431\u044b\u0442\u044c \u043d\u0430 \u0431\u0430\u0437\u043e\u0432\u043e\u043c \u043a\u043b\u0430\u0441\u0441\u0435.", player);
			else
				show("You must be on your base class to use this service.", player);
			return;
		}
		if(!Config.SERVICES_CHANGE_BASE_HERO && player.isHero())
		{
			if(player.isLangRus())
				show("\u041d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u043d\u043e \u0434\u043b\u044f \u0433\u0435\u0440\u043e\u0435\u0432.", player);
			else
				show("Not available for heroes.", player);
			return;
		}
		if(Config.ENABLE_OLYMPIAD && (Olympiad.isRegistered(player) || player.getOlympiadGameId() > -1))
		{
			if(player.isLangRus())
				player.sendMessage("\u041d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u043d\u043e \u043f\u043e\u043a\u0430 \u0432\u044b \u043d\u0430\u0445\u043e\u0434\u0438\u0442\u0435\u0441\u044c \u0432 \u0441\u043f\u0438\u0441\u043a\u0435 \u043e\u0436\u0438\u0434\u0430\u043d\u0438\u044f \u041e\u043b\u0438\u043c\u043f\u0438\u0430\u0434\u044b.");
			else
				player.sendMessage("Impossible while you are in Grand Olympiad Games list.");
			return;
		}
		final int target = Integer.parseInt(param[0]);
		if(target < 88 || target > 118)
		{
			player.sendMessage(player.isLangRus() ? "\u041d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u043d\u044b\u0439 \u043a\u043b\u0430\u0441\u0441!" : "Incorrect class!");
			return;
		}
		if(player.getBaseClassId() == target)
		{
			player.sendMessage(player.isLangRus() ? "\u042d\u0442\u043e\u0442 \u043a\u043b\u0430\u0441\u0441 \u0443\u0436\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u0412\u0430\u0448\u0435\u0439 \u043e\u0441\u043d\u043e\u0432\u043e\u0439!" : "This class is your base class!");
			return;
		}
		if(player.isCastingNow())
		{
			player.sendMessage(player.isLangRus() ? "\u0417\u0430\u043f\u0440\u0435\u0449\u0435\u043d\u043e \u0432\u043e \u0432\u0440\u0435\u043c\u044f \u043a\u0430\u0441\u0442\u0430!" : "You can't do it while casting!");
			return;
		}
		if(getItemCount(player, Config.SERVICES_CHANGE_BASE_ITEM) < Config.SERVICES_CHANGE_BASE_PRICE)
		{
			if(Config.SERVICES_CHANGE_BASE_ITEM == 57)
				player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			else
				player.sendPacket(Msg.INCORRECT_ITEM_COUNT);
			return;
		}
		if(player.recording)
			player.writeBot(false);
		final long exp = player.getActiveClass().getExp();
		final int sp = player.getActiveClass().getSp();
		final byte lvl = player.getActiveClass().getLevel();
		player.getActiveClass().setBase(false);
		player.checkSkills(1);
		List<Integer> ids = new ArrayList<Integer>();
		for(final SubClass sc : player.getSubClasses().values())
			ids.add(sc.getClassId());
		for(final int id : ids)
			player.modifySubClass(id, 0);
		ids = null;
		mysql.set("UPDATE `character_subclasses` SET `class_id`=" + target + " WHERE `char_obj_id`=" + player.getObjectId() + " AND `isBase`=1 LIMIT 1");
		final SubClass newClass = new SubClass();
		newClass.setClassId(target);
		newClass.setPlayer(player);
		newClass.setBase(true);
		newClass.setActive(true);
		newClass.setExp(exp);
		newClass.setSp(sp);
		newClass.setLevel(lvl);
		player.getSubClasses().put(target, newClass);
		player.setActiveClass(newClass);
		player.setBaseClass(target);
		player.setHairColor(0);
		player.setHairStyle(0);
		player.setFace(0);
		removeItem(player, Config.SERVICES_CHANGE_BASE_ITEM, Config.SERVICES_CHANGE_BASE_PRICE);
		if(player.isNoble())
		{
			mysql.set("DELETE FROM `heroes` WHERE `char_id`=" + player.getObjectId() + " LIMIT 1");
			mysql.set("DELETE FROM `heroes_diary` WHERE `charId`=" + player.getObjectId());
			mysql.set("DELETE FROM `olympiad_nobles` WHERE `char_id`=" + player.getObjectId());
			Olympiad._nobles.remove(player.getObjectId());
			Olympiad.addNoble(player);
			Hero.deleteDiary(player.getObjectId());
			Hero.deleteHero(player.getObjectId());
		}
		Log.addLog(player + " [" + player.getObjectId() + "] base changed to " + target, "services");
		player.abortCast(true, false);
		player.getAbnormalList().stopAll();
		player.kick(true);
	}

	public void changebase_page()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(Config.SERVICES_CHANGE_BASE_ITEM < 1)
		{
			if(player.isLangRus())
				player.sendMessage("Сервис отключен.");
			else
				player.sendMessage("Service disabled.");
			return;
		}
		if(player.getLevel() < 76)
		{
			player.sendMessage(player.isLangRus() ? "\u0414\u043e\u0441\u0442\u0443\u043f\u043d\u043e \u0442\u043e\u043b\u044c\u043a\u043e \u0441 76-\u0433\u043e \u0443\u0440\u043e\u0432\u043d\u044f." : "Required 76+ level.");
			return;
		}
		if(!player.isInPeaceZone())
		{
			if(player.isLangRus())
				show("\u0414\u043e\u0441\u0442\u0443\u043f\u043d\u043e \u0442\u043e\u043b\u044c\u043a\u043e \u0432 \u043c\u0438\u0440\u043d\u043e\u0439 \u0442\u0435\u0440\u0440\u0438\u0442\u043e\u0440\u0438\u0438.", player);
			else
				show("You must be in peace zone to use this service.", player);
			return;
		}
		if(player.isSubClassActive())
		{
			if(player.isLangRus())
				show("\u0412\u044b \u0434\u043e\u043b\u0436\u043d\u044b \u0431\u044b\u0442\u044c \u043d\u0430 \u0431\u0430\u0437\u043e\u0432\u043e\u043c \u043a\u043b\u0430\u0441\u0441\u0435.", player);
			else
				show("You must be on your base class to use this service.", player);
			return;
		}
		if(!Config.SERVICES_CHANGE_BASE_HERO && player.isHero())
		{
			if(player.isLangRus())
				show("\u041d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u043d\u043e \u0434\u043b\u044f \u0433\u0435\u0440\u043e\u0435\u0432.", player);
			else
				show("Not available for heroes.", player);
			return;
		}
		String append = player.isLangRus() ? "\u0421\u043c\u0435\u043d\u0430 \u0431\u0430\u0437\u043e\u0432\u043e\u0433\u043e \u043a\u043b\u0430\u0441\u0441\u0430:" : "Base class changing:";
		append += "<br>";
		append = append + "<font color=\"LEVEL\">" + (player.isLangRus() ? "\u0421\u043c\u0435\u043d\u0438\u0442\u044c \u0431\u0430\u0437\u043e\u0432\u044b\u0439 \u043a\u043b\u0430\u0441\u0441 \u0437\u0430</font> <font color=\"FF0000\">" + Config.SERVICES_CHANGE_BASE_PRICE + " " + ItemTable.getInstance().getTemplate(Config.SERVICES_CHANGE_BASE_ITEM).getName() + "</font><font color=\"LEVEL\">. \u041f\u043e\u0441\u043b\u0435 \u0441\u043c\u0435\u043d\u044b \u043a\u043b\u0438\u0435\u043d\u0442 \u0430\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0437\u0430\u043a\u0440\u043e\u0435\u0442\u0441\u044f.</font>" : "Change character base class " + Config.SERVICES_CHANGE_BASE_PRICE + " " + ItemTable.getInstance().getTemplate(Config.SERVICES_CHANGE_BASE_ITEM).getName()) + "</font>";
		append += "<br>";
		for(int i = 88; i <= 118; ++i)
			append = append + "<a action=\"bypass -h scripts_services.Rename:changebase " + i + "\" msg=\"" + (player.isLangRus() ? "\u0412\u044b \u0432\u044b\u0431\u0440\u0430\u043b\u0438 " + CharTemplateTable.getClassNameById(i) + " \u0431\u0430\u0437\u043e\u0432\u044b\u043c \u043a\u043b\u0430\u0441\u0441\u043e\u043c. \u0412\u0441\u0435 \u0412\u0430\u0448\u044b \u0441\u0430\u0431\u043a\u043b\u0430\u0441\u0441\u044b \u0431\u0443\u0434\u0443\u0442 \u0443\u0434\u0430\u043b\u0435\u043d\u044b, \u0430 \u0434\u043e\u0441\u0442\u0438\u0436\u0435\u043d\u0438\u044f \u0432 \u041e\u043b\u0438\u043c\u043f\u0438\u0430\u0434\u0435 \u0431\u0443\u0434\u0443\u0442 \u0441\u0431\u0440\u043e\u0448\u0435\u043d\u044b. \u041f\u0440\u043e\u0434\u043e\u043b\u0436\u0438\u0442\u044c?" : "You chose " + CharTemplateTable.getClassNameById(i) + " as your base class. All your subclasses will be deleted and all your achievements in the Grand Olympiad will be reset. Continue?") + "\">" + new CustomMessage("scripts.services.BaseChange.Button").addString(ClassId.values()[i].toString()).toString(player) + "</a><br>";
		show(append, player);
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
}
