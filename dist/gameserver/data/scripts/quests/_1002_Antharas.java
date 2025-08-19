package quests;

import bosses.AntharasManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.Location;

public class _1002_Antharas extends Quest implements ScriptFile
{
	private static final int HEART = 13001;
	private static final int PORTAL_STONE = 3865;
	private static final Location TELEPORT_POSITION;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _1002_Antharas()
	{
		super("Antharas", false);
		this.addStartNpc(13001);
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		if(npcId != 13001)
			return null;
		if(st.getPlayer().isFlying())
			return "<html><body>Heart of Warding:<br>You may not enter while flying a wyvern.</body></html>";
		final int state = AntharasManager.isEnableEnterToLair();
		if(state == 1)
		{
			if(st.getQuestItemsCount(3865) >= 1L)
			{
				st.takeItems(3865, 1L);
				AntharasManager.setAntharasSpawnTask();
				st.getPlayer().teleToLocation(_1002_Antharas.TELEPORT_POSITION);
				st.exitCurrentQuest(true);
				return null;
			}
			st.exitCurrentQuest(true);
			return "<html><body>Heart of Warding:<br>You do not have the proper stones needed for teleport.<br>It is for the teleport where does 1 stone to you need.</body></html>";
		}
		else
		{
			st.exitCurrentQuest(true);
			if(state == 2)
				return "<html><body>Heart of Warding:<br>Antharas has already awoke!<br>You are not allowed to enter into Lair of Antharas.</body></html>";
			return "<html><body>Heart of Warding:<br>Antharas is still reborning.<br>You cannot invade the nest now.</body></html>";
		}
	}

	@Override
	public boolean isVisible(Player player)
	{
		return false;
	}

	static
	{
		TELEPORT_POSITION = new Location(173826, 115333, -7704);
	}
}
