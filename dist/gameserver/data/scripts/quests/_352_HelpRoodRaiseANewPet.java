package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _352_HelpRoodRaiseANewPet extends Quest implements ScriptFile
{
	private static int Rood;
	private static int Lienrik;
	private static int Lienrik_Lad;
	private static int LIENRIK_EGG1;
	private static int LIENRIK_EGG2;
	private static int LIENRIK_EGG1_Chance;
	private static int LIENRIK_EGG2_Chance;

	public _352_HelpRoodRaiseANewPet()
	{
		super(false);
		this.addStartNpc(_352_HelpRoodRaiseANewPet.Rood);
		this.addKillId(new int[] { _352_HelpRoodRaiseANewPet.Lienrik });
		this.addKillId(new int[] { _352_HelpRoodRaiseANewPet.Lienrik_Lad });
		addQuestItem(new int[] { _352_HelpRoodRaiseANewPet.LIENRIK_EGG1 });
		addQuestItem(new int[] { _352_HelpRoodRaiseANewPet.LIENRIK_EGG2 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int _state = st.getState();
		if(event.equalsIgnoreCase("31067-04.htm") && _state == 1)
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("31067-09.htm") && _state == 2)
		{
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		if(npc.getNpcId() != _352_HelpRoodRaiseANewPet.Rood)
			return htmltext;
		final int _state = st.getState();
		if(_state == 1)
		{
			if(st.getPlayer().getLevel() < 39)
			{
				htmltext = "31067-00.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "31067-01.htm";
				st.set("cond", "0");
			}
		}
		else if(_state == 2)
		{
			final long reward = st.getQuestItemsCount(_352_HelpRoodRaiseANewPet.LIENRIK_EGG1) * 209L + st.getQuestItemsCount(_352_HelpRoodRaiseANewPet.LIENRIK_EGG2) * 2050L;
			if(reward > 0L)
			{
				htmltext = "31067-08.htm";
				st.takeItems(_352_HelpRoodRaiseANewPet.LIENRIK_EGG1, -1L);
				st.takeItems(_352_HelpRoodRaiseANewPet.LIENRIK_EGG2, -1L);
				st.giveItems(57, reward);
				st.playSound(Quest.SOUND_MIDDLE);
			}
			else
				htmltext = "31067-05.htm";
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState qs)
	{
		if(qs.getState() != 2)
			return null;
		if(Rnd.chance(_352_HelpRoodRaiseANewPet.LIENRIK_EGG1_Chance))
		{
			qs.giveItems(_352_HelpRoodRaiseANewPet.LIENRIK_EGG1, 1L);
			qs.playSound(Quest.SOUND_ITEMGET);
		}
		else if(Rnd.chance(_352_HelpRoodRaiseANewPet.LIENRIK_EGG2_Chance))
		{
			qs.giveItems(_352_HelpRoodRaiseANewPet.LIENRIK_EGG2, 1L);
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
		_352_HelpRoodRaiseANewPet.Rood = 31067;
		_352_HelpRoodRaiseANewPet.Lienrik = 20786;
		_352_HelpRoodRaiseANewPet.Lienrik_Lad = 20787;
		_352_HelpRoodRaiseANewPet.LIENRIK_EGG1 = 5860;
		_352_HelpRoodRaiseANewPet.LIENRIK_EGG2 = 5861;
		_352_HelpRoodRaiseANewPet.LIENRIK_EGG1_Chance = 30;
		_352_HelpRoodRaiseANewPet.LIENRIK_EGG2_Chance = 7;
	}
}
