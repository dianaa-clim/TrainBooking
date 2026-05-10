package ro.axonsoft.eval.minibank.model;

import java.util.List;

public class TransactionPageResponse {
    private List<TransactionResponse> content;
    private long totalElements;
    private int totalPages;
    private int number;
    private int size;

    public TransactionPageResponse(List<TransactionResponse> content,
                                   long totalElements,
                                   int totalPages,
                                   int number,
                                   int size) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.number = number;
        this.size = size;
    }

    public List<TransactionResponse> getContent() {
        return content;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getNumber() {
        return number;
    }

    public int getSize() {
        return size;
    }
}