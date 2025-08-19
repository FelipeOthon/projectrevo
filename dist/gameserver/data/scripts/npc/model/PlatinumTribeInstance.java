package npc.model;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.MonsterInstance;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.templates.npc.NpcTemplate;

public final class PlatinumTribeInstance extends MonsterInstance
{
	public PlatinumTribeInstance(final int objectId, final NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void reduceCurrentHp(final double damage, final Creature attacker, final Skill skill, final int poleHitCount, final boolean crit, final boolean awake, final boolean standUp, final boolean directHp, final boolean canReflect, final boolean transferDamage, final boolean isDot, final boolean sendMessage)
	{
		if(attacker != null && attacker.isPlayer() && attacker.getActiveWeaponInstance() == null && skill == null && !isDot)
		{
			final QuestState st = ((Player) attacker).getQuestState(348);
			if(st != null && st.getInt("reward1") == 1 && st.getQuestItemsCount(4294) > 0L && Rnd.chance(Config.FIRST_BLOODED_FABRIC))
			{
				st.takeItems(4294, 1L);
				st.giveItems(4295, 1L);
				st.playSound("ItemSound.quest_itemget");
				if(st.getInt("cond") != 24)
					st.set("reward1", "2");
				else
				{
					st.playSound("ItemSound.quest_finish");
					st.exitCurrentQuest(true);
				}
			}
		}
		super.reduceCurrentHp(damage, attacker, skill, poleHitCount, crit, awake, standUp, directHp, canReflect, transferDamage, isDot, sendMessage);
	}
}
