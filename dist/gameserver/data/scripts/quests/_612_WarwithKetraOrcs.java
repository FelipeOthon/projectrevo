package quests;

import java.util.HashMap;
import java.util.Map;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.AddonsConfig;

public class _612_WarwithKetraOrcs extends Quest implements ScriptFile
{
	private static final int DURAI = 31377;
	private static final int MOLAR_OF_KETRA_ORC = 7234;
	private static final int NEPENTHES_SEED = 7187;
	private static final int[] KETRA_NPC_LIST;
	private static final Map<Integer, Double> ChanceMolar;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _612_WarwithKetraOrcs()
	{
		super(true);
		_612_WarwithKetraOrcs.ChanceMolar.put(21324, 50.0);
		_612_WarwithKetraOrcs.ChanceMolar.put(21327, 51.0);
		_612_WarwithKetraOrcs.ChanceMolar.put(21328, 52.2);
		_612_WarwithKetraOrcs.ChanceMolar.put(21329, 51.9);
		_612_WarwithKetraOrcs.ChanceMolar.put(21331, 52.9);
		_612_WarwithKetraOrcs.ChanceMolar.put(21332, 52.9);
		_612_WarwithKetraOrcs.ChanceMolar.put(21334, 53.9);
		_612_WarwithKetraOrcs.ChanceMolar.put(21336, 56.8);
		_612_WarwithKetraOrcs.ChanceMolar.put(21338, 55.8);
		_612_WarwithKetraOrcs.ChanceMolar.put(21339, 56.8);
		_612_WarwithKetraOrcs.ChanceMolar.put(21340, 66.4);
		_612_WarwithKetraOrcs.ChanceMolar.put(21342, 56.8);
		_612_WarwithKetraOrcs.ChanceMolar.put(21343, 54.8);
		_612_WarwithKetraOrcs.ChanceMolar.put(21345, 71.3);
		_612_WarwithKetraOrcs.ChanceMolar.put(21347, 77.3);
		_612_WarwithKetraOrcs.ChanceMolar.put(21325, 80.0);
		_612_WarwithKetraOrcs.ChanceMolar.put(21335, 80.0);
		_612_WarwithKetraOrcs.ChanceMolar.put(21344, 80.0);
		_612_WarwithKetraOrcs.ChanceMolar.put(21346, 80.0);
		this.addStartNpc(31377);
		_612_WarwithKetraOrcs.KETRA_NPC_LIST[0] = 21324;
		_612_WarwithKetraOrcs.KETRA_NPC_LIST[1] = 21325;
		_612_WarwithKetraOrcs.KETRA_NPC_LIST[2] = 21327;
		_612_WarwithKetraOrcs.KETRA_NPC_LIST[3] = 21328;
		_612_WarwithKetraOrcs.KETRA_NPC_LIST[4] = 21329;
		_612_WarwithKetraOrcs.KETRA_NPC_LIST[5] = 21331;
		_612_WarwithKetraOrcs.KETRA_NPC_LIST[6] = 21332;
		_612_WarwithKetraOrcs.KETRA_NPC_LIST[7] = 21334;
		_612_WarwithKetraOrcs.KETRA_NPC_LIST[8] = 21335;
		_612_WarwithKetraOrcs.KETRA_NPC_LIST[9] = 21336;
		_612_WarwithKetraOrcs.KETRA_NPC_LIST[10] = 21338;
		_612_WarwithKetraOrcs.KETRA_NPC_LIST[11] = 21339;
		_612_WarwithKetraOrcs.KETRA_NPC_LIST[12] = 21340;
		_612_WarwithKetraOrcs.KETRA_NPC_LIST[13] = 21342;
		_612_WarwithKetraOrcs.KETRA_NPC_LIST[14] = 21343;
		_612_WarwithKetraOrcs.KETRA_NPC_LIST[15] = 21344;
		_612_WarwithKetraOrcs.KETRA_NPC_LIST[16] = 21345;
		_612_WarwithKetraOrcs.KETRA_NPC_LIST[17] = 21346;
		_612_WarwithKetraOrcs.KETRA_NPC_LIST[18] = 21347;
		this.addKillId(_612_WarwithKetraOrcs.KETRA_NPC_LIST);
		addQuestItem(new int[] { 7234 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("quest_accept"))
		{
			htmltext = "elder_ashas_barka_durai_q0612_0104.htm";
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("elder_ashas_barka_durai_q0612_0202.htm"))
		{
			final long ec = st.getQuestItemsCount(7234) / 5L;
			if(ec > 0L)
			{
				st.takeItems(7234, ec * 5L);
				st.giveItems(7187, ec);
			}
			else
				htmltext = "elder_ashas_barka_durai_q0612_0203.htm";
		}
		else if(event.equalsIgnoreCase("elder_ashas_barka_durai_q0612_0204.htm"))
		{
			st.takeItems(7234, -1L);
			st.playSound(Quest.SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(cond == 0)
		{
			if(st.getPlayer().getLevel() >= 74)
				htmltext = "elder_ashas_barka_durai_q0612_0101.htm";
			else
			{
				htmltext = "elder_ashas_barka_durai_q0612_0103.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(cond > 0 && st.getQuestItemsCount(7234) == 0L)
			htmltext = "elder_ashas_barka_durai_q0612_0106.htm";
		else if(cond > 0 && st.getQuestItemsCount(7234) > 0L)
			htmltext = "elder_ashas_barka_durai_q0612_0105.htm";
		return htmltext;
	}

	public boolean isKetraNpc(final int npc)
	{
		for(final int i : _612_WarwithKetraOrcs.KETRA_NPC_LIST)
			if(npc == i)
				return true;
		return false;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int cond = st.getInt("cond");
		final int npcId = npc.getNpcId();
		if(cond > 0 && isKetraNpc(npcId))
		{
			st.rollAndGive(7234, (int) AddonsConfig.getQuestRewardRates(this), _612_WarwithKetraOrcs.ChanceMolar.get(npcId));
			if(cond == 1 && st.getQuestItemsCount(7234) >= 100L)
			{
				st.set("cond", "2");
				st.playSound(Quest.SOUND_MIDDLE);
			}
		}
		return null;
	}

	static
	{
		KETRA_NPC_LIST = new int[19];
		ChanceMolar = new HashMap<Integer, Double>();
	}
}
