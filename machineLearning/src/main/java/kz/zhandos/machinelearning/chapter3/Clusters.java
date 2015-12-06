package kz.zhandos.machinelearning.chapter3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;

import kz.zhandos.machinelearning.chapter2.PearsonCorrelation;

class BiCluster {
  Object self;
  List<Double> vec;
  BiCluster left;
  BiCluster right;
  Double distance;
  Integer id;

  BiCluster(Object self, List<Double> vec, BiCluster left, BiCluster right, Double distance,
      Integer id) {
    this.left = left;
    this.right = right;
    this.vec = vec;
    this.id = id;
    this.distance = distance;
  }

  @Override
  public String toString() {
    return "BiCluster [left=" + left + ", right=" + right + ", distance=" + distance + ", id=" + id
        + "]";
  }



}



public class Clusters {
  private final static Logger log = Logger.getLogger(Clusters.class);

  public static Pair<List<String>, Pair<List<String>, List<List<Double>>>> readFile(String filename)
      throws FileNotFoundException {
    Pair<List<String>, Pair<List<String>, List<List<Double>>>> ret = null;
    Scanner s = null;
    log.info(
        "вызов Pair<List<String>, Pair<List<String>, List<List<Double>>>> readFile(String filename)");
    // def readfile(filename):
    // lines=[line for line in file(filename)]
    // # First line is the column titles
    // colnames=lines[0].strip( ).split('\t')[1:]
    // rownames=[]
    // data=[]
    // for line in lines[1:]:
    // p=line.strip( ).split('\t')
    // # First column in each row is the rowname
    // rownames.append(p[0])
    // # The data for this row is the remainder of the row
    // data.append([float(x) for x in p[1:]])
    // return rownames,colnames,data
    try {
      List<String> colnames = new ArrayList<String>();
      List<String> rownames = new ArrayList<String>();
      List<List<Double>> data = new ArrayList<List<Double>>();

      File input = new File(Clusters.class.getResource(filename).getPath());
      s = new Scanner(input);
      int i = 0;
      while (s.hasNextLine()) {
        String[] row = s.nextLine().split("\t");
        if (i == 0) {
          colnames.addAll(Arrays.asList(row));
        } else {
          rownames.add(row[0]);
          List<Double> r = new ArrayList<Double>();
          for (int k = 1; k < row.length; k++) {
            if (row[k].trim().length() > 0)
              r.add(Double.parseDouble(row[k]));
          }
          data.add(r);
        }
        i++;
      }
      s.close();
      Pair<List<String>, List<List<Double>>> p = Pair.pair(colnames, data);
      Pair<List<String>, Pair<List<String>, List<List<Double>>>> pair = Pair.pair(rownames, p);
      ret = pair;
    } catch (Exception e) {
      log.error(e);
    } finally {
      if (s != null)
        s.close();
    }
    log.info(
        "конец вызова Pair<List<String>, Pair<List<String>, List<List<Double>>>> readFile(String filename)");
    return ret;
  }

