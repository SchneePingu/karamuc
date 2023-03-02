package lib

import (
	"testing"

	"github.com/SchneePingu/karamuc/lib"
)

func TestFetchingData(t *testing.T) {
	date := "25.05.2023"

	bookingDataDto := lib.FetchBookingDataDto(date)

	numberOfDates := len(bookingDataDto.Dates)
	expectedNumberOfDates := 4

	if numberOfDates != expectedNumberOfDates {
		t.Fatalf(`Expected %d dates, found %d dates.`, expectedNumberOfDates, numberOfDates)
	}

	numberOfRoomSizes := len(bookingDataDto.RoomSizes)

	if numberOfRoomSizes == 0 {
		t.Fatalf(`Expected rooms, found none.`)
	}

	firstDate := string(bookingDataDto.Dates[0].Date)
	firstRoomSize := bookingDataDto.RoomSizes[0]

	numberOfDailyBookings := len(bookingDataDto.DailyBookings[firstDate][firstRoomSize])

	if numberOfDailyBookings == 0 {
		t.Fatalf(`Expected daily bookings, found none.`)
	}
}
