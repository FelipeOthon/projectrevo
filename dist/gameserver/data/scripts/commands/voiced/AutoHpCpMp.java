package commands.voiced;

import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.data.htm.HtmTemplates;
import l2s.gameserver.handler.IVoicedCommandHandler;
import l2s.gameserver.handler.VoicedCommandHandler;
import l2s.gameserver.listener.actor.*;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.Skill.SkillType;
import l2s.gameserver.model.actor.instances.creature.Abnormal;
import l2s.gameserver.model.actor.instances.creature.AbnormalList;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.skills.EffectType;
import l2s.gameserver.skills.effects.EffectTemplate;
import l2s.gameserver.utils.ItemFunctions;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Bonux
**/
public class AutoHpCpMp implements IVoicedCommandHandler, ScriptFile
{
	private static class AutoHpTask implements Runnable {
		@Override
		public void run() {
			for(Player player : HP_PLAYERS) {
				tryUseHpPotion(player);
			}
		}
	}

	private static class AutoMpTask implements Runnable {
		@Override
		public void run() {
			for(Player player : MP_PLAYERS) {
				tryUseMpPotion(player);
			}
		}
	}

	private static class AutoCpTask implements Runnable {
		@Override
		public void run() {
			for(Player player : CP_PLAYERS) {
				tryUseCpPotion(player);
			}
		}
	}

	private static class ChangeCurrentCpListener implements OnChangeCurrentCpListener
	{
		@Override
		public void onChangeCurrentCp(Creature actor, double oldCp, double newCp)
		{
			if(!actor.isPlayer() || actor.isDead())
				return;

			Player player = actor.getPlayer();
			if(canUseCpPotion(player))
				CP_PLAYERS.add(player);
			else
				CP_PLAYERS.remove(player);
		}
	}

	private static boolean canUseCpPotion(Player player) {
		if(!player.getVarBoolean("acp_enabled", true))
			return false;

		int percent = player.getVarInt("autocp", 0);
		double newCp = player.getCurrentCp();
		double currentPercent = newCp / (player.getMaxCp() / 100.);
		if(percent <= 0 || currentPercent < 0 || currentPercent > percent)
			return false;
		return true;
	}

	private static void tryUseCpPotion(Player player) {
		if(!canUseCpPotion(player))
			return;

		ItemInstance effectedItem = null;
		int effectedItemPower = 0;

		ItemInstance instantItem = null;
		int instantItemPower = 0;

		final List<Abnormal> abnormals = player.getAbnormalList().getEffectsByType(EffectType.CombatPointHealOverTime);
		loop: for(ItemInstance item : player.getInventory().getItems())
		{
			Skill skill = item.getTemplate().getFirstSkill();
			if(skill == null)
				continue;

			EffectTemplate[] effectTemplates = skill.getEffectTemplates();
			if(effectTemplates == null)
				continue;

			for(EffectTemplate et : effectTemplates)
			{
				if(et.getEffectType() == EffectType.CombatPointHealOverTime)
				{
					for(Abnormal abnormal : abnormals)
					{
						if(AbnormalList.checkStackType(et, abnormal.getTemplate()) && et._stackOrder <= abnormal.getStackOrder())
						{
							// Не хиляем, если уже наложена какая-либо хилка.
							effectedItem = null;
							effectedItemPower = 0;
							continue loop;
						}
					}

					if(!ItemFunctions.checkUseItem(player, item, false))
						continue loop;

					int power = (int) et._value;
					if(power > effectedItemPower)
					{
						if(skill.checkCondition(player, player, false, false, true, true, false))
						{
							effectedItem = item;
							effectedItemPower = power;
							continue loop;
						}
					}
				}
			}
		}

		loop: for(ItemInstance item : player.getInventory().getItems())
		{
			Skill skill = item.getTemplate().getFirstSkill();
			if(skill == null)
				continue;

			if(!ItemFunctions.checkUseItem(player, item, false))
				continue;

			EffectTemplate[] effectTemplates = skill.getEffectTemplates();
			if(effectTemplates != null) {
				for (EffectTemplate et : effectTemplates) {
					if (/*et.getEffectType() == EffectType.HealCP || */et.getEffectType() == EffectType.CPHealPercent) {
						int power = (int) et._value;
						if (et.getEffectType() == EffectType.CPHealPercent)
							power = power * (player.getMaxCp() / 100);
						if (power > instantItemPower) {
							if (skill.checkCondition(player, player, false, false, true, true, false)) {
								instantItem = item;
								instantItemPower = power;
								continue loop;
							}
						}
					}
				}
			}

			if(skill.getSkillType() == SkillType.COMBATPOINTHEAL/* || skill.getSkillType() == SkillType.COMBATPOINTHEAL_PERCENT*/)
			{
				int power = (int) skill.getPower();
				/*if(skill.getSkillType() == SkillType.COMBATPOINTHEAL_PERCENT)
					power = power * (player.getMaxCp() / 100);*/
				if(power > instantItemPower)
				{
					if(skill.checkCondition(player, player, false, false, true, true, false))
					{
						instantItem = item;
						instantItemPower = power;
						continue loop;
					}
				}
			}
		}

		if(instantItem != null)
			useItem(player, instantItem);

		if(effectedItem != null)
		{
			int percent = player.getVarInt("autocp", 0);
			if(instantItemPower == 0 || percent >= (player.getCurrentCp() + instantItemPower) / (player.getMaxCp() / 100.))
				useItem(player, effectedItem);
		}
	}

