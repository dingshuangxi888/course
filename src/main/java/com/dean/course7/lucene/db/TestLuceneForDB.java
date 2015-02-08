package com.dean.course7.lucene.db;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dean on 15/1/18.
 */
public class TestLuceneForDB {

    private Analyzer analyzer;

    private Directory directory;

    public TestLuceneForDB(Analyzer analyzer, Directory directory) {
        this.analyzer = analyzer;
        this.directory = directory;
    }

    public void createIndex(ResultSet rs) throws Exception {
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LATEST, analyzer);

        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

        if (rs != null) {
            while (rs.next()) {
                Document document = new Document();
                document.add(new TextField("userName", rs.getString("userName"), Field.Store.YES));
                document.add(new TextField("newsContent", rs.getString("newsContent"), Field.Store.YES));
                indexWriter.addDocument(document);
            }
        }

        indexWriter.close();
    }

    public ResultSet getContentsFromDB() throws Exception {
        String sql = "SELECT * FROM news n LEFT JOIN user u ON n.userId = u.userId;";
        Connection conn = DBUtil.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        return pstmt.executeQuery();
    }

    public List<String> search(String keyword) throws Exception {
        List<String> result = new ArrayList<>();
        DirectoryReader directoryReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
        // query by news content
        queryByFieldName("newsContent", keyword, result, indexSearcher);

        // query by user name
        queryByFieldName("userName", keyword, result, indexSearcher);
        directoryReader.close();
        return result;
    }

    private void queryByFieldName(String fieldName, String keyword, List<String> result, IndexSearcher indexSearcher) throws ParseException, IOException {
        QueryParser queryParser = new QueryParser(fieldName, analyzer);
        Query query = queryParser.parse(keyword);
        TopDocs topDocs = indexSearcher.search(query, null, 1000);
        if (topDocs != null && topDocs.scoreDocs != null) {
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document document = indexSearcher.doc(scoreDoc.doc);
                result.add(document.get("newsContent"));
            }
        }
    }

    public void searchAndPrint(String keyword) throws Exception {
        List<String> strs = search(keyword);
        for (String str : strs) {
            System.out.println(str);
        }
    }

    public static void main(String[] args) {

        try {
            Analyzer analyzer = new CJKAnalyzer();
            Directory directory = new RAMDirectory();
            TestLuceneForDB test = new TestLuceneForDB(analyzer, directory);
            ResultSet rs = test.getContentsFromDB();
            test.createIndex(rs);
            test.searchAndPrint("ÖÜ½ÜÂ×½á»é");

            test.searchAndPrint("ÔÃäü");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
