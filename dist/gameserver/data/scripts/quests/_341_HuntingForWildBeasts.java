package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _341_HuntingForWildBeasts extends Quest implements ScriptFile
{
	private static int PANO;
	private static int Red_Bear;
	private static int Dion_Grizzly;
	private static int Brown_Bear;
	private static int Grizzly_Bear;
	private static int BEAR_SKIN;
	private static int BEAR_SKIN_CHANCE;

	public _341_HuntingForWildBeasts()
	{
		super(false);
		this.addStartNpc(_341_HuntingForWildBeasts.PANO);
		this.addKillId(new int[] { _341_HuntingForWildBeasts.Red_Bear });
		this.addKillId(new int[] { _341_HuntingForWildBeasts.Dion_Grizzly });
		this.addKillId(new int[] { _341_HuntingForWildBeasts.Brown_Bear });
		this.addKillId(new int[] { _341_HuntingForWildBeasts.Grizzly_Bear });
		addQuestItem(new int[] { _341_HuntingForWildBeasts.BEAR_SKIN });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("quest_accept") && st.getState() == 1)
		{
			htmltext = "pano_q0341_04.htm";
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		if(npc.getNpcId() != _341_HuntingForWildBeasts.PANO)
			return htmltext;
		final int _state = st.getState();
		if(_state == 1)
		{
			if(st.getPlayer().getLevel() >= 20)
			{
				htmltext = "pano_q0341_01.htm";
				st.set("cond", "0");
			}
			else
			{
				htmltext = "pano_q0341_02.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(_state == 2)
			if(st.getQuestItemsCount(_341_HuntingForWildBeasts.BEAR_SKIN) >= 20L)
			{
				htmltext = "pano_q0341_05.htm";
				st.takeItems(_341_HuntingForWildBeasts.BEAR_SKIN, -1L);
				st.giveItems(57, 3710L);
				st.playSound(Quest.SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
			else
				htmltext = "pano_q0341_06.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState qs)
	{
		if(qs.getState() != 2)
			return null;
		final long BEAR_SKIN_COUNT = qs.getQuestItemsCount(_341_HuntingForWildBeasts.BEAR_SKIN);
		if(BEAR_SKIN_COUNT < 20L && Rnd.chance(_341_HuntingForWildBeasts.BEAR_SKIN_CHANCE))
		{
			qs.giveItems(_341_HuntingForWildBeasts.BEAR_SKIN, 1L);
			if(BEAR_SKIN_COUNT == 19L)
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
		_341_HuntingForWildBeasts.PANO = 30078;
		_341_HuntingForWildBeasts.Red_Bear = 20021;
		_341_HuntingForWildBeasts.Dion_Grizzly = 20203;
		_341_HuntingForWildBeasts.Brown_Bear = 20310;
		_341_HuntingForWildBeasts.Grizzly_Bear = 20335;
		_341_HuntingForWildBeasts.BEAR_SKIN = 4259;
		_341_HuntingForWildBeasts.BEAR_SKIN_CHANCE = 40;
	}
}
