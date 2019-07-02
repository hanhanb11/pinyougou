package han.dhll;


import han.dhll.pojo.TbItem;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-solr.xml")
public class TestPage {
    @Autowired
    private SolrTemplate solrTemplate;

    //测试增加
    @Test
    public void testAdd() {
        for (int i = 0; i < 50; i++) {
            TbItem item = new TbItem();
            item.setId(i+1L);
            item.setBrand("华为");
            item.setCategory("手机");
            item.setGoodsId(1L);
            item.setSeller("华为 2 号专卖店");
            item.setTitle("华为 Mate9"+i);
            item.setPrice(2000d);


            solrTemplate.saveBean("han", item);
            solrTemplate.commit("han");
        }

    }
    //删除
    @Test
    public void dele() throws IOException, SolrServerException {
        solrTemplate.deleteByIds("han", "5");
        solrTemplate.commit("han");
    }


    //查询
    @Test
    public void findOne() throws IOException, SolrServerException {

        Query query = new SimpleQuery("*:*");//查询全部
        Criteria criteria = new Criteria("item_category").contains("手机");
        criteria.and("item_brand").contains("2");
//        query.setOffset(10L);//索引开始
//        query.setRows(10);//每页10条
        ScoredPage<TbItem> page = solrTemplate.queryForPage("han", query, TbItem.class);
        for (TbItem item : page) {
            System.out.println(item.getTitle() +" .... " +item.getPrice()+" ..."+ item.getBrand());
        }
        System.out.println("总记录数"+ page.getTotalElements());
        System.out.println("总页数"+ page.getTotalPages());

    }


    //删除All
    @Test
    public void deleAll() throws IOException, SolrServerException {
        SolrDataQuery query = new SimpleQuery("*:*");
        solrTemplate.delete("han",query);
        solrTemplate.commit("han");
    }



}