	private static class ChangeCurrentHpListener implements OnChangeCurrentHpListener
	{
		@Override
		public void onChangeCurrentHp(Creature actor, double oldHp, double newHp)
		{
			if(!actor.isPlayer() || actor.isDead())
				return;

			Player player = actor.getPlayer();
			if(canUseHpPotion(player))
				HP_PLAYERS.add(player);
			else
				HP_PLAYERS.remove(player);
		}
	}

	private static boolean canUseHpPotion(Player player) {
		if(!player.getVarBoolean("acp_enabled", true))
			return false;

		int percent = player.getVarInt("autohp", 0);
		double newHp = player.getCurrentHp();
		double currentPercent = newHp / (player.getMaxHp() / 100.);
		if(percent <= 0 || currentPercent <= 0 || currentPercent > percent)
			return false;
		return true;
	}

	private static void tryUseHpPotion(Player player) {
		if(!canUseHpPotion(player))
			return;

		ItemInstance effectedItem = null;
		int effectedItemPower = 0;

		ItemInstance instantItem = null;
		int instantItemPower = 0;

		final List<Abnormal> effects = player.getAbnormalList().getEffectsByType(EffectType.HealOverTime);
		loop: for(ItemInstance item : player.getInventory().getItems())
		{
			Skill skill = item.getTemplate().getFirstSkill();
			if(skill == null)
				continue;

			EffectTemplate[] effectTemplates = skill.getEffectTemplates();
			if(effectTemplates == null)
				continue;

			for(EffectTemplate et : effectTemplates)
			{
				if(et.getEffectType() == EffectType.HealOverTime)
				{
					for(Abnormal abnormal : effects)
					{
						if(AbnormalList.checkStackType(et, abnormal.getTemplate()) && et._stackOrder <= abnormal.getStackOrder())
						{
							// Не хиляем, если уже наложена какая-либо хилка.
							effectedItem = null;
							effectedItemPower = 0;
							continue loop;
						}
					}

					if(!ItemFunctions.checkUseItem(player, item, false))
						continue loop;

					int power = (int) et._value;
					if(power > effectedItemPower)
					{
						if(skill.checkCondition(player, player, false, false, true, true, false))
						{
							effectedItem = item;
							effectedItemPower = power;
							continue loop;
						}
					}
				}
			}
		}

		loop: for(ItemInstance item : player.getInventory().getItems())
		{
			Skill skill = item.getTemplate().getFirstSkill();
			if(skill == null)
				continue;

			if(!ItemFunctions.checkUseItem(player, item, false))
				continue;

			EffectTemplate[] effectTemplates = skill.getEffectTemplates();
			if(effectTemplates != null) {
				for (EffectTemplate et : effectTemplates) {
					if (et.getEffectType() == EffectType.Heal || et.getEffectType() == EffectType.HealPercent) {
						int power = (int) et._value;
						if (et.getEffectType() == EffectType.HealPercent)
							power = power * (player.getMaxHp() / 100);
						if (power > instantItemPower) {
							if (skill.checkCondition(player, player, false, false, true, true, false)) {
								instantItem = item;
								instantItemPower = power;
								continue loop;
							}
						}
					}
				}
			}

			if(skill.getSkillType() == SkillType.HEAL || skill.getSkillType() == SkillType.HEAL_PERCENT)
			{
				int power = (int) skill.getPower();
				if(skill.getSkillType() == SkillType.HEAL_PERCENT)
					power = power * (player.getMaxHp() / 100);
				if(power > instantItemPower)
				{
					if(skill.checkCondition(player, player, false, false, true, true, false))
					{
						instantItem = item;
						instantItemPower = power;
						continue loop;
					}
				}
			}
		}

		if(instantItem != null)
			useItem(player, instantItem);

		if(effectedItem != null)
		{
			int percent = player.getVarInt("autohp", 0);
			if(instantItemPower == 0 || percent >= (player.getCurrentHp() + instantItemPower) / (player.getMaxHp() / 100.))
				useItem(player, effectedItem);
		}
	}

