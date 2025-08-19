package items;

import java.util.ArrayList;
import java.util.List;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.cache.Msg;
import l2s.gameserver.handler.items.IItemHandler;
import l2s.gameserver.handler.items.ItemHandler;
import l2s.gameserver.model.Playable;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.components.IBroadcastPacket;
import l2s.gameserver.network.l2.s2c.ShowXMasSeal;
import l2s.gameserver.network.l2.s2c.SystemMessage;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;

public class Extractable implements IItemHandler, ScriptFile
{
	private static final int[] ITEM_IDS = new int[]{
		//19999,
		5555,
		8060,
		8534,
		8535,
		8536,
		8537,
		8538,
		8539,
		8540,
		5916,
		5944,
		5955,
		5966,
		5967,
		5968,
		5969,
		6007,
		6008,
		6009,
		6010,
		7725,
		7637,
		7636,
		7629,
		7630,
		7631,
		7632,
		7633,
		7634,
		7635
	};

	@Override
	public int[] getItemIds()
	{
		return ITEM_IDS;
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

	@Override
	public boolean useItem(final Playable playable, final ItemInstance item, final Boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
			return false;
		final Player player = playable.getPlayer();
		final int itemId = item.getItemId();

		/*if(itemId == 19999)
		{
			Functions.show("newspaper/00000000.htm", player);
			return true;
		}*/

		if(itemId == 5555)
		{
			player.sendPacket(new ShowXMasSeal(5555));
			return true;
		}

		if(itemId == 8060)
		{
			if(!canBeExtracted(8060, player))
				return false;

			if(Functions.getItemCount(player, 8058) > 0L)
			{
				Functions.removeItem(player, 8058, 1L);
				Functions.addItem(player, 8059, 1L);
				return true;
			}
			return false;
		}

		if(itemId == 8534)
		{
			if(!canBeExtracted(8534, player))
				return false;

			final int[] list = { 853, 916, 884 };
			final int[] chances = { 17, 17, 17 };
			final int[] counts = { 1, 1, 1 };
			Functions.removeItem(player, 8534, 1L);
			extract_item_r(list, counts, chances, player);
			return true;
		}

		if(itemId == 8535)
		{
			if(!canBeExtracted(8535, player))
				return false;

			final int[] list = { 854, 917, 885 };
			final int[] chances = { 17, 17, 17 };
			final int[] counts = { 1, 1, 1 };
			Functions.removeItem(player, 8535, 1L);
			extract_item_r(list, counts, chances, player);
			return true;
		}

		if(itemId == 8536)
		{
			if(!canBeExtracted(8536, player))
				return false;

			final int[] list = { 855, 119, 886 };
			final int[] chances = { 17, 17, 17 };
			final int[] counts = { 1, 1, 1 };
			Functions.removeItem(player, 8536, 1L);
			extract_item_r(list, counts, chances, player);
			return true;
		}

		if(itemId == 8537)
		{
			if(!canBeExtracted(8537, player))
				return false;

			final int[] list = { 856, 918, 887 };
			final int[] chances = { 17, 17, 17 };
			final int[] counts = { 1, 1, 1 };
			Functions.removeItem(player, 8537, 1L);
			extract_item_r(list, counts, chances, player);
			return true;
		}

		if(itemId == 8538)
		{
			if(!canBeExtracted(8538, player))
				return false;

			final int[] list = { 864, 926, 895 };
			final int[] chances = { 17, 17, 17 };
			final int[] counts = { 1, 1, 1 };
			Functions.removeItem(player, 8538, 1L);
			extract_item_r(list, counts, chances, player);
			return true;
		}

		if(itemId == 8539)
		{
			if(!canBeExtracted(8539, player))
				return false;

			final int[] list = { 8179, 8178, 8177 };
			final int[] chances = { 10, 20, 30 };
			final int[] counts = { 1, 1, 1 };
			Functions.removeItem(player, 8539, 1L);
			extract_item_r(list, counts, chances, player);
			return true;
		}

		if(itemId == 8540)
		{
			if(!canBeExtracted(8540, player))
				return false;

			Functions.removeItem(player, 8540, 1L);
			if(Rnd.chance(30))
				Functions.addItem(player, 8175, 1L);
			return true;
		}

		if(itemId == 5916)
		{
			if(!canBeExtracted(5916, player))
				return false;

			final int[] list = { 5917, 5918, 5919, 5920, 736 };
			final int[] counts = { 1, 1, 1, 1, 1 };
			Functions.removeItem(player, 5916, 1L);
			extract_item(list, counts, player);
			return true;
		}

		if(itemId == 5944)
		{
			if(!canBeExtracted(5944, player))
				return false;

			final int[] list = { 5922, 5923, 5924, 5925, 5926, 5927, 5928, 5929, 5930, 5931, 5932, 5933, 5934, 5935, 5936, 5937, 5938, 5939, 5940, 5941, 5942, 5943 };
			final int[] counts = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
			if(ctrl)
			{
				final long item_count = Functions.removeItem(player, 5944, Long.MAX_VALUE);
				for(final int[] res : mass_extract_item(item_count, list, counts, player))
					Functions.addItem(player, res[0], res[1]);
			}
			else
			{
				Functions.removeItem(player, 5944, 1L);
				extract_item(list, counts, player);
			}
			return true;
		}

		if(itemId == 5955)
		{
			if(!canBeExtracted(5955, player))
				return false;

			final int[] list = { 5942, 5943, 5945, 5946, 5947, 5948, 5949, 5950, 5951, 5952, 5953, 5954 };
			final int[] counts = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
			if(ctrl)
			{
				final long item_count = Functions.removeItem(player, 5955, Long.MAX_VALUE);
				for(final int[] res : mass_extract_item(item_count, list, counts, player))
					Functions.addItem(player, res[0], res[1]);
			}
			else
			{
				Functions.removeItem(player, 5955, 1L);
				extract_item(list, counts, player);
			}
			return true;
		}

		if(itemId == 5966)
		{
			if(!canBeExtracted(5966, player))
				return false;

			final int[] list = { 5970, 5971, 5977, 5978, 5979, 5986, 5993, 5994, 5995, 5997, 5983, 6001 };
			final int[] counts = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
			Functions.removeItem(player, 5966, 1L);
			extract_item(list, counts, player);
			return true;
		}

		if(itemId == 5967)
		{
			if(!canBeExtracted(5967, player))
				return false;

			final int[] list = { 5970, 5971, 5975, 5976, 5980, 5985, 5993, 5994, 5995, 5997, 5983, 6001 };
			final int[] counts = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
			Functions.removeItem(player, 5967, 1L);
			extract_item(list, counts, player);
			return true;
		}

		if(itemId == 5968)
		{
			if(!canBeExtracted(5968, player))
				return false;

			final int[] list = { 5973, 5974, 5981, 5984, 5989, 5990, 5991, 5992, 5996, 5998, 5999, 6000, 5988, 5983, 6001 };
			final int[] counts = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
			Functions.removeItem(player, 5968, 1L);
			extract_item(list, counts, player);
			return true;
		}

		if(itemId == 5969)
		{
			if(!canBeExtracted(5969, player))
				return false;

			final int[] list = { 5970, 5971, 5982, 5987, 5989, 5990, 5991, 5992, 5996, 5998, 5999, 6000, 5972, 6001 };
			final int[] counts = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
			Functions.removeItem(player, 5969, 1L);
			extract_item(list, counts, player);
			return true;
		}

		if(itemId == 6007)
		{
			if(!canBeExtracted(6007, player))
				return false;

			final int[] list = { 6019, 6013, 6014, 6016 };
			final int[] counts = { 2, 2, 1, 1 };
			final int[] chances = { 30, 30, 20, 20 };
			if(ctrl)
			{
				final long item_count = player.getInventory().getCountOf(6007);
				Functions.removeItem(player, 6007, item_count);
				for(final int[] res : mass_extract_item_r(item_count, list, counts, chances, player))
					Functions.addItem(player, res[0], res[1]);
			}
			else
			{
				Functions.removeItem(player, 6007, 1L);
				extract_item_r(list, counts, chances, player);
			}
			return true;
		}

		if(itemId == 6008)
		{
			if(!canBeExtracted(6008, player))
				return false;

			final int[] list = { 6017, 6020, 6014, 6016 };
			final int[] counts = { 2, 2, 1, 1 };
			final int[] chances = { 10, 20, 35, 35 };
			if(ctrl)
			{
				final long item_count = player.getInventory().getCountOf(6008);
				Functions.removeItem(player, 6008, item_count);
				for(final int[] res : mass_extract_item_r(item_count, list, counts, chances, player))
					Functions.addItem(player, res[0], res[1]);
			}
			else
			{
				Functions.removeItem(player, 6008, 1L);
				extract_item_r(list, counts, chances, player);
			}
			return true;
		}

		if(itemId == 6009)
		{
			if(!canBeExtracted(6009, player))
				return false;

			final int[] list = { 6012, 6018, 6019, 6013 };
			final int[] counts = { 1, 2, 2, 1 };
			final int[] chances = { 20, 20, 20, 40 };
			if(ctrl)
			{
				final long item_count = player.getInventory().getCountOf(6009);
				Functions.removeItem(player, 6009, item_count);
				for(final int[] res : mass_extract_item_r(item_count, list, counts, chances, player))
					Functions.addItem(player, res[0], res[1]);
			}
			else
			{
				Functions.removeItem(player, 6009, 1L);
				extract_item_r(list, counts, chances, player);
			}
			return true;
		}

		if(itemId == 6010)
		{
			if(!canBeExtracted(6010, player))
				return false;

			final int[] list = { 6017, 6020, 6016, 6015 };
			final int[] counts = { 2, 2, 1, 2 };
			final int[] chances = { 20, 20, 35, 25 };
			if(ctrl)
			{
				final long item_count = player.getInventory().getCountOf(6010);
				Functions.removeItem(player, 6010, item_count);
				for(final int[] res : mass_extract_item_r(item_count, list, counts, chances, player))
					Functions.addItem(player, res[0], res[1]);
			}
			else
			{
				Functions.removeItem(player, 6010, 1L);
				extract_item_r(list, counts, chances, player);
			}
			return true;
		}

		if(itemId == 7725)
		{
			if(!canBeExtracted(7725, player))
				return false;

			final int[] list = { 6035, 1060, 735, 1540, 1061, 1539 };
			final int[] counts = { 1, 1, 1, 1, 1, 1 };
			final int[] chances = { 7, 39, 7, 3, 12, 32 };
			if(ctrl)
			{
				final long item_count = player.getInventory().getCountOf(7725);
				Functions.removeItem(player, 7725, item_count);
				for(final int[] res : mass_extract_item_r(item_count, list, counts, chances, player))
					Functions.addItem(player, res[0], res[1]);
			}
			else
			{
				Functions.removeItem(player, 7725, 1L);
				extract_item_r(list, counts, chances, player);
			}
			return true;
		}

		if(itemId == 7637)
		{
			if(!canBeExtracted(7637, player))
				return false;

			final int[] list = { 4039, 4041, 4043, 4044, 4042, 4040 };
			final int[] counts = { 4, 1, 4, 4, 2, 2 };
			final int[] chances = { 20, 10, 20, 20, 15, 15 };
			if(ctrl)
			{
				final long item_count = player.getInventory().getCountOf(7637);
				Functions.removeItem(player, 7637, item_count);
				for(final int[] res : mass_extract_item_r(item_count, list, counts, chances, player))
					Functions.addItem(player, res[0], res[1]);
			}
			else
			{
				Functions.removeItem(player, 7637, 1L);
				extract_item_r(list, counts, chances, player);
			}
			return true;
		}

		if(itemId == 7636)
		{
			if(!canBeExtracted(7636, player))
				return false;

			final int[] list = { 1875, 1882, 1880, 1874, 1877, 1881, 1879, 1876 };
			final int[] counts = { 3, 3, 4, 1, 3, 1, 3, 6 };
			final int[] chances = { 10, 20, 10, 10, 10, 12, 12, 16 };
			if(ctrl)
			{
				final long item_count = player.getInventory().getCountOf(7636);
				Functions.removeItem(player, 7636, item_count);
				for(final int[] res : mass_extract_item_r(item_count, list, counts, chances, player))
					Functions.addItem(player, res[0], res[1]);
			}
			else
			{
				Functions.removeItem(player, 7636, 1L);
				extract_item_r(list, counts, chances, player);
			}
			return true;
		}

		if(itemId == 7629)
		{
			if(!canBeExtracted(7629, player))
				return false;

			final int[] list = { 6688, 6689, 6690, 6691, 6693, 6694, 6695, 6696, 6697, 7579, 57 };
			final int[] counts = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 330000 };
			final int[] chances = { 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 10 };
			Functions.removeItem(player, 7629, 1L);
			extract_item_r(list, counts, chances, player);
			return true;
		}

		if(itemId == 7630)
		{
			if(!canBeExtracted(7630, player))
				return false;

			final int[] list = { 6703, 6704, 6705, 6706, 6708, 6709, 6710, 6712, 6713, 6714, 57 };
			final int[] counts = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 292000 };
			final int[] chances = { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 20 };
			Functions.removeItem(player, 7630, 1L);
			extract_item_r(list, counts, chances, player);
			return true;
		}

