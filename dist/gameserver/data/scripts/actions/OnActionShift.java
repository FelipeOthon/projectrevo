package actions;

import java.util.Set;
import java.util.TreeSet;

import commands.admin.AdminEditChar;
import l2s.gameserver.Config;
import l2s.gameserver.cache.InfoCache;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.model.AggroList;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.base.Experience;
import l2s.gameserver.model.instances.DoorInstance;
import l2s.gameserver.model.instances.MonsterInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.skills.Stats;
import l2s.gameserver.utils.DropList;
import l2s.gameserver.utils.Util;

public class OnActionShift extends Functions implements ScriptFile
{
	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public boolean OnActionShift_NpcInstance(final Player player, final GameObject object)
	{
		if(player == null || object == null)
			return false;
		if(!Config.ALLOW_NPC_SHIFTCLICK && !player.isGM())
		{
			if(Config.ALT_GAME_SHOW_DROPLIST && object.isNpc())
			{
				final NpcInstance _npc = (NpcInstance) object;
				if(_npc.isDead())
					return false;
				this.droplist(player, _npc);
			}
			return false;
		}
		if(object.isNpc())
		{
			final NpcInstance npc = (NpcInstance) object;
			if(npc.isDead())
				return false;
			String dialog;
			if(Config.ALT_FULL_NPC_STATS_PAGE)
			{
				dialog = HtmCache.getInstance().getHtml("scripts/actions/player.NpcInstance.onActionShift.full.htm", player);
				dialog = dialog.replaceFirst("%class%", String.valueOf(npc.getClass().getSimpleName().replaceFirst("Instance", "")));
				dialog = dialog.replaceFirst("%id%", String.valueOf(npc.getNpcId()));
				dialog = dialog.replaceFirst("%respawn%", String.valueOf(npc.getSpawn() != null ? npc.getSpawn().getRespawnDelayRandom() == 0 ? Util.formatTime(npc.getSpawn().getRespawnDelay()) : Util.formatTime(npc.getSpawn().getRespawnDelay() - npc.getSpawn().getRespawnDelayRandom()) + " - " + Util.formatTime(npc.getSpawn().getRespawnDelay() + npc.getSpawn().getRespawnDelayRandom()) : "0"));
				dialog = dialog.replaceFirst("%walkSpeed%", String.valueOf(npc.getWalkSpeed()));
				dialog = dialog.replaceFirst("%evs%", String.valueOf(npc.getEvasionRate((Creature) null)));
				dialog = dialog.replaceFirst("%acc%", String.valueOf(npc.getAccuracy()));
				dialog = dialog.replaceFirst("%crt%", String.valueOf(npc.getCriticalHit((Creature) null, (Skill) null)));
				dialog = dialog.replaceFirst("%aspd%", String.valueOf(npc.getPAtkSpd()));
				dialog = dialog.replaceFirst("%cspd%", String.valueOf(npc.getMAtkSpd()));
				dialog = dialog.replaceFirst("%loc%", String.valueOf(npc.getSpawn() != null ? npc.getSpawn().getLocation() : "0"));
				dialog = dialog.replaceFirst("%dist%", String.valueOf((int) npc.getDistance3D(player)));
				dialog = dialog.replaceFirst("%killed%", String.valueOf(npc.getTemplate().killscount));
				dialog = dialog.replaceFirst("%spReward%", String.valueOf(npc.getSpReward()));
				dialog = dialog.replaceFirst("%STR%", String.valueOf(npc.getSTR()));
				dialog = dialog.replaceFirst("%DEX%", String.valueOf(npc.getDEX()));
				dialog = dialog.replaceFirst("%CON%", String.valueOf(npc.getCON()));
				dialog = dialog.replaceFirst("%INT%", String.valueOf(npc.getINT()));
				dialog = dialog.replaceFirst("%WIT%", String.valueOf(npc.getWIT()));
				dialog = dialog.replaceFirst("%MEN%", String.valueOf(npc.getMEN()));
				dialog = dialog.replaceFirst("%xyz%", npc.getLoc().getX() + " " + npc.getLoc().getY() + " " + npc.getLoc().getZ());
				dialog = dialog.replaceFirst("%heading%", String.valueOf(npc.getLoc().h));
				dialog = dialog.replaceFirst("%ai_type%", npc.getAI().getClass().getSimpleName());
			}
			else
				dialog = HtmCache.getInstance().getHtml("scripts/actions/player.NpcInstance.onActionShift.htm", player);
			dialog = dialog.replaceFirst("%name%", npc.getName());
			dialog = dialog.replaceFirst("%level%", String.valueOf(npc.getLevel()));
			dialog = dialog.replaceFirst("%factionId%", npc.getFactionId().equals("") ? "none" : npc.getFactionId());
			dialog = dialog.replaceFirst("%aggro%", String.valueOf(npc.getAggroRange()));
			dialog = dialog.replaceFirst("%maxHp%", String.valueOf(npc.getMaxHp()));
			dialog = dialog.replaceFirst("%maxMp%", String.valueOf(npc.getMaxMp()));
			dialog = dialog.replaceFirst("%pDef%", String.valueOf(npc.getPDef((Creature) null)));
			dialog = dialog.replaceFirst("%mDef%", String.valueOf(npc.getMDef((Creature) null, (Skill) null)));
			dialog = dialog.replaceFirst("%pAtk%", String.valueOf(npc.getPAtk((Creature) null)));
			dialog = dialog.replaceFirst("%mAtk%", String.valueOf(npc.getMAtk((Creature) null, (Skill) null)));
			dialog = dialog.replaceFirst("%expReward%", String.valueOf(npc.getExpReward()));
			dialog = dialog.replaceFirst("%runSpeed%", String.valueOf(npc.getRunSpeed()));
			if(player.getPlayerAccess().IsGM)
				dialog = dialog.replaceFirst("%AI%", String.valueOf(npc.getAI()) + ",<br1>active: " + npc.getAI().isActive() + ",<br1>intention: " + npc.getAI().getIntention());
			else
				dialog = dialog.replaceFirst("%AI%", "");
			show(dialog, player);
		}
		return true;
	}

