package eu.intent.sdk.api;

/**
 * API results can sometimes be sorted by a property, ascending or descending.
 */

public class ITSort {
    public String property;
    public Order order;

    public ITSort(String property, Order order) {
        this.property = property;
        this.order = order;
    }

    @Override
    public String toString() {
        return (order == Order.DESCENDING ? "-" : "") + property;
    }

    public enum Order {
        ASCENDING, DESCENDING
    }
}
