public class Casino 
{
	private Wheel[] floor;
	private int maxWheels;
	private int activeWheels;
	Casino(int numberWheels)
	{
		floor = new Wheel[numberWheels];
		activeWheels = 0;
		maxWheels = numberWheels;
	}
	public boolean addWheel(Wheel w)
	{
		if (activeWheels < maxWheels)
		{
			floor[activeWheels] = w;
			activeWheels++;
			return true;
		}
		else
			return false;
	}
	
	public int numberOfWheels()
	{
		return activeWheels;
	}
	
	
	public Wheel useWheel(int i)
	{
		return floor[i];
	}
	
	public void close()
	{
		for (int x = 0; x < activeWheels; x++)
			floor[x].closeReport();
	}
}
