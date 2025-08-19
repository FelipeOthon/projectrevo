package quests;

import java.util.HashMap;
import java.util.Map;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _371_ShriekOfGhosts extends Quest implements ScriptFile
{
	private static int REVA;
	private static int PATRIN;
	private static int Hallates_Warrior;
	private static int Hallates_Knight;
	private static int Hallates_Commander;
	private static int Ancient_Porcelain__Excellent;
	private static int Ancient_Porcelain__High_Quality;
	private static int Ancient_Porcelain__Low_Quality;
	private static int Ancient_Porcelain__Lowest_Quality;
	private static int Ancient_Ash_Urn;
	private static int Ancient_Porcelain;
	private static int Urn_Chance;
	private static int Ancient_Porcelain__Excellent_Chance;
	private static int Ancient_Porcelain__High_Quality_Chance;
	private static int Ancient_Porcelain__Low_Quality_Chance;
	private static int Ancient_Porcelain__Lowest_Quality_Chance;
	private Map<Integer, Integer> common_chances;

	public _371_ShriekOfGhosts()
	{
		super(true);
		common_chances = new HashMap<Integer, Integer>();
		this.addStartNpc(_371_ShriekOfGhosts.REVA);
		this.addTalkId(new int[] { _371_ShriekOfGhosts.PATRIN });
		this.addKillId(new int[] { _371_ShriekOfGhosts.Hallates_Warrior });
		this.addKillId(new int[] { _371_ShriekOfGhosts.Hallates_Knight });
		this.addKillId(new int[] { _371_ShriekOfGhosts.Hallates_Commander });
		addQuestItem(new int[] { _371_ShriekOfGhosts.Ancient_Ash_Urn });
		common_chances.put(_371_ShriekOfGhosts.Hallates_Warrior, 71);
		common_chances.put(_371_ShriekOfGhosts.Hallates_Knight, 74);
		common_chances.put(_371_ShriekOfGhosts.Hallates_Commander, 82);
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		final int _state = st.getState();
		if(event.equalsIgnoreCase("30867-03.htm") && _state == 1)
		{
			st.setState(2);
			st.set("cond", "1");
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30867-10.htm") && _state == 2)
		{
			final long Ancient_Ash_Urn_count = st.getQuestItemsCount(_371_ShriekOfGhosts.Ancient_Ash_Urn);
			if(Ancient_Ash_Urn_count > 0L)
			{
				st.takeItems(_371_ShriekOfGhosts.Ancient_Ash_Urn, -1L);
				st.giveItems(57, Ancient_Ash_Urn_count * 1000L);
			}
			st.exitCurrentQuest(true);
		}
		else if(event.equalsIgnoreCase("30867-TRADE") && _state == 2)
		{
			final long Ancient_Ash_Urn_count = st.getQuestItemsCount(_371_ShriekOfGhosts.Ancient_Ash_Urn);
			if(Ancient_Ash_Urn_count > 0L)
			{
				htmltext = Ancient_Ash_Urn_count > 100L ? "30867-08.htm" : "30867-07.htm";
				final int bonus = Ancient_Ash_Urn_count > 100L ? 17000 : 3000;
				st.takeItems(_371_ShriekOfGhosts.Ancient_Ash_Urn, -1L);
				st.giveItems(57, bonus + Ancient_Ash_Urn_count * 1000L);
			}
			else
				htmltext = "30867-06.htm";
		}
		else if(event.equalsIgnoreCase("30929-TRADE") && _state == 2)
		{
			final long Ancient_Porcelain_count = st.getQuestItemsCount(_371_ShriekOfGhosts.Ancient_Porcelain);
			if(Ancient_Porcelain_count > 0L)
			{
				st.takeItems(_371_ShriekOfGhosts.Ancient_Porcelain, 1L);
				final int chance = Rnd.get(100);
				if(chance < _371_ShriekOfGhosts.Ancient_Porcelain__Excellent_Chance)
				{
					st.giveItems(_371_ShriekOfGhosts.Ancient_Porcelain__Excellent, 1L);
					htmltext = "30929-03.htm";
				}
				else if(chance < _371_ShriekOfGhosts.Ancient_Porcelain__High_Quality_Chance)
				{
					st.giveItems(_371_ShriekOfGhosts.Ancient_Porcelain__High_Quality, 1L);
					htmltext = "30929-04.htm";
				}
				else if(chance < _371_ShriekOfGhosts.Ancient_Porcelain__Low_Quality_Chance)
				{
					st.giveItems(_371_ShriekOfGhosts.Ancient_Porcelain__Low_Quality, 1L);
					htmltext = "30929-05.htm";
				}
				else if(chance < _371_ShriekOfGhosts.Ancient_Porcelain__Lowest_Quality_Chance)
				{
					st.giveItems(_371_ShriekOfGhosts.Ancient_Porcelain__Lowest_Quality, 1L);
					htmltext = "30929-06.htm";
				}
				else
					htmltext = "30929-07.htm";
			}
			else
				htmltext = "30929-02.htm";
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int _state = st.getState();
		final int npcId = npc.getNpcId();
		if(_state == 1)
		{
			if(npcId != _371_ShriekOfGhosts.REVA)
				return htmltext;
			if(st.getPlayer().getLevel() >= 59)
			{
				htmltext = "30867-02.htm";
				st.set("cond", "0");
			}
			else
			{
				htmltext = "30867-01.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(_state == 2 && npcId == _371_ShriekOfGhosts.REVA)
			htmltext = st.getQuestItemsCount(_371_ShriekOfGhosts.Ancient_Porcelain) > 0L ? "30867-05.htm" : "30867-04.htm";
		else if(_state == 2 && npcId == _371_ShriekOfGhosts.PATRIN)
			htmltext = "30929-01.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState qs)
	{
		final Player player = qs.getRandomPartyMember(2, Config.ALT_PARTY_DISTRIBUTION_RANGE);
		if(player == null)
			return null;
		final QuestState st = player.getQuestState(qs.getQuest().getId());
		final Integer _chance = common_chances.get(npc.getNpcId());
		if(_chance == null)
			return null;
		if(Rnd.chance(_chance))
		{
			st.giveItems(Rnd.chance(_371_ShriekOfGhosts.Urn_Chance) ? _371_ShriekOfGhosts.Ancient_Ash_Urn : _371_ShriekOfGhosts.Ancient_Porcelain, 1L);
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
		_371_ShriekOfGhosts.REVA = 30867;
		_371_ShriekOfGhosts.PATRIN = 30929;
		_371_ShriekOfGhosts.Hallates_Warrior = 20818;
		_371_ShriekOfGhosts.Hallates_Knight = 20820;
		_371_ShriekOfGhosts.Hallates_Commander = 20824;
		_371_ShriekOfGhosts.Ancient_Porcelain__Excellent = 6003;
		_371_ShriekOfGhosts.Ancient_Porcelain__High_Quality = 6004;
		_371_ShriekOfGhosts.Ancient_Porcelain__Low_Quality = 6005;
		_371_ShriekOfGhosts.Ancient_Porcelain__Lowest_Quality = 6006;
		_371_ShriekOfGhosts.Ancient_Ash_Urn = 5903;
		_371_ShriekOfGhosts.Ancient_Porcelain = 6002;
		_371_ShriekOfGhosts.Urn_Chance = 43;
		_371_ShriekOfGhosts.Ancient_Porcelain__Excellent_Chance = 1;
		_371_ShriekOfGhosts.Ancient_Porcelain__High_Quality_Chance = 14;
		_371_ShriekOfGhosts.Ancient_Porcelain__Low_Quality_Chance = 46;
		_371_ShriekOfGhosts.Ancient_Porcelain__Lowest_Quality_Chance = 84;
	}
}