		if(itemId == 7631)
		{
			if(!canBeExtracted(7631, player))
				return false;

			final int[] list = { 6701, 6702, 6707, 6711, 57 };
			final int[] counts = { 1, 1, 1, 1, 930000 };
			final int[] chances = { 20, 20, 20, 20, 20 };
			Functions.removeItem(player, 7631, 1L);
			extract_item_r(list, counts, chances, player);
			return true;
		}

		if(itemId == 7632)
		{
			if(!canBeExtracted(7632, player))
				return false;

			int[] list;
			if(Config.ALT_100_RECIPES_S)
				list = new int[] { 6858, 6860, 6862, 6864, 6868, 6870, 6872, 6876, 6878, 6880, 57 };
			else
				list = new int[] { 6857, 6859, 6861, 6863, 6867, 6869, 6871, 6875, 6877, 6879, 57 };
			final int[] counts = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 340000 };
			final int[] chances = { 8, 9, 8, 9, 8, 9, 8, 9, 8, 9, 7 };
			Functions.removeItem(player, 7632, 1L);
			extract_item_r(list, counts, chances, player);
			return true;
		}

		if(itemId == 7633)
		{
			if(!canBeExtracted(7633, player))
				return false;

			int[] list;
			if(Config.ALT_100_RECIPES_S)
				list = new int[] { 6854, 6856, 6866, 6874, 57 };
			else
				list = new int[] { 6853, 6855, 6865, 6873, 57 };
			final int[] counts = { 1, 1, 1, 1, 850000 };
			final int[] chances = { 20, 20, 20, 20, 20 };
			Functions.removeItem(player, 7633, 1L);
			extract_item_r(list, counts, chances, player);
			return true;
		}

		if(itemId == 7634)
		{
			if(!canBeExtracted(7634, player))
				return false;

			final int[] list = { 1874, 1875, 1876, 1877, 1879, 1880, 1881, 1882, 57 };
			final int[] counts = { 20, 20, 20, 20, 20, 20, 20, 20, 150000 };
			final int[] chances = { 10, 10, 16, 11, 10, 5, 10, 18, 10 };
			Functions.removeItem(player, 7634, 1L);
			extract_item_r(list, counts, chances, player);
			return true;
		}

		if(itemId == 7635)
		{
			if(!canBeExtracted(7635, player))
				return false;

			final int[] list = { 4039, 4040, 4041, 4042, 4043, 4044, 57 };
			final int[] counts = { 4, 4, 4, 4, 4, 4, 160000 };
			final int[] chances = { 20, 10, 10, 10, 20, 20, 10 };
			Functions.removeItem(player, 7635, 1L);
			extract_item_r(list, counts, chances, player);
			return true;
		}
		return false;
	}

	private static void extract_item(final int[] list, final int[] counts, final Player player)
	{
		final int index = Rnd.get(list.length);
		final int id = list[index];
		final int count = counts[index];
		Functions.addItem(player, id, count);
	}

	private static List<int[]> mass_extract_item(final long source_count, final int[] list, final int[] counts, final Player player)
	{
		final List<int[]> result = new ArrayList<int[]>((int) Math.min(list.length, source_count));
		for(int n = 1; n <= source_count; ++n)
		{
			final int index = Rnd.get(list.length);
			final int item = list[index];
			final int count = counts[index];
			int[] old = null;
			for(final int[] res : result)
				if(res[0] == item)
					old = res;
			if(old == null)
				result.add(new int[] { item, count });
			else
			{
				final int[] array = old;
				final int n2 = 1;
				array[n2] += count;
			}
		}
		return result;
	}

	private static void extract_item_r(final int[] list, final int[] counts, final int[] chances, final Player player)
	{
		int sum = 0;
		for(int i = 0; i < list.length; ++i)
			sum += chances[i];
		final int[] table = new int[sum];
		int k = 0;
		for(int j = 0; j < list.length; ++j)
			for(int l = 0; l < chances[j]; ++l)
			{
				table[k] = j;
				++k;
			}
		int j = table[Rnd.get(table.length)];
		final int item = list[j];
		final int count = counts[j];
		Functions.addItem(player, item, count);
	}

	private static List<int[]> mass_extract_item_r(final long source_count, final int[] list, final int[] counts, final int[] chances, final Player player)
	{
		final List<int[]> result = new ArrayList<int[]>((int) Math.min(list.length, source_count));
		int sum = 0;
		for(int i = 0; i < list.length; ++i)
			sum += chances[i];
		final int[] table = new int[sum];
		int k = 0;
		for(int j = 0; j < list.length; ++j)
			for(int l = 0; l < chances[j]; ++l)
			{
				table[k] = j;
				++k;
			}
		for(int n = 1; n <= source_count; ++n)
		{
			final int m = table[Rnd.get(table.length)];
			final int item = list[m];
			final int count = counts[m];
			int[] old = null;
			for(final int[] res : result)
				if(res[0] == item)
					old = res;
			if(old == null)
				result.add(new int[] { item, count });
			else
			{
				final int[] array = old;
				final int n2 = 1;
				array[n2] += count;
			}
		}
		return result;
	}

	private static boolean canBeExtracted(final int itemId, final Player player)
	{
		if(player.getWeightPenalty() >= 3 || player.getInventory().getSize() > player.getInventoryLimit() - 10)
		{
			player.sendPacket(new IBroadcastPacket[] { Msg.YOUR_INVENTORY_IS_FULL, new SystemMessage(113).addItemName(itemId) });
			return false;
		}
		return true;
	}
}