	private static class ChangeCurrentMpListener implements OnChangeCurrentMpListener {
		@Override
		public void onChangeCurrentMp(Creature actor, double oldMp, double newMp) {
			if(!actor.isPlayer() || actor.isDead())
				return;

			Player player = actor.getPlayer();
			if(canUseMpPotion(player))
				MP_PLAYERS.add(player);
			else
				MP_PLAYERS.remove(player);
		}
	}

	private static boolean canUseMpPotion(Player player) {
		if(!player.getVarBoolean("acp_enabled", true))
			return false;

		int percent = player.getVarInt("automp", 0);
		double newMp = player.getCurrentMp();
		double currentPercent = newMp / (player.getMaxMp() / 100.);
		if(percent <= 0 || currentPercent < 0 || currentPercent > percent)
			return false;
		return true;
	}

	private static void tryUseMpPotion(Player player) {
		if(!canUseMpPotion(player))
			return;

		ItemInstance effectedItem = null;
		int effectedItemPower = 0;

		ItemInstance instantItem = null;
		int instantItemPower = 0;

		final List<Abnormal> abnormals = player.getAbnormalList().getEffectsByType(EffectType.ManaHealOverTime);
		loop: for(ItemInstance item : player.getInventory().getItems())
		{
			Skill skill = item.getTemplate().getFirstSkill();
			if(skill == null)
				continue;

			EffectTemplate[] effectTemplates = skill.getEffectTemplates();
			if(effectTemplates == null)
				continue;

			for(EffectTemplate et : effectTemplates)
			{
				if(et.getEffectType() == EffectType.ManaHealOverTime)
				{
					for(Abnormal abnormal : abnormals)
					{
						if(AbnormalList.checkStackType(et, abnormal.getTemplate()) && et._stackOrder <= abnormal.getStackOrder())
						{
							// Не хиляем, если уже наложена какая-либо хилка.
							effectedItem = null;
							effectedItemPower = 0;
							continue loop;
						}
					}

					if(!ItemFunctions.checkUseItem(player, item, false))
						continue loop;

					int power = (int) et._value;
					if(power > effectedItemPower)
					{
						if(skill.checkCondition(player, player, false, false, true, true, false))
						{
							effectedItem = item;
							effectedItemPower = power;
							continue loop;
						}
					}
				}
			}
		}

		loop: for(ItemInstance item : player.getInventory().getItems())
		{
			Skill skill = item.getTemplate().getFirstSkill();
			if(skill == null)
				continue;

			if(!ItemFunctions.checkUseItem(player, item, false))
				continue;

			EffectTemplate[] effectTemplates = skill.getEffectTemplates();
			if(effectTemplates != null) {
				for (EffectTemplate et : effectTemplates) {
					if (et.getEffectType() == EffectType.ManaHeal || et.getEffectType() == EffectType.ManaHealPercent) {
						int power = (int) et._value;
						if (et.getEffectType() == EffectType.ManaHealPercent)
							power = power * (player.getMaxMp() / 100);
						if (power > instantItemPower) {
							if (skill.checkCondition(player, player, false, false, true, true, false)) {
								instantItem = item;
								instantItemPower = power;
								continue loop;
							}
						}
					}
				}
			}

			if(skill.getSkillType() == SkillType.MANAHEAL || skill.getSkillType() == SkillType.MANAHEAL_PERCENT)
			{
				int power = (int) skill.getPower();
				if(skill.getSkillType() == SkillType.MANAHEAL_PERCENT)
					power = power * (player.getMaxMp() / 100);
				if(power > instantItemPower)
				{
					if(skill.checkCondition(player, player, false, false, true, true, false))
					{
						instantItem = item;
						instantItemPower = power;
						continue loop;
					}
				}
			}
		}

		if(instantItem != null)
			useItem(player, instantItem);

		if(effectedItem != null)
		{
			int percent = player.getVarInt("automp", 0);
			if(instantItemPower == 0 || percent >= (player.getCurrentMp() + instantItemPower) / (player.getMaxMp() / 100.))
				useItem(player, effectedItem);
		}
	}

