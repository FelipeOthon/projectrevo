package quests;

import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _641_AttackSailren extends Quest implements ScriptFile
{
	private static int STATUE;
	private static int VEL1;
	private static int VEL2;
	private static int VEL3;
	private static int VEL4;
	private static int VEL5;
	private static int PTE;
	private static int FRAGMENTS;
	private static int GAZKH;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _641_AttackSailren()
	{
		super(true);
		this.addStartNpc(_641_AttackSailren.STATUE);
		this.addKillId(new int[] { _641_AttackSailren.VEL1 });
		this.addKillId(new int[] { _641_AttackSailren.VEL2 });
		this.addKillId(new int[] { _641_AttackSailren.VEL3 });
		this.addKillId(new int[] { _641_AttackSailren.VEL4 });
		this.addKillId(new int[] { _641_AttackSailren.VEL5 });
		this.addKillId(new int[] { _641_AttackSailren.PTE });
		addQuestItem(new int[] { _641_AttackSailren.FRAGMENTS });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		if(event.equalsIgnoreCase("statue_of_shilen_q0641_05.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
			st.playSound(Quest.SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("statue_of_shilen_q0641_08.htm"))
		{
			st.playSound(Quest.SOUND_FINISH);
			st.takeItems(_641_AttackSailren.FRAGMENTS, -1L);
			st.giveItems(_641_AttackSailren.GAZKH, 1L);
			st.exitCurrentQuest(true);
			st.unset("cond");
		}
		return event;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(cond == 0)
		{
			final QuestState qs = st.getPlayer().getQuestState(126);
			if(qs == null || !qs.isCompleted())
				htmltext = "statue_of_shilen_q0641_02.htm";
			else if(st.getPlayer().getLevel() >= 77)
				htmltext = "statue_of_shilen_q0641_01.htm";
			else
				st.exitCurrentQuest(true);
		}
		else if(cond == 1)
			htmltext = "statue_of_shilen_q0641_05.htm";
		else if(cond == 2)
			htmltext = "statue_of_shilen_q0641_07.htm";
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		if(st.getQuestItemsCount(_641_AttackSailren.FRAGMENTS) < 30L)
		{
			st.giveItems(_641_AttackSailren.FRAGMENTS, 1L);
			if(st.getQuestItemsCount(_641_AttackSailren.FRAGMENTS) == 30L)
			{
				st.playSound(Quest.SOUND_MIDDLE);
				st.set("cond", "2");
				st.setState(2);
			}
			else
				st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}

	static
	{
		_641_AttackSailren.STATUE = 32109;
		_641_AttackSailren.VEL1 = 22196;
		_641_AttackSailren.VEL2 = 22197;
		_641_AttackSailren.VEL3 = 22198;
		_641_AttackSailren.VEL4 = 22218;
		_641_AttackSailren.VEL5 = 22223;
		_641_AttackSailren.PTE = 22199;
		_641_AttackSailren.FRAGMENTS = 8782;
		_641_AttackSailren.GAZKH = 8784;
	}
}
