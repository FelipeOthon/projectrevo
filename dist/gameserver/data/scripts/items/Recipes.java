package items;

import java.util.Collection;

import l2s.gameserver.RecipeController;
import l2s.gameserver.cache.Msg;
import l2s.gameserver.handler.items.IItemHandler;
import l2s.gameserver.handler.items.ItemHandler;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.RecipeList;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.s2c.RecipeBookItemList;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.ScriptFile;

public class Recipes implements IItemHandler, ScriptFile
{
	private static int[] _itemIds;

	public Recipes()
	{
		final Collection<RecipeList> rc = RecipeController.getInstance().getRecipes();
		Recipes._itemIds = new int[rc.size()];
		int i = 0;
		for(final RecipeList r : rc)
			Recipes._itemIds[i++] = r.getRecipeId();
	}

	@Override
	public boolean useItem(final Playable playable, final ItemInstance item, final Boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
			return false;
		final Player player = (Player) playable;
		if(item == null || item.getIntegerLimitedCount() < 1)
		{
			player.sendPacket(Msg.INCORRECT_ITEM_COUNT);
			return false;
		}
		final RecipeList rp = RecipeController.getInstance().getRecipeByItemId(item.getItemId());
		if(rp.isDwarvenRecipe())
		{
			if(player.getDwarvenRecipeLimit() > 0)
			{
				if(player.getDwarvenRecipeBook().size() >= player.getDwarvenRecipeLimit())
				{
					player.sendPacket(Msg.NO_FURTHER_RECIPES_MAY_BE_REGISTERED);
					return false;
				}
				if(rp.getLevel() > player.getSkillLevel(172))
				{
					player.sendPacket(Msg.CREATE_ITEM_LEVEL_IS_TOO_LOW_TO_REGISTER_THIS_RECIPE);
					return false;
				}
				if(player.findRecipe(rp))
				{
					player.sendPacket(Msg.THAT_RECIPE_IS_ALREADY_REGISTERED);
					return false;
				}
				player.registerRecipe(rp, true);
				player.sendPacket(new SystemMessage(851).addString(item.getTemplate().getName()));
				player.getInventory().destroyItem(item, 1L, true);
				final RecipeBookItemList response = new RecipeBookItemList(true, (int) player.getCurrentMp());
				response.setRecipes(player.getDwarvenRecipeBook());
				player.sendPacket(response);
			}
			else
				player.sendPacket(Msg.YOU_ARE_NOT_AUTHORIZED_TO_REGISTER_A_RECIPE);
		}
		else if(player.getCommonRecipeLimit() > 0)
		{
			if(player.getCommonRecipeBook().size() >= player.getCommonRecipeLimit())
			{
				player.sendPacket(Msg.NO_FURTHER_RECIPES_MAY_BE_REGISTERED);
				return false;
			}
			if(player.findRecipe(rp))
			{
				player.sendPacket(Msg.THAT_RECIPE_IS_ALREADY_REGISTERED);
				return false;
			}
			player.registerRecipe(rp, true);
			player.sendPacket(new SystemMessage(851).addString(item.getTemplate().getName()));
			player.getInventory().destroyItem(item, 1L, true);
			final RecipeBookItemList response = new RecipeBookItemList(true, (int) player.getCurrentMp());
			response.setRecipes(player.getCommonRecipeBook());
			player.sendPacket(response);
		}
		else
			player.sendPacket(Msg.YOU_ARE_NOT_AUTHORIZED_TO_REGISTER_A_RECIPE);
		return true;
	}

	@Override
	public int[] getItemIds()
	{
		return Recipes._itemIds;
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
		Recipes._itemIds = null;
	}
}
