import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Queue;

public class Wheel 
{
	public static final int BLACK     =  0;			// even numbers
	public static final int RED       =  1;			// odd numbers
	public static final int GREEN     =  2;			// 00 OR 0
	public static final int NUMBER    =  3;			// number bet
	public static final int MIN_NUM   =  1;			// smallest number to bet
	public static final int MAX_NUM   = 36;			// largest number to bet
	public static final int MAX_PLAYERS = 5;
	    
	// private name constants -- internal use only
	private final int MAX_POSITIONS = 38;	// number of positions on wheel
	private final int NUMBER_PAYOFF = 36;	// payoff for number bet
	private final int COLOR_PAYOFF  = 2;	// payoff for color bet


	// private variables -- internal use only
	private int ballPosition;				// 00, 0, 1 .. 10
	private int color;						// GREEN, RED, OR BLACK
	private int initialBalance;
	private int currentActive;
	private int currentNumberPlayers;
	protected String wheelName;
	protected Player[] players = new Player[MAX_PLAYERS];
	protected boolean[] active = new boolean[MAX_PLAYERS];
	private int cash;
	protected Chips availableChips;
	protected PrintWriter outFile;
	private int roundsPlayed;
	private int transactions;
	//private static int HouseStanding = 0;
	
	public void welcomeMessage()
	{
		System.out.println("\n|||||||||||||||||||||||||||||||||||||||||||||||||||||||");
		System.out.printf("%-52s", "|*| Welcome to a simple version of a roulette game.");
		System.out.println("|*|");
	    System.out.printf("%-52s", "|*| You can place a bet on black, red, or a number.");
		System.out.println("|*|");
	   	System.out.printf("%-52s", "|*| A color bet is paid " + COLOR_PAYOFF + "Xs the bet amount.");		
		System.out.println("|*|");
	    System.out.printf("%-52s", "|*| A number bet is paid " + NUMBER_PAYOFF + "Xs the bet amount.");		
		System.out.println("|*|");
	   	System.out.printf("%-52s", "|*| You can bet on a number from " + MIN_NUM + " to " + MAX_NUM + ".");		
		System.out.println("|*|");
	   	System.out.printf("%-52s", "|*| Gamble responsibly.  Have fun and good luck!");
		System.out.println("|*|");
		System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||");
	}
	
	Wheel(int o, int f, int tf, int h, String wn)
	{
		availableChips = new Chips(o, f, tf, h);
		currentNumberPlayers = 0;
		currentActive = 0;
		cash = 0;
		wheelName = wn;
		roundsPlayed = 0;
		transactions = 0;
		initialBalance = availableChips.value();
		String fileName = wheelName + " Report.txt";
		try 
		{
			outFile = new PrintWriter(fileName);
		} catch (FileNotFoundException e) 
		{
			System.out.println("File could not be created");
			e.printStackTrace();
		}
		outFile.println("Game: " + wheelName);
		outFile.println("Initial Balance: $" + initialBalance);
		outFile.println("  Cash:\t\t$" + cash);
		outFile.println("  $100 chips:\t" + availableChips.getHundreds());
		outFile.println("  $25  chips:\t" + availableChips.getTwentyFives());
		outFile.println("  $5   chips:\t" + availableChips.getFives());
		outFile.println("  $1   chips:\t" + availableChips.getOnes());
		outFile.println();

		
	}
	
	public boolean addPlayer(Queue<Player> inLine)
	{
		if(inLine.peek() == null || currentNumberPlayers >= MAX_PLAYERS)
			return false;
		else
		{
			int insertIndex = 0;
			while(active[insertIndex] == true)
				insertIndex++;
			players[insertIndex] = inLine.peek();
			players[insertIndex].setPlayerNumber(insertIndex + 1);
			outFile.println("["+inLine.peek().playerName() + " joined the game]");
			inLine.remove();
			active[insertIndex] = true;
			currentNumberPlayers++;
			return true;
		}
	}