  public static BiCluster hcluster(List<List<Double>> rows) throws Exception {
    // def hcluster(rows,distance=pearson):
    // distances={}
    // currentclustid=-1
    // # Clusters are initially just the rows
    // clust=[bicluster(rows[i],id=i) for i in range(len(rows))]
    // while len(clust)>1:
    // lowestpair=(0,1)
    // closest=distance(clust[0].vec,clust[1].vec)

    BiCluster ret = null;
    log.info("вызов hcluster(List<List<Double>> rows)");
    try {
      int currentclustid = -1;
      Map<Pair<Integer, Integer>, Double> distances = new HashMap<Pair<Integer, Integer>, Double>();

      Pair<Integer, Integer> lowestPair = null;
      Double closest = null;

      Map<Integer, BiCluster> clust = new HashMap<Integer, BiCluster>();
      List<Double> mergevec = new ArrayList<Double>();
      int id = 0;
      for (List<Double> row : rows) {
        BiCluster biCluster = new BiCluster(null, row, null, null, null, id);
        clust.put(biCluster.id, biCluster);
        id++;
      }

      while (clust.size() > 1) {
        BiCluster b[] = clust.values().toArray(new BiCluster[clust.keySet().size()]);
        lowestPair = Pair.pair(b[0].id, b[1].id);
        closest = PearsonCorrelation.correlation(clust.get(lowestPair.first).vec,
            clust.get(lowestPair.second).vec);


        // # loop through every pair looking for the smallest distance
        // for i in range(len(clust)):
        // for j in range(i+1,len(clust)):
        // # distances is the cache of distance calculations
        // if (clust[i].id,clust[j].id) not in distances:
        // distances[(clust[i].id,clust[j].id)]=distance(clust[i].vec,clust[j].vec)
        // d=distances[(clust[i].id,clust[j].id)]
        // if d<closest:
        // closest=d
        // lowestpair=(i,j)

        for (int i = 0; i < clust.size(); i++) {
          for (int j = i + 1; j < clust.size(); j++) {
            Pair<Integer, Integer> p = Pair.pair(b[i].id, b[j].id);
            if (!distances.containsKey(p)) {
              distances.put(p, PearsonCorrelation.correlation(b[i].vec, b[j].vec));
              double d = distances.get(p);
              if (d < closest)
                closest = d;
              lowestPair = p;
            }
          }
        }

        // # calculate the average of the two clusters
        // mergevec=[
        // (clust[lowestpair[0]].vec[i]+clust[lowestpair[1]].vec[i])/2.0
        // for i in range(len(clust[0].vec))]
        // # create the new cluster
        // newcluster=bicluster(mergevec,left=clust[lowestpair[0]],
        // right=clust[lowestpair[1]],
        // distance=closest,id=currentclustid)
        // # cluster ids that weren't in the original set are negative
        // currentclustid-=1
        // del clust[lowestpair[1]]
        // del clust[lowestpair[0]]
        // clust.append(newcluster)
        // return clust[0]
        for (int i = 0; i < clust.get(lowestPair.first).vec.size(); i++) {
          mergevec.add((clust.get(lowestPair.first).vec.get(i).doubleValue()
              + clust.get(lowestPair.second).vec.get(i).doubleValue()) / 2.0);
        }
        BiCluster newcluster = new BiCluster(null, mergevec, clust.get(lowestPair.first),
            clust.get(lowestPair.second), closest, currentclustid);
        currentclustid -= 1;
        clust.remove(lowestPair.first);
        clust.remove(lowestPair.second);
        clust.put(newcluster.id, newcluster);
      }
      ret = (BiCluster) clust.values().toArray()[0];
    } catch (Exception e) {
      log.error("ee", e);
      throw e;
    }
    log.info("конец вызова hcluster(List<List<Double>> rows)");
    return ret;
  }


  public static void printclust(BiCluster cluster, List<String> labels, Integer n) {
    // def printclust(clust,labels=None,n=0):
    // # indent to make a hierarchy layout
    // for i in range(n): print ' ',
    // if clust.id<0:
    // # negative id means that this is branch
    // print '-'
    // else:
    // # positive id means that this is an endpoint
    // if labels==None: print clust.id
    // else: print labels[clust.id]
    // # now print the right and left branches
    // if clust.left!=None: printclust(clust.left,labels=labels,n=n+1)
    // if clust.right!=None: printclust(clust.right,labels=labels,n=n+1)
    System.out.println();
    if (n == null)
      n = 0;
    for (int i = 0; i < n; i++)
      System.out.print(" ");
    if (cluster.id < 0) {
      System.out.print("-");
    } else {
      if (labels == null)
        System.out.print(cluster.id);
      else
        System.out.print(labels.get(cluster.id));
    }
    if (cluster.left != null) {
      printclust(cluster.left, labels, n + 1);
    }
    if (cluster.right != null)
      printclust(cluster.right, labels, n + 1);
  }



  public static void main(String[] args) throws Exception {
    Pair<List<String>, Pair<List<String>, List<List<Double>>>> p = readFile("blogdata.txt");
    printclust(hcluster(p.second.second), p.first, null);
  }

}
