package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _643_RiseAndFallOfTheElrokiTribe extends Quest implements ScriptFile
{
	private static int DROP_CHANCE;
	private static int BONES_OF_A_PLAINS_DINOSAUR;
	private static int[] PLAIN_DINOSAURS;
	private static int[] REWARDS;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _643_RiseAndFallOfTheElrokiTribe()
	{
		super(true);
		this.addStartNpc(32106);
		this.addTalkId(new int[] { 32117 });
		for(final int npc : _643_RiseAndFallOfTheElrokiTribe.PLAIN_DINOSAURS)
			this.addKillId(new int[] { npc });
		addQuestItem(new int[] { _643_RiseAndFallOfTheElrokiTribe.BONES_OF_A_PLAINS_DINOSAUR });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("32106-03.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("32117-03.htm"))
		{
			final long count = st.getQuestItemsCount(_643_RiseAndFallOfTheElrokiTribe.BONES_OF_A_PLAINS_DINOSAUR);
			if(count >= 300L)
			{
				st.takeItems(_643_RiseAndFallOfTheElrokiTribe.BONES_OF_A_PLAINS_DINOSAUR, 300L);
				st.giveItems(_643_RiseAndFallOfTheElrokiTribe.REWARDS[Rnd.get(_643_RiseAndFallOfTheElrokiTribe.REWARDS.length)], 5L, false);
			}
			else
				htmltext = "32117-04.htm";
		}
		else if(event.equalsIgnoreCase("None"))
			htmltext = null;
		else if(event.equalsIgnoreCase("Quit"))
		{
			htmltext = null;
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		if(st.getInt("cond") == 0)
		{
			if(st.getPlayer().getLevel() >= 75)
				htmltext = "32106-01.htm";
			else
			{
				htmltext = "32106-00.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(st.getState() == 2)
		{
			final int npcId = npc.getNpcId();
			if(npcId == 32106)
			{
				final long count = st.getQuestItemsCount(_643_RiseAndFallOfTheElrokiTribe.BONES_OF_A_PLAINS_DINOSAUR);
				if(count == 0L)
					htmltext = "32106-05.htm";
				else
				{
					htmltext = "32106-06.htm";
					st.takeItems(_643_RiseAndFallOfTheElrokiTribe.BONES_OF_A_PLAINS_DINOSAUR, -1L);
					st.giveItems(57, count * 1374L, false);
				}
			}
			else if(npcId == 32117)
				htmltext = "32117-01.htm";
		}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getInt("cond") == 1)
			st.rollAndGive(_643_RiseAndFallOfTheElrokiTribe.BONES_OF_A_PLAINS_DINOSAUR, 1, _643_RiseAndFallOfTheElrokiTribe.DROP_CHANCE);
		return null;
	}

	static
	{
		_643_RiseAndFallOfTheElrokiTribe.DROP_CHANCE = 75;
		_643_RiseAndFallOfTheElrokiTribe.BONES_OF_A_PLAINS_DINOSAUR = 8776;
		_643_RiseAndFallOfTheElrokiTribe.PLAIN_DINOSAURS = new int[] { 22208, 22209, 22210, 22211, 22212, 22213, 22221, 22222, 22226, 22227 };
		_643_RiseAndFallOfTheElrokiTribe.REWARDS = new int[] { 8712, 8713, 8714, 8715, 8716, 8717, 8718, 8719, 8720, 8721, 8722 };
	}
}
