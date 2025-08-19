package items;

import l2s.gameserver.handler.items.IItemHandler;
import l2s.gameserver.handler.items.ItemHandler;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.templates.item.ItemGrade;
import l2s.gameserver.templates.item.WeaponTemplate;

public class FishShots implements IItemHandler, ScriptFile
{
	private static int[] _itemIds;
	private static int[] _skillIds;
	static final SystemMessage THIS_FISHING_SHOT_IS_NOT_FIT_FOR_THE_FISHING_POLE_CRYSTAL;
	static final SystemMessage POWER_OF_MANA_ENABLED;

	@Override
	public boolean useItem(final Playable playable, final ItemInstance item, final Boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
			return false;
		final Player player = (Player) playable;
		final int FishshotId = item.getItemId();
		boolean isAutoSoulShot = false;
		if(player.getAutoSoulShot().contains(FishshotId))
			isAutoSoulShot = true;
		final ItemInstance weaponInst = player.getActiveWeaponInstance();
		final WeaponTemplate weaponItem = player.getActiveWeaponItem();
		if(weaponInst == null || weaponItem.getItemType() != WeaponTemplate.WeaponType.ROD)
		{
			if(isAutoSoulShot)
				player.removeAutoSoulShot(FishshotId);
			return true;
		}
		if(item.getIntegerLimitedCount() < 1)
		{
			if(isAutoSoulShot)
				player.removeAutoSoulShot(FishshotId);
			return true;
		}
		if(weaponInst.getChargedFishshot())
			return true;
		final ItemGrade grade = weaponItem.getItemGrade();
		if((grade != ItemGrade.NONE || FishshotId == 6535) && (grade != ItemGrade.D || FishshotId == 6536) && (grade != ItemGrade.C || FishshotId == 6537) && (grade != ItemGrade.B || FishshotId == 6538) && (grade != ItemGrade.A || FishshotId == 6539) && (grade != ItemGrade.S || FishshotId == 6540))
		{
			weaponInst.setChargedFishshot(true);
			player.getInventory().destroyItem(item.getObjectId(), 1L, false);
			player.sendPacket(FishShots.POWER_OF_MANA_ENABLED);
			player.broadcastPacket(new L2GameServerPacket[] { new MagicSkillUse(player, player, FishShots._skillIds[grade.ordinal()], 1, 0, 0L) });
			return true;
		}
		if(isAutoSoulShot)
			return true;
		player.sendPacket(FishShots.THIS_FISHING_SHOT_IS_NOT_FIT_FOR_THE_FISHING_POLE_CRYSTAL);
		return true;
	}

	@Override
	public int[] getItemIds()
	{
		return FishShots._itemIds;
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

	static
	{
		FishShots._itemIds = new int[] { 6535, 6536, 6537, 6538, 6539, 6540 };
		FishShots._skillIds = new int[] { 2181, 2182, 2183, 2184, 2185, 2186 };
		THIS_FISHING_SHOT_IS_NOT_FIT_FOR_THE_FISHING_POLE_CRYSTAL = new SystemMessage(1479);
		POWER_OF_MANA_ENABLED = new SystemMessage(533);
	}
}
