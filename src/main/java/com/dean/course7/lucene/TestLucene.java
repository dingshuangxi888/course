package com.dean.course7.lucene;

import net.paoding.analysis.analyzer.PaodingAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
 * Created by dean on 15/1/18.
 */
public class TestLucene {

    private final String fieldName = "text";

    private Analyzer analyzer;

    private Directory directory;

    public TestLucene(Analyzer analyzer, Directory directory) {
        this.analyzer = analyzer;
        this.directory = directory;
    }

    public void indexFiles(String text) throws Exception {
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LATEST, analyzer);

        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

        Document document = new Document();
        document.add(new TextField(fieldName, text, Field.Store.YES));

        indexWriter.addDocument(document);
        indexWriter.close();
    }

    public void searchFiles(String keyword) throws Exception {
        DirectoryReader directoryReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
        QueryParser queryParser = new QueryParser(fieldName, analyzer);
        Query query = queryParser.parse(keyword);
        TopDocs topDocs = indexSearcher.search(query, null, 1000);
        if (topDocs != null && topDocs.scoreDocs != null) {
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document document = indexSearcher.doc(scoreDoc.doc);
                System.out.println(document.get(fieldName));
            }
        }
        directoryReader.close();
    }


    public static void main(String[] args) {
        try {
//            Analyzer analyzer = new StandardAnalyzer();
            Analyzer analyzer = new CJKAnalyzer();
//            Analyzer analyzer = new PaodingAnalyzer();
            Directory directory = new RAMDirectory();
            TestLucene testLucene = new TestLucene(analyzer, directory);

            String text = "ÖÜ½ÜÂ×À¥ÁèÓ¢¹ú´ó»é£¬»éÀñÏÖ³¡Î¨ÃÀÀËÂþ";

            testLucene.indexFiles(text);

            testLucene.searchFiles("½ÜÂ×");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
