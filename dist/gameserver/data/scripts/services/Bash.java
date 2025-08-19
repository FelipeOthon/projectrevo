package services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import l2s.gameserver.Config;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.handler.AdminCommandHandler;
import l2s.gameserver.handler.IAdminCommandHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.scripts.ScriptFile;
import l2s.gameserver.utils.Files;

public class Bash extends Functions implements IAdminCommandHandler, ScriptFile
{
	private static final Logger _log;
	private static String[] _adminCommands;
	private static String wrongPage;
	private static String notPage;
	private static String readPage;
	private static String xmlData;
	private static List<String> quotes;
	private static ScheduledFuture<?> _scheduledReload;

	@Override
	public boolean useAdminCommand(final String command, final Player activeChar)
	{
		if(!activeChar.getPlayerAccess().IsEventGm)
			return false;
		if(command.startsWith("admin_bashreload"))
		{
			loadData();
			activeChar.sendMessage("Bash service reloaded.");
		}
		return true;
	}

	public void showQuote(final String[] var)
	{
		final Player player = getSelf();
		if(player == null)
			return;
		final NpcInstance lastNpc = player.getLastNpc();
		if(!NpcInstance.canBypassCheck(player, lastNpc))
			return;
		int page = 1;
		final int totalPages = Bash.quotes.size();
		try
		{
			page = Integer.parseInt(var[0]);
		}
		catch(NumberFormatException e)
		{
			show(HtmCache.getInstance().getHtml(Bash.wrongPage, player) + navBar(1, totalPages), player, lastNpc);
			return;
		}
		if(page > totalPages && page == 1)
		{
			show(HtmCache.getInstance().getHtml(Bash.notPage, player), player, lastNpc);
			return;
		}
		if(page > totalPages || page < 1)
		{
			show(HtmCache.getInstance().getHtml(Bash.wrongPage, player) + navBar(1, totalPages), player, lastNpc);
			return;
		}
		String html = HtmCache.getInstance().getHtml(Bash.readPage, player);
		html = html.replaceFirst("%quote%", Bash.quotes.get(page - 1));
		html = html.replaceFirst("%page%", String.valueOf(page));
		html = html.replaceFirst("%total_pages%", String.valueOf(totalPages));
		html += navBar(page, totalPages);
		show(html, player, lastNpc);
	}

	private int parseRSS()
	{
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setIgnoringComments(true);
		Document doc = null;
		try
		{
			doc = factory.newDocumentBuilder().parse(new File(Bash.xmlData));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(doc == null)
			return 0;
		Bash.quotes.clear();
		int quotesCounter = 0;
		for(Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
			if("rss".equalsIgnoreCase(n.getNodeName()))
				for(Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
					if("channel".equalsIgnoreCase(d.getNodeName()))
						for(Node i = d.getFirstChild(); i != null; i = i.getNextSibling())
							if("item".equalsIgnoreCase(i.getNodeName()))
								for(Node z = i.getFirstChild(); z != null; z = z.getNextSibling())
									if("description".equalsIgnoreCase(z.getNodeName()))
									{
										Bash.quotes.add(z.getTextContent().replaceAll("\\\\", "").replaceAll("\\$", ""));
										++quotesCounter;
									}
		return quotesCounter;
	}

	public String getPage(final String url_server, final String url_document)
	{
		final StringBuilder buf = new StringBuilder();
		try
		{
			Socket s;
			try
			{
				s = new Socket(url_server, 80);
			}
			catch(Exception e2)
			{
				return null;
			}
			s.setSoTimeout(30000);
			final BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream(), "Cp1251"));
			final PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "UTF-8"));
			out.print("GET http://" + url_server + "/" + url_document + " HTTP/1.1\r\nUser-Agent: MMoCore\r\nHost: " + url_server + "\r\nAccept: */*\r\nConnection: close\r\n\r\n");
			out.flush();
			boolean header = true;
			for(String line = in.readLine(); line != null; line = in.readLine())
			{
				if(header && line.startsWith("<?xml "))
					header = false;
				if(!header)
					buf.append(line).append("\r\n");
				if(!header && line.startsWith("</rss>"))
					break;
			}
			s.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return buf.toString();
	}

