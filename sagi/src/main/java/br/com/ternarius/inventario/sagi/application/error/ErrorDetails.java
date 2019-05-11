package br.com.ternarius.inventario.sagi.application.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {
    private String title;
    private int status;
    private long timestamp;
    private String message;


    public static final class Builder {
        private String title;
        private int status;
        private long timestamp;
        private String message;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder timestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public ErrorDetails build() {
            ErrorDetails errorDetails = new ErrorDetails();

            errorDetails.status = this.status;
            errorDetails.title = this.title;
            errorDetails.message = this.message;
            errorDetails.timestamp = this.timestamp;

            return errorDetails;
        }
    }
}
