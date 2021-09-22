import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Player 
{
	protected int playerNumber;
	protected Chips[] betAmount;
	protected int[] betType;
	protected int[] betNumber;
	protected int cash;
	protected int currentBet;
	private int initialCash;
	protected Chips availableChips;
	
	////////// Constructor //////////
	Player(int c)
	{
		if (c >= 0)
			cash = c;
		else
			cash = 0;
		initialCash = cash;
		availableChips = new Chips();
		betAmount = new Chips[3];
		for (int x = 0; x < 3; x++)
			betAmount[x] = new Chips();
		betType = new int[3];
		betNumber = new int[3];
		currentBet = 0;
	}
	
	//||||||||| Accessors |||||||||//
	
	 public String playerName()
	 {
		 return ("Player #" + playerNumber);

	 }
	 
	 public int playerNumber()
	 {
		 return playerNumber;
	 }

	public int standing()
	{
		return cash + availableChips.value() - initialCash;
	}	
	protected void printChips()
	{
		System.out.println(playerName());
		System.out.println("  Player Chips Available");
		System.out.println("  ----------------------");
		System.out.println("  $100: " + availableChips.getHundreds());
		System.out.println("  $25:  " + availableChips.getTwentyFives());
		System.out.println("  $5:   " + availableChips.getFives());
		System.out.println("  $1:   " + availableChips.getOnes());
	}
	public int getCash()
	{
		return cash;
	}
	
	//||||||||| Mutators |||||||||//
	
		////////// playerNumber //////////
	public void setPlayerNumber(int i)
	{
		if (i > 0 && i <= 5)
			playerNumber = i;
	}
		////////// Chips/Cash //////////
	public void tradeChips(Wheel w)
	{
		boolean valid = true;
		String line;
		do
		{
			try
			{
				printChips();
				w.printChips();
				valid = true;
				System.out.println("Enter # chips " + playerName() 
				+ " is trading in separated by a space (h, twf, f, o) or -1 to exit: ");
				Scanner scan = new Scanner(System.in);
				line = scan.nextLine();
				if (line.equals("-1"))
					valid = true;
				else
				{
					Scanner lineScan = new Scanner(line);
					int hundreds = lineScan.nextInt();
					int twentyFives = lineScan.nextInt();
					int fives = lineScan.nextInt();
					int ones = lineScan.nextInt();
					Chips trading = new Chips(ones, fives, twentyFives, hundreds);
					
					if (trading.getHundreds() > availableChips.getHundreds() ||
						trading.getTwentyFives() > availableChips.getTwentyFives() ||
						trading.getFives() > availableChips.getFives() ||
						trading.getOnes() > availableChips.getOnes())
					{
						valid = false;
						System.out.println("**" + playerName() + " does not have sufficient chips to trade!**");
					}
					else
					{
						System.out.println("Enter # chips " + playerName() 
						+ " wants in exchange separated by a space (h, twf, f, o) or -1 to exit: ");
						line = scan.nextLine();
						lineScan = new Scanner(line);
						hundreds = lineScan.nextInt();
						twentyFives = lineScan.nextInt();
						fives = lineScan.nextInt();
						ones = lineScan.nextInt();
						Chips wanted = new Chips(ones, fives, twentyFives, hundreds);	
						if (w.exchangeChips(wanted, trading, this))
						{
							availableChips.remove(trading);
							System.out.println("[" + playerName() + " exchanged the chips]");
						}
						else
						{
							System.out.println("**Please ensure trade is balanced!**");
							valid = false;
						}
					}
					
				}
			}
			catch (NumberFormatException exception)
			{
				System.out.println("**Please enter integer values only!**");
			}
			catch (InputMismatchException exception)
			{
				System.out.println("**Please enter integer values only!**");
				valid = false;
			}
			catch (NoSuchElementException exception)
			{
				System.out.println("**Please enter all values on same line!**");
				valid = false;
			}
		} while (!valid);
		
	}
	
	public void takeChips(Chips c)
	{
		availableChips.add(c);
	}
	
	public void buyChips(Wheel w)
	{
		boolean valid = true;
		String line;
		do
		{
			try
			{
				w.printChips();
				valid = true;
				System.out.println(playerName() + "'s available Cash: " + cash);
				System.out.println("Enter # chips separated by a space (h, twf, f, o) or -1 to exit: ");
				Scanner scan = new Scanner(System.in);
				line = scan.nextLine();
				if (line.equals("-1"))
					valid = true;
				else
				{
					Scanner lineScan = new Scanner(line);
					int hundreds = lineScan.nextInt();
					int twentyFives = lineScan.nextInt();
					int fives = lineScan.nextInt();
					int ones = lineScan.nextInt();
					Chips wanted = new Chips(ones, fives, twentyFives, hundreds);
					if (wanted.value() > cash)
					{
						valid = false;
						System.out.println("**" + playerName() + " does not have sufficient cash!**");
					}
					else if (w.sellChips(wanted))
					{
						
						System.out.println("[" + playerName() + " purchased the chips]");
						cash -= wanted.value();
					}
					else
					{
						System.out.println("**Wheel does not have sufficient chips!**");
						valid = false;
					}
				}
			}
			catch (InputMismatchException exception)
			{
				System.out.println("**Please enter integer values only!**");
				valid = false;
			}
			catch (NoSuchElementException exception)
			{
				System.out.println("**Please enter all values on same line!**");
				valid = false;
			}
		} while (!valid);
		
	}
	
	public boolean buyIn(Wheel w)
	{
		if (cash < 100 )
		{
			System.out.println("**" + playerName() + " does not have cash for $100 dollar buy-in.");
			return false;
		}
		else
		{
			if (w.standardChipSale())
			{
				System.out.println("[" + playerName() + " purchased the chips]");
				cash -= 100;
				return true;
			}
			else
				System.out.println("**The wheel does not have enough chips for the standard buy-in!**");
			return false;
		}
			
	}
		////////// betAmount/betType/betNumber/availableChips //////////
	public boolean makeBet(Scanner scan, Wheel w)
    {
		if (availableChips.value() == 0)
		{
			System.out.println("**" + playerName() + " has no chips to bet with!**");
			return false;
		}
		else if (currentBet < 3)
		{
			boolean valid;
			System.out.println(playerName());
			do
			{
				
				try
				{
					valid = true;
			      	System.out.print("\tEnter your choice of bet: ");
			      	betType[currentBet] = scan.nextInt();
			      	if (betType[currentBet] < 1 || betType[currentBet] > 3)
			      	{
						System.out.println("**Please select an option from the menu!**");
			    		valid = false;
			      	}
				}
				catch (InputMismatchException exception)
				{
					System.out.println("**Please select an option from the menu!**");
					scan.nextLine();
					valid = false;
				}   	
	      	}
	      	while (!valid);
			
	      	switch(betType[currentBet])
	      	{
	      	case 1: betType[currentBet] = Wheel.BLACK; break;
	      	case 2: betType[currentBet] = Wheel.RED; break;
	      	default: betType[currentBet] = Wheel.NUMBER; break;
	      	}
	      	
	      	
	      	if (betType[currentBet] == Wheel.NUMBER)
	      	{
	      		boolean validNumber = true;
	      		do {
	      			try
	      			{
			      		System.out.print("\tEnter the number you wish to bet on: ");
			      		betNumber[currentBet] = scan.nextInt();
			      		while (betNumber[currentBet] < Wheel.MIN_NUM || betNumber[currentBet] > Wheel.MAX_NUM)
			      		{
			      			System.out.println(playerName());
			          		System.out.println("\tInvalid Number! Please enter a number between " 
			          				+ Wheel.MIN_NUM + " and " + Wheel.MAX_NUM + ":");
			        		System.out.println(playerName());
			          		System.out.print("\tEnter the number you wish to bet on: ");
			          		betNumber[currentBet] = scan.nextInt();
			      		}
			      		validNumber = true;
		      		}
	      			catch(NumberFormatException exception)
					{
	      				scan.nextLine();
						System.out.println("**Please enter integer values only!**");
						validNumber = false;
					}
					catch (InputMismatchException exception)
					{
						scan.nextLine();
						System.out.println("**Please enter integer values only!**");
						validNumber = false;
					}
	      		} while (!validNumber);
	      	}
			printChips();
			int hundreds = 0;
			int twentyFives = 0;
			int fives = 0;
			int ones = 0;
			betAmount[currentBet].clear();
			do
			{
				try
				{
					valid = true;
					System.out.println(playerName());
					System.out.println("Enter # chips bet separated by a space (h, twf, f, o): ");
					hundreds = scan.nextInt();
					twentyFives = scan.nextInt();
					fives = scan.nextInt();
					ones = scan.nextInt();
					if (hundreds > availableChips.getHundreds())
					{
						System.out.println("**You do not have that many $100 chips!**");
						valid = false;
					}
					if (twentyFives > availableChips.getTwentyFives())
					{
						System.out.println("**You do not have that many $25 chips!**");
						valid = false;
					}
					if (fives > availableChips.getFives())
					{
						System.out.println("**You do not have that many $5 chips!**");
						valid = false;
					}
					if (ones > availableChips.getOnes())
					{
						System.out.println("**You do not have that many $1 chips!**");
						valid = false;
					}
					
				}
				catch(NumberFormatException exception)
				{
					scan.nextLine();
					System.out.println("**Please enter integer values only!**");
					valid = false;
				}
				catch (InputMismatchException exception)
				{
					scan.nextLine();
					System.out.println("**Please enter integer values only!**");
					valid = false;
				}
			
			} while (!valid);
			///////////////////// Place possible bet into temporary chips variable
			Chips possibleBet = new Chips (ones, fives, twentyFives, hundreds);
			if (w.validBet(possibleBet))
			{
				betAmount[currentBet].add(new Chips(ones, fives, twentyFives, hundreds));
				availableChips.remove(new Chips(ones, fives, twentyFives, hundreds));
				System.out.println("[" + playerName() + " placed the bet]");
				currentBet++;
				return true;
			}
			else
			{
				System.out.println("**Your bet is not within the appropriate range!**");
				return false;
			}
		////////////////////////// Error message for invalid bet (too low or high) see wheel class for valid bet
		}
		else
			System.out.println("**You've already placed the maximum of 3 bets!**");
		return false;
    }

	public void payment(Wheel w)
	{
		if (currentBet > 0)
		{
			int winnings = w.payOff(betAmount, betType, betNumber, currentBet, this);
			System.out.print("[" + playerName());
	    	if (winnings > 0)
	    	{
	    		System.out.println(" won $" + winnings + "]");
	    	}
	    	else
	    	{
	    		System.out.println(" lost all bets]");
	    	}
	    	availableChips.add(betAmount[0]);
	    	printChips();
		 }
		currentBet = 0;
		betAmount[0].clear();
	}
	
	//|| Utility ||//
	public boolean hasChips()
	{
		return availableChips.value() > 0;
	}
	//|| Other ||//
	public void leaveWheel()
	{
		System.out.print("[" + playerName() + " has left ");
		if (standing() == 0)
		{
			System.out.println("and has broken even]");
		}
		else if (standing() > 0)
		{
			System.out.println("with $" + standing() + " profit]");
		}
		else
			System.out.println("with $" + Math.abs(standing()) + " in losses]");
	 }


}
