package kz.zhandos.machinelearning.mybatis;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface SearchMapper {

  @Insert("insert into wordlocation(urlid,wordid,location)" + //
      "values(#{urlid},#{wordid},#{location})")
  public void insWordLocation();

  @Insert("insert into wordlist(word) values(#{word})")
  public void insWord(@Param("word") String word);

  @Select("select * from wordlist")
  public List<HashMap<?, ?>> wordList();


}
