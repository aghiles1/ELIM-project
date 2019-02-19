package fr.polytech.unice.si5.kmeans; /**************************************************************************
 * @author Jason Altschuler
 * 
 * @tags machine learning, computer vision, data mining
 * 
 * PURPOSE: Clusters n-dimensional points.
 * 
 * Algorithm: KMeans++. An improved version of K-Means clustering algorithm.
 * 
 * For full documentation, see readme.txt
 *************************************************************************/


// TODO: plot points for visualization

// TODO: consistency with terms "clustering" and "iteration" 
// (assignment + update step or 'x' number of those)

// TODO: Give user option to define stopping criteria based on time elapsed

import fr.polytech.unice.si5.entity.MockedDB;
import fr.polytech.unice.si5.entity.MushroomFound;
import fr.polytech.unice.si5.entity.Position;

import java.io.IOException;
import java.util.*;

public class KMeans {

   /***********************************************************************
    * Data structures
    **********************************************************************/
   // user-defined parameters
   private int k;                // number of centroids
   private double[][] points;    // n-dimensional data points. 

   // optional parameters
   private int iterations;       // number of times to repeat the clustering. Choose run with lowest WCSS
   private boolean pp;           // true --> KMeans++. false --> basic random sampling
   private double epsilon;       // stops running when improvement in error < epsilon
   private boolean useEpsilon;   // true  --> stop running when marginal improvement in WCSS < epsilon
                                 // false --> stop running when 0 improvement
   private boolean L1norm;       // true --> L1 norm to calculate distance; false --> L2 norm

   // calculated from dimension of points[][]
   private int m;                // number of data points   (# of pixels for PhenoRipper)  
   private int n;                // number of dimensions    (# of channels for PhenoRipper)

   // output
   private double[][] centroids; // position vectors of centroids                      dim(2): (k) by (number of channels)
   private int[] assignment;     // assigns each point to nearest centroid [0, k-1]    dim(1): (number of pixels)
   private double WCSS;          // within-cluster sum-of-squares. Cost function to minimize

   // timing information
   private long start;
   private long end;
   
   /***********************************************************************
    * Constructors
    **********************************************************************/
   
   /**
    * Empty constructor is private to ensure that clients have to use the 
    * Builder inner class to create a KMeans object.
    */
   private KMeans() {} 

   /**
    * The proper way to construct a KMeans object: from an inner class object.
    * @param builder See inner class named Builder
    */
   private KMeans(Builder builder) {
      // start timing
      start = System.currentTimeMillis();
      
      // use information from builder
      k = builder.k;
      points = builder.points;
      iterations = builder.iterations;
      pp = builder.pp;
      epsilon = builder.epsilon;
      useEpsilon = builder.useEpsilon;
      L1norm = builder.L1norm;

      // get dimensions to set last 2 fields
      m = points.length;
      n = points[0].length;

      // run KMeans++ clustering algorithm
      run();
      
      end = System.currentTimeMillis();
   }


   /**
    * Builder class for constructing KMeans objects.
    * 
    * For descriptions of the fields in this (inner) class, see outer class.
    */
   public static class Builder {
      // required
      private final int k;
      private final double[][] points;

      // optional (default values given)
      private int iterations     = 10;
      private boolean pp         = true;
      private double epsilon     = .001;
      private boolean useEpsilon = true;
      private boolean L1norm = true;

      /**
       * Sets required parameters and checks that are a sufficient # of distinct
       * points to run KMeans.
       */
      public Builder(int k, double[][] points) {
         // check dimensions are valid
         if (k > points.length)
            throw new IllegalArgumentException("Required: # of points >= # of clusters");
         
         // check that there is a sufficient # of distinct points to run KMeans
         HashSet<double[]> hashSet = new HashSet<double[]>(k);
         int distinct = 0;
      
         for (int i = 0; i < points.length; i++) {
            if (!hashSet.contains(points[i])) {
               distinct++;
               if (distinct >= k)
                  break;
               hashSet.add(points[i]);
            }
         }
         
         if (distinct < k)
            throw new IllegalArgumentException("Required: # of distinct points >= # of clusters");
         
         this.k = k;
         this.points = points;
      }

      
      /**
       * Sets optional parameter. Default value is 50. 
       */
      public Builder iterations(int iterations) {
         if (iterations < 1) 
            throw new IllegalArgumentException("Required: non-negative number of iterations. Ex: 50");
         this.iterations = iterations;
         return this;
      }

      /**
       * Sets optional parameter. Default value is true.
       */
      public Builder pp(boolean pp) {
         this.pp = pp;
         return this;
      }

