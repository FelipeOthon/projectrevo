package commands.voiced;

import java.text.NumberFormat;
import java.util.Locale;

import l2s.gameserver.Config;
import l2s.gameserver.handler.IVoicedCommandHandler;
import l2s.gameserver.handler.VoicedCommandHandler;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.skills.Formulas;
import l2s.gameserver.skills.Stats;

public class MyInfo extends Functions implements IVoicedCommandHandler, ScriptFile
{
	private static final String[] _commandList;

	@Override
	public boolean useVoicedCommand(final String command, final Player activeChar, final String args)
	{
		if(!Config.ALLOW_VOICED_COMMANDS)
			return false;

		if(command.equalsIgnoreCase("myinfo"))
		{
			if(!Config.ALLOW_MY_INFO && !activeChar.isGM())
				return true;
			showInfo(activeChar);
			return true;
		}
		else
		{
			if(command.equalsIgnoreCase("atkrange"))
			{
				activeChar.sendMessage("Attack range: " + activeChar.getPhysicalAttackRange());
				if(activeChar.isGM() && activeChar.getTarget() != null && activeChar.getTarget().isCreature())
					activeChar.sendMessage(activeChar.getTarget().getName() + " attack range: " + ((Creature) activeChar.getTarget()).getPhysicalAttackRange());
				return true;
			}
			return false;
		}
	}

