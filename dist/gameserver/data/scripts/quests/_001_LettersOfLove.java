package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _001_LettersOfLove extends Quest implements ScriptFile
{
	public final int DARIN = 30048;
	public final int ROXXY = 30006;
	public final int BAULRO = 30033;
	public final short DARINGS_LETTER = 687;
	public final short RAPUNZELS_KERCHIEF = 688;
	public final short DARINGS_RECEIPT = 1079;
	public final short BAULS_POTION = 1080;
	public final short NECKLACE = 906;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _001_LettersOfLove()
	{
		super(false);
		this.addStartNpc(30048);
		this.addTalkId(new int[] { 30006 });
		this.addTalkId(new int[] { 30033 });
		addQuestItem(new int[] { 687 });
		addQuestItem(new int[] { 688 });
		addQuestItem(new int[] { 1079 });
		addQuestItem(new int[] { 1080 });
	}

	@Override
	public String onEvent(final String event, final QuestState qs, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("30048-06.htm"))
		{
			qs.set("cond", "1");
			qs.setState(2);
			qs.giveItems(687, 1L, false);
			qs.playSound(Quest.SOUND_ACCEPT);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		switch(npcId)
		{
			case 30048:
			{
				if(cond == 0)
				{
					if(st.getPlayer().getLevel() >= 2)
					{
						htmltext = "30048-02.htm";
						break;
					}
					htmltext = "30048-00.htm";
					st.exitCurrentQuest(true);
					break;
				}
				else
				{
					if(cond == 1)
					{
						htmltext = "30048-07.htm";
						break;
					}
					if(cond == 2 && st.getQuestItemsCount(688) == 1L)
					{
						htmltext = "30048-08.htm";
						st.takeItems(688, -1L);
						st.giveItems(1079, 1L, false);
						st.set("cond", "3");
						st.playSound(Quest.SOUND_MIDDLE);
						break;
					}
					if(cond == 3)
					{
						htmltext = "30048-09.htm";
						break;
					}
					if(cond == 4 && st.getQuestItemsCount(1080) == 1L)
					{
						htmltext = "30048-10.htm";
						st.takeItems(1080, -1L);
						st.giveItems(906, 1L, false);
						st.unset("cond");
						st.playSound(Quest.SOUND_FINISH);
						st.exitCurrentQuest(false);
						break;
					}
					break;
				}
			}
			case 30006:
			{
				if(cond == 1 && st.getQuestItemsCount(688) == 0L && st.getQuestItemsCount(687) > 0L)
				{
					htmltext = "30006-01.htm";
					st.takeItems(687, -1L);
					st.giveItems(688, 1L, false);
					st.set("cond", "2");
					st.playSound(Quest.SOUND_MIDDLE);
					break;
				}
				if(cond == 2 && st.getQuestItemsCount(688) > 0L)
				{
					htmltext = "30006-02.htm";
					break;
				}
				if(cond > 2 && (st.getQuestItemsCount(1080) > 0L || st.getQuestItemsCount(1079) > 0L))
				{
					htmltext = "30006-03.htm";
					break;
				}
				break;
			}
			case 30033:
			{
				if(cond == 3 && st.getQuestItemsCount(1079) == 1L)
				{
					htmltext = "30033-01.htm";
					st.takeItems(1079, -1L);
					st.giveItems(1080, 1L, false);
					st.set("cond", "4");
					st.playSound(Quest.SOUND_MIDDLE);
					break;
				}
				if(cond == 4)
				{
					htmltext = "30033-02.htm";
					break;
				}
				break;
			}
		}
		return htmltext;
	}
}
