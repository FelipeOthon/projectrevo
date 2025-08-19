package quests;

import bosses.BaiumManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;

public class _1001_WakeUpBaium extends Quest implements ScriptFile
{
	private static final int BaiumNpc = 29025;

	public _1001_WakeUpBaium()
	{
		super("Wake Up Baium", true);
		this.addStartNpc(29025);
	}

	@Override
	public synchronized String onTalk(final NpcInstance npc, final QuestState st)
	{
		if(npc.getNpcId() != 29025)
			return null;
		if(!NpcInstance.canBypassCheck(st.getPlayer(), npc))
			return "It's too far from the npc to work!";
		if(st.getPlayer().getZ() < 10030)
			return "Invalid height!";
		if(!st.getPlayer().getVarBoolean("BaiumEnter"))
		{
			st.exitCurrentQuest(true);
			return "Conditions are not right to wake up Baium!";
		}
		if(npc.isBusy())
			return "Baium is busy!";
		npc.setBusy(true);
		npc.setBusyMessage("Attending another player's request.");
		st.getPlayer().unsetVar("BaiumEnter");
		Functions.npcSay(npc, "You call my name! Now you gonna die!");
		BaiumManager.spawnBaium(st.getPlayer().toString(), st.getPlayer().getObjectId());
		return "You call my name! Now you gonna die!";
	}

	@Override
	public boolean isVisible(Player player)
	{
		return false;
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
}
