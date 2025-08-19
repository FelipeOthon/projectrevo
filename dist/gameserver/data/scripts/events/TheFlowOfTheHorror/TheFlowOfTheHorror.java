package events.TheFlowOfTheHorror;

import java.util.ArrayList;
import java.util.List;

import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.idfactory.IdFactory;
import l2s.gameserver.instancemanager.ServerVariables;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.MonsterInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.tables.NpcTable;
import l2s.gameserver.templates.npc.NpcTemplate;
import l2s.gameserver.utils.Location;

public class TheFlowOfTheHorror extends Functions implements ScriptFile
{
	private static int Gilmore;
	private static int Shackle;
	private static NpcInstance oldGilmore;
	private static int _stage;
	private static ArrayList<MonsterInstance> _spawns;
	private static ArrayList<Location> points11;
	private static ArrayList<Location> points12;
	private static ArrayList<Location> points13;
	private static ArrayList<Location> points21;
	private static ArrayList<Location> points22;
	private static ArrayList<Location> points23;
	private static ArrayList<Location> points31;
	private static ArrayList<Location> points32;
	private static ArrayList<Location> points33;

	@Override
	public void onLoad()
	{
		TheFlowOfTheHorror.points11.add(new Location(84211, 117965, -3020));
		TheFlowOfTheHorror.points11.add(new Location(83389, 117590, -3036));
		TheFlowOfTheHorror.points11.add(new Location(82226, 117051, -3150));
		TheFlowOfTheHorror.points11.add(new Location(80902, 116155, -3533));
		TheFlowOfTheHorror.points11.add(new Location(79832, 115784, -3733));
		TheFlowOfTheHorror.points11.add(new Location(78442, 116510, -3823));
		TheFlowOfTheHorror.points11.add(new Location(76299, 117355, -3786));
		TheFlowOfTheHorror.points11.add(new Location(74244, 117674, -3785));
		TheFlowOfTheHorror.points12.add(new Location(84231, 117597, -3020));
		TheFlowOfTheHorror.points12.add(new Location(82536, 116986, -3093));
		TheFlowOfTheHorror.points12.add(new Location(79428, 116341, -3749));
		TheFlowOfTheHorror.points12.add(new Location(76970, 117362, -3771));
		TheFlowOfTheHorror.points12.add(new Location(74322, 117845, -3767));
		TheFlowOfTheHorror.points13.add(new Location(83962, 118387, -3022));
		TheFlowOfTheHorror.points13.add(new Location(81960, 116925, -3216));
		TheFlowOfTheHorror.points13.add(new Location(80223, 116059, -3665));
		TheFlowOfTheHorror.points13.add(new Location(78214, 116783, -3854));
		TheFlowOfTheHorror.points13.add(new Location(76208, 117462, -3791));
		TheFlowOfTheHorror.points13.add(new Location(74278, 117454, -3804));
		TheFlowOfTheHorror.points21.add(new Location(79192, 111481, -3011));
		TheFlowOfTheHorror.points21.add(new Location(79014, 112396, -3090));
		TheFlowOfTheHorror.points21.add(new Location(79309, 113692, -3437));
		TheFlowOfTheHorror.points21.add(new Location(79350, 115337, -3758));
		TheFlowOfTheHorror.points21.add(new Location(78390, 116309, -3772));
		TheFlowOfTheHorror.points21.add(new Location(76794, 117092, -3821));
		TheFlowOfTheHorror.points21.add(new Location(74451, 117623, -3797));
		TheFlowOfTheHorror.points22.add(new Location(79297, 111456, -3017));
		TheFlowOfTheHorror.points22.add(new Location(79020, 112217, -3087));
		TheFlowOfTheHorror.points22.add(new Location(79167, 113236, -3289));
		TheFlowOfTheHorror.points22.add(new Location(79513, 115408, -3752));
		TheFlowOfTheHorror.points22.add(new Location(78555, 116816, -3812));
		TheFlowOfTheHorror.points22.add(new Location(76932, 117277, -3781));
		TheFlowOfTheHorror.points22.add(new Location(75422, 117788, -3755));
		TheFlowOfTheHorror.points22.add(new Location(74223, 117898, -3753));
		TheFlowOfTheHorror.points23.add(new Location(79635, 110741, -3003));
		TheFlowOfTheHorror.points23.add(new Location(78994, 111858, -3061));
		TheFlowOfTheHorror.points23.add(new Location(79088, 112949, -3226));
		TheFlowOfTheHorror.points23.add(new Location(79424, 114499, -3674));
		TheFlowOfTheHorror.points23.add(new Location(78913, 116266, -3779));
		TheFlowOfTheHorror.points23.add(new Location(76930, 117137, -3819));
		TheFlowOfTheHorror.points23.add(new Location(75533, 117569, -3781));
		TheFlowOfTheHorror.points23.add(new Location(74255, 117398, -3804));
		TheFlowOfTheHorror.points31.add(new Location(83128, 111358, -3663));
		TheFlowOfTheHorror.points31.add(new Location(81538, 111896, -3631));
		TheFlowOfTheHorror.points31.add(new Location(80312, 113837, -3752));
		TheFlowOfTheHorror.points31.add(new Location(79012, 115998, -3772));
		TheFlowOfTheHorror.points31.add(new Location(77377, 117052, -3812));
		TheFlowOfTheHorror.points31.add(new Location(75394, 117608, -3772));
		TheFlowOfTheHorror.points31.add(new Location(73998, 117647, -3784));
		TheFlowOfTheHorror.points32.add(new Location(83245, 110790, -3772));
		TheFlowOfTheHorror.points32.add(new Location(81832, 111379, -3641));
		TheFlowOfTheHorror.points32.add(new Location(81405, 112403, -3648));
		TheFlowOfTheHorror.points32.add(new Location(79827, 114496, -3752));
		TheFlowOfTheHorror.points32.add(new Location(78174, 116968, -3821));
		TheFlowOfTheHorror.points32.add(new Location(75944, 117653, -3777));
		TheFlowOfTheHorror.points32.add(new Location(74379, 117939, -3755));
		TheFlowOfTheHorror.points33.add(new Location(82584, 111930, -3568));
		TheFlowOfTheHorror.points33.add(new Location(81389, 111989, -3647));
		TheFlowOfTheHorror.points33.add(new Location(80129, 114044, -3748));
		TheFlowOfTheHorror.points33.add(new Location(79190, 115579, -3743));
		TheFlowOfTheHorror.points33.add(new Location(77989, 116811, -3849));
		TheFlowOfTheHorror.points33.add(new Location(76009, 117405, -3800));
		TheFlowOfTheHorror.points33.add(new Location(74113, 117441, -3797));
		if(isActive())
		{
			activateAI();
			ScriptFile._log.info("Loaded Event: The Flow Of The Horror [state: activated]");
		}
		else
			ScriptFile._log.info("Loaded Event: The Flow Of The Horror [state: deactivated]");
	}

