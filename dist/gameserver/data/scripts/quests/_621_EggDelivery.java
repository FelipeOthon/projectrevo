package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.AddonsConfig;

public class _621_EggDelivery extends Quest implements ScriptFile
{
	private static int JEREMY;
	private static int VALENTINE;
	private static int PULIN;
	private static int NAFF;
	private static int CROCUS;
	private static int KUBER;
	private static int BEOLIN;
	private static final int BoiledEgg = 7206;
	private static final int FeeOfBoiledEgg = 7196;
	private static final int HastePotion = 734;
	private static final int RecipeSealedTateossianRing = 6849;
	private static final int RecipeSealedTateossianEarring = 6847;
	private static final int RecipeSealedTateossianNecklace = 6851;
	private static int Tateossian_CHANCE;

	public _621_EggDelivery()
	{
		super(false);
		this.addStartNpc(_621_EggDelivery.JEREMY);
		this.addTalkId(new int[] { _621_EggDelivery.VALENTINE });
		this.addTalkId(new int[] { _621_EggDelivery.PULIN });
		this.addTalkId(new int[] { _621_EggDelivery.NAFF });
		this.addTalkId(new int[] { _621_EggDelivery.CROCUS });
		this.addTalkId(new int[] { _621_EggDelivery.KUBER });
		this.addTalkId(new int[] { _621_EggDelivery.BEOLIN });
		addQuestItem(new int[] { 7206 });
		addQuestItem(new int[] { 7196 });
	}

	private static void takeEgg(final QuestState st, final int setcond)
	{
		st.set("cond", String.valueOf(setcond));
		st.takeItems(7206, 1L);
		st.giveItems(7196, 1L);
		st.playSound(Quest.SOUND_MIDDLE);
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int _state = st.getState();
		final int cond = st.getInt("cond");
		final long BoiledEgg_count = st.getQuestItemsCount(7206);
		if(event.equalsIgnoreCase("jeremy_q0621_0104.htm") && _state == 1)
		{
			st.takeItems(7206, -1L);
			st.takeItems(7196, -1L);
			st.setState(2);
			st.set("cond", "1");
			st.giveItems(7206, 5L);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("pulin_q0621_0201.htm") && cond == 1 && BoiledEgg_count > 0L)
			takeEgg(st, 2);
		else if(event.equalsIgnoreCase("naff_q0621_0301.htm") && cond == 2 && BoiledEgg_count > 0L)
			takeEgg(st, 3);
		else if(event.equalsIgnoreCase("crocus_q0621_0401.htm") && cond == 3 && BoiledEgg_count > 0L)
			takeEgg(st, 4);
		else if(event.equalsIgnoreCase("kuber_q0621_0501.htm") && cond == 4 && BoiledEgg_count > 0L)
			takeEgg(st, 5);
		else if(event.equalsIgnoreCase("beolin_q0621_0601.htm") && cond == 5 && BoiledEgg_count > 0L)
			takeEgg(st, 6);
		else if(event.equalsIgnoreCase("jeremy_q0621_0701.htm") && cond == 6 && st.getQuestItemsCount(7196) >= 5L)
			st.set("cond", "7");
		else if(event.equalsIgnoreCase("brewer_valentine_q0621_0801.htm") && cond == 7 && st.getQuestItemsCount(7196) >= 5L)
		{
			st.takeItems(7206, -1L);
			st.takeItems(7196, -1L);
			if(Rnd.chance(_621_EggDelivery.Tateossian_CHANCE * AddonsConfig.getQuestDropRates(this)))
			{
				if(Rnd.chance(40))
					st.giveItems(6849 + (Config.ALT_100_RECIPES_S ? 1 : 0), 1L);
				else if(Rnd.chance(40))
					st.giveItems(6847 + (Config.ALT_100_RECIPES_S ? 1 : 0), 1L);
				else
					st.giveItems(6851 + (Config.ALT_100_RECIPES_S ? 1 : 0), 1L);
			}
			else
			{
				st.giveItems(57, 18800L);
				st.giveItems(734, 1L, true);
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
			final long BoiledEgg_count = st.getQuestItemsCount(7206);
			final long FeeOfBoiledEgg_count = st.getQuestItemsCount(7196);
			if(cond == 1 && npcId == _621_EggDelivery.PULIN && BoiledEgg_count > 0L)
				htmltext = "pulin_q0621_0101.htm";
			if(cond == 2 && npcId == _621_EggDelivery.NAFF && BoiledEgg_count > 0L)
				htmltext = "naff_q0621_0201.htm";
			if(cond == 3 && npcId == _621_EggDelivery.CROCUS && BoiledEgg_count > 0L)
				htmltext = "crocus_q0621_0301.htm";
			if(cond == 4 && npcId == _621_EggDelivery.KUBER && BoiledEgg_count > 0L)
				htmltext = "kuber_q0621_0401.htm";
			if(cond == 5 && npcId == _621_EggDelivery.BEOLIN && BoiledEgg_count > 0L)
				htmltext = "beolin_q0621_0501.htm";
			if(cond == 6 && npcId == _621_EggDelivery.JEREMY && FeeOfBoiledEgg_count >= 5L)
				htmltext = "jeremy_q0621_0601.htm";
			if(cond == 7 && npcId == _621_EggDelivery.JEREMY && FeeOfBoiledEgg_count >= 5L)
				htmltext = "jeremy_q0621_0703.htm";
			if(cond == 7 && npcId == _621_EggDelivery.VALENTINE && FeeOfBoiledEgg_count >= 5L)
				htmltext = "brewer_valentine_q0621_0701.htm";
			else if(cond > 0 && npcId == _621_EggDelivery.JEREMY && BoiledEgg_count > 0L)
				htmltext = "jeremy_q0621_0104.htm";
			return htmltext;
		}
		if(npcId != _621_EggDelivery.JEREMY)
			return htmltext;
		if(st.getPlayer().getLevel() >= 68)
		{
			st.set("cond", "0");
			return "jeremy_q0621_0101.htm";
		}
		st.exitCurrentQuest(true);
		return "jeremy_q0621_0103.htm";
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
		_621_EggDelivery.JEREMY = 31521;
		_621_EggDelivery.VALENTINE = 31584;
		_621_EggDelivery.PULIN = 31543;
		_621_EggDelivery.NAFF = 31544;
		_621_EggDelivery.CROCUS = 31545;
		_621_EggDelivery.KUBER = 31546;
		_621_EggDelivery.BEOLIN = 31547;
		_621_EggDelivery.Tateossian_CHANCE = 20;
	}
}
