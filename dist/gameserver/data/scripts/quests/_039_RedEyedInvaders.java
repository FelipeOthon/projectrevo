package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _039_RedEyedInvaders extends Quest implements ScriptFile
{
	int BBN;
	int RBN;
	int IP;
	int GML;
	int[] REW;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _039_RedEyedInvaders()
	{
		super(false);
		BBN = 7178;
		RBN = 7179;
		IP = 7180;
		GML = 7181;
		REW = new int[] { 6521, 6529, 6535 };
		this.addStartNpc(30334);
		this.addTalkId(new int[] { 30332 });
		this.addKillId(new int[] { 20919 });
		this.addKillId(new int[] { 20920 });
		this.addKillId(new int[] { 20921 });
		this.addKillId(new int[] { 20925 });
		addQuestItem(new int[] { BBN, IP, RBN, GML });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("guard_babenco_q0039_0104.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("captain_bathia_q0039_0201.htm"))
		{
			st.set("cond", "2");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("captain_bathia_q0039_0301.htm"))
		{
			if(st.getQuestItemsCount(BBN) == 100L && st.getQuestItemsCount(RBN) == 100L)
			{
				st.set("cond", "4");
				st.takeItems(BBN, -1L);
				st.takeItems(RBN, -1L);
				st.playSound(Quest.SOUND_ACCEPT);
			}
			else
				htmltext = "captain_bathia_q0039_0203.htm";
		}
		else if(event.equals("captain_bathia_q0039_0401.htm"))
			if(st.getQuestItemsCount(IP) == 30L && st.getQuestItemsCount(GML) == 30L)
			{
				st.takeItems(IP, -1L);
				st.takeItems(GML, -1L);
				st.giveItems(REW[0], 60L);
				st.giveItems(REW[1], 1L);
				st.giveItems(REW[2], 500L);
				st.addExpAndSp(62366L, 2783L, true);
				st.set("cond", "0");
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(false);
			}
			else
				htmltext = "captain_bathia_q0039_0304.htm";
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(npcId == 30334)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() < 20)
				{
					htmltext = "guard_babenco_q0039_0102.htm";
					st.exitCurrentQuest(true);
				}
				else if(st.getPlayer().getLevel() >= 20)
					htmltext = "guard_babenco_q0039_0101.htm";
			}
			else if(cond == 1)
				htmltext = "guard_babenco_q0039_0105.htm";
		}
		else if(npcId == 30332)
			if(cond == 1)
				htmltext = "captain_bathia_q0039_0101.htm";
			else if(cond == 2 && (st.getQuestItemsCount(BBN) < 100L || st.getQuestItemsCount(RBN) < 100L))
				htmltext = "captain_bathia_q0039_0203.htm";
			else if(cond == 3 && st.getQuestItemsCount(BBN) == 100L && st.getQuestItemsCount(RBN) == 100L)
				htmltext = "captain_bathia_q0039_0202.htm";
			else if(cond == 4 && (st.getQuestItemsCount(IP) < 30L || st.getQuestItemsCount(GML) < 30L))
				htmltext = "captain_bathia_q0039_0304.htm";
			else if(cond == 5 && st.getQuestItemsCount(IP) == 30L && st.getQuestItemsCount(GML) == 30L)
				htmltext = "captain_bathia_q0039_0303.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(cond == 2)
		{
			if((npcId == 20919 || npcId == 20920) && st.getQuestItemsCount(BBN) <= 99L)
				st.giveItems(BBN, 1L);
			else if(npcId == 20921 && st.getQuestItemsCount(RBN) <= 99L)
				st.giveItems(RBN, 1L);
			st.playSound(Quest.SOUND_ITEMGET);
			if(st.getQuestItemsCount(BBN) + st.getQuestItemsCount(RBN) == 200L)
			{
				st.set("cond", "3");
				st.playSound(Quest.SOUND_MIDDLE);
			}
		}
		if(cond == 4)
		{
			if((npcId == 20920 || npcId == 20921) && st.getQuestItemsCount(IP) <= 29L)
				st.giveItems(IP, 1L);
			else if(npcId == 20925 && st.getQuestItemsCount(GML) <= 29L)
				st.giveItems(GML, 1L);
			st.playSound(Quest.SOUND_ITEMGET);
			if(st.getQuestItemsCount(IP) + st.getQuestItemsCount(GML) == 60L)
			{
				st.set("cond", "5");
				st.playSound(Quest.SOUND_MIDDLE);
			}
		}
		return null;
	}
}
