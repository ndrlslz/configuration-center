package com.ndrlslz.configuration.center.core.model;

import java.util.List;

public class Page<T> {
    private List<T> content;
    private int size;
    private int number;
    private int totalElements;
    private int totalPages;

    private Page(Builder<T> pageBuilder) {
        this.content = pageBuilder.content;
        this.size = pageBuilder.size;
        this.number = pageBuilder.number;
        this.totalElements = pageBuilder.totalElements;
        this.totalPages = pageBuilder.totalPages;
    }

    public List<T> getContent() {
        return content;
    }

    public int getSize() {
        return size;
    }

    public int getNumber() {
        return number;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public static class Builder<T> {
        private List<T> content;
        private int size;
        private int number;
        private int totalElements;
        private int totalPages;

        public Builder<T> withContent(List<T> content) {
            this.content = content;
            return this;
        }

        public Builder<T> withSize(int size) {
            this.size = size;
            return this;
        }

        public Builder<T> withNumber(int number) {
            this.number = number;
            return this;
        }

        public Builder<T> withTotalElements(int totalElements) {
            this.totalElements = totalElements;
            return this;
        }

        public Builder<T> withTotalPages(int totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        public Page<T> build() {
            return new Page<>(this);
        }
    }
}