	public boolean removePlayer(int playerNumber)
	{
		try
		{
			int count = 0;
			int index = -1;
			for (int x = 0; x < MAX_PLAYERS; x++)
			{
				if (active[x])
					count++;
				if (count == playerNumber)
				{
					index = x;
					x = MAX_PLAYERS;
				}
			}
			if(active[index])
			{	
				outFile.println("[" +players[index].playerName() + " left the game]");
				players[index].leaveWheel();
				active[index] = false;
				currentNumberPlayers--;
				return true;
			}
			else
			{
				System.out.println("**Please select an option from the menu!**");
				return false;
			}
		}
		catch (IndexOutOfBoundsException exception)
		{
			System.out.println("**Please select an option from the menu!**");
			return false;
		}
	}
	
	public void betOptions()
	{
		System.out.println("Betting Options:");
		System.out.println("    1. Bet on black (even numbers)");
		System.out.println("    2. Bet on red (odd numbers)");
		System.out.println("    3. Bet on a number between " + MIN_NUM +
			" and " + MAX_NUM);
		System.out.println();
	}
	
	public void spin()
    {
    	ballPosition = (int)(Math.random() * MAX_POSITIONS - 1);
    	
    	if (ballPosition < 1)
    		color = GREEN;
    	else if (ballPosition % 2 == 0)
    		color = BLACK;
    	else
    		color = RED;
    	String sColor;
    	
    	switch(color)
    	{
    	case BLACK: sColor = "Black"; break;
    	case RED: sColor = "Red"; break;
    	default: sColor = "Green"; break;
    	}
    	
    	System.out.println("\nThe wheel spins and.....");
    	try
    	{
    		Thread.sleep(3000);
    	}
    	catch (InterruptedException exception)
    	{
    		
    	}
    	System.out.println("The ball landed on " + ballPosition + " "+ sColor + "!");
    	try
    	{
    		Thread.sleep(1500);
    	}
    	catch (InterruptedException exception)
    	{
    		
    	}
		roundsPlayed++;
		outFile.println();
		outFile.println("Round " + roundsPlayed + " (" + sColor + " " + ballPosition + ")");
		outFile.println("Trans Player BAmount  ($100 $25  $5  $1) BType  Pay ($100 $25  $5  $1)");
    }
	public boolean standardChipSale()
	{

		if (	availableChips.getTwentyFives() >= 2 &&
				availableChips.getFives() >= 5 &&
				availableChips.getOnes() >= 25 )
		{
			Chips standard = new Chips(25, 5, 2, 0);
			availableChips.remove(standard);
			players[currentActive].takeChips(standard);
			cash += 100;
			outFile.println("[" + players[currentActive].playerName() + " exchanged $100 for 2 $25-chips, 5 $5-chips, 25 $1-chips]");
			return true;
		}
		else
			return false;
	}
	public boolean sellChips(Chips wanted)
	{
		if (	availableChips.getHundreds() >= wanted.getHundreds() &&	
				availableChips.getTwentyFives() >= wanted.getTwentyFives() &&
				availableChips.getFives() >= wanted.getFives() &&
				availableChips.getOnes() >= wanted.getOnes() 
			)
		{
			players[currentActive].takeChips(wanted);
			availableChips.remove(wanted);
			cash += wanted.value();
			///////////////print more to report///////////////////////
			outFile.print("[" + players[currentActive].playerName() + " exchanged $" + wanted.value() + " for ");
			if (wanted.getHundreds() > 0)
				outFile.print(wanted.getHundreds() + " $100-chips");
			if (wanted.getTwentyFives() > 0 && wanted.getHundreds() > 0)
				outFile.print(", " + wanted.getTwentyFives() + ", $25-chips");
			else if (wanted.getTwentyFives() > 0)
				outFile.print(wanted.getTwentyFives() + " $25-chips");
			if (wanted.getFives() > 0 && (wanted.getHundreds() > 0 || wanted.getTwentyFives() > 0))
				outFile.print(", " + wanted.getFives() + ", $5-chips");
			else if (wanted.getFives() > 0)
				outFile.print(", " + wanted.getFives() + " $5-chips");
			if (wanted.getFives() > 0 && (wanted.getHundreds() > 0 || wanted.getTwentyFives() > 0 || wanted.getFives() > 0))
				outFile.print(", " + wanted.getOnes() + ", $1-chips");
			else if (wanted.getOnes() > 0)
				outFile.print(", " + wanted.getOnes() + " $1-chips");
			outFile.println("]");
			
			return true;
		}
		else
			return false;
	}
	
