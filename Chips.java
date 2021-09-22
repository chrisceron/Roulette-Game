
public class Chips 
{
	private int ones;
	private int fives;
	private int twentyFives;
	private int hundreds;
	
	Chips(int o, int f, int tf, int h)
	{
		if (o >= 0 && f >= 0 && tf >= 0 && h >= 0)
		{
			ones = o;
			fives = f;
			twentyFives = tf;
			hundreds = h;
		}
		else
		{
			ones = 0;
			fives = 0;
			twentyFives = 0;
			hundreds = 0;
		}
	}
	Chips()
	{
		ones = 0;
		fives = 0;
		twentyFives = 0;
		hundreds = 0;
	}
	
	public void addOnes(int o)
	{
		if (o > 0)
			ones += o;
	}
	public void addFives(int f)
	{
		if (f > 0)
			fives += f;
	}
	public void addTwentyFives(int tf)
	{
		if (tf > 0)
			twentyFives += tf;
	}
	public void addHundreds(int h)
	{
		if (h > 0)
			hundreds += h;
	}
	
	public boolean removeOnes(int o)
	{
		if (o > 0 && o <= ones)
		{
			ones -= o;
			return true;
		}
		
		return false;
	}
	public boolean removeFives(int f)
	{
		if (f >= 0 && f <= fives)
		{
			fives -= f;
			return true;
		}
		return false;
	}
	public boolean removeTwentyFives(int tf)
	{
		if (tf > 0 && tf <= twentyFives)
		{
			twentyFives -= tf;
			return true;
		}
		return false;
	}
	public boolean removeHundreds(int h)
	{
		if (h > 0 && h <= hundreds)
		{
			hundreds -= h;
			return true;
		}
		return false;
	}
	
	public void setOnes(int o)
	{
		ones = o;
	}
	public void setFives(int f)
	{
		fives = f;
	}
	public void setTwentyFives(int tf)
	{
		twentyFives = tf;
	}
	public void setHundreds(int h)
	{
		hundreds = h;
	}
	public int getOnes()
	{
		return ones;
	}
	public int getFives()
	{
		return fives;
	}
	public int getTwentyFives()
	{
		return twentyFives;
	}
	public int getHundreds()
	{
		return hundreds;
	}
	public int value()
	{
		return 	getOnes() +
				getFives() * 5 +
				getTwentyFives() * 25 +
				getHundreds() * 100;
	}
	public void clear()
	{
		setOnes(0);
		setFives(0);
		setTwentyFives(0);
		setHundreds(0);
	}
	
	public void add(Chips c)
	{
		addOnes(c.getOnes());
		addFives(c.getFives());
		addTwentyFives(c.getTwentyFives());
		addHundreds(c.getHundreds());
	}
	
	public void remove(Chips c)
	{
		removeOnes(c.getOnes());
		removeFives(c.getFives());
		removeTwentyFives(c.getTwentyFives());
		removeHundreds(c.getHundreds());	
	}
	
	public String toString()
	{
		String result = "";
		if(ones > 0) result += "Ones: " + ones;
		if(fives > 0) result += "\nFives: " + fives;
		if(twentyFives > 0) result += "\nTwentyFives: " + twentyFives;
		if(hundreds > 0) result += "\nHundreds: " + hundreds;
		
		return result;
	}
}
