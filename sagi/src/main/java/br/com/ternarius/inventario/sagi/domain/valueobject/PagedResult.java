package br.com.ternarius.inventario.sagi.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PagedResult {
    private final int sizeDefault = 10;
    private final int totalPages;
    private long totalElements;
    private int size;
    private int number;

    public Boolean verifyPageRamge(int pageRequest) {
        return totalPages >= pageRequest;
    }

    public Boolean hasPrevious() {
        return number > 0;
    }

    public Boolean hasNext() {
        return totalPages - 1 > number;
    }

    public Boolean hasSameTotalPages(int totalPages) {
        return this.totalPages == totalPages;
    }

    public Boolean verifySizeRange(int sizeRequest) {
        return sizeRequest <= sizeDefault;
    }
}