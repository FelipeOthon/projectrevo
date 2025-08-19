package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _032_AnObviousLie extends Quest implements ScriptFile
{
	int MAXIMILIAN;
	int GENTLER;
	int MIKI_THE_CAT;
	int ALLIGATOR;
	int CHANCE_FOR_DROP;
	int MAP;
	int MEDICINAL_HERB;
	int SPIRIT_ORES;
	int THREAD;
	int SUEDE;
	int RACCOON_EAR;
	int CAT_EAR;
	int RABBIT_EAR;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _032_AnObviousLie()
	{
		super(false);
		MAXIMILIAN = 30120;
		GENTLER = 30094;
		MIKI_THE_CAT = 31706;
		ALLIGATOR = 20135;
		CHANCE_FOR_DROP = 30;
		MAP = 7165;
		MEDICINAL_HERB = 7166;
		SPIRIT_ORES = 3031;
		THREAD = 1868;
		SUEDE = 1866;
		RACCOON_EAR = 7680;
		CAT_EAR = 6843;
		RABBIT_EAR = 7683;
		this.addStartNpc(MAXIMILIAN);
		this.addTalkId(new int[] { MAXIMILIAN });
		this.addTalkId(new int[] { GENTLER });
		this.addTalkId(new int[] { MIKI_THE_CAT });
		this.addKillId(new int[] { ALLIGATOR });
		addQuestItem(new int[] { MEDICINAL_HERB });
		addQuestItem(new int[] { MAP });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("30120-1.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("30094-1.htm"))
		{
			st.giveItems(MAP, 1L);
			st.set("cond", "2");
		}
		else if(event.equals("31706-1.htm"))
		{
			st.takeItems(MAP, 1L);
			st.set("cond", "3");
		}
		else if(event.equals("30094-4.htm"))
		{
			if(st.getQuestItemsCount(MEDICINAL_HERB) > 19L)
			{
				st.takeItems(MEDICINAL_HERB, 20L);
				st.set("cond", "5");
			}
			else
			{
				htmltext = "You don't have enough materials";
				st.set("cond", "3");
			}
		}
		else if(event.equals("30094-7.htm"))
		{
			if(st.getQuestItemsCount(SPIRIT_ORES) >= 500L)
			{
				st.takeItems(SPIRIT_ORES, 500L);
				st.set("cond", "6");
			}
			else
				htmltext = "You don't have enough materials";
		}
		else if(event.equals("31706-4.htm"))
			st.set("cond", "7");
		else if(event.equals("30094-10.htm"))
			st.set("cond", "8");
		else if(event.equals("30094-13.htm"))
		{
			if(st.getQuestItemsCount(THREAD) < 1000L || st.getQuestItemsCount(SUEDE) < 500L)
				htmltext = "You don't have enough materials";
		}
		else if(event.equalsIgnoreCase("cat") || event.equalsIgnoreCase("racoon") || event.equalsIgnoreCase("rabbit"))
			if(st.getInt("cond") == 8 && st.getQuestItemsCount(THREAD) >= 1000L && st.getQuestItemsCount(SUEDE) >= 500L)
			{
				st.takeItems(THREAD, 1000L);
				st.takeItems(SUEDE, 500L);
				if(event.equalsIgnoreCase("cat"))
					st.giveItems(CAT_EAR, 1L);
				else if(event.equalsIgnoreCase("racoon"))
					st.giveItems(RACCOON_EAR, 1L);
				else if(event.equalsIgnoreCase("rabbit"))
					st.giveItems(RABBIT_EAR, 1L);
				st.unset("cond");
				st.playSound(Quest.SOUND_FINISH);
				htmltext = "30094-14.htm";
				st.exitCurrentQuest(false);
			}
			else
				htmltext = "You don't have enough materials";
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == MAXIMILIAN)
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 45)
					htmltext = "30120-0.htm";
				else
				{
					htmltext = "30120-0a.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				htmltext = "30120-2.htm";
		if(npcId == GENTLER)
			if(cond == 1)
				htmltext = "30094-0.htm";
			else if(cond == 2)
				htmltext = "30094-2.htm";
			else if(cond == 3)
				htmltext = "30094-forgot.htm";
			else if(cond == 4)
				htmltext = "30094-3.htm";
			else if(cond == 5 && st.getQuestItemsCount(SPIRIT_ORES) < 500L)
				htmltext = "30094-5.htm";
			else if(cond == 5 && st.getQuestItemsCount(SPIRIT_ORES) >= 500L)
				htmltext = "30094-6.htm";
			else if(cond == 6)
				htmltext = "30094-8.htm";
			else if(cond == 7)
				htmltext = "30094-9.htm";
			else if(cond == 8 && (st.getQuestItemsCount(THREAD) < 1000L || st.getQuestItemsCount(SUEDE) < 500L))
				htmltext = "30094-11.htm";
			else if(cond == 8 && st.getQuestItemsCount(THREAD) >= 1000L && st.getQuestItemsCount(SUEDE) >= 500L)
				htmltext = "30094-12.htm";
		if(npcId == MIKI_THE_CAT)
			if(cond == 2)
				htmltext = "31706-0.htm";
			else if(cond == 3)
				htmltext = "31706-2.htm";
			else if(cond == 6)
				htmltext = "31706-3.htm";
			else if(cond == 7)
				htmltext = "31706-5.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final long count = st.getQuestItemsCount(MEDICINAL_HERB);
		if(Rnd.chance(CHANCE_FOR_DROP) && st.getInt("cond") == 3 && count < 20L)
		{
			st.giveItems(MEDICINAL_HERB, 1L);
			if(count == 19L)
			{
				st.playSound(Quest.SOUND_MIDDLE);
				st.set("cond", "4");
			}
			else
				st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}
}
