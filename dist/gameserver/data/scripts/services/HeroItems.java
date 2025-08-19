package services;

import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.model.Player;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;

public class HeroItems extends Functions implements ScriptFile
{
	private static final String[][] HERO_ITEMS;
	private static final int[] weaponIds;

	public void getweapon(final String[] var)
	{
		final Player player = getSelf();
		if(player == null)
			return;
		final int item = Integer.parseInt(var[0]);
		if(item < 6611 && item > 6621)
		{
			ScriptFile._log.info(player.getName() + " tried to obtain non hero item using hero weapon service. Ban him!");
			return;
		}
		if(player.isHero() && !hasHeroWeapon(player))
			addItem(player, item, 1L);
	}

	public void rendershop(final String[] val)
	{
		final Player player = getSelf();
		if(player == null || !player.isHero() || hasHeroWeapon(player))
			return;
		String htmltext = "";
		if(val[0].equalsIgnoreCase("list"))
		{
			htmltext = "<html><body><font color=\"LEVEL\">List of Hero Weapons:</font><table border=0 width=270><tr><td>";
			for(int i = 0; i < HeroItems.HERO_ITEMS.length; ++i)
			{
				htmltext += "<tr><td width=32 height=45 valign=top>";
				htmltext = htmltext + "<img src=icon." + HeroItems.HERO_ITEMS[i][1] + " width=32 height=32></td>";
				htmltext = htmltext + "<td valign=top>[<a action=\"bypass -h scripts_services.HeroItems:rendershop " + i + "\">" + HeroItems.HERO_ITEMS[i][2] + "</a>]<br1>";
				htmltext = htmltext + "Type: " + HeroItems.HERO_ITEMS[i][5] + ", Patk/Matk: " + HeroItems.HERO_ITEMS[i][4];
				htmltext += "</td></tr>";
			}
			htmltext += "</table>";
		}
		else if(Integer.parseInt(val[0]) >= 0 && Integer.parseInt(val[0]) <= HeroItems.HERO_ITEMS.length)
		{
			final int itemIndex = Integer.parseInt(val[0]);
			htmltext = "<html><body><font color=\"LEVEL\">Item Information:</font><table border=0 width=270><tr><td>";
			htmltext += "<img src=\"L2UI.SquareWhite\" width=270 height=1>";
			htmltext += "<table border=0 width=240>";
			htmltext += "<tr><td width=32 height=45 valign=top>";
			htmltext = htmltext + "<img src=icon." + HeroItems.HERO_ITEMS[itemIndex][1] + " width=32 height=32></td>";
			htmltext = htmltext + "<td valign=top>[<a action=\"bypass -h scripts_services.HeroItems:getweapon " + HeroItems.HERO_ITEMS[itemIndex][0] + "\" msg=\"1484\">" + HeroItems.HERO_ITEMS[itemIndex][2] + "</a>]<br1>";
			htmltext = htmltext + "Type: " + HeroItems.HERO_ITEMS[itemIndex][5] + ", Patk/Matk: " + HeroItems.HERO_ITEMS[itemIndex][4] + "<br1>";
			htmltext += "</td></tr></table>";
			htmltext = htmltext + "<font color=\"B09878\">" + HeroItems.HERO_ITEMS[itemIndex][3] + "</font>";
			htmltext += "</td></tr></table><br>";
			htmltext += "<img src=\"L2UI.SquareWhite\" width=270 height=1><br><br>";
			htmltext += "<CENTER><button value=Back action=\"bypass -h scripts_services.HeroItems:rendershop list\" width=67 height=19 back=L2UI.DefaultButton_click fore=L2UI.DefaultButton></CENTER>";
		}
		show(htmltext, player);
	}

	public void getcir()
	{
		final Player player = getSelf();
		if(player == null)
			return;
		if(player.isHero())
		{
			if(player.getInventory().getItemByItemId(6842) != null || player.getWarehouse().findItemId(6842) != null)
			{
				show(HtmCache.getInstance().getHtml("olympiad/already_have_circlet.htm", player), player);
				return;
			}
			addItem(player, 6842, 1L);
		}
	}

	private static boolean hasHeroWeapon(final Player player)
	{
		for(final int i : HeroItems.weaponIds)
			if(player.getInventory().getItemByItemId(i) != null || player.getWarehouse().findItemId(i) != null)
			{
				show(HtmCache.getInstance().getHtml("olympiad/already_have_weapon.htm", player), player);
				return true;
			}
		return false;
	}

