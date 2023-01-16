package lib

import (
    "sort"
)

func sortRoomBookings(roomBookings RoomBookings) {
    for _, bookingSlots := range roomBookings {
        sortBookingSlots(bookingSlots)
    }
}

func sortBookingSlots(slots []BookingSlot) {
    sort.Slice(slots, func(i, j int) bool {
        if (slots[i].from.hour != slots[j].from.hour) {
            return rebaseHourTimeToNoon(slots[i].from.hour) <
                rebaseHourTimeToNoon(slots[j].from.hour)
        }

        if (slots[i].from.minute != slots[j].from.minute) {
            return slots[i].from.minute < slots[j].from.minute
        }

        if (slots[i].until.hour != slots[j].until.hour) {
            return rebaseHourTimeToNoon(slots[i].until.hour) <
                rebaseHourTimeToNoon(slots[j].until.hour)
        }

        if (slots[i].until.minute != slots[j].until.minute) {
            return slots[i].until.minute < slots[j].until.minute
        }

        if (slots[i].price != slots[j].price) {
            return (slots[i].price < slots[j].price)
        }

        return slots[i].roomName < slots[j].roomName
    })
}

func rebaseHourTimeToNoon(hour int) int {
   return (hour + 12) % 24
}
