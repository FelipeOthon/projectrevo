package events.TheFlowOfTheHorror;

import l2s.commons.util.Rnd;
import l2s.gameserver.ai.CtrlIntention;
import l2s.gameserver.ai.Fighter;
import l2s.gameserver.model.World;
import l2s.gameserver.model.instances.MonsterInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.L2GameServerPacket;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.skills.Stats;
import l2s.gameserver.skills.funcs.FuncMul;
import l2s.gameserver.tables.SkillTable;
import l2s.gameserver.utils.Location;

public class GilmoreAI extends Fighter
{
	private Location[] points_stage1;
	private Location[] points_stage2;
	private String[] text_stage1;
	private String[] text_stage2;
	private long wait_timeout;
	private boolean wait;
	private int index;
	private int step_stage2;

	public GilmoreAI(final NpcInstance actor)
	{
		super(actor);
		points_stage1 = new Location[7];
		points_stage2 = new Location[1];
		text_stage1 = new String[7];
		text_stage2 = new String[2];
		wait_timeout = 0L;
		wait = false;
		step_stage2 = 1;
		AI_TASK_ATTACK_DELAY = 250L;
		points_stage1[0] = new Location(73195, 118483, -3722);
		points_stage1[1] = new Location(73535, 117945, -3754);
		points_stage1[2] = new Location(73446, 117334, -3752);
		points_stage1[3] = new Location(72847, 117311, -3711);
		points_stage1[4] = new Location(72296, 117720, -3694);
		points_stage1[5] = new Location(72463, 118401, -3694);
		points_stage1[6] = new Location(72912, 117895, -3723);
		points_stage2[0] = new Location(73615, 117629, -3765);
		text_stage1[0] = "Text1";
		text_stage1[1] = "Text2";
		text_stage1[2] = "Text3";
		text_stage1[3] = "Text4";
		text_stage1[4] = "Text5";
		text_stage1[5] = "Text6";
		text_stage1[6] = "Text7";
		text_stage2[0] = "\u0413\u043e\u0442\u043e\u0432\u044b?";
		text_stage2[1] = "\u041d\u0430\u0447\u043d\u0435\u043c, \u043d\u0435\u043b\u044c\u0437\u044f \u0442\u0435\u0440\u044f\u0442\u044c \u043d\u0438 \u043c\u0438\u043d\u0443\u0442\u044b!";
	}

	@Override
	public boolean isGlobalAI()
	{
		return true;
	}

	@Override
	protected boolean thinkActive()
	{
		final NpcInstance _thisActor = getActor();
		if(_thisActor.isDead())
			return true;
		if(_def_think)
		{
			doTask();
			return true;
		}
		if(System.currentTimeMillis() > wait_timeout)
		{
			if(!wait)
			{
				switch(TheFlowOfTheHorror.getStage())
				{
					case 1:
					{
						if(Rnd.chance(30))
						{
							index = Rnd.get(text_stage1.length);
							Functions.npcShout(_thisActor, text_stage1[index], 0);
							wait_timeout = System.currentTimeMillis() + 10000L;
							return wait = true;
						}
						break;
					}
					case 2:
					{
						switch(step_stage2)
						{
							case 1:
							{
								Functions.npcShout(_thisActor, text_stage2[0], 0);
								wait_timeout = System.currentTimeMillis() + 10000L;
								return wait = true;
							}
						}
					}
				}
			}
			wait_timeout = 0L;
			wait = false;
			_thisActor.setRunning();
			switch(TheFlowOfTheHorror.getStage())
			{
				case 1:
				{
					index = Rnd.get(points_stage1.length);
					addTaskMove(points_stage1[index], true);
					doTask();
					return true;
				}
				case 2:
				{
					switch(step_stage2)
					{
						case 1:
						{
							Functions.npcShout(_thisActor, text_stage2[1], 0);
							addTaskMove(points_stage2[0], true);
							doTask();
							step_stage2 = 2;
							return true;
						}
						case 2:
						{
							_thisActor.setHeading(0);
							_thisActor.stopMove();
							_thisActor.broadcastPacketToOthers(new L2GameServerPacket[] { new MagicSkillUse(_thisActor, _thisActor, 454, 1, 3000, 0L) });
							step_stage2 = 3;
							return true;
						}
						case 3:
						{
							_thisActor.addStatFunc(new FuncMul(Stats.MAGIC_ATTACK_SPEED, 64, this, 5.0));
							_thisActor.addStatFunc(new FuncMul(Stats.MAGIC_DAMAGE, 64, this, 10.0));
							_thisActor.addStatFunc(new FuncMul(Stats.PHYSICAL_DAMAGE, 64, this, 10.0));
							_thisActor.addStatFunc(new FuncMul(Stats.RUN_SPEED, 64, this, 3.0));
							_thisActor.addSkill(SkillTable.getInstance().getInfo(1467, 1));
							_thisActor.broadcastUserInfo(true);
							step_stage2 = 4;
							return true;
						}
						case 4:
						{
							setIntention(CtrlIntention.AI_INTENTION_ATTACK, null);
							return true;
						}
						case 10:
						{
							_thisActor.removeStatsOwner(this);
							step_stage2 = 11;
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	protected boolean createNewTask()
	{
		clearTasks();
		final NpcInstance _thisActor = getActor();
		for(final NpcInstance npc : World.getAroundNpc(_thisActor, 1000, 400))
			if(Rnd.chance(10) && npc != null && npc.getNpcId() == 20235)
			{
				final MonsterInstance monster = (MonsterInstance) npc;
				if(Rnd.chance(20))
					addTaskCast(monster, _thisActor.getKnownSkill(1467));
				else
					addTaskAttack(monster);
				return true;
			}
		return true;
	}
}
