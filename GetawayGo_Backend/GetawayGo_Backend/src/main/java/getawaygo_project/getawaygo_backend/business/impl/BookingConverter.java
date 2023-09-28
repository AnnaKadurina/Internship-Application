package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.domain.Booking;
import getawaygo_project.getawaygo_backend.persistance.entity.BookingEntity;

public class BookingConverter {
    private BookingConverter(){}
    public static Booking convert(BookingEntity booking){
        return Booking.builder()
                .bookingId(booking.getBookingId())
                .userId(booking.getUserId().getUserId())
                .propertyId(booking.getPropertyId().getPropertyId())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .price(booking.getPrice())
                .build();
    }
}
