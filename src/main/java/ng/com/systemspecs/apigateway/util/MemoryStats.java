package ng.com.systemspecs.apigateway.util;

public class MemoryStats {
    public Long getHeapSize() {
        return heapSize;
    }

    public void setHeapSize(Long heapSize) {
        this.heapSize = heapSize;
    }

    public Long getHeapMaxSize() {
        return heapMaxSize;
    }

    public void setHeapMaxSize(Long heapMaxSize) {
        this.heapMaxSize = heapMaxSize;
    }

    public Long getHeapFreeSize() {
        return heapFreeSize;
    }

    public void setHeapFreeSize(Long heapFreeSize) {
        this.heapFreeSize = heapFreeSize;
    }

    private Long heapSize=Runtime.getRuntime().totalMemory()/(1024*1024);;
    private Long heapMaxSize=Runtime.getRuntime().maxMemory()/(1024*1024);;
    private Long heapFreeSize=Runtime.getRuntime().freeMemory()/(1024*1024);;
}
