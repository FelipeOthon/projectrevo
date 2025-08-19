package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _002_WhatWomenWant extends Quest implements ScriptFile
{
	int ARUJIEN;
	int MIRABEL;
	int HERBIEL;
	int GREENIS;
	int ARUJIENS_LETTER1;
	int ARUJIENS_LETTER2;
	int ARUJIENS_LETTER3;
	int POETRY_BOOK;
	int GREENIS_LETTER;
	int ADENA;
	int BEGINNERS_POTION;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _002_WhatWomenWant()
	{
		super(false);
		ARUJIEN = 30223;
		MIRABEL = 30146;
		HERBIEL = 30150;
		GREENIS = 30157;
		ARUJIENS_LETTER1 = 1092;
		ARUJIENS_LETTER2 = 1093;
		ARUJIENS_LETTER3 = 1094;
		POETRY_BOOK = 689;
		GREENIS_LETTER = 693;
		ADENA = 57;
		BEGINNERS_POTION = 1073;
		this.addStartNpc(ARUJIEN);
		this.addTalkId(new int[] { MIRABEL });
		this.addTalkId(new int[] { HERBIEL });
		this.addTalkId(new int[] { GREENIS });
		addQuestItem(new int[] { GREENIS_LETTER, ARUJIENS_LETTER3, ARUJIENS_LETTER1, ARUJIENS_LETTER2, POETRY_BOOK });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("30223-04.htm"))
		{
			st.giveItems(ARUJIENS_LETTER1, 1L, false);
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30223-08.htm"))
		{
			st.takeItems(ARUJIENS_LETTER3, -1L);
			st.giveItems(POETRY_BOOK, 1L, false);
			st.set("cond", "4");
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("30223-10.htm"))
		{
			st.takeItems(ARUJIENS_LETTER3, -1L);
			st.giveItems(ADENA, 450L, true);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(npcId == ARUJIEN)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getRace().ordinal() != 1 && st.getPlayer().getRace().ordinal() != 0)
					htmltext = "30223-00.htm";
				else if(st.getPlayer().getLevel() >= 2)
					htmltext = "30223-02.htm";
				else
				{
					htmltext = "30223-01.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1 && st.getQuestItemsCount(ARUJIENS_LETTER1) > 0L)
				htmltext = "30223-05.htm";
			else if(cond == 2 && st.getQuestItemsCount(ARUJIENS_LETTER2) > 0L)
				htmltext = "30223-06.htm";
			else if(cond == 3 && st.getQuestItemsCount(ARUJIENS_LETTER3) > 0L)
				htmltext = "30223-07.htm";
			else if(cond == 4 && st.getQuestItemsCount(POETRY_BOOK) > 0L)
				htmltext = "30223-11.htm";
			else if(cond == 5 && st.getQuestItemsCount(GREENIS_LETTER) > 0L)
			{
				htmltext = "30223-10.htm";
				st.takeItems(GREENIS_LETTER, -1L);
				st.giveItems(BEGINNERS_POTION, 5L, true);
				st.unset("cond");
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(false);
			}
		}
		else if(npcId == MIRABEL)
		{
			if(cond == 1 && st.getQuestItemsCount(ARUJIENS_LETTER1) > 0L)
			{
				htmltext = "30146-01.htm";
				st.takeItems(ARUJIENS_LETTER1, -1L);
				st.giveItems(ARUJIENS_LETTER2, 1L, false);
				st.set("cond", "2");
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else if(cond == 2)
				htmltext = "30146-02.htm";
		}
		else if(npcId == HERBIEL)
		{
			if(cond == 2 && st.getQuestItemsCount(ARUJIENS_LETTER2) > 0L)
			{
				htmltext = "30150-01.htm";
				st.takeItems(ARUJIENS_LETTER2, -1L);
				st.giveItems(ARUJIENS_LETTER3, 1L, false);
				st.set("cond", "3");
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else if(cond == 3)
				htmltext = "30150-02.htm";
		}
		else if(npcId == GREENIS)
			if(cond == 4 && st.getQuestItemsCount(POETRY_BOOK) > 0L)
			{
				htmltext = "30157-02.htm";
				st.takeItems(POETRY_BOOK, -1L);
				st.giveItems(GREENIS_LETTER, 1L, false);
				st.set("cond", "5");
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else if(cond == 5 && st.getQuestItemsCount(GREENIS_LETTER) > 0L)
				htmltext = "30157-03.htm";
			else
				htmltext = "30157-01.htm";
		return htmltext;
	}
}
