package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.ai.DefaultAI;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.tables.SkillTable;

public class Scarlet extends DefaultAI
{
	private static final Skill[] DaemonAttack;
	private static final Skill[] DaemonCharge;
	private static final Skill[] DaemonField;
	private static final Skill YokeOfScarlet;
	private static final Skill DaemonDrain;
	private static final int _strongScarletId = 29047;
	private static final int _frintezzasSwordId = 7903;

	public Scarlet(final NpcInstance actor)
	{
		super(actor);
	}

	@Override
	public boolean createNewTask()
	{
		clearTasks();
		final Creature target;
		if((target = prepareTarget()) == null)
			return false;
		final NpcInstance actor = getActor();
		if(actor == null || actor.isDead())
			return false;
		int stage = 0;
		if(actor.getNpcId() == 29047)
			stage = 2;
		else if(actor.getRightHandItem() == 7903)
			stage = 1;
		final double distance = actor.getDistance(target);
		Skill r_skill = null;
		final double hp_per = actor.getCurrentHpPercents();
		switch(stage)
		{
			case 0:
			{
				if(Rnd.get(10000) < 2000)
				{
					r_skill = Scarlet.DaemonCharge[3];
					break;
				}
				if(Rnd.get(10000) < 500)
				{
					r_skill = Scarlet.YokeOfScarlet;
					break;
				}
				r_skill = Scarlet.DaemonAttack[0];
				break;
			}
			case 1:
			{
				if(Rnd.get(10000) < 2000)
				{
					r_skill = Scarlet.DaemonCharge[4];
					break;
				}
				if(hp_per > 50.0)
				{
					if(Rnd.get(10000) < 500)
					{
						r_skill = Scarlet.DaemonCharge[1];
						break;
					}
					if(Rnd.get(10000) < 500)
					{
						r_skill = Scarlet.YokeOfScarlet;
						break;
					}
					if(Rnd.get(10000) < 500)
					{
						r_skill = Scarlet.DaemonField[0];
						break;
					}
					r_skill = Scarlet.DaemonAttack[1];
					break;
				}
				else
				{
					if(Rnd.get(10000) < 1500)
					{
						r_skill = Scarlet.DaemonCharge[1];
						break;
					}
					if(Rnd.get(10000) < 1500)
					{
						r_skill = Scarlet.YokeOfScarlet;
						break;
					}
					if(Rnd.get(10000) < 1000)
					{
						r_skill = Scarlet.DaemonField[0];
						break;
					}
					r_skill = Scarlet.DaemonAttack[1];
					break;
				}
			}
			case 2:
			{
				if(Rnd.get(10000) < 2000)
				{
					r_skill = Scarlet.DaemonCharge[5];
					break;
				}
				if(Rnd.get(10000) < 1000)
				{
					r_skill = Scarlet.DaemonCharge[2];
					break;
				}
				if(hp_per > 75.0)
				{
					if(Rnd.get(10000) < 1000)
					{
						r_skill = Scarlet.YokeOfScarlet;
						break;
					}
					r_skill = Scarlet.DaemonAttack[2];
					break;
				}
				else if(hp_per > 50.0)
				{
					if(Rnd.get(10000) < 750)
					{
						r_skill = Scarlet.YokeOfScarlet;
						break;
					}
					if(Rnd.get(10000) < 500)
					{
						r_skill = Scarlet.DaemonField[1];
						break;
					}
					if(Rnd.get(10000) < 500)
					{
						r_skill = Scarlet.DaemonDrain;
						break;
					}
					r_skill = Scarlet.DaemonAttack[2];
					break;
				}
				else if(hp_per > 25.0)
				{
					if(Rnd.get(10000) < 1000)
					{
						r_skill = Scarlet.YokeOfScarlet;
						break;
					}
					if(Rnd.get(10000) < 1000)
					{
						r_skill = Scarlet.DaemonField[1];
						break;
					}
					if(Rnd.get(10000) < 500)
					{
						r_skill = Scarlet.DaemonDrain;
						break;
					}
					r_skill = Scarlet.DaemonAttack[2];
					break;
				}
				else if(hp_per > 10.0)
				{
					if(Rnd.get(10000) < 1000)
					{
						r_skill = Scarlet.YokeOfScarlet;
						break;
					}
					if(Rnd.get(10000) < 1000)
					{
						r_skill = Scarlet.DaemonField[1];
						break;
					}
					if(Rnd.get(10000) < 1000)
					{
						r_skill = Scarlet.DaemonDrain;
						break;
					}
					r_skill = Scarlet.DaemonAttack[2];
					break;
				}
				else
				{
					if(Rnd.get(10000) < 500)
					{
						r_skill = Scarlet.YokeOfScarlet;
						break;
					}
					if(Rnd.get(10000) < 500)
					{
						r_skill = Scarlet.DaemonField[1];
						break;
					}
					if(Rnd.get(10000) < 1000)
					{
						r_skill = Scarlet.DaemonDrain;
						break;
					}
					r_skill = Scarlet.DaemonAttack[2];
					break;
				}
			}
		}
		if(r_skill == null)
			r_skill = Scarlet.DaemonAttack[stage];
		return chooseTaskAndTargets(r_skill, target, distance);
	}

	@Override
	public boolean isGlobalAI()
	{
		return true;
	}

	@Override
	protected void onEvtDead(final Creature killer)
	{
		final NpcInstance actor = getActor();
		if(actor != null && actor.getNpcId() == 29047)
			ThreadPoolManager.getInstance().schedule(new Runnable(){
				@Override
				public void run()
				{
					ItemInstance.deleteItems(8192);
				}
			}, 10000L);
		super.onEvtDead(killer);
	}

	static
	{
		DaemonAttack = new Skill[] {
				SkillTable.getInstance().getInfo(5014, 1),
				SkillTable.getInstance().getInfo(5014, 2),
				SkillTable.getInstance().getInfo(5014, 3) };
		DaemonCharge = new Skill[] {
				SkillTable.getInstance().getInfo(5015, 1),
				SkillTable.getInstance().getInfo(5015, 2),
				SkillTable.getInstance().getInfo(5015, 3),
				SkillTable.getInstance().getInfo(5015, 4),
				SkillTable.getInstance().getInfo(5015, 5),
				SkillTable.getInstance().getInfo(5015, 6) };
		DaemonField = new Skill[] { SkillTable.getInstance().getInfo(5018, 1), SkillTable.getInstance().getInfo(5018, 2) };
		YokeOfScarlet = SkillTable.getInstance().getInfo(5016, 1);
		DaemonDrain = SkillTable.getInstance().getInfo(5019, 1);
	}
}