	private static class PlayerEnterListener implements OnPlayerEnterListener
	{
		@Override
		public void onPlayerEnter(Player player)
		{
			if(!Config.ALLOW_VOICED_COMMANDS || !Config.ALLOW_AUTOHEAL_COMMANDS)
				return;

			int percent = player.getVarInt("autocp", 0);
			if(percent > 0)
			{
				player.addListener(CHANGE_CURRENT_CP_LISTENER);
				if(player.isLangRus())
					player.sendMessage("Вы используете систему автоматического восстановления CP. Ваше CP будет автоматически восстанавливаться при значении " + percent + "% и меньше.");
				else
					player.sendMessage("You are using an automatic CP recovery. Your CP will automatically recover at a value of " + percent + "% or less.");
			}
			percent = player.getVarInt("autohp", 0);
			if(percent > 0)
			{
				player.addListener(CHANGE_CURRENT_HP_LISTENER);
				if(player.isLangRus())
					player.sendMessage("Вы используете систему автоматического восстановления HP. Ваше HP будет автоматически восстанавливаться при значении " + percent + "% и меньше.");
				else
					player.sendMessage("You are using an automatic HP recovery. Your HP will automatically recover at a value of " + percent + "% or less.");
			}
			percent = player.getVarInt("automp", 0);
			if(percent > 0)
			{
				player.addListener(CHANGE_CURRENT_MP_LISTENER);
				if(player.isLangRus())
					player.sendMessage("Вы используете систему автоматического восстановления MP. Ваше MP будет автоматически восстанавливаться при значении " + percent + "% и меньше.");
				else
					player.sendMessage("You are using an automatic MP recovery. Your MP will automatically recover at a value of " + percent + "% or less.");
			}
		}
	}

	private static final OnChangeCurrentCpListener CHANGE_CURRENT_CP_LISTENER = new ChangeCurrentCpListener();
	private static final OnChangeCurrentHpListener CHANGE_CURRENT_HP_LISTENER = new ChangeCurrentHpListener();
	private static final OnChangeCurrentMpListener CHANGE_CURRENT_MP_LISTENER = new ChangeCurrentMpListener();
	private static final OnPlayerEnterListener PLAYER_ENTER_LISTENER = new PlayerEnterListener();

	private static final String[] COMMANDS_LIST = new String[] { "acp", "autocp", "autohp", "automp" };

	private static final Set<Player> HP_PLAYERS = new CopyOnWriteArraySet<>();
	private static final Set<Player> MP_PLAYERS = new CopyOnWriteArraySet<>();
	private static final Set<Player> CP_PLAYERS = new CopyOnWriteArraySet<>();

