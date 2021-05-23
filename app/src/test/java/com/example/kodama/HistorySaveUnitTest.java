package com.example.kodama;

import com.example.kodama.models.PlantCard;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(MockitoJUnitRunner.class)
public class HistorySaveUnitTest {
    @Mock
    PlantCard mockPlantCard;
    ArrayList<PlantCard> mockPlantList;

    @Before
    public void setUp(){
        mockPlantCard = Mockito.mock(PlantCard.class);

    }
    @After
    public void tearDown(){
        mockPlantCard = null;
    }

    @Test
    public void testVerifyIfExists(){
        assertTrue("verify if exists", mockPlantList.stream().filter(o->o.getName().matches(mockPlantCard.getName())).findFirst().isPresent());
    }

}