      /**
       * Sets optional parameter. Default value is .001.
       */
      public Builder epsilon(double epsilon) {
         if (epsilon < 0.0)
            throw new IllegalArgumentException("Required: non-negative value of epsilon. Ex: .001"); 

         this.epsilon = epsilon;
         return this;
      }

      /**
       * Sets optional parameter. Default value is true.
       */
      public Builder useEpsilon(boolean useEpsilon) {
         this.useEpsilon = useEpsilon;
         return this;
      }
      
      /**
       * Sets optional parameter. Default value is true
       */
      public Builder useL1norm(boolean L1norm) {
         this.L1norm = L1norm;
         return this;
      }

      /**
       * Build a KMeans object
       */
      public KMeans build() {
         return new KMeans(this);
      }
   }


   /***********************************************************************
    * KMeans clustering algorithm
    **********************************************************************/

   /** 
    * Run KMeans algorithm
    */
   private void run() {
      // for choosing the best run
      double bestWCSS = Double.POSITIVE_INFINITY;
      double[][] bestCentroids = new double[0][0];
      int[] bestAssignment = new int[0];

      // run multiple times and then choose the best run
      for (int n = 0; n < iterations; n++) {
         cluster();

         // store info if it was the best run so far
         if (WCSS < bestWCSS) {
            bestWCSS = WCSS;
            bestCentroids = centroids;
            bestAssignment = assignment;
         }
      }

      // keep info from best run
      WCSS = bestWCSS;
      centroids = bestCentroids;
      assignment = bestAssignment;
   }


   /**
    * Perform KMeans clustering algorithm once.
    */
   private void cluster() {
      // continue to re-cluster until marginal gains are small enough
      chooseInitialCentroids();
      WCSS = Double.POSITIVE_INFINITY; 
      double prevWCSS;
      do {  
         assignmentStep();   // assign points to the closest centroids

         updateStep();       // update centroids

         prevWCSS = WCSS;    // check if cost function meets stopping criteria
         calcWCSS();
      } while (!stop(prevWCSS));
   }


   /** 
    * Assigns to each data point the nearest centroid.
    */
   private void assignmentStep() {
      assignment = new int[m];

      double tempDist;
      double minValue;
      int minLocation;

      for (int i = 0; i < m; i++) {
         minLocation = 0;
         minValue = Double.POSITIVE_INFINITY;
         for (int j = 0; j < k; j++) {
            tempDist = distance(points[i], centroids[j]);
            if (tempDist < minValue) {
               minValue = tempDist;
               minLocation = j;
            }
         }

         assignment[i] = minLocation;
      }

   }


   /** 
    * Updates the centroids.
    */
   private void updateStep() {
      // reuse memory is faster than re-allocation
      for (int i = 0; i < k; i++)
         for (int j = 0; j < n; j++)
            centroids[i][j] = 0;
      
      int[] clustSize = new int[k];

      // sum points assigned to each cluster
      for (int i = 0; i < m; i++) {
         clustSize[assignment[i]]++;
         for (int j = 0; j < n; j++)
            centroids[assignment[i]][j] += points[i][j];
      }
      
      // store indices of empty clusters
      HashSet<Integer> emptyCentroids = new HashSet<Integer>();

      // divide to get averages -> centroids
      for (int i = 0; i < k; i++) {
         if (clustSize[i] == 0)
            emptyCentroids.add(i);

         else
            for (int j = 0; j < n; j++)
               centroids[i][j] /= clustSize[i];
      }
      
      // gracefully handle empty clusters by assigning to that centroid an unused data point
      if (emptyCentroids.size() != 0) {
         HashSet<double[]> nonemptyCentroids = new HashSet<double[]>(k - emptyCentroids.size());
         for (int i = 0; i < k; i++)
            if (!emptyCentroids.contains(i))
               nonemptyCentroids.add(centroids[i]);
         
         Random r = new Random();
         for (int i : emptyCentroids)
            while (true) {
               int rand = r.nextInt(points.length);
               if (!nonemptyCentroids.contains(points[rand])) {
                  nonemptyCentroids.add(points[rand]);
                  centroids[i] = points[rand];
                  break;
               }
            }

      }
      
   }


   /***********************************************************************
    * Choose initial centroids
    **********************************************************************/
   /**
    * Uses either plusplus (KMeans++) or a basic randoms sample to choose initial centroids
    */
   private void chooseInitialCentroids() {
      if (pp)
         plusplus();
      else
         basicRandSample();
   }

