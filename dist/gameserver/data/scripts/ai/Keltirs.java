package ai;

import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.ai.Fighter;
import l2s.gameserver.geodata.GeoEngine;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.tables.NpcTable;
import l2s.gameserver.utils.Location;

public class Keltirs extends Fighter
{
	private static final int range = 600;
	private static final int voicetime = 8000;
	private long _lastAction;
	private static final String[] _retreatText;
	private static final String[] _fightText;
	private static final int[] _list;

	public Keltirs(final NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected boolean createNewTask()
	{
		final NpcInstance actor = getActor();
		if(actor == null)
			return false;
		if(!Rnd.chance(60))
		{
			final Location sloc = actor.getSpawnedLoc();
			final int spawnX = sloc.x;
			final int spawnY = sloc.y;
			final int spawnZ = sloc.z;
			final int x = spawnX + Rnd.get(1200) - 600;
			final int y = spawnY + Rnd.get(1200) - 600;
			final int z = GeoEngine.getLowerHeight(x, y, spawnZ, actor.getGeoIndex());
			actor.setRunning();
			actor.moveToLocation(x, y, z, 0, true);
			this.addTaskMove(spawnX, spawnY, spawnZ, false);
			if(System.currentTimeMillis() - _lastAction > 8000L)
			{
				Functions.npcSay(actor, Keltirs._retreatText[Rnd.get(Keltirs._retreatText.length)]);
				_lastAction = System.currentTimeMillis();
			}
			return true;
		}
		final Creature target;
		if((target = prepareTarget()) == null)
			return false;
		this.addTaskAttack(target);
		if(System.currentTimeMillis() - _lastAction > 8000L)
		{
			Functions.npcSay(actor, Keltirs._fightText[Rnd.get(Keltirs._fightText.length)]);
			_lastAction = System.currentTimeMillis();
		}
		return true;
	}

	public static void onLoad()
	{
		if(Config.ALT_AI_KELTIRS)
			for(int id : Keltirs._list)
			{
				for(NpcInstance i : GameObjectsStorage.getNpcs(false, id))
					i.setAI(new Keltirs(i));
				NpcTable.getTemplate(id).ai_type = "Keltirs";
			}
	}

	static
	{
		_retreatText = new String[] {
				"\u041d\u0435 \u0442\u0440\u043e\u0433\u0430\u0439 \u043c\u0435\u043d\u044f, \u044f \u0431\u043e\u044e\u0441\u044c!",
				"\u0422\u044b \u0441\u0442\u0440\u0430\u0448\u043d\u044b\u0439! \u0411\u0440\u0430\u0442\u044c\u044f, \u0443\u0431\u0435\u0433\u0430\u0435\u043c!",
				"\u041f\u043e\u043b\u0443\u043d\u0434\u0440\u0430! \u0421\u0435\u0437\u043e\u043d \u043e\u0445\u043e\u0442\u044b \u043e\u0442\u043a\u0440\u044b\u0442!!!",
				"\u0415\u0441\u043b\u0438 \u0435\u0449\u0435 \u0440\u0430\u0437 \u043c\u0435\u043d\u044f \u0443\u0434\u0430\u0440\u0438\u0448\u044c - \u0443 \u0442\u0435\u0431\u044f \u0431\u0443\u0434\u0443\u0442 \u043d\u0435\u043f\u0440\u0438\u044f\u0442\u043d\u043e\u0441\u0442\u0438!",
				"\u0411\u0440\u0430\u043a\u043e\u043d\u044c\u0435\u0440, \u044f \u0442\u0435\u0431\u044f \u0441\u0434\u0430\u043c \u043f\u0440\u0430\u0432\u043e\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u0435\u043b\u044c\u043d\u044b\u043c \u043e\u0440\u0433\u0430\u043d\u0430\u043c!",
				"\u0414\u0435\u043b\u0430\u0435\u043c \u043d\u043e\u0433\u0438, \u0437\u0430 60 \u0441\u0435\u043a ^-_-^",
				"\u041d\u0430\u0441 \u043d\u0435 \u0434\u043e\u0433\u043e\u043d\u044f\u0442, \u043d\u0430\u0441 \u043d\u0435 \u0434\u043e\u0433\u043e\u043d\u044f\u0442..." };
		_fightText = new String[] {
				"\u0412\u0441\u0435\u0445 \u0443\u0431\u044c\u044e, \u043e\u0434\u0438\u043d \u043e\u0441\u0442\u0430\u043d\u0443\u0441\u044c!",
				"\u0420\u0440\u0440\u0440\u0440\u0440\u0440\u0440!",
				"\u0411\u0435\u0439 \u0433\u0430\u0434\u0430!",
				"\u0425\u043e\u0447\u0435\u0448\u044c, \u0437\u0430 \u0436\u043e\u043f\u0443 \u0443\u043a\u0443\u0448\u0443",
				"\u0429\u0430\u0441 \u041a\u0423\u0421\u042c \u0432\u0441\u0435\u043c \u0441\u0434\u0435\u043b\u0430\u044e..." };
		_list = new int[] {
				20481,
				20529,
				20530,
				20531,
				20532,
				20533,
				20534,
				20535,
				20536,
				20537,
				20538,
				20539,
				20544,
				20545,
				22229,
				22230,
				22231,
				18003 };
	}
}
