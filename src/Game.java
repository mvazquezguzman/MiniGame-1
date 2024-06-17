import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;

/**
 * Class: Game
 * @author Matilda Vazquez-Guzman
 * @version 1.1
 * Course: ITEC 3860 Summer 2024
 * Written: June 17, 2024
 * This class is a game where the player navigates through different rooms.
 */

class Room
{
    private int id; // Room ID
    private String name; // Room Name
    private String description; // Room Description
    private boolean visited; // Whether the room has been visited
    private Map<String, Integer> exits; // Exits from the room

    /**
     * Room Constructor
     * @param id - The ID of the room
     * @param name - the name of the room
     * @param description - the description of the room
     */
    public Room(int id, String name, String description)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.visited = false;
        this.exits = new HashMap<>();
    }

    /**
     * Adds an exit to the room
     * @param direction - of the exit
     * @param roomId - the ID of the room that the exit leads to
     */
    public void addExit(String direction, int roomId)
    {
        exits.put(direction, roomId);
    }

    /**
     * @return the ID of the room
     */
    public int getId()
    {
        return id;
    }

    /**
     * @return the name of the room
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the description of the room
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @return whether the room has been visited
     */
    public boolean isVisited()
    {
        return visited;
    }

    /**
     * Sets the visited status of the room
     * @param visited the new visited status
     */
    public void setVisited(boolean visited)
    {
        this.visited = visited;
    }

    /**
     * @return the exits of the room
     */
    public Map<String, Integer> getExits()
    {
        return exits;
    }
}

/**
 * Main Game Class
 */
public class Game
{
    private static Map<Integer, Room> rooms = new HashMap<>();
    private static Room currentRoom;

    /**
     * Main method to start the game
     * @param args
     */
    public static void main(String[] args)
    {
        loadRooms("Rooms.txt");
        playGame();
    }

    /**
     * Method to load rooms from the Rooms.txt file
     * @param filename
     */
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

    /**
     * Method to check if a line represents an exit
     * @param line
     * @return true if the line is an exit line, false otherwise
     */
    private static boolean isExitLine(String line)
    {
        return line.startsWith("EAST") || line.startsWith("WEST") || line.startsWith("NORTH") || line.startsWith("SOUTH") || line.startsWith("UP") || line.startsWith("DOWN");
    }

    /**
     * Method to play the game
     */
    private static void playGame()
    {
        System.out.println("Welcome to my game. You will proceed through rooms based upon your entries.");
        System.out.println("To exit the game, enter X");
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        while (true)
        {
            // Display current room's name and visit status
            System.out.println(currentRoom.getName() + (currentRoom.isVisited() ? " (Visited)" : " (Not Visited)"));
            System.out.println(currentRoom.getDescription());
            System.out.print("Exits: ");
            currentRoom.getExits().keySet().forEach(exit -> System.out.print(exit + " "));
            System.out.println();
            System.out.print("What would you like to do? ");
            String command = scanner.nextLine().trim().toUpperCase();

            if (command.equals("X")) // Exit the game
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