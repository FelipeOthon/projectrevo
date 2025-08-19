package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.AddonsConfig;

public class _622_DeliveryofSpecialLiquor extends Quest implements ScriptFile
{
	private static int JEREMY;
	private static int LIETTA;
	private static int PULIN;
	private static int NAFF;
	private static int CROCUS;
	private static int KUBER;
	private static int BEOLIN;
	private static int SpecialDrink;
	private static int FeeOfSpecialDrink;
	private static int RecipeSealedTateossianRing;
	private static int RecipeSealedTateossianEarring;
	private static int RecipeSealedTateossianNecklace;
	private static int HastePotion;
	private static int Tateossian_CHANCE;

	public _622_DeliveryofSpecialLiquor()
	{
		super(false);
		this.addStartNpc(_622_DeliveryofSpecialLiquor.JEREMY);
		this.addTalkId(new int[] { _622_DeliveryofSpecialLiquor.LIETTA });
		this.addTalkId(new int[] { _622_DeliveryofSpecialLiquor.PULIN });
		this.addTalkId(new int[] { _622_DeliveryofSpecialLiquor.NAFF });
		this.addTalkId(new int[] { _622_DeliveryofSpecialLiquor.CROCUS });
		this.addTalkId(new int[] { _622_DeliveryofSpecialLiquor.KUBER });
		this.addTalkId(new int[] { _622_DeliveryofSpecialLiquor.BEOLIN });
		addQuestItem(new int[] { _622_DeliveryofSpecialLiquor.SpecialDrink });
		addQuestItem(new int[] { _622_DeliveryofSpecialLiquor.FeeOfSpecialDrink });
	}

