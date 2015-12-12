CREATE TABLE wordlist(
   ROWID INTEGER PRIMARY KEY   AUTOINCREMENT,
   word           TEXT    NOT NULL,
   UNIQUE (word) 
  );
  
  
  CREATE TABLE urllist(
   ROWID INTEGER PRIMARY KEY   AUTOINCREMENT,
   url   TEXT    NOT NULL,
   UNIQUE (url) 
  );
  
  CREATE TABLE link(
   ROWID INTEGER PRIMARY KEY   AUTOINCREMENT,
   FROMID INT    NOT NULL,
   TOID INT    NOT NULL,
   FOREIGN KEY(FROMID) REFERENCES urllist(rowid),
   FOREIGN KEY(TOID) REFERENCES urllist(rowid),
   UNIQUE (ROWID,FROMID,TOID) 
  ); 
  
  CREATE TABLE linkwords(
   LINKID INT NOT NULL,
   WORDID INT NOT NULL,
   FOREIGN KEY(LINKID) REFERENCES link(rowid),
   FOREIGN KEY(WORDID) REFERENCES wordlist(rowid),
   UNIQUE (LINKID,WORDID)
  );
  
  
  CREATE TABLE wordlocation(
   URLID INT NOT NULL,
   WORDID INT NOT NULL,
   LOCATION INT NOT NULL,
   FOREIGN KEY(URLID) REFERENCES urllist(rowid),
   FOREIGN KEY(WORDID) REFERENCES wordlist(rowid),
   UNIQUE (URLID,WORDID,LOCATION) 
  );
  
  CREATE INDEX wordidx on wordlist(word);
  create index urlidx on urllist(url);
  create index wordurlidx on wordlocation(wordid);
  create index urltoidx on link(toid);
  create index urlfromidx on link(fromid);




  
  