	public String getNpcRaceById(final int raceId)
	{
		switch(raceId)
		{
			case 1:
			{
				return "Undead";
			}
			case 2:
			{
				return "Magic Creatures";
			}
			case 3:
			{
				return "Beasts";
			}
			case 4:
			{
				return "Animals";
			}
			case 5:
			{
				return "Plants";
			}
			case 6:
			{
				return "Humanoids";
			}
			case 7:
			{
				return "Spirits";
			}
			case 8:
			{
				return "Angels";
			}
			case 9:
			{
				return "Demons";
			}
			case 10:
			{
				return "Dragons";
			}
			case 11:
			{
				return "Giants";
			}
			case 12:
			{
				return "Bugs";
			}
			case 13:
			{
				return "Fairies";
			}
			case 14:
			{
				return "Humans";
			}
			case 15:
			{
				return "Elves";
			}
			case 16:
			{
				return "Dark Elves";
			}
			case 17:
			{
				return "Orcs";
			}
			case 18:
			{
				return "Dwarves";
			}
			case 19:
			{
				return "Others";
			}
			case 20:
			{
				return "Non-living Beings";
			}
			case 21:
			{
				return "Siege Weapons";
			}
			case 22:
			{
				return "Defending Army";
			}
			case 23:
			{
				return "Mercenaries";
			}
			case 24:
			{
				return "Unknown Creature";
			}
			default:
			{
				return "Not defined";
			}
		}
	}

	public void droplist()
	{
		final Player player = getSelf();
		final NpcInstance npc = getNpc();
		if(player == null || npc == null)
			return;
		this.droplist(player, npc);
	}

	public void droplist(final Player player, final NpcInstance npc)
	{
		if(player == null || npc == null)
			return;
		if(!Config.ALT_GAME_GEN_DROPLIST_ON_DEMAND)
			show(InfoCache.getFromDroplistCache(npc.getNpcId()), player);
		else
		{
			final int diff = npc.calculateLevelDiffForDrop(player.isInParty() ? player.getParty().getLevel() : player.getLevel());
			double mult = 1.0;
			if(diff > 0)
				mult = Experience.penaltyModifier(diff, 9.0);
			mult = npc.calcStat(Stats.DROP, mult, (Creature) null, (Skill) null);
			show(DropList.generateDroplist(npc.getTemplate(), npc.isMonster() ? (MonsterInstance) npc : null, mult, player), player);
		}
	}

