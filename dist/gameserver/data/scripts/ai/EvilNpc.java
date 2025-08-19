package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.tables.SkillTable;

public class EvilNpc extends DefaultAI
{
	private long _lastAction;
	private static final String[] _txt;

	public EvilNpc(final NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtAttacked(final Creature attacker, final Skill skill, final int damage)
	{
		final NpcInstance actor = getActor();
		if(actor == null || attacker == null || attacker.getPlayer() == null)
			return;
		actor.startAttackStanceTask();
		if(System.currentTimeMillis() - _lastAction > 3000L)
		{
			final int chance = Rnd.get(0, 100);
			if(chance < 2)
			{
				attacker.getPlayer().setKarma(attacker.getPlayer().getKarma() + 5);
				attacker.sendChanges();
			}
			else if(chance < 4)
				actor.doCast(SkillTable.getInstance().getInfo(4578, 1), attacker, true);
			else
				actor.doCast(SkillTable.getInstance().getInfo(4185, 7), attacker, true);
			Functions.npcSay(actor, attacker.getName() + ", " + EvilNpc._txt[Rnd.get(EvilNpc._txt.length)]);
			_lastAction = System.currentTimeMillis();
		}
	}

	static
	{
		_txt = new String[] {
				"\u043e\u0442\u0441\u0442\u0430\u043d\u044c!",
				"\u0443\u0439\u043c\u0438\u0441\u044c!",
				"\u044f \u0442\u0435\u0431\u0435 \u043e\u0442\u043e\u043c\u0449\u0443, \u043f\u043e\u0442\u043e\u043c \u0431\u0443\u0434\u0435\u0448\u044c \u043f\u0440\u043e\u0449\u0435\u043d\u0438\u044f \u043f\u0440\u043e\u0441\u0438\u0442\u044c!",
				"\u0443 \u0442\u0435\u0431\u044f \u0431\u0443\u0434\u0443\u0442 \u043d\u0435\u043f\u0440\u0438\u044f\u0442\u043d\u043e\u0441\u0442\u0438!",
				"\u044f \u043d\u0430 \u0442\u0435\u0431\u044f \u043f\u043e\u0436\u0430\u043b\u0443\u044e\u0441\u044c, \u0442\u0435\u0431\u044f \u0430\u0440\u0435\u0441\u0442\u0443\u044e\u0442!" };
	}
}