	public static void spawnNewWave()
	{
		spawn(TheFlowOfTheHorror.Shackle, TheFlowOfTheHorror.points11);
		spawn(TheFlowOfTheHorror.Shackle, TheFlowOfTheHorror.points12);
		spawn(TheFlowOfTheHorror.Shackle, TheFlowOfTheHorror.points13);
		spawn(TheFlowOfTheHorror.Shackle, TheFlowOfTheHorror.points21);
		spawn(TheFlowOfTheHorror.Shackle, TheFlowOfTheHorror.points22);
		spawn(TheFlowOfTheHorror.Shackle, TheFlowOfTheHorror.points23);
		spawn(TheFlowOfTheHorror.Shackle, TheFlowOfTheHorror.points31);
		spawn(TheFlowOfTheHorror.Shackle, TheFlowOfTheHorror.points32);
		spawn(TheFlowOfTheHorror.Shackle, TheFlowOfTheHorror.points33);
		TheFlowOfTheHorror._stage = 2;
	}

	private static void spawn(final int id, final ArrayList<Location> points)
	{
		final NpcTemplate template = NpcTable.getTemplate(id);
		final MonsterInstance monster = new MonsterInstance(IdFactory.getInstance().getNextId(), template);
		monster.setCurrentHpMp(monster.getMaxHp(), monster.getMaxMp(), true);
		monster.setXYZ(points.get(0).getX(), points.get(0).getY(), points.get(0).getZ());
		final MonstersAI ai = new MonstersAI(monster);
		monster.setAI(ai);
		monster.spawnMe();
		ai.setPoints(points);
		ai.startAITask();
		TheFlowOfTheHorror._spawns.add(monster);
	}