	public void stats()
	{
		final Player player = getSelf();
		final NpcInstance npc = getNpc();
		if(npc == null || player == null)
			return;
		String dialog = HtmCache.getInstance().getHtml("scripts/actions/player.NpcInstance.stats.htm", player);
		dialog = dialog.replaceFirst("%name%", npc.getName());
		dialog = dialog.replaceFirst("%level%", String.valueOf(npc.getLevel()));
		dialog = dialog.replaceFirst("%factionId%", npc.getFactionId());
		dialog = dialog.replaceFirst("%aggro%", String.valueOf(npc.getAggroRange()));
		dialog = dialog.replaceFirst("%race%", getNpcRaceById(npc.getTemplate().getRace()));
		dialog = dialog.replaceFirst("%herbs%", String.valueOf(npc.getTemplate().isDropHerbs));
		dialog = dialog.replaceFirst("%maxHp%", String.valueOf(npc.getMaxHp()));
		dialog = dialog.replaceFirst("%maxMp%", String.valueOf(npc.getMaxMp()));
		dialog = dialog.replaceFirst("%pDef%", String.valueOf(npc.getPDef((Creature) null)));
		dialog = dialog.replaceFirst("%mDef%", String.valueOf(npc.getMDef((Creature) null, (Skill) null)));
		dialog = dialog.replaceFirst("%pAtk%", String.valueOf(npc.getPAtk((Creature) null)));
		dialog = dialog.replaceFirst("%mAtk%", String.valueOf(npc.getMAtk((Creature) null, (Skill) null)));
		dialog = dialog.replaceFirst("%accuracy%", String.valueOf(npc.getAccuracy()));
		dialog = dialog.replaceFirst("%evasionRate%", String.valueOf(npc.getEvasionRate((Creature) null)));
		dialog = dialog.replaceFirst("%criticalHit%", String.valueOf(npc.getCriticalHit((Creature) null, (Skill) null)));
		dialog = dialog.replaceFirst("%runSpeed%", String.valueOf(npc.getRunSpeed()));
		dialog = dialog.replaceFirst("%walkSpeed%", String.valueOf(npc.getWalkSpeed()));
		dialog = dialog.replaceFirst("%pAtkSpd%", String.valueOf(npc.getPAtkSpd()));
		dialog = dialog.replaceFirst("%mAtkSpd%", String.valueOf(npc.getMAtkSpd()));
		dialog = dialog.replaceFirst("%STR%", String.valueOf(npc.getSTR()));
		dialog = dialog.replaceFirst("%DEX%", String.valueOf(npc.getDEX()));
		dialog = dialog.replaceFirst("%CON%", String.valueOf(npc.getCON()));
		dialog = dialog.replaceFirst("%INT%", String.valueOf(npc.getINT()));
		dialog = dialog.replaceFirst("%WIT%", String.valueOf(npc.getWIT()));
		dialog = dialog.replaceFirst("%MEN%", String.valueOf(npc.getMEN()));
		show(dialog, player);
	}