	@Override
	public void onLoad()
	{
		ScriptFile._log.info("Loaded Service: Hero Items");
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	static
	{
		HERO_ITEMS = new String[][] {
				{
						"6611",
						"weapon_the_sword_of_hero_i00",
						"Infinity Blade",
						"During a critical attack, decreases one's P. Def and increases de-buff casting ability, damage shield effect, Max HP, Max MP, Max CP, and shield defense power. Also enhances damage to target during PvP.",
						"297/137",
						"Sword" },
				{
						"6612",
						"weapon_the_two_handed_sword_of_hero_i00",
						"Infinity Cleaver",
						"Increases Max HP, Max CP, critical power and critical chance. Inflicts extra damage when a critical attack occurs and has possibility of reflecting the skill back on the player. Also enhances damage to target during PvP.",
						"361/137",
						"Two Handed Sword" },
				{
						"6613",
						"weapon_the_axe_of_hero_i00",
						"Infinity Axe",
						"During a critical attack, it bestows one the ability to cause internal conflict to one's opponent. Damage shield function, Max HP, Max MP, Max CP as well as one's shield defense rate are increased. It also enhances damage to one's opponent during PvP.",
						"297/137",
						"Blunt" },
				{
						"6614",
						"weapon_the_mace_of_hero_i00",
						"Infinity Rod",
						"When good magic is casted upon a target, increases MaxMP, MaxCP, Casting Spd, and MP regeneration rate. Also recovers HP 100% and enhances damage to target during PvP.",
						"238/182",
						"Blunt" },
				{
						"6615",
						"weapon_the_hammer_of_hero_i00",
						"Infinity Crusher",
						"Increases MaxHP, MaxCP, and Atk. Spd. Stuns a target when a critical attack occurs and has possibility of reflecting the skill back on the player. Also enhances damage to target during PvP.",
						"361/137",
						"Blunt" },
				{
						"6616",
						"weapon_the_staff_of_hero_i00",
						"Infinity Scepter",
						"When casting good magic, it can recover HP by 100% at a certain rate, increases MAX MP, MaxCP, M. Atk., lower MP Consumption, increases the Magic Critical rate, and reduce the Magic Cancel. Enhances damage to target during PvP.",
						"290/182",
						"Blunt" },
				{
						"6617",
						"weapon_the_dagger_of_hero_i00",
						"Infinity Stinger",
						"Increases MaxMP, MaxCP, Atk. Spd., MP regen rate, and the success rate of Mortal and Deadly Blow from the back of the target. Silences the target when a critical attack occurs and has Vampiric Rage effect. Also enhances damage to target during PvP.",
						"260/137",
						"Dagger" },
				{
						"6618",
						"weapon_the_fist_of_hero_i00",
						"Infinity Fang",
						"Increases MaxHP, MaxMP, MaxCP and evasion. Stuns a target when a critical attack occurs and has possibility of reflecting the skill back on the player at a certain probability rate. Also enhances damage to target during PvP.",
						"361/137",
						"Dual Fist" },
				{
						"6619",
						"weapon_the_bow_of_hero_i00",
						"Infinity Bow",
						"Increases MaxMP/MaxCP and decreases re-use delay of a bow. Slows target when a critical attack occurs and has Cheap Shot effect. Also enhances damage to target during PvP.",
						"614/137",
						"Bow" },
				{
						"6620",
						"weapon_the_dualsword_of_hero_i00",
						"Infinity Wing",
						"When a critical attack occurs, increases MaxHP, MaxMP, MaxCP and critical chance. Silences the target and has possibility of reflecting the skill back on the target. Also enhances damage to target during PvP.",
						"361/137",
						"Dual Sword" },
				{
						"6621",
						"weapon_the_pole_of_hero_i00",
						"Infinity Spear",
						"During a critical attack, increases MaxHP, Max CP, Atk. Spd. and Accuracy. Casts dispel on a target and has possibility of reflecting the skill back on the target. Also enhances damage to target during PvP.",
						"297/137",
						"Pole" } };
		weaponIds = new int[] { 6611, 6612, 6613, 6614, 6615, 6616, 6617, 6618, 6619, 6620, 6621 };
	}
}
