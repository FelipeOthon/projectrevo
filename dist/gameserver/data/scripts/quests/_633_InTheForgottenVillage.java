package quests;

import java.util.HashMap;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _633_InTheForgottenVillage extends Quest implements ScriptFile
{
	private static int MINA;
	private static int RIB_BONE;
	private static int Z_LIVER;
	private static HashMap<Integer, Double> DAMOBS;
	private static HashMap<Integer, Double> UNDEADS;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _633_InTheForgottenVillage()
	{
		super(true);
		_633_InTheForgottenVillage.DAMOBS.put(21557, 32.8);
		_633_InTheForgottenVillage.DAMOBS.put(21558, 32.8);
		_633_InTheForgottenVillage.DAMOBS.put(21559, 33.7);
		_633_InTheForgottenVillage.DAMOBS.put(21560, 33.7);
		_633_InTheForgottenVillage.DAMOBS.put(21563, 34.2);
		_633_InTheForgottenVillage.DAMOBS.put(21564, 34.8);
		_633_InTheForgottenVillage.DAMOBS.put(21565, 35.1);
		_633_InTheForgottenVillage.DAMOBS.put(21566, 35.9);
		_633_InTheForgottenVillage.DAMOBS.put(21567, 35.9);
		_633_InTheForgottenVillage.DAMOBS.put(21572, 36.5);
		_633_InTheForgottenVillage.DAMOBS.put(21574, 38.3);
		_633_InTheForgottenVillage.DAMOBS.put(21575, 38.3);
		_633_InTheForgottenVillage.DAMOBS.put(21580, 38.5);
		_633_InTheForgottenVillage.DAMOBS.put(21581, 39.5);
		_633_InTheForgottenVillage.DAMOBS.put(21583, 39.7);
		_633_InTheForgottenVillage.DAMOBS.put(21584, 40.1);
		_633_InTheForgottenVillage.UNDEADS.put(21553, 34.7);
		_633_InTheForgottenVillage.UNDEADS.put(21554, 34.7);
		_633_InTheForgottenVillage.UNDEADS.put(21561, 45.0);
		_633_InTheForgottenVillage.UNDEADS.put(21578, 50.1);
		_633_InTheForgottenVillage.UNDEADS.put(21596, 35.9);
		_633_InTheForgottenVillage.UNDEADS.put(21597, 37.0);
		_633_InTheForgottenVillage.UNDEADS.put(21598, 44.1);
		_633_InTheForgottenVillage.UNDEADS.put(21599, 39.5);
		_633_InTheForgottenVillage.UNDEADS.put(21600, 40.8);
		_633_InTheForgottenVillage.UNDEADS.put(21601, 41.1);
		this.addStartNpc(_633_InTheForgottenVillage.MINA);
		addQuestItem(new int[] { _633_InTheForgottenVillage.RIB_BONE });
		for(final int i : _633_InTheForgottenVillage.UNDEADS.keySet())
			this.addKillId(new int[] { i });
		for(final int i : _633_InTheForgottenVillage.DAMOBS.keySet())
			this.addKillId(new int[] { i });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("quest_accept"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
			htmltext = "day_mina_q0633_0104.htm";
		}
		if(event.equalsIgnoreCase("633_4"))
		{
			st.takeItems(_633_InTheForgottenVillage.RIB_BONE, -1L);
			st.playSound(Quest.SOUND_FINISH);
			htmltext = "day_mina_q0633_0204.htm";
			st.exitCurrentQuest(true);
		}
		else if(event.equalsIgnoreCase("633_1"))
			htmltext = "day_mina_q0633_0201.htm";
		else if(event.equalsIgnoreCase("633_3") && st.getInt("cond") == 2)
			if(st.getQuestItemsCount(_633_InTheForgottenVillage.RIB_BONE) >= 200L)
			{
				st.takeItems(_633_InTheForgottenVillage.RIB_BONE, -1L);
				st.giveItems(57, 25000L, true);
				st.addExpAndSp(305235L, 0L, true);
				st.playSound(Quest.SOUND_FINISH);
				st.set("cond", "1");
				htmltext = "day_mina_q0633_0202.htm";
			}
			else
				htmltext = "day_mina_q0633_0203.htm";
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		final int id = st.getState();
		if(npcId == _633_InTheForgottenVillage.MINA)
			if(id == 1)
			{
				if(st.getPlayer().getLevel() > 65)
					htmltext = "day_mina_q0633_0101.htm";
				else
				{
					htmltext = "day_mina_q0633_0103.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				htmltext = "day_mina_q0633_0106.htm";
			else if(cond == 2)
				htmltext = "day_mina_q0633_0105.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		if(_633_InTheForgottenVillage.UNDEADS.containsKey(npcId))
			st.rollAndGive(_633_InTheForgottenVillage.Z_LIVER, 1, _633_InTheForgottenVillage.UNDEADS.get(npcId));
		else if(_633_InTheForgottenVillage.DAMOBS.containsKey(npcId))
		{
			final long count = st.getQuestItemsCount(_633_InTheForgottenVillage.RIB_BONE);
			if(count < 200L && Rnd.chance(_633_InTheForgottenVillage.DAMOBS.get(npcId)))
			{
				st.giveItems(_633_InTheForgottenVillage.RIB_BONE, (long) Config.RATE_QUESTS_DROP);
				if(count >= 199L)
				{
					st.set("cond", "2");
					st.playSound(Quest.SOUND_MIDDLE);
				}
				else
					st.playSound(Quest.SOUND_ITEMGET);
			}
		}
		return null;
	}

	static
	{
		_633_InTheForgottenVillage.MINA = 31388;
		_633_InTheForgottenVillage.RIB_BONE = 7544;
		_633_InTheForgottenVillage.Z_LIVER = 7545;
		_633_InTheForgottenVillage.DAMOBS = new HashMap<Integer, Double>();
		_633_InTheForgottenVillage.UNDEADS = new HashMap<Integer, Double>();
	}
}
