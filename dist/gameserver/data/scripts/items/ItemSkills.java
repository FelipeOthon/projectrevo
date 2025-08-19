package items;

import l2s.gameserver.handler.items.IItemHandler;
import l2s.gameserver.handler.items.ItemHandler;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.ItemTable;
import l2s.gameserver.templates.item.ItemTemplate;

import org.napile.primitive.sets.IntSet;
import org.napile.primitive.sets.impl.HashIntSet;

/**
 * @author Bonux
**/
public class ItemSkills implements IItemHandler, ScriptFile
{
	private int[] _itemIds;

	public ItemSkills()
	{
		IntSet set = new HashIntSet();
		for(ItemTemplate template : ItemTable.getInstance().getAllTemplates())
		{
			if(template != null)
			{
				Skill[] skills = template.getAttachedSkills();
				if(skills != null)
				{
					for(Skill skill : skills)
					{
						if(skill.isHandler())
						{
							set.add(template.getItemId());
							break;
						}
					}
				}
			}
		}
		_itemIds = set.toArray();
	}

	@Override
	public boolean useItem(Playable playable, ItemInstance item, Boolean ctrl)
	{
		Player player = playable.getPlayer();
		if(player == null)
			return false;

		Skill[] skills = item.getTemplate().getAttachedSkills();
		if(skills != null && skills.length > 0)
		{
			for(Skill skill : skills)
			{
				if(skill.isHandler())
				{
					Creature aimingTarget = skill.getAimingTarget(player, player.getTarget());
					if(skill.checkCondition(player, aimingTarget, ctrl, false, true))
						player.getAI().Cast(skill, aimingTarget, ctrl, false);
				}
			}
			return true;
		}
		return false;
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
