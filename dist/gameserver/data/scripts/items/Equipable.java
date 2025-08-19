package items;

import l2s.gameserver.Config;
import l2s.gameserver.handler.items.IItemHandler;
import l2s.gameserver.handler.items.ItemHandler;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.ItemTable;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.utils.TimeUtils;

import org.napile.primitive.sets.IntSet;
import org.napile.primitive.sets.impl.HashIntSet;

/**
 * @author Bonux
**/
public class Equipable implements IItemHandler, ScriptFile
{
	private int[] _itemIds;

	public Equipable()
	{
		IntSet set = new HashIntSet();
		for(ItemTemplate template : ItemTable.getInstance().getAllTemplates())
		{
			if(template != null && template.isEquipable())
				set.add(template.getItemId());
		}
		_itemIds = set.toArray();
	}

	@Override
	public boolean useItem(Playable playable, ItemInstance item, Boolean ctrl)
	{
		if(!playable.isPlayer())
			return false;

		Player player = playable.getPlayer();
		if(player.isCastingNow())
		{
			player.sendPacket(new SystemMessage(SystemMessage.YOU_CANNOT_USE_EQUIPMENT_WHEN_USING_OTHER_SKILLS_OR_MAGIC));
			return false;
		}

		if(player.isStunned() || player.isSleeping() || player.isParalyzed() || player.isAlikeDead())
		{
			player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(item.getItemId()));
			return false;
		}

		if(player.inTvT && Config.TvT_CustomItems)
		{
			player.sendMessage(player.isLangRus() ? "Экипировка недоступна в ивенте." : "Equipment not available in event.");
			return false;
		}

		int bodyPart = item.getBodyPart();
		if(bodyPart == ItemTemplate.SLOT_LR_HAND || bodyPart == ItemTemplate.SLOT_L_HAND || bodyPart == ItemTemplate.SLOT_R_HAND)
		{
			if(player.isMounted())
			{
				player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(item.getItemId()));
				return false;
			}
			if(player.isCursedWeaponEquipped() || player.isFlagEquipped())
			{
				player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(item.getItemId()));
				return false;
			}
		}

		if(player.isPotionsDisabled())
		{
			player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(item.getItemId()));
			return false;
		}

		if(item.isCursed())
		{
			player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(item.getItemId()));
			return false;
		}

		if((item.getCustomFlags() & ItemInstance.FLAG_NO_UNEQUIP) == ItemInstance.FLAG_NO_UNEQUIP)
		{
			player.sendActionFailed();
			return false;
		}

		if(player.isInOlympiadMode() && item.isHeroWeapon())
		{
			player.sendPacket(new SystemMessage(SystemMessage.THIS_ITEM_CANT_BE_EQUIPPED_FOR_THE_OLYMPIAD_EVENT));
			return false;
		}

		if(item.isEquipped())
		{
			if(player.recording)
				player.recBot(3, item.getBodyPart(), 1, 0, 0, 0, 0);
			player.getInventory().unEquipItemInBodySlotAndNotify(item.getBodyPart(), item);
			return true;
		}

		if(Config.ZONE_EQUIP && player.restrictEquipZone(item.getItemId()))
		{
			player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(Integer.valueOf(item.getItemId())));
			return false;
		}

		player.getInventory().equipItem(item, true);

		if(!item.isEquipped())
			return false;

		SystemMessage sm;
		if(item.getEnchantLevel() > 0)
		{
			sm = new SystemMessage(SystemMessage.EQUIPPED__S1_S2);
			sm.addNumber(Integer.valueOf(item.getEnchantLevel()));
			sm.addItemName(Integer.valueOf(item.getItemId()));
		}
		else
			sm = new SystemMessage(SystemMessage.YOU_HAVE_EQUIPPED_YOUR_S1).addItemName(Integer.valueOf(item.getItemId()));

		player.sendPacket(sm);

		if(item.isTemporalItem())
			player.sendPacket(new SystemMessage(SystemMessage.S1_S2).addString((player.isLangRus() ? ": истекает " : ": expires ") + TimeUtils.toSimpleFormat(item.getLifeTimeRemaining() * 1000L + System.currentTimeMillis())).addItemName(Integer.valueOf(item.getItemId())));

		if(item.getTemplate().getType2() != ItemTemplate.TYPE2_ACCESSORY)
			player.broadcastUserInfo(true);

		return true;
	}

	@Override
	public int[] getItemIds()
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
	{
		//
	}

	@Override
	public void onShutdown()
	{
		//
	}
}
