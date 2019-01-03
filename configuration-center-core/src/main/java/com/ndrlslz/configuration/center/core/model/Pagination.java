package com.ndrlslz.configuration.center.core.model;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class Pagination {
    private long size;
    private long number;

    private Pagination(Builder builder) {
        this.size = builder.size;
        this.number = builder.number;
    }

    public long getSize() {
        return size;
    }

    public long getNumber() {
        return number;
    }

    public static void check(Pagination pagination) {
        checkNotNull(pagination, "pagination cannot be null");
        checkState(pagination.getNumber() > -1, "pagination number must not be negative!");
        checkState(pagination.getSize() > 0, "pagination size must be positive!");
    }

    public static class Builder {
        private long size;
        private long number;

        public Builder withSize(long size) {
            this.size = size;
            return this;
        }

        public Builder withNumber(long number) {
            this.number = number;
            return this;
        }

        public Pagination build() {
            return new Pagination(this);
        }
    }

}
