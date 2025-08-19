package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _364_JovialAccordion extends Quest implements ScriptFile
{
	private static int BARBADO;
	private static int SWAN;
	private static int SABRIN;
	private static int BEER_CHEST;
	private static int CLOTH_CHEST;
	private static int KEY_1;
	private static int KEY_2;
	private static int BEER;
	private static int ECHO;

	public _364_JovialAccordion()
	{
		super(false);
		this.addStartNpc(_364_JovialAccordion.BARBADO);
		this.addTalkId(new int[] { _364_JovialAccordion.SWAN });
		this.addTalkId(new int[] { _364_JovialAccordion.SABRIN });
		this.addTalkId(new int[] { _364_JovialAccordion.BEER_CHEST });
		this.addTalkId(new int[] { _364_JovialAccordion.CLOTH_CHEST });
		addQuestItem(new int[] { _364_JovialAccordion.KEY_1 });
		addQuestItem(new int[] { _364_JovialAccordion.KEY_2 });
		addQuestItem(new int[] { _364_JovialAccordion.BEER });
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		if(st.getState() == 1)
		{
			if(npcId != _364_JovialAccordion.BARBADO)
				return htmltext;
			st.set("cond", "0");
			st.set("ok", "0");
		}
		final int cond = st.getInt("cond");
		if(npcId == _364_JovialAccordion.BARBADO)
		{
			if(cond == 0)
				htmltext = "30959-01.htm";
			else if(cond == 3)
			{
				htmltext = "30959-03.htm";
				st.giveItems(_364_JovialAccordion.ECHO, 1L);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
			else if(cond > 0)
				htmltext = "30959-02.htm";
		}
		else if(npcId == _364_JovialAccordion.SWAN)
		{
			if(cond == 1)
				htmltext = "30957-01.htm";
			else if(cond == 3)
				htmltext = "30957-05.htm";
			else if(cond == 2)
				if(st.getInt("ok") == 1 && st.getQuestItemsCount(_364_JovialAccordion.KEY_1) == 0L)
				{
					st.set("cond", "3");
					htmltext = "30957-04.htm";
				}
				else
					htmltext = "30957-03.htm";
		}
		else if(npcId == _364_JovialAccordion.SABRIN && cond == 2 && st.getQuestItemsCount(_364_JovialAccordion.BEER) > 0L)
		{
			st.set("ok", "1");
			st.takeItems(_364_JovialAccordion.BEER, -1L);
			htmltext = "30060-01.htm";
		}
		else if(npcId == _364_JovialAccordion.BEER_CHEST && cond == 2)
			htmltext = "30960-01.htm";
		else if(npcId == _364_JovialAccordion.CLOTH_CHEST && cond == 2)
			htmltext = "30961-01.htm";
		return htmltext;
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		final int _state = st.getState();
		final int cond = st.getInt("cond");
		if(event.equalsIgnoreCase("30959-02.htm") && _state == 1 && cond == 0)
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30957-02.htm") && _state == 2 && cond == 1)
		{
			st.set("cond", "2");
			st.giveItems(_364_JovialAccordion.KEY_1, 1L);
			st.giveItems(_364_JovialAccordion.KEY_2, 1L);
		}
		else if(event.equalsIgnoreCase("30960-03.htm") && cond == 2 && st.getQuestItemsCount(_364_JovialAccordion.KEY_2) > 0L)
		{
			st.takeItems(_364_JovialAccordion.KEY_2, -1L);
			st.giveItems(_364_JovialAccordion.BEER, 1L);
			htmltext = "30960-02.htm";
		}
		else if(event.equalsIgnoreCase("30961-03.htm") && cond == 2 && st.getQuestItemsCount(_364_JovialAccordion.KEY_1) > 0L)
		{
			st.takeItems(_364_JovialAccordion.KEY_1, -1L);
			htmltext = "30961-02.htm";
		}
		return htmltext;
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
		_364_JovialAccordion.BARBADO = 30959;
		_364_JovialAccordion.SWAN = 30957;
		_364_JovialAccordion.SABRIN = 30060;
		_364_JovialAccordion.BEER_CHEST = 30960;
		_364_JovialAccordion.CLOTH_CHEST = 30961;
		_364_JovialAccordion.KEY_1 = 4323;
		_364_JovialAccordion.KEY_2 = 4324;
		_364_JovialAccordion.BEER = 4321;
		_364_JovialAccordion.ECHO = 4421;
	}
}
