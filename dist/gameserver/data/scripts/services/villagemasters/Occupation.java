package services.villagemasters;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.instances.VillageMasterInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;

public class Occupation extends Functions implements ScriptFile
{
	@Override
	public void onLoad()
	{
		ScriptFile._log.info("Loaded Service: Villagemasters [Changing occupations]");
	}

	public void onTalk30026()
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final ClassId classId = pl.getClassId();
		String htmltext;
		if(classId == ClassId.Fighter)
			htmltext = "01.htm";
		else if(classId == ClassId.Warrior || classId == ClassId.Knight || classId == ClassId.Rogue)
			htmltext = "08.htm";
		else if(classId == ClassId.Warlord || classId == ClassId.Paladin || classId == ClassId.TreasureHunter)
			htmltext = "09.htm";
		else if(classId == ClassId.Gladiator || classId == ClassId.DarkAvenger || classId == ClassId.Hawkeye)
			htmltext = "09.htm";
		else
			htmltext = "10.htm";
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30026/" + htmltext, new Object[0]);
	}

	public void onTalk30031()
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final ClassId classId = pl.getClassId();
		String htmltext;
		if(classId == ClassId.Wizard || classId == ClassId.Cleric)
			htmltext = "06.htm";
		else if(classId == ClassId.Sorceror || classId == ClassId.Necromancer || classId == ClassId.Warlock || classId == ClassId.Bishop || classId == ClassId.Prophet)
			htmltext = "07.htm";
		else if(classId == ClassId.Mage)
			htmltext = "01.htm";
		else
			htmltext = "08.htm";
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30031/" + htmltext, new Object[0]);
	}

	public void onTalk30037()
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final ClassId classId = pl.getClassId();
		String htmltext;
		if(classId == ClassId.ElvenMage)
			htmltext = "01.htm";
		else if(classId == ClassId.Mage)
			htmltext = "08.htm";
		else if(classId == ClassId.Wizard || classId == ClassId.Cleric || classId == ClassId.ElvenWizard || classId == ClassId.Oracle)
			htmltext = "31.htm";
		else if(classId == ClassId.Sorceror || classId == ClassId.Necromancer || classId == ClassId.Bishop || classId == ClassId.Warlock || classId == ClassId.Prophet)
			htmltext = "32.htm";
		else if(classId == ClassId.Spellsinger || classId == ClassId.Elder || classId == ClassId.ElementalSummoner)
			htmltext = "32.htm";
		else
			htmltext = "33.htm";
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30037/" + htmltext, new Object[0]);
	}

	public void onChange30037(final String[] args)
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final short MARK_OF_FAITH_ID = 1201;
		final short ETERNITY_DIAMOND_ID = 1230;
		final short LEAF_OF_ORACLE_ID = 1235;
		final short BEAD_OF_SEASON_ID = 1292;
		final short classid = Short.parseShort(args[0]);
		final int Level = pl.getLevel();
		String htmltext = "33.htm";
		if(classid == 26 && pl.getClassId() == ClassId.ElvenMage)
		{
			if(Level <= 19 && pl.getInventory().getItemByItemId(ETERNITY_DIAMOND_ID) == null)
				htmltext = "15.htm";
			else if(Level <= 19 && pl.getInventory().getItemByItemId(ETERNITY_DIAMOND_ID) != null)
				htmltext = "16.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(ETERNITY_DIAMOND_ID) == null)
				htmltext = "17.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(ETERNITY_DIAMOND_ID) != null)
			{
				pl.getInventory().destroyItemByItemId(ETERNITY_DIAMOND_ID, 1L, false);
				pl.setClassId(classid, false, true);
				htmltext = "18.htm";
			}
		}
		else if(classid == 29 && pl.getClassId() == ClassId.ElvenMage)
		{
			if(Level <= 19 && pl.getInventory().getItemByItemId(LEAF_OF_ORACLE_ID) == null)
				htmltext = "19.htm";
			if(Level <= 19 && pl.getInventory().getItemByItemId(LEAF_OF_ORACLE_ID) != null)
				htmltext = "20.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(LEAF_OF_ORACLE_ID) == null)
				htmltext = "21.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(LEAF_OF_ORACLE_ID) != null)
			{
				pl.getInventory().destroyItemByItemId(LEAF_OF_ORACLE_ID, 1L, false);
				pl.setClassId(classid, false, true);
				htmltext = "22.htm";
			}
		}
		else if(classid == 11 && pl.getClassId() == ClassId.Mage)
		{
			if(Level <= 19 && pl.getInventory().getItemByItemId(BEAD_OF_SEASON_ID) == null)
				htmltext = "23.htm";
			if(Level <= 19 && pl.getInventory().getItemByItemId(BEAD_OF_SEASON_ID) != null)
				htmltext = "24.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(BEAD_OF_SEASON_ID) == null)
				htmltext = "25.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(BEAD_OF_SEASON_ID) != null)
			{
				pl.getInventory().destroyItemByItemId(BEAD_OF_SEASON_ID, 1L, false);
				pl.setClassId(classid, false, true);
				htmltext = "26.htm";
			}
		}
		else if(classid == 15 && pl.getClassId() == ClassId.Mage)
		{
			if(Level <= 19 && pl.getInventory().getItemByItemId(MARK_OF_FAITH_ID) == null)
				htmltext = "27.htm";
			if(Level <= 19 && pl.getInventory().getItemByItemId(MARK_OF_FAITH_ID) != null)
				htmltext = "28.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(MARK_OF_FAITH_ID) == null)
				htmltext = "29.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(MARK_OF_FAITH_ID) != null)
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_FAITH_ID, 1L, false);
				pl.setClassId(classid, false, true);
				htmltext = "30.htm";
			}
		}
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30037/" + htmltext, new Object[0]);
	}

	public void onTalk30066()
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final ClassId classId = pl.getClassId();
		String htmltext;
		if(classId == ClassId.ElvenFighter)
			htmltext = "01.htm";
		else if(classId == ClassId.Fighter)
			htmltext = "08.htm";
		else if(classId == ClassId.ElvenKnight || classId == ClassId.ElvenScout || classId == ClassId.Warrior || classId == ClassId.Knight || classId == ClassId.Rogue)
			htmltext = "38.htm";
		else if(classId == ClassId.TempleKnight || classId == ClassId.PlainsWalker || classId == ClassId.SwordSinger || classId == ClassId.SilverRanger)
			htmltext = "39.htm";
		else if(classId == ClassId.Warlord || classId == ClassId.Paladin || classId == ClassId.TreasureHunter)
			htmltext = "39.htm";
		else if(classId == ClassId.Gladiator || classId == ClassId.DarkAvenger || classId == ClassId.Hawkeye)
			htmltext = "39.htm";
		else
			htmltext = "40.htm";
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30066/" + htmltext, new Object[0]);
	}

	public void onChange30066(final String[] args)
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final short MEDALLION_OF_WARRIOR_ID = 1145;
		final short SWORD_OF_RITUAL_ID = 1161;
		final short BEZIQUES_RECOMMENDATION_ID = 1190;
		final short ELVEN_KNIGHT_BROOCH_ID = 1204;
		final short REORIA_RECOMMENDATION_ID = 1217;
		final short newclass = Short.parseShort(args[0]);
		final int Level = pl.getLevel();
		final ClassId classId = pl.getClassId();
		String htmltext = "No Quest";
		if(newclass == 19 && classId == ClassId.ElvenFighter)
		{
			if(Level <= 19 && pl.getInventory().getItemByItemId(ELVEN_KNIGHT_BROOCH_ID) == null)
				htmltext = "18.htm";
			if(Level <= 19 && pl.getInventory().getItemByItemId(ELVEN_KNIGHT_BROOCH_ID) != null)
				htmltext = "19.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(ELVEN_KNIGHT_BROOCH_ID) == null)
				htmltext = "20.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(ELVEN_KNIGHT_BROOCH_ID) != null)
			{
				pl.getInventory().destroyItemByItemId(ELVEN_KNIGHT_BROOCH_ID, 1L, false);
				pl.setClassId(newclass, false, true);
				htmltext = "21.htm";
			}
		}
		if(newclass == 22 && classId == ClassId.ElvenFighter)
		{
			if(Level <= 19 && pl.getInventory().getItemByItemId(REORIA_RECOMMENDATION_ID) == null)
				htmltext = "22.htm";
			if(Level <= 19 && pl.getInventory().getItemByItemId(REORIA_RECOMMENDATION_ID) != null)
				htmltext = "23.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(REORIA_RECOMMENDATION_ID) == null)
				htmltext = "24.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(REORIA_RECOMMENDATION_ID) != null)
			{
				pl.getInventory().destroyItemByItemId(REORIA_RECOMMENDATION_ID, 1L, false);
				pl.setClassId(newclass, false, true);
				htmltext = "25.htm";
			}
		}
		if(newclass == 1 && classId == ClassId.Fighter)
		{
			if(Level <= 19 && pl.getInventory().getItemByItemId(MEDALLION_OF_WARRIOR_ID) == null)
				htmltext = "26.htm";
			if(Level <= 19 && pl.getInventory().getItemByItemId(MEDALLION_OF_WARRIOR_ID) != null)
				htmltext = "27.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(MEDALLION_OF_WARRIOR_ID) == null)
				htmltext = "28.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(MEDALLION_OF_WARRIOR_ID) != null)
			{
				pl.getInventory().destroyItemByItemId(MEDALLION_OF_WARRIOR_ID, 1L, false);
				pl.setClassId(newclass, false, true);
				htmltext = "29.htm";
			}
		}
		if(newclass == 4 && classId == ClassId.Fighter)
		{
			if(Level <= 19 && pl.getInventory().getItemByItemId(SWORD_OF_RITUAL_ID) == null)
				htmltext = "30.htm";
			if(Level <= 19 && pl.getInventory().getItemByItemId(SWORD_OF_RITUAL_ID) != null)
				htmltext = "31.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(SWORD_OF_RITUAL_ID) == null)
				htmltext = "32.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(SWORD_OF_RITUAL_ID) != null)
			{
				pl.getInventory().destroyItemByItemId(SWORD_OF_RITUAL_ID, 1L, false);
				pl.setClassId(newclass, false, true);
				htmltext = "33.htm";
			}
		}
		if(newclass == 7 && classId == ClassId.Fighter)
		{
			if(Level <= 19 && pl.getInventory().getItemByItemId(BEZIQUES_RECOMMENDATION_ID) == null)
				htmltext = "34.htm";
			if(Level <= 19 && pl.getInventory().getItemByItemId(BEZIQUES_RECOMMENDATION_ID) != null)
				htmltext = "35.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(BEZIQUES_RECOMMENDATION_ID) == null)
				htmltext = "36.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(BEZIQUES_RECOMMENDATION_ID) != null)
			{
				pl.getInventory().destroyItemByItemId(BEZIQUES_RECOMMENDATION_ID, 1L, false);
				pl.setClassId(newclass, false, true);
				htmltext = "37.htm";
			}
		}
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30066/" + htmltext, new Object[0]);
	}

	public void onTalk30511()
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final ClassId classId = pl.getClassId();
		String htmltext;
		if(classId == ClassId.Scavenger)
			htmltext = "01.htm";
		else if(classId == ClassId.DwarvenFighter)
			htmltext = "09.htm";
		else if(classId == ClassId.BountyHunter || classId == ClassId.Warsmith)
			htmltext = "10.htm";
		else
			htmltext = "11.htm";
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30511/" + htmltext, new Object[0]);
	}

	public void onChange30511(final String[] args)
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final short MARK_OF_SEARCHER_ID = 2809;
		final short MARK_OF_GUILDSMAN_ID = 3119;
		final short MARK_OF_PROSPERITY_ID = 3238;
		final short newclass = Short.parseShort(args[0]);
		final int Level = pl.getLevel();
		final ClassId classId = pl.getClassId();
		String htmltext = "No Quest";
		if(newclass == 55 && classId == ClassId.Scavenger)
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_SEARCHER_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_GUILDSMAN_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_PROSPERITY_ID) == null)
					htmltext = "05.htm";
				else
					htmltext = "06.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_SEARCHER_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_GUILDSMAN_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_PROSPERITY_ID) == null)
				htmltext = "07.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_SEARCHER_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_GUILDSMAN_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_PROSPERITY_ID, 1L, false);
				pl.setClassId(newclass, false, true);
				htmltext = "08.htm";
			}
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30511/" + htmltext, new Object[0]);
	}

	public void onTalk30070()
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final ClassId classId = pl.getClassId();
		String htmltext;
		if(classId == ClassId.ElvenMage)
			htmltext = "01.htm";
		else if(classId == ClassId.Wizard || classId == ClassId.Cleric || classId == ClassId.ElvenWizard || classId == ClassId.Oracle)
			htmltext = "31.htm";
		else if(classId == ClassId.Sorceror || classId == ClassId.Necromancer || classId == ClassId.Bishop || classId == ClassId.Warlock || classId == ClassId.Prophet || classId == ClassId.Spellsinger || classId == ClassId.Elder || classId == ClassId.ElementalSummoner)
			htmltext = "32.htm";
		else if(classId == ClassId.Mage)
			htmltext = "08.htm";
		else
			htmltext = "33.htm";
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30070/" + htmltext, new Object[0]);
	}

	public void onChange30070(final String[] args)
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final short MARK_OF_FAITH_ID = 1201;
		final short ETERNITY_DIAMOND_ID = 1230;
		final short LEAF_OF_ORACLE_ID = 1235;
		final short BEAD_OF_SEASON_ID = 1292;
		final short event = Short.parseShort(args[0]);
		final int Level = pl.getLevel();
		final ClassId classId = pl.getClassId();
		String htmltext = "No Quest";
		if(event == 26 && classId == ClassId.ElvenMage)
		{
			if(Level <= 19 && pl.getInventory().getItemByItemId(ETERNITY_DIAMOND_ID) == null)
				htmltext = "15.htm";
			if(Level <= 19 && pl.getInventory().getItemByItemId(ETERNITY_DIAMOND_ID) != null)
				htmltext = "16.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(ETERNITY_DIAMOND_ID) == null)
				htmltext = "17.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(ETERNITY_DIAMOND_ID) != null)
			{
				pl.getInventory().destroyItemByItemId(ETERNITY_DIAMOND_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "18.htm";
			}
		}
		else if(event == 29 && classId == ClassId.ElvenMage)
		{
			if(Level <= 19 && pl.getInventory().getItemByItemId(LEAF_OF_ORACLE_ID) == null)
				htmltext = "19.htm";
			if(Level <= 19 && pl.getInventory().getItemByItemId(LEAF_OF_ORACLE_ID) != null)
				htmltext = "20.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(LEAF_OF_ORACLE_ID) == null)
				htmltext = "21.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(LEAF_OF_ORACLE_ID) != null)
			{
				pl.getInventory().destroyItemByItemId(LEAF_OF_ORACLE_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "22.htm";
			}
		}
		else if(event == 11 && classId == ClassId.Mage)
		{
			if(Level <= 19 && pl.getInventory().getItemByItemId(BEAD_OF_SEASON_ID) == null)
				htmltext = "23.htm";
			if(Level <= 19 && pl.getInventory().getItemByItemId(BEAD_OF_SEASON_ID) != null)
				htmltext = "24.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(BEAD_OF_SEASON_ID) == null)
				htmltext = "25.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(BEAD_OF_SEASON_ID) != null)
			{
				pl.getInventory().destroyItemByItemId(BEAD_OF_SEASON_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "26.htm";
			}
		}
		else if(event == 15 && classId == ClassId.Mage)
		{
			if(Level <= 19 && pl.getInventory().getItemByItemId(MARK_OF_FAITH_ID) != null)
				htmltext = "27.htm";
			if(Level <= 19 && pl.getInventory().getItemByItemId(MARK_OF_FAITH_ID) == null)
				htmltext = "28.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(MARK_OF_FAITH_ID) != null)
				htmltext = "29.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(MARK_OF_FAITH_ID) == null)
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_FAITH_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "30.htm";
			}
		}
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30070/" + htmltext, new Object[0]);
	}

	public void onTalk30154()
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final ClassId classId = pl.getClassId();
		String htmltext;
		if(classId == ClassId.ElvenFighter)
			htmltext = "01.htm";
		else if(classId == ClassId.ElvenMage)
			htmltext = "02.htm";
		else if(classId == ClassId.ElvenWizard || classId == ClassId.Oracle || classId == ClassId.ElvenKnight || classId == ClassId.ElvenScout)
			htmltext = "12.htm";
		else if(pl.getRace() == Race.elf)
			htmltext = "13.htm";
		else
			htmltext = "11.htm";
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30154/" + htmltext, new Object[0]);
	}

	public void onTalk30358()
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final ClassId classId = pl.getClassId();
		String htmltext;
		if(classId == ClassId.DarkFighter)
			htmltext = "01.htm";
		else if(classId == ClassId.DarkMage)
			htmltext = "02.htm";
		else if(classId == ClassId.DarkWizard || classId == ClassId.ShillienOracle || classId == ClassId.PalusKnight || classId == ClassId.Assassin)
			htmltext = "12.htm";
		else if(pl.getRace() == Race.darkelf)
			htmltext = "13.htm";
		else
			htmltext = "11.htm";
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30358/" + htmltext, new Object[0]);
	}

	public void onTalk30498()
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final ClassId classId = pl.getClassId();
		String htmltext;
		if(classId == ClassId.DwarvenFighter)
			htmltext = "01.htm";
		else if(classId == ClassId.Scavenger || classId == ClassId.Artisan)
			htmltext = "09.htm";
		else if(pl.getRace() == Race.dwarf)
			htmltext = "10.htm";
		else
			htmltext = "11.htm";
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30498/" + htmltext, new Object[0]);
	}

	public void onChange30498(final String[] args)
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final short RING_OF_RAVEN_ID = 1642;
		final short event = Short.parseShort(args[0]);
		final int Level = pl.getLevel();
		final ClassId classId = pl.getClassId();
		String htmltext = "No Quest";
		if(event == 54 && classId == ClassId.DwarvenFighter)
		{
			if(Level <= 19 && pl.getInventory().getItemByItemId(RING_OF_RAVEN_ID) == null)
				htmltext = "05.htm";
			if(Level <= 19 && pl.getInventory().getItemByItemId(RING_OF_RAVEN_ID) != null)
				htmltext = "06.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(RING_OF_RAVEN_ID) == null)
				htmltext = "07.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(RING_OF_RAVEN_ID) != null)
			{
				pl.getInventory().destroyItemByItemId(RING_OF_RAVEN_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "08.htm";
			}
		}
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30498/" + htmltext, new Object[0]);
	}

	public void onTalk30499()
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final ClassId classId = pl.getClassId();
		String htmltext;
		if(classId == ClassId.DwarvenFighter)
			htmltext = "01.htm";
		else if(classId == ClassId.Scavenger || classId == ClassId.Artisan)
			htmltext = "09.htm";
		else if(pl.getRace() == Race.dwarf)
			htmltext = "10.htm";
		else
			htmltext = "11.htm";
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30499/" + htmltext, new Object[0]);
	}

	public void onChange30499(final String[] args)
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final short PASS_FINAL_ID = 1635;
		final short event = Short.parseShort(args[0]);
		final int Level = pl.getLevel();
		final ClassId classId = pl.getClassId();
		String htmltext = "No Quest";
		if(event == 56 && classId == ClassId.DwarvenFighter)
		{
			if(Level <= 19 && pl.getInventory().getItemByItemId(PASS_FINAL_ID) == null)
				htmltext = "05.htm";
			if(Level <= 19 && pl.getInventory().getItemByItemId(PASS_FINAL_ID) != null)
				htmltext = "06.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(PASS_FINAL_ID) == null)
				htmltext = "07.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(PASS_FINAL_ID) != null)
			{
				pl.getInventory().destroyItemByItemId(PASS_FINAL_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "08.htm";
			}
		}
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30499/" + htmltext, new Object[0]);
	}

	public void onTalk30525()
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final ClassId classId = pl.getClassId();
		String htmltext;
		if(classId == ClassId.DwarvenFighter)
			htmltext = "01.htm";
		else if(classId == ClassId.Artisan)
			htmltext = "05.htm";
		else if(classId == ClassId.Warsmith)
			htmltext = "06.htm";
		else
			htmltext = "07.htm";
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30525/" + htmltext, new Object[0]);
	}

	public void onTalk30520()
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final ClassId classId = pl.getClassId();
		String htmltext;
		if(classId == ClassId.DwarvenFighter)
			htmltext = "01.htm";
		else if(classId == ClassId.Artisan || classId == ClassId.Scavenger)
			htmltext = "05.htm";
		else if(classId == ClassId.Warsmith || classId == ClassId.BountyHunter)
			htmltext = "06.htm";
		else
			htmltext = "07.htm";
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30520/" + htmltext, new Object[0]);
	}

	public void onTalk30512()
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final ClassId classId = pl.getClassId();
		String htmltext;
		if(classId == ClassId.Artisan)
			htmltext = "01.htm";
		else if(classId == ClassId.DwarvenFighter)
			htmltext = "09.htm";
		else if(classId == ClassId.Warsmith || classId == ClassId.BountyHunter)
			htmltext = "10.htm";
		else
			htmltext = "11.htm";
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30512/" + htmltext, new Object[0]);
	}

	public void onChange30512(final String[] args)
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final short MARK_OF_MAESTRO_ID = 2867;
		final short MARK_OF_GUILDSMAN_ID = 3119;
		final short MARK_OF_PROSPERITY_ID = 3238;
		final short event = Short.parseShort(args[0]);
		final int Level = pl.getLevel();
		final ClassId classId = pl.getClassId();
		String htmltext = "No Quest";
		if(event == 57 && classId == ClassId.Artisan)
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_MAESTRO_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_GUILDSMAN_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_PROSPERITY_ID) == null)
					htmltext = "05.htm";
				else
					htmltext = "06.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_MAESTRO_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_GUILDSMAN_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_PROSPERITY_ID) == null)
				htmltext = "07.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_GUILDSMAN_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_MAESTRO_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_PROSPERITY_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "08.htm";
			}
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30512/" + htmltext, new Object[0]);
	}

	public void onTalk30565()
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final ClassId classId = pl.getClassId();
		String htmltext;
		if(classId == ClassId.OrcFighter)
			htmltext = "01.htm";
		else if(classId == ClassId.OrcRaider || classId == ClassId.OrcMonk || classId == ClassId.OrcShaman)
			htmltext = "09.htm";
		else if(classId == ClassId.OrcMage)
			htmltext = "16.htm";
		else if(pl.getRace() == Race.orc)
			htmltext = "10.htm";
		else
			htmltext = "11.htm";
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30565/" + htmltext, new Object[0]);
	}

	public void onTalk30109()
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final ClassId classId = pl.getClassId();
		String htmltext;
		if(classId == ClassId.ElvenKnight)
			htmltext = "01.htm";
		else if(classId == ClassId.Knight)
			htmltext = "08.htm";
		else if(classId == ClassId.Rogue)
			htmltext = "15.htm";
		else if(classId == ClassId.ElvenScout)
			htmltext = "22.htm";
		else if(classId == ClassId.Warrior)
			htmltext = "29.htm";
		else if(classId == ClassId.ElvenFighter || classId == ClassId.Fighter)
			htmltext = "76.htm";
		else if(classId == ClassId.TempleKnight || classId == ClassId.PlainsWalker || classId == ClassId.SwordSinger || classId == ClassId.SilverRanger)
			htmltext = "77.htm";
		else if(classId == ClassId.Warlord || classId == ClassId.Paladin || classId == ClassId.TreasureHunter)
			htmltext = "77.htm";
		else if(classId == ClassId.Gladiator || classId == ClassId.DarkAvenger || classId == ClassId.Hawkeye)
			htmltext = "77.htm";
		else
			htmltext = "78.htm";
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30109/" + htmltext, new Object[0]);
	}

	public void onChange30109(final String[] args)
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final short MARK_OF_CHALLENGER_ID = 2627;
		final short MARK_OF_DUTY_ID = 2633;
		final short MARK_OF_SEEKER_ID = 2673;
		final short MARK_OF_TRUST_ID = 2734;
		final short MARK_OF_DUELIST_ID = 2762;
		final short MARK_OF_SEARCHER_ID = 2809;
		final short MARK_OF_HEALER_ID = 2820;
		final short MARK_OF_LIFE_ID = 3140;
		final short MARK_OF_CHAMPION_ID = 3276;
		final short MARK_OF_SAGITTARIUS_ID = 3293;
		final short MARK_OF_WITCHCRAFT_ID = 3307;
		final short event = Short.parseShort(args[0]);
		final int Level = pl.getLevel();
		final ClassId classId = pl.getClassId();
		String htmltext = "No Quest";
		if(event == 20 && classId == ClassId.ElvenKnight)
		{
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_DUTY_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_LIFE_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_HEALER_ID) == null)
					htmltext = "36.htm";
				else
					htmltext = "37.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_DUTY_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_LIFE_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_HEALER_ID) == null)
				htmltext = "38.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_DUTY_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_LIFE_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_HEALER_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "39.htm";
			}
		}
		else if(event == 21 && classId == ClassId.ElvenKnight)
		{
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_CHALLENGER_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_LIFE_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_DUELIST_ID) == null)
					htmltext = "40.htm";
				else
					htmltext = "41.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_CHALLENGER_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_LIFE_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_DUELIST_ID) == null)
				htmltext = "42.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_CHALLENGER_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_LIFE_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_DUELIST_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "43.htm";
			}
		}
		else if(event == 5 && classId == ClassId.Knight)
		{
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_DUTY_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_TRUST_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_HEALER_ID) == null)
					htmltext = "44.htm";
				else
					htmltext = "45.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_DUTY_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_TRUST_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_HEALER_ID) == null)
				htmltext = "46.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_DUTY_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_TRUST_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_HEALER_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "47.htm";
			}
		}
		else if(event == 6 && classId == ClassId.Knight)
		{
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_DUTY_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_TRUST_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_WITCHCRAFT_ID) == null)
					htmltext = "48.htm";
				else
					htmltext = "49.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_DUTY_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_TRUST_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_WITCHCRAFT_ID) == null)
				htmltext = "50.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_DUTY_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_TRUST_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_WITCHCRAFT_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "51.htm";
			}
		}
		else if(event == 8 && classId == ClassId.Rogue)
		{
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_SEEKER_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_TRUST_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_SEARCHER_ID) == null)
					htmltext = "52.htm";
				else
					htmltext = "53.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_SEEKER_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_TRUST_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_SEARCHER_ID) == null)
				htmltext = "54.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_SEEKER_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_TRUST_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_SEARCHER_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "55.htm";
			}
		}
		else if(event == 9 && classId == ClassId.Rogue)
		{
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_SEEKER_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_TRUST_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_SAGITTARIUS_ID) == null)
					htmltext = "56.htm";
				else
					htmltext = "57.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_SEEKER_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_TRUST_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_SAGITTARIUS_ID) == null)
				htmltext = "58.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_SEEKER_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_TRUST_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_SAGITTARIUS_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "59.htm";
			}
		}
		else if(event == 23 && classId == ClassId.ElvenScout)
		{
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_SEEKER_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_LIFE_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_SEARCHER_ID) == null)
					htmltext = "60.htm";
				else
					htmltext = "61.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_SEEKER_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_LIFE_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_SEARCHER_ID) == null)
				htmltext = "62.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_SEEKER_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_LIFE_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_SEARCHER_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "63.htm";
			}
		}
		else if(event == 24 && classId == ClassId.ElvenScout)
		{
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_SEEKER_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_LIFE_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_SAGITTARIUS_ID) == null)
					htmltext = "64.htm";
				else
					htmltext = "65.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_SEEKER_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_LIFE_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_SAGITTARIUS_ID) == null)
				htmltext = "66.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_SEEKER_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_LIFE_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_SAGITTARIUS_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "67.htm";
			}
		}
		else if(event == 2 && classId == ClassId.Warrior)
		{
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_CHALLENGER_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_TRUST_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_DUELIST_ID) == null)
					htmltext = "68.htm";
				else
					htmltext = "69.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_CHALLENGER_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_TRUST_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_DUELIST_ID) == null)
				htmltext = "70.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_CHALLENGER_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_TRUST_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_DUELIST_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "71.htm";
			}
		}
		else if(event == 3 && classId == ClassId.Warrior)
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_CHALLENGER_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_TRUST_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_CHAMPION_ID) == null)
					htmltext = "72.htm";
				else
					htmltext = "73.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_CHALLENGER_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_TRUST_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_CHAMPION_ID) == null)
				htmltext = "74.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_CHALLENGER_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_TRUST_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_CHAMPION_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "75.htm";
			}
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30109/" + htmltext, new Object[0]);
	}

	public void onTalk30115()
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final ClassId classId = pl.getClassId();
		String htmltext;
		if(classId == ClassId.ElvenWizard)
			htmltext = "01.htm";
		else if(classId == ClassId.Wizard)
			htmltext = "08.htm";
		else if(classId == ClassId.Sorceror || classId == ClassId.Necromancer || classId == ClassId.Warlock)
			htmltext = "39.htm";
		else if(classId == ClassId.Spellsinger || classId == ClassId.ElementalSummoner)
			htmltext = "39.htm";
		else if((pl.getRace() == Race.elf || pl.getRace() == Race.human) && classId.getType().isMagician())
			htmltext = "38.htm";
		else
			htmltext = "40.htm";
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30115/" + htmltext, new Object[0]);
	}

	public void onChange30115(final String[] args)
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final short MARK_OF_SCHOLAR_ID = 2674;
		final short MARK_OF_TRUST_ID = 2734;
		final short MARK_OF_MAGUS_ID = 2840;
		final short MARK_OF_LIFE_ID = 3140;
		final short MARK_OF_WITCHCRFAT_ID = 3307;
		final short MARK_OF_SUMMONER_ID = 3336;
		final short event = Short.parseShort(args[0]);
		final int Level = pl.getLevel();
		final ClassId classId = pl.getClassId();
		String htmltext = "No Quest";
		if(event == 27 && classId == ClassId.ElvenWizard)
		{
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_SCHOLAR_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_LIFE_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_MAGUS_ID) == null)
					htmltext = "18.htm";
				else
					htmltext = "19.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_SCHOLAR_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_LIFE_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_MAGUS_ID) == null)
				htmltext = "20.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_SCHOLAR_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_LIFE_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_MAGUS_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "21.htm";
			}
		}
		else if(event == 28 && classId == ClassId.ElvenWizard)
		{
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_SCHOLAR_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_LIFE_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_SUMMONER_ID) == null)
					htmltext = "22.htm";
				else
					htmltext = "23.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_SCHOLAR_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_LIFE_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_SUMMONER_ID) == null)
				htmltext = "24.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_SCHOLAR_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_LIFE_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_SUMMONER_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "25.htm";
			}
		}
		else if(event == 12 && classId == ClassId.Wizard)
		{
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_SCHOLAR_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_TRUST_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_MAGUS_ID) == null)
					htmltext = "26.htm";
				else
					htmltext = "27.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_SCHOLAR_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_TRUST_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_MAGUS_ID) == null)
				htmltext = "28.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_SCHOLAR_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_TRUST_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_MAGUS_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "29.htm";
			}
		}
		else if(event == 13 && classId == ClassId.Wizard)
		{
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_SCHOLAR_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_TRUST_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_WITCHCRFAT_ID) == null)
					htmltext = "30.htm";
				else
					htmltext = "31.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_SCHOLAR_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_TRUST_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_WITCHCRFAT_ID) == null)
				htmltext = "32.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_SCHOLAR_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_TRUST_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_WITCHCRFAT_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "33.htm";
			}
		}
		else if(event == 14 && classId == ClassId.Wizard)
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_SCHOLAR_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_TRUST_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_SUMMONER_ID) == null)
					htmltext = "34.htm";
				else
					htmltext = "35.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_SCHOLAR_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_TRUST_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_SUMMONER_ID) == null)
				htmltext = "36.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_SCHOLAR_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_TRUST_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_SUMMONER_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "37.htm";
			}
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30115/" + htmltext, new Object[0]);
	}

	public void onTalk30120()
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final ClassId classId = pl.getClassId();
		String htmltext;
		if(classId == ClassId.Oracle)
			htmltext = "01.htm";
		else if(classId == ClassId.Cleric)
			htmltext = "05.htm";
		else if(classId == ClassId.Elder || classId == ClassId.Bishop || classId == ClassId.Prophet)
			htmltext = "25.htm";
		else if((pl.getRace() == Race.human || pl.getRace() == Race.elf) && classId.getType().isMagician())
			htmltext = "24.htm";
		else
			htmltext = "26.htm";
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30120/" + htmltext, new Object[0]);
	}

	public void onChange30120(final String[] args)
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final short MARK_OF_PILGRIM_ID = 2721;
		final short MARK_OF_TRUST_ID = 2734;
		final short MARK_OF_HEALER_ID = 2820;
		final short MARK_OF_REFORMER_ID = 2821;
		final short MARK_OF_LIFE_ID = 3140;
		final short event = Short.parseShort(args[0]);
		final int Level = pl.getLevel();
		final ClassId classId = pl.getClassId();
		String htmltext = "No Quest";
		if(event == 30 || classId == ClassId.Oracle)
		{
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_PILGRIM_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_LIFE_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_HEALER_ID) == null)
					htmltext = "12.htm";
				else
					htmltext = "13.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_PILGRIM_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_LIFE_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_HEALER_ID) == null)
				htmltext = "14.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_PILGRIM_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_LIFE_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_HEALER_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "15.htm";
			}
		}
		else if(event == 16 && classId == ClassId.Cleric)
		{
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_PILGRIM_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_TRUST_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_HEALER_ID) == null)
					htmltext = "16.htm";
				else
					htmltext = "17.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_PILGRIM_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_TRUST_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_HEALER_ID) == null)
				htmltext = "18.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_PILGRIM_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_TRUST_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_HEALER_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "19.htm";
			}
		}
		else if(event == 17 && classId == ClassId.Cleric)
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_PILGRIM_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_TRUST_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_REFORMER_ID) == null)
					htmltext = "20.htm";
				else
					htmltext = "21.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_PILGRIM_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_TRUST_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_REFORMER_ID) == null)
				htmltext = "22.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_PILGRIM_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_TRUST_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_REFORMER_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "23.htm";
			}
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30120/" + htmltext, new Object[0]);
	}

	public void onTalk30500()
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final ClassId classId = pl.getClassId();
		String htmltext;
		if(classId == ClassId.OrcFighter)
			htmltext = "01.htm";
		else if(classId == ClassId.OrcMage)
			htmltext = "06.htm";
		else if(classId == ClassId.OrcRaider || classId == ClassId.OrcMonk || classId == ClassId.OrcShaman)
			htmltext = "21.htm";
		else if(classId == ClassId.Destroyer || classId == ClassId.Tyrant || classId == ClassId.Overlord || classId == ClassId.Warcryer)
			htmltext = "22.htm";
		else
			htmltext = "23.htm";
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30500/" + htmltext, new Object[0]);
	}

	public void onChange30500(final String[] args)
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final short MARK_OF_RAIDER_ID = 1592;
		final short KHAVATARI_TOTEM_ID = 1615;
		final short MASK_OF_MEDIUM_ID = 1631;
		final short event = Short.parseShort(args[0]);
		final int Level = pl.getLevel();
		final ClassId classId = pl.getClassId();
		String htmltext = "No Quest";
		if(event == 45 && classId == ClassId.OrcFighter)
		{
			if(Level <= 19 && pl.getInventory().getItemByItemId(MARK_OF_RAIDER_ID) == null)
				htmltext = "09.htm";
			if(Level <= 19 && pl.getInventory().getItemByItemId(MARK_OF_RAIDER_ID) != null)
				htmltext = "10.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(MARK_OF_RAIDER_ID) == null)
				htmltext = "11.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(MARK_OF_RAIDER_ID) != null)
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_RAIDER_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "12.htm";
			}
		}
		else if(event == 47 && classId == ClassId.OrcFighter)
		{
			if(Level <= 19 && pl.getInventory().getItemByItemId(KHAVATARI_TOTEM_ID) == null)
				htmltext = "13.htm";
			if(Level <= 19 && pl.getInventory().getItemByItemId(KHAVATARI_TOTEM_ID) != null)
				htmltext = "14.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(KHAVATARI_TOTEM_ID) == null)
				htmltext = "15.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(KHAVATARI_TOTEM_ID) != null)
			{
				pl.getInventory().destroyItemByItemId(KHAVATARI_TOTEM_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "16.htm";
			}
		}
		else if(event == 50 && classId == ClassId.OrcMage)
		{
			if(Level <= 19 && pl.getInventory().getItemByItemId(MASK_OF_MEDIUM_ID) == null)
				htmltext = "17.htm";
			if(Level <= 19 && pl.getInventory().getItemByItemId(MASK_OF_MEDIUM_ID) != null)
				htmltext = "18.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(MASK_OF_MEDIUM_ID) == null)
				htmltext = "19.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(MASK_OF_MEDIUM_ID) != null)
			{
				pl.getInventory().destroyItemByItemId(MASK_OF_MEDIUM_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "20.htm";
			}
		}
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30500/" + htmltext, new Object[0]);
	}

	public void onTalk30290()
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final ClassId classId = pl.getClassId();
		String htmltext;
		if(classId == ClassId.DarkFighter)
			htmltext = "01.htm";
		else if(classId == ClassId.DarkMage)
			htmltext = "08.htm";
		else if(classId == ClassId.PalusKnight || classId == ClassId.Assassin || classId == ClassId.DarkWizard || classId == ClassId.ShillienOracle)
			htmltext = "31.htm";
		else if(pl.getRace() == Race.darkelf)
			htmltext = "32.htm";
		else
			htmltext = "33.htm";
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30290/" + htmltext, new Object[0]);
	}

	public void onChange30290(final String[] args)
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final short GAZE_OF_ABYSS_ID = 1244;
		final short IRON_HEART_ID = 1252;
		final short JEWEL_OF_DARKNESS_ID = 1261;
		final short ORB_OF_ABYSS_ID = 1270;
		final short event = Short.parseShort(args[0]);
		final int Level = pl.getLevel();
		final ClassId classId = pl.getClassId();
		String htmltext = "No Quest";
		if(event == 32 && classId == ClassId.DarkFighter)
		{
			if(Level <= 19 && pl.getInventory().getItemByItemId(GAZE_OF_ABYSS_ID) == null)
				htmltext = "15.htm";
			if(Level <= 19 && pl.getInventory().getItemByItemId(GAZE_OF_ABYSS_ID) != null)
				htmltext = "16.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(GAZE_OF_ABYSS_ID) == null)
				htmltext = "17.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(GAZE_OF_ABYSS_ID) != null)
			{
				pl.getInventory().destroyItemByItemId(GAZE_OF_ABYSS_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "18.htm";
			}
		}
		else if(event == 35 && classId == ClassId.DarkFighter)
		{
			if(Level <= 19 && pl.getInventory().getItemByItemId(IRON_HEART_ID) == null)
				htmltext = "19.htm";
			if(Level <= 19 && pl.getInventory().getItemByItemId(IRON_HEART_ID) != null)
				htmltext = "20.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(IRON_HEART_ID) == null)
				htmltext = "21.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(IRON_HEART_ID) != null)
			{
				pl.getInventory().destroyItemByItemId(IRON_HEART_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "22.htm";
			}
		}
		else if(event == 39 && classId == ClassId.DarkMage)
		{
			if(Level <= 19 && pl.getInventory().getItemByItemId(JEWEL_OF_DARKNESS_ID) == null)
				htmltext = "23.htm";
			if(Level <= 19 && pl.getInventory().getItemByItemId(JEWEL_OF_DARKNESS_ID) != null)
				htmltext = "24.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(JEWEL_OF_DARKNESS_ID) == null)
				htmltext = "25.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(JEWEL_OF_DARKNESS_ID) != null)
			{
				pl.getInventory().destroyItemByItemId(JEWEL_OF_DARKNESS_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "26.htm";
			}
		}
		else if(event == 42 && classId == ClassId.DarkMage)
		{
			if(Level <= 19 && pl.getInventory().getItemByItemId(ORB_OF_ABYSS_ID) == null)
				htmltext = "27.htm";
			if(Level <= 19 && pl.getInventory().getItemByItemId(ORB_OF_ABYSS_ID) != null)
				htmltext = "28.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(ORB_OF_ABYSS_ID) == null)
				htmltext = "29.htm";
			if(Level >= 20 && pl.getInventory().getItemByItemId(ORB_OF_ABYSS_ID) != null)
			{
				pl.getInventory().destroyItemByItemId(ORB_OF_ABYSS_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "30.htm";
			}
		}
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30290/" + htmltext, new Object[0]);
	}

	public void onTalk30513()
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final ClassId classId = pl.getClassId();
		String htmltext;
		if(classId == ClassId.OrcMonk)
			htmltext = "01.htm";
		else if(classId == ClassId.OrcRaider)
			htmltext = "05.htm";
		else if(classId == ClassId.OrcShaman)
			htmltext = "09.htm";
		else if(classId == ClassId.Destroyer || classId == ClassId.Tyrant || classId == ClassId.Overlord || classId == ClassId.Warcryer)
			htmltext = "32.htm";
		else if(classId == ClassId.OrcFighter || classId == ClassId.OrcMage)
			htmltext = "33.htm";
		else
			htmltext = "34.htm";
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30513/" + htmltext, new Object[0]);
	}

	public void onChange30513(final String[] args)
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final short MARK_OF_CHALLENGER_ID = 2627;
		final short MARK_OF_PILGRIM_ID = 2721;
		final short MARK_OF_DUELIST_ID = 2762;
		final short MARK_OF_WARSPIRIT_ID = 2879;
		final short MARK_OF_GLORY_ID = 3203;
		final short MARK_OF_CHAMPION_ID = 3276;
		final short MARK_OF_LORD_ID = 3390;
		final short event = Short.parseShort(args[0]);
		final int Level = pl.getLevel();
		final ClassId classId = pl.getClassId();
		String htmltext = "No Quest";
		if(event == 48 && classId == ClassId.OrcMonk)
		{
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_CHALLENGER_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_GLORY_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_DUELIST_ID) == null)
					htmltext = "16.htm";
				else
					htmltext = "17.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_CHALLENGER_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_GLORY_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_DUELIST_ID) == null)
				htmltext = "18.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_CHALLENGER_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_GLORY_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_DUELIST_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "19.htm";
			}
		}
		else if(event == 46 && classId == ClassId.OrcRaider)
		{
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_CHALLENGER_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_GLORY_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_CHAMPION_ID) == null)
					htmltext = "20.htm";
				else
					htmltext = "21.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_CHALLENGER_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_GLORY_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_CHAMPION_ID) == null)
				htmltext = "22.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_CHALLENGER_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_GLORY_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_CHAMPION_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "23.htm";
			}
		}
		else if(event == 51 && classId == ClassId.OrcShaman)
		{
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_PILGRIM_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_GLORY_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_LORD_ID) == null)
					htmltext = "24.htm";
				else
					htmltext = "25.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_PILGRIM_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_GLORY_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_LORD_ID) == null)
				htmltext = "26.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_PILGRIM_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_GLORY_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_LORD_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "27.htm";
			}
		}
		else if(event == 52 && classId == ClassId.OrcShaman)
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_PILGRIM_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_GLORY_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_WARSPIRIT_ID) == null)
					htmltext = "28.htm";
				else
					htmltext = "29.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_PILGRIM_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_GLORY_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_WARSPIRIT_ID) == null)
				htmltext = "30.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_PILGRIM_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_GLORY_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_WARSPIRIT_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "31.htm";
			}
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30513/" + htmltext, new Object[0]);
	}

	public void onTalk30474()
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final ClassId classId = pl.getClassId();
		String htmltext;
		if(npc.getNpcId() == 30175)
		{
			if(classId == ClassId.ShillienOracle)
				htmltext = "08.htm";
			else if(classId == ClassId.DarkWizard)
				htmltext = "19.htm";
			else if(classId == ClassId.Spellhowler || classId == ClassId.ShillienElder || classId == ClassId.PhantomSummoner)
				htmltext = "54.htm";
			else if(classId == ClassId.DarkMage)
				htmltext = "55.htm";
			else
				htmltext = "56.htm";
		}
		else if(classId == ClassId.PalusKnight)
			htmltext = "01.htm";
		else if(classId == ClassId.ShillienOracle)
			htmltext = "08.htm";
		else if(classId == ClassId.Assassin)
			htmltext = "12.htm";
		else if(classId == ClassId.DarkWizard)
			htmltext = "19.htm";
		else if(classId == ClassId.ShillienKnight || classId == ClassId.AbyssWalker || classId == ClassId.Bladedancer || classId == ClassId.PhantomRanger)
			htmltext = "54.htm";
		else if(classId == ClassId.Spellhowler || classId == ClassId.ShillienElder || classId == ClassId.PhantomSummoner)
			htmltext = "54.htm";
		else if(classId == ClassId.DarkFighter || classId == ClassId.DarkMage)
			htmltext = "55.htm";
		else
			htmltext = "56.htm";
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30474/" + htmltext, new Object[0]);
	}

	public void onChange30474(final String[] args)
	{
		final Player pl = getSelf();
		final NpcInstance npc = getNpc();
		if(pl == null || npc == null)
			return;
		if(!(npc instanceof VillageMasterInstance))
		{
			show("I have nothing to say you", pl);
			return;
		}
		final short MARK_OF_CHALLENGER_ID = 2627;
		final short MARK_OF_DUTY_ID = 2633;
		final short MARK_OF_SEEKER_ID = 2673;
		final short MARK_OF_SCHOLAR_ID = 2674;
		final short MARK_OF_PILGRIM_ID = 2721;
		final short MARK_OF_DUELIST_ID = 2762;
		final short MARK_OF_SEARCHER_ID = 2809;
		final short MARK_OF_REFORMER_ID = 2821;
		final short MARK_OF_MAGUS_ID = 2840;
		final short MARK_OF_FATE_ID = 3172;
		final short MARK_OF_SAGITTARIUS_ID = 3293;
		final short MARK_OF_WITCHCRAFT_ID = 3307;
		final short MARK_OF_SUMMONER_ID = 3336;
		final short event = Short.parseShort(args[0]);
		final int Level = pl.getLevel();
		final ClassId classId = pl.getClassId();
		String htmltext = "No Quest";
		if(event == 33 && classId == ClassId.PalusKnight)
		{
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_DUTY_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_FATE_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_WITCHCRAFT_ID) == null)
					htmltext = "26.htm";
				else
					htmltext = "27.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_DUTY_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_FATE_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_WITCHCRAFT_ID) == null)
				htmltext = "28.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_DUTY_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_FATE_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_WITCHCRAFT_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "29.htm";
			}
		}
		else if(event == 34 && classId == ClassId.PalusKnight)
		{
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_CHALLENGER_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_FATE_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_DUELIST_ID) == null)
					htmltext = "30.htm";
				else
					htmltext = "31.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_CHALLENGER_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_FATE_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_DUELIST_ID) == null)
				htmltext = "32.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_CHALLENGER_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_FATE_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_DUELIST_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "33.htm";
			}
		}
		else if(event == 43 && classId == ClassId.ShillienOracle)
		{
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_PILGRIM_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_FATE_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_REFORMER_ID) == null)
					htmltext = "34.htm";
				else
					htmltext = "35.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_PILGRIM_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_FATE_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_REFORMER_ID) == null)
				htmltext = "36.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_PILGRIM_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_FATE_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_REFORMER_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "37.htm";
			}
		}
		else if(event == 36 && classId == ClassId.Assassin)
		{
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_SEEKER_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_FATE_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_SEARCHER_ID) == null)
					htmltext = "38.htm";
				else
					htmltext = "39.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_SEEKER_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_FATE_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_SEARCHER_ID) == null)
				htmltext = "40.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_SEEKER_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_FATE_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_SEARCHER_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "41.htm";
			}
		}
		else if(event == 37 && classId == ClassId.Assassin)
		{
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_SEEKER_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_FATE_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_SAGITTARIUS_ID) == null)
					htmltext = "42.htm";
				else
					htmltext = "43.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_SEEKER_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_FATE_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_SAGITTARIUS_ID) == null)
				htmltext = "44.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_SEEKER_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_FATE_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_SAGITTARIUS_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "45.htm";
			}
		}
		else if(event == 40 && classId == ClassId.DarkWizard)
		{
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_SCHOLAR_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_FATE_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_MAGUS_ID) == null)
					htmltext = "46.htm";
				else
					htmltext = "47.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_SCHOLAR_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_FATE_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_MAGUS_ID) == null)
				htmltext = "48.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_SCHOLAR_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_FATE_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_MAGUS_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "49.htm";
			}
		}
		else if(event == 41 && classId == ClassId.DarkWizard)
			if(Level <= 39)
			{
				if(pl.getInventory().getItemByItemId(MARK_OF_SCHOLAR_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_FATE_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_SUMMONER_ID) == null)
					htmltext = "50.htm";
				else
					htmltext = "51.htm";
			}
			else if(pl.getInventory().getItemByItemId(MARK_OF_SCHOLAR_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_FATE_ID) == null || pl.getInventory().getItemByItemId(MARK_OF_SUMMONER_ID) == null)
				htmltext = "52.htm";
			else
			{
				pl.getInventory().destroyItemByItemId(MARK_OF_SCHOLAR_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_FATE_ID, 1L, false);
				pl.getInventory().destroyItemByItemId(MARK_OF_SUMMONER_ID, 1L, false);
				pl.setClassId(event, false, true);
				htmltext = "53.htm";
			}
		((VillageMasterInstance) npc).showChatWindow(pl, "villagemaster/30474/" + htmltext, new Object[0]);
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}
}
