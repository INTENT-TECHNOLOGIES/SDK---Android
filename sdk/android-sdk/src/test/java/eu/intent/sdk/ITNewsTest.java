package eu.intent.sdk;

import org.junit.Test;

import eu.intent.sdk.model.ITNews;

import static junit.framework.Assert.assertEquals;

public class ITNewsTest {
    @Test
    public void jsonToNewsList() {
        String json = TestUtils.INSTANCE.getResourceAsString("news.json");
        ITNews[] newsList = TestUtils.INSTANCE.getGson().fromJson(json, ITNews[].class);
        assertEquals(3, newsList.length);
        assertEquals("1094f82e-496b-44b7-b5d7-56ca7f5ca2b8", newsList[0].id);
        assertEquals("6f277cc9-a19c-4a5b-a248-500a58e06a7f", newsList[1].id);
        assertEquals("e2c81312-8188-425e-a01a-d37b0620f585", newsList[2].id);
        assertEquals(ITNews.Type.INFO, newsList[0].type);
        assertEquals(ITNews.Type.TIP, newsList[1].type);
        assertEquals(ITNews.Type.EVENT, newsList[2].type);
        assertEquals(1453280215560L, newsList[0].timestamp);
        assertEquals(1437380650291L, newsList[1].timestamp);
        assertEquals(1430291942922L, newsList[2].timestamp);
        assertEquals("Remise en service de la chaufferie collective", newsList[0].title);
        assertEquals("Application Mes Services", newsList[1].title);
        assertEquals("Maintenance de l'ascenseur", newsList[2].title);
        assertEquals("Remise en service de la chaufferie collective en début d'après midi", newsList[0].text);
        assertEquals("Intent-Technologies vous souhaite une bonne réception et utilisation de l'application Mes Services.", newsList[1].text);
        assertEquals("L'ascenseur est hors service pour cause de problème technique. Il sera réparé dans les 2 jours à venir. Je reste à votre disposition pour les services de portage.", newsList[2].text);
        assertEquals(0, newsList[0].eventTimestamp);
        assertEquals(0, newsList[1].eventTimestamp);
        assertEquals(1430299876543L, newsList[2].eventTimestamp);
        assertEquals(null, newsList[0].eventLocation);
        assertEquals(null, newsList[1].eventLocation);
        assertEquals("Hall D", newsList[2].eventLocation);
    }
}