	public void resists()
	{
		final Player player = getSelf();
		final NpcInstance npc = getNpc();
		if(npc == null || player == null)
			return;
		final StringBuilder dialog = new StringBuilder("<html><body><center><font color=\"LEVEL\">");
		dialog.append(npc.getName()).append("<br></font></center><table width=\"70%\">");
		final int FIRE_RECEPTIVE = 100 - (int) npc.calcStat(Stats.FIRE_RECEPTIVE, 100.0, (Creature) null, (Skill) null);
		if(FIRE_RECEPTIVE != 0)
			dialog.append("<tr><td>Fire</td><td>").append(FIRE_RECEPTIVE).append("%</td></tr>");
		final int WIND_RECEPTIVE = 100 - (int) npc.calcStat(Stats.WIND_RECEPTIVE, 100.0, (Creature) null, (Skill) null);
		if(WIND_RECEPTIVE != 0)
			dialog.append("<tr><td>Wind</td><td>").append(WIND_RECEPTIVE).append("%</td></tr>");
		final int WATER_RECEPTIVE = 100 - (int) npc.calcStat(Stats.WATER_RECEPTIVE, 100.0, (Creature) null, (Skill) null);
		if(WATER_RECEPTIVE != 0)
			dialog.append("<tr><td>Water</td><td>").append(WATER_RECEPTIVE).append("%</td></tr>");
		final int EARTH_RECEPTIVE = 100 - (int) npc.calcStat(Stats.EARTH_RECEPTIVE, 100.0, (Creature) null, (Skill) null);
		if(EARTH_RECEPTIVE != 0)
			dialog.append("<tr><td>Earth</td><td>").append(EARTH_RECEPTIVE).append("%</td></tr>");
		final int SACRED_RECEPTIVE = 100 - (int) npc.calcStat(Stats.SACRED_RECEPTIVE, 100.0, (Creature) null, (Skill) null);
		if(SACRED_RECEPTIVE != 0)
			dialog.append("<tr><td>Light</td><td>").append(SACRED_RECEPTIVE).append("%</td></tr>");
		final int UNHOLY_RECEPTIVE = 100 - (int) npc.calcStat(Stats.UNHOLY_RECEPTIVE, 100.0, (Creature) null, (Skill) null);
		if(UNHOLY_RECEPTIVE != 0)
			dialog.append("<tr><td>Darkness</td><td>").append(UNHOLY_RECEPTIVE).append("%</td></tr>");
		final int BLEED_RECEPTIVE = (int) npc.calcStat(Stats.BLEED_RESIST, (Creature) null, (Skill) null);
		if(BLEED_RECEPTIVE != 0)
			dialog.append("<tr><td>Bleed</td><td>").append(BLEED_RECEPTIVE == Integer.MAX_VALUE ? "Max" : BLEED_RECEPTIVE == Integer.MIN_VALUE ? "Min" : BLEED_RECEPTIVE + "%").append("</td></tr>");
		final int POISON_RECEPTIVE = (int) npc.calcStat(Stats.POISON_RESIST, (Creature) null, (Skill) null);
		if(POISON_RECEPTIVE != 0)
			dialog.append("<tr><td>Poison</td><td>").append(POISON_RECEPTIVE == Integer.MAX_VALUE ? "Max" : POISON_RECEPTIVE == Integer.MIN_VALUE ? "Min" : POISON_RECEPTIVE + "%").append("</td></tr>");
		final int DEATH_RECEPTIVE = 100 - (int) npc.calcStat(Stats.DEATH_VULNERABILITY, 100.0, (Creature) null, (Skill) null);
		if(DEATH_RECEPTIVE != 0)
			dialog.append("<tr><td>Death</td><td>").append(DEATH_RECEPTIVE).append("%</td></tr>");
		final int STUN_RECEPTIVE = (int) npc.calcStat(Stats.STUN_RESIST, (Creature) null, (Skill) null);
		if(STUN_RECEPTIVE != 0)
			dialog.append("<tr><td>Stun</td><td>").append(STUN_RECEPTIVE == Integer.MAX_VALUE ? "Max" : STUN_RECEPTIVE == Integer.MIN_VALUE ? "Min" : STUN_RECEPTIVE + "%").append("</td></tr>");
		final int ROOT_RECEPTIVE = (int) npc.calcStat(Stats.ROOT_RESIST, (Creature) null, (Skill) null);
		if(ROOT_RECEPTIVE != 0)
			dialog.append("<tr><td>Root</td><td>").append(ROOT_RECEPTIVE == Integer.MAX_VALUE ? "Max" : ROOT_RECEPTIVE == Integer.MIN_VALUE ? "Min" : ROOT_RECEPTIVE + "%").append("</td></tr>");
		final int SLEEP_RECEPTIVE = (int) npc.calcStat(Stats.SLEEP_RESIST, (Creature) null, (Skill) null);
		if(SLEEP_RECEPTIVE != 0)
			dialog.append("<tr><td>Sleep</td><td>").append(SLEEP_RECEPTIVE == Integer.MAX_VALUE ? "Max" : SLEEP_RECEPTIVE == Integer.MIN_VALUE ? "Min" : SLEEP_RECEPTIVE + "%").append("</td></tr>");
		final int PARALYZE_RECEPTIVE = (int) npc.calcStat(Stats.PARALYZE_RESIST, (Creature) null, (Skill) null);
		if(PARALYZE_RECEPTIVE != 0)
			dialog.append("<tr><td>Paralyze</td><td>").append(PARALYZE_RECEPTIVE == Integer.MAX_VALUE ? "Max" : PARALYZE_RECEPTIVE == Integer.MIN_VALUE ? "Min" : PARALYZE_RECEPTIVE + "%").append("</td></tr>");
		final int MENTAL_RECEPTIVE = (int) npc.calcStat(Stats.MENTAL_RESIST, (Creature) null, (Skill) null);
		if(MENTAL_RECEPTIVE != 0)
			dialog.append("<tr><td>Mental</td><td>").append(MENTAL_RECEPTIVE == Integer.MAX_VALUE ? "Max" : MENTAL_RECEPTIVE == Integer.MIN_VALUE ? "Min" : MENTAL_RECEPTIVE + "%").append("</td></tr>");
		final int DEBUFF_RECEPTIVE = (int) npc.calcStat(Stats.DEBUFF_RESIST, (Creature) null, (Skill) null);
		if(DEBUFF_RECEPTIVE != 0)
			dialog.append("<tr><td>Debuff</td><td>").append(DEBUFF_RECEPTIVE == Integer.MAX_VALUE ? "Max" : DEBUFF_RECEPTIVE == Integer.MIN_VALUE ? "Min" : DEBUFF_RECEPTIVE + "%").append("</td></tr>");
		final int CANCEL_RECEPTIVE = (int) npc.calcStat(Stats.CANCEL_RESIST, (Creature) null, (Skill) null);
		if(CANCEL_RECEPTIVE != 0)
			dialog.append("<tr><td>Cancel</td><td>").append(CANCEL_RECEPTIVE == Integer.MAX_VALUE ? "Max" : CANCEL_RECEPTIVE == Integer.MIN_VALUE ? "Min" : CANCEL_RECEPTIVE + "%").append("</td></tr>");
		final int SWORD_WPN_RECEPTIVE = 100 - (int) npc.calcStat(Stats.SWORD_WPN_RECEPTIVE, 100.0, (Creature) null, (Skill) null);
		if(SWORD_WPN_RECEPTIVE != 0)
			dialog.append("<tr><td>Sword</td><td>").append(SWORD_WPN_RECEPTIVE).append("%</td></tr>");
		final int DUAL_WPN_RECEPTIVE = 100 - (int) npc.calcStat(Stats.DUAL_WPN_RECEPTIVE, 100.0, (Creature) null, (Skill) null);
		if(DUAL_WPN_RECEPTIVE != 0)
			dialog.append("<tr><td>Dual Sword</td><td>").append(DUAL_WPN_RECEPTIVE).append("%</td></tr>");
		final int BLUNT_WPN_RECEPTIVE = 100 - (int) npc.calcStat(Stats.BLUNT_WPN_RECEPTIVE, 100.0, (Creature) null, (Skill) null);
		if(BLUNT_WPN_RECEPTIVE != 0)
			dialog.append("<tr><td>Blunt</td><td>").append(BLUNT_WPN_RECEPTIVE).append("%</td></tr>");
		final int DAGGER_WPN_RECEPTIVE = 100 - (int) npc.calcStat(Stats.DAGGER_WPN_RECEPTIVE, 100.0, (Creature) null, (Skill) null);
		if(DAGGER_WPN_RECEPTIVE != 0)
			dialog.append("<tr><td>Dagger</td><td>").append(DAGGER_WPN_RECEPTIVE).append("%</td></tr>");
		final int BOW_WPN_RECEPTIVE = 100 - (int) npc.calcStat(Stats.BOW_WPN_RECEPTIVE, 100.0, (Creature) null, (Skill) null);
		if(BOW_WPN_RECEPTIVE != 0)
			dialog.append("<tr><td>Bow</td><td>").append(BOW_WPN_RECEPTIVE).append("%</td></tr>");
		final int POLE_WPN_RECEPTIVE = 100 - (int) npc.calcStat(Stats.POLE_WPN_RECEPTIVE, 100.0, (Creature) null, (Skill) null);
		if(POLE_WPN_RECEPTIVE != 0)
			dialog.append("<tr><td>Polearm</td><td>").append(POLE_WPN_RECEPTIVE).append("%</td></tr>");
		final int FIST_WPN_RECEPTIVE = 100 - (int) npc.calcStat(Stats.FIST_WPN_RECEPTIVE, 100.0, (Creature) null, (Skill) null);
		if(FIST_WPN_RECEPTIVE != 0)
			dialog.append("<tr><td>Fist weapons</td><td>").append(FIST_WPN_RECEPTIVE).append("%</td></tr>");
		if(FIRE_RECEPTIVE == 0 && WIND_RECEPTIVE == 0 && WATER_RECEPTIVE == 0 && EARTH_RECEPTIVE == 0 && UNHOLY_RECEPTIVE == 0 && SACRED_RECEPTIVE == 0 && BLEED_RECEPTIVE == 0 && DEATH_RECEPTIVE == 0 && STUN_RECEPTIVE == 0 && POISON_RECEPTIVE == 0 && ROOT_RECEPTIVE == 0 && SLEEP_RECEPTIVE == 0 && PARALYZE_RECEPTIVE == 0 && MENTAL_RECEPTIVE == 0 && DEBUFF_RECEPTIVE == 0 && CANCEL_RECEPTIVE == 0 && SWORD_WPN_RECEPTIVE == 0 && DUAL_WPN_RECEPTIVE == 0 && BLUNT_WPN_RECEPTIVE == 0 && DAGGER_WPN_RECEPTIVE == 0 && BOW_WPN_RECEPTIVE == 0 && POLE_WPN_RECEPTIVE == 0 && FIST_WPN_RECEPTIVE == 0)
			dialog.append("</table>No resists</body></html>");
		else
			dialog.append("</table></body></html>");
		show(dialog.toString(), player);
	}

