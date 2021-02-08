package com.wlznsb.iossupersign.dao;

import com.wlznsb.iossupersign.entity.Domain;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DomainDaoTest {

    @Autowired
    private DomainDao domainDao;
    @Test
    public void add() {
        Domain domain = new Domain(null,"https://www.baidu.com",new Date(),1);
        System.out.println(domainDao.add(domain));
    }

    @Test
    public void dele() {
        System.out.println(domainDao.dele(1));
    }

    @Test
    public void query() {
        domainDao.queryAll();
    }
}