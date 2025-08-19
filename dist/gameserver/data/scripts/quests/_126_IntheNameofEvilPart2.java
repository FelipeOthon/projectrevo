package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _126_IntheNameofEvilPart2 extends Quest implements ScriptFile
{
	private int Mushika;
	private int Asamah;
	private int UluKaimu;
	private int BaluKaimu;
	private int ChutaKaimu;
	private int WarriorGrave;
	private int ShilenStoneStatue;
	private int BONEPOWDER;
	private int EPITAPH;
	private int EWA;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _126_IntheNameofEvilPart2()
	{
		super(false);
		Mushika = 32114;
		Asamah = 32115;
		UluKaimu = 32119;
		BaluKaimu = 32120;
		ChutaKaimu = 32121;
		WarriorGrave = 32122;
		ShilenStoneStatue = 32109;
		BONEPOWDER = 8783;
		EPITAPH = 8781;
		EWA = 729;
		this.addStartNpc(Asamah);
		this.addTalkId(new int[] { Mushika });
		this.addTalkId(new int[] { UluKaimu });
		this.addTalkId(new int[] { BaluKaimu });
		this.addTalkId(new int[] { ChutaKaimu });
		this.addTalkId(new int[] { WarriorGrave });
		this.addTalkId(new int[] { ShilenStoneStatue });
		addQuestItem(new int[] { BONEPOWDER, EPITAPH });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("asamah_q126_4.htm"))
		{
			st.setState(2);
			st.setCond(1);
			st.playSound(Quest.SOUND_ACCEPT);
			st.takeAllItems(EPITAPH);
		}
		else if(event.equalsIgnoreCase("asamah_q126_7.htm"))
		{
			st.setCond(2);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("ulukaimu_q126_2.htm"))
		{
			st.setCond(3);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("ulukaimu_q126_8.htm"))
		{
			st.setCond(4);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("ulukaimu_q126_10.htm"))
		{
			st.setCond(5);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("balukaimu_q126_2.htm"))
		{
			st.setCond(6);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("balukaimu_q126_7.htm"))
		{
			st.setCond(7);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("balukaimu_q126_9.htm"))
		{
			st.setCond(8);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("chutakaimu_q126_2.htm"))
		{
			st.setCond(9);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("chutakaimu_q126_9.htm"))
		{
			st.setCond(10);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("chutakaimu_q126_14.htm"))
		{
			st.setCond(11);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("warriorgrave_q126_2.htm"))
		{
			st.setCond(12);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("warriorgrave_q126_10.htm"))
		{
			st.setCond(13);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("warriorgrave_q126_19.htm"))
		{
			st.setCond(14);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("warriorgrave_q126_20.htm"))
		{
			st.setCond(15);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("warriorgrave_q126_23.htm"))
		{
			st.setCond(16);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("warriorgrave_q126_25.htm"))
		{
			st.setCond(17);
			st.giveItems(BONEPOWDER, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("warriorgrave_q126_27.htm"))
		{
			st.setCond(18);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("shilenstatue_q126_2.htm"))
		{
			st.setCond(19);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("shilenstatue_q126_13.htm"))
		{
			st.setCond(20);
			st.takeAllItems(BONEPOWDER);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("asamah_q126_10.htm"))
		{
			st.setCond(21);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("asamah_q126_17.htm"))
		{
			st.setCond(22);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("mushika_q126_3.htm"))
		{
			st.setCond(23);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("mushika_q126_4.htm"))
		{
			st.giveItems(EWA, 1L);
			st.giveItems(57, 460483L);
			st.addExpAndSp(1015973L, 102802L);
			st.playSound(Quest.SOUND_FINISH);
			st.setState(3);
			st.exitCurrentQuest(false);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getCond();
		if(npcId == Asamah)
		{
			if(cond == 0)
			{
				final QuestState qs = st.getPlayer().getQuestState(125);
				if(st.getPlayer().getLevel() >= 77 && qs != null && qs.isCompleted())
					htmltext = "asamah_q126_1.htm";
				else
				{
					htmltext = "asamah_q126_0.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				htmltext = "asamah_q126_4.htm";
			else if(cond == 20)
				htmltext = "asamah_q126_8.htm";
			else if(cond == 21)
				htmltext = "asamah_q126_10.htm";
			else if(cond == 22)
				htmltext = "asamah_q126_17.htm";
			else
				htmltext = "asamah_q126_0a.htm";
		}
		else if(npcId == UluKaimu)
		{
			if(cond == 2)
				htmltext = "ulukaimu_q126_1.htm";
			else if(cond == 3)
				htmltext = "ulukaimu_q126_2.htm";
			else if(cond == 4)
				htmltext = "ulukaimu_q126_8.htm";
			else if(cond == 5)
				htmltext = "ulukaimu_q126_10.htm";
			else
				htmltext = "ulukaimu_q126_0.htm";
		}
		else if(npcId == BaluKaimu)
		{
			if(cond == 5)
				htmltext = "balukaimu_q126_1.htm";
			else if(cond == 6)
				htmltext = "balukaimu_q126_2.htm";
			else if(cond == 7)
				htmltext = "balukaimu_q126_7.htm";
			else if(cond == 8)
				htmltext = "balukaimu_q126_9.htm";
			else
				htmltext = "balukaimu_q126_0.htm";
		}
		else if(npcId == ChutaKaimu)
		{
			if(cond == 8)
				htmltext = "chutakaimu_q126_1.htm";
			else if(cond == 9)
				htmltext = "chutakaimu_q126_2.htm";
			else if(cond == 10)
				htmltext = "chutakaimu_q126_9.htm";
			else if(cond == 11)
				htmltext = "chutakaimu_q126_14.htm";
			else
				htmltext = "chutakaimu_q126_0.htm";
		}
		else if(npcId == WarriorGrave)
		{
			if(cond == 11)
				htmltext = "warriorgrave_q126_1.htm";
			else if(cond == 12)
				htmltext = "warriorgrave_q126_2.htm";
			else if(cond == 13)
				htmltext = "warriorgrave_q126_10.htm";
			else if(cond == 14)
				htmltext = "warriorgrave_q126_19.htm";
			else if(cond == 15)
				htmltext = "warriorgrave_q126_20.htm";
			else if(cond == 16)
				htmltext = "warriorgrave_q126_23.htm";
			else if(cond == 17)
				htmltext = "warriorgrave_q126_25.htm";
			else if(cond == 18)
				htmltext = "warriorgrave_q126_27.htm";
			else
				htmltext = "warriorgrave_q126_0.htm";
		}
		else if(npcId == ShilenStoneStatue)
		{
			if(cond == 18)
				htmltext = "shilenstatue_q126_1.htm";
			else if(cond == 19)
				htmltext = "shilenstatue_q126_2.htm";
			else if(cond == 20)
				htmltext = "shilenstatue_q126_13.htm";
			else
				htmltext = "shilenstatue_q126_0.htm";
		}
		else if(npcId == Mushika)
			if(cond == 22)
				htmltext = "mushika_q126_1.htm";
			else if(cond == 23)
				htmltext = "mushika_q126_3.htm";
			else
				htmltext = "mushika_q126_0.htm";
		return htmltext;
	}
}
