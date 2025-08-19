package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _358_IllegitimateChildOfAGoddess extends Quest implements ScriptFile
{
	private static final int DROP_RATE = 70;
	private static final int REQUIRED = 108;
	private static final int SN_SCALE = 5868;
	private static final int SPhoenixNecl70 = 6329;
	private static final int SPhoenixEarr70 = 6331;
	private static final int SPhoenixRing70 = 6333;
	private static final int SMajestNecl70 = 6335;
	private static final int SMajestEarr70 = 6337;
	private static final int SMajestRing70 = 6339;
	private static final int SDarkCryShield60 = 5364;
	private static final int SNightMareShield60 = 5366;
	private static final String defaulttext = "noquest";
	private static final int OLTLIN = 30862;
	private static final int MOB1 = 20672;
	private static final int MOB2 = 20673;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _358_IllegitimateChildOfAGoddess()
	{
		super(true);
		this.addStartNpc(30862);
		this.addKillId(new int[] { 20672 });
		this.addKillId(new int[] { 20673 });
		addQuestItem(new int[] { 5868 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("30862-5.htm"))
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30862-6.htm"))
			st.exitCurrentQuest(true);
		else if(event.equalsIgnoreCase("30862-7.htm"))
			if(st.getQuestItemsCount(5868) >= 108L)
			{
				st.takeItems(5868, 108L);
				for(int i = 0; i < (int) st.getRateQuestsReward(); ++i)
				{
					final int chance = Rnd.get(100);
					int item;
					if(chance <= 16)
						item = 6329;
					else if(chance <= 33)
						item = 6331;
					else if(chance <= 50)
						item = 6333;
					else if(chance <= 58)
						item = 6335;
					else if(chance <= 67)
						item = 6337;
					else if(chance <= 76)
						item = 6339;
					else if(chance <= 84)
						item = 5364;
					else
						item = 5366;
					if(Config.ALT_100_RECIPES_A)
						++item;
					st.giveItems(item, 1L);
				}
				st.exitCurrentQuest(true);
				st.playSound(Quest.SOUND_FINISH);
			}
			else
				htmltext = "30862-4.htm";
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int id = st.getState();
		if(id == 1)
		{
			if(st.getPlayer().getLevel() < 63)
			{
				st.exitCurrentQuest(true);
				htmltext = "30862-1.htm";
			}
			else
				htmltext = "30862-2.htm";
		}
		else if(id == 2)
			if(st.getQuestItemsCount(5868) >= 108L)
				htmltext = "30862-3.htm";
			else
				htmltext = "30862-4.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final long count = st.getQuestItemsCount(5868);
		if(count < 108L && Rnd.chance(70))
		{
			st.giveItems(5868, 1L);
			if(count + 1L == 108L)
			{
				st.setState(2);
				st.playSound(Quest.SOUND_MIDDLE);
				st.set("cond", "2");
			}
			else
				st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}
}
