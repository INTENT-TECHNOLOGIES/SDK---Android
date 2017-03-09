package eu.intent.sdk.api;

import android.content.Context;

import java.util.Date;
import java.util.List;

import eu.intent.sdk.api.internal.ProxyCallback;
import eu.intent.sdk.model.ITAssetType;
import eu.intent.sdk.model.ITLinkedAssetType;
import eu.intent.sdk.model.ITTicket;
import eu.intent.sdk.util.ITDateUtils;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * A wrapper to call the Ticket / Operation API.
 */

public class ITTicketApi {
    private Service mService;

    public ITTicketApi(Context context) {
        mService = ITRetrofitUtils.getRetrofitInstance(context).create(Service.class);
    }

    /**
     * Searches tickets.
     */
    public void search(SearchCriteria criteria, ITApiCallback<List<ITTicket>> callback) {
        mService.search(criteria).enqueue(new ProxyCallback<>(callback));
    }

    private interface Service {
        @POST("operations/v1/tickets/search")
        Call<List<ITTicket>> search(@Body SearchCriteria criteria);
    }

    public static class SearchCriteria {
        public String assetReference;
        public ITAssetType assetType;
        public String caseReference;
        public String contractReference;
        public String from;
        public String interventionReference;
        public String[] linkedAssets;
        public String status;
        public String to;
        public String tradeReference;
        public String updatedSince;
        public Boolean withLogs;

        public static class Builder {
            private String mAssetReference;
            private ITAssetType mAssetType;
            private String mCaseReference;
            private String mContractReference;
            private long mFrom;
            private String mInterventionReference;
            private String[] mLinkedAssets;
            private String mStatus;
            private long mTo;
            private String mTradeReference;
            private long mUpdatedSince;
            private Boolean mWithLogs;

            public SearchCriteria create() {
                SearchCriteria criteria = new SearchCriteria();
                criteria.assetReference = mAssetReference;
                criteria.assetType = mAssetType;
                criteria.caseReference = mCaseReference;
                criteria.contractReference = mContractReference;
                criteria.from = mFrom > 0 ? ITDateUtils.formatDateIso8601(new Date(mFrom)) : null;
                criteria.interventionReference = mInterventionReference;
                criteria.linkedAssets = mLinkedAssets;
                criteria.status = mStatus;
                criteria.to = mTo > 0 ? ITDateUtils.formatDateIso8601(new Date(mTo)) : null;
                criteria.tradeReference = mTradeReference;
                criteria.updatedSince = mUpdatedSince > 0 ? ITDateUtils.formatDateIso8601(new Date(mUpdatedSince)) : null;
                criteria.withLogs = mWithLogs;
                return criteria;
            }

            public Builder assetReference(String assetReference) {
                mAssetReference = assetReference;
                return this;
            }

            public Builder assetType(ITAssetType assetType) {
                mAssetType = assetType;
                return this;
            }

            public Builder caseReference(String caseReference) {
                mCaseReference = caseReference;
                return this;
            }

            public Builder contractReference(String contractReference) {
                mContractReference = contractReference;
                return this;
            }

            public Builder fromDate(long timestamp) {
                mFrom = timestamp;
                return this;
            }

            public Builder interventionReference(String interventionReference) {
                mInterventionReference = interventionReference;
                return this;
            }

            public Builder status(String status) {
                mStatus = status;
                return this;
            }

            public Builder toDate(long timestamp) {
                mTo = timestamp;
                return this;
            }

            public Builder tradeReference(String tradeReference) {
                mTradeReference = tradeReference;
                return this;
            }

            public Builder updatedSince(long timestamp) {
                mUpdatedSince = timestamp;
                return this;
            }

            public Builder withLinkedAssets(ITLinkedAssetType... assetTypes) {
                if (assetTypes == null) {
                    mLinkedAssets = null;
                } else {
                    mLinkedAssets = new String[assetTypes.length];
                    for (int i = 0; i < assetTypes.length; i++) {
                        mLinkedAssets[i] = assetTypes[i].toString();
                    }
                }
                return this;
            }

            public Builder withLogs(boolean returnLogsInResult) {
                mWithLogs = returnLogsInResult;
                return this;
            }
        }
    }
}
