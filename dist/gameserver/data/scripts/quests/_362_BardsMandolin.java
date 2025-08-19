package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _362_BardsMandolin extends Quest implements ScriptFile
{
	private static int SWAN;
	private static int NANARIN;
	private static int GALION;
	private static int WOODROW;
	private static int SWANS_FLUTE;
	private static int SWANS_LETTER;
	private static int Musical_Score__Theme_of_Journey;

	public _362_BardsMandolin()
	{
		super(false);
		this.addStartNpc(_362_BardsMandolin.SWAN);
		this.addTalkId(new int[] { _362_BardsMandolin.NANARIN });
		this.addTalkId(new int[] { _362_BardsMandolin.GALION });
		this.addTalkId(new int[] { _362_BardsMandolin.WOODROW });
		addQuestItem(new int[] { _362_BardsMandolin.SWANS_FLUTE });
		addQuestItem(new int[] { _362_BardsMandolin.SWANS_LETTER });
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		if(st.getState() == 1)
		{
			if(npcId != _362_BardsMandolin.SWAN)
				return htmltext;
			st.set("cond", "0");
		}
		final int cond = st.getInt("cond");
		if(npcId == _362_BardsMandolin.SWAN)
		{
			if(cond == 0)
				htmltext = "30957_1.htm";
			else if(cond == 3 && st.getQuestItemsCount(_362_BardsMandolin.SWANS_FLUTE) > 0L && st.getQuestItemsCount(_362_BardsMandolin.SWANS_LETTER) == 0L)
			{
				htmltext = "30957_3.htm";
				st.set("cond", "4");
				st.giveItems(_362_BardsMandolin.SWANS_LETTER, 1L);
			}
			else if(cond == 4 && st.getQuestItemsCount(_362_BardsMandolin.SWANS_FLUTE) > 0L && st.getQuestItemsCount(_362_BardsMandolin.SWANS_LETTER) > 0L)
				htmltext = "30957_6.htm";
			else if(cond == 5)
				htmltext = "30957_4.htm";
		}
		else if(npcId == _362_BardsMandolin.WOODROW && cond == 1)
		{
			htmltext = "30837_1.htm";
			st.set("cond", "2");
		}
		else if(npcId == _362_BardsMandolin.GALION && cond == 2)
		{
			htmltext = "30958_1.htm";
			st.set("cond", "3");
			st.giveItems(_362_BardsMandolin.SWANS_FLUTE, 1L);
			st.playSound(Quest.SOUND_ITEMGET);
		}
		else if(npcId == _362_BardsMandolin.NANARIN && cond == 4 && st.getQuestItemsCount(_362_BardsMandolin.SWANS_FLUTE) > 0L && st.getQuestItemsCount(_362_BardsMandolin.SWANS_LETTER) > 0L)
		{
			htmltext = "30956_1.htm";
			st.takeItems(_362_BardsMandolin.SWANS_FLUTE, 1L);
			st.takeItems(_362_BardsMandolin.SWANS_LETTER, 1L);
			st.set("cond", "5");
		}
		return htmltext;
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int _state = st.getState();
		final int cond = st.getInt("cond");
		if(event.equalsIgnoreCase("30957_2.htm") && _state == 1 && cond == 0)
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30957_5.htm") && _state == 2 && cond == 5)
		{
			st.giveItems(57, 10000L);
			st.giveItems(_362_BardsMandolin.Musical_Score__Theme_of_Journey, 1L);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return event;
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

	static
	{
		_362_BardsMandolin.SWAN = 30957;
		_362_BardsMandolin.NANARIN = 30956;
		_362_BardsMandolin.GALION = 30958;
		_362_BardsMandolin.WOODROW = 30837;
		_362_BardsMandolin.SWANS_FLUTE = 4316;
		_362_BardsMandolin.SWANS_LETTER = 4317;
		_362_BardsMandolin.Musical_Score__Theme_of_Journey = 4410;
	}
}
