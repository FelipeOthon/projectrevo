import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * @author Bonux
**/
public class UTFToString
{
	private static final Pattern UTF_CHAR_PATTERN = Pattern.compile("(\\\\u([0-9A-Fa-f]{4}))");

    public static void main(String[] args)
	{
		while(true)
		{
			Scanner sc = new Scanner(System.in);
			System.out.print("Enter coded text: ");
			String codedText = sc.nextLine();
			String result = codedText;
			Matcher matcher = UTF_CHAR_PATTERN.matcher(codedText);
			while(matcher.find())
			{
				result = result.replace(matcher.group(1), String.valueOf((char) Integer.parseInt(matcher.group(2), 16)));
			}
			System.out.println(result);
			StringSelection ss = new StringSelection(result);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
			System.out.println("Result copied to clipboard!");
		}
    }
}