package items;

import l2s.gameserver.Config;
import l2s.gameserver.cache.Msg;
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

public class BlessedSpiritShot implements IItemHandler, ScriptFile
{
	private static final int[] _itemIds = new int[] { 3947, 3948, 3949, 3950, 3951, 3952 };
	private static final short[] _skillIds = new short[] { 2061, 2160, 2161, 2162, 2163, 2164, 2164, 2164 };
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
		if(player.isInOlympiadMode())
		{
			player.sendPacket(Msg.THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT);
			return false;
		}

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

		final int spiritshotId = item.getItemId();
		final ItemGrade grade = weaponItem.getItemGrade();
		final int blessedsoulSpiritConsumption = Config.INFINITY_SS ? 1 : weaponItem.getSpiritShotCount();
		final int count = item.getIntegerLimitedCount();
		final ItemTemplate itemTemplate = item.getTemplate();
		if(blessedsoulSpiritConsumption == 0)
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
		else if(grade == ItemGrade.NONE && spiritshotId != 3947 || grade == ItemGrade.D && spiritshotId != 3948 || grade == ItemGrade.C && spiritshotId != 3949 || grade == ItemGrade.B && spiritshotId != 3950 || grade == ItemGrade.A && spiritshotId != 3951 || grade == ItemGrade.S && spiritshotId != 3952)
		{
			if(isAutoSoulShot)
				return true;

			player.sendPacket(SPIRITSHOT_DOES_NOT_MATCH_WEAPON_GRADE);
			return true;
		}
		else
		{
			if(count >= blessedsoulSpiritConsumption)
			{
				weaponInst.setChargedSpiritshot(ItemInstance.CHARGED_BLESSED_SPIRITSHOT);

				if(!Config.INFINITY_SS)
					player.getInventory().destroyItem(item, blessedsoulSpiritConsumption, false);

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
