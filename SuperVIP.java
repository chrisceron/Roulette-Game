import java.util.Scanner;

public class SuperVIP extends VIP_Player
{
	protected int numberOfBets;
	protected int cashBack;
	
	SuperVIP(int cash, int ID, String name)
	{
		super(cash,ID,name);
		numberOfBets = 0;
		cashBack = 0;
	}
	public boolean makeBet(Scanner scan, Wheel w)
    {
		if (super.makeBet(scan,  w))
		{
			numberOfBets++;
			return true;
		}
		else
			return false;
    }
	public void leaveWheel()
	{
		super.leaveWheel();
	}
	
	protected int getCashBack()
	{ 
		if (numberOfBets >= 20)
			return super.getCashBack() + 50;
		else if (numberOfBets >= 10)
			return super.getCashBack() + 20;
		else
			return super.getCashBack();
	}

}
