package items;

import l2s.gameserver.Config;
import l2s.gameserver.handler.items.IItemHandler;
import l2s.gameserver.handler.items.ItemHandler;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.ExAutoSoulShot;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.ItemTable;
import l2s.gameserver.templates.item.ItemGrade;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.templates.item.WeaponTemplate;

public class SpiritShot implements IItemHandler, ScriptFile
{
	private static final int[] _itemIds = new int[] { 5790, 2509, 2510, 2511, 2512, 2513, 2514 };
	private static final short[] _skillIds = new short[] { 2061, 2155, 2156, 2157, 2158, 2159, 2159, 2159 };
	static final SystemMessage POWER_OF_MANA_ENABLED = new SystemMessage(533);
	static final SystemMessage NOT_ENOUGH_SPIRITSHOTS = new SystemMessage(531);
	static final SystemMessage SPIRITSHOT_DOES_NOT_MATCH_WEAPON_GRADE = new SystemMessage(530);
	static final SystemMessage CANNOT_USE_SPIRITSHOTS = new SystemMessage(532);

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
				player.sendPacket(CANNOT_USE_SPIRITSHOTS);
			return true;
		}

		if(weaponInst.getChargedSpiritshot() != ItemInstance.CHARGED_NONE)
			return true;

		final int SpiritshotId = item.getItemId();
		final ItemGrade grade = weaponItem.getItemGrade();
		final int soulSpiritConsumption = Config.INFINITY_SS ? 1 : weaponItem.getSpiritShotCount();
		final int count = item.getIntegerLimitedCount();
		final ItemTemplate itemTemplate = item.getTemplate();
		if(soulSpiritConsumption == 0)
		{
			if(isAutoSoulShot)
			{
				player.removeAutoSoulShot(SoulshotId);
				player.sendPacket(new ExAutoSoulShot(SoulshotId, false));
				player.sendPacket(new SystemMessage(1434).addString(itemTemplate.getName()));
				return true;
			}
			player.sendPacket(CANNOT_USE_SPIRITSHOTS);
			return true;
		}
		else if(grade == ItemGrade.NONE && SpiritshotId != 5790 && SpiritshotId != 2509 || grade == ItemGrade.D && SpiritshotId != 2510 || grade == ItemGrade.C && SpiritshotId != 2511 || grade == ItemGrade.B && SpiritshotId != 2512 || grade == ItemGrade.A && SpiritshotId != 2513 || grade == ItemGrade.S && SpiritshotId != 2514)
		{
			if(isAutoSoulShot)
				return true;

			player.sendPacket(SPIRITSHOT_DOES_NOT_MATCH_WEAPON_GRADE);
			return true;
		}
		else
		{
			if(count >= soulSpiritConsumption)
			{
				weaponInst.setChargedSpiritshot(ItemInstance.CHARGED_SPIRITSHOT);

				if(!Config.INFINITY_SS)
					player.getInventory().destroyItem(item, soulSpiritConsumption, false);

				player.sendPacket(POWER_OF_MANA_ENABLED);
				player.broadcastPacket(new MagicSkillUse(player, player, _skillIds[grade.ordinal()], 1, 0, 0L));
				return true;
			}

			if(isAutoSoulShot)
			{
				player.removeAutoSoulShot(SoulshotId);
				player.sendPacket(new ExAutoSoulShot(SoulshotId, false));
				player.sendPacket(new SystemMessage(1434).addString(itemTemplate.getName()));
				return false;
			}
			player.sendPacket(NOT_ENOUGH_SPIRITSHOTS);
			return false;
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