	public void aggro()
	{
		final Player player = getSelf();
		final NpcInstance npc = getNpc();
		if(player == null || npc == null)
			return;
		final StringBuilder dialog = new StringBuilder("<html><body><table width=\"80%\"><tr><td>Attacker</td><td>Damage</td><td>Hate</td></tr>");
		final Set<AggroList.HateInfo> set = new TreeSet<AggroList.HateInfo>(AggroList.HateComparator.getInstance());
		set.addAll(npc.getAggroList().getCharMap().values());
		for(final AggroList.HateInfo aggroInfo : set)
			dialog.append("<tr><td>" + aggroInfo.attacker.getName() + "</td><td>" + aggroInfo.damage + "</td><td>" + aggroInfo.hate + "</td></tr>");
		dialog.append("</table><br><center><button value=\"");
		dialog.append(player.isLangRus() ? "\u041e\u0431\u043d\u043e\u0432\u0438\u0442\u044c" : "Refresh");
		dialog.append("\" action=\"bypass -h scripts_actions.OnActionShift:aggro\" width=100 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\" /></center></body></html>");
		show(dialog.toString(), player);
	}

	public boolean OnActionShift_DoorInstance(final Player player, final GameObject object)
	{
		if(player == null || object == null || !player.getPlayerAccess().Door)
			return false;
		if(object.isDoor())
		{
			final DoorInstance door = (DoorInstance) object;
			String dialog = HtmCache.getInstance().getHtml("scripts/actions/admin.DoorInstance.onActionShift.htm", player);
			dialog = dialog.replaceFirst("%CurrentHp%", String.valueOf(door.getCurrentHp()));
			dialog = dialog.replaceFirst("%MaxHp%", String.valueOf(door.getMaxHp()));
			dialog = dialog.replaceFirst("%ObjectId%", String.valueOf(door.getObjectId()));
			dialog = dialog.replaceFirst("%doorId%", String.valueOf(door.getDoorId()));
			dialog = dialog.replaceFirst("%pdef%", String.valueOf(door.getPDef((Creature) null)));
			dialog = dialog.replaceFirst("%mdef%", String.valueOf(door.getMDef((Creature) null, (Skill) null)));
			dialog = dialog.replaceFirst("bypass -h admin_open", "bypass -h admin_open " + door.getDoorId());
			dialog = dialog.replaceFirst("bypass -h admin_close", "bypass -h admin_close " + door.getDoorId());
			show(dialog, player);
			player.sendActionFailed();
		}
		return true;
	}

	public boolean OnActionShift_Player(final Player player, final GameObject object)
	{
		if(player == null || object == null || !player.getPlayerAccess().CanViewChar)
			return false;
		if(object.isPlayer())
			AdminEditChar.showCharacterList(player, (Player) object);
		return true;
	}

	public boolean OnActionShift_Summon(final Player player, final GameObject object)
	{
		return player != null && object != null && player.getPlayerAccess().CanViewChar && object.isSummon() && false;
	}
}