	private void activateAI()
	{
		List<NpcInstance> targets = GameObjectsStorage.getNpcs(false, TheFlowOfTheHorror.Gilmore);
		if(!targets.isEmpty())
		{
			oldGilmore = targets.get(0);
			oldGilmore.decayMe();
			final NpcTemplate template = NpcTable.getTemplate(TheFlowOfTheHorror.Gilmore);
			final MonsterInstance monster = new MonsterInstance(IdFactory.getInstance().getNextId(), template);
			monster.setCurrentHpMp(monster.getMaxHp(), monster.getMaxMp(), true);
			monster.setXYZ(73329, 117705, -3741);
			final GilmoreAI ai = new GilmoreAI(monster);
			monster.setAI(ai);
			monster.spawnMe();
			ai.startAITask();
			TheFlowOfTheHorror._spawns.add(monster);
		}
	}

	private void deactivateAI()
	{
		for(final MonsterInstance monster : TheFlowOfTheHorror._spawns)
			if(monster != null)
			{
				monster.getAI().stopAITask();
				monster.deleteMe();
			}
		if(TheFlowOfTheHorror.oldGilmore != null)
			TheFlowOfTheHorror.oldGilmore.spawnMe();
	}

	private static boolean isActive()
	{
		return ServerVariables.getString("TheFlowOfTheHorror", "off").equalsIgnoreCase("on");
	}

	public void startEvent()
	{
		final Player player = getSelf();
		if(!player.getPlayerAccess().IsEventGm)
			return;
		if(!isActive())
		{
			ServerVariables.set("TheFlowOfTheHorror", "on");
			activateAI();
			ScriptFile._log.info("Event 'The Flow Of The Horror' started.");
		}
		else
			player.sendMessage("Event 'The Flow Of The Horror' already started.");
		show(HtmCache.getInstance().getHtml("admin/events.htm", player), player);
	}

	public void stopEvent()
	{
		final Player player = getSelf();
		if(!player.getPlayerAccess().IsEventGm)
			return;
		if(isActive())
		{
			ServerVariables.unset("TheFlowOfTheHorror");
			deactivateAI();
			ScriptFile._log.info("Event 'The Flow Of The Horror' stopped.");
		}
		else
			player.sendMessage("Event 'The Flow Of The Horror' not started.");
		show(HtmCache.getInstance().getHtml("admin/events.htm", player), player);
	}

	@Override
	public void onReload()
	{
		deactivateAI();
	}

	@Override
	public void onShutdown()
	{
		deactivateAI();
	}

	public static int getStage()
	{
		return TheFlowOfTheHorror._stage;
	}

	public static void setStage(final int stage)
	{
		TheFlowOfTheHorror._stage = stage;
	}

	static
	{
		TheFlowOfTheHorror.Gilmore = 30754;
		TheFlowOfTheHorror.Shackle = 20235;
		TheFlowOfTheHorror._stage = 1;
		TheFlowOfTheHorror._spawns = new ArrayList<MonsterInstance>();
		TheFlowOfTheHorror.points11 = new ArrayList<Location>();
		TheFlowOfTheHorror.points12 = new ArrayList<Location>();
		TheFlowOfTheHorror.points13 = new ArrayList<Location>();
		TheFlowOfTheHorror.points21 = new ArrayList<Location>();
		TheFlowOfTheHorror.points22 = new ArrayList<Location>();
		TheFlowOfTheHorror.points23 = new ArrayList<Location>();
		TheFlowOfTheHorror.points31 = new ArrayList<Location>();
		TheFlowOfTheHorror.points32 = new ArrayList<Location>();
		TheFlowOfTheHorror.points33 = new ArrayList<Location>();
	}
}