	public int payOff(Chips[] bet_amount, int[] bet_type, int[] number_bet, int number_of_bets, Player p)
    {
		int total_pay = 0;

		if (number_of_bets > 0)
		{
			String variableMsg = "Spin Results for " + p.playerName();
			int boxLength = variableMsg.length() + 4;
			System.out.println();
			for (int x = 0; x < boxLength; x++)
				System.out.print("*");
			System.out.println();
			System.out.println("* " + variableMsg + " *");
			for (int x = 0; x < boxLength; x++)
				System.out.print("*");
			System.out.println();
			System.out.println("  Number of bets: " + number_of_bets);
			Chips payout = new Chips();
			Chips betHolder = new Chips();
			for (int x = 0; x < number_of_bets; x++)
			{
				betHolder.clear();
				transactions++;
				outFile.printf("%3d", transactions);
				outFile.printf("%9d", p.playerNumber());
				outFile.printf("%6d", bet_amount[x].value());
				outFile.print("    (");
				outFile.printf("%4d", bet_amount[x].getHundreds());
				outFile.printf("%4d", bet_amount[x].getTwentyFives());
				outFile.printf("%4d", bet_amount[x].getFives());
				outFile.printf("%4d", bet_amount[x].getOnes());
				outFile.print(")  ");

				if(bet_type[x] == NUMBER)
				{
					System.out.println("  Bet Amount for Bet " + (x + 1) +": $" + bet_amount[x].value() + " on number "+ number_bet[x]);
					outFile.print(" ");
					outFile.printf("%-2d", number_bet[x]);
				}
				else if (bet_type[x] == BLACK)
				{
					System.out.println("  Bet Amount for Bet " + (x + 1) +": $" + bet_amount[x].value() + " on color Black");
					outFile.print(" B ");
				}
				else 
				{
					System.out.println("  Bet Amount for Bet " + (x + 1) +": $" + bet_amount[x].value() + " on color Red");
					outFile.print(" R ");
				}
				payout.clear();
				if (color == GREEN)
				{
					availableChips.add(bet_amount[x]);	
					bet_amount[x].clear();
				}
				else if (bet_type[x] == NUMBER)
				{
					if (ballPosition == number_bet[x])
		    		{
		    			payout.add(payPile(bet_amount[x].value() * (NUMBER_PAYOFF - 1)));
		    			availableChips.remove(payout);
		    			betHolder.add(bet_amount[x]);
		    			bet_amount[x].add(payout);
		    		}
		    		else
		    		{
						availableChips.add(bet_amount[x]);
		    			bet_amount[x].clear();
		    		}
		    	}
		    	else
		    	{
		    		if (color == BLACK && bet_type[x] == BLACK)
		    		{
		    			payout.add(payPile(bet_amount[x].value() * (COLOR_PAYOFF - 1)));
		    			availableChips.remove(payout);
		    			betHolder.add(bet_amount[x]);
		    			bet_amount[x].add(payout);
	
		    		}
		    		else if (color == RED && bet_type[x] == RED)
		    		{
		    			payout.add(payPile(bet_amount[x].value() * (COLOR_PAYOFF - 1)));
		    			availableChips.remove(payout);
		    			betHolder.add(bet_amount[x]);
		    			bet_amount[x].add(payout);
		    		}
		    		else 
		    		{
						availableChips.add(bet_amount[x]);
		    			bet_amount[x].clear();
		    		}
		    	}
				payout.add(betHolder);
				total_pay += payout.value();
				outFile.printf("%6d", payout.value());
				outFile.print(" (");
				outFile.printf("%4d", payout.getHundreds());
				outFile.printf("%4d", payout.getTwentyFives());
				outFile.printf("%4d", payout.getFives());
				outFile.printf("%4d", payout.getOnes());
				outFile.println(")");
				
				int lastActive = 0;
				for (int y = 0; y < MAX_PLAYERS; y++)
				{
					if (active[y])
						lastActive = y;
				}
				if (p.playerNumber() == players[lastActive].playerNumber() &&
					x == number_of_bets - 1)
					outFile.println();
				
				if (x > 0)
				{
					bet_amount[0].add(bet_amount[x]);
					bet_amount[x].clear();
				}
			}
		}
		return total_pay;
    }
	public void printChips()
	{
		System.out.println("  Wheel Chips Available For Exchange");
		System.out.println("  ----------------------------------");
		System.out.println("  $100: " + availableChips.getHundreds());
		System.out.println("  $25:  " + availableChips.getTwentyFives());
		System.out.println("  $5:   " + availableChips.getFives());
		System.out.println("  $1:   " + availableChips.getOnes());

	}
	public void incrementCurrentPlayerNumber()
	{
		currentActive = (currentActive+1) % MAX_PLAYERS;
	}
	public int getCurrentPlayerNumber()
	{
		return currentActive + 1;
	}
    public int getWheelStanding()
    {
    	return (availableChips.value() + cash - initialBalance);
    }
    public String getName()
    {
    	return wheelName;
    }
    public void printPlayerOptions(int x)
    {
    	System.out.println("\n" + players[x].playerName() + "'s Options");
    	System.out.println("  1. Make bet");
    	System.out.println("  2. Buy Chips ($100)");
    	System.out.println("  3. Buy Chips (other)");
    	System.out.println("  4. Exchange Chips");
    	System.out.println("  5. View Chips");
    	System.out.println("  6. End Turn");
    }
   public void playRound()
   {
	   welcomeMessage();
	   for(int x = 0; x < MAX_PLAYERS; x++)
	   {
		   if (active[x])
		   {
			   currentActive = x;
			   boolean finished = false;
			   
			   do {
				   Scanner scan = new Scanner(System.in);
				   printPlayerOptions(x);
				   String choice = scan.nextLine();
				   if (choice.equals("1"))
				   {
					   if (players[x].hasChips())
						   betOptions();
					   players[x].makeBet(scan, this);
				   }
				   else if (choice.equals("2"))
				   {
					  players[x].buyIn(this);
				   }
				   else if (choice.equals("3"))
				   {
					   players[x].buyChips(this);
				   }
				   else if (choice.equals("4"))
				   {
					   players[x].tradeChips(this);
				   }
				   else if (choice.equals("5"))
				   {
					   System.out.println();
					   players[x].printChips();
				   }
				   else if (choice.equals("6"))
					   finished = true;
				   else
				   {
					   System.out.println("Please selection an option from the menu!");
					   finished = false;
				   }
			   } while (!finished);
		   }
	   }
	   spin();
	   for(int x = 0; x < MAX_PLAYERS; x++)
	   {
		   if (active[x])
		   {
			   players[x].payment(this);
		   }
	   }
	   
   }
   
