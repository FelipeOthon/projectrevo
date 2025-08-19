package ai;

import l2s.gameserver.ai.Fighter;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.tables.SkillTable;

public class Box extends Fighter
{
	public Box(final NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtAttacked(final Creature attacker, final Skill skill, final int damage)
	{
		final NpcInstance actor = getActor();
		if(actor.isDead())
			return;
		if(attacker != null)
		{
			int skillLevel = 1;
			switch(actor.getLevel())
			{
				case 21:
				case 24:
				case 27:
				{
					skillLevel = 2;
					break;
				}
				case 30:
				case 33:
				case 36:
				case 39:
				{
					skillLevel = 3;
					break;
				}
				case 42:
				case 45:
				case 48:
				{
					skillLevel = 4;
					break;
				}
				case 51:
				case 54:
				case 57:
				{
					skillLevel = 5;
					break;
				}
				case 60:
				case 63:
				case 66:
				case 69:
				{
					skillLevel = 6;
					break;
				}
				case 72:
				case 75:
				case 78:
				{
					skillLevel = 7;
					break;
				}
				case 81:
				case 84:
				{
					skillLevel = 8;
					break;
				}
				case 85:
				{
					skillLevel = 9;
					break;
				}
				default:
				{
					skillLevel = 2;
					break;
				}
			}
			actor.doCast(SkillTable.getInstance().getInfo(4614, skillLevel), attacker, false);
			actor.doDie(attacker);
		}
	}
}
