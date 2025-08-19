package npc.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import l2s.gameserver.data.xml.holder.OptionDataHolder;
import l2s.gameserver.stats.triggers.TriggerInfo;
import l2s.gameserver.templates.OptionDataTemplate;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javolution.text.TextBuilder;
import l2s.commons.dbcp.DbUtils;
import l2s.gameserver.Config;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.instancemanager.PlayerManager;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.Skill.SkillTargetType;
import l2s.gameserver.model.Skill.SkillType;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.ExMailArrived;
import l2s.gameserver.network.l2.s2c.ItemList;
import l2s.gameserver.network.l2.s2c.NpcHtmlMessage;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.tables.ItemTable;
import l2s.gameserver.templates.item.ItemGrade;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.templates.npc.NpcTemplate;
import l2s.gameserver.utils.Log;
import l2s.gameserver.utils.Util;

public class AuctionInstance extends NpcInstance
{
	private static Logger _log = LoggerFactory.getLogger(AuctionInstance.class);

	// включить аукцион
	private static final boolean ALLOW_AUCTION = true;
	// использовать стандартную страницу (default/80007.htm)
	private static final boolean DEFAULT_PAGE = false;
	// отправлять оплату на почту
	private static final boolean ON_MAIL = false;
	// разрешить аугментированные предметы
	private static final boolean ALLOW_AUGMENT = true;
	// запрещенные предметы
	private static final int[] FORBIDDEN = { 6611, 6612, 6613, 6614, 6615, 6616, 6617, 6618, 6619, 6620, 6621 };
	// исключения для грейда
	private static final int[] EXCLUSION = { 6660 };
	// валюта
	private static final String MONEY = "57,Adena;9909,Silver;9900,Gold";
	// валюта за аугментированные предметы
	private static final String MONEY_AUGMENT = "4037,CoL;9909,Silver;9900,Gold";
	// ид предмета, который взимается за выставление шмотки
	private static final int ITEM_COST = 0;
	// количество предмета, который взимается за выставление шмотки
	private static final int ITEM_COUNT_COST = 1;
	// ид предмета, который взимается за выставление аугментированного оружия
	private static final int ITEM_AUGMENT_COST = 0;
	// количество предмета, который взимается за выставление аугментированного оружия
	private static final int ITEM_COUNT_AUGMENT_COST = 1;
	// минимальная цена аугментированного оружия
	private static final int MIN_AUGMENT_PRICE = 1;
	// минимальная заточка предмета для выставления на аукцион
	private static final int MINIMUM_ENCHANT = 0;

	private static final int PAGE_LIMIT = 10;
	private static final int SORT_LIMIT = 8;
	private static final int MAX_ADENA = 2147000000;
	private static final String CENCH = "CCCC33";
	private static final String CPRICE = "669966";
	private static final String CITEM = "993366";
	private static final String CAUG1 = "333366";
	private static final String CAUG2 = "006699";
	private static final String[] sorts = { "Оружие", "Броня", "Бижутерия" };
	private static final long serialVersionUID = 1L;
	private static Map<Integer, String> AUC_MONEYS = new HashMap<Integer, String>();
	private static Map<Integer, String> AUC_AUGMENT_MONEYS = new HashMap<Integer, String>();
	private static final ReentrantLock buyLock = new ReentrantLock();

	public AuctionInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public String getHtmlPath(int npcId, int val, Player player)
	{
		if(DEFAULT_PAGE)
			return super.getHtmlPath(npcId, val, player);
		else
			return "<html><body><br>На аукционе Вы можете торговать заточенными и аугментированными шмотками.<br><button value=\"Просмотр\" action=\"bypass -h npc_%objectId%_list\" width=100 height=18 back=\"sek.cbui94\" fore=\"sek.cbui92\"><br><br>Если шмотку купили, оплата придет " + (ON_MAIL && Config.ALLOW_MAIL ? "на почту" : "в инвентарь") + ".</body></html>";
	}