   /** 
    * Randomly chooses (without replacement) k data points as initial centroids. 
    */
   private void basicRandSample() {
      centroids = new double[k][n];
      double[][] copy = points;

      Random gen = new Random();

      int rand;
      for (int i = 0; i < k; i++) {
         rand = gen.nextInt(m - i);
         for (int j = 0; j < n; j++) {
            centroids[i][j] = copy[rand][j];       // store chosen centroid
            copy[rand][j] = copy[m - 1 - i][j];    // ensure sampling without replacement
         }
      }
   }

   /** 
    * Randomly chooses (without replacement) k data points as initial centroids using a
    * weighted probability distribution (proportional to D(x)^2 where D(x) is the 
    * distance from a data point to the nearest, already chosen centroid). 
    */
   // TODO: see if some of this code is extraneous (can be deleted)
   private void plusplus() {
      centroids = new double[k][n];       
      double[] distToClosestCentroid = new double[m];
      double[] weightedDistribution  = new double[m];  // cumulative sum of squared distances

      Random gen = new Random();
      int choose = 0;

      for (int c = 0; c < k; c++) {

         // first centroid: choose any data point
         if (c == 0)
            choose = gen.nextInt(m);

         // after first centroid, use a weighted distribution
         else {

            // check if the most recently added centroid is closer to any of the points than previously added ones
            for (int p = 0; p < m; p++) {
               // gives chosen points 0 probability of being chosen again -> sampling without replacement
               double tempDistance = Distance.L2(points[p], centroids[c - 1]); // need L2 norm here, not L1

               // base case: if we have only chosen one centroid so far, nothing to compare to
               if (c == 1)
                  distToClosestCentroid[p] = tempDistance;

               else { // c != 1 
                  if (tempDistance < distToClosestCentroid[p])
                     distToClosestCentroid[p] = tempDistance;
               }

               // no need to square because the distance is the square of the euclidean dist
               if (p == 0)
                  weightedDistribution[0] = distToClosestCentroid[0];
               else weightedDistribution[p] = weightedDistribution[p-1] + distToClosestCentroid[p];

            }

            // choose the next centroid
            double rand = gen.nextDouble();
            for (int j = m - 1; j > 0; j--) {
               // TODO: review and try to optimize
               // starts at the largest bin. EDIT: not actually the largest
               if (rand > weightedDistribution[j - 1] / weightedDistribution[m - 1]) { 
                  choose = j; // one bigger than the one above
                  break;
               }
               else // Because of invalid dimension errors, we can't make the forloop go to j2 > -1 when we have (j2-1) in the loop.
                  choose = 0;
            }
         }  

         // store the chosen centroid
         for (int i = 0; i < n; i++)
            centroids[c][i] = points[choose][i];
      }   
   }


   /***********************************************************************
    * Cutoff to stop clustering
    **********************************************************************/    

   /**
    * Calculates whether to stop the run
    * @param prevWCSS error from previous step in the run
    * @return
    */
   private boolean stop(double prevWCSS) {
      if (useEpsilon)
         return epsilonTest(prevWCSS);
      else
         return prevWCSS == WCSS; // TODO: make comment (more exact, but could be much slower)
      // could this take infinite amount of time? double compare...
      // I think not because WCSS is calc in same way as prevWCSS (if data structs don't change)
   }

   /**
    * Signals to stop running KMeans when the marginal improvement in WCSS
    * from the last step is small.
    * @param prevWCSS error from previous step in the run
    * @return
    */
   private boolean epsilonTest(double prevWCSS) {
      return epsilon > 1 - (WCSS / prevWCSS);
   }

   /***********************************************************************
    * Utility functions
    **********************************************************************/
   /**
    * Calculates distance between two n-dimensional points.
    * @param x
    * @param y
    * @return
    */
   private double distance(double[] x, double[] y) {
      return L1norm ? Distance.L1(x, y) : Distance.L2(x, y);
   }
   
   private static class Distance {

      /**
       * L1 norm: distance(X,Y) = sum_i=1:n[|x_i - y_i|]
       * <P> Minkowski distance of order 1.
       * @param x
       * @param y
       * @return
       */
      public static double L1(double[] x, double[] y) {
         if (x.length != y.length) throw new IllegalArgumentException("dimension error");
         double dist = 0;
         for (int i = 0; i < x.length; i++) 
            dist += Math.abs(x[i] - y[i]);
         return dist;
      }
      
      /**
       * L2 norm: distance(X,Y) = sqrt(sum_i=1:n[(x_i-y_i)^2])
       * <P> Euclidean distance, or Minkowski distance of order 2.
       * @param x
       * @param y
       * @return
       */
      public static double L2(double[] x, double[] y) {
         if (x.length != y.length) throw new IllegalArgumentException("dimension error");
         double dist = 0;
         for (int i = 0; i < x.length; i++)
            dist += Math.abs((x[i] - y[i]) * (x[i] - y[i]));
         return dist;
      }
   }
   
