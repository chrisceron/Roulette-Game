
public class Wheel100_A extends Wheel
{

	private int minBet;
	private int maxBet;
	
	Wheel100_A(int o, int f, int tf, int h, int min, int max, String wn)
	{
		super(o, f, tf, h, wn);
		minBet = min;
		maxBet = max;
	}
	
	public int getMinBet() {return minBet;}
	
	public int getMaxBet() {return maxBet;}
	
	public void betOptions()
	{
		super.betOptions();
		System.out.print("[*****  MINIMUM BET: ");
		System.out.printf("%3d", minBet);
		
		System.out.println("  *****]");
		System.out.print("[*****  MAXIMUM BET: ");
		System.out.printf("%3d", maxBet);
		System.out.println("  *****]\n");
		
	}
	
	public boolean validBet(Chips bet)
	{
		if (bet.value() >= minBet && bet.value() <= maxBet)
			return true;
		else
			return false;
	}
	
	public String requirements()
	{
		String req = "(";
		String min = String.format("%03d", minBet);
		String max = String.format("%03d", maxBet);
		return req + min + "-" + max + ")";
	}
}
