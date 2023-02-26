package lib

import (
	"errors"
	"fmt"
	"io"
	"log"
	"regexp"
	"strconv"
	"strings"

	"github.com/PuerkitoBio/goquery"
)

func readRoomBookings(reader io.ReadCloser) RoomBookings {
	defer reader.Close()

	readerContent, _ := goquery.NewDocumentFromReader(reader)

	roomBookings := make(RoomBookings)

	// The booking data for a room is given in the following HTML format:
	// <div class="single_product"> BOOKING DATA </div>
	readerContent.Find(".single_product").Each(
		func(_ int, roomSelection *goquery.Selection) {
			roomSize, err := readRoomSize(roomSelection)
			if err != nil {
				log.Println(err.Error())
				return
			}

			bookingSlots := readBookingSlots(roomSelection)
			if len(bookingSlots) == 0 {
				return
			}

			existingBookingSlots := roomBookings[roomSize]
			roomBookings[roomSize] = append(existingBookingSlots,
				bookingSlots[:]...)
		})

	return roomBookings
}

func readRoomSize(roomSelection *goquery.Selection) (RoomSize, error) {
	// The room size (number of persons) is given in the following HTML format:
	// <div class="product_person">  2 Personen  </div>
	roomSizeDescription := roomSelection.Find(".product_person").First().Text()

	roomSizePattern := regexp.MustCompile(`\d+`)
	roomSizeAsString := string(roomSizePattern.Find(
		[]byte(roomSizeDescription)))

	roomSize, err := strconv.Atoi(roomSizeAsString)
	if err != nil {
		return roomSize, errors.New(
			fmt.Sprintf("Cannot read room size from '%s' (%s).",
				roomSizeAsString, roomSizeDescription))
	}

	return roomSize, nil
}

func readRoomName(roomSelection *goquery.Selection) string {
	// The room name is given in the following HTML format:
	// <div class="product_name">  Fugu  </div>
	roomNameDescription := roomSelection.Find(".product_name").First().Text()

	roomName := strings.TrimSpace(roomNameDescription)

	return roomName
}

func readBookingSlots(roomSelection *goquery.Selection) []BookingSlot {
	bookingSlots := make([]BookingSlot, 0)

	roomName := readRoomName(roomSelection)

	// A room's booking slots are given in the following HTML format:
	// <div class="product_meta">
	// <div class="row_time_slot"> TIME SLOTS </div> </div>
	roomSelection.Find(".product_meta").First().Find(".row_time_slot").Each(
		func(_ int, bookingSlotSelection *goquery.Selection) {
			bookingSlot, err := readBookingSlot(bookingSlotSelection)
			if err != nil {
				log.Println(err.Error())
				return
			}

			if bookingSlot.isNotAvailable {
				return
			}

			bookingSlot.roomName = roomName

			bookingSlots = append(bookingSlots, bookingSlot)
		})

	return bookingSlots
}

func readBookingSlot(bookingSlotSelection *goquery.Selection) (BookingSlot,
	error) {
	var bookingSlot BookingSlot

	bookingSlot.isNotAvailable = isBookingSlotNotBookable(bookingSlotSelection)

	if bookingSlot.isNotAvailable {
		return bookingSlot, nil
	}

	from, until, err := readTimesForBookingSlot(bookingSlotSelection)
	if err != nil {
		return bookingSlot, err
	}

	bookingSlot.from = from
	bookingSlot.until = until

	price, err := readPriceForBookingSlot(bookingSlotSelection)
	if err != nil {
		return bookingSlot, err
	}

	bookingSlot.price = price

	return bookingSlot, nil
}

func isBookingSlotNotBookable(bookingSlotSelection *goquery.Selection) bool {
	htmlClasses, _ := bookingSlotSelection.Attr("class")
	// The website uses a HTML class with the name "not_avaliable"
	// when a time slot is not available for booking.
	isNotAvailable := strings.Contains(htmlClasses, "not_avaliable")
	// The website uses a class with the name "display_none"
	// when a time slot is not visible for booking.
	isNotVisible := strings.Contains(htmlClasses, "display_none")

	return isNotAvailable || isNotVisible
}

func readTimesForBookingSlot(bookingSlotSelection *goquery.Selection) (Time,
	Time, error) {
	var from Time
	var until Time

	// The time span of a booking slot is given in the following HTML format:
	// <div class="product_time_slot">  17:00 - 19:00  </div>
	timeSpanDescription := bookingSlotSelection.Find(".product_time_slot").
		First().Text()

	timePattern := regexp.MustCompile(`\w+:\w+`)
	timesAsString := timePattern.FindAll([]byte(timeSpanDescription), -1)
	if len(timesAsString) != 2 {
		return from, until, errors.New(
			fmt.Sprintf("Cannot read time slot from '%s'",
				timeSpanDescription))
	}

	from, err := toTime(string(timesAsString[0]))
	if err != nil {
		return from, until, err
	}

	until, err = toTime(string(timesAsString[1]))
	if err != nil {
		return from, until, err
	}

	return from, until, nil
}

func toTime(timeAsString string) (Time, error) {
	var time Time

	numbersInTime := strings.Split(timeAsString, ":")
	if len(numbersInTime) != 2 {
		return time, errors.New(fmt.Sprintf("Cannot read time from '%s'.",
			timeAsString))
	}

	hour, err := strconv.Atoi(numbersInTime[0])
	if err != nil {
		return time, errors.New(
			fmt.Sprintf("Cannot read hour from '%s' (%s).", numbersInTime[0],
				timeAsString))
	}

	minute, err := strconv.Atoi(numbersInTime[1])
	if err != nil {
		return time, errors.New(
			fmt.Sprintf("Cannot read minute from '%s' (%s).", numbersInTime[1],
				timeAsString))
	}

	time.hour = hour
	time.minute = minute

	return time, nil
}

func readPriceForBookingSlot(bookingSlotSelection *goquery.Selection) (int,
	error) {
	// The price of a booking slot is given in the following HTML format:
	// <div class="product_price_slot">  EuroSymbol 100  </div>
	priceDescription := bookingSlotSelection.Find(".product_price_slot").
		First().Text()

	pricePattern := regexp.MustCompile(`\w+`)
	priceAsString := string(pricePattern.Find([]byte(priceDescription)))

	price, err := strconv.Atoi(priceAsString)
	if err != nil {
		return price, errors.New(
			fmt.Sprintf("Cannot read price from '%s' (%s).", priceAsString,
				priceDescription))
	}

	return price, nil
}
