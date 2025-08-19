package quests;

import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _160_NerupasFavor extends Quest implements ScriptFile
{
	private static int SILVERY_SPIDERSILK;
	private static int UNOS_RECEIPT;
	private static int CELS_TICKET;
	private static int NIGHTSHADE_LEAF;
	private static int LESSER_HEALING_POTION;
	private static int NERUPA;
	private static int UNOREN;
	private static int CREAMEES;
	private static int JULIA;
	private static int COND1;
	private static int COND2;
	private static int COND3;
	private static int COND4;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _160_NerupasFavor()
	{
		super(false);
		this.addStartNpc(_160_NerupasFavor.NERUPA);
		this.addTalkId(new int[] { _160_NerupasFavor.UNOREN, _160_NerupasFavor.CREAMEES, _160_NerupasFavor.JULIA });
		addQuestItem(new int[] {
				_160_NerupasFavor.SILVERY_SPIDERSILK,
				_160_NerupasFavor.UNOS_RECEIPT,
				_160_NerupasFavor.CELS_TICKET,
				_160_NerupasFavor.NIGHTSHADE_LEAF });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("quest_accept"))
		{
			htmltext = "nerupa_q0160_04.htm";
			st.setCond(_160_NerupasFavor.COND1);
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
			st.giveItems(_160_NerupasFavor.SILVERY_SPIDERSILK, 1L);
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int cond = st.getCond();
		if(npcId == _160_NerupasFavor.NERUPA)
		{
			if(st.getState() == 1)
			{
				if(st.getPlayer().getRace() != Race.elf)
					htmltext = "nerupa_q0160_00.htm";
				else if(st.getPlayer().getLevel() < 3)
				{
					htmltext = "nerupa_q0160_02.htm";
					st.exitCurrentQuest(true);
				}
				else
					htmltext = "nerupa_q0160_03.htm";
			}
			else if(cond == _160_NerupasFavor.COND1)
				htmltext = "nerupa_q0160_04.htm";
			else if(cond == _160_NerupasFavor.COND4 && st.getQuestItemsCount(_160_NerupasFavor.NIGHTSHADE_LEAF) > 0L)
			{
				st.takeItems(_160_NerupasFavor.NIGHTSHADE_LEAF, -1L);
				st.giveItems(_160_NerupasFavor.LESSER_HEALING_POTION, 5L);
				st.addExpAndSp(1000L, 0L);
				st.playSound(Quest.SOUND_FINISH);
				htmltext = "nerupa_q0160_06.htm";
				st.exitCurrentQuest(false);
			}
			else
				htmltext = "nerupa_q0160_05.htm";
		}
		else if(npcId == _160_NerupasFavor.UNOREN)
		{
			if(cond == _160_NerupasFavor.COND1)
			{
				st.takeItems(_160_NerupasFavor.SILVERY_SPIDERSILK, -1L);
				st.giveItems(_160_NerupasFavor.UNOS_RECEIPT, 1L);
				st.setCond(_160_NerupasFavor.COND2);
				htmltext = "uno_q0160_01.htm";
			}
			else if(cond == _160_NerupasFavor.COND2 || cond == _160_NerupasFavor.COND3)
				htmltext = "uno_q0160_02.htm";
			else if(cond == _160_NerupasFavor.COND4)
				htmltext = "uno_q0160_03.htm";
		}
		else if(npcId == _160_NerupasFavor.CREAMEES)
		{
			if(cond == _160_NerupasFavor.COND2)
			{
				st.takeItems(_160_NerupasFavor.UNOS_RECEIPT, -1L);
				st.giveItems(_160_NerupasFavor.CELS_TICKET, 1L);
				st.setCond(_160_NerupasFavor.COND3);
				htmltext = "cel_q0160_01.htm";
			}
			else if(cond == _160_NerupasFavor.COND3)
				htmltext = "cel_q0160_02.htm";
			else if(cond == _160_NerupasFavor.COND4)
				htmltext = "cel_q0160_03.htm";
		}
		else if(npcId == _160_NerupasFavor.JULIA)
			if(cond == _160_NerupasFavor.COND3)
			{
				st.takeItems(_160_NerupasFavor.CELS_TICKET, -1L);
				st.giveItems(_160_NerupasFavor.NIGHTSHADE_LEAF, 1L);
				htmltext = "jud_q0160_01.htm";
				st.setCond(_160_NerupasFavor.COND4);
			}
			else if(cond == _160_NerupasFavor.COND4)
				htmltext = "jud_q0160_02.htm";
		return htmltext;
	}

	static
	{
		_160_NerupasFavor.SILVERY_SPIDERSILK = 1026;
		_160_NerupasFavor.UNOS_RECEIPT = 1027;
		_160_NerupasFavor.CELS_TICKET = 1028;
		_160_NerupasFavor.NIGHTSHADE_LEAF = 1029;
		_160_NerupasFavor.LESSER_HEALING_POTION = 1060;
		_160_NerupasFavor.NERUPA = 30370;
		_160_NerupasFavor.UNOREN = 30147;
		_160_NerupasFavor.CREAMEES = 30149;
		_160_NerupasFavor.JULIA = 30152;
		_160_NerupasFavor.COND1 = 1;
		_160_NerupasFavor.COND2 = 2;
		_160_NerupasFavor.COND3 = 3;
		_160_NerupasFavor.COND4 = 4;
	}
}
