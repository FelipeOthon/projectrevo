package ai;

import l2s.gameserver.Config;
import l2s.gameserver.ai.Fighter;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Party;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.SocialAction;

public class Barakiel extends Fighter
{
	public Barakiel(final NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtDead(final Creature killer)
	{
		if(Config.NOBLE_KILL_RB && killer != null)
		{
			final Player player = killer.getPlayer();
			if(player != null)
			{
				if(!player.isNoble() && player.getClassId().getLevel() > 2)
				{
					player.setNoble();
					player.getInventory().addItem(7694, 1L);
					player.broadcastPacket(new L2GameServerPacket[] { new SocialAction(player.getObjectId(), 16) });
				}
				final Party party = player.getParty();
				if(party != null)
				{
					final NpcInstance actor = getActor();
					for(final Player pm : party.getPartyMembers())
						if(player.getObjectId() != pm.getObjectId() && !pm.isNoble() && pm.getClassId().getLevel() > 2 && (pm.isInRange(actor, Config.PARTY_QUEST_ITEMS_RANGE) || pm.isInRange(player, Config.PARTY_QUEST_ITEMS_RANGE)))
						{
							pm.setNoble();
							pm.getInventory().addItem(7694, 1L);
							pm.broadcastPacket(new L2GameServerPacket[] { new SocialAction(pm.getObjectId(), 16) });
						}
				}
			}
		}
		super.onEvtDead(killer);
	}
}