	@Override
	public void onSpawn()
	{
		if(AUC_MONEYS.isEmpty())
			for(String augs : MONEY.split(";"))
			{
				String[] aug = augs.split(",");
				try
				{
					AUC_MONEYS.put(Integer.valueOf(Integer.parseInt(aug[0])), aug[1]);
				}
				catch(NumberFormatException nfe)
				{
					if(!aug[0].equals(""))
						_log.warn("AucMoney error: " + aug[0]);
				}
			}
		if(AUC_AUGMENT_MONEYS.isEmpty())
			for(String augs : MONEY_AUGMENT.split(";"))
			{
				String[] aug = augs.split(",");
				try
				{
					AUC_AUGMENT_MONEYS.put(Integer.valueOf(Integer.parseInt(aug[0])), aug[1]);
				}
				catch(NumberFormatException nfe)
				{
					if(!aug[0].equals(""))
						_log.warn("AucAugmentMoney error: " + aug[0]);
				}
			}
		super.onSpawn();
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
			return;
		if(!ALLOW_AUCTION)
		{
			message(player, player.isLangRus() ? "Сервис отключен." : "Service disabled.");
			return;
		}
		NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		String htmltext = "";
		if(command.equalsIgnoreCase("list"))
		{
			htmltext = "<html><body><table width=280><tr><td>Аукцион</td><td align=right><button value=\"Офис\" action=\"bypass -h npc_%objectId%_office\" width=70 height=18 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td align=right><button value=\"Добавить\" action=\"bypass -h npc_%objectId%_add\" width=70 height=18 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td align=right><button value=\"Поиск\" action=\"bypass -h npc_%objectId%_search\" width=40 height=18 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr></table><br>";
			htmltext += showSellItems(player, 1, 0, 0, 0, 0, -1, 0);
			htmltext += "</body></html>";
		}
		else if(command.startsWith("StockShowPage_"))
		{
			String[] params = command.split("_");
			int page, me, itemId, augment, type2, enchant;
			try
			{
				page = Integer.parseInt(params[1]);
				me = Integer.parseInt(params[2]);
				itemId = Integer.parseInt(params[3]);
				augment = Integer.parseInt(params[4]);
				type2 = Integer.parseInt(params[5]);
				enchant = Integer.parseInt(params[6]);
			}
			catch(Exception e)
			{
				message(player, "Ошибка запроса листания страниц!");
				return;
			}
			htmltext = "<html><body><table width=280><tr><td>Аукцион</td><td align=right><button value=\"Офис\" action=\"bypass -h npc_%objectId%_office\" width=70 height=18 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td align=right><button value=\"Добавить\" action=\"bypass -h npc_%objectId%_add\" width=70 height=18 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td align=right><button value=\"Поиск\" action=\"bypass -h npc_%objectId%_search\" width=40 height=18 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr></table><br><br1>";
			htmltext += showSellItems(player, page, me, 0, itemId, augment, type2, enchant);
			htmltext += "<br></body></html>";
		}
		else if(command.startsWith("StockShowItem_"))
		{
			int sellId;
			try
			{
				sellId = Integer.parseInt(command.replace("StockShowItem_", ""));
			}
			catch(Exception e)
			{
				message(player, "Ошибка запроса просмотра шмотки!");
				return;
			}
			TextBuilder text = new TextBuilder("<html><body>");
			Connection con = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			try
			{
				con = DatabaseFactory.getInstance().getConnection();
				st = con.prepareStatement("SELECT itemId, enchant, `variation1_id`,`variation2_id`, price, money, ownerId, shadow FROM `z_stock_items` WHERE `id`=? LIMIT 1");
				st.setInt(1, sellId);
				rs = st.executeQuery();
				if(rs.next())
				{
					int itemId = rs.getInt("itemId");
					ItemTemplate brokeItem = ItemTable.getInstance().getTemplate(itemId);
					if(brokeItem == null)
					{
						message(player, "Просмотр шмотки: ошибка запроса!");
						return;
					}
					text.append("<font color=666666>ID шмотки: " + itemId + "</font> <button value=\"Найти еще\" action=\"bypass -h npc_%objectId%_find_ 777 _ " + itemId + " _ Id шмотки\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\">");
					int enchant = rs.getInt("enchant");
					int variation1Id = rs.getInt("variation1_id");
					int variation2Id = rs.getInt("variation2_id");
					int price = rs.getInt("price");
					int money = rs.getInt("money");
					int charId = rs.getInt("ownerId");
					int shadow = rs.getInt("shadow");
					String valute = getMoneyCall(money, variation1Id > 0 || variation1Id > 0);
					text.append("<table width=300><tr><td><img src=\"" + brokeItem.getIcon() + "\" width=32 height=32></td><td><font color=LEVEL>" + brokeItem.getName() + " +" + enchant + "</font><br></td></tr></table><br><br>");
					text.append("Продавец: " + getSellerName(con, charId) + "<br><br>");
					text.append(getAugmentSkillDesc(variation1Id, variation1Id) + "<br1>");
					if(variation1Id > 0 || variation1Id > 0)
						text.append("<button value=\"Найти еще\" action=\"bypass -h npc_%objectId%_find_ 777 _ " + variation1Id + " _ " + variation2Id + " _ Id аугмента\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"><br><br>");
					if(player.getObjectId() == charId)
						text.append("<button value=\"Забрать\" action=\"bypass -h npc_%objectId%_StockBuyItem_" + sellId + "\" width=60 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"><br>");
					else
					{
						ItemInstance payment = player.getInventory().getItemByItemId(money);
						String fprice = Util.formatAdena(price);
						if(payment != null && payment.getCount() >= price)
						{
							text.append("<font color=33CC00>Стоимость: " + fprice + " " + valute + "</font><br>");
							text.append("<button value=\"Купить\" action=\"bypass -h npc_%objectId%_StockBuyItem_" + sellId + "\" width=60 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"><br>");
						}
						else
						{
							text.append("<font color=CC3333>Стоимость: " + fprice + " " + valute + "</font><br>");
							text.append("<font color=999999>[Купить]</font>");
						}
					}
				}
				else
				{
					message(player, "Просмотр шмотки: не найдена или уже купили!");
					return;
				}
			}
			catch(SQLException e)
			{
				_log.error("[ERROR] Auction, StockShowItem error: " + e, e);
			}
			finally
			{
				DbUtils.closeQuietly(con, st, rs);
			}
			text.append("<br><br><a action=\"bypass -h npc_%objectId%_list\">Вернуться</a><br>");
			text.append("</body></html>");
			htmltext = text.toString();
			text.clear();
		}
		else if(command.startsWith("StockBuyItem_"))
		{
			int sellId;
			try
			{
				sellId = Integer.parseInt(command.replace("StockBuyItem_", ""));
			}
			catch(Exception e)
			{
				message(player, "Ошибка запроса покупки шмотки!");
				return;
			}
			Connection con = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			player.block();
			buyLock.lock();
			try
			{
				con = DatabaseFactory.getInstance().getConnection();
				st = con.prepareStatement("SELECT itemId, itemName, enchant, variation1_id, variation2_id, variation_stone_id, price, money, ownerId, shadow FROM `z_stock_items` WHERE `id`=? LIMIT 1");
				st.setInt(1, sellId);
				rs = st.executeQuery();
				if(rs.next())
				{
					int itemId = rs.getInt("itemId");
					ItemTemplate bItem = ItemTable.getInstance().getTemplate(itemId);
					if(bItem == null)
					{
						message(player, "Покупка шмотки: ошибка запроса!");
						return;
					}
					String itemName = rs.getString("itemName");
					int enchant = rs.getInt("enchant");
					int variation1Id = rs.getInt("variation1_id");
					int variation2Id = rs.getInt("variation2_id");
					int variationStoneId = rs.getInt("variation_stone_id");
					int price = rs.getInt("price");
					int money = rs.getInt("money");
					int charId = rs.getInt("ownerId");
					int shadow = rs.getInt("shadow");
					String valute = getMoneyCall(money, variation1Id > 0 || variation2Id > 0);
					if(player.getObjectId() == charId)
						price = 0;
					if(price > 0)
					{
						ItemInstance pay = player.getInventory().getItemByItemId(money);
						if(pay != null && pay.getCount() >= price)
							player.getInventory().destroyItemByItemId(money, price, true);
						else
						{
							message(player, "Покупка шмотки: у Вас нет необходимых предметов!");
							return;
						}
					}
					PreparedStatement zabiraem = null;
					try
					{
						zabiraem = con.prepareStatement("DELETE FROM `z_stock_items` WHERE `id`=?");
						zabiraem.setInt(1, sellId);
						zabiraem.executeUpdate();
					}
					catch(Exception e)
					{
						message(player, "Покупка шмотки: ошибка базы данных!");
						return;
					}
					finally
					{
						DbUtils.closeQuietly(zabiraem);
					}
					if(price > 0 && !transferPay(con, charId, itemId, enchant, variation1Id, variation2Id, price, money))
					{
						message(player, "Покупка шмотки: ошибка запроса!");
						return;
					}
					ItemInstance item = player.getInventory().addItem(itemId, 1);
					if(enchant != 0)
						item.setEnchantLevel(enchant);
					item.setVariationStoneId(variationStoneId);
					item.setVariation1Id(variation1Id);
					item.setVariation2Id(variation2Id);
					item.updateDatabase();

					player.sendPacket(SystemMessage.obtainItems(item));
					player.sendPacket(new ItemList(player, false));
					Log.addLog("Buy: " + player.toString() + " | " + item, "auction");
					String action = "Снято";
					String payment = "";
					if(price > 0)
					{
						action = "Куплено";
						payment = "Стоимость: <font color=" + CPRICE + ">" + Util.formatAdena(price) + " " + valute + "</font>";
						Player alarm = GameObjectsStorage.getPlayer(charId);
						if(alarm != null)
							alarm.sendMessage("Уведомление с аукциона: " + bItem.getName() + " (+" + enchant + ") купили!");
					}
					htmltext = "<html><body><table><tr><td width=260>Аукцион</td><td align=right width=70><a action=\"bypass -h npc_%objectId%_list\">Назад|<-</a></td></tr></table><br>";
					htmltext += action + "<br1> <font color=" + CITEM + ">" + bItem.getName() + "</font><font color=" + CENCH + "> +" + enchant + "</font><br> " + getAugmentSkillDesc(variation1Id, variation2Id) + " <br> " + payment + "</body></html>";
				}
				else
				{
					message(player, "Покупка шмотки: не найдена или уже купили!");
					return;
				}
			}
			catch(SQLException e)
			{
				_log.error("[ERROR] Auction, StockBuyItem error: " + e, e);
			}
			finally
			{
				DbUtils.closeQuietly(con, st, rs);
				buyLock.unlock();
				player.unblock();
			}
		}
		else if(command.equalsIgnoreCase("search"))
		{
			htmltext = "<html><body><table><tr><td width=260>Аукцион</td><td align=right width=70><a action=\"bypass -h npc_%objectId%_list\">Назад|<-</a></td></tr></table><br>";
			htmltext += "<table width=260><tr>";
			int step = 0;
			for(int i = 0; i < sorts.length; i++)
			{
				htmltext += "<td><button value=\"" + sorts[i] + "\" action=\"bypass -h npc_%objectId%_find_ " + i + " _ no _ no\" width=70 height=18 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>";
				step++;
				if(step == 4)
				{
					step = 0;
					htmltext += "</tr><tr>";
				}
			}
			htmltext += "</tr></table><br><table width=220 border=0><tr><td align=right><edit var=\"value\" width=150 length=\"16\"></td><td align=right><combobox width=71 var=keyword list=\"Id шмотки;Id аугмента;min заточка\"></td><td><button value=\"Поиск\" action=\"bypass -h npc_%objectId%_find_ 777 _ $value _ $keyword\" width=50 height=18 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr></table>";
			htmltext += "<br></body></html>";
		}
		else if(command.startsWith("find_ "))
		{
			String[] params = command.split("_");
			int type2;
			String value, ptype, psort;
			try
			{
				type2 = Integer.parseInt(params[1].trim());
				value = params[2].trim();
				ptype = params[3].trim();
			}
			catch(Exception e)
			{
				message(player, "Ошибка запроса поиска!");
				return;
			}
			if(value.equals("") || ptype.equals(""))
			{
				htmltext = "<html><body>Задан пустой поисковый запрос<br><br><a action=\"bypass -h npc_%objectId%_list\">Вернуться</a></body></html>";
				html.setHtml(htmltext);
				player.sendPacket(html);
				htmltext = null;
				html = null;
				return;
			}
			if(type2 != 777 && type2 >= 0)
			{
				ptype = "Тип шмотки";
				psort = showSellItems(player, 1, 0, 0, 0, 0, type2, 0);
				if(type2 >= sorts.length)
				{
					message(player, "Превышен лимит сортировки!");
					return;
				}
				value = sorts[type2];
			}
			else
			{
				int id;
				try
				{
					id = Integer.parseInt(value);
				}
				catch(Exception e)
				{
					message(player, "Ошибка запроса сортировки!");
					return;
				}
				if(ptype.equals("Id шмотки"))
					psort = showSellItems(player, 1, 0, 0, id, 0, -1, 0);
				else if(ptype.equals("min заточка"))
					psort = showSellItems(player, 1, 0, 0, 0, 0, -1, id);
				else
					psort = showSellItems(player, 1, 0, 0, 0, id, -1, 0);
			}
			htmltext = "<html><body><table><tr><td width=260>Аукцион</td><td align=right width=70><a action=\"bypass -h npc_%objectId%_search\">Назад|<-</a></td></tr></table><br>";
			htmltext += "<table><tr><td width=260><font color=336699>Поиск по:</font> <font color=CC66CC>" + ptype + ":</font> <font color=CC33CC>" + value + "</font></td><td align=right width=70></td></tr></table><br1>";
			htmltext += psort;
			htmltext += "<br><a action=\"bypass -h npc_%objectId%_list\">Вернуться</a><br></body></html>";
		}
		else if(command.equalsIgnoreCase("add"))
		{
			htmltext = "<html><body><table><tr><td width=260>Аукцион</td><td align=right width=70><a action=\"bypass -h npc_%objectId%_list\">Назад|<-</a></td></tr></table><br1>";
			htmltext += "<font color=LEVEL>Что хотите выставить на продажу?</font><br1>";
			htmltext += "Оружие:<br1>";
			htmltext += "<table width=240><tr><td><button value=\"Выбрать\" action=\"bypass -h npc_%objectId%_enchanted_0\" width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>";
			if(ALLOW_AUGMENT)
				htmltext += "<td><button value=\"Аугментированное\" action=\"bypass -h npc_%objectId%_augment_1\" width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td>";
			htmltext += "</tr></table><br>--------<br1>";
			htmltext += "<button value=\"Броня (+)\" action=\"bypass -h npc_%objectId%_enchanted_1\" width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"><br>";
			htmltext += "<button value=\"Бижутерия (+)\" action=\"bypass -h npc_%objectId%_enchanted_2\" width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"><br>";
			htmltext += "</body></html>";
		}
		else if(command.startsWith("enchanted_"))
		{
			int chType = Integer.parseInt(command.replace("enchanted_", ""));
			if(chType > 3)
			{
				message(player, "Ошибка запроса добавления шмоток!");
				return;
			}
			htmltext = "<html><body><table><tr><td width=260>Аукцион</td><td align=right width=70><a action=\"bypass -h npc_%objectId%_add\">Назад|<-</a></td></tr></table><br1>";
			htmltext += "Шаг 1.<br>Выберите шмотку:<br><br><table width=300>";
			clearVars(player);
			for(ItemInstance item : player.getInventory().getItems())
			{
				if(item == null)
					continue;
				ItemTemplate itemTemplate = item.getTemplate();
				int itemId = itemTemplate.getItemId();
				int itemType = itemTemplate.getType2();
				ItemGrade itemGrade = itemTemplate.getItemGrade();
				int preench = item.getEnchantLevel();
				if(item.canBeEnchanted() && preench >= MINIMUM_ENCHANT && itemType == chType && !ArrayUtils.contains(FORBIDDEN, itemId) && (itemGrade.ordinal() > ItemGrade.B.ordinal() || ArrayUtils.contains(EXCLUSION, itemId)) && !item.isEquipped() && (ALLOW_AUGMENT || !item.isAugmented()))
				{
					String augment = "";
					if(chType == 0)
						augment = "<font color=333366>Нет аугмента</font>";
					if(item.isAugmented())
					{
						augment = getAugmentSkillDesc(item);
					}
					htmltext += "<tr><td><img src=\"" + itemTemplate.getIcon() + "\" width=32 height=32><br></td><td><a action=\"bypass -h npc_%objectId%_step2_" + item.getObjectId() + "_1\">" + itemTemplate.getName() + "</a> <font color=" + CENCH + ">+" + preench + "</font><br1>" + augment + "</td></tr>";
				}
			}
			htmltext += "</table><br></body></html>";
		}
		else if(command.startsWith("augment_"))
		{
			if(!ALLOW_AUGMENT)
			{
				message(player, "Аументированные шмотки запрещены!");
				return;
			}
			int chType = Integer.parseInt(command.replace("augment_", ""));
			if(chType != 1)
			{
				message(player, "Ошибка запроса добавления шмоток аугмента!");
				return;
			}
			htmltext = "<html><body><table><tr><td width=260>Аукцион</td><td align=right width=70><a action=\"bypass -h npc_%objectId%_add\">Назад|<-</a></td></tr></table><br1>";
			htmltext += "Шаг 1.<br>Выберите шмотку:<br><br><table width=300>";
			clearVars(player);
			for(ItemInstance item : player.getInventory().getItems())
			{
				if(item == null)
					continue;
				ItemTemplate itemTemplate = item.getTemplate();
				int itemId = itemTemplate.getItemId();
				int itemType = itemTemplate.getType2();
				ItemGrade itemGrade = itemTemplate.getItemGrade();
				int preench = item.getEnchantLevel();
				if(item.canBeEnchanted() && preench >= MINIMUM_ENCHANT && item.isAugmented() && getAugmentSkill(item) != null && itemType == 0 && !ArrayUtils.contains(FORBIDDEN, itemId) && (itemGrade.ordinal() > ItemGrade.B.ordinal() || ArrayUtils.contains(EXCLUSION, itemId)) && !item.isEquipped())
				{
					String augment = "";
					if(chType == 0)
						augment = "<font color=333366>Нет аугмента</font>";
					else
						augment = getAugmentSkillDesc(item);

					htmltext += "<tr><td><img src=\"" + itemTemplate.getIcon() + "\" width=32 height=32><br></td><td><a action=\"bypass -h npc_%objectId%_step2_" + item.getObjectId() + "_2\">" + itemTemplate.getName() + "</a> <font color=" + CENCH + ">+" + preench + "</font><br1>" + augment + "</td></tr>";
				}
			}
			htmltext += "</table><br></body></html>";
		}
		else if(command.startsWith("step2_"))
		{
			String[] params = command.split("_");
			int itemObjId, chType;
			try
			{
				itemObjId = Integer.parseInt(params[1]);
				chType = Integer.parseInt(params[2]);
			}
			catch(Exception e)
			{
				message(player, "Ошибка шагового запроса!");
				return;
			}
			ItemInstance item = player.getInventory().getItemByObjectId(itemObjId);
			if(item != null && item.canBeEnchanted() && !ArrayUtils.contains(FORBIDDEN, item.getItemId()) && (item.getTemplate().getItemGrade().ordinal() > ItemGrade.B.ordinal() || ArrayUtils.contains(EXCLUSION, item.getItemId())) && !item.isEquipped() && (ALLOW_AUGMENT || !item.isAugmented()))
			{
				ItemTemplate itemTemplate = item.getTemplate();
				int preench = item.getEnchantLevel();
				if(preench < MINIMUM_ENCHANT)
				{
					message(player, "Уровень заточки предмета слишком низок!");
					return;
				}
				String augment = "";
				if(chType == 2)
					augment = "<font color=333366>Нет аугмента</font>";
				int variation1Id = 0;
				int variation2Id = 0;
				boolean aug = false;
				if(item.isAugmented() && getAugmentSkill(item) != null)
				{
					variation1Id = item.getVariation1Id();
					variation2Id = item.getVariation2Id();
					augment = getAugmentSkillDesc(item);
					aug = true;
				}

				player.setVar("auc_variation1", String.valueOf(variation1Id));
				player.setVar("auc_variation2", String.valueOf(variation2Id));
				player.setVar("auc_objId", String.valueOf(itemObjId));
				player.setVar("auc_enchant", String.valueOf(preench));
				htmltext = "<html><body>Шаг 2.<br>Подтверждаете?<br>";
				htmltext += "<table width=300><tr><td><img src=\"" + itemTemplate.getIcon() + "\" width=32 height=32></td><td><font color=" + CITEM + ">" + itemTemplate.getName() + "</font><font color=" + CENCH + ">+" + preench + "</font><br>" + augment + "</td></tr></table><br><br>";
				htmltext += "Введите желаемую цену и выберите валюту:<br>";
				String mvars = "";
				if(aug)
					for(int i : AUC_AUGMENT_MONEYS.keySet())
						mvars += AUC_AUGMENT_MONEYS.get(i) + ";";
				else
					for(int i : AUC_MONEYS.keySet())
						mvars += AUC_MONEYS.get(i) + ";";
				htmltext += "<table width=300><tr><td><edit var=\"price\" width=70 length=\"16\"></td><td><combobox width=100 var=type list=\"" + mvars + "\"></td></tr></table><br>";
				htmltext += "<button value=\"Продолжить\" action=\"bypass -h npc_%objectId%_step3_ $price _ $type\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"><br>";
				htmltext += "<br><a action=\"bypass -h npc_%objectId%_add\">Вернуться</a></body></html>";
			}
			else
				htmltext = "<html><body>Ошибка!<br><a action=\"bypass -h npc_%objectId%_step1\">Вернуться</a></body></html>";
		}
		else if(command.startsWith("step3_ "))
		{
			String[] params = command.split("_ ");
			int price;
			String mvar;
			try
			{
				price = Integer.parseInt(params[1].trim());
				mvar = params[2];
			}
			catch(Exception e)
			{
				message(player, "Ошибка третьего шагового запроса!");
				return;
			}
			if(price <= 0)
			{
				message(player, "Ошибка цены!");
				return;
			}
			player.block();
			int myItem = player.getVarInt("auc_objId", 0);
			ItemInstance item = player.getInventory().getItemByObjectId(myItem);
			int mvarId = 57;
			boolean aug = item != null && item.isAugmented() && getAugmentSkill(item) != null;
			if(aug)
			{
				for(int i : AUC_AUGMENT_MONEYS.keySet())
					if(AUC_AUGMENT_MONEYS.get(i).equals(mvar))
					{
						mvarId = i;
						break;
					}
			}
			else
				for(int i : AUC_MONEYS.keySet())
					if(AUC_MONEYS.get(i).equals(mvar))
					{
						mvarId = i;
						break;
					}
			if(mvarId == 57 && price > MAX_ADENA)
			{
				clearVars(player);
				message(player, "Максимальная цена " + Util.formatAdena(MAX_ADENA) + " Adena.");
				player.unblock();
				return;
			}
			if(aug && price < MIN_AUGMENT_PRICE)
			{
				clearVars(player);
				message(player, "Минимальная цена " + MIN_AUGMENT_PRICE + ".");
				player.unblock();
				return;
			}
			int itemCost = aug ? ITEM_AUGMENT_COST : ITEM_COST;
			int itemCntCost = aug ? ITEM_COUNT_AUGMENT_COST : ITEM_COUNT_COST;
			if(item != null && item.canBeEnchanted() && item.getEnchantLevel() >= MINIMUM_ENCHANT && !item.isEquipped() && (ALLOW_AUGMENT || !item.isAugmented()))
			{
				ItemInstance cost = null;
				if(itemCost > 0)
				{
					cost = player.getInventory().getItemByItemId(itemCost);
					if(cost == null || cost.getCount() < itemCntCost)
					{
						clearVars(player);
						message(player, "Недостаточно предметов для оплаты выставления шмота!");
						player.unblock();
						return;
					}
				}
				int myEnch = player.getVarInt("auc_enchant", -1);
				int myVariation1 = player.getVarInt("auc_variation1", 0);
				int myVariation2 = player.getVarInt("auc_variation2", 0);
				int itemId = item.getItemId();
				ItemTemplate itemTemplate = item.getTemplate();
				int itemType = itemTemplate.getType2();
				int preench = item.getEnchantLevel();
				if(preench != myEnch)
				{
					clearVars(player);
					message(player, "Неправильная заточка!");
					player.unblock();
					return;
				}
				int variation1Id = 0;
				int variation2Id = 0;
				int variationStoneId = 0;
				if(aug)
				{
					variation1Id = item.getVariation1Id();
					variation2Id = item.getVariation2Id();
					variationStoneId = item.getVariationStoneId();
					if(myVariation1 != variation1Id && myVariation2 != variation2Id)
					{
						clearVars(player);
						message(player, "Неправильный аугмент!");
						player.unblock();
						return;
					}
				}
				Connection con2 = null;
				PreparedStatement storeitem = null;
				try
				{
					con2 = DatabaseFactory.getInstance().getConnection();
					storeitem = con2.prepareStatement("INSERT INTO `z_stock_items` (`id`,`itemId`,`itemName`,`enchant`,`variation1_id`,`variation2_id`,`variation_stone_id`,`price`,`money`,`type`,`ownerId`,`shadow`) VALUES (NULL,?,?,?,?,?,?,?,?,?,?,?);");
					storeitem.setInt(1, itemId);
					storeitem.setString(2, itemTemplate.getName());
					storeitem.setInt(3, preench);
					storeitem.setInt(4, variation1Id);
					storeitem.setInt(5, variation2Id);
					storeitem.setInt(6, variationStoneId);
					storeitem.setInt(7, price);
					storeitem.setInt(8, mvarId);
					storeitem.setInt(9, itemType);
					storeitem.setInt(10, player.getObjectId());
					storeitem.setInt(11, 0);
					storeitem.executeUpdate();
				}
				catch(Exception e)
				{
					clearVars(player);
					message(player, "Ошибка базы данных!");
					player.unblock();
					return;
				}
				finally
				{
					DbUtils.closeQuietly(con2, storeitem);
				}
				player.getInventory().destroyItem(myItem, 1, true);
				if(itemCost > 0)
					player.getInventory().destroyItem(cost, itemCntCost, false);
				clearVars(player);
				htmltext = "<html><body><table><tr><td width=260>Аукцион</td><td align=right width=70><a action=\"bypass -h npc_%objectId%_add\">Назад|<-</a></td></tr></table><br1>";
				htmltext += "<font color=" + CITEM + ">" + itemTemplate.getName() + "</font><font color=" + CENCH + ">+" + preench + "</font><br1> " + getAugmentSkillDesc(variation1Id, variation2Id) + " <br1>выставлена на продажу!</body></html>";
			}
			else
			{
				clearVars(player);
				message(player, "Ошибка в глубоком запросе!");
				player.unblock();
				return;
			}
			player.unblock();
		}
		else if(command.equalsIgnoreCase("office"))
		{
			htmltext = "<html><body><table><tr><td width=260>Аукцион</td><td align=right width=70><a action=\"bypass -h npc_%objectId%_list\">Назад|<-</a></td></tr></table><br1>";
			htmltext += "Привет, " + player.getName() + ".<br>";
			htmltext += "<button value=\"Мои шмотки\" action=\"bypass -h npc_%objectId%_StockShowPage_1_1_0_0_-1_-1\" width=70 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"><br>";
			htmltext += "</body></html>";
		}
		else
		{
			player.sendActionFailed();
			return;
		}
		html.setHtml(htmltext);
		player.sendPacket(html);
		htmltext = null;
		html = null;
	}

	private String getMoneyCall(int money, boolean aug)
	{
		return aug ? AUC_AUGMENT_MONEYS.get(Integer.valueOf(money)) : AUC_MONEYS.get(Integer.valueOf(money));
	}

	private String getSellerName(Connection con, int objId)
	{
		PreparedStatement st = null;
		ResultSet rset = null;
		try
		{
			st = con.prepareStatement("SELECT char_name FROM `characters` WHERE `obj_Id` = ? LIMIT 0,1");
			st.setInt(1, objId);
			rset = st.executeQuery();
			if(rset.next())
			{
				String str = rset.getString("char_name");
				return str;
			}
		}
		catch(Exception e)
		{
			_log.error("[ERROR] Auction, getSellerName() error: " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(st, rset);
		}
		return "???";
	}

	public static TriggerInfo getVariationTrigger(int variationId) {
		OptionDataTemplate optionData = OptionDataHolder.getInstance().getTemplate(variationId);
		if(optionData == null)
			return null;

		List<TriggerInfo> triggers = optionData.getTriggerList();
		if(triggers.isEmpty())
			return null;

		return triggers.get(0);
	}

	public static Skill getVariationSkill(int variationId) {
		OptionDataTemplate optionData = OptionDataHolder.getInstance().getTemplate(variationId);
		if(optionData == null)
			return null;

		List<Skill> skills = optionData.getSkills();
		if(skills.isEmpty())
			return null;

		return skills.get(0);
	}

	public static Skill getAugmentSkill(int variation1Id, int variation2Id) {
		TriggerInfo trigger = getVariationTrigger(variation2Id);
		Skill skill = getVariationSkill(variation2Id);
		if(trigger == null && skill == null) {
			trigger = getVariationTrigger(variation1Id);
			skill = getVariationSkill(variation1Id);
		}

		if(trigger != null)
			return trigger.getSkill();

		return skill;
	}

	public static Skill getAugmentSkill(ItemInstance item) {
		return getAugmentSkill(item.getVariation1Id(), item.getVariation2Id());
	}

	public static String getAugmentSkillDesc(int variation1Id, int variation2Id) {
		TriggerInfo trigger = getVariationTrigger(variation2Id);
		Skill skill = getVariationSkill(variation2Id);
		if(trigger == null && skill == null) {
			trigger = getVariationTrigger(variation1Id);
			skill = getVariationSkill(variation1Id);
		}

		if(trigger == null && skill == null)
			return "";

		Skill augment;
		String augName;
		String stype = "Пассив";
		String ttype = "";
		String power = "";
		if(trigger != null) {
			augment = trigger.getSkill();
			augName = augment.getName();
			stype = "Шанс";
		} else {
			augment = skill;
			augName = augment.getName();
			if (augment.isActive()) {
				stype = "Актив";
				if (augment.getTargetType() == SkillTargetType.TARGET_AURA || augment.getTargetType() == SkillTargetType.TARGET_AREA)
					ttype = "<br1>(Массовый)";
				if (augment.getSkillType() == SkillType.MDAM || augment.getSkillType() == SkillType.PDAM || augment.getSkillType() == SkillType.DRAIN)
					power = ":" + augment.getPower() + "power";
			}
		}
		augName = augName.replace("Item Skill: ", "");
		return "<font color=" + CAUG1 + ">Аугмент:</font> <font color=" + CAUG2 + ">" + augName + " Lv " + augment.getLevel() + " (" + stype + power + ")" + ttype + "</font>";
	}

	public static String getAugmentSkillDesc(ItemInstance item) {
		return getAugmentSkillDesc(item.getVariation1Id(), item.getVariation2Id());
	}

	private int getPageCount(Connection con, int me, int itemId, int augment, int type2, int charId, int enchant)
	{
		int rowCount = 0;
		PreparedStatement st = null;
		ResultSet rs = null;
		try
		{
			if(type2 >= 0)
			{
				st = con.prepareStatement("SELECT COUNT(`id`) FROM `z_stock_items` WHERE `type` = ?");
				st.setInt(1, type2);
			}
			else if(itemId > 0)
			{
				st = con.prepareStatement("SELECT COUNT(`id`) FROM `z_stock_items` WHERE `itemId` = ?");
				st.setInt(1, itemId);
			}
			else if(augment > 0)
			{
				st = con.prepareStatement("SELECT COUNT(`id`) FROM `z_stock_items` WHERE `augment` = ?");
				st.setInt(1, augment);
			}
			else if(enchant > 0)
			{
				st = con.prepareStatement("SELECT COUNT(`id`) FROM `z_stock_items` WHERE `enchant` >= ?");
				st.setInt(1, enchant);
			}
			else if(me == 1)
			{
				st = con.prepareStatement("SELECT COUNT(`id`) FROM `z_stock_items` WHERE `ownerId` = ?");
				st.setInt(1, charId);
			}
			else
				st = con.prepareStatement("SELECT COUNT(`id`) FROM `z_stock_items` WHERE `id` > 0");

			rs = st.executeQuery();
			if(rs.next())
				rowCount = rs.getInt(1);
		}
		catch(SQLException e)
		{
			_log.error("[ERROR] Auction, getPageCount() error: " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(st, rs);
		}
		if(rowCount == 0)
			return 0;

		int pages = rowCount / PAGE_LIMIT + 1;
		if(rowCount % PAGE_LIMIT > 0)
			pages++;
		return pages;
	}

	private boolean transferPay(Connection con, int charId, int itemId, int enchant, int variation1Id, int variation2Id, int price, int money)
	{
		ItemTemplate item = ItemTable.getInstance().getTemplate(itemId);
		if(item == null)
		{
			_log.warn("[ERROR] Auction, transferPay() error: not found itemId " + itemId);
			return false;
		}
		Player alarm = GameObjectsStorage.getPlayer(charId);
		if(ON_MAIL && Config.ALLOW_MAIL)
		{
			/*Calendar cal = Calendar.getInstance();
			String data = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
			String time = new SimpleDateFormat("HH-mm-ss").format(cal.getTime());*/
			TextBuilder text = new TextBuilder("");
			text.append("Итем: <font color=FF3399>" + item.getName() + " +" + enchant + " " + getAugmentSkillDesc(variation1Id, variation2Id) + "</font><br1> был успешно продан.<br1>");
			text.append("Благодарим за сотрудничество.");
			PreparedStatement st = null;
			try
			{
				st = con.prepareStatement("INSERT INTO `z_bbs_mail` (`from`, `to`, `tema`, `text`, `datetime`, `read`, `item_id`, `item_count`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
				st.setInt(1, 555);
				st.setInt(2, charId);
				st.setString(3, "Шмотка продана");
				st.setString(4, text.toString());
				st.setLong(5, System.currentTimeMillis());
				st.setInt(6, 0);
				st.setInt(7, money);
				st.setInt(8, price);
				st.execute();

				if(alarm != null)
				{
					alarm.sendMessage("Уведомление с аукциона: проверьте почту!");
					alarm.sendPacket(new ExMailArrived());
					Log.addLog("Sell: " + alarm.toString() + " | itemId: " + money + " | count: " + price, "auction");
				}
				else
					Log.addLog("Sell: " + PlayerManager.getNameByObjectId(charId) + "[" + charId + "] | itemId: " + money + " | count: " + price, "auction");
				return true;
			}
			catch(SQLException e)
			{
				_log.error("[ERROR] Auction, transferPay() error: " + e, e);
			}
			finally
			{
				text.clear();
				DbUtils.closeQuietly(st);
			}
		}
		else
		{
			if(alarm != null)
			{
				alarm.getInventory().addItem(money, price);
				alarm.sendPacket(SystemMessage.obtainItems(money, price, 0));
				alarm.sendMessage("Уведомление с аукциона: шмот продан!");
				Log.addLog("Sell: " + alarm.toString() + " | itemId: " + money + " | count: " + price, "auction");
			}
			else
			{
				Util.giveItem(charId, money, price);
				Log.addLog("Sell: " + PlayerManager.getNameByObjectId(charId) + "[" + charId + "] | itemId: " + money + " | count: " + price, "auction");
			}
			return true;
		}
		return false;
	}

	private String showSellItems(Player player, int page, int me, int last, int itemId, int augment, int type2, int enchant)
	{
		TextBuilder text = new TextBuilder("<br><table width=300><tr><td width=36></td><td width=264>");
		if(last == 1)
			text.append("Последние " + PAGE_LIMIT + ":</td></tr>");
		else
			text.append("Страница " + page + ":</td></tr>");
		int limit1 = (page - 1) * PAGE_LIMIT;
		String htmltext = "";
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			con.setTransactionIsolation(1);
			if(type2 >= 0)
			{
				st = con.prepareStatement("SELECT id, itemId, enchant, `variation1_id`,`variation2_id`, price, money, ownerId, shadow FROM `z_stock_items` WHERE `type` = ? ORDER BY `id` DESC LIMIT ?, ?");
				st.setInt(1, type2);
				st.setInt(2, limit1);
				st.setInt(3, PAGE_LIMIT);
			}
			else if(itemId > 0)
			{
				st = con.prepareStatement("SELECT id, itemId, enchant, `variation1_id`,`variation2_id`, price, money, ownerId, shadow FROM `z_stock_items` WHERE `itemId` = ? ORDER BY `id` DESC LIMIT ?, ?");
				st.setInt(1, itemId);
				st.setInt(2, limit1);
				st.setInt(3, PAGE_LIMIT);
			}
			else if(augment > 0)
			{
				st = con.prepareStatement("SELECT id, itemId, enchant, `variation1_id`,`variation2_id`, price, money, ownerId, shadow FROM `z_stock_items` WHERE `augment` = ? ORDER BY `id` DESC LIMIT ?, ?");
				st.setInt(1, augment);
				st.setInt(2, limit1);
				st.setInt(3, PAGE_LIMIT);
			}
			else if(enchant > 0)
			{
				st = con.prepareStatement("SELECT id, itemId, enchant, `variation1_id`,`variation2_id`, price, money, ownerId, shadow FROM `z_stock_items` WHERE `enchant` >= ? ORDER BY `id` DESC LIMIT ?, ?");
				st.setInt(1, enchant);
				st.setInt(2, limit1);
				st.setInt(3, PAGE_LIMIT);
			}
			else if(me == 1)
			{
				st = con.prepareStatement("SELECT id, itemId, enchant, `variation1_id`,`variation2_id`, price, money, ownerId, shadow FROM `z_stock_items` WHERE `ownerId` = ? ORDER BY `id` DESC LIMIT ?, ?");
				st.setInt(1, player.getObjectId());
				st.setInt(2, limit1);
				st.setInt(3, PAGE_LIMIT);
			}
			else
			{
				st = con.prepareStatement("SELECT id, itemId, enchant, `variation1_id`,`variation2_id`, price, money, ownerId, shadow FROM `z_stock_items` ORDER BY `id` DESC LIMIT ?, ?");
				st.setInt(1, limit1);
				st.setInt(2, PAGE_LIMIT);
			}
			rs = st.executeQuery();
			while(rs.next())
			{
				int sId = rs.getInt("id");
				int itmId = rs.getInt("itemId");
				int ownerId = rs.getInt("ownerId");
				ItemTemplate brokeItem = ItemTable.getInstance().getTemplate(itmId);
				if(brokeItem == null)
					continue;
				String priceB = "<font color=" + CPRICE + ">" + Util.formatAdena(rs.getInt("price")) + " " + getMoneyCall(rs.getInt("money"), rs.getInt("variation1_id") > 0 || rs.getInt("variation2_id") > 0) + " \"" + getSellerName(con, ownerId) + "</font>";
				if(player.getObjectId() == ownerId)
					priceB = "<table width=240><tr><td width=160><font color=666699>" + Util.formatAdena(rs.getInt("price")) + " " + getMoneyCall(rs.getInt("money"), rs.getInt("variation1_id") > 0 || rs.getInt("variation2_id") > 0) + "</font></td><td align=right><button value=\"X\" action=\"bypass -h npc_%objectId%_StockBuyItem_" + sId + "\" width=25 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr></table><br1>";
				text.append("<tr><td><img src=\"" + brokeItem.getIcon() + "\" width=32 height=32></td><td><a action=\"bypass -h npc_%objectId%_StockShowItem_" + sId + "\"> <font color=" + CITEM + ">" + brokeItem.getName() + "</font></a><font color=" + CENCH + ">+" + rs.getInt("enchant") + "</font> <br1> " + priceB + " <br1>" + getAugmentSkillDesc(rs.getInt("variation1_id"), rs.getInt("variation2_id")) + "</td></tr>");
			}
			text.append("</table><br>");
			if(last == 1)
				text.append("<br>");
			else
			{
				int pages = getPageCount(con, me, itemId, augment, type2, player.getObjectId(), enchant);
				if(pages > 1)
					text.append(sortPages(page, pages, me, itemId, augment, type2, enchant));
			}
			htmltext = text.toString();
			text.clear();
		}
		catch(SQLException e)
		{
			_log.error("[ERROR] Auction, showSellItems() error: " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, st, rs);
		}
		return htmltext;
	}

	private String sortPages(int page, int pages, int me, int itemId, int augment, int type2, int enchant)
	{
		TextBuilder text = new TextBuilder("<br>Страницы:<br1><table width=300><tr>");
		int step = 1;
		int s = page - 3;
		int f = page + 3;
		if(page < SORT_LIMIT && s < SORT_LIMIT)
			s = 1;
		if(page >= SORT_LIMIT)
			text.append("<td><a action=\"bypass -h npc_%objectId%_StockShowPage_" + s + "_" + me + "_" + itemId + "_" + augment + "_" + type2 + "_" + enchant + "\"> ... </a></td>");
		for(int i = s; i <= pages; i++)
		{
			int al = i + 1;
			if(i == page)
				text.append("<td>" + i + "</td>");
			else if(al <= pages)
				text.append("<td><a action=\"bypass -h npc_%objectId%_StockShowPage_" + i + "_" + me + "_" + itemId + "_" + augment + "_" + type2 + "_" + enchant + "\">" + i + "</a></td>");

			if(step == SORT_LIMIT && f < pages)
			{
				if(al < pages)
					text.append("<td><a action=\"bypass -h npc_%objectId%_StockShowPage_" + al + "_" + me + "_" + itemId + "_" + augment + "_" + type2 + "_" + enchant + "\"> ... </a></td>");
				break;
			}
			step++;
		}
		text.append("</tr></table><br>");
		String htmltext = text.toString();
		text.clear();
		return htmltext;
	}

	private void message(Player player, String text)
	{
		String htmltext = "<html><body><br>" + text + "</body></html>";
		NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setHtml(htmltext);
		player.sendPacket(html);
		htmltext = null;
		html = null;
	}

	private static void clearVars(Player player)
	{
		player.unsetVar("auc_objId");
		player.unsetVar("auc_variation1");
		player.unsetVar("auc_variation2");
		player.unsetVar("auc_enchant");
	}
}