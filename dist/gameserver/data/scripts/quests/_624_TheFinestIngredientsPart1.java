package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _624_TheFinestIngredientsPart1 extends Quest implements ScriptFile
{
	private static int JEREMY;
	private static int HOT_SPRINGS_ATROX;
	private static int HOT_SPRINGS_NEPENTHES;
	private static int HOT_SPRINGS_ATROXSPAWN;
	private static int HOT_SPRINGS_BANDERSNATCHLING;
	private static int SECRET_SPICE;
	private static int TRUNK_OF_NEPENTHES;
	private static int FOOT_OF_BANDERSNATCHLING;
	private static int CRYOLITE;
	private static int SAUCE;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _624_TheFinestIngredientsPart1()
	{
		super(true);
		this.addStartNpc(_624_TheFinestIngredientsPart1.JEREMY);
		this.addKillId(new int[] { _624_TheFinestIngredientsPart1.HOT_SPRINGS_ATROX });
		this.addKillId(new int[] { _624_TheFinestIngredientsPart1.HOT_SPRINGS_NEPENTHES });
		this.addKillId(new int[] { _624_TheFinestIngredientsPart1.HOT_SPRINGS_ATROXSPAWN });
		this.addKillId(new int[] { _624_TheFinestIngredientsPart1.HOT_SPRINGS_BANDERSNATCHLING });
		addQuestItem(new int[] { _624_TheFinestIngredientsPart1.TRUNK_OF_NEPENTHES });
		addQuestItem(new int[] { _624_TheFinestIngredientsPart1.FOOT_OF_BANDERSNATCHLING });
		addQuestItem(new int[] { _624_TheFinestIngredientsPart1.SECRET_SPICE });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("jeremy_q0624_0104.htm"))
		{
			if(st.getPlayer().getLevel() >= 73)
			{
				st.setState(2);
				st.set("cond", "1");
				st.playSound(Quest.SOUND_ACCEPT);
			}
			else
			{
				htmltext = "jeremy_q0624_0103.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(event.equalsIgnoreCase("jeremy_q0624_0201.htm"))
			if(st.getQuestItemsCount(_624_TheFinestIngredientsPart1.TRUNK_OF_NEPENTHES) == 50L && st.getQuestItemsCount(_624_TheFinestIngredientsPart1.FOOT_OF_BANDERSNATCHLING) == 50L && st.getQuestItemsCount(_624_TheFinestIngredientsPart1.SECRET_SPICE) == 50L)
			{
				st.takeItems(_624_TheFinestIngredientsPart1.TRUNK_OF_NEPENTHES, -1L);
				st.takeItems(_624_TheFinestIngredientsPart1.FOOT_OF_BANDERSNATCHLING, -1L);
				st.takeItems(_624_TheFinestIngredientsPart1.SECRET_SPICE, -1L);
				st.playSound(Quest.SOUND_FINISH);
				st.giveItems(_624_TheFinestIngredientsPart1.SAUCE, 1L);
				st.giveItems(_624_TheFinestIngredientsPart1.CRYOLITE, 1L);
				htmltext = "jeremy_q0624_0201.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "jeremy_q0624_0202.htm";
				st.set("cond", "1");
			}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(cond == 0)
			htmltext = "jeremy_q0624_0101.htm";
		else if(cond != 3)
			htmltext = "jeremy_q0624_0106.htm";
		else
			htmltext = "jeremy_q0624_0105.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getState() != 2)
			return null;
		final int npcId = npc.getNpcId();
		if(st.getInt("cond") == 1)
		{
			if(npcId == _624_TheFinestIngredientsPart1.HOT_SPRINGS_NEPENTHES && st.getQuestItemsCount(_624_TheFinestIngredientsPart1.TRUNK_OF_NEPENTHES) < 50L)
				st.rollAndGive(_624_TheFinestIngredientsPart1.TRUNK_OF_NEPENTHES, 1, 1, 50, 100.0);
			else if(npcId == _624_TheFinestIngredientsPart1.HOT_SPRINGS_BANDERSNATCHLING && st.getQuestItemsCount(_624_TheFinestIngredientsPart1.FOOT_OF_BANDERSNATCHLING) < 50L)
				st.rollAndGive(_624_TheFinestIngredientsPart1.FOOT_OF_BANDERSNATCHLING, 1, 1, 50, 100.0);
			else if((npcId == _624_TheFinestIngredientsPart1.HOT_SPRINGS_ATROX || npcId == _624_TheFinestIngredientsPart1.HOT_SPRINGS_ATROXSPAWN) && st.getQuestItemsCount(_624_TheFinestIngredientsPart1.SECRET_SPICE) < 50L)
				st.rollAndGive(_624_TheFinestIngredientsPart1.SECRET_SPICE, 1, 1, 50, 100.0);
			onKillCheck(st);
		}
		return null;
	}

	private void onKillCheck(final QuestState st)
	{
		if(st.getQuestItemsCount(_624_TheFinestIngredientsPart1.TRUNK_OF_NEPENTHES) == 50L && st.getQuestItemsCount(_624_TheFinestIngredientsPart1.FOOT_OF_BANDERSNATCHLING) == 50L && st.getQuestItemsCount(_624_TheFinestIngredientsPart1.SECRET_SPICE) == 50L)
		{
			st.playSound(Quest.SOUND_MIDDLE);
			st.set("cond", "3");
		}
		else
			st.playSound(Quest.SOUND_ITEMGET);
	}

	static
	{
		_624_TheFinestIngredientsPart1.JEREMY = 31521;
		_624_TheFinestIngredientsPart1.HOT_SPRINGS_ATROX = 21321;
		_624_TheFinestIngredientsPart1.HOT_SPRINGS_NEPENTHES = 21319;
		_624_TheFinestIngredientsPart1.HOT_SPRINGS_ATROXSPAWN = 21317;
		_624_TheFinestIngredientsPart1.HOT_SPRINGS_BANDERSNATCHLING = 21314;
		_624_TheFinestIngredientsPart1.SECRET_SPICE = 7204;
		_624_TheFinestIngredientsPart1.TRUNK_OF_NEPENTHES = 7202;
		_624_TheFinestIngredientsPart1.FOOT_OF_BANDERSNATCHLING = 7203;
		_624_TheFinestIngredientsPart1.CRYOLITE = 7080;
		_624_TheFinestIngredientsPart1.SAUCE = 7205;
	}
}
