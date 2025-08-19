package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _262_TradewiththeIvoryTower extends Quest implements ScriptFile
{
	public final int VOLODOS = 30137;
	public final int GREEN_FUNGUS = 20007;
	public final int BLOOD_FUNGUS = 20400;
	public final int FUNGUS_SAC = 707;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _262_TradewiththeIvoryTower()
	{
		super(false);
		this.addStartNpc(30137);
		this.addKillId(new int[] { 20400, 20007 });
		addQuestItem(new int[] { 707 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equals("vollodos_q0262_03.htm"))
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
		final int cond = st.getInt("cond");
		if(cond == 0)
		{
			if(st.getPlayer().getLevel() >= 8)
			{
				htmltext = "vollodos_q0262_02.htm";
				return htmltext;
			}
			htmltext = "vollodos_q0262_01.htm";
			st.exitCurrentQuest(true);
		}
		else if(cond == 1 && st.getQuestItemsCount(707) < 10L)
			htmltext = "vollodos_q0262_04.htm";
		else if(cond == 2 && st.getQuestItemsCount(707) >= 10L)
		{
			st.giveItems(57, 3000L);
			st.takeItems(707, -1L);
			st.set("cond", "0");
			st.playSound(Quest.SOUND_FINISH);
			htmltext = "vollodos_q0262_05.htm";
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int random = Rnd.get(10);
		if(st.getInt("cond") == 1 && st.getQuestItemsCount(707) < 10L && (npcId == 20007 && random < 3 || npcId == 20400 && random < 4))
		{
			st.giveItems(707, 1L);
			if(st.getQuestItemsCount(707) == 10L)
			{
				st.set("cond", "2");
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else
				st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}
}
