package com.wlznsb.iossupersign.dao;

import com.wlznsb.iossupersign.entity.Distribute;
import com.wlznsb.iossupersign.util.GetIpaInfoUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class DistributeDaoTest {

    @Autowired
    private DistributeDao distributeDao;

    @Test
    void add() {
        Distribute distribute = new Distribute(null, "123", "123", "123", "111", "123", "123", "","123",new Date(), "你好", "1", 0, "123");
      //  System.out.println(distributeDao.add(distribute));
        //System.out.println(distribute.getId());;
    }

    @Test
    void dele() {

        System.out.println(new File("").getAbsolutePath());
       // System.out.println(distributeDao.dele(4));
    }

    @Test
    void query() throws FileNotFoundException {
        Map<String, Object> mapIpa = GetIpaInfoUtil.readIPA("C:\\Users\\xujimu\\Desktop\\qqqq.ipa","C:\\Users\\xujimu\\Desktop\\1.png");
        System.out.println(mapIpa.toString());
    }

    @Test
    void getId() {
        System.out.println(distributeDao.getId());
    }


    @Test
    void updateBuyDownCodeUrl(){
        distributeDao.updateBuyDownCodeUrl("1212121",2,"https://wwqe");
    }
    @Test
    void updateDownCode(){
        distributeDao.updateDownCode("1212121", 2, 1);
    }


}
