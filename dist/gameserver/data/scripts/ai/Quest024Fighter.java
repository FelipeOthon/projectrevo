package ai;

import l2s.gameserver.ai.Fighter;
import l2s.gameserver.instancemanager.QuestManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.World;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;

public class Quest024Fighter extends Fighter
{
	private String myEvent;

	public Quest024Fighter(final NpcInstance actor)
	{
		super(actor);
		myEvent = "playerInMobRange_" + actor.getNpcId();
	}

	@Override
	protected boolean thinkActive()
	{
		final Quest q = QuestManager.getQuest(24);
		if(q != null)
			for(final Player player : World.getAroundPlayers(getActor(), 300, 200))
				player.processQuestEvent(q.getId(), myEvent, (NpcInstance) null);
		return super.thinkActive();
	}
}
