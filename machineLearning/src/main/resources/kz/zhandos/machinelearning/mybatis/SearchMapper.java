package kz.zhandos.machinelearning.mybatis;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface SearchMapper {

  @Insert("insert into wordlocation(urlid,wordid,location)" + //
      " select #{urlid},#{wordid},#{location} " + //
      " where not exists( select null from wordlocation " + //
      " where urlid=#{urlid} and wordid=#{wordid} and location=#{location})")
  public void insWordLocation(@Param("urlid") Long urlid, @Param("wordid") Long wordid,
      @Param("location") Integer location);

  @Select("select * from wordlist")
  public List<HashMap<?, ?>> wordList();


  @Select("Select rowid from ${table} where ${field}=#{value}")
  public Long getEntryId(@Param("table") String table, @Param("field") String field,
      @Param("value") String value);

  @Select("insert into ${table}(${field}) values (#{value})")
  public void insTableValue(@Param("table") String table, @Param("field") String field,
      @Param("value") String value);


  @Select("select exists (select null from urllist where url=#{url})")
  public Boolean isIndexedUrl(@Param("url") String url);

  @Select("select exists (select null from wordlocation where urlid=#{url})")
  public Boolean isCrawled(@Param("url") String url);

  @Select("select * from ${table}")
  public List<HashMap<?, ?>> getAllFromTable(@Param("table") String table);


  @Select("${sql}")
  public List<HashMap<?, ?>> runSQL(@Param("sql") String sql);


  @Select("select strength from ${table} where fromid=#{fromid} and toid=#{toid}")
  public Double slStrength(@Param("table") String table, @Param("fromid") Long fromid,
      @Param("toid") Long toid);

  @Select("select rowid from ${table} where fromid=#{fromid} and toid=#{toid}")
  public Long slRowID(@Param("table") String table, @Param("fromid") Long fromid,
      @Param("toid") Long toid);

  @Insert("insert into ${table} (fromid,toid,strength) values(#{fromid},#{toid},#{strength}) ")
  public Long insStrength(@Param("table") String table, @Param("fromid") Long fromid,
      @Param("toid") Long toid, @Param("strength") Double strength);

  @Update("update  ${table} set strength=#{strength} where rowid= #{rowid}")
  public Long updStrength(@Param("table") String table, @Param("rowid") Long rowid,
      @Param("strength") Double strength);



  @Update("${sql}")
  public void update(@Param("sql") String sql);

}
