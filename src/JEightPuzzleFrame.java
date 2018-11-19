import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;
import static java.lang.Math.sqrt;

public class JEightPuzzleFrame extends JFrame
{
    private int size = 3;
    private JButton buttons[] = new JButton[size*size - 1];
    private JPanel emptyButton = new JPanel();
    private int board[][] = new int[size][size];
    private Container container = new Container();
    
    public static void main(String[] args)
    {
        if(args.length >= 1)
        {
            new JEightPuzzleFrame("Sliding Puzzle", "FGCU_logo.png", Integer.parseInt(args[0]));
        }
        else
        {
            new JEightPuzzleFrame("Sliding Puzzle","FGCU_logo.png");
        }
    }
    
    JEightPuzzleFrame(String title, String path, int size)
    {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        this.size = size;
        this.buttons = new JButton[size*size - 1];
        this.board = new int[size][size];
        
        try
        {
            setButtons(path);
        }
        catch(FileNotFoundException e0)
        {
            System.out.println("File not found.");
            System.exit(1);
        }
    
        //Initializes the array with values.
        int sum = 1;
        for(int i = 0; i < size; i++)
        {
            for(int j = 0; j < size; j++)
            {
                board[i][j] = sum++;
            }
        }
        //sets bottom right
        board[size - 1][size - 1] = 0;
    
        //Does the initial, simple shuffle for the first game.
        startShuffle();
    
        //Builds the JPanel.
        build();
        container.setLayout(new GridLayout(size, size,0,0));
        add(container);
        setVisible(true);
        revalidate();
        repaint();
    }
    
    JEightPuzzleFrame(String title, String path)
    {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try
        {
            setButtons(path);
        }
        catch(FileNotFoundException e0)
        {
            System.out.println("File not found.");
            System.exit(1);
        }
        
        //Initializes the array with values.
        int sum = 1;
        for(int i = 0; i < size; i++)
        {
            for(int j = 0; j < size; j++)
            {
                board[i][j] = sum++;
            }
        }
        //sets bottom right
        board[size - 1][size - 1] = 0;
        
        //Does the initial, simple shuffle for the first game.
        startShuffle();
        
        //Builds the JPanel.
        build();
        container.setLayout(new GridLayout(size, size,0,0));
        add(container);
        setVisible(true);
        revalidate();
        repaint();
    }

    //Shuffles it simply. Works for every size slider puzzle.
    private void startShuffle()
    {
        for(int i = size - 1; i >= 0; i--)
        {
            for(int j = 0; j < size; j++)
            {
                if(i % 2 == size % 2)
                {
                    swapWithoutCheck(j, i);
                }
                else
                {
                    swapWithoutCheck(size - 1 - j,i);
                }
            }
        }
    }

    //Randomly shuffles the board.
    private void shuffle()
    {
        Random x = new Random(), y = new Random();
        for(int i = 0; i < 10000; i++)
        {
            swapWithoutCheck(x.nextInt(size), y.nextInt(size));
        }
    }

    //Searches for the button that called it and swaps that element.
    private void searchAndSwap(String button)
    {
        for(int i = 0; i < size; i++)
        {
            for(int j = 0; j < size; j++)
            {
                if(Integer.parseInt(button) == (board[i][j] - 1))
                {
                    swap(j, i);
                    return;
                }
            }
        }
    }
    
    //Swaps values using swapWithoutCheck, then checks the solution.
    private void swap(int x1, int y1)
    {
        swapWithoutCheck(x1, y1);
        checkSolution();
    }
    
    /**
     * Checks whether a given point can be swapped with the empty pane.
     *
     * @param x1
     * @param y1
     */
    
    //Will swap values without checking. For use when shuffling board.
    private void swapWithoutCheck(int x1, int y1)
    {
        int y2 = 20, x2 = 20;
    
        //Finds location of empty pane.
        for(int i = 0; i < size; i++)
        {
            for(int j = 0; j < size; j++)
            {
                if(board[j][i] == 0)
                {
                    x2 = i;
                    y2 = j;
                }
            }
        }
    
        //checks whether empty pane is adjacent to clicked button.
        if(distance(x1, y1, x2, y2) <= 1)
        {
            board[y2][x2] = board[y1][x1];
            board[y1][x1] = 0;
            build();
        }
    }
    
    /**
     * Distance function. Easier or more efficient than checking on all sides.
     *
     * @param x1 first point's x variable
     * @param y1 first point's y variable
     * @param x2 second point's x variable
     * @param y2 secont point's y variable
     * @return returns the distance between point (x1, y1) and (x2, y2)
     */
    private double distance(double x1, double y1, double x2, double y2)
    {
        return  sqrt(Math.pow((x1-x2), 2) + Math.pow((y1-y2), 2));
    }

    //Sets the buttons up with images.
    private void setButtons(String path) throws FileNotFoundException
    {
        try
        {
            BufferedImage image1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream(path));
            setSize(image1.getWidth(),image1.getHeight());
    
            int count = 0;
            BufferedImage buttonImage;
            ImageIcon ico;
            int width = image1.getWidth() / size;
            int height = image1.getHeight() / size;
            for(int i = 0; i < size; i++)
            {
                for(int j = 0; j < size; j++)
                {
                    if(count < size * size - 1)
                    {
                        buttonImage = image1.getSubimage(j * width, i * height, width, height);
                        ico = new ImageIcon(buttonImage);
                        buttons[count] = new JButton();
                        buttons[count].setIcon(ico);
                        buttons[count].setSize(width, height);
                        buttons[count].setActionCommand("" + count); //Sets the action command to be the index of the button.
                        buttons[count].addActionListener(new ActionListener()
                        {
                            @Override
                            public void actionPerformed(ActionEvent e)
                            {
                                searchAndSwap(e.getActionCommand()); //Passes the index of the button and sends it to the search function.
                            }
                        });
                        buttons[count++].isVisible();
                    }
                    else
                    {
                        emptyButton.setSize(width, height);
                        emptyButton.isVisible();
                    }
                }
            }
        }
        catch(IOException e0)
        {
            System.out.println("File invalid; cancelling program.");
            System.exit(1);
        }
    }
    
    //Uses the container to add all elements, then revalidate.
    private void build()
    {
        container.removeAll();
        for(int i = 0; i < size; i++)
        {
            for(int j = 0; j < size; j++)
            {
                if(board[i][j] > 0)
                {
                    container.add(buttons[board[i][j] - 1]);
                }
                else
                {
                    container.add(emptyButton);
                }
            }
        }
        revalidate();
    }

    //Checks to make sure each number in the array is in its proper place. If
    // all array items are in their proper place, the puzzle is solved.
    private void checkSolution()
    {
        boolean solved = true;
        int sum = 1;
        for(int i = 0; i < size; i++)
        {
            for(int j = 0; j < size; j++)
            {
                if(board[i][j] != sum++ && i + j != 2 * (size - 1))
                {
                    solved = false;
                }
            }
        }
        if(solved)
        {
            int input = JOptionPane.showConfirmDialog(null,"Congratulations! You've won!" +
                    "\n\nWould you like to play again?", "Play again?", JOptionPane.YES_NO_OPTION);
            if(input == JOptionPane.OK_OPTION)
            {
                shuffle();
            }
            else
            {
                System.exit(0);
            }
        }
    }
}