   /** 
    * Calculates WCSS (Within-Cluster-Sum-of-Squares), a measure of the clustering's error.
    */
   private void calcWCSS() {
      double WCSS = 0;
      int assignedClust;

      for (int i = 0; i < m; i++) {
         assignedClust = assignment[i];
         WCSS += distance(points[i], centroids[assignedClust]);
      }     

      this.WCSS = WCSS;
   }

   /***********************************************************************
    * Accessors
    ***********************************************************************/
   public int[] getAssignment() {
      return assignment;
   }

   public double[][] getCentroids() {
      return centroids;
   }

   public double getWCSS() {
      return WCSS;
   }
   
   public String getTiming() {
      return "KMeans++ took: " + (double) (end - start) / 1000.0 + " seconds";
   }


   public static List<List<MushroomFound>> getKmeans(List<MushroomFound> listMush){
      int degradation = 1;

      int numberMush = listMush.size();

      double[][] points = new double[numberMush][3];
      for (int i = 0; i < numberMush; i++) {
         int deg = listMush.get(i).getDegradation();
         points[i] = new double[]{listMush.get(i).getPosition().getLatitude(), listMush.get(i).getPosition().getLongitude(), listMush.get(i).getType().ordinal()};
         if (degradation < deg) {
            degradation = deg;

         }
      }
      int k = (numberMush - 1)/degradation;
      KMeans clustering = new KMeans.Builder(k, points)
              .iterations(50)
              .pp(true)
              .epsilon(.001)
              .useEpsilon(true)
              .build();

      double[][] centroids = clustering.getCentroids();
      int[] assignment = clustering.getAssignment();

      List<List<MushroomFound>> list = new ArrayList<List<MushroomFound>>();

      for (int i = 0; i < k; i++)
         list.add(new ArrayList());
      for (int i = 0; i < assignment.length; i++)
         list.get(assignment[i]).add(listMush.get(i));

      for (int i = 0; i < k; i++)
         for (int j = 0; j < list.get(i).size(); j++)
            System.out.println(" "+ i + "lat : " + list.get(i).get(j).getPosition().getLatitude() + "  lng : " + list.get(i).get(j).getPosition().getLongitude());
      return list;
   }
   /***********************************************************************
    * Unit testing
    **********************************************************************/
   
   public static void main(String args[]) throws IOException {
   /*   int k = 3;
      double[][] points = {
              {43.741566, 7.336638, 0},
              {43.740698, 7.340842, 0},
              {43.744108, 7.335008, 1},
              {43.745100, 7.342086, 1},
              {43.741684, 7.342950, 1},
              {43.741358, 7.335593, 2},
              {43.743807, 7.333287, 2},
              {43.743053, 7.337476, 2},

              {43.740741, 7.022455, 0},
              {43.739687, 7.015506, 2},
              {43.737454, 7.034209, 0},
              {43.739954, 7.020924, 0},
              {43.734558, 7.024227, 2},
              {43.740729, 7.026414, 1},
              {43.735985, 7.024098, 1},
              {43.742874, 7.030963, 1},
              {43.734937, 7.020153, 1},

              {43.925618, 7.349110, 0},
              {43.927040, 7.358461, 1},
              {43.926175, 7.369100, 0},
              {43.924197, 7.353056, 0},
              {43.916901, 7.350740, 1},
              {43.917581, 7.361893, 1},
              {43.931738, 7.339415, 1},
              {43.932814, 7.353167, 2},
              {43.924530, 7.359000, 2}
      };
      KMeans clustering = new KMeans.Builder(k, points)
                                    .iterations(50)
                                    .pp(true)
                                    .epsilon(.001)
                                    .useEpsilon(true)
                                    .build();

      double[][] centroids = clustering.getCentroids();
      int[] ass = clustering.getAssignment();
      List<List<Position>> list = new ArrayList<List<Position>>();

     for (int i = 0; i < k; i++)
         list.add(new ArrayList());
      for (int i = 0; i < ass.length; i++)
         list.get(ass[i]).add(new Position(points[i][1],points[i][0]));

      for (int i = 0; i < k; i++)
         for (int j = 0; j < list.get(i).size(); j++)
            System.out.println(" "+ i + "lat : " + list.get(i).get(j).getLatitude() + "  lng : " + list.get(i).get(j).getLongitude());
*/
       getKmeans(new MockedDB().getMushroomsPos());
   }



}