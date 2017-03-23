package eu.intent.sdk;

import org.junit.Test;

import eu.intent.sdk.model.ITClassifiedAd;
import eu.intent.sdk.model.ITClassifiedAdCategory;
import eu.intent.sdk.model.ITClassifiedAdExpiry;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class ITClassifiedAdTest {
    @Test
    public void jsonToClassifiedAdList() {
        String json = TestUtils.INSTANCE.getResourceAsString("classified_ads.json");
        ITClassifiedAd[] ads = TestUtils.INSTANCE.getGson().fromJson(json, ITClassifiedAd[].class);
        assertEquals(3, ads.length);
        assertEquals("fdf3ddb0-af61-4935-9de2-b1fa70ff7013", ads[0].id);
        assertEquals("acfb15dd-5a2d-48e3-b386-554f97037888", ads[1].id);
        assertEquals("f0d59b70-4ec6-4f9a-9842-2487279d4f3f", ads[2].id);
        assertEquals(ITClassifiedAd.Visibility.HIDDEN, ads[0].visibility);
        assertEquals(ITClassifiedAd.Visibility.PUBLIC, ads[1].visibility);
        assertEquals(ITClassifiedAd.Visibility.PUBLIC, ads[2].visibility);
        assertEquals("cat_baby_sitting", ads[0].category);
        assertEquals("cat_computing", ads[1].category);
        assertEquals("cat_home_help", ads[2].category);
        assertEquals(ITClassifiedAd.Type.DEMAND, ads[0].type);
        assertEquals(ITClassifiedAd.Type.DEMAND, ads[1].type);
        assertEquals(ITClassifiedAd.Type.OFFER, ads[2].type);
        assertEquals("J'ai une petite fille de 6 mois à faire garder le semaine du 11 avril.", ads[0].text);
        assertEquals("Quelqu'un pourrait-il me proposer des cours d'informatique pour utiliser mon iPad ?", ads[1].text);
        assertEquals("Je peux faire le ménage chez vous en semaine et le samedi.", ads[2].text);
        assertEquals(1458829166222L, ads[0].creationDate);
        assertEquals(1458829288502L, ads[1].creationDate);
        assertEquals(1458829195404L, ads[2].creationDate);
        assertEquals(1458829260916L, ads[0].updateDate);
        assertEquals(1458829303748L, ads[1].updateDate);
        assertEquals(1458829195404L, ads[2].updateDate);
        assertEquals(1461248366222L, ads[0].expirationDate);
        assertEquals(1461248488502L, ads[1].expirationDate);
        assertEquals(1461248395404L, ads[2].expirationDate);
        assertEquals("John D.", ads[0].publisherName);
        assertEquals("Luke S.", ads[1].publisherName);
        assertEquals("John D.", ads[2].publisherName);
        assertEquals(true, ads[0].isMine);
        assertEquals(false, ads[1].isMine);
        assertEquals(true, ads[2].isMine);
        assertEquals(false, ads[0].hasMyReply);
        assertEquals(true, ads[1].hasMyReply);
        assertEquals(false, ads[2].hasMyReply);
        assertEquals(1, ads[0].replies.size());
        assertEquals(0, ads[1].replies.size());
        assertEquals(0, ads[2].replies.size());
        assertEquals("John D.", ads[0].replies.get(0).contactName);
        assertEquals("", ads[0].replies.get(0).contactPhone);
        assertEquals("g.lett@intent-technologies.eu", ads[0].replies.get(0).contactEmail);
        assertEquals("urn:bd22f84eb9ad4fb6a4db5d3af599915f.homes.demo1.intent:339b35b4-2597-410f-ab44-491b973b4cd4", ads[0].replies.get(0).contactId);
        assertEquals(1458829260916L, ads[0].replies.get(0).date);
    }

    @Test
    public void jsonToClassifiedAdExpiry() {
        String json = TestUtils.INSTANCE.getResourceAsString("classified_ad_expiry.json");
        ITClassifiedAdExpiry classifiedAdExpiry = TestUtils.INSTANCE.getGson().fromJson(json, ITClassifiedAdExpiry.class);
        assertTrue(classifiedAdExpiry.expirationDate > 0);
    }

    @Test
    public void jsonToClassifiedAdCategories() {
        String json = TestUtils.INSTANCE.getResourceAsString("classified_ad_categories.json");
        ITClassifiedAdCategory[] classifiedAdCategories = TestUtils.INSTANCE.getGson().fromJson(json, ITClassifiedAdCategory[].class);
        assertEquals(2, classifiedAdCategories.length);
        assertEquals("cat_event", classifiedAdCategories[0].key);
        assertEquals("cat_other", classifiedAdCategories[1].key);
        assertEquals(2, classifiedAdCategories[0].labels.size());
        assertEquals(2, classifiedAdCategories[1].labels.size());
        assertEquals("Event", classifiedAdCategories[0].labels.get("en"));
        assertEquals("Other", classifiedAdCategories[1].labels.get("en"));
        assertEquals("Évènement", classifiedAdCategories[0].labels.get("fr"));
        assertEquals("Autre", classifiedAdCategories[1].labels.get("fr"));
    }
}
