package eu.intent.sdktests;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.reflect.TypeToken;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Type;
import java.util.List;

import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.model.ITClassifiedAd;
import eu.intent.sdk.model.ITClassifiedAdCategory;
import eu.intent.sdk.model.ITClassifiedAdExpiry;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ITClassifiedAdTest {
    @Test
    public void jsonToClassifiedAdList() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.classified_ads);
        Type listType = new TypeToken<List<ITClassifiedAd>>() {
        }.getType();
        List<ITClassifiedAd> ads = ITRetrofitUtils.getGson().fromJson(json, listType);
        assertEquals(3, ads.size());
        assertEquals("fdf3ddb0-af61-4935-9de2-b1fa70ff7013", ads.get(0).id);
        assertEquals("acfb15dd-5a2d-48e3-b386-554f97037888", ads.get(1).id);
        assertEquals("f0d59b70-4ec6-4f9a-9842-2487279d4f3f", ads.get(2).id);
        assertEquals(ITClassifiedAd.Visibility.HIDDEN, ads.get(0).visibility);
        assertEquals(ITClassifiedAd.Visibility.PUBLIC, ads.get(1).visibility);
        assertEquals(ITClassifiedAd.Visibility.PUBLIC, ads.get(2).visibility);
        assertEquals("cat_baby_sitting", ads.get(0).category);
        assertEquals("cat_computing", ads.get(1).category);
        assertEquals("cat_home_help", ads.get(2).category);
        assertEquals(ITClassifiedAd.Type.DEMAND, ads.get(0).type);
        assertEquals(ITClassifiedAd.Type.DEMAND, ads.get(1).type);
        assertEquals(ITClassifiedAd.Type.OFFER, ads.get(2).type);
        assertEquals("J'ai une petite fille de 6 mois à faire garder le semaine du 11 avril.", ads.get(0).text);
        assertEquals("Quelqu'un pourrait-il me proposer des cours d'informatique pour utiliser mon iPad ?", ads.get(1).text);
        assertEquals("Je peux faire le ménage chez vous en semaine et le samedi.", ads.get(2).text);
        assertEquals(1458829166222l, ads.get(0).creationDate);
        assertEquals(1458829288502l, ads.get(1).creationDate);
        assertEquals(1458829195404l, ads.get(2).creationDate);
        assertEquals(1458829260916l, ads.get(0).updateDate);
        assertEquals(1458829303748l, ads.get(1).updateDate);
        assertEquals(1458829195404l, ads.get(2).updateDate);
        assertEquals(1461248366222l, ads.get(0).expirationDate);
        assertEquals(1461248488502l, ads.get(1).expirationDate);
        assertEquals(1461248395404l, ads.get(2).expirationDate);
        assertEquals("John D.", ads.get(0).publisherName);
        assertEquals("Luke S.", ads.get(1).publisherName);
        assertEquals("John D.", ads.get(2).publisherName);
        assertEquals(true, ads.get(0).isMine);
        assertEquals(false, ads.get(1).isMine);
        assertEquals(true, ads.get(2).isMine);
        assertEquals(false, ads.get(0).hasMyReply);
        assertEquals(true, ads.get(1).hasMyReply);
        assertEquals(false, ads.get(2).hasMyReply);
        assertEquals(1, ads.get(0).replies.size());
        assertEquals(0, ads.get(1).replies.size());
        assertEquals(0, ads.get(2).replies.size());
        assertEquals("John D.", ads.get(0).replies.get(0).contactName);
        assertEquals("", ads.get(0).replies.get(0).contactPhone);
        assertEquals("g.lett@intent-technologies.eu", ads.get(0).replies.get(0).contactEmail);
        assertEquals("urn:bd22f84eb9ad4fb6a4db5d3af599915f.homes.demo1.intent:339b35b4-2597-410f-ab44-491b973b4cd4", ads.get(0).replies.get(0).contactId);
        assertEquals(1458829260916l, ads.get(0).replies.get(0).date);
    }

    @Test
    public void jsonToClassifiedAdExpiry() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.classified_ad_expiry);
        ITClassifiedAdExpiry classifiedAdExpiry = ITRetrofitUtils.getGson().fromJson(json, ITClassifiedAdExpiry.class);
        assertTrue(classifiedAdExpiry.expirationDate > 0);
    }

    @Test
    public void jsonToClassifiedAdCategories() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.classified_ad_categories);
        Type listType = new TypeToken<List<ITClassifiedAdCategory>>() {
        }.getType();
        List<ITClassifiedAdCategory> classifiedAdCategories = ITRetrofitUtils.getGson().fromJson(json, listType);
        assertEquals(2, classifiedAdCategories.size());
        assertEquals("cat_event", classifiedAdCategories.get(0).key);
        assertEquals("cat_other", classifiedAdCategories.get(1).key);
        assertEquals(2, classifiedAdCategories.get(0).labels.size());
        assertEquals(2, classifiedAdCategories.get(1).labels.size());
        assertEquals("Event", classifiedAdCategories.get(0).labels.get("en"));
        assertEquals("Other", classifiedAdCategories.get(1).labels.get("en"));
        assertEquals("Évènement", classifiedAdCategories.get(0).labels.get("fr"));
        assertEquals("Autre", classifiedAdCategories.get(1).labels.get("fr"));
    }
}
