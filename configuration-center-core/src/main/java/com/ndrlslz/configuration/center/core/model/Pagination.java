package com.ndrlslz.configuration.center.core.model;

import static com.google.common.base.Preconditions.checkState;

public class Pagination {
    private int size;
    private int number;

    private Pagination(Builder builder) {
        this.size = builder.size;
        this.number = builder.number;
    }

    public int getSize() {
        return size;
    }

    public int getNumber() {
        return number;
    }

    public static class Builder {
        private static final int DEFAULT_PAGINATION_SIZE = 10;
        private static final int DEFAULT_PAGINATION_NUMBER = 0;
        private int size = DEFAULT_PAGINATION_SIZE;
        private int number = DEFAULT_PAGINATION_NUMBER;

        public Builder withSize(int size) {
            checkState(size > 0, "pagination size must be positive!");

            this.size = size;
            return this;
        }

        public Builder withNumber(int number) {
            checkState(number > -1, "pagination number must not be negative!");

            this.number = number;
            return this;
        }

        public Pagination build() {
            return new Pagination(this);
        }
    }

}
