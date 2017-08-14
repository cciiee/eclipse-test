package com.itheima.solrj;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.junit.Test;

public class solrJDemo {
 //solrJ的增删改查
    //正式解决
	//我想剞劂冲突 哈哈哈哈

	@Test
	public void testAdd() throws SolrServerException, IOException{
		String baseURL="http://localhost:8080/solr";
		SolrServer solrServer=new HttpSolrServer(baseURL);
		
		SolrInputDocument doc=new SolrInputDocument();
		//添加
		doc.setField("id", 9);
		doc.setField("name", "赵李莹");
		solrServer.add(doc);
		solrServer.commit();
	}
	
	@Test
	public void testDelete() throws SolrServerException, IOException{
		String baseURL="http://localhost:8080/solr";
		SolrServer solrServer=new HttpSolrServer(baseURL);
		
	
		
//		solrServer.deleteById("10");
//根据条件删除
		solrServer.deleteByQuery("name:赵");
		solrServer.commit();
	}
	//查询
	@Test
	public  void testQuery() throws SolrServerException{
		String baseURL="http://localhost:8080/solr";
		SolrServer solrServer=new HttpSolrServer(baseURL);
		//条件对象
		SolrQuery params=new SolrQuery();
		params.set("q", "*:*");
		QueryResponse response = solrServer.query(params);
	    SolrDocumentList docs = response.getResults();
	    long numFound = docs.getNumFound();
	    System.out.println("总条数:"+numFound);
	    for (SolrDocument doc : docs) {
			System.out.println("id"+doc.get("id"));
			System.out.println("name"+doc.get("name"));
		
		}
	}
	
	//复杂查询
	//关键词查询 过滤条件 排序 分页  查询指定域  默认查询的域  高亮
	@Test
	public void Query22() throws SolrServerException{
		String baseURL="http://localhost:8080/solr";
		SolrServer solrServer=new HttpSolrServer(baseURL);
		//子类查询
		SolrQuery params=new SolrQuery();
		params.set("q", "钻石");
		params.set("fq", "product_price:[7 TO 10}");
		params.setSort("product_price",ORDER.desc);
		//分页
		params.setStart(0);
		params.setRows(3);
		params.set("fl", "id,product_name,product_price");
		params.set("df", "product_name");
		//打开高亮开关
		params.setHighlight(true);
		//2.添加高亮的域
		params.addHighlightField("product_name");
		//3.添加高亮前缀后缀
		params.setHighlightSimplePre("<span style='color:red'>");
		
		params.setHighlightSimplePost("</span>");
		
		//执行查询
		QueryResponse response=solrServer.query(params);
		//结果集
		SolrDocumentList docs = response.getResults();
		//4.获取高亮
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		
		
		
		//总条数
		long numFound = docs.getNumFound();
		System.out.println("总条数:"+numFound);
		
		for (SolrDocument doc : docs) {
			System.out.println("id"+doc.get("id"));
			System.out.println("name"+doc.get("product_name"));
			System.out.println("price"+doc.get("product_price"));
			
			Map<String, List<String>> map = highlighting.get(doc.get("id"));
//我要循环打印,我不知道打印哪个id的高亮部分,所以要doc.get("id");
			List<String> list = map.get("product_name");
//得到一个map集合含有一个至n个的高亮域,所以是map集合,当然如果你提前知道要高亮的部分,就直接来"product_name"
			System.out.println("高亮的名称:"+list.get(0));
			
		}
		
		
		
	}
} 
