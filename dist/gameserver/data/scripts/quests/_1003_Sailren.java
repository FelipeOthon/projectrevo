package quests;

import bosses.SailrenManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _1003_Sailren extends Quest implements ScriptFile
{
	private static final int STATUE = 32109;
	private static final int GAZKH = 8784;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _1003_Sailren()
	{
		super("Sailren", false);
		this.addStartNpc(32109);
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		String htmltext = null;
		if(st.getPlayer().getLevel() < 75)
			htmltext = "32109-03.htm";
		else if(st.getQuestItemsCount(8784) > 0L)
		{
			final int check = SailrenManager.canIntoSailrenLair(st.getPlayer());
			if(check == 2)
				htmltext = "32109-05.htm";
			else if(check == 3)
				htmltext = "32109-04.htm";
			else if(check == 4)
				htmltext = "32109-01.htm";
			else
			{
				st.takeItems(8784, 1L);
				SailrenManager.setSailrenSpawnTask();
				SailrenManager.entryToSailrenLair(st.getPlayer());
			}
		}
		else
			htmltext = "32109-02.htm";
		st.exitCurrentQuest(true);
		return htmltext;
	}

	@Override
	public boolean isVisible(Player player)
	{
		return false;
	}
}
