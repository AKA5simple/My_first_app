package com.example.myapplication.data;

import static org.junit.Assert.*;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.myapplication.R;
import com.example.myapplication.data.DataSaver;
import com.example.myapplication.data.ShopItem;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.manipulation.Ordering;

import java.util.ArrayList;

public class DataSaverTest {
    DataSaver dataSaverBackup;
    ArrayList<ShopItem> shopItemsBackup;

    @Before
    public void setUp() throws Exception {
        dataSaverBackup=new DataSaver();
        Context targetContext=InstrumentationRegistry.getInstrumentation().getTargetContext();
        shopItemsBackup=dataSaverBackup.Load(targetContext);
    }

    @After
    public void tearDown() throws Exception {
        Context targetContext=InstrumentationRegistry.getInstrumentation().getTargetContext();
        dataSaverBackup.Save(targetContext,shopItemsBackup);
    }

    @Test
    public void saveAndload(){
        DataSaver dataSaver=new DataSaver();
        Context targetContext=InstrumentationRegistry.getInstrumentation().getTargetContext();
        ArrayList<ShopItem> shopItems=new ArrayList<>();
        ShopItem shopItem=new ShopItem("测试",56.7, R.drawable.funny_3);
        shopItems.add(shopItem);
        shopItem=new ShopItem("正常",12.3,R.drawable.funny_2);
        shopItems.add(shopItem);
        dataSaver.Save(targetContext,shopItems);

        DataSaver dataLoader=new DataSaver();
        ArrayList<ShopItem> shopItemsRead=dataSaver.Load(targetContext);
        Assert.assertEquals(shopItems.size(),shopItemsRead.size());
        for(int index=0;index<shopItems.size();++index)
        {
            Assert.assertEquals(shopItems.get(index).getTitle(),shopItemsRead.get(index).getTitle());
            Assert.assertEquals(shopItems.get(index).getPrice(),shopItemsRead.get(index).getPrice(),1e-2);
            Assert.assertEquals(shopItems.get(index).getResourceId(),shopItemsRead.get(index).getResourceId());
        }
    }
}