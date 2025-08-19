package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _037_PleaseMakeMeFormalWear extends Quest implements ScriptFile
{
	private static final int MYSTERIOUS_CLOTH = 7076;
	private static final int JEWEL_BOX = 7077;
	private static final int SEWING_KIT = 7078;
	private static final int DRESS_SHOES_BOX = 7113;
	private static final int SIGNET_RING = 7164;
	private static final int ICE_WINE = 7160;
	private static final int BOX_OF_COOKIES = 7159;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _037_PleaseMakeMeFormalWear()
	{
		super(false);
		this.addStartNpc(30842);
		this.addTalkId(new int[] { 30842 });
		this.addTalkId(new int[] { 31520 });
		this.addTalkId(new int[] { 31521 });
		this.addTalkId(new int[] { 31627 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("30842-1.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("31520-1.htm"))
		{
			st.giveItems(7164, 1L);
			st.set("cond", "2");
		}
		else if(event.equals("31521-1.htm"))
		{
			st.takeItems(7164, 1L);
			st.giveItems(7160, 1L);
			st.set("cond", "3");
		}
		else if(event.equals("31627-1.htm"))
		{
			if(st.getQuestItemsCount(7160) > 0L)
			{
				st.takeItems(7160, 1L);
				st.set("cond", "4");
			}
			else
				htmltext = "You don't have enough materials";
		}
		else if(event.equals("31521-3.htm"))
		{
			st.giveItems(7159, 1L);
			st.set("cond", "5");
		}
		else if(event.equals("31520-3.htm"))
		{
			st.takeItems(7159, 1L);
			st.set("cond", "6");
		}
		else if(event.equals("31520-5.htm"))
		{
			st.takeItems(7076, 1L);
			st.takeItems(7077, 1L);
			st.takeItems(7078, 1L);
			st.set("cond", "7");
		}
		else if(event.equals("31520-7.htm"))
			if(st.getQuestItemsCount(7113) > 0L)
			{
				st.takeItems(7113, 1L);
				st.giveItems(6408, 1L);
				st.unset("cond");
				st.playSound(Quest.SOUND_FINISH);
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
		if(npcId == 30842)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 60)
					htmltext = "30842-0.htm";
				else
				{
					htmltext = "30842-2.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				htmltext = "30842-1.htm";
		}
		else if(npcId == 31520)
		{
			if(cond == 1)
				htmltext = "31520-0.htm";
			else if(cond == 2)
				htmltext = "31520-1.htm";
			else if(cond == 5 || cond == 6)
			{
				if(st.getQuestItemsCount(7076) > 0L && st.getQuestItemsCount(7077) > 0L && st.getQuestItemsCount(7078) > 0L)
					htmltext = "31520-4.htm";
				else if(st.getQuestItemsCount(7159) > 0L)
					htmltext = "31520-2.htm";
				else
					htmltext = "31520-3.htm";
			}
			else if(cond == 7)
				if(st.getQuestItemsCount(7113) > 0L)
					htmltext = "31520-6.htm";
				else
					htmltext = "31520-5.htm";
		}
		else if(npcId == 31521)
		{
			if(st.getQuestItemsCount(7164) > 0L)
				htmltext = "31521-0.htm";
			else if(cond == 3)
				htmltext = "31521-1.htm";
			else if(cond == 4)
				htmltext = "31521-2.htm";
			else if(cond == 5)
				htmltext = "31521-3.htm";
		}
		else if(npcId == 31627)
			htmltext = "31627-0.htm";
		return htmltext;
	}
}
