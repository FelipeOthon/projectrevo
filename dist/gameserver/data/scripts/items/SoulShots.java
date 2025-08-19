package items;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.handler.items.IItemHandler;
import l2s.gameserver.handler.items.ItemHandler;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.ExAutoSoulShot;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.skills.Stats;
import l2s.gameserver.tables.ItemTable;
import l2s.gameserver.templates.item.ItemGrade;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.templates.item.WeaponTemplate;

public class SoulShots implements IItemHandler, ScriptFile
{
	private static final int[] _itemIds = new int[] { 5789, 1835, 1463, 1464, 1465, 1466, 1467 };
	private static final short[] _skillIds = new short[] { 2039, 2150, 2151, 2152, 2153, 2154, 2154, 2154 };
	static final SystemMessage POWER_OF_THE_SPIRITS_ENABLED = new SystemMessage(342);
	static final SystemMessage NOT_ENOUGH_SOULSHOTS = new SystemMessage(338);
	static final SystemMessage SOULSHOT_DOES_NOT_MATCH_WEAPON_GRADE = new SystemMessage(337);
	static final SystemMessage CANNOT_USE_SOULSHOTS = new SystemMessage(339);

	@Override
	public boolean useItem(final Playable playable, final ItemInstance item, final Boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
			return true;

		Player player = playable.getPlayer();
		final ItemInstance weaponInst = player.getActiveWeaponInstance();
		final WeaponTemplate weaponItem = player.getActiveWeaponItem();
		final int SoulshotId = item.getItemId();
		boolean isAutoSoulShot = false;

		if(player.getAutoSoulShot().contains(SoulshotId))
			isAutoSoulShot = true;

		if(weaponInst == null)
		{
			if(!isAutoSoulShot)
				player.sendPacket(CANNOT_USE_SOULSHOTS);
			return true;
		}

		if(weaponInst.getChargedSoulshot() != ItemInstance.CHARGED_NONE)
			return true;

		final ItemGrade grade = weaponItem.getItemGrade();
		int soulShotConsumption = Config.INFINITY_SS ? 1 : weaponItem.getSoulShotCount();
		final int count = item.getIntegerLimitedCount();
		final ItemTemplate itemTemplate = item.getTemplate();
		if(soulShotConsumption == 0)
		{
			if(isAutoSoulShot)
			{
				player.removeAutoSoulShot(SoulshotId);
				player.sendPacket(new ExAutoSoulShot(SoulshotId, false));
				player.sendPacket(new SystemMessage(1434).addString(itemTemplate.getName()));
				return true;
			}
			player.sendPacket(CANNOT_USE_SOULSHOTS);
			return true;
		}
		else if(grade == ItemGrade.NONE && SoulshotId != 5789 && SoulshotId != 1835 || grade == ItemGrade.D && SoulshotId != 1463 || grade == ItemGrade.C && SoulshotId != 1464 || grade == ItemGrade.B && SoulshotId != 1465 || grade == ItemGrade.A && SoulshotId != 1466 || grade == ItemGrade.S && SoulshotId != 1467)
		{
			if(isAutoSoulShot)
				return true;

			player.sendPacket(SOULSHOT_DOES_NOT_MATCH_WEAPON_GRADE);
			return true;
		}
		else
		{
			if(weaponItem.getItemType() == WeaponTemplate.WeaponType.BOW)
			{
				final int newSS = (int) player.calcStat(Stats.SS_USE_BOW, soulShotConsumption, (Creature) null, (Skill) null);
				if(newSS < soulShotConsumption && Rnd.chance(player.calcStat(Stats.SS_USE_BOW_CHANCE, soulShotConsumption, (Creature) null, (Skill) null)))
					soulShotConsumption = newSS;
			}

			if(count < soulShotConsumption)
			{
				player.sendPacket(NOT_ENOUGH_SOULSHOTS);
				return false;
			}

			weaponInst.setChargedSoulshot(ItemInstance.CHARGED_SOULSHOT);

			if(!Config.INFINITY_SS)
				player.getInventory().destroyItem(item, soulShotConsumption, false);

			player.sendPacket(POWER_OF_THE_SPIRITS_ENABLED);
			player.broadcastPacket(new MagicSkillUse(player, player, _skillIds[grade.ordinal()], 1, 0, 0L));
			return true;
		}
	}

	@Override
	public final int[] getItemIds()
	{
		return _itemIds;
	}

	@Override
	public void onLoad()
	{
		ItemHandler.getInstance().registerItemHandler(this);
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}
}