	private static void showInfo(final Player player)
	{
		if(player == null)
			return;
		final StringBuilder dialog = new StringBuilder("<html><body>");
		final NumberFormat df = NumberFormat.getNumberInstance(Locale.ENGLISH);
		df.setMaximumFractionDigits(4);
		df.setMinimumFractionDigits(1);
		dialog.append("<center><font color=\"LEVEL\">Basic info</font></center><br1><table width=\"70%\">");
		dialog.append("<tr><td>Name</td><td>").append(player.getName()).append("</td></tr>");
		dialog.append("<tr><td>Level</td><td>").append(player.getLevel()).append("</td></tr>");
		dialog.append("<tr><td>Class</td><td>").append(player.getClassId().name()).append("</td></tr>");
		dialog.append("<tr><td>Object id</td><td>").append(player.getObjectId()).append("</td></tr>");
		dialog.append("<tr><td>IP</td><td>").append(player.getNetConnection().getIpAddr()).append("</td></tr>");
		dialog.append("<tr><td>Login</td><td>").append(player.getAccountName()).append("</td></tr>");
		dialog.append("</table><br><center><font color=\"LEVEL\">Stats</font></center><br1><table width=\"70%\">");
		dialog.append("<tr><td>HP regeneration</td><td>").append(df.format(Formulas.calcHpRegen(player))).append("</td></tr>");
		dialog.append("<tr><td>MP regeneration</td><td>").append(df.format(Formulas.calcMpRegen(player))).append("</td></tr>");
		dialog.append("<tr><td>CP regeneration</td><td>").append(df.format(Formulas.calcCpRegen(player))).append("</td></tr>");
		dialog.append("<tr><td>HP drain</td><td>").append(df.format(player.calcStat(Stats.ABSORB_DAMAGE_PERCENT, 0.0, (Creature) null, (Skill) null))).append("%</td></tr>");
		dialog.append("<tr><td>HP gain bonus</td><td>").append(df.format(player.calcStat(Stats.HEAL_EFFECTIVNESS, 100.0, (Creature) null, (Skill) null) - 100.0)).append("%</td></tr>");
		dialog.append("<tr><td>MP gain bonus</td><td>").append(df.format(player.calcStat(Stats.MANAHEAL_EFFECTIVNESS, 100.0, (Creature) null, (Skill) null) - 100.0)).append("%</td></tr>");
		dialog.append("<tr><td>Critical damage</td><td>").append(df.format(player.calcStat(Stats.CRITICAL_DAMAGE, 100.0, (Creature) null, (Skill) null) + 100.0)).append("% + ").append((int) player.calcStat(Stats.CRITICAL_DAMAGE_STATIC, 0.0, (Creature) null, (Skill) null)).append("</td></tr>");
		if(Config.MCRIT_MYINFO)
			dialog.append("<tr><td>Magic critical</td><td>").append(df.format(player.getCriticalMagic((Creature) null, (Skill) null) / 10.0)).append("%</td></tr>");
		dialog.append("</table><br><center><font color=\"LEVEL\">Resists</font></center><br1><table width=\"70%\">");
		final int FIRE_RECEPTIVE = 100 - (int) player.calcStat(Stats.FIRE_RECEPTIVE, 100.0, (Creature) null, (Skill) null);
		if(FIRE_RECEPTIVE != 0)
			dialog.append("<tr><td>Fire</td><td>").append(FIRE_RECEPTIVE).append("</td></tr>");
		final int WIND_RECEPTIVE = 100 - (int) player.calcStat(Stats.WIND_RECEPTIVE, 100.0, (Creature) null, (Skill) null);
		if(WIND_RECEPTIVE != 0)
			dialog.append("<tr><td>Wind</td><td>").append(WIND_RECEPTIVE).append("</td></tr>");
		final int WATER_RECEPTIVE = 100 - (int) player.calcStat(Stats.WATER_RECEPTIVE, 100.0, (Creature) null, (Skill) null);
		if(WATER_RECEPTIVE != 0)
			dialog.append("<tr><td>Water</td><td>").append(WATER_RECEPTIVE).append("</td></tr>");
		final int EARTH_RECEPTIVE = 100 - (int) player.calcStat(Stats.EARTH_RECEPTIVE, 100.0, (Creature) null, (Skill) null);
		if(EARTH_RECEPTIVE != 0)
			dialog.append("<tr><td>Earth</td><td>").append(EARTH_RECEPTIVE).append("</td></tr>");
		final int SACRED_RECEPTIVE = 100 - (int) player.calcStat(Stats.SACRED_RECEPTIVE, 100.0, (Creature) null, (Skill) null);
		if(SACRED_RECEPTIVE != 0)
			dialog.append("<tr><td>Light</td><td>").append(SACRED_RECEPTIVE).append("</td></tr>");
		final int UNHOLY_RECEPTIVE = 100 - (int) player.calcStat(Stats.UNHOLY_RECEPTIVE, 100.0, (Creature) null, (Skill) null);
		if(UNHOLY_RECEPTIVE != 0)
			dialog.append("<tr><td>Darkness</td><td>").append(UNHOLY_RECEPTIVE).append("</td></tr>");
		final int BLEED_RECEPTIVE = (int) player.calcStat(Stats.BLEED_RESIST, (Creature) null, (Skill) null);
		if(BLEED_RECEPTIVE != 0)
			dialog.append("<tr><td>Bleed</td><td>").append(BLEED_RECEPTIVE == Integer.MAX_VALUE ? "Max" : BLEED_RECEPTIVE == Integer.MIN_VALUE ? "Min" : BLEED_RECEPTIVE + "%").append("</td></tr>");
		final int POISON_RECEPTIVE = (int) player.calcStat(Stats.POISON_RESIST, (Creature) null, (Skill) null);
		if(POISON_RECEPTIVE != 0)
			dialog.append("<tr><td>Poison</td><td>").append(POISON_RECEPTIVE == Integer.MAX_VALUE ? "Max" : POISON_RECEPTIVE == Integer.MIN_VALUE ? "Min" : POISON_RECEPTIVE + "%").append("</td></tr>");
		final int DEATH_RECEPTIVE = 100 - (int) player.calcStat(Stats.DEATH_VULNERABILITY, 100.0, (Creature) null, (Skill) null);
		if(DEATH_RECEPTIVE != 0)
			dialog.append("<tr><td>Death</td><td>").append(DEATH_RECEPTIVE).append("%</td></tr>");
		final int STUN_RECEPTIVE = (int) player.calcStat(Stats.STUN_RESIST, (Creature) null, (Skill) null);
		if(STUN_RECEPTIVE != 0)
			dialog.append("<tr><td>Stun</td><td>").append(STUN_RECEPTIVE == Integer.MAX_VALUE ? "Max" : STUN_RECEPTIVE == Integer.MIN_VALUE ? "Min" : STUN_RECEPTIVE + "%").append("</td></tr>");
		final int ROOT_RECEPTIVE = (int) player.calcStat(Stats.ROOT_RESIST, (Creature) null, (Skill) null);
		if(ROOT_RECEPTIVE != 0)
			dialog.append("<tr><td>Root</td><td>").append(ROOT_RECEPTIVE == Integer.MAX_VALUE ? "Max" : ROOT_RECEPTIVE == Integer.MIN_VALUE ? "Min" : ROOT_RECEPTIVE + "%").append("</td></tr>");
		final int SLEEP_RECEPTIVE = (int) player.calcStat(Stats.SLEEP_RESIST, (Creature) null, (Skill) null);
		if(SLEEP_RECEPTIVE != 0)
			dialog.append("<tr><td>Sleep</td><td>").append(SLEEP_RECEPTIVE == Integer.MAX_VALUE ? "Max" : SLEEP_RECEPTIVE == Integer.MIN_VALUE ? "Min" : SLEEP_RECEPTIVE + "%").append("</td></tr>");
		final int PARALYZE_RECEPTIVE = (int) player.calcStat(Stats.PARALYZE_RESIST, (Creature) null, (Skill) null);
		if(PARALYZE_RECEPTIVE != 0)
			dialog.append("<tr><td>Paralyze</td><td>").append(PARALYZE_RECEPTIVE == Integer.MAX_VALUE ? "Max" : PARALYZE_RECEPTIVE == Integer.MIN_VALUE ? "Min" : PARALYZE_RECEPTIVE + "%").append("</td></tr>");
		final int MENTAL_RECEPTIVE = (int) player.calcStat(Stats.MENTAL_RESIST, (Creature) null, (Skill) null);
		if(MENTAL_RECEPTIVE != 0)
			dialog.append("<tr><td>Mental</td><td>").append(MENTAL_RECEPTIVE == Integer.MAX_VALUE ? "Max" : MENTAL_RECEPTIVE == Integer.MIN_VALUE ? "Min" : MENTAL_RECEPTIVE + "%").append("</td></tr>");
		final int DEBUFF_RECEPTIVE = (int) player.calcStat(Stats.DEBUFF_RESIST, (Creature) null, (Skill) null);
		if(DEBUFF_RECEPTIVE != 0)
			dialog.append("<tr><td>Debuff</td><td>").append(DEBUFF_RECEPTIVE == Integer.MAX_VALUE ? "Max" : DEBUFF_RECEPTIVE == Integer.MIN_VALUE ? "Min" : DEBUFF_RECEPTIVE + "%").append("</td></tr>");
		final int CANCEL_RECEPTIVE = (int) player.calcStat(Stats.CANCEL_RESIST, (Creature) null, (Skill) null);
		if(CANCEL_RECEPTIVE != 0)
			dialog.append("<tr><td>Cancel</td><td>").append(CANCEL_RECEPTIVE == Integer.MAX_VALUE ? "Max" : CANCEL_RECEPTIVE == Integer.MIN_VALUE ? "Min" : CANCEL_RECEPTIVE + "%").append("</td></tr>");
		final int SWORD_WPN_RECEPTIVE = 100 - (int) player.calcStat(Stats.SWORD_WPN_RECEPTIVE, 100.0, (Creature) null, (Skill) null);
		if(SWORD_WPN_RECEPTIVE != 0)
			dialog.append("<tr><td>Sword</td><td>").append(SWORD_WPN_RECEPTIVE).append("%</td></tr>");
		final int DUAL_WPN_RECEPTIVE = 100 - (int) player.calcStat(Stats.DUAL_WPN_RECEPTIVE, 100.0, (Creature) null, (Skill) null);
		if(DUAL_WPN_RECEPTIVE != 0)
			dialog.append("<tr><td>Dual Sword</td><td>").append(DUAL_WPN_RECEPTIVE).append("%</td></tr>");
		final int BLUNT_WPN_RECEPTIVE = 100 - (int) player.calcStat(Stats.BLUNT_WPN_RECEPTIVE, 100.0, (Creature) null, (Skill) null);
		if(BLUNT_WPN_RECEPTIVE != 0)
			dialog.append("<tr><td>Blunt</td><td>").append(BLUNT_WPN_RECEPTIVE).append("%</td></tr>");
		final int DAGGER_WPN_RECEPTIVE = 100 - (int) player.calcStat(Stats.DAGGER_WPN_RECEPTIVE, 100.0, (Creature) null, (Skill) null);
		if(DAGGER_WPN_RECEPTIVE != 0)
			dialog.append("<tr><td>Dagger</td><td>").append(DAGGER_WPN_RECEPTIVE).append("%</td></tr>");
		final int BOW_WPN_RECEPTIVE = 100 - (int) player.calcStat(Stats.BOW_WPN_RECEPTIVE, 100.0, (Creature) null, (Skill) null);
		if(BOW_WPN_RECEPTIVE != 0)
			dialog.append("<tr><td>Bow</td><td>").append(BOW_WPN_RECEPTIVE).append("%</td></tr>");
		final int POLE_WPN_RECEPTIVE = 100 - (int) player.calcStat(Stats.POLE_WPN_RECEPTIVE, 100.0, (Creature) null, (Skill) null);
		if(POLE_WPN_RECEPTIVE != 0)
			dialog.append("<tr><td>Polearm</td><td>").append(POLE_WPN_RECEPTIVE).append("%</td></tr>");
		final int FIST_WPN_RECEPTIVE = 100 - (int) player.calcStat(Stats.FIST_WPN_RECEPTIVE, 100.0, (Creature) null, (Skill) null);
		if(FIST_WPN_RECEPTIVE != 0)
			dialog.append("<tr><td>Fist weapons</td><td>").append(FIST_WPN_RECEPTIVE).append("%</td></tr>");
		if(FIRE_RECEPTIVE == 0 && WIND_RECEPTIVE == 0 && WATER_RECEPTIVE == 0 && EARTH_RECEPTIVE == 0 && UNHOLY_RECEPTIVE == 0 && SACRED_RECEPTIVE == 0 && BLEED_RECEPTIVE == 0 && DEATH_RECEPTIVE == 0 && STUN_RECEPTIVE == 0 && POISON_RECEPTIVE == 0 && ROOT_RECEPTIVE == 0 && SLEEP_RECEPTIVE == 0 && PARALYZE_RECEPTIVE == 0 && MENTAL_RECEPTIVE == 0 && DEBUFF_RECEPTIVE == 0 && CANCEL_RECEPTIVE == 0 && SWORD_WPN_RECEPTIVE == 0 && DUAL_WPN_RECEPTIVE == 0 && BLUNT_WPN_RECEPTIVE == 0 && DAGGER_WPN_RECEPTIVE == 0 && BOW_WPN_RECEPTIVE == 0 && POLE_WPN_RECEPTIVE == 0 && FIST_WPN_RECEPTIVE == 0)
			dialog.append("</table>No resists</body></html>");
		else
			dialog.append("</table></body></html>");
		show(dialog.toString(), player);
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return MyInfo._commandList;
	}

	@Override
	public void onLoad()
	{
		VoicedCommandHandler.getInstance().registerVoicedCommandHandler(this);
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	static
	{
		_commandList = new String[] { "myinfo", "atkrange" };
	}
}
