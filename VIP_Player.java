import java.util.InputMismatchException;
import java.util.Scanner;

public class VIP_Player extends Player
{
	final double CASH_BACK_RATE = 0.05;
	private int cashBack;
	protected int totalBet;
	protected String name;
	private int ID;
	
	VIP_Player(int c, int id, String name)
	{
		super(c);
		ID = id;
		this.name = name;
		cashBack = 0;
		totalBet = 0;
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean makeBet(Scanner scan, Wheel w)
    {
		if (super.makeBet(scan,  w))
		{
			totalBet += betAmount[currentBet - 1].value();
			return true;
		}
		else
			return false;
    }
	
	public void leaveWheel()
	{
		cashBack = getCashBack();
		super.leaveWheel();
		System.out.println("[" + name + " earned $" + cashBack + " cash back this session]");
	}
	
	public String playerName()
	{
		return name;
	}
	
	public int playerNumber()
	{
		return ID;
	}
	
	protected int getCashBack()
	{
		if (totalBet > 0)
			return (int)Math.round(totalBet * CASH_BACK_RATE);
		else
			return 0;
	}
}