	private static void takeDrink(final QuestState st, final int setcond)
	{
		st.set("cond", String.valueOf(setcond));
		st.takeItems(_622_DeliveryofSpecialLiquor.SpecialDrink, 1L);
		st.giveItems(_622_DeliveryofSpecialLiquor.FeeOfSpecialDrink, 1L);
		st.playSound(Quest.SOUND_MIDDLE);
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int _state = st.getState();
		final int cond = st.getInt("cond");
		final long SpecialDrink_count = st.getQuestItemsCount(_622_DeliveryofSpecialLiquor.SpecialDrink);
		if(event.equalsIgnoreCase("jeremy_q0622_0104.htm") && _state == 1)
		{
			st.setState(2);
			st.set("cond", "1");
			st.takeItems(_622_DeliveryofSpecialLiquor.SpecialDrink, -1L);
			st.takeItems(_622_DeliveryofSpecialLiquor.FeeOfSpecialDrink, -1L);
			st.giveItems(_622_DeliveryofSpecialLiquor.SpecialDrink, 5L);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("beolin_q0622_0201.htm") && cond == 1 && SpecialDrink_count > 0L)
			takeDrink(st, 2);
		else if(event.equalsIgnoreCase("kuber_q0622_0301.htm") && cond == 2 && SpecialDrink_count > 0L)
			takeDrink(st, 3);
		else if(event.equalsIgnoreCase("crocus_q0622_0401.htm") && cond == 3 && SpecialDrink_count > 0L)
			takeDrink(st, 4);
		else if(event.equalsIgnoreCase("naff_q0622_0501.htm") && cond == 4 && SpecialDrink_count > 0L)
			takeDrink(st, 5);
		else if(event.equalsIgnoreCase("pulin_q0622_0601.htm") && cond == 5 && SpecialDrink_count > 0L)
			takeDrink(st, 6);
		else if(event.equalsIgnoreCase("jeremy_q0622_0701.htm") && cond == 6 && st.getQuestItemsCount(_622_DeliveryofSpecialLiquor.FeeOfSpecialDrink) >= 5L)
			st.set("cond", "7");
		else if(event.equalsIgnoreCase("warehouse_keeper_lietta_q0622_0801.htm") && cond == 7 && st.getQuestItemsCount(_622_DeliveryofSpecialLiquor.FeeOfSpecialDrink) >= 5L)
		{
			st.takeItems(_622_DeliveryofSpecialLiquor.SpecialDrink, -1L);
			st.takeItems(_622_DeliveryofSpecialLiquor.FeeOfSpecialDrink, -1L);
			if(Rnd.chance(_622_DeliveryofSpecialLiquor.Tateossian_CHANCE * AddonsConfig.getQuestDropRates(this)))
			{
				if(Rnd.chance(40))
					st.giveItems(_622_DeliveryofSpecialLiquor.RecipeSealedTateossianRing + (Config.ALT_100_RECIPES_S ? 1 : 0), 1L);
				else if(Rnd.chance(40))
					st.giveItems(_622_DeliveryofSpecialLiquor.RecipeSealedTateossianEarring + (Config.ALT_100_RECIPES_S ? 1 : 0), 1L);
				else
					st.giveItems(_622_DeliveryofSpecialLiquor.RecipeSealedTateossianNecklace + (Config.ALT_100_RECIPES_S ? 1 : 0), 1L);
			}
			else
			{
				st.giveItems(57, 18800L);
				st.giveItems(_622_DeliveryofSpecialLiquor.HastePotion, 1L, true);
			}
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		if(st.getState() != 1)
		{
			final int cond = st.getInt("cond");
			final long SpecialDrink_count = st.getQuestItemsCount(_622_DeliveryofSpecialLiquor.SpecialDrink);
			final long FeeOfSpecialDrink_count = st.getQuestItemsCount(_622_DeliveryofSpecialLiquor.FeeOfSpecialDrink);
			if(cond == 1 && npcId == _622_DeliveryofSpecialLiquor.BEOLIN && SpecialDrink_count > 0L)
				htmltext = "beolin_q0622_0101.htm";
			else if(cond == 2 && npcId == _622_DeliveryofSpecialLiquor.KUBER && SpecialDrink_count > 0L)
				htmltext = "kuber_q0622_0201.htm";
			else if(cond == 3 && npcId == _622_DeliveryofSpecialLiquor.CROCUS && SpecialDrink_count > 0L)
				htmltext = "crocus_q0622_0301.htm";
			else if(cond == 4 && npcId == _622_DeliveryofSpecialLiquor.NAFF && SpecialDrink_count > 0L)
				htmltext = "naff_q0622_0401.htm";
			else if(cond == 5 && npcId == _622_DeliveryofSpecialLiquor.PULIN && SpecialDrink_count > 0L)
				htmltext = "pulin_q0622_0501.htm";
			else if(cond == 6 && npcId == _622_DeliveryofSpecialLiquor.JEREMY && FeeOfSpecialDrink_count >= 5L)
				htmltext = "jeremy_q0622_0601.htm";
			else if(cond == 7 && npcId == _622_DeliveryofSpecialLiquor.JEREMY && FeeOfSpecialDrink_count >= 5L)
				htmltext = "jeremy_q0622_0703.htm";
			else if(cond == 7 && npcId == _622_DeliveryofSpecialLiquor.LIETTA && FeeOfSpecialDrink_count >= 5L)
				htmltext = "warehouse_keeper_lietta_q0622_0701.htm";
			else if(cond > 0 && npcId == _622_DeliveryofSpecialLiquor.JEREMY && SpecialDrink_count > 0L)
				htmltext = "jeremy_q0622_0104.htm";
			return htmltext;
		}
		if(npcId != _622_DeliveryofSpecialLiquor.JEREMY)
			return htmltext;
		if(st.getPlayer().getLevel() >= 68)
		{
			st.set("cond", "0");
			return "jeremy_q0622_0101.htm";
		}
		st.exitCurrentQuest(true);
		return "jeremy_q0622_0103.htm";
	}

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	static
	{
		_622_DeliveryofSpecialLiquor.JEREMY = 31521;
		_622_DeliveryofSpecialLiquor.LIETTA = 31267;
		_622_DeliveryofSpecialLiquor.PULIN = 31543;
		_622_DeliveryofSpecialLiquor.NAFF = 31544;
		_622_DeliveryofSpecialLiquor.CROCUS = 31545;
		_622_DeliveryofSpecialLiquor.KUBER = 31546;
		_622_DeliveryofSpecialLiquor.BEOLIN = 31547;
		_622_DeliveryofSpecialLiquor.SpecialDrink = 7207;
		_622_DeliveryofSpecialLiquor.FeeOfSpecialDrink = 7198;
		_622_DeliveryofSpecialLiquor.RecipeSealedTateossianRing = 6849;
		_622_DeliveryofSpecialLiquor.RecipeSealedTateossianEarring = 6847;
		_622_DeliveryofSpecialLiquor.RecipeSealedTateossianNecklace = 6851;
		_622_DeliveryofSpecialLiquor.HastePotion = 734;
		_622_DeliveryofSpecialLiquor.Tateossian_CHANCE = 20;
	}
}
