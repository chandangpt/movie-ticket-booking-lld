package com.chandan.api;

import com.chandan.services.BookingService;
import com.chandan.services.PaymentsService;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class PaymentController {
    private final PaymentsService paymentService;
    private final BookingService bookingService;
    public void paymentFailed(@NonNull final String bookingId, @NonNull final String userId) {
        paymentService.processPaymentFailed(bookingService.getBooking(bookingId), userId);
    }

    public void paymentSuccess(@NonNull final String bookingId, @NonNull final String userId) {
        bookingService.confirmBooking(bookingService.getBooking(bookingId), userId);
    }
}
