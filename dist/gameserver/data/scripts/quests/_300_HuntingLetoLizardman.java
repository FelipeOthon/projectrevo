package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _300_HuntingLetoLizardman extends Quest implements ScriptFile
{
	private static int RATH;
	private static int BRACELET_OF_LIZARDMAN;
	private static int ANIMAL_BONE;
	private static int ANIMAL_SKIN;
	private static int BRACELET_OF_LIZARDMAN_CHANCE;

	public _300_HuntingLetoLizardman()
	{
		super(false);
		this.addStartNpc(_300_HuntingLetoLizardman.RATH);
		for(int lizardman_id = 20577; lizardman_id <= 20582; ++lizardman_id)
			this.addKillId(new int[] { lizardman_id });
		addQuestItem(new int[] { _300_HuntingLetoLizardman.BRACELET_OF_LIZARDMAN });
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		if(npc.getNpcId() != _300_HuntingLetoLizardman.RATH)
			return htmltext;
		if(st.getState() == 1)
		{
			if(st.getPlayer().getLevel() < 34)
			{
				htmltext = "rarshints_q0300_0103.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "rarshints_q0300_0101.htm";
				st.set("cond", "0");
			}
		}
		else if(st.getQuestItemsCount(_300_HuntingLetoLizardman.BRACELET_OF_LIZARDMAN) < 60L)
		{
			htmltext = "rarshints_q0300_0106.htm";
			st.set("cond", "1");
		}
		else
			htmltext = "rarshints_q0300_0105.htm";
		return htmltext;
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		final int _state = st.getState();
		if(event.equalsIgnoreCase("rarshints_q0300_0104.htm") && _state == 1)
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("rarshints_q0300_0201.htm") && _state == 2)
			if(st.getQuestItemsCount(_300_HuntingLetoLizardman.BRACELET_OF_LIZARDMAN) < 60L)
			{
				htmltext = "rarshints_q0300_0202.htm";
				st.set("cond", "1");
			}
			else
			{
				st.takeItems(_300_HuntingLetoLizardman.BRACELET_OF_LIZARDMAN, -1L);
				switch(Rnd.get(3))
				{
					case 0:
					{
						st.giveItems(57, 30000L, true);
						break;
					}
					case 1:
					{
						st.giveItems(_300_HuntingLetoLizardman.ANIMAL_BONE, 50L, true);
						break;
					}
					case 2:
					{
						st.giveItems(_300_HuntingLetoLizardman.ANIMAL_SKIN, 50L, true);
						break;
					}
				}
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState qs)
	{
		if(qs.getState() != 2)
			return null;
		final long _count = qs.getQuestItemsCount(_300_HuntingLetoLizardman.BRACELET_OF_LIZARDMAN);
		if(_count < 60L && Rnd.chance(_300_HuntingLetoLizardman.BRACELET_OF_LIZARDMAN_CHANCE))
		{
			qs.giveItems(_300_HuntingLetoLizardman.BRACELET_OF_LIZARDMAN, 1L);
			if(_count == 59L)
			{
				qs.set("cond", "2");
				qs.playSound(Quest.SOUND_MIDDLE);
			}
			else
				qs.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
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
		_300_HuntingLetoLizardman.RATH = 30126;
		_300_HuntingLetoLizardman.BRACELET_OF_LIZARDMAN = 7139;
		_300_HuntingLetoLizardman.ANIMAL_BONE = 1872;
		_300_HuntingLetoLizardman.ANIMAL_SKIN = 1867;
		_300_HuntingLetoLizardman.BRACELET_OF_LIZARDMAN_CHANCE = 70;
	}
}
