import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.Border;

import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.*;
import java.awt.geom.*;
import java.awt.Point;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.applet.*;
import java.awt.event.*;



public class ProjectFrame extends JFrame {
  CH_Algorithms algs = new CH_Algorithms();

  DrawingPanel drawingPanel;
  JPanel buttonPanel1 = new JPanel();
  JPanel buttonPanel2 = new JPanel();
  JPanel buttonPanel3 = new JPanel();
  JPanel buttonPanel4 = new JPanel();

  //non existent buttons to test brute and incremental from CH_Algorithms
  JButton btnBrute = new JButton("Brute Force");
  JButton btnIncrement = new JButton("Incremental");
  
  // Buttons on the interface
  JButton btnInstant = new JButton("Compute Convex Hull");
  JButton btnBruteStep = new JButton("Brute Force");
  JButton btnIncrementStep = new JButton("Incremental");
  JButton btnGiftwrapStep = new JButton("Gift Wrap");

  JButton btnTestAll = new JButton("Time All 3");
  JButton btnClear = new JButton("Clear");

  ArrayList<Point> points = (ArrayList<Point>) new ArrayList<Point>(); 
  ArrayList<Point> pointsCH = (ArrayList<Point>) new ArrayList<Point>(); 
  ArrayList<Line2D> edgesCH = (ArrayList<Line2D>) new ArrayList<Line2D>();
  ArrayList<Line2D> edgesGhost = (ArrayList<Line2D>) new ArrayList<Line2D>();
  ArrayList<Line2D> edgesGreen = (ArrayList<Line2D>) new ArrayList<Line2D>();

  // global JLabels to be modified later.
  JLabel textTimes = new JLabel("<html><strong>How much longer does it take to compute compared to GiftWrap? :</strong><br>Brute Force: " + "?" + " times longer<br>Incremental: "
      + "?" + " times longer<br>Giftwrap: " + 1 + " times longer... duh! </html>");

  JLabel textStep = new JLabel("<html>or Show Step by Step: </html>");
  JLabel textStatus = new JLabel("<html>Ready to Compute! Start by clicking points in the red area below!</html>");



  public ProjectFrame(String title) {
    super(title);
    drawingPanel = new DrawingPanel(points, pointsCH, edgesCH, edgesGhost, edgesGreen);

    Border borderBlack = BorderFactory.createLineBorder(Color.black);
    
    //textStatus.setBorder(borderBlack);
    //buttonPanel1.setBorder(borderBlack);
    buttonPanel1.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
    //buttonPanel2.setBorder(borderBlack);
    buttonPanel2.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
    //buttonPanel3.setBorder(borderBlack);
    drawingPanel.setBorder(BorderFactory.createLineBorder(Color.red, 4));
    //textTimes.setBorder(borderBlack);
    
    textStatus.setForeground(Color.BLUE);
    textStatus.setFont(new Font("Verdana", 1, 20));
    textTimes.setFont(new Font("Verdana", 0, 13));
    btnClear.setPreferredSize(new Dimension(80, 50));

    setLayout(new GridBagLayout());
    GridBagConstraints gBC = new GridBagConstraints();
    gBC.fill = GridBagConstraints.HORIZONTAL;

    // 1st row, Status
    //gBC.anchor = GridBagConstraints.NORTH;
    gBC.insets = new Insets(5, 5, 5, 5);
    gBC.gridwidth = 2;
    gBC.gridx = 0;
    gBC.gridy = 0;
    gBC.weighty = 1;
    textStatus.setHorizontalAlignment(JLabel.CENTER);
    //textStatus.setVerticalAlignment(JLabel.CENTER);
    add(textStatus, gBC);

    // 2nd row, "Instant Convex Hull"
    gBC.insets = new Insets(0, 0, 0, 0);
    gBC.weighty = 0;
    gBC.gridwidth = 2;
    gBC.gridx = 0;
    gBC.gridy = 1;
    add(buttonPanel1, gBC);
    
    // 3rd Row, Step by Step Convex Hulls, 3 buttons.
    gBC.anchor = GridBagConstraints.CENTER;
    gBC.weighty = 0.0;
    gBC.gridx = 0;
    gBC.gridy = 2;
    add(buttonPanel2, gBC);
    buttonPanel2.add(textStep, gBC);

    // 4th Row, Test All and Time Text
    gBC.anchor = GridBagConstraints.EAST;
    gBC.weightx = 0;
    gBC.weighty = 0.3;
    gBC.gridwidth = 1;
    gBC.gridx = 0;
    gBC.gridy = 3;
    //add(buttonPanel3, gBC);

    gBC.anchor = GridBagConstraints.CENTER;
    textTimes.setHorizontalAlignment(JLabel.CENTER);
    gBC.gridx = 1;
    gBC.gridy = 3;
    //add(textTimes, gBC);

    // 5th Row, Drawing Panel.
    gBC.weighty = 0.0;
    gBC.gridx = 0;
    gBC.gridy = 4;
    gBC.gridwidth = 3;
    gBC.ipady = 400;
    add(drawingPanel, gBC);

    // 6th Row, Clear button.
    gBC.gridx = 0;
    gBC.gridy = 5;
    gBC.ipady = 0;
    add(buttonPanel4, gBC);

    setSize(new Dimension(1000, 800));
    setLocation(400, 100);
    
    createMenubar();
    createButtons();
    
  }

