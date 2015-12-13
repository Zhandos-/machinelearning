package kz.zhandos.machinelearning.chapter4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import kz.zhandos.machinelearning.chapter3.Clusters;
import kz.zhandos.machinelearning.mybatis.SearchDAO;
import kz.zhandos.machinelearning.mybatis.SearchMapper;

public class NN {

  private Double ai[] = null;
  private Double ah[] = null;
  private Double ao[] = null;

  private Double wi[][] = null;
  private Double wo[][] = null;
  List<Integer> hiddenids = null;

  private final static Logger log = Logger.getLogger(Clusters.class);

  private final static SearchDAO DAO = new SearchDAO();
  private final static SearchMapper MAPPER = DAO.getMapper();

  public Double getstrength(Long fromid, Long toid, int layer) {
    String table = layer == 0 ? "wordhidden" : "hiddenurl";
    return MAPPER.slStrength(table, fromid, toid);
  }


  public void setstrength(Long fromid, Long toid, int layer, Double strength) {
    String table = layer == 0 ? "wordhidden" : "hiddenurl";
    Long res = MAPPER.slRowID(table, fromid, toid);
    if (res == null) {
      MAPPER.insStrength(table, fromid, toid, strength);
    } else {
      MAPPER.updStrength(table, res, strength);
    }
  }

  public void generatehiddennode(List<Long> wordids, List<Long> urls) {
    if (wordids.size() > 3)
      return;
    String createkey = "";
    for (Long id : wordids) {
      createkey += "_" + id;
    }
    Long hiddenid = MAPPER.getEntryId("hiddennode", "create_key", createkey);
    if (hiddenid == null) {
      MAPPER.insTableValue("hiddennode", "create_key", createkey);
      DAO.commitChanges();
    }
    hiddenid = MAPPER.getEntryId("hiddennode", "create_key", createkey);

    for (Long id : wordids) {
      setstrength(id, hiddenid, 0, 0.1d / wordids.size());
    }
    for (Long url : urls) {
      setstrength(hiddenid, url, 1, 0.1d);
    }
    DAO.commitChanges();
  }


  public Set<Integer> getAllHiddenIds(List<Long> wordids, List<Long> urls) {
    Map<Integer, Integer> l1 = new HashMap<Integer, Integer>();

    for (Long wordid : wordids) {
      List<HashMap<?, ?>> rows =
          MAPPER.runSQL(String.format("select toid from wordhidden where fromid=%d", wordid));
      for (Map<?, ?> map : rows) {
        l1.put((Integer) map.get("TOID"), 1);
      }
    }
    for (Long urlid : urls) {
      List<HashMap<?, ?>> rows =
          MAPPER.runSQL(String.format("select fromid from hiddenurl where fromid=%d", urlid));
      for (Map<?, ?> map : rows) {
        l1.put((Integer) map.get("FROMID"), 1);
      }
    }
    return l1.keySet();
  }


  public void setupnetwork(List<Long> wordids, List<Long> urls) {
    hiddenids = new ArrayList<Integer>(getAllHiddenIds(wordids, urls));
    ai = new Double[wordids.size()];
    ah = new Double[hiddenids.size()];
    ao = new Double[urls.size()];

    wi = new Double[hiddenids.size()][wordids.size()];

    for (int i = 0; i < hiddenids.size(); i++) {
      for (int k = 0; k < wordids.size(); k++) {
        wi[i][k] = getstrength(wordids.get(k), hiddenids.get(i).longValue(), 0);
      }
    }

    wo = new Double[hiddenids.size()][urls.size()];
    for (int i = 0; i < hiddenids.size(); i++) {
      for (int k = 0; k < urls.size(); k++) {
        wo[i][k] = (getstrength(hiddenids.get(i).longValue(), urls.get(k), 1));
      }
    }
  }

  public Double[] feedforward(List<Long> wordids, List<Long> urls) {
    for (int i = 0; i < wordids.size(); i++) {
      ai[i] = 1.0d;
    }

    for (int j = 0; j < hiddenids.size(); j++) {
      double sum = 0.0d;
      for (int i = 0; i < wordids.size(); i++) {
        sum = sum + ai[i] * (wi[j][i] == null ? 0.0d : wi[j][i]);
      }
      ah[j] = Math.tanh(sum);
    }


    for (int j = 0; j < urls.size(); j++) {
      double sum = 0.0d;
      for (int i = 0; i < hiddenids.size(); i++) {
        sum = sum + ah[i] * (wo[i][j] == null ? 0.0d : wo[i][j]);
      }
      ao[j] = Math.tanh(sum);
    }
    return ao;
  }


  public Double[] getresult(List<Long> wordids, List<Long> urls) {
    setupnetwork(wordids, urls);
    return feedforward(wordids, urls);
  }



  public static void main(String args[]) {
    Long wWorld = 101l, wRiver = 102l, wBank = 103l;
    Long uWorldBank = 201l, uRiver = 202l, uEarth = 203l;

    NN nn = new NN();
    nn.generatehiddennode(Arrays.asList(wWorld, wBank), Arrays.asList(uWorldBank, uRiver, uEarth));
    for (Double d : nn.getresult(Arrays.asList(wWorld, wBank),
        Arrays.asList(uWorldBank, uRiver, uEarth))) {
      System.out.print(d + " ");
    }
  }


}