	private static ScheduledFuture<?> AUTO_HP_TASK = null;
	private static ScheduledFuture<?> AUTO_MP_TASK = null;
	private static ScheduledFuture<?> AUTO_CP_TASK = null;

	@Override
	public String[] getVoicedCommandList()
	{
		return COMMANDS_LIST;
	}

	@Override
	public boolean useVoicedCommand(String command, Player activeChar, String args)
	{
		if(!Config.ALLOW_VOICED_COMMANDS || !Config.ALLOW_AUTOHEAL_COMMANDS)
			return false;

		if(command.equalsIgnoreCase("acp"))
		{
			int autoHp = activeChar.getVarInt("autohp", 0);
			int autoMp = activeChar.getVarInt("automp", 0);
			int autoCp = activeChar.getVarInt("autocp", 0);
			try
			{
				String[] params = args.split("\\s+");
				if(params[0].equalsIgnoreCase("hp"))
				{
					int newAutoHp = Math.min(99, Integer.parseInt(params[1]));
					if(newAutoHp != autoHp)
					{
						if(newAutoHp > 0)
						{
							activeChar.setVar("autohp", newAutoHp, -1);
							if(autoHp > 0)
							{
								if(activeChar.isLangRus())
									activeChar.sendMessage("Ваше HP будет автоматически восстанавливаться при значении " + newAutoHp + "% и меньше.");
								else
									activeChar.sendMessage("Your HP will automatically recover at a value of " + newAutoHp + "% or less.");
							}
							else
							{
								activeChar.addListener(CHANGE_CURRENT_HP_LISTENER);
								if(activeChar.isLangRus())
									activeChar.sendMessage("Вы включили систему автоматического восстановления HP. Ваше HP будет автоматически восстанавливаться при значении " + newAutoHp + "% и меньше.");
								else
									activeChar.sendMessage("You have enabled an automatic HP recovery. Your HP will automatically recover at a value of " + newAutoHp + "% or less.");
							}
						}
						else
						{
							activeChar.unsetVar("autohp");
							activeChar.removeListener(CHANGE_CURRENT_HP_LISTENER);
							if(activeChar.isLangRus())
								activeChar.sendMessage("Система автоматического восстановления HP отключена.");
							else
								activeChar.sendMessage("HP automatic recovery system disabled.");
						}
						autoHp = newAutoHp;
					}
				}
				else if(params[0].equalsIgnoreCase("mp"))
				{
					int newAutoMp = Math.min(99, Integer.parseInt(params[1]));
					if(newAutoMp != autoMp)
					{
						if(newAutoMp > 0)
						{
							activeChar.setVar("automp", newAutoMp, -1);
							if(autoMp > 0)
							{
								if(activeChar.isLangRus())
									activeChar.sendMessage("Ваше MP будет автоматически восстанавливаться при значении " + newAutoMp + "% и меньше.");
								else
									activeChar.sendMessage("Your MP will automatically recover at a value of " + newAutoMp + "% or less.");
							}
							else
							{
								activeChar.addListener(CHANGE_CURRENT_MP_LISTENER);
								if(activeChar.isLangRus())
									activeChar.sendMessage("Вы включили систему автоматического восстановления MP. Ваше MP будет автоматически восстанавливаться при значении " + newAutoMp + "% и меньше.");
								else
									activeChar.sendMessage("You have enabled an automatic MP recovery. Your MP will automatically recover at a value of " + newAutoMp + "% or less.");
							}
						}
						else
						{
							activeChar.unsetVar("automp");
							activeChar.removeListener(CHANGE_CURRENT_MP_LISTENER);
							if(activeChar.isLangRus())
								activeChar.sendMessage("Система автоматического восстановления MP отключена.");
							else
								activeChar.sendMessage("MP automatic recovery system disabled.");
						}
						autoMp = newAutoMp;
					}
				}
				else if(params[0].equalsIgnoreCase("cp"))
				{
					int newAutoCp = Math.min(99, Integer.parseInt(params[1]));
					if(newAutoCp != autoCp)
					{
						if(newAutoCp > 0)
						{
							activeChar.setVar("autocp", newAutoCp, -1);
							if(autoCp > 0)
							{
								if(activeChar.isLangRus())
									activeChar.sendMessage("Ваше CP будет автоматически восстанавливаться при значении " + newAutoCp + "% и меньше.");
								else
									activeChar.sendMessage("Your CP will automatically recover at a value of " + newAutoCp + "% or less.");
							}
							else
							{
								activeChar.addListener(CHANGE_CURRENT_CP_LISTENER);
								if(activeChar.isLangRus())
									activeChar.sendMessage("Вы включили систему автоматического восстановления CP. Ваше CP будет автоматически восстанавливаться при значении " + newAutoCp + "% и меньше.");
								else
									activeChar.sendMessage("You have enabled an automatic CP recovery. Your CP will automatically recover at a value of " + newAutoCp + "% or less.");
							}
						}
						else
						{
							activeChar.unsetVar("autocp");
							activeChar.removeListener(CHANGE_CURRENT_CP_LISTENER);
							if(activeChar.isLangRus())
								activeChar.sendMessage("Система автоматического восстановления CP отключена.");
							else
								activeChar.sendMessage("CP automatic recovery system disabled.");
						}
						autoCp = newAutoCp;
					}
				}
			}
			catch(Exception e)
			{
				//
			}
			HtmTemplates tpls = HtmCache.getInstance().getTemplates("command/acp.htm", activeChar);
			String html = tpls.get(0);
			html = html.replace("<?auto_hp?>", autoHp > 0 ? autoHp + "%" : tpls.get(1));
			html = html.replace("<?auto_mp?>", autoMp > 0 ? autoMp + "%" : tpls.get(1));
			html = html.replace("<?auto_cp?>", autoCp > 0 ? autoCp + "%" : tpls.get(1));
			Functions.show(html, activeChar);
		}
		else if(command.equalsIgnoreCase("autocp"))
		{
			int percent;
			try
			{
				percent = Math.min(99, Integer.parseInt(args));
			}
			catch(NumberFormatException e)
			{
				if(activeChar.isLangRus())
					activeChar.sendMessage("Неверное использование комманды! Используйте: .autocp [ПРОЦЕНТ_CP_ДЛЯ_НАЧАЛА_ВОССТАНОВЛЕНИЯ]");
				else
					activeChar.sendMessage("Incorrect use commands! Use: .autocp [CP_PERCENT_FOR EARLY_RECOVERY]");
				return false;
			}
			if(percent <= 0)
			{
				if(activeChar.getVarInt("autocp", 0) > 0)
				{
					activeChar.removeListener(CHANGE_CURRENT_CP_LISTENER);
					activeChar.unsetVar("autocp");
					if(activeChar.isLangRus())
						activeChar.sendMessage("Система автоматического восстановления CP отключена.");
					else
						activeChar.sendMessage("CP automatic recovery system disabled.");
				}
				else
				{
					if(activeChar.isLangRus())
						activeChar.sendMessage("Нельзя указать нулевое или отрицательное значение!");
					else
						activeChar.sendMessage("You can not specify zero or negative value!");
				}
				return false;
			}
			activeChar.addListener(CHANGE_CURRENT_CP_LISTENER);
			activeChar.setVar("autocp", percent, -1);
			if(activeChar.isLangRus())
				activeChar.sendMessage("Вы включили систему автоматического восстановления CP. Ваше CP будет автоматически восстанавливаться при значении " + percent + "% и меньше.");
			else
				activeChar.sendMessage("You have enabled an automatic CP recovery. Your CP will automatically recover at a value of " + percent + "% or less.");
			return true;
		}
		else if(command.equalsIgnoreCase("autohp"))
		{
			int percent;
			try
			{
				percent = Math.min(99, Integer.parseInt(args));
			}
			catch(NumberFormatException e)
			{
				if(activeChar.isLangRus())
					activeChar.sendMessage("Неверное использование комманды! Используйте: .autohp [ПРОЦЕНТ_HP_ДЛЯ_НАЧАЛА_ВОССТАНОВЛЕНИЯ]");
				else
					activeChar.sendMessage("Incorrect use commands! Use: .autohp [HP_PERCENT_FOR EARLY_RECOVERY]");
				return false;
			}
			if(percent <= 0)
			{
				if(activeChar.getVarInt("autohp", 0) > 0)
				{
					activeChar.removeListener(CHANGE_CURRENT_HP_LISTENER);
					activeChar.unsetVar("autohp");
					if(activeChar.isLangRus())
						activeChar.sendMessage("Система автоматического восстановления HP отключена.");
					else
						activeChar.sendMessage("HP automatic recovery system disabled.");
				}
				else
				{
					if(activeChar.isLangRus())
						activeChar.sendMessage("Нельзя указать нулевое или отрицательное значение!");
					else
						activeChar.sendMessage("You can not specify zero or negative value!");
				}
				return false;
			}
			activeChar.addListener(CHANGE_CURRENT_HP_LISTENER);
			activeChar.setVar("autohp", percent, -1);
			if(activeChar.isLangRus())
				activeChar.sendMessage("Вы включили систему автоматического восстановления HP. Ваше HP будет автоматически восстанавливаться при значении " + percent + "% и меньше.");
			else
				activeChar.sendMessage("You have enabled an automatic HP recovery. Your HP will automatically recover at a value of " + percent + "% or less.");
			return true;
		}
		else if(command.equalsIgnoreCase("automp"))
		{
			int percent;
			try
			{
				percent = Math.min(99, Integer.parseInt(args));
			}
			catch(NumberFormatException e)
			{
				if(activeChar.isLangRus())
					activeChar.sendMessage("Неверное использование комманды! Используйте: .automp [ПРОЦЕНТ_MP_ДЛЯ_НАЧАЛА_ВОССТАНОВЛЕНИЯ]");
				else
					activeChar.sendMessage("Incorrect use commands! Use: .automp [MP_PERCENT_FOR EARLY_RECOVERY]");
				return false;
			}
			if(percent <= 0)
			{
				if(activeChar.getVarInt("automp", 0) > 0)
				{
					activeChar.removeListener(CHANGE_CURRENT_MP_LISTENER);
					activeChar.unsetVar("automp");
					if(activeChar.isLangRus())
						activeChar.sendMessage("Система автоматического восстановления MP отключена.");
					else
						activeChar.sendMessage("MP automatic recovery system disabled.");
				}
				else
				{
					if(activeChar.isLangRus())
						activeChar.sendMessage("Нельзя указать нулевое или отрицательное значение!");
					else
						activeChar.sendMessage("You can not specify zero or negative value!");
					return false;
				}
			}
			activeChar.addListener(CHANGE_CURRENT_MP_LISTENER);
			activeChar.setVar("automp", percent, -1);
			if(activeChar.isLangRus())
				activeChar.sendMessage("Вы включили систему автоматического восстановления MP. Ваше MP будет автоматически восстанавливаться при значении " + percent + "% и меньше.");
			else
				activeChar.sendMessage("You have enabled an automatic MP recovery. Your MP will automatically recover at a value of " + percent + "% or less.");
			return true;
		}
		return false;
	}

	@Override
	public void onLoad()
	{
		CharListenerList.addGlobal(PLAYER_ENTER_LISTENER);
		VoicedCommandHandler.getInstance().registerVoicedCommandHandler(this);
		AUTO_HP_TASK = ThreadPoolManager.getInstance().scheduleAtFixedRate(new AutoHpTask(), 300L, 300L);
		AUTO_MP_TASK = ThreadPoolManager.getInstance().scheduleAtFixedRate(new AutoMpTask(), 300L, 300L);
		AUTO_CP_TASK = ThreadPoolManager.getInstance().scheduleAtFixedRate(new AutoCpTask(), 300L, 300L);
	}

	@Override
	public void onReload()
	{
		//
	}

	@Override
	public void onShutdown()
	{
		//
	}

	private static void useItem(Player player, ItemInstance item)
	{
		// Запускаем в новом потоке, потому что итем может юзнуться несколько раз проигнорировав откат итема
		ThreadPoolManager.getInstance().execute(() -> player.useItem(item, false, false));
	}
}