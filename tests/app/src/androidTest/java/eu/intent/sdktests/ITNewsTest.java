package eu.intent.sdktests;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.reflect.TypeToken;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Type;
import java.util.List;

import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.model.ITNews;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ITNewsTest {
    @Test
    public void jsonToNewsList() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.news);
        Type listType = new TypeToken<List<ITNews>>() {
        }.getType();
        List<ITNews> newsList = ITRetrofitUtils.getGson().fromJson(json, listType);
        assertEquals(3, newsList.size());
        assertEquals("1094f82e-496b-44b7-b5d7-56ca7f5ca2b8", newsList.get(0).id);
        assertEquals("6f277cc9-a19c-4a5b-a248-500a58e06a7f", newsList.get(1).id);
        assertEquals("e2c81312-8188-425e-a01a-d37b0620f585", newsList.get(2).id);
        assertEquals(ITNews.Type.INFO, newsList.get(0).type);
        assertEquals(ITNews.Type.TIP, newsList.get(1).type);
        assertEquals(ITNews.Type.EVENT, newsList.get(2).type);
        assertEquals(1453280215560L, newsList.get(0).timestamp);
        assertEquals(1437380650291L, newsList.get(1).timestamp);
        assertEquals(1430291942922L, newsList.get(2).timestamp);
        assertEquals("Remise en service de la chaufferie collective", newsList.get(0).title);
        assertEquals("Application Mes Services", newsList.get(1).title);
        assertEquals("Maintenance de l'ascenseur", newsList.get(2).title);
        assertEquals("Remise en service de la chaufferie collective en début d'après midi", newsList.get(0).text);
        assertEquals("Intent-Technologies vous souhaite une bonne réception et utilisation de l'application Mes Services.", newsList.get(1).text);
        assertEquals("L'ascenseur est hors service pour cause de problème technique. Il sera réparé dans les 2 jours à venir. Je reste à votre disposition pour les services de portage.", newsList.get(2).text);
        assertEquals(0, newsList.get(0).eventTimestamp);
        assertEquals(0, newsList.get(1).eventTimestamp);
        assertEquals(1430299876543L, newsList.get(2).eventTimestamp);
        assertEquals(null, newsList.get(0).eventLocation);
        assertEquals(null, newsList.get(1).eventLocation);
        assertEquals("Hall D", newsList.get(2).eventLocation);
    }
}
