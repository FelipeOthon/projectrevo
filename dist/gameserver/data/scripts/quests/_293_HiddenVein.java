package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.network.l2.s2c.ExShowScreenMessage;
import l2s.gameserver.scripts.ScriptFile;

public class _293_HiddenVein extends Quest implements ScriptFile
{
	private static int Filaur;
	private static int Chichirin;
	private static int Utuku_Orc;
	private static int Utuku_Orc_Archer;
	private static int Utuku_Orc_Grunt;
	private static int Chrysolite_Ore;
	private static int Torn_Map_Fragment;
	private static int Hidden_Ore_Map;
	private static int Torn_Map_Fragment_Chance;
	private static int Chrysolite_Ore_Chance;

	public _293_HiddenVein()
	{
		super(false);
		this.addStartNpc(_293_HiddenVein.Filaur);
		this.addTalkId(new int[] { _293_HiddenVein.Chichirin });
		this.addKillId(new int[] { _293_HiddenVein.Utuku_Orc });
		this.addKillId(new int[] { _293_HiddenVein.Utuku_Orc_Archer });
		this.addKillId(new int[] { _293_HiddenVein.Utuku_Orc_Grunt });
		addQuestItem(new int[] { _293_HiddenVein.Chrysolite_Ore });
		addQuestItem(new int[] { _293_HiddenVein.Torn_Map_Fragment });
		addQuestItem(new int[] { _293_HiddenVein.Hidden_Ore_Map });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int _state = st.getState();
		if(event.equalsIgnoreCase("elder_filaur_q0293_03.htm") && _state == 1)
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("elder_filaur_q0293_06.htm") && _state == 2)
		{
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		else if(event.equalsIgnoreCase("chichirin_q0293_03.htm") && _state == 2)
		{
			if(st.getQuestItemsCount(_293_HiddenVein.Torn_Map_Fragment) < 4L)
				return "chichirin_q0293_02.htm";
			st.takeItems(_293_HiddenVein.Torn_Map_Fragment, 4L);
			st.giveItems(_293_HiddenVein.Hidden_Ore_Map, 1L);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int _state = st.getState();
		final int npcId = npc.getNpcId();
		if(_state == 1)
		{
			if(npcId != _293_HiddenVein.Filaur)
				return "noquest";
			if(st.getPlayer().getRace() != Race.dwarf)
			{
				st.exitCurrentQuest(true);
				return "elder_filaur_q0293_00.htm";
			}
			if(st.getPlayer().getLevel() < 6)
			{
				st.exitCurrentQuest(true);
				return "elder_filaur_q0293_01.htm";
			}
			st.set("cond", "0");
			return "elder_filaur_q0293_02.htm";
		}
		else
		{
			if(_state != 2)
				return "noquest";
			if(npcId == _293_HiddenVein.Filaur)
			{
				final long Chrysolite_Ore_count = st.getQuestItemsCount(_293_HiddenVein.Chrysolite_Ore);
				final long Hidden_Ore_Map_count = st.getQuestItemsCount(_293_HiddenVein.Hidden_Ore_Map);
				final long reward = st.getQuestItemsCount(_293_HiddenVein.Chrysolite_Ore) * 10L + st.getQuestItemsCount(_293_HiddenVein.Hidden_Ore_Map) * 1000L;
				if(reward == 0L)
					return "elder_filaur_q0293_04.htm";
				if(Chrysolite_Ore_count > 0L)
					st.takeItems(_293_HiddenVein.Chrysolite_Ore, -1L);
				if(Hidden_Ore_Map_count > 0L)
					st.takeItems(_293_HiddenVein.Hidden_Ore_Map, -1L);
				st.giveItems(57, reward);
				if(st.getPlayer().getClassId().getLevel() == 1 && !st.getPlayer().getVarBoolean("p1q2"))
				{
					st.getPlayer().setVar("p1q2", "1");
					st.getPlayer().sendPacket(new ExShowScreenMessage("Acquisition of Soulshot for beginners complete.\n                  Go find the Newbie Guide.", 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true));
					final QuestState qs = st.getPlayer().getQuestState(255);
					if(qs != null && qs.getInt("Ex") != 10)
					{
						st.showQuestionMark(26);
						qs.set("Ex", "10");
						if(st.getPlayer().isMageClass())
						{
							st.playTutorialVoice("tutorial_voice_027");
							st.giveItems(5790, 3000L);
						}
						else
						{
							st.playTutorialVoice("tutorial_voice_026");
							st.giveItems(5789, 6000L);
						}
					}
				}
				return Chrysolite_Ore_count > 0L && Hidden_Ore_Map_count > 0L ? "elder_filaur_q0293_09.htm" : Hidden_Ore_Map_count > 0L ? "elder_filaur_q0293_08.htm" : "elder_filaur_q0293_05.htm";
			}
			else
			{
				if(npcId == _293_HiddenVein.Chichirin)
					return "chichirin_q0293_01.htm";
				return "noquest";
			}
		}
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState qs)
	{
		if(qs.getState() != 2)
			return null;
		if(Rnd.chance(_293_HiddenVein.Torn_Map_Fragment_Chance))
		{
			qs.giveItems(_293_HiddenVein.Torn_Map_Fragment, 1L);
			qs.playSound(Quest.SOUND_ITEMGET);
		}
		else if(Rnd.chance(_293_HiddenVein.Chrysolite_Ore_Chance))
		{
			qs.giveItems(_293_HiddenVein.Chrysolite_Ore, 1L);
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
		_293_HiddenVein.Filaur = 30535;
		_293_HiddenVein.Chichirin = 30539;
		_293_HiddenVein.Utuku_Orc = 20446;
		_293_HiddenVein.Utuku_Orc_Archer = 20447;
		_293_HiddenVein.Utuku_Orc_Grunt = 20448;
		_293_HiddenVein.Chrysolite_Ore = 1488;
		_293_HiddenVein.Torn_Map_Fragment = 1489;
		_293_HiddenVein.Hidden_Ore_Map = 1490;
		_293_HiddenVein.Torn_Map_Fragment_Chance = 5;
		_293_HiddenVein.Chrysolite_Ore_Chance = 45;
	}
}
