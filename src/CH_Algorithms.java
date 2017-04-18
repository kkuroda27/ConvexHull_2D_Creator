import java.awt.Point;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class CH_Algorithms {

  // Brute = O(n^3)
  // Incremental = O(n^2)
  // Gift Wrap = O(hn), h = # of points on CH

  // ///////// BRUTE FORCE ////////

  public static ArrayList<Line2D> bruteForce(ArrayList<Point> points) {
    // initialize output array of lines that represent the Convex Hull
    ArrayList<Line2D> linesCH = new ArrayList<Line2D>();

    int size = points.size();
    if (size < 3) {
      return linesCH;
    }
    // sort points lexicographically
    points = mergeSort(points);

    // Brute force: the next two for loops iterates through every possible pair of points (index i &
    // j) once.
    for (int i = 0; i < size - 1; i++) {
      for (int j = i + 1; j < size; j++) {

        // Using CCW/CW, we want to see if all other points lie exclusively on one side of the line
        // (i,j).
        // To start, we need to test one initial case to test against. This can be any point not i
        // or j.
        // We set this test index "o" to be index 0, 1, or 2, depending on what i or j is.
        int o = 0;
        if (i == 0) {
          if (j == 1) {
            o = 2;
          } else {
            o = 1;
          }
        } else {
        };

        // For each pair, we test every other point (index k) against the line (i,j) to determine if
        // they lie on the same side as the initial case with index "o".
        // a boolean that starts as true, but at the end, if even one case of point k does NOT lie
        // on the same side as the rest, isCH becomes "false" and we know not to include this edge
        // in the CH.
        boolean isCH = true;

        for (int k = 1; k < size; k++) {
          if ((k == i) || (k == j)) {
          } else {
            // check if for the pair i and j, if all other points k lie on the left side.
            if (ccw(points.get(i), points.get(j), points.get(o))) {
              if (ccw(points.get(i), points.get(j), points.get(k))) {
                // if yes, do nothing
              } else {
                isCH = false;
              }
              
            } else {           
              if (cw(points.get(i), points.get(j), points.get(k))) {
                // if yes, do nothing
              } else {
                isCH = false;
              }
              
            }
          }
        }
        if (isCH) {
          Line2D tempLine = new Line2D.Double(points.get(i), points.get(j));
          linesCH.add(tempLine);
        } else {
        }

      }
    }

    return linesCH;

  }

  ///////// INCREMENTAL
  public static ArrayList<Point> incremental(ArrayList<Point> points) {
    // Initialize output CH array of points.
    ArrayList<Point> pointsCH = new ArrayList<Point>();

    int size = points.size();
    if (size < 3) {
      return points;
    }
    // sort points lexicographically.
    points = mergeSort(points);

    // Now we will add the first 3 points to convex hull in CCW order.

    // First, just add the first point from sorted list.
    pointsCH.add(points.get(0));

    // Now for the 2nd and 3rd point, the ccw order depends on the ccw order.
    if (ccw(points.get(0), points.get(1), points.get(2))) {
      pointsCH.add(points.get(1));
      pointsCH.add(points.get(2));
    } else {
      pointsCH.add(points.get(2));
      pointsCH.add(points.get(1));
      // swap points array accordingly
      points.set(1, pointsCH.get(2));
      points.set(2, pointsCH.get(1));

    }

    // now loop for rest of points, incrementally adding one at a time to hull.
    for (int i = 3; i < size; i++) {

      // take the new point.....
      Point newp = points.get(i);

      // Find upper tangent point of existing CH in relation to new point.
      Point upper = findUpperTangent(pointsCH, newp);

      // if upper tangent is beginning point == leftmost point, just make upper the last point
      // before in CH.
      // This is done because our CH array is in ccw order, so having the upper tangent be the
      // beginning point again will screw things up.
      if (upper == points.get(0)) {
        int last = pointsCH.size() - 1;
        upper = pointsCH.get(last);
      }

      // Find lower tangent point of existing CH in relation to new point.
      Point lower = findLowerTangent(pointsCH, newp);

      // Now we will remove points in between only if upper and lower is NOT the same.

      // get index of upper and lower tangent points.
      int indexL = pointsCH.lastIndexOf(lower);
      int indexU = pointsCH.lastIndexOf(upper);

      // CHECK! Do deletions only if upper tangent and lower tangent are not equal
      if (indexL != indexU) {
        pointsCH.subList(indexL + 1, indexU).clear();
        pointsCH.add(indexL + 1, newp);
      } else {
        // Upper and Lower are the same when new point is the last point to add (If the Upper is the
        // last point, we changed it leftmost point previously, and we dont need to remove any
        // points);
        pointsCH.add(newp);
      }

    }

    return pointsCH;

  }

  // method used in incremental to find Upper Tangent point.
  public static Point findUpperTangent(ArrayList<Point> array, Point point) {
    int upper = 0;
    // find upper tangent point that has no other points above the tangent of that point and the new
    // point.
    for (int i = 0; i < array.size(); i++) {

      // if ccw returns true, in other words, the three points are ccw or colinear, which is what we
      // want.
      if (ccw(point, array.get(upper), array.get(i))) {
        // System.out.println("ccw or colinear! keep ");
      } else {
        upper = i;
      }
    }

    return array.get(upper);
  }

  // method used in incremental to find Lower Tangent point.
  public static Point findLowerTangent(ArrayList<Point> array, Point point) {
    int lower = 0;
    // find lower tangent point that has no other points below the tangent of that point and the new
    // point.
    for (int i = 0; i < array.size(); i++) {

      // if cw returns true, in other words, the three points are cw or colinear, which is what we
      // want.
      if (cw(point, array.get(lower), array.get(i))) {
      } else {
        lower = i;
      }
    }
    return array.get(lower);
  }



  // //////////// GIFT WRAPPING
  // //////////////////////////////////////////////////////////////////////////////////////////////////////////////
  public static ArrayList<Point> giftWrap(ArrayList<Point> points) {
    // initialize CH points output array.
    ArrayList<Point> outputList = new ArrayList<Point>();

    int size = points.size();
    if (size < 3) {
      return points;
    }

    // find most bottom point, so find point with smallest y coordinate.
    int q1 = 0;
    for (int i = 1; i < size; i++) {
      if (points.get(i).getY() < points.get(q1).getY()) {
        q1 = i;
      } else {
      }
    }
    int bottom = q1;
    int q2 = -1;
    outputList.add(points.get(q1));

    // now choose next point as q2 and keep finding next vertex via giftwrap.
    // Keep going until the next point is the bottom point, or the one we started with.
    while (q2 != bottom) {
      // q2 = next point in list, but if q1 is the last point, q2 == 1st point.
      q2 = (q1 + 1) % size;
      for (int i = 0; i < size; i++) {

        // if clock wise, keep moving, if not, that current point is a better next point.
        if (ccw(points.get(q1), points.get(q2), points.get(i))) {
        } else {
          q2 = i;
        }
      }

      if (q2 != bottom) {
        // if q2 is not the bottom, add that point.
        outputList.add(points.get(q2));
      } else {
      }
      q1 = q2;
    }
    return outputList;
  }

  // Orientation test: Returns true if all points are ccw or colinear
  public static boolean ccw(Point a, Point b, Point c) {
    int sol = (b.y - a.y) * (c.x - b.x) - (b.x - a.x) * (c.y - b.y);
    if (sol > 0) {
      return false;
    } else {
      return true;
    }
  }

  // Orientation test: Returns true if all points are cw or colinear
  public static boolean cw(Point a, Point b, Point c) {
    int sol = (b.y - a.y) * (c.x - b.x) - (b.x - a.x) * (c.y - b.y);
    if (sol < 0) {
      return false;
    } else {
      return true;
    }
  }

  // /////////// MERGE SORT!
  // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // sort lexographically ( by x first, break ties by y ).
  public static ArrayList<Point> mergeSort(ArrayList<Point> points) {

    if (points.size() <= 1) {
      return points;
    }
    ArrayList<Point> sortedList = new ArrayList<Point>();

    ArrayList<Point> left = new ArrayList<Point>();
    ArrayList<Point> right = new ArrayList<Point>();
    int middle = points.size() / 2;
    // Splits the array into unsortedList size lists of size one
    for (int i = 0; i < points.size(); i++) {
      if (i < middle) {
        left.add(points.get(i));
      } else {
        right.add(points.get(i));
      }
    }
    left = mergeSort(left);
    right = mergeSort(right);
    // combines the lists
    sortedList = merge(left, right);
    return sortedList;
  }

  private static ArrayList<Point> merge(ArrayList<Point> left, ArrayList<Point> right) {
    ArrayList<Point> mergedList = new ArrayList<Point>();
    while (left.size() > 0 || right.size() > 0) {
      if (left.size() > 0 && right.size() > 0) {
        if (left.get(0).getX() < right.get(0).getX()) {
          mergedList.add(left.get(0));
          left.remove(0);
        } else if (left.get(0).getX() > right.get(0).getX()) {
          mergedList.add(right.get(0));
          right.remove(0);
        } else if (left.get(0).getX() == right.get(0).getX()) {
          // if x values are tied, break by y-coordinate.
          if (left.get(0).getY() > right.get(0).getY()) {
            mergedList.add(right.get(0));
            right.remove(0);
          } else {
            mergedList.add(left.get(0));
            left.remove(0);
          }
        }
      } else if (left.size() > 0) {
        mergedList.add(left.get(0));
        left.remove(0);
      } else if (right.size() > 0) {
        mergedList.add(right.get(0));
        right.remove(0);
      }
    }
    return mergedList;
  }

  // ////// PRINT ARRAYLIST OF POINTS //////////////////////////////////////////////////////
  public static void printList(ArrayList<Point> list) {
    for (int i = 0; i < list.size(); i++) {
      Point curr = list.get(i);
      System.out.println(curr.getX() + "," + curr.getY());
    }
  }



  // /////// MAIN
  // //////////////////////////////////////////////////////////////////////////////////////
  public static void main(String[] arguments) {
    ArrayList<Point> array = new ArrayList<Point>();

    Point p1 = new Point(1, 0);
    Point p2 = new Point(2, 2);
    Point p3 = new Point(3, 1);
    Point p4 = new Point(4, 3);
    Point p5 = new Point(3, 3);
    Point p6 = new Point(2, 5);
    Point p7 = new Point(0, 2);

    /*
     * Point p1 = new Point(1,0); Point p2 = new Point(4,0); Point p3 = new Point(5,1); Point p4 =
     * new Point(6,2); Point p5 = new Point(5,4); Point p6 = new Point(3,5); Point p7 = new
     * Point(2,4); Point p8 = new Point(0,3); Point p9 = new Point(2,1); Point p10 = new Point(2,2);
     * Point p11 = new Point(2,3); Point p12 = new Point(3,1); Point p13 = new Point(3,2); Point p14
     * = new Point(3,3);
     */
    array.add(p1);
    array.add(p2);
    array.add(p3);
    array.add(p4);
    array.add(p5);
    array.add(p6);
    array.add(p7);
    /*
     * array.add(p8); array.add(p9); array.add(p10); array.add(p11); array.add(p12); array.add(p13);
     * array.add(p14);
     */

    // System.out.println(array.size());
    printList(array);

    // TEST GIFT WRAP!
    // ArrayList<Point> giftWrappedArray = giftWrap(array);
    // System.out.println("GIFTWRAPPED!");
    // printList(giftWrappedArray);

    // TEST INCREMENTAL!
    // ArrayList<Point> incArray = incremental(array);
    // System.out.println("INCREMENTAL CH!!");
    // printList(incArray);


    // TEST MERGESORT
    ArrayList<Point> array1 = new ArrayList<Point>();

    Point pp1 = new Point(1, 0);
    Point pp2 = new Point(1, 2);
    Point pp3 = new Point(1, 1);
    Point pp4 = new Point(1, 3);
    Point pp5 = new Point(3, 5);

    array1.add(pp1);
    array1.add(pp2);
    array1.add(pp3);
    array1.add(pp4);
    array1.add(pp5);
    // printList(array1);

    // ArrayList<Point> sortedArray = mergeSort(array1);
    // System.out.println("SORT!");
    // printList(sortedArray);

  }

}
