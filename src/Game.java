import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;

/**
 * Class Name: Game
 * Author: Matilda Vazquez-Guzman
 * Course: ITEC 3860 Summer 2024
 * Written: June 5, 2024
 */

class Room
{
    private int id;
    private String name;
    private String description;
    private boolean visited;
    private Map<String, Integer> exits;

    public Room(int id, String name, String description)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.visited = false;
        this.exits = new HashMap<>();
    }

    public void addExit(String direction, int roomId)
    {
        exits.put(direction, roomId);
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public boolean isVisited()
    {
        return visited;
    }

    public void setVisited(boolean visited)
    {
        this.visited = visited;
    }

    public Map<String, Integer> getExits()
    {
        return exits;
    }
}

public class Game
{
    private static Map<Integer, Room> rooms = new HashMap<>();
    private static Room currentRoom;

    public static void main(String[] args)
    {
        loadRooms("Rooms.txt");
        playGame();
    }

    private static void loadRooms(String filename)
    {
        try (BufferedReader br = new BufferedReader(new FileReader(filename)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                // Split line into ID and name
                String[] parts = line.split(",", 2);
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                StringBuilder description = new StringBuilder();

                // Read description lines until an exit line is found
                while ((line = br.readLine()) != null && !isExitLine(line))
                {
                    description.append(line).append(" ");
                }
                Room room = new Room(id, name, description.toString().trim());

                // Read exit lines
                while (line != null && isExitLine(line))
                {
                    String[] exitParts = line.split(",");
                    room.addExit(exitParts[0].trim().toUpperCase(), Integer.parseInt(exitParts[1].trim()));
                    line = br.readLine();
                }
                rooms.put(id, room);
            }
            currentRoom = rooms.get(1); // Start game in room with ID 1
        }
        catch (IOException e)
        {
            System.err.println("Error reading rooms file: " + e.getMessage());
        }
    }

    private static boolean isExitLine(String line)
    {
        return line.startsWith("EAST") || line.startsWith("WEST") || line.startsWith("NORTH") || line.startsWith("SOUTH") || line.startsWith("UP") || line.startsWith("DOWN");
    }

    private static void playGame()
    {
        System.out.println("Welcome to my game. You will proceed through rooms based upon your entries.");
        System.out.println("To exit the game, enter X");
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        while (true)
        {
            System.out.println(currentRoom.getName() + (currentRoom.isVisited() ? " (Visited)" : " (Not Visited)"));
            System.out.println(currentRoom.getDescription());
            System.out.print("Exits: ");
            currentRoom.getExits().keySet().forEach(exit -> System.out.print(exit + " "));
            System.out.println();
            System.out.print("What would you like to do? ");
            String command = scanner.nextLine().trim().toUpperCase();

            if (command.equals("X"))
            {
                System.out.println("Thank you for playing.");
                break;
            }
            if (currentRoom.getExits().containsKey(command))
            {
                currentRoom.setVisited(true);
                currentRoom = rooms.get(currentRoom.getExits().get(command));
            }
            else
            {
                System.out.println("Invalid command. Please try again.");
            }
            System.out.println();
        }
        scanner.close();
    }
}