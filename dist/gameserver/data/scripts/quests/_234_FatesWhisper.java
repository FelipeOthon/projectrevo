package quests;

import java.util.List;

import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _234_FatesWhisper extends Quest implements ScriptFile
{
	private static final int PIPETTE_KNIFE = 4665;
	private static final int REIRIAS_SOUL_ORB = 4666;
	private static final int KERNONS_INFERNIUM_SCEPTER = 4667;
	private static final int GOLCONDAS_INFERNIUM_SCEPTER = 4668;
	private static final int HALLATES_INFERNIUM_SCEPTER = 4669;
	private static final int REORINS_HAMMER = 4670;
	private static final int REORINS_MOLD = 4671;
	private static final int INFERNIUM_VARNISH = 4672;
	private static final int RED_PIPETTE_KNIFE = 4673;
	private static final int STAR_OF_DESTINY = 5011;
	private static final int CRYSTAL_B = 1460;
	private static final int Damaskus = 79;
	private static final int Lance = 97;
	private static final int Samurai = 2626;
	private static final int Staff = 210;
	private static final int BOP = 287;
	private static final int Battle = 175;
	private static final int Demons = 234;
	private static final int Bellion = 268;
	private static final int Glory = 171;
	private static final int WizTear = 7889;
	private static final int GuardianSword = 7883;
	private static final int Tallum = 80;
	private static final int Infernal = 7884;
	private static final int Carnage = 288;
	private static final int Halberd = 98;
	private static final int Elemental = 150;
	private static final int Dasparion = 212;
	private static final int Spiritual = 7894;
	private static final int Bloody = 235;
	private static final int Blood = 269;
	private static final int Meteor = 2504;
	private static final int Destroyer = 7899;
	private static final int Keshanberk = 5704;
	private static final int REORIN = 31002;
	private static final int CLIFF = 30182;
	private static final int FERRIS = 30847;
	private static final int ZENKIN = 30178;
	private static final int KASPAR = 30833;
	private static final int CABRIOCOFFER = 31027;
	private static final int CHEST_KERNON = 31028;
	private static final int CHEST_GOLKONDA = 31029;
	private static final int CHEST_HALLATE = 31030;
	private static final int SHILLEN_MESSAGER = 25035;
	private static final int DEATH_LORD = 25220;
	private static final int KERNON = 25054;
	private static final int LONGHORN = 25126;
	private static final int BAIUM = 29020;

	@Override
	public void onLoad()
	{}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	public _234_FatesWhisper()
	{
		super(false);
		this.addStartNpc(31002);
		this.addTalkId(new int[] { 31002 });
		this.addTalkId(new int[] { 30182 });
		this.addTalkId(new int[] { 30847 });
		this.addTalkId(new int[] { 30178 });
		this.addTalkId(new int[] { 30833 });
		this.addTalkId(new int[] { 31027 });
		this.addTalkId(new int[] { 31028 });
		this.addTalkId(new int[] { 31029 });
		this.addTalkId(new int[] { 31030 });
		this.addKillId(new int[] { 25035 });
		this.addKillId(new int[] { 25220 });
		this.addKillId(new int[] { 25054 });
		this.addKillId(new int[] { 25126 });
		addAttackId(new int[] { 29020 });
		addQuestItem(new int[] { 4666, 4669, 4667, 4668, 4672, 4670, 4671, 4665, 4673 });
	}

	@Override
	public String onEvent(final String event, final QuestState st, final NpcInstance npc)
	{
		String htmltext = event;
		int oldweapon = 0;
		int newweapon = 0;
		if(event.equalsIgnoreCase("31002-03.htm"))
		{
			st.set("cond", "1");
			st.setState(2);
		}
		else if(event.equalsIgnoreCase("31002-05b.htm"))
		{
			st.takeItems(4666, -1L);
			st.set("cond", "2");
			st.setState(2);
		}
		else if(event.equalsIgnoreCase("31030-02.htm"))
		{
			st.setState(2);
			st.giveItems(4669, 1L, false);
		}
		else if(event.equalsIgnoreCase("31028-02.htm"))
		{
			st.setState(2);
			st.giveItems(4667, 1L, false);
		}
		else if(event.equalsIgnoreCase("31029-02.htm"))
		{
			st.setState(2);
			st.giveItems(4668, 1L, false);
		}
		else if(event.equalsIgnoreCase("31002-06a.htm"))
		{
			st.takeItems(4669, -1L);
			st.takeItems(4667, -1L);
			st.takeItems(4668, -1L);
			st.set("cond", "3");
			st.setState(2);
		}
		else if(event.equalsIgnoreCase("30182-01c.htm"))
		{
			st.takeItems(4672, -1L);
			st.setState(2);
			st.giveItems(4672, 1L, false);
		}
		else if(event.equalsIgnoreCase("31002-07a.htm"))
		{
			st.set("cond", "4");
			st.setState(2);
		}
		else if(event.equalsIgnoreCase("31002-08a.htm"))
		{
			st.takeItems(4670, -1L);
			st.set("cond", "5");
			st.setState(2);
		}
		else if(event.equalsIgnoreCase("30833-01b.htm"))
		{
			st.set("cond", "7");
			st.giveItems(4665, 1L, false);
			st.setState(2);
		}
		else if(event.equalsIgnoreCase("Damaskus.htm"))
			oldweapon = 79;
		else if(event.equalsIgnoreCase("Samurai.htm"))
			oldweapon = 2626;
		else if(event.equalsIgnoreCase("BOP.htm"))
			oldweapon = 287;
		else if(event.equalsIgnoreCase("Lance.htm"))
			oldweapon = 97;
		else if(event.equalsIgnoreCase("Battle.htm"))
			oldweapon = 175;
		else if(event.equalsIgnoreCase("Staff.htm"))
			oldweapon = 210;
		else if(event.equalsIgnoreCase("Demons.htm"))
			oldweapon = 234;
		else if(event.equalsIgnoreCase("Bellion.htm"))
			oldweapon = 268;
		else if(event.equalsIgnoreCase("Glory.htm"))
			oldweapon = 171;
		else if(event.equalsIgnoreCase("WizTear.htm"))
			oldweapon = 7889;
		else if(event.equalsIgnoreCase("GuardianSword.htm"))
			oldweapon = 7883;
		else if(event.equalsIgnoreCase("Tallum"))
			newweapon = 80;
		else if(event.equalsIgnoreCase("Infernal"))
			newweapon = 7884;
		else if(event.equalsIgnoreCase("Carnage"))
			newweapon = 288;
		else if(event.equalsIgnoreCase("Halberd"))
			newweapon = 98;
		else if(event.equalsIgnoreCase("Elemental"))
			newweapon = 150;
		else if(event.equalsIgnoreCase("Dasparion"))
			newweapon = 212;
		else if(event.equalsIgnoreCase("Spiritual"))
			newweapon = 7894;
		else if(event.equalsIgnoreCase("Bloody"))
			newweapon = 235;
		else if(event.equalsIgnoreCase("Blood"))
			newweapon = 269;
		else if(event.equalsIgnoreCase("Meteor"))
			newweapon = 2504;
		else if(event.equalsIgnoreCase("Destroyer"))
			newweapon = 7899;
		else if(event.equalsIgnoreCase("Keshanberk"))
			newweapon = 5704;
		else if(event.equalsIgnoreCase("CABRIOCOFFER_Fail"))
		{
			List<NpcInstance> isQuest = GameObjectsStorage.getNpcs(false, 31027);
			if(!isQuest.isEmpty())
				isQuest.get(0).deleteMe();
		}
		else if(event.equalsIgnoreCase("CHEST_HALLATE_Fail"))
		{
			List<NpcInstance> isQuest = GameObjectsStorage.getNpcs(false, 31030);
			if(!isQuest.isEmpty())
				isQuest.get(0).deleteMe();
		}
		else if(event.equalsIgnoreCase("CHEST_KERNON_Fail"))
		{
			List<NpcInstance> isQuest = GameObjectsStorage.getNpcs(false, 31028);
			if(!isQuest.isEmpty())
				isQuest.get(0).deleteMe();
		}
		else if(event.equalsIgnoreCase("CHEST_GOLKONDA_Fail"))
		{
			List<NpcInstance> isQuest = GameObjectsStorage.getNpcs(false, 31029);
			if(!isQuest.isEmpty())
				isQuest.get(0).deleteMe();
		}
		if(oldweapon != 0)
			if(st.getQuestItemsCount(oldweapon) >= 1L)
			{
				if(st.getQuestItemsCount(1460) >= 984L)
				{
					st.set("oldweapon", String.valueOf(oldweapon));
					st.takeItems(1460, 984L);
					st.set("cond", "10");
					st.setState(2);
				}
				else
					htmltext = "cheeter.htm";
			}
			else
				htmltext = "noweapon.htm";
		if(newweapon != 0)
		{
			final ItemInstance[] olditem = st.getPlayer().getInventory().getAllItemsById(st.getInt("oldweapon"));
			ItemInstance itemtotake = null;
			for(final ItemInstance i : olditem)
				if(!i.isAugmented() && i.getEnchantLevel() == 0)
				{
					itemtotake = i;
					break;
				}
			if(itemtotake != null)
			{
				st.getPlayer().getInventory().destroyItem(itemtotake, 1L, true);
				st.giveItems(newweapon, 1L, false);
				st.giveItems(5011, 1L, false);
				st.unset("cond");
				st.unset("oldweapon");
				st.playSound(Quest.SOUND_FINISH);
				htmltext = "make.htm";
				st.exitCurrentQuest(false);
			}
			else
				htmltext = "noweapon.htm";
		}
		return htmltext;
	}

	@Override
	public String onTalk(final NpcInstance npc, final QuestState st)
	{
		if(st.getState() == 3)
			return "completed";
		final int npcId = npc.getNpcId();
		String htmltext = "noquest";
		final int cond = st.getInt("cond");
		if(npcId == 31002)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 75)
					htmltext = "31002-02.htm";
				else
				{
					htmltext = "31002-01.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1 && st.getQuestItemsCount(4666) >= 1L)
				htmltext = "31002-05.htm";
			else if(cond == 2 && st.getQuestItemsCount(4669) >= 1L && st.getQuestItemsCount(4667) >= 1L && st.getQuestItemsCount(4668) >= 1L)
				htmltext = "31002-06.htm";
			else if(cond == 3 && st.getQuestItemsCount(4672) >= 1L)
				htmltext = "31002-07.htm";
			else if(cond == 4 && st.getQuestItemsCount(4670) >= 1L)
				htmltext = "31002-08.htm";
			else if(cond == 8 && st.getQuestItemsCount(4671) >= 1L)
			{
				st.takeItems(4671, -1L);
				st.set("cond", "9");
				st.setState(2);
				htmltext = "31002-09.htm";
			}
			else if(cond == 9 && st.getQuestItemsCount(1460) >= 984L)
				htmltext = "31002-10.htm";
			else if(cond == 10)
				htmltext = "a-grade.htm";
		}
		else if(npcId == 31027 && cond == 1 && st.getQuestItemsCount(4666) == 0L)
		{
			st.setState(2);
			st.giveItems(4666, 1L, false);
			htmltext = "31027-01.htm";
		}
		else if(npcId == 31030 && cond == 2 && st.getQuestItemsCount(4669) == 0L)
			htmltext = "31030-01.htm";
		else if(npcId == 31028 && cond == 2 && st.getQuestItemsCount(4667) == 0L)
			htmltext = "31028-01.htm";
		else if(npcId == 31029 && cond == 2 && st.getQuestItemsCount(4668) == 0L)
			htmltext = "31029-01.htm";
		else if(npcId == 30182 && cond == 3 && st.getQuestItemsCount(4672) == 0L)
			htmltext = "30182-01.htm";
		else if(npcId == 30847 && cond == 4 && st.getQuestItemsCount(4670) == 0L)
		{
			st.giveItems(4670, 1L, false);
			htmltext = "30847-01.htm";
		}
		else if(npcId == 30178 && st.getQuestItemsCount(4671) == 0L)
		{
			st.set("cond", "6");
			st.setState(2);
			htmltext = "30178-01.htm";
		}
		else if(npcId == 30833)
			if(cond == 6)
				htmltext = "30833-01.htm";
			else if(cond == 7 && st.getQuestItemsCount(4673) == 1L)
			{
				st.set("cond", "8");
				st.setState(2);
				st.takeItems(4673, -1L);
				st.giveItems(4671, 1L, false);
				htmltext = "30833-03.htm";
			}
		return htmltext;
	}

	@Override
	public String onKill(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(cond == 1 && npcId == 25035)
		{
			List<NpcInstance> isQuest = GameObjectsStorage.getNpcs(false, 31027);
			if(isQuest.isEmpty())
			{
				st.addSpawn(31027);
				st.playSound(Quest.SOUND_MIDDLE);
				st.startQuestTimer("CABRIOCOFFER_Fail", 120000L);
			}
		}
		if(cond == 2 && npcId == 25220)
		{
			List<NpcInstance> isQuest = GameObjectsStorage.getNpcs(false, 31030);
			if(isQuest.isEmpty())
			{
				st.addSpawn(31030);
				st.playSound(Quest.SOUND_MIDDLE);
				st.startQuestTimer("CHEST_HALLATE_Fail", 120000L);
			}
		}
		if(cond == 2 && npcId == 25054)
		{
			List<NpcInstance> isQuest = GameObjectsStorage.getNpcs(false, 31028);
			if(isQuest.isEmpty())
			{
				st.addSpawn(31028);
				st.playSound(Quest.SOUND_MIDDLE);
				st.startQuestTimer("CHEST_KERNON_Fail", 120000L);
			}
		}
		if(cond == 2 && npcId == 25126)
		{
			List<NpcInstance> isQuest = GameObjectsStorage.getNpcs(false, 31029);
			if(isQuest.isEmpty())
			{
				st.addSpawn(31029);
				st.playSound(Quest.SOUND_MIDDLE);
				st.startQuestTimer("CHEST_GOLKONDA_Fail", 120000L);
			}
		}
		return null;
	}

	@Override
	public String onAttack(final NpcInstance npc, final QuestState st)
	{
		final int npcId = npc.getNpcId();
		final int cond = st.getInt("cond");
		if(cond == 7 && npcId == 29020 && st.getQuestItemsCount(4665) >= 1L && st.getQuestItemsCount(4673) == 0L && st.getItemEquipped(7) == 4665)
		{
			st.takeItems(4665, -1L);
			st.giveItems(4673, 1L, false);
			st.playSound(Quest.SOUND_ITEMGET);
		}
		return null;
	}
}