   public boolean checkActive()
   {
	   for(int i = 0; i < MAX_PLAYERS; i++)
	   {
		   if(active[i])
			   return true;
	   }
	   return false;
   }
   
   private Chips payPile(int amount)
   {
	   int hundreds = 0;
	   int twFives = 0;
	   int fives = 0;
	   int ones = 0;
	   
	   if (amount >= 100)
		   hundreds = amount / 100;
	   amount %= 100;
	   if (amount >= 25)
		   twFives = amount / 25;
	   amount %= 25;
	   if (amount >= 5)
		   fives = amount / 5;
	   amount %= 5;
	   if (amount >= 1)
		   ones = amount;
	   amount %= 1;
	   Chips temp = new Chips(ones, fives, twFives, hundreds);
	   return temp;
   }
   public boolean validBet(Chips bet)
   {
	   // compare the bet.value() with the min and max bet and return appropriately in the 100A/B class
	   return true;
   }
   
   public void printPlayers()
   {
	   System.out.println("\n" + wheelName + "'s Current Players");
	   int current = 1;
	   for (int x = 0; x < MAX_PLAYERS; x++)
	   {
		   if (active[x])
		   {
			   System.out.println("  " + current + ". " +  players[x].playerName());
			   current++;
		   }
	   }
	}
   
