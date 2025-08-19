package services;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.cache.Msg;
import l2s.gameserver.data.xml.holder.OptionDataHolder;
import l2s.gameserver.database.mysql;
import l2s.gameserver.instancemanager.PlayerManager;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.components.CustomMessage;
import l2s.gameserver.network.l2.s2c.NpcHtmlMessage;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.stats.triggers.TriggerInfo;
import l2s.gameserver.tables.ItemTable;
import l2s.gameserver.templates.OptionDataTemplate;
import l2s.gameserver.templates.item.ItemGrade;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.templates.item.WeaponFightType;
import l2s.gameserver.utils.Util;
import l2s.gameserver.utils.VariationUtils;
import npc.model.AuctionInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class SkillLS extends Functions implements ScriptFile
{
	private static final Logger LOGGER = LoggerFactory.getLogger(SkillLS.class);

	private static final TIntObjectMap<OptionDataTemplate> EMPTY_VARIATIONS_MAP = new TIntObjectHashMap<>(0);

	private static final TIntObjectMap<OptionDataTemplate> WARRIOR_SKILL_VARIATIONS = new TIntObjectHashMap<>();
	private static final TIntObjectMap<OptionDataTemplate> MAGE_SKILL_VARIATIONS = new TIntObjectHashMap<>();

	public void listTr()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(Config.TRANSFER_AUGMENT_ITEM < 1)
		{
			if(player.isLangRus())
				player.sendMessage("Сервис отключен.");
			else
				player.sendMessage("Service disabled.");
			return;
		}
		String append = "<br>";
		append = append + "<font color=\"LEVEL\">" + new CustomMessage("scripts.services.SkillLS.transfer.For").addString(Util.formatAdena(Config.TRANSFER_AUGMENT_PRICE)).addItemName(Config.TRANSFER_AUGMENT_ITEM).toString(player) + "</font>";
		append += "<br><center><edit var=\"name\" width=170 height=10 length=16></center><br><br>";
		for(final ItemInstance item : player.getInventory().getItems())
			if(item.isAugmented())
			{
				append = append + "<table><tr><td height=32 width=32 valign=top><img src=" + item.getTemplate().getIcon() + " width=32 height=32></td>";
				append = append + "<td align=left valign=top width=250><font color=CCFF99><a action=\"bypass -h scripts_services.SkillLS:doTr " + item.getObjectId() + " $name\" msg=\"" + (player.isLangRus() ? "\u0410\u0443\u0433\u043c\u0435\u043d\u0442\u0430\u0446\u0438\u044f \u0431\u0443\u0434\u0435\u0442 \u043f\u0435\u0440\u0435\u043d\u0435\u0441\u0435\u043d\u0430 \u0438\u0437 \u0432\u044b\u0431\u0440\u0430\u043d\u043d\u043e\u0433\u043e \u043e\u0440\u0443\u0436\u0438\u044f \u043d\u0430 \u044d\u043a\u0438\u043f\u0438\u0440\u043e\u0432\u0430\u043d\u043d\u043e\u0435 \u043e\u0440\u0443\u0436\u0438\u0435 \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436\u0430 \u0441 \u0443\u043a\u0430\u0437\u0430\u043d\u043d\u044b\u043c \u0438\u043c\u0435\u043d\u0435\u043c. \u0415\u0441\u043b\u0438 \u0432\u044b \u043d\u0435 \u0432\u0432\u0435\u043b\u0438 \u0438\u043c\u044f, \u0442\u043e \u043f\u0435\u0440\u0435\u043d\u043e\u0441 \u043e\u0441\u0443\u0449\u0435\u0441\u0442\u0432\u0438\u0442\u0441\u044f \u0432 \u0432\u0430\u0448\u0435 \u044d\u043a\u0438\u043f\u0438\u0440\u043e\u0432\u0430\u043d\u043d\u043e\u0435 \u043e\u0440\u0443\u0436\u0438\u0435. \u041f\u0440\u043e\u0434\u043e\u043b\u0436\u0438\u0442\u044c?" : "Are you sure?") + "\">" + item.getTemplate().getName();
				if(!item.getTemplate().getAdditionalName().equals(""))
					append = append + " (" + item.getTemplate().getAdditionalName() + ")";
				if(item.getEnchantLevel() > 0)
					append = append + " +" + item.getEnchantLevel();
				append = append + "</a></font><br1>" + AuctionInstance.getAugmentSkillDesc(item) + "</td></tr></table>";
			}
		show(append, player);
	}

	public void doTr(final String[] param)
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(Config.TRANSFER_AUGMENT_ITEM < 1)
		{
			if(player.isLangRus())
				player.sendMessage("Сервис отключен.");
			else
				player.sendMessage("Service disabled.");
			return;
		}
		final ItemInstance wpn = player.getInventory().getItemByObjectId(Integer.parseInt(param[0]));
		if(wpn == null || !wpn.isWeapon())
		{
			if(player.isLangRus())
				player.sendMessage("Оружие не найдено.");
			else
				player.sendMessage("Weapon not found.");
			return;
		}
		if(!wpn.isAugmented())
		{
			if(player.isLangRus())
				player.sendMessage("Выбранное оружие не имеет аугментации.");
			else
				player.sendMessage("Weapon is not augmented.");
			return;
		}
		if(haveItem(player, Config.TRANSFER_AUGMENT_ITEM, Config.TRANSFER_AUGMENT_PRICE))
		{
			boolean state = false;
			String name = null;
			Player receiver = null;
			try
			{
				name = param[1].trim();
			}
			catch(Exception e)
			{
				name = player.getName();
			}
			if(player.getName().equalsIgnoreCase(name))
				receiver = player;
			else
				receiver = GameObjectsStorage.getPlayer(name);
			if(receiver != null)
			{
				name = receiver.getName();
				final ItemInstance w = receiver.getActiveWeaponInstance();
				if(!check(w, player))
					return;
				deleteItem(player, Config.TRANSFER_AUGMENT_ITEM, Config.TRANSFER_AUGMENT_PRICE);

				VariationUtils.setVariation(player, w, wpn.getVariationStoneId(), wpn.getVariation1Id(), wpn.getVariation2Id());

				state = !player.getName().equalsIgnoreCase(name);
			}
			else
			{
				final int receiverId = PlayerManager.getObjectIdByName(name);
				if(receiverId <= 0)
				{
					if(player.isLangRus())
						player.sendMessage("Персонаж с именем " + name + " не найден.");
					else
						player.sendMessage("Character " + name + " not exist.");
					return;
				}

				final int[] wn = PlayerManager.getCharWeaponById(receiverId);
				if(wn.length < 2)
				{
					if(player.isLangRus())
						player.sendMessage("На персонаже нет экипированного оружия.");
					else
						player.sendMessage("Character don't have equipped weapon.");
					return;
				}

				int variation1Id = mysql.simple_get_int("variation1_id", "items", "object_id=" + wn[0]);
				int variation2Id = mysql.simple_get_int("variation2_id", "items", "object_id=" + wn[0]);
				if(variation1Id > 0 || variation2Id > 0)
				{
					if(player.isLangRus())
						player.sendMessage("Оружие персонажа уже имеет аугментацию.");
					else
						player.sendMessage("Character's weapon must be without augmentation.");
					return;
				}

				if(!checkTemp(ItemTable.getInstance().getTemplate(wn[1]), player))
					return;

				if(!deleteItem(player, Config.TRANSFER_AUGMENT_ITEM, Config.TRANSFER_AUGMENT_PRICE))
					return;

				int variationStoneId = mysql.simple_get_int("variation_stone_id", "items", "object_id=" + wn[0]);
				mysql.set("UPDATE items SET variation1_id=0, variation2_id=0, variation_stone_id=0 WHERE object_id=?", wn[0]);
				mysql.set("UPDATE items SET variation1_id=?, variation2_id=?, variation_stone_id=? WHERE object_id=?", variation1Id, variation2Id, variationStoneId, wpn.getObjectId());
			}

			VariationUtils.setVariation(player, wpn, 0, 0, 0);

			if(!player.getName().equalsIgnoreCase(name))
			{
				if(player.isLangRus())
					player.sendMessage("Перенос аугментации на персонажа " + name + " успешно выполнен.");
				else
					player.sendMessage("Transfer augmentation on character " + name + " completed successfully.");
			}
			else if(player.isLangRus())
				player.sendMessage("Перенос аугментации успешно выполнен.");
			else
				player.sendMessage("Transfer augmentation completed successfully.");
			if(state)
				if(receiver.isLangRus())
					receiver.sendMessage("\u041f\u0435\u0440\u0441\u043e\u043d\u0430\u0436 " + player.getName() + " \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u043f\u0435\u0440\u0435\u043d\u0435\u0441 \u0432 \u0432\u0430\u0448\u0435 \u043e\u0440\u0443\u0436\u0438\u0435 \u0441\u0432\u043e\u044e \u0430\u0443\u0433\u043c\u0435\u043d\u0442\u0430\u0446\u0438\u044e.");
				else
					receiver.sendMessage("Character " + player.getName() + " you have successfully transferred augmentation.");
		}
		else if(Config.TRANSFER_AUGMENT_ITEM == 57)
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
		else
			player.sendPacket(Msg.INCORRECT_ITEM_COUNT);
	}

	public void listBAS(final String[] param)
	{
		final Player player = getSelf();
		if(player == null)
			return;

		if(Config.AUGMENT_SERVICE_COST_ITEM_ID < 1)
		{
			player.sendMessage(player.isLangRus() ? "Сервис отключен." : "Service disabled.");
			return;
		}

		ItemInstance weapon = player.getActiveWeaponInstance();
		if(weapon == null) {
			player.sendMessage(player.isLangRus() ? "Для покупки аугментации необходимо одеть оружие." : "To purchase augmentation, you must wear a weapon.");
			return;
		}

		TIntObjectMap<OptionDataTemplate> variations = getSkillVariations(weapon);
		if(variations.isEmpty())
		{
			player.sendMessage(player.isLangRus() ? "Вы не можете приобрести аугментацию для Вашего оружия." : "You cannot purchase augmentation for your weapon.");
			return;
		}

		int page = Integer.parseInt(param[0]);
		final int MaxPerPage = 8;
		int MaxPages = variations.size() / MaxPerPage;
		if(variations.size() > (MaxPerPage * MaxPages))
			MaxPages++;
		if(page > MaxPages)
			page = MaxPages;

		final NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		final StringBuffer replyMSG = new StringBuffer("<html><body>");
		replyMSG.append("<title>Buy Augment Skill</title><br>");
		replyMSG.append("<center><br><font color=\"ff0000\">" + (player.isLangRus() ? "Покупка скила аугментации" : "Buy augment skill") + "</font></center><br1>");

		final String msg = player.isLangRus() ? "Аугментация с выбранным скилом будет вставлена в Ваше экипированное оружие! Желаете продолжить?" : "Augmentation with the selected skill will be inserted into your equipped weapon! Continue?";
		replyMSG.append("<table width=290 border=0>");
		replyMSG.append("<tr><td><img src=\"L2UI.SquareWhite\" width=290 height=1><br></td></tr>");

		final int SkillsStart = MaxPerPage * page;
		int l = 0;
		for(TIntObjectIterator<OptionDataTemplate> iterator = variations.iterator(); iterator.hasNext();) {
			iterator.advance();
			if(l >= SkillsStart && l < (SkillsStart + MaxPerPage))
			{
				replyMSG.append("<tr><td>");
				if(l % 2 == 0)
					replyMSG.append("<table align=center width=290 bgcolor=000000>");
				else
					replyMSG.append("<table align=center width=290>");
				replyMSG.append("<tr><td>");

				int variation2Id = iterator.key();
				replyMSG.append("<font color=LEVEL><a action=\"bypass -h scripts_services.SkillLS:getBAS " + variation2Id + "\" msg=\"" + msg + "\">" + AuctionInstance.getAugmentSkillDesc(0, variation2Id) + "</a></font>");
				replyMSG.append("</td></tr><tr><td>");
				replyMSG.append("Цена: <font color=\"ffff33\">" + Config.AUGMENT_SERVICE_COST_ITEM_COUNT + " " + ItemTable.getInstance().getTemplate(Config.AUGMENT_SERVICE_COST_ITEM_ID).getName() + "</font>");
				replyMSG.append("</td></tr>");
				replyMSG.append("</table></td></tr>");
			}
			l++;
		}
		replyMSG.append("<tr><td height=4></td></tr>");
		replyMSG.append("<tr><td><img src=\"L2UI.SquareWhite\" width=290 height=1><br></td></tr>");
		replyMSG.append("</table>");
		String pages = "<center><table align=center><tr>";
		for(int x = 0; x < MaxPages; ++x)
		{
			final int pagenr = x + 1;
			pages = pages + "<td align=center><a action=\"bypass -h scripts_services.SkillLS:listBAS " + x + "\"><font color=\"" + (x == page ? "ffff33" : "33ff00") + "\">" + pagenr + "</font></a></td>";
		}
		pages += "</tr></table></center>";
		replyMSG.append(pages);
		replyMSG.append("</body></html>");
		adminReply.setHtml(replyMSG.toString());
		player.sendPacket(adminReply);
	}

	public void getBAS(final String[] param)
	{
		final Player player = getSelf();
		if(player == null)
			return;

		if(Config.AUGMENT_SERVICE_COST_ITEM_ID < 1)
		{
			player.sendMessage(player.isLangRus() ? "Сервис отключен." : "Service disabled.");
			return;
		}

		ItemInstance weapon = player.getActiveWeaponInstance();
		if(weapon == null) {
			player.sendMessage(player.isLangRus() ? "Для покупки аугментации необходимо одеть оружие." : "To purchase augmentation, you must wear a weapon.");
			return;
		}

		int variation2Id = Integer.parseInt(param[0]);
		Skill sk = AuctionInstance.getAugmentSkill(0, variation2Id);
		if(sk == null)
		{
			player.sendMessage("\u0421\u043a\u0438\u043b \u043d\u0435 \u0441\u0443\u0449\u0435\u0441\u0442\u0432\u0443\u0435\u0442!");
			return;
		}

		if(!sk.isItemSkill())
		{
			player.sendMessage("\u0414\u0430\u043d\u043d\u044b\u0439 \u0441\u043a\u0438\u043b \u043d\u0435\u043b\u044c\u0437\u044f \u043f\u0440\u0438\u043e\u0431\u0440\u0435\u0441\u0442\u0438!");
			return;
		}

		OptionDataTemplate skillVariation = getSkillVariations(weapon).get(variation2Id);
		OptionDataTemplate statsVariation = getRandomStatVariation(weapon);
		if(skillVariation == null || statsVariation == null)
		{
			player.sendMessage(player.isLangRus() ? "Вы не можете приобрести данную аугментацию для Вашего оружия." : "You cannot purchase this augmentation for your weapon.");
			return;
		}

		final ItemInstance wpn = player.getActiveWeaponInstance();
		if(!check(wpn, player))
			return;

		if(deleteItem(player, Config.AUGMENT_SERVICE_COST_ITEM_ID, Config.AUGMENT_SERVICE_COST_ITEM_COUNT))
		{
			VariationUtils.setVariation(player, wpn, 0, statsVariation.getId(), skillVariation.getId());
			player.sendMessage("\u0412\u044b \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u043f\u0440\u0438\u043e\u0431\u0440\u0435\u043b\u0438 \u0430\u0443\u0433\u043c\u0435\u043d\u0442\u0430\u0446\u0438\u044e \u0441\u043e \u0441\u043a\u0438\u043b\u043e\u043c!");
			return;
		}
		player.sendPacket(Config.AUGMENT_SERVICE_COST_ITEM_ID == 57 ? Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA : Msg.INCORRECT_ITEM_COUNT);
	}

	public static boolean check(final ItemInstance item, final Player player)
	{
		if(item == null)
		{
			player.sendMessage(player.isLangRus() ? "\u041d\u0430 \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436\u0435 \u043d\u0435\u0442 \u044d\u043a\u0438\u043f\u0438\u0440\u043e\u0432\u0430\u043d\u043d\u043e\u0433\u043e \u043e\u0440\u0443\u0436\u0438\u044f." : "Character don't have equipped weapon.");
			return false;
		}
		if(item.isWear())
		{
			player.sendMessage(player.isLangRus() ? "\u041f\u0440\u0438\u043c\u0435\u0440\u043e\u0447\u043d\u044b\u0435 \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u044b \u043d\u0435\u0432\u043e\u0437\u043c\u043e\u0436\u043d\u043e \u0430\u0443\u0433\u043c\u0435\u043d\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c." : "Wear item can't be augmented.");
			return false;
		}
		if(item.isAugmented())
		{
			player.sendMessage(player.isLangRus() ? "\u041e\u0440\u0443\u0436\u0438\u0435 \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436\u0430 \u0443\u0436\u0435 \u0438\u043c\u0435\u0435\u0442 \u0430\u0443\u0433\u043c\u0435\u043d\u0442\u0430\u0446\u0438\u044e." : "Character's weapon must be without augmentation.");
			return false;
		}
		return checkTemp(item.getTemplate(), player);
	}

	private static boolean checkTemp(final ItemTemplate item, final Player player)
	{
		if(!item.isWeapon())
		{
			player.sendMessage(player.isLangRus() ? "\u0414\u0430\u043d\u043d\u044b\u0439 \u043f\u0440\u0435\u0434\u043c\u0435\u0442 \u043d\u0435 \u044f\u0432\u043b\u044f\u0435\u0442\u0441\u044f \u043e\u0440\u0443\u0436\u0438\u0435\u043c." : "This item is not a weapon.");
			return false;
		}
		if(item.getItemGrade().ordinal() < ItemGrade.S.ordinal())
		{
			player.sendMessage(player.isLangRus() ? "\u041d\u0435\u043b\u044c\u0437\u044f \u0430\u0443\u0433\u043c\u0435\u043d\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u043e\u0440\u0443\u0436\u0438\u0435 \u043d\u0438\u0436\u0435 S \u0440\u0430\u043d\u0433\u0430." : "You can't augment weapon below S-grade.");
			return false;
		}
		if(item.isHeroItem())
		{
			player.sendMessage(player.isLangRus() ? "\u0413\u0435\u0440\u043e\u0439\u0441\u043a\u043e\u0435 \u043e\u0440\u0443\u0436\u0438\u0435 \u043d\u0435\u0432\u043e\u0437\u043c\u043e\u0436\u043d\u043e \u0430\u0443\u0433\u043c\u0435\u043d\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c." : "Hero weapon can't be augmented.");
			return false;
		}
		if(item.isShadowItem())
		{
			player.sendMessage(player.isLangRus() ? "\u0422\u0435\u043d\u0435\u0432\u044b\u0435 \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u044b \u043d\u0435\u0432\u043e\u0437\u043c\u043e\u0436\u043d\u043e \u0430\u0443\u0433\u043c\u0435\u043d\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c." : "Shadow item can't be augmented.");
			return false;
		}
		if(item.isTemporal())
		{
			player.sendMessage(player.isLangRus() ? "\u0412\u0440\u0435\u043c\u0435\u043d\u043d\u044b\u0435 \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u044b \u043d\u0435\u0432\u043e\u0437\u043c\u043e\u0436\u043d\u043e \u0430\u0443\u0433\u043c\u0435\u043d\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c." : "Limited item by time can't be augmented.");
			return false;
		}
		if(!item.isDestroyable())
		{
			player.sendMessage(player.isLangRus() ? "\u041e\u0440\u0443\u0436\u0438\u0435 \u043f\u0435\u0440\u0441\u043e\u043d\u0430\u0436\u0430 \u043d\u0435\u0432\u043e\u0437\u043c\u043e\u0436\u043d\u043e \u0430\u0443\u0433\u043c\u0435\u043d\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c." : "Character's weapon can't be augmented.");
			return false;
		}
		return true;
	}

	@Override
	public void onLoad()
	{
		loadVariations(WARRIOR_SKILL_VARIATIONS, WeaponFightType.WARRIOR);
		loadVariations(MAGE_SKILL_VARIATIONS, WeaponFightType.MAGE);
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	private static void loadVariations(TIntObjectMap<OptionDataTemplate> variationsMap, WeaponFightType weaponFightType) {
		final int[] skillVariations = weaponFightType == WeaponFightType.MAGE ? Config.AUGMENT_SERVICE_SKILLS_VARIATIONS_MAGE : Config.AUGMENT_SERVICE_SKILLS_VARIATIONS_WARRIOR;
		if(skillVariations.length == 0) {
			LOGGER.warn(SkillLS.class.getSimpleName() + ": Skills variations is empty for weapon TYPE[" + weaponFightType + "!");
			return;
		}

		final int[] statsVariations = weaponFightType == WeaponFightType.MAGE ? Config.AUGMENT_SERVICE_STATS_VARIATIONS_MAGE : Config.AUGMENT_SERVICE_STATS_VARIATIONS_WARRIOR;
		if(statsVariations.length == 0) {
			LOGGER.warn(SkillLS.class.getSimpleName() + ": Stats variations is empty for weapon TYPE[" + weaponFightType + "!");
			return;
		}

		for(int optionDataId : skillVariations) {
			OptionDataTemplate skillsOptionData = OptionDataHolder.getInstance().getTemplate(optionDataId);
			if(skillsOptionData == null) {
				LOGGER.warn(SkillLS.class.getSimpleName() + ": Cannot found option data ID[" + optionDataId + "]!");
				continue;
			}

			if(skillsOptionData.getSkills().isEmpty() && skillsOptionData.getTriggerList().isEmpty()) {
				LOGGER.warn(SkillLS.class.getSimpleName() + ": Skills variation option data ID[" + optionDataId + "] dont have skills and triggers!");
				continue;
			}
			variationsMap.put(skillsOptionData.getId(), skillsOptionData);
		}
	}

	private static TIntObjectMap<OptionDataTemplate> getSkillVariations(ItemInstance weapon) {
		if(weapon == null)
			return EMPTY_VARIATIONS_MAP;

		WeaponFightType weaponFightType = weapon.getTemplate().getWeaponFightType();
		return weaponFightType == WeaponFightType.MAGE ? MAGE_SKILL_VARIATIONS : WARRIOR_SKILL_VARIATIONS;
	}

	private static OptionDataTemplate getRandomStatVariation(ItemInstance weapon) {
		if(weapon == null)
			return null;

		WeaponFightType weaponFightType = weapon.getTemplate().getWeaponFightType();
		int[] statVariationsId = weaponFightType == WeaponFightType.MAGE ? Config.AUGMENT_SERVICE_STATS_VARIATIONS_MAGE : Config.AUGMENT_SERVICE_STATS_VARIATIONS_WARRIOR;
		return OptionDataHolder.getInstance().getTemplate(Rnd.get(statVariationsId));
	}

	private static Map<Skill, OptionDataTemplate> getVariationSkills(ItemInstance weapon) {
		Map<Skill, OptionDataTemplate> skills = new HashMap<>();
		if(weapon == null)
			return skills;

		TIntObjectMap<OptionDataTemplate> variations = getSkillVariations(weapon);
		for(OptionDataTemplate optionData : variations.valueCollection()) {
			for(Skill skill : optionData.getSkills())
				skills.put(skill, optionData);

			for(TriggerInfo triggerInfo : optionData.getTriggerList()) {
				Skill skill = triggerInfo.getSkill();
				if(skill != null)
					skills.put(skill, optionData);
			}
		}
		return skills;
	}
}
