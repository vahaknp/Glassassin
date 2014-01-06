import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class GetNames
{
	public static void main (String args[])
	{
		// Input
		String[] adjective = new String[]{"Robert"};

		// Hashmap init
		Map<String,String[]> data_base = new HashMap<String,String[]>();
		data_base.put("Student", new String[]{"Robert", "Serg"});
		data_base.put("Programmer", new String[]{"Robert", "Hrayr"});
		data_base.put("Tall", new String[]{"Hrayr", "Artak", "Gegham"});
		data_base.put("Blonde", new String[]{"Robert", "Vem", "Gegham", "Vshnasp"});

		ArrayList<String[]> returnedNames = new ArrayList<String[]>();
		
		for(int i=0; i<adjective.length; i++)
			if (data_base.containsKey(adjective[i]))
				returnedNames.add(data_base.get(adjective[i]));
		
		if(returnedNames.size() > 0)
		{
			String[] finalNames = returnedNames.get(0);
			for(int i=1; i<returnedNames.size(); i++)
				finalNames = intersect(finalNames, returnedNames.get(i));
			
			System.out.println(Arrays.toString(finalNames));
		}
		else
			System.out.println("No such adjective");

	}
	
	public static String[] intersect(String[] a, String[] b)
	{
		ArrayList<String> c = new ArrayList<String>();
		
		for (int j = 0; j < a.length; j++)
			for(int i=0; i < b.length; i++)
				if(a[j].equals(b[i]))
					c.add(a[j]);
		
		return c.toArray(new String[c.size()]);
	}
}