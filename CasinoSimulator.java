import java.io.*;
import java.util.Scanner;
import java.util.Queue;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class CasinoSimulator 
{

	public static void main(String[] args)
	{
		//String gfile = "games";
		String gfile = "gamesMulModels";
		String pfile = "players";
		
		final int MAX_NUMBER_WHEELS = 5;
		int choice;
		Casino WonderWheels = new Casino(MAX_NUMBER_WHEELS);
		Queue<Player> line = new LinkedList<Player>();
		Wheel currentWheel = null;
		Scanner scan = new Scanner(System.in);
		loadWheels(gfile, WonderWheels);
		printAvailableGames(WonderWheels);
		loadPlayers(pfile, line);
		choice = gameMenu();
		while (choice != 3)
		{
			if (choice == 1)
			{
				boolean done = false;
				currentWheel = selectGame(WonderWheels);
				if (currentWheel == null)
				{
					done = true;
				}
				while(!done)
				{
					choice = selectWheelAction(currentWheel);
					if (choice == 1)
					{
						try
						{
						if (currentWheel.getNumberPlayers() == 5)
							System.out.println("["+ currentWheel.getName() + " is full]");
						else 
						{
							if (line.peek().playerName().length() >= 6)
							{
								if (line.peek().playerName().substring(0,  6).equals("Player"))
									System.out.print("[Player");
								else
									System.out.print("[" + line.peek().playerName());
							}
							else
								System.out.print("[" + line.peek().playerName());
							if(currentWheel.addPlayer(line))
								System.out.println(" was added to " + currentWheel.getName() + "]");
							else
								System.out.println(" could not be added to " + currentWheel.getName() + "]");
							}
						}
						catch (NullPointerException e)
						{
							System.out.println("[There are no players in the queue]\n");
						}
					}
					else if (choice == 2)
					{
						if(!currentWheel.checkActive())
							System.out.println("[The wheel currently has no players]");
							else currentWheel.playRound();
						}
					else if (choice == 3)
					{
						removePlayerMenu(currentWheel);
					}
					else
						done = true;		
				}
				
			}
			else if (choice == 2)
			{
				addPlayerToLine(line);
			}
			choice = gameMenu();

		}
		WonderWheels.close();
		printLine(line);
	}
	
	
	
	public static void printWheelMenu(Wheel w)
	{
		System.out.println("\n" + w.getName() + " Menu Options");
		System.out.println("  1. Add a player");
		System.out.println("  2. Play a round");
		System.out.println("  3. Remove a player (Player wishes to leave)");
		System.out.println("  4. Return to Main Menu");
	}
	public static int selectWheelAction(Wheel w)
	{
		boolean valid = false;
		String selection;
		Scanner scan = new Scanner(System.in);
		
		do {
			printWheelMenu(w);
			selection = scan.nextLine();
			if (selection.equals("1") || selection.equals("2") 
					|| selection.equals("3") || selection.equals("4"))
				valid = true;

			else
			{
				System.out.println("**Please select an option from the menu!**");
			}
		} while(!valid);
		
		return Integer.parseInt(selection);	
	}
	public static void printAvailableGames(Casino c)
	{
		System.out.print("Available games: " );
		for (int x = 0; x < c.numberOfWheels(); x++)
			System.out.print(c.useWheel(x).getName() + " ");
		System.out.println("");
	}
	public static void printAvailableGamesList(Casino c)
	{
		System.out.println("\nAvailable Games (Bet Range) (Players)");
		for (int x = 0; x < c.numberOfWheels(); x++)
		{
			System.out.print("  " + (x + 1) + ". " + c.useWheel(x).getName());
			System.out.printf("%16s", c.useWheel(x).requirements());
			System.out.println("    (" + c.useWheel(x).getNumberPlayers() + "/" +
					c.useWheel(x).getMaxPlayers() + ")");
		}
		
	}
	public static int gameMenu()
	{
		boolean valid = false;
		Scanner scan = new Scanner(System.in);
		String selection = "";
		while (!valid)
		{
			printGameMenu();
			selection = scan.nextLine();
			if (selection.equals("1"))
			{
				valid = true;
			}
			else if (selection.equals("2"))
			{
				valid = true;
			}
			else if (selection.equals("3"))
			{
				valid = true;
			}
			else
			{
				System.out.println("**Please select an option from the menu!**");
			}
		}
		
		return Integer.parseInt(selection);
	}
	public static Wheel selectGame(Casino c)
	{
		boolean valid = false;
		Scanner scan = new Scanner(System.in);
		int index = 0;
		do {
			printAvailableGamesList(c);
			System.out.println("  " + (c.numberOfWheels() + 1) + ". Return to Main Menu");
			try
			{
				valid = true;
				int choice = Integer.parseInt(scan.nextLine());
				index = choice - 1;
				if (index >= c.numberOfWheels() || choice <= 0)
				{
					if (index == c.numberOfWheels())
					{
						Wheel empty = null;
						return empty;
					}
					System.out.println("**Please select an option from the menu!**");
					valid = false;
				}
				else
				{
					valid = true;
				}
			}
			catch (NumberFormatException e)
			{
				System.out.println("**Please select an option from the menu!**");
				valid = false;
			}
			catch (IndexOutOfBoundsException e)
			{
				System.out.println("**Please select an option from the menu!**");
				valid = false;
			}
		} while (!valid);
		return c.useWheel(index);
	}	
	public static void addPlayerToLine(Queue<Player> q)
	{

		Player temp = null;
		boolean valid = false;
		Scanner scan = new Scanner(System.in);
		int playerType = 0;
		int cash = 0;
		int ID = 0;
		String name;
		do {
			try
			{
				valid = true;
				printPlayerTypes();	
				playerType = Integer.parseInt(scan.nextLine());
				System.out.println("PlayerType: " + playerType);
				if (playerType < 1 || playerType > 4)
				{
					System.out.println("**Please select an option from the menu!**");
					valid = false;
				}
				else
				{
					valid = true;
				}
						
			}
			catch(NoSuchElementException e)
			{
				System.out.println("No value found!");
				valid = false;
			}
			catch(NumberFormatException e)
			{
				System.out.println("**Please select an option from the menu!**");
				valid = false;
			}
		}
		while (!valid);
		if (playerType == 4)
			return;
		do {
			try
			{
				valid = true;
				System.out.print("How much cash does the player have: ");
				cash = Integer.parseInt(scan.nextLine());
				if (cash < 0)
				{
					System.out.println("Please enter a positive value!");
							valid = false;
				}
				else
				{
					valid = true;
				}
			}
			catch(NumberFormatException e)
			{
				System.out.println("Enter integer values only!");
				valid = false;
			}
		}
		while (!valid);
		if (playerType == 1)
		{
			temp = new Player(cash);
		}
		
		else if (playerType == 2 || playerType == 3)
		{
			do {
				try
				{
					valid = true;
					System.out.print("Enter the players ID: ");
					ID = Integer.parseInt(scan.nextLine());
					if (ID < 1)
					{
						System.out.println("Please enter a positive value!");
						valid = false;
					}
					else
					{
						valid = true;
					}
				}
				catch(NumberFormatException e)
				{
					System.out.println("Enter integer values only!");
					valid = false;
				}
			}
			while (!valid);
			System.out.print("Enter the player's name: " );
			name = scan.nextLine();
			if (playerType == 2)
				temp = new VIP_Player(cash, ID, name);
			else
				temp = new SuperVIP(cash, ID, name);
		}
		if (temp != null)
		{
			if (temp.playerName().length() >= 6)
			{
				if (temp.playerName().substring(0, 6).equals("Player"))
					System.out.println("[Player was added to the queue]");
				else
					System.out.println("[" + temp.playerName() + " was added to the queue]");
			}
			else
				System.out.println("[" + temp.playerName() + " was added to the queue]");
		}
		q.add(temp);
	}

	public static void printPlayerTypes()
	{
		System.out.println("Types of Players");
		System.out.println("  1. Regular");
		System.out.println("  2. VIP");
		System.out.println("  3. Super VIP");
		System.out.println("  4. Back to Main Menu");
	}
	public static void printGameMenu()
	{
		System.out.println("\nMain Menu");
		System.out.println("  1. Select a game");
		System.out.println("  2. Add a new player to the list");
		System.out.println("  3. Quit");

	}
	public static void loadWheels(String fileName, Casino c)
	{
		System.out.println("Loading games...");
		boolean loaded = false;
		try
		{
			File file = new File(fileName);
			Scanner fileScan = new Scanner(file);
			while (fileScan.hasNext())
			{
				String machineType;
				String machineName;
				int numberOfMachines;
				int minBet;
				int maxBet;
				int ones;
				int fives;
				int twentyFives;
				int hundreds;
				machineType = fileScan.next();
				numberOfMachines = fileScan.nextInt();
				//System.out.println(machineType + " " + numberOfMachines);
				fileScan.nextLine();
				for (int x = 0; x < numberOfMachines; x++)
				{
					minBet = fileScan.nextInt();
					maxBet = fileScan.nextInt();
					hundreds = fileScan.nextInt();
					twentyFives = fileScan.nextInt();
					fives = fileScan.nextInt();
					ones = fileScan.nextInt();
					//System.out.println(minBet + " " + ones);
					machineName = machineType + (x + 1);
					
					if (machineType.equals("100A"))
					{
						File report = new File(machineName + " Report.txt");
						//PrintWriter printer
						Wheel temp = new Wheel100_A(ones, fives, twentyFives, hundreds , minBet, maxBet, machineName);
						c.addWheel(temp);
					}
					else if (machineType.equals("100B"))
					{
						Wheel temp = new Wheel100_B(ones, fives, twentyFives, hundreds , minBet, maxBet, machineName);
						c.addWheel(temp);
					}
					else
					{
						System.out.println("Check file for bad data.");
					}
				}
			}
			loaded = true;
		}
		catch (FileNotFoundException exception)
		{
			System.out.println("File was not found.");
			loaded = false;
		}
		if (loaded)
			System.out.println("Games successfully loaded");
		else
			System.out.println("Games could not be loaded");

	}
	
	public static void loadPlayers(String fileName, Queue<Player> line)
	{
		System.out.println("Loading players...");
		boolean loaded = false;
		try
		{
			File file = new File(fileName);
			Scanner fileScan = new Scanner(file);
			Player temp = null;
			int id = 0;
			int cash = 0;
			String name = "";
			int playerType = 0;
			while (fileScan.hasNext())
			{
				playerType = fileScan.nextInt();
				cash = fileScan.nextInt();
				if (playerType == 0)
				{
					temp = new Player(cash);
					line.add(temp);
				}
				else if (playerType == 1)
				{
					id = fileScan.nextInt();
					name = fileScan.nextLine();
					name = name.substring(1, name.length());
					temp = new VIP_Player(cash, id, name);
					line.add(temp);
				}
				else if (playerType == 2)
				{
					id = fileScan.nextInt();
					name = fileScan.nextLine();
					name = name.substring(1, name.length());
					temp = new SuperVIP(cash, id, name);
					line.add(temp);
				}
			}
			loaded = true;
		}
		catch (FileNotFoundException exception)
		{
			System.out.println(fileName + " not found in project folder!");
			loaded = false;
		}
		catch (NumberFormatException exception)
		{
			System.out.println("Check file for correct formatting!");
			loaded = false;
		}
		
		if (loaded)
			System.out.println("Players were successfully loaded");
		else
			System.out.println("Players could not be loaded");

	}
	public static void removePlayerMenu(Wheel w)
	{
		boolean valid = true;
		do 
		{
			Scanner scan = new Scanner(System.in);
			try
			{
				valid = true;
				w.printPlayers();
				System.out.println("  " + (w.getNumberPlayers() + 1) + ". Return to main menu");
				String input = scan.nextLine();
				int choice = Integer.parseInt(input);
				
				if (choice != w.getNumberPlayers() +1)
				{
					if (!w.removePlayer(choice))
						valid = false;
				}
			}
			catch (NumberFormatException exception)
			{
				System.out.println("**Please select an option from the menu!**");
				valid = false;
			}
			
		} while (!valid);
	}
	
	public static void printLine(Queue<Player> l)
	{
		System.out.println();
		while(l.peek() != null)
		{
			Player temp = l.poll();
			if(temp.playerName().substring(0,  6).equals("Player"))
				System.out.println("Regular player was left in line...");
			else	
			System.out.println(temp.playerName() +" was left in line..."); 
		}
	}
}
