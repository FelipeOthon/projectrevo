package quests;

import java.util.HashMap;
import java.util.Map;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.AddonsConfig;

public class _606_WarwithVarkaSilenos extends Quest implements ScriptFile
{
	private static final int KADUN_ZU_KETRA = 31370;
	private static final int VARKAS_MANE = 7233;
	private static final int HORN_OF_BUFFALO = 7186;
	private static final int[] VARKA_NPC_LIST;
	private static final Map<Integer, Double> ChanceMane;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _606_WarwithVarkaSilenos()
	{
		super(true);
		_606_WarwithVarkaSilenos.ChanceMane.put(21350, 50.0);
		_606_WarwithVarkaSilenos.ChanceMane.put(21353, 51.0);
		_606_WarwithVarkaSilenos.ChanceMane.put(21354, 52.2);
		_606_WarwithVarkaSilenos.ChanceMane.put(21355, 51.9);
		_606_WarwithVarkaSilenos.ChanceMane.put(21357, 52.9);
		_606_WarwithVarkaSilenos.ChanceMane.put(21358, 52.9);
		_606_WarwithVarkaSilenos.ChanceMane.put(21360, 53.9);
		_606_WarwithVarkaSilenos.ChanceMane.put(21362, 56.8);
		_606_WarwithVarkaSilenos.ChanceMane.put(21364, 55.8);
		_606_WarwithVarkaSilenos.ChanceMane.put(21365, 56.8);
		_606_WarwithVarkaSilenos.ChanceMane.put(21366, 66.4);
		_606_WarwithVarkaSilenos.ChanceMane.put(21368, 56.8);
		_606_WarwithVarkaSilenos.ChanceMane.put(21369, 54.8);
		_606_WarwithVarkaSilenos.ChanceMane.put(21371, 71.3);
		_606_WarwithVarkaSilenos.ChanceMane.put(21373, 77.3);
		_606_WarwithVarkaSilenos.ChanceMane.put(21351, 80.0);
		_606_WarwithVarkaSilenos.ChanceMane.put(21361, 80.0);
		_606_WarwithVarkaSilenos.ChanceMane.put(21370, 80.0);
		_606_WarwithVarkaSilenos.ChanceMane.put(21372, 80.0);
		_606_WarwithVarkaSilenos.ChanceMane.put(21374, 80.0);
		this.addStartNpc(31370);
		_606_WarwithVarkaSilenos.VARKA_NPC_LIST[0] = 21350;
		_606_WarwithVarkaSilenos.VARKA_NPC_LIST[1] = 21351;
		_606_WarwithVarkaSilenos.VARKA_NPC_LIST[2] = 21353;
		_606_WarwithVarkaSilenos.VARKA_NPC_LIST[3] = 21354;
		_606_WarwithVarkaSilenos.VARKA_NPC_LIST[4] = 21355;
		_606_WarwithVarkaSilenos.VARKA_NPC_LIST[5] = 21357;
		_606_WarwithVarkaSilenos.VARKA_NPC_LIST[6] = 21358;
		_606_WarwithVarkaSilenos.VARKA_NPC_LIST[7] = 21360;
		_606_WarwithVarkaSilenos.VARKA_NPC_LIST[8] = 21361;
		_606_WarwithVarkaSilenos.VARKA_NPC_LIST[9] = 21362;
		_606_WarwithVarkaSilenos.VARKA_NPC_LIST[10] = 21364;
		_606_WarwithVarkaSilenos.VARKA_NPC_LIST[11] = 21365;
		_606_WarwithVarkaSilenos.VARKA_NPC_LIST[12] = 21366;
		_606_WarwithVarkaSilenos.VARKA_NPC_LIST[13] = 21368;
		_606_WarwithVarkaSilenos.VARKA_NPC_LIST[14] = 21369;
		_606_WarwithVarkaSilenos.VARKA_NPC_LIST[15] = 21370;
		_606_WarwithVarkaSilenos.VARKA_NPC_LIST[16] = 21371;
		_606_WarwithVarkaSilenos.VARKA_NPC_LIST[17] = 21372;
		_606_WarwithVarkaSilenos.VARKA_NPC_LIST[18] = 21373;
		_606_WarwithVarkaSilenos.VARKA_NPC_LIST[19] = 21374;
		this.addKillId(_606_WarwithVarkaSilenos.VARKA_NPC_LIST);
		addQuestItem(new int[] { 7233 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("quest_accept"))
		{
			htmltext = "elder_kadun_zu_ketra_q0606_0104.htm";
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equals("606_3"))
		{
			final long ec = st.getQuestItemsCount(7233) / 5L;
			if(ec > 0L)
			{
				htmltext = "elder_kadun_zu_ketra_q0606_0202.htm";
				st.takeItems(7233, ec * 5L);
				st.giveItems(7186, ec);
			}
			else
				htmltext = "elder_kadun_zu_ketra_q0606_0203.htm";
		}
		else if(event.equals("606_4"))
		{
			htmltext = "elder_kadun_zu_ketra_q0606_0204.htm";
			st.takeItems(7233, -1L);
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
				htmltext = "elder_kadun_zu_ketra_q0606_0101.htm";
			else
			{
				htmltext = "elder_kadun_zu_ketra_q0606_0103.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(cond > 0 && st.getQuestItemsCount(7233) == 0L)
			htmltext = "elder_kadun_zu_ketra_q0606_0106.htm";
		else if(cond > 0 && st.getQuestItemsCount(7233) > 0L)
			htmltext = "elder_kadun_zu_ketra_q0606_0105.htm";
		return htmltext;
	}

	public boolean isVarkaNpc(final int npc)
	{
		for(final int i : _606_WarwithVarkaSilenos.VARKA_NPC_LIST)
			if(npc == i)
				return true;
		return false;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int cond = st.getInt("cond");
		final int npcId = npc.getNpcId();
		if(cond > 0 && isVarkaNpc(npcId))
		{
			st.rollAndGive(7233, (int) AddonsConfig.getQuestRewardRates(this), _606_WarwithVarkaSilenos.ChanceMane.get(npcId));
			if(cond == 1 && st.getQuestItemsCount(7233) >= 100L)
			{
				st.set("cond", "2");
				st.playSound(Quest.SOUND_MIDDLE);
			}
		}
		return null;
	}

	static
	{
		VARKA_NPC_LIST = new int[20];
		ChanceMane = new HashMap<Integer, Double>();
	}
}
