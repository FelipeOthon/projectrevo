package quests;

import java.util.HashMap;
import java.util.Map;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _372_LegacyOfInsolence extends Quest implements ScriptFile
{
	private static int HOLLY;
	private static int WALDERAL;
	private static int DESMOND;
	private static int PATRIN;
	private static int CLAUDIA;
	private static int CORRUPT_SAGE;
	private static int ERIN_EDIUNCE;
	private static int HALLATE_INSP;
	private static int PLATINUM_OVL;
	private static int PLATINUM_PRE;
	private static int MESSENGER_A1;
	private static int MESSENGER_A2;
	private static int Ancient_Red_Papyrus;
	private static int Ancient_Blue_Papyrus;
	private static int Ancient_Black_Papyrus;
	private static int Ancient_White_Papyrus;
	private static int[] Revelation_of_the_Seals_Range;
	private static int[] Ancient_Epic_Chapter_Range;
	private static int[] Imperial_Genealogy_Range;
	private static int[] Blueprint_Tower_of_Insolence_Range;
	private static int[] Reward_Dark_Crystal;
	private static int[] Reward_Tallum;
	private static int[] Reward_Nightmare;
	private static int[] Reward_Majestic;
	private static int Three_Recipes_Reward_Chance;
	private static int Two_Recipes_Reward_Chance;
	private static int Adena4k_Reward_Chance;
	private final Map<Integer, int[]> DROPLIST;

	public _372_LegacyOfInsolence()
	{
		super(true);
		DROPLIST = new HashMap<Integer, int[]>();
		this.addStartNpc(_372_LegacyOfInsolence.WALDERAL);
		this.addTalkId(new int[] { _372_LegacyOfInsolence.HOLLY });
		this.addTalkId(new int[] { _372_LegacyOfInsolence.DESMOND });
		this.addTalkId(new int[] { _372_LegacyOfInsolence.PATRIN });
		this.addTalkId(new int[] { _372_LegacyOfInsolence.CLAUDIA });
		this.addKillId(new int[] { _372_LegacyOfInsolence.CORRUPT_SAGE });
		this.addKillId(new int[] { _372_LegacyOfInsolence.ERIN_EDIUNCE });
		this.addKillId(new int[] { _372_LegacyOfInsolence.HALLATE_INSP });
		this.addKillId(new int[] { _372_LegacyOfInsolence.PLATINUM_OVL });
		this.addKillId(new int[] { _372_LegacyOfInsolence.PLATINUM_PRE });
		this.addKillId(new int[] { _372_LegacyOfInsolence.MESSENGER_A1 });
		this.addKillId(new int[] { _372_LegacyOfInsolence.MESSENGER_A2 });
		DROPLIST.put(_372_LegacyOfInsolence.CORRUPT_SAGE, new int[] { _372_LegacyOfInsolence.Ancient_Red_Papyrus, 35 });
		DROPLIST.put(_372_LegacyOfInsolence.ERIN_EDIUNCE, new int[] { _372_LegacyOfInsolence.Ancient_Red_Papyrus, 40 });
		DROPLIST.put(_372_LegacyOfInsolence.HALLATE_INSP, new int[] { _372_LegacyOfInsolence.Ancient_Red_Papyrus, 45 });
		DROPLIST.put(_372_LegacyOfInsolence.PLATINUM_OVL, new int[] { _372_LegacyOfInsolence.Ancient_Blue_Papyrus, 40 });
		DROPLIST.put(_372_LegacyOfInsolence.PLATINUM_PRE, new int[] { _372_LegacyOfInsolence.Ancient_Black_Papyrus, 25 });
		DROPLIST.put(_372_LegacyOfInsolence.MESSENGER_A1, new int[] { _372_LegacyOfInsolence.Ancient_White_Papyrus, 25 });
		DROPLIST.put(_372_LegacyOfInsolence.MESSENGER_A2, new int[] { _372_LegacyOfInsolence.Ancient_White_Papyrus, 25 });
	}

	private static void giveRecipe(final QuestState st, final int recipe_id)
	{
		st.giveItems(Config.ALT_100_RECIPES_A ? recipe_id + 1 : recipe_id, 1L);
	}

	private static boolean check_and_reward(final QuestState st, final int[] items_range, final int[] reward)
	{
		for(int item_id = items_range[0]; item_id <= items_range[1]; ++item_id)
			if(st.getQuestItemsCount(item_id) < 1L)
				return false;
		for(int item_id = items_range[0]; item_id <= items_range[1]; ++item_id)
			st.takeItems(item_id, 1L);
		if(Rnd.chance(_372_LegacyOfInsolence.Three_Recipes_Reward_Chance))
		{
			for(final int reward_item_id : reward)
				giveRecipe(st, reward_item_id);
			st.playSound(Quest.SOUND_JACKPOT);
		}
		else if(Rnd.chance(_372_LegacyOfInsolence.Two_Recipes_Reward_Chance))
		{
			final int ignore_reward_id = reward[Rnd.get(reward.length)];
			for(final int reward_item_id2 : reward)
				if(reward_item_id2 != ignore_reward_id)
					giveRecipe(st, reward_item_id2);
			st.playSound(Quest.SOUND_JACKPOT);
		}
		else if(Rnd.chance(_372_LegacyOfInsolence.Adena4k_Reward_Chance))
			st.giveItems(57, 4000L, false);
		else
			giveRecipe(st, reward[Rnd.get(reward.length)]);
		return true;
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		final int _state = st.getState();
		if(_state == 1)
		{
			if(event.equalsIgnoreCase("30844-6.htm"))
			{
				st.setState(2);
				st.set("cond", "1");
				st.playSound(Quest.SOUND_ACCEPT);
			}
			else if(event.equalsIgnoreCase("30844-9.htm"))
				st.set("cond", "2");
			else if(event.equalsIgnoreCase("30844-7.htm"))
			{
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
		}
		else if(_state == 2)
			if(event.equalsIgnoreCase("30839-exchange"))
				htmltext = check_and_reward(st, _372_LegacyOfInsolence.Imperial_Genealogy_Range, _372_LegacyOfInsolence.Reward_Dark_Crystal) ? "30839-2.htm" : "30839-3.htm";
			else if(event.equalsIgnoreCase("30855-exchange"))
				htmltext = check_and_reward(st, _372_LegacyOfInsolence.Revelation_of_the_Seals_Range, _372_LegacyOfInsolence.Reward_Majestic) ? "30855-2.htm" : "30855-3.htm";
			else if(event.equalsIgnoreCase("30929-exchange"))
				htmltext = check_and_reward(st, _372_LegacyOfInsolence.Ancient_Epic_Chapter_Range, _372_LegacyOfInsolence.Reward_Tallum) ? "30839-2.htm" : "30839-3.htm";
			else if(event.equalsIgnoreCase("31001-exchange"))
				htmltext = check_and_reward(st, _372_LegacyOfInsolence.Revelation_of_the_Seals_Range, _372_LegacyOfInsolence.Reward_Nightmare) ? "30839-2.htm" : "30839-3.htm";
			else if(event.equalsIgnoreCase("30844-DarkCrystal"))
				htmltext = check_and_reward(st, _372_LegacyOfInsolence.Blueprint_Tower_of_Insolence_Range, _372_LegacyOfInsolence.Reward_Dark_Crystal) ? "30844-11.htm" : "30844-12.htm";
			else if(event.equalsIgnoreCase("30844-Tallum"))
				htmltext = check_and_reward(st, _372_LegacyOfInsolence.Blueprint_Tower_of_Insolence_Range, _372_LegacyOfInsolence.Reward_Tallum) ? "30844-11.htm" : "30844-12.htm";
			else if(event.equalsIgnoreCase("30844-Nightmare"))
				htmltext = check_and_reward(st, _372_LegacyOfInsolence.Blueprint_Tower_of_Insolence_Range, _372_LegacyOfInsolence.Reward_Nightmare) ? "30844-11.htm" : "30844-12.htm";
			else if(event.equalsIgnoreCase("30844-Majestic"))
				htmltext = check_and_reward(st, _372_LegacyOfInsolence.Blueprint_Tower_of_Insolence_Range, _372_LegacyOfInsolence.Reward_Majestic) ? "30844-11.htm" : "30844-12.htm";
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int _state = st.getState();
		final int npcId = npc.getNpcId();
		if(_state == 1)
		{
			if(npcId != _372_LegacyOfInsolence.WALDERAL)
				return htmltext;
			if(st.getPlayer().getLevel() >= 59)
				htmltext = "30844-4.htm";
			else
			{
				htmltext = "30844-5.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(_state == 2)
			htmltext = String.valueOf(npcId) + "-1.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState qs)
	{
		if(qs.getState() != 2)
			return null;
		final int[] drop = DROPLIST.get(npc.getNpcId());
		if(drop == null)
			return null;
		qs.rollAndGive(drop[0], 1, drop[1]);
		return null;
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
		_372_LegacyOfInsolence.HOLLY = 30839;
		_372_LegacyOfInsolence.WALDERAL = 30844;
		_372_LegacyOfInsolence.DESMOND = 30855;
		_372_LegacyOfInsolence.PATRIN = 30929;
		_372_LegacyOfInsolence.CLAUDIA = 31001;
		_372_LegacyOfInsolence.CORRUPT_SAGE = 20817;
		_372_LegacyOfInsolence.ERIN_EDIUNCE = 20821;
		_372_LegacyOfInsolence.HALLATE_INSP = 20825;
		_372_LegacyOfInsolence.PLATINUM_OVL = 20829;
		_372_LegacyOfInsolence.PLATINUM_PRE = 21069;
		_372_LegacyOfInsolence.MESSENGER_A1 = 21062;
		_372_LegacyOfInsolence.MESSENGER_A2 = 21063;
		_372_LegacyOfInsolence.Ancient_Red_Papyrus = 5966;
		_372_LegacyOfInsolence.Ancient_Blue_Papyrus = 5967;
		_372_LegacyOfInsolence.Ancient_Black_Papyrus = 5968;
		_372_LegacyOfInsolence.Ancient_White_Papyrus = 5969;
		_372_LegacyOfInsolence.Revelation_of_the_Seals_Range = new int[] { 5972, 5978 };
		_372_LegacyOfInsolence.Ancient_Epic_Chapter_Range = new int[] { 5979, 5983 };
		_372_LegacyOfInsolence.Imperial_Genealogy_Range = new int[] { 5984, 5988 };
		_372_LegacyOfInsolence.Blueprint_Tower_of_Insolence_Range = new int[] { 5989, 6001 };
		_372_LegacyOfInsolence.Reward_Dark_Crystal = new int[] { 5368, 5392, 5426 };
		_372_LegacyOfInsolence.Reward_Tallum = new int[] { 5370, 5394, 5428 };
		_372_LegacyOfInsolence.Reward_Nightmare = new int[] { 5380, 5404, 5430 };
		_372_LegacyOfInsolence.Reward_Majestic = new int[] { 5382, 5406, 5432 };
		_372_LegacyOfInsolence.Three_Recipes_Reward_Chance = 1;
		_372_LegacyOfInsolence.Two_Recipes_Reward_Chance = 2;
		_372_LegacyOfInsolence.Adena4k_Reward_Chance = 2;
	}
}
