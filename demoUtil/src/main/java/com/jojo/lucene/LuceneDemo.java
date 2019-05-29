package com.jojo.lucene;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class LuceneDemo {

	/**
	 * 索引存储目录
	 */
	private static final String INDEX_DIR = "D:\\Workspace\\test\\lucene\\index";

	/**
	 * 要索引的文件
	 */
	private static final String DATA_DIR = "D:\\Workspace\\test\\BoxCarChildren";

	public static void createIndex() throws Exception {
		// 创建分词器
		Analyzer standardAnalyzer = new StandardAnalyzer();

		// 保存索引的类
		IndexWriter indexWriter = null;
		// 配置
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(standardAnalyzer);
		indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
		// 索引存储路径

		indexWriter = new IndexWriter(getIndexDirectory(), indexWriterConfig);
		System.out.println("IndexWriter初始化完毕");

		// 开始添加索引
		// 文本存储目录
		File dataDir = new File(DATA_DIR);
		for (File file : dataDir.listFiles()) {
			long startTime = System.currentTimeMillis();
			Document document = new Document();
			// 路径
			String name = "path";
			String value = file.getAbsolutePath();
			Field pathField = new StringField(name, value, Field.Store.YES);
			document.add(pathField);

			// 内容
			name = "content";
			Reader reader = Files.newReader(file, Charsets.UTF_8);
			Field contentField = new TextField(name, reader);
			document.add(contentField);

			indexWriter.addDocument(document);
			IOUtils.closeQuietly(reader);
			long endTime = System.currentTimeMillis();
			System.out.println("文件" + file.getName() + "索引创建完毕，耗时：" + (endTime - startTime) + "ms");
		}
		// 必须关闭，不然索引无法写入文件
		IOUtils.closeQuietly(indexWriter);
		System.out.println("writer关闭");
	}

	/**
	 * 索引目录
	 * 
	 * @return
	 * @throws IOException
	 */
	private static Directory getIndexDirectory() throws IOException {
		Path indexPath = Paths.get(INDEX_DIR);
		Directory directory = FSDirectory.open(indexPath);
		return directory;
	}

	public static void main(String[] args) throws Exception {
		IndexReader reader = DirectoryReader.open(getIndexDirectory());

		IndexSearcher searcher = new IndexSearcher(reader);

		Analyzer analyzer = new StandardAnalyzer(); // 标准分词器
		String searchField = "content";
		String q = "We have a grandfather in Greenfield, but we don’t like him,";
		QueryParser parser = new QueryParser(searchField, analyzer);
		Query query = parser.parse(q);
		TopDocs hits = searcher.search(query, 100);
		System.out.println("匹配 " + q + "查询到" + hits.totalHits + "个记录");
		int i = 0;
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.doc(scoreDoc.doc);
			System.out.println((++i)+"   "+doc.get("path"));
		}

	}
}
