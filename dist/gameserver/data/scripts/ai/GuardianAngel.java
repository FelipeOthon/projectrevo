package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;

public class GuardianAngel extends DefaultAI
{
	static final String[] flood;

	public GuardianAngel(final NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if(actor != null)
			Functions.npcSay(actor, GuardianAngel.flood[Rnd.get(2)]);
		return super.thinkActive();
	}

	@Override
	protected void onEvtDead(final Creature killer)
	{
		final NpcInstance actor = getActor();
		if(actor != null)
			Functions.npcSay(actor, GuardianAngel.flood[2]);
		super.onEvtDead(killer);
	}

	static
	{
		flood = new String[] {
				"Waaaah! Step back from the confounded box! I will take it myself!",
				"Grr! Who are you and why have you stopped my?",
				"Grr. I've been hit..." };
	}
}
