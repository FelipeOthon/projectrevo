package items;

import l2s.gameserver.handler.items.IItemHandler;
import l2s.gameserver.handler.items.ItemHandler;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.PetDataTable;
import l2s.gameserver.tables.SkillTable;

public class PetSummon implements IItemHandler, ScriptFile
{
	private static final int[] _itemIds = PetDataTable._itemControlIds;
	private static final int _skillId = 2046;

	@Override
	public boolean useItem(final Playable playable, final ItemInstance item, final Boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
			return false;
		final Player player = (Player) playable;
		player.setPetControlItem(item);
		player.getAI().Cast(SkillTable.getInstance().getInfo(2046, 1), player, false, true);
		return true;
	}

	@Override
	public final int[] getItemIds()
	{
		return PetSummon._itemIds;
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
