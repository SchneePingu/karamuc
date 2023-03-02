package lib

import (
	"encoding/json"
	"sort"
	"sync"
	"time"
)

func FetchBookingData(date string) string {
	bookingDataDto := FetchBookingDataDto(date)

	return toJsonString(bookingDataDto)
}

func FetchBookingDataDto(date string) BookingDataDto {
	dailyBookings := make(DailyBookings)

	var processDailyBookings sync.WaitGroup

	workingDayDates := getWorkingDayDates(date)
	for _, workingDayDate := range workingDayDates {
		processDailyBookings.Add(1)

		go func(date string) {
			defer processDailyBookings.Done()

			response, err := requestBookingWebsite(date)
			if err != nil {
				return
			}

			defer response.Body.Close()
			roomBookings := readRoomBookings(response.Body)

			sortRoomBookings(roomBookings)

			dailyBookings[date] = roomBookings
		}(workingDayDate)
	}

	processDailyBookings.Wait()

	roomSizes := getRoomSizes(dailyBookings)
	bookingData := BookingData{workingDayDates, roomSizes, dailyBookings}

	return mapBookingData(bookingData)
}

func getWorkingDayDates(date string) []string {
	var workingDayDates []string

	dateTime, err := toDateTime(date)
	if err != nil {
		return workingDayDates
	}

	weekdayIndex := int(dateTime.Weekday())

	workingDayIndices := []int{3, 4, 5, 6}
	sort.Ints(workingDayIndices)

	for _, workingDayIndex := range workingDayIndices {
		daysBetweenWorkingDayAndWeekday := workingDayIndex - weekdayIndex

		workingDay := dateTime.AddDate(0, 0, daysBetweenWorkingDayAndWeekday)
		workingDayDate := fromDateTime(workingDay)

		workingDayDates = append(workingDayDates, workingDayDate)
	}

	return workingDayDates
}

func toDateTime(date string) (time.Time, error) {
	return time.Parse("02.01.2006", date)
}

func fromDateTime(date time.Time) string {
	return date.Format("02.01.2006")
}

func getRoomSizes(dailyBookings DailyBookings) []int {
	roomSizes := make([]int, 0)

	roomSizesSet := make(map[int]bool)

	for _, roomBookings := range dailyBookings {
		for roomSize, _ := range roomBookings {
			roomSizesSet[roomSize] = true
		}
	}

	for roomSize, _ := range roomSizesSet {
		roomSizes = append(roomSizes, roomSize)
	}

	sort.Ints(roomSizes)

	return roomSizes
}

func toJsonString(bookingDataDto BookingDataDto) string {
	byteArray, _ := json.Marshal(bookingDataDto)
	jsonString := string(byteArray)

	return jsonString
}
