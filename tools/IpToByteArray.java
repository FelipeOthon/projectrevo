import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author Bonux
**/
public class IpToByteArray
{
	private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    public static void main(String[] args)
	{
		while(true)
		{
			Scanner sc = new Scanner(System.in);
			System.out.print("Enter IP: ");
			if(sc.hasNext(IPADDRESS_PATTERN))
			{
				String ip = sc.next();
				byte[] bytes = ip.getBytes();
				String result = "CLIENTS.put(new String(new byte[]{ ";
				System.out.print("Result: ");
				for(int i = 0; i < bytes.length; i++)
				{
					result += byteToHex(bytes[i]);
					if(i != (bytes.length - 1))
						result += ", ";
				}
				result += " }), -1);\t// ";
				System.out.println(result);
				StringSelection ss = new StringSelection(result);
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
				System.out.println("Result copied to clipboard!");
				break;
			}
			else
			{
				System.out.println("Wrong IP format! Try again.");
			}
		}
    }

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String byteToHex(byte b)
	{
		String hex = "0x";
		int v = b & 0xFF;
		hex += hexArray[v >>> 4];
		hex += hexArray[v & 0x0F];
		return hex;
	}
}