  public void disableButtons() {
    btnInstant.setEnabled(false);
    btnBruteStep.setEnabled(false);
    btnIncrementStep.setEnabled(false);
    btnGiftwrapStep.setEnabled(false);
    btnTestAll.setEnabled(false);
    btnClear.setEnabled(false);
    textStatus.setText("<html>Please Wait... In Progress</html>");
    textStatus.setForeground(Color.RED);
  }

  public void enableButtons() {
    btnInstant.setEnabled(true);
    btnBruteStep.setEnabled(true);
    btnIncrementStep.setEnabled(true);
    btnGiftwrapStep.setEnabled(true);
    btnTestAll.setEnabled(true);
    btnClear.setEnabled(true);
    textStatus.setText("<html>Ready to Compute! Start by clicking points in the area below!</html>");
    textStatus.setForeground(Color.BLUE);

  }

  ActionListener myActionListener = new ActionListener() {
    public void actionPerformed(ActionEvent e) {
      disableButtons();
    }
  };

  public void createButtons() {

    btnIncrementStep.addActionListener(myActionListener);
    btnGiftwrapStep.addActionListener(myActionListener);
    btnBruteStep.addActionListener(myActionListener);

    // buttonPanel.add(btnBrute);
    // buttonPanel.add(btnIncrement);
    buttonPanel1.add(btnInstant);
    buttonPanel2.add(btnBruteStep);
    buttonPanel2.add(btnIncrementStep);
    buttonPanel2.add(btnGiftwrapStep);
    buttonPanel3.add(btnTestAll);
    buttonPanel4.add(btnClear); 
    
    // button for Brute Force
    btnBrute.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        clearAllShapes();
        ArrayList<Line2D> bruteEdges = algs.bruteForce(points);
        edgesCH.addAll(bruteEdges);
        repaint();
      }
    });

    // Button for Incremental Algorithm
    btnIncrement.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        clearAllShapes();
        ArrayList<Point> incArray = algs.incremental(points);
        pointsCH.addAll(incArray);
        repaint();
      }
    });

    // button for Gift Wrap Algorithm, used as INSTANT.
    btnInstant.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        clearAllShapes();
        ArrayList<Point> giftArray = algs.giftWrap(points);
        pointsCH.addAll(giftArray);
        repaint();
        enableButtons();
      }
    });


    // button for Brute Force STEP Algorithm

    btnBruteStep.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {

        Thread brute = new Thread() {
          public void run() {
            clearAllShapes();

            ArrayList<Point> pointsCH = new ArrayList<Point>();
            ArrayList<Point> points2 = points;
            int time = 300;
            int size = points2.size();
            if (size < 3) {
              // do nothing
            }else{
            //points2 = algs.mergeSort(points2);

            pointsCH.add(points2.get(0));
            for (int i = 0; i < size - 1; i++) {
              for (int j = i + 1; j < size; j++) {
                int o = 0;
                if (i == 0) {
                  if (j == 1) {
                    o = 2;
                  } else {
                    o = 1;
                  }
                } else {
                };

                edgesGhost.clear();
                Line2D currGhostEdge = new Line2D.Double(points2.get(i), points2.get(j));
                edgesGhost.add(currGhostEdge);
                paintThenRest(time);

                boolean isCH = true;

                for (int k = 1; k < size; k++) {
                  if ((k == i) || (k == j)) {
                  } else {
                    if (algs.ccw(points2.get(i), points2.get(j), points2.get(o))) {
                      if (algs.ccw(points2.get(i), points2.get(j), points2.get(k))) {

                      } else {
                        isCH = false;
                      }

                    } else {
                      if (algs.cw(points2.get(i), points2.get(j), points2.get(k))) {

                      } else {
                        isCH = false;
                      }

                    }
                  }
                }
                if (isCH) {
                  Line2D tempLine = new Line2D.Double(points2.get(i), points2.get(j));
                  edgesCH.add(tempLine);
                  paintThenRest(time);
                } else {
                }

              }
              edgesGhost.clear();
              repaint();
            }
            } // end else statement
            paintThenRest(time);
            enableButtons();

          } // end public void run()

        }; // end new Thread()
        
        brute.start();

      } // end actionPerformed()
    }); // end actionListener



    // Button for Incremental STEP Algorithm
    btnIncrementStep.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        Thread incremental = new Thread() {
          public void run() {

            clearAllShapes();

            ArrayList<Point> points2 = points;
            int size = points2.size();
            int time = 300;

            if (size < 3) {
              // do nothing
            } else {
              points2 = CH_Algorithms.mergeSort(points2);

              pointsCH.add(points2.get(0));

              if (CH_Algorithms.ccw(points2.get(0), points2.get(1), points2.get(2))) {
                pointsCH.add(points2.get(1));
                pointsCH.add(points2.get(2));
              } else {
                pointsCH.add(points2.get(2));
                pointsCH.add(points2.get(1));
                points2.set(1, pointsCH.get(2));
                points2.set(2, pointsCH.get(1));

              }
              paintThenRest(time);
              for (int i = 3; i < size; i++) {

                Point newp = points2.get(i);
                Point upper = CH_Algorithms.findUpperTangent(pointsCH, newp);
                Point lower = CH_Algorithms.findLowerTangent(pointsCH, newp);

                int indexL = pointsCH.lastIndexOf(lower);
                int indexU = pointsCH.lastIndexOf(upper);

                if (upper == points2.get(0)) {

                  pointsCH.subList(indexL + 1, pointsCH.size()).clear();
                  pointsCH.add(newp);

                } else if (lower == points2.get(0)) {
                  pointsCH.subList(1, indexU).clear();
                  pointsCH.add(1, newp);

                } else {
                  pointsCH.subList(indexL + 1, indexU).clear();
                  pointsCH.add(indexL + 1, newp);
                }

                paintThenRest(time);

              } // ends for loop
            }; // ends else statement for size > 3.
            paintThenRest(time);
            enableButtons();
          }
        };
        incremental.start();

      }
    });


    // Button for Gift Wrap Step Algorithm
    btnGiftwrapStep.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        Thread giftWrap = new Thread() {
          public void run() {

            clearAllShapes();
            ArrayList<Point> points2 = points;
            int time = 200;
            
            int size = points2.size();
            if (size < 3) {
              // do nothing
            } else {

              int q1 = 0;
              for (int i = 1; i < size; i++) {
                if (points2.get(i).getY() < points2.get(q1).getY()) {
                  q1 = i;
                } else {
                }
              }
              int bottom = q1;
              int q2 = -1;
              pointsCH.add(points2.get(q1));
              paintThenRest(time);


              while (q2 != bottom) {
                edgesGreen.clear();
                q2 = (q1 + 1) % size;
                Line2D currGuess = new Line2D.Double(points2.get(q1), points2.get(q2));
                edgesGreen.add(currGuess);

                paintThenRest(time);

                for (int i = 0; i < size; i++) {
                  if (q2 != i) {
                    Line2D tempLines = new Line2D.Double(points2.get(q2), points2.get(i));
                    edgesGhost.clear();
                    edgesGhost.add(tempLines);

                    paintThenRest(time);

                    if (CH_Algorithms.ccw(points2.get(q1), points2.get(q2), points2.get(i))) {
                    } else {
                      q2 = i;
                      edgesGreen.clear();
                      Line2D currGuess2 = new Line2D.Double(points2.get(q1), points2.get(q2));
                      edgesGreen.add(currGuess2);

                    }
                  } else {
                  }
                }
                // pointsCH.add(points2.get(q2));
                edgesGreen.clear();
                Line2D currEdgeCH = new Line2D.Double(points2.get(q1), points2.get(q2));
                edgesCH.add(currEdgeCH);

                paintThenRest(time);

                q1 = q2;
              }
            }
            edgesGhost.clear();
            paintThenRest(time);
            enableButtons();

          }
        };
        giftWrap.start();
      }
    });

    // Button for running all algorithms and displaying run times.
    btnTestAll.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {

        // variables to measure time elapsed to run each algorithm.
        long startTime;
        long endTime;
        long elapsedTime = 0;
        int brute, inc;
        Long temp;
        // Run each algorithm, and measure running time.
        
        // Gift Wrap
        startTime = System.nanoTime();
        ArrayList<Point> giftArray = algs.giftWrap(points);
        endTime = System.nanoTime();
        elapsedTime = endTime - startTime;
        long gift = TimeUnit.MICROSECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS);
        
        // Brute Force
        startTime = System.nanoTime();
        ArrayList<Line2D> bruteArray = algs.bruteForce(points);
        endTime = System.nanoTime();
        elapsedTime = endTime - startTime;
        temp = TimeUnit.MICROSECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS);
        temp = temp / gift;
        brute = temp.intValue();
        
        // Incremental
        startTime = System.nanoTime();
        ArrayList<Point> incArray = algs.incremental(points);
        endTime = System.nanoTime();
        elapsedTime = endTime - startTime;
        temp = TimeUnit.MICROSECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS);
        temp = temp / gift;
        inc = temp.intValue();
        


        // modify Jlabel to display run times for each CH algorithm.
        textTimes.setText("<html><strong>How much longer does it take to compute compared to GiftWrap? :</strong><br>Brute Force: " + brute + " times longer<br>Incremental: "
            + inc + " times longer<br>Giftwrap: " + 1 + " times longer... duh! </html>");

        // use Brute Force method to find CH and paint it.
        edgesCH.clear();
        edgesCH.addAll(bruteArray);

        paintThenRest(200);
        enableButtons();

      }

    });

    btnClear.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        // clear input
        points.clear();
        clearAllShapes();

        repaint();
      }
    });
    
  }

  public void paintThenRest(int n) {
    repaint();

    try {
      Thread.sleep(n);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void clearAllShapes() {
    pointsCH.clear();
    edgesCH.clear();
    edgesGhost.clear();
    edgesGreen.clear();
    
  }

  public void createMenubar() {
    JMenuBar menubar = new JMenuBar();

    JMenu fileM = new JMenu("File");
    fileM.setMnemonic(KeyEvent.VK_F);

    // Clear menu item
    JMenuItem clearMI = new JMenuItem("Clear");
    clearMI.setMnemonic(KeyEvent.VK_L);
    clearMI.setToolTipText("Save input");
    clearMI.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        // clear input
        points.clear();
        clearAllShapes();

        repaint();
      }
    });
    fileM.add(clearMI);

    // Load menu item
    JMenuItem loadMI = new JMenuItem("Load");
    loadMI.setMnemonic(KeyEvent.VK_L);
    loadMI.setToolTipText("Save input");
    loadMI.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        // load input

        String filename;
        System.out.print("Please enter name for graph file: ");
        Scanner scanner = new Scanner(System.in);
        filename = scanner.nextLine();

        try {
          scanner = new Scanner(new File(filename));
          int x, y, n = 0;

          while (scanner.hasNext()) {
            // New point
            x = (int) scanner.nextDouble();
            y = (int) scanner.nextDouble();
            n++;
            System.out.println("Point " + x + "," + y);
            points.add(new Point(x, y));
          } // while
        } catch (FileNotFoundException e) {
          System.err.println("Could not find file " + filename + ". " + e);
          System.exit(0);
        } catch (IOException e) {
          System.err.println("Error reading integer from file " + filename + ". " + e);
          System.exit(0);
        }
        repaint();
      }
    });
    fileM.add(loadMI);


    // Save menu item
    JMenuItem saveMI = new JMenuItem("Save");
    saveMI.setMnemonic(KeyEvent.VK_S);
    saveMI.setToolTipText("Save input");
    saveMI.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        // save input

        String filename;
        System.out.print("Please enter name to save graph file: ");
        Scanner scanner = new Scanner(System.in);
        filename = scanner.nextLine();

        try {
          PrintWriter writer = new PrintWriter(filename);
          for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            writer.print(point.getX());
            writer.print(" ");
            writer.println(point.getY());
          }
          writer.close();
        } catch (FileNotFoundException e) {
          System.err.println("Could not find file " + filename + ". " + e);
          System.exit(0);
        } catch (IOException e) {
          System.err.println("Error reading integer from file " + filename + ". " + e);
          System.exit(0);
        }
      }
    });
    fileM.add(saveMI);

    // Exit menu item
    JMenuItem exitMI = new JMenuItem("Exit");
    exitMI.setMnemonic(KeyEvent.VK_E);
    exitMI.setToolTipText("Exit application");
    exitMI.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        System.exit(0);
      }
    });
    fileM.add(exitMI);


    menubar.add(fileM);
    setJMenuBar(menubar);

  }


  public static void main(String[] args) {
    ProjectFrame frame = new ProjectFrame("Project Frame");
    frame.setVisible(true); // displays the frame
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InstantiationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (UnsupportedLookAndFeelException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