	private String navBar(final int curPage, final int totalPages)
	{
		String html = "<br><center><table border=0 width=240><tr><td widht=30>";
		if(curPage > 1)
			html = html + "<a action=\"bypass -h scripts_services.Bash:showQuote " + (curPage - 1) + "\">";
		html += "&lt;&lt;&lt; \u041d\u0430\u0437\u0430\u0434";
		if(curPage > 1)
			html += "</a>";
		html = html + "</td><td widht=160>&nbsp;[" + curPage + "]&nbsp;</td><td widht=40>";
		if(curPage < totalPages)
			html = html + "<a action=\"bypass -h scripts_services.Bash:showQuote " + (curPage + 1) + "\">";
		html += "\u0412\u043f\u0435\u0440\u0435\u0434 &gt;&gt;&gt;";
		if(curPage < totalPages)
			html += "</a>";
		html += "</td></tr></table></center>";
		html += "<table border=0 width=240><tr><td width=150>";
		html += "\u041f\u0435\u0440\u0435\u0439\u0442\u0438 \u043d\u0430 \u0441\u0442\u0440\u0430\u043d\u0438\u0446\u0443:</td><td><edit var=\"page\" width=40 height=12></td><td>";
		html += "<button value=\"\u043f\u0435\u0440\u0435\u0439\u0442\u0438\" action=\"bypass -h scripts_services.Bash:showQuote $page\" width=60 height=18 back=\"sek.cbui94\" fore=\"sek.cbui92\">";
		html += "</td></tr></table>";
		return html;
	}

	public static String DialogAppend_30086(final Integer val)
	{
		if(val != 0 || !Config.SERVICES_BASH_ENABLED)
			return "";
		return "<br><a action=\"bypass -h scripts_services.Bash:showQuote 1\">\u041f\u043e\u0447\u0438\u0442\u0430\u0442\u044c \u0411\u0430\u0448 \u043e\u0440\u0433</a>";
	}

	public void loadData()
	{
		if(Config.SERVICES_BASH_RELOAD_TIME > 0)
		{
			if(Bash._scheduledReload != null)
				Bash._scheduledReload.cancel(false);
			Bash._scheduledReload = ThreadPoolManager.getInstance().schedule(() -> Bash.this.loadData(), Config.SERVICES_BASH_RELOAD_TIME * 60 * 60 * 1000L);
		}
		String data;
		try
		{
			data = getPage("bash.im", "rss/");
		}
		catch(Exception E)
		{
			data = null;
		}
		if(data == null)
		{
			Bash._log.info("Service: Bash - RSS data download failed.");
			return;
		}
		data = data.replaceFirst("windows-1251", "utf-8");
		if(!Config.SERVICES_BASH_SKIP_DOWNLOAD)
		{
			Files.writeFile(Bash.xmlData, data);
			Bash._log.info("Service: Bash - RSS data download completed.");
		}
		final int parse = parseRSS();
		if(parse == 0)
		{
			Bash._log.warn("Service: Bash - RSS data parse error.");
			return;
		}
		Bash._log.info("Service: Bash - RSS data parsed: loaded " + parse + " quotes.");
	}

	@Override
	public void onLoad()
	{
		Bash._log.info("Loaded Service: Bash [" + (Config.SERVICES_BASH_ENABLED ? "enabled]" : "disabled]"));
		if(Config.SERVICES_BASH_ENABLED)
		{
			AdminCommandHandler.getInstance().registerAdminCommandHandler(this);
			loadData();
		}
	}

	@Override
	public void onReload()
	{}

	@Override
	public void onShutdown()
	{}

	@Override
	public String[] getAdminCommandList()
	{
		return Bash._adminCommands;
	}

	static
	{
		_log = LoggerFactory.getLogger(Bash.class);
		Bash._adminCommands = new String[] { "admin_bashreload" };
		Bash.wrongPage = "scripts/services/Bash-wrongPage.htm";
		Bash.notPage = "scripts/services/Bash-notPage.htm";
		Bash.readPage = "scripts/services/Bash-readPage.htm";
		Bash.xmlData = Config.DATAPACK_ROOT + "/data/bash.xml";
		Bash.quotes = new ArrayList<String>();
	}
}
