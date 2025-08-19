package npc.model;

import java.util.ArrayList;
import java.util.List;

import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.RaidBossInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.templates.npc.NpcTemplate;

public class ImmortalInstance extends RaidBossInstance
{
	public List<Integer> ids;

	public ImmortalInstance(final int objectId, final NpcTemplate template)
	{
		super(objectId, template);
		ids = new ArrayList<Integer>();
	}

	@Override
	public void reduceCurrentHp(final double damage, final Creature attacker, final Skill skill, final int poleHitCount, final boolean crit, final boolean awake, final boolean standUp, final boolean directHp, final boolean canReflect, final boolean transferDamage, final boolean isDot, final boolean sendMessage)
	{
		final Player player = attacker.getPlayer();
		if(player == null || !ids.contains(player.getObjectId()))
			return;
		super.reduceCurrentHp(damage, attacker, skill, poleHitCount, crit, awake, standUp, directHp, canReflect, transferDamage, isDot, sendMessage);
	}

	@Override
	public void onDeath(final Creature killer)
	{
		super.onDeath(killer);
		if(!ids.isEmpty())
			ids.clear();
		final Player player = killer.getPlayer();
		if(player != null)
		{
			player.getInventory().destroyItemByItemId(9174, 1L, false);
			Functions.npcSay(this, "\u041f\u0440\u043e\u043a\u043b\u044f\u0442\u044c\u0435!!! " + player.getName() + " \u043d\u0430\u043d\u0435\u0441 \u043c\u043d\u0435 \u0441\u043e\u043a\u0440\u0443\u0448\u0438\u0442\u0435\u043b\u044c\u043d\u044b\u0439 \u0443\u0434\u0430\u0440...");
		}
	}

	@Override
	public void onSpawn()
	{
		super.onSpawn();
		Functions.npcSay(this, "\u041a\u0430\u043a \u0436\u0435 \u044f \u0441\u043e\u0441\u043a\u0443\u0447\u0438\u043b\u0441\u044f \u043f\u043e \u0432\u0430\u0448\u0435\u0439 \u043a\u0440\u043e\u0432\u0443\u0448\u043a\u0435!");
	}
}
