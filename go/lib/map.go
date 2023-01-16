package lib

import (
    "fmt"
    "strconv"
)

func mapBookingData(bookingData BookingData) BookingDataDto {
    dateDtos := make([]DateDto, 0, len(bookingData.dates))
    for _, date := range bookingData.dates {
        dateDto := mapDate(date)
        dateDtos = append(dateDtos, dateDto)
    }

    roomSizeDtos := make([]RoomSizeDto, 0, len(bookingData.roomSizes))
    for _, roomSize := range bookingData.roomSizes {
        roomSizeDto := mapRoomSize(roomSize)
        roomSizeDtos = append(roomSizeDtos, roomSizeDto)
    }

    dailyBookingsDto := mapDailyBookings(bookingData.dailyBookings)

    bookingDataDto := BookingDataDto{dateDtos, roomSizeDtos, dailyBookingsDto}

    return bookingDataDto
}

func mapDate(date Date) DateDto {
    var dateDto DateDto

    dateDto.Date = date
    dateDto.Weekday = getWeekday(date)

    return dateDto
}

func getWeekday(date Date) string {
    dateTime, err  := toDateTime(date)
    if (err != nil) {
        return ""
    }

    weekdayIndex := dateTime.Weekday()
    switch weekdayIndex {
        case 0: return "So"
        case 1: return "Mo"
        case 2: return "Di"
        case 3: return "Mi"
        case 4: return "Do"
        case 5: return "Fr"
        case 6: return "Sa"
        default: return ""
    }
}

func mapDailyBookings(dailyBookings DailyBookings) DailyBookingsDto {
    dailyBookingsDto := make(DailyBookingsDto)

    for date, roomBookings := range dailyBookings {
        roomBookingsDto := mapRoomBookings(roomBookings)

        dailyBookingsDto[date] = roomBookingsDto
    }

    return dailyBookingsDto
}

func mapRoomBookings(roomBookings RoomBookings) RoomBookingsDto {
    roomBookingsDto := make(RoomBookingsDto)

    for roomSize, bookingSlots := range roomBookings {
        bookingSlotsDto := make([]BookingSlotDto, 0, len(bookingSlots))

        for _, bookingSlot := range bookingSlots {
            bookingSlotsDto = append(bookingSlotsDto, mapBookingSlot(bookingSlot))
        }

        roomSizeDto := mapRoomSize(roomSize)
        roomBookingsDto[roomSizeDto] = bookingSlotsDto
    }

    return roomBookingsDto
}

func mapBookingSlot(bookingSlot BookingSlot) BookingSlotDto {
    return BookingSlotDto{mapTime(bookingSlot.from),
        mapTime(bookingSlot.until),
        bookingSlot.price,
        bookingSlot.roomName}
}

func mapTime(time Time) TimeDto {
    return fmt.Sprintf("%02d:%02d", time.hour, time.minute)
}

func mapRoomSize(roomSize RoomSize) RoomSizeDto {
    return strconv.Itoa(roomSize)
}
