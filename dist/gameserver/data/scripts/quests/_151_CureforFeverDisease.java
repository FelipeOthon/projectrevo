package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;
import l2s.gameserver.scripts.ScriptFile;

public class _151_CureforFeverDisease extends Quest implements ScriptFile
{
	int POISON_SAC;
	int FEVER_MEDICINE;
	int ROUND_SHIELD;

	public _151_CureforFeverDisease()
	{
		super(false);
		POISON_SAC = 703;
		FEVER_MEDICINE = 704;
		ROUND_SHIELD = 102;
		this.addStartNpc(30050);
		this.addTalkId(new int[] { 30032 });
		this.addKillId(new int[] { 20103, 20106, 20108 });
		addQuestItem(new int[] { FEVER_MEDICINE, POISON_SAC });
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

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("quest_accept"))
		{
			htmltext = "elias_q0151_03.htm";
			st.setCond(1);
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int id = st.getState();
		int cond = 0;
		if(id != 1)
			cond = st.getCond();
		if(npcId == 30050)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 15)
					htmltext = "elias_q0151_02.htm";
				else
				{
					htmltext = "elias_q0151_01.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1 && st.getQuestItemsCount(POISON_SAC) == 0L && st.getQuestItemsCount(FEVER_MEDICINE) == 0L)
				htmltext = "elias_q0151_04.htm";
			else if(cond == 1 && st.getQuestItemsCount(POISON_SAC) == 1L)
				htmltext = "elias_q0151_05.htm";
			else if(cond == 3 && st.getQuestItemsCount(FEVER_MEDICINE) == 1L)
			{
				st.takeItems(FEVER_MEDICINE, -1L);
				st.giveItems(ROUND_SHIELD, 1L);
				st.getPlayer().addExpAndSp(13106L, 613L, false, false);
				if(st.getPlayer().getClassId().getLevel() == 1 && !st.getPlayer().getVarBoolean("p1q4"))
				{
					st.getPlayer().setVar("p1q4", "1");
					st.getPlayer().sendPacket(new ExShowScreenMessage("Now go find the Newbie Guide.", 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true));
				}
				htmltext = "elias_q0151_06.htm";
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(false);
			}
		}
		else if(npcId == 30032)
			if(cond == 2 && st.getQuestItemsCount(POISON_SAC) > 0L)
			{
				st.giveItems(FEVER_MEDICINE, 1L);
				st.takeItems(POISON_SAC, -1L);
				st.setCond(3);
				htmltext = "yohan_q0151_01.htm";
			}
			else if(cond == 3 && st.getQuestItemsCount(FEVER_MEDICINE) > 0L)
				htmltext = "yohan_q0151_02.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		if((npcId == 20103 || npcId == 20106 || npcId == 20108) && st.getQuestItemsCount(POISON_SAC) == 0L && st.getCond() == 1 && Rnd.nextBoolean())
		{
			st.setCond(2);
			st.giveItems(POISON_SAC, 1L);
			st.playSound(Quest.SOUND_MIDDLE);
		}
		return null;
	}
}
