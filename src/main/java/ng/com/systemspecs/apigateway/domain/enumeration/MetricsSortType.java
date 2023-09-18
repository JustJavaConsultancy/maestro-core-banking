package ng.com.systemspecs.apigateway.domain.enumeration;

public enum MetricsSortType {

    WEEK("week"), MONTH("month"), YEAR("year");

    String name ;

    MetricsSortType(String name) {
        this.name = name;
    }
}
