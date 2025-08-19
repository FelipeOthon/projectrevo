package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _646_SignsOfRevolt extends Quest implements ScriptFile
{
	private static int TORRANT;
	private static int Ragna_Orc;
	private static int Ragna_Orc_Sorcerer;
	private static int Guardian_of_the_Ghost_Town;
	private static int Varangkas_Succubus;
	private static int Steel;
	private static int Coarse_Bone_Powder;
	private static int Leather;
	private static int CURSED_DOLL;
	private static int CURSED_DOLL_Chance;

	public _646_SignsOfRevolt()
	{
		super(false);
		this.addStartNpc(_646_SignsOfRevolt.TORRANT);
		for(int Ragna_Orc_id = _646_SignsOfRevolt.Ragna_Orc; Ragna_Orc_id <= _646_SignsOfRevolt.Ragna_Orc_Sorcerer; ++Ragna_Orc_id)
			this.addKillId(new int[] { Ragna_Orc_id });
		this.addKillId(new int[] { _646_SignsOfRevolt.Guardian_of_the_Ghost_Town });
		this.addKillId(new int[] { _646_SignsOfRevolt.Varangkas_Succubus });
		addQuestItem(new int[] { _646_SignsOfRevolt.CURSED_DOLL });
	}

	private static String doReward(final QuestState st, final int reward_id, final int _count)
	{
		if(st.getQuestItemsCount(_646_SignsOfRevolt.CURSED_DOLL) < 180L)
			return null;
		st.takeItems(_646_SignsOfRevolt.CURSED_DOLL, -1L);
		st.giveItems(reward_id, _count, true);
		st.playSound(Quest.SOUND_FINISH);
		st.exitCurrentQuest(true);
		return "torant_q0646_0202.htm";
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		final int _state = st.getState();
		if(event.equalsIgnoreCase("torant_q0646_0103.htm") && _state == 1)
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else
		{
			if(event.equalsIgnoreCase("reward_adena") && _state == 2)
				return doReward(st, 57, 21600);
			if(event.equalsIgnoreCase("reward_cbp") && _state == 2)
				return doReward(st, _646_SignsOfRevolt.Coarse_Bone_Powder, 12);
			if(event.equalsIgnoreCase("reward_steel") && _state == 2)
				return doReward(st, _646_SignsOfRevolt.Steel, 9);
			if(event.equalsIgnoreCase("reward_leather") && _state == 2)
				return doReward(st, _646_SignsOfRevolt.Leather, 20);
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		if(npc.getNpcId() != _646_SignsOfRevolt.TORRANT)
			return htmltext;
		final int _state = st.getState();
		if(_state == 1)
		{
			if(st.getPlayer().getLevel() < 40)
			{
				htmltext = "torant_q0646_0102.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "torant_q0646_0101.htm";
				st.set("cond", "0");
			}
		}
		else if(_state == 2)
			htmltext = st.getQuestItemsCount(_646_SignsOfRevolt.CURSED_DOLL) >= 180L ? "torant_q0646_0105.htm" : "torant_q0646_0106.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState qs)
	{
		final Player player = qs.getRandomPartyMember(2, Config.ALT_PARTY_DISTRIBUTION_RANGE);
		if(player == null)
			return null;
		final QuestState st = player.getQuestState(qs.getQuest().getId());
		final long CURSED_DOLL_COUNT = st.getQuestItemsCount(_646_SignsOfRevolt.CURSED_DOLL);
		if(CURSED_DOLL_COUNT < 180L && Rnd.chance(_646_SignsOfRevolt.CURSED_DOLL_Chance))
		{
			st.giveItems(_646_SignsOfRevolt.CURSED_DOLL, 1L);
			if(CURSED_DOLL_COUNT == 179L)
			{
				st.playSound(Quest.SOUND_MIDDLE);
				st.set("cond", "2");
			}
			else
				st.playSound(Quest.SOUND_ITEMGET);
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
		_646_SignsOfRevolt.TORRANT = 32016;
		_646_SignsOfRevolt.Ragna_Orc = 22029;
		_646_SignsOfRevolt.Ragna_Orc_Sorcerer = 22044;
		_646_SignsOfRevolt.Guardian_of_the_Ghost_Town = 22047;
		_646_SignsOfRevolt.Varangkas_Succubus = 22049;
		_646_SignsOfRevolt.Steel = 1880;
		_646_SignsOfRevolt.Coarse_Bone_Powder = 1881;
		_646_SignsOfRevolt.Leather = 1882;
		_646_SignsOfRevolt.CURSED_DOLL = 8087;
		_646_SignsOfRevolt.CURSED_DOLL_Chance = 75;
	}
}
