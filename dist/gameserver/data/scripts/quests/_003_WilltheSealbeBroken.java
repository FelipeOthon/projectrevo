package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _003_WilltheSealbeBroken extends Quest implements ScriptFile
{
	int StartNpc;
	int[] Monster;
	int OnyxBeastEye;
	int TaintStone;
	int SuccubusBlood;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _003_WilltheSealbeBroken()
	{
		super(false);
		StartNpc = 30141;
		Monster = new int[] { 20031, 20041, 20046, 20048, 20052, 20057 };
		OnyxBeastEye = 1081;
		TaintStone = 1082;
		SuccubusBlood = 1083;
		this.addStartNpc(StartNpc);
		for(final int npcId : Monster)
			this.addKillId(new int[] { npcId });
		addQuestItem(new int[] { OnyxBeastEye, TaintStone, SuccubusBlood });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("30141-03.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int id = st.getState();
		if(id == 1)
		{
			if(st.getPlayer().getRace().ordinal() != 2)
			{
				htmltext = "30141-00.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				if(st.getPlayer().getLevel() >= 16)
				{
					htmltext = "30141-02.htm";
					return htmltext;
				}
				htmltext = "30141-01.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(id == 2)
			if(st.getQuestItemsCount(OnyxBeastEye) > 0L && st.getQuestItemsCount(TaintStone) > 0L && st.getQuestItemsCount(SuccubusBlood) > 0L)
			{
				htmltext = "30141-06.htm";
				st.takeItems(OnyxBeastEye, -1L);
				st.takeItems(TaintStone, -1L);
				st.takeItems(SuccubusBlood, -1L);
				st.giveItems(956, 1L, true);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(false);
			}
			else
				htmltext = "30141-04.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int id = st.getState();
		if(id == 2)
		{
			if(npcId == Monster[0] && st.getQuestItemsCount(OnyxBeastEye) == 0L)
			{
				st.giveItems(OnyxBeastEye, 1L, false);
				st.playSound(Quest.SOUND_ITEMGET);
			}
			else if((npcId == Monster[1] || npcId == Monster[2]) && st.getQuestItemsCount(TaintStone) == 0L)
			{
				st.giveItems(TaintStone, 1L, false);
				st.playSound(Quest.SOUND_ITEMGET);
			}
			else if((npcId == Monster[3] || npcId == Monster[4] || npcId == Monster[5]) && st.getQuestItemsCount(SuccubusBlood) == 0L)
			{
				st.giveItems(SuccubusBlood, 1L, false);
				st.playSound(Quest.SOUND_ITEMGET);
			}
			if(st.getQuestItemsCount(OnyxBeastEye) > 0L && st.getQuestItemsCount(TaintStone) > 0L && st.getQuestItemsCount(SuccubusBlood) > 0L)
			{
				st.set("cond", "2");
				st.playSound(Quest.SOUND_MIDDLE);
			}
		}
		return null;
	}
}