	public boolean exchangeChips(Chips wanted, Chips trading, Player p)
	{
		if (wanted.value() == trading.value() && wanted.value() > 0)
		{
			if (availableChips.getHundreds() >= wanted.getHundreds() &&
				availableChips.getTwentyFives() >= wanted.getTwentyFives() &&
				availableChips.getFives() >= wanted.getFives() &&
				availableChips.getOnes() >= wanted.getOnes())
			{
				availableChips.remove(wanted);
				availableChips.add(trading);
				p.takeChips(wanted);
				outFile.print("[" + p.playerName() + " exchanged");
				if (trading.getHundreds() > 0)
					outFile.print(" " + trading.getHundreds() + "$100-chips");
				if (trading.getTwentyFives() > 0 && trading.getHundreds() > 0)
					outFile.print(", " + trading.getTwentyFives() + " $25-chips");
				else if (trading.getTwentyFives() > 0)
					outFile.print(" " + trading.getTwentyFives() + " $25-chips");
				if (trading.getFives() > 0 && (trading.getHundreds()> 0 || trading.getTwentyFives() > 0))
					outFile.print(", " + trading.getFives() + " $5-chips");
				else if (trading.getFives() > 0)
					outFile.print(" " + trading.getFives() + " $5-chips");
				if (trading.getOnes() > 0 && (trading.getHundreds()> 0 
											|| trading.getTwentyFives() > 0
											|| trading.getFives() > 0))
					outFile.print(", " + trading.getOnes() + "$1-chips");
				else if (trading.getOnes() > 0)
					outFile.print(" " + trading.getOnes() + "$1-chips");
				outFile.println("]");
				return true;
			}
			else
				return false;
		}
		else
			return false;
	}
	public int getNumberPlayers()
	{
		return currentNumberPlayers;
	}
	public void takeChips(Chips c)
	{
		availableChips.add(c);
	}
	public void closeReport()
	{
		System.out.println("Remaining players leave Wheel " + wheelName + " as the Casino closes...");
		for (int x = 0; x < MAX_PLAYERS; x++)
		{
			if (active[x])
			{
				outFile.println("[" +players[x].playerName() + " left the game]");
				players[x].leaveWheel();
				active[x] = false;
				currentNumberPlayers--;
			}
		}
		outFile.println();
		outFile.println("\nEnding Balance: $" + (availableChips.value() + cash));
		outFile.println("  Cash:         $" + cash);
		outFile.println("  $100 chips:   " + availableChips.getHundreds());
		outFile.println("  $25 chips:    " + availableChips.getTwentyFives());
		outFile.println("  $5 chips:     " + availableChips.getFives());
		outFile.println("  $1 chips:     " + availableChips.getOnes());
		outFile.print("  Wheel Standing for this session: " );
		if (getWheelStanding() == 0)
			outFile.print("broke even." );
		else if (getWheelStanding() > 0)
			outFile.print("$" + getWheelStanding() + " profit");
		else
			outFile.print("$" + Math.abs(getWheelStanding()) + " loss");
		outFile.close();
	}
	
	public String requirements()
	{
		return "";
	}
	
	public int getMaxPlayers()
	{
		return MAX_PLAYERS;
	}
}
