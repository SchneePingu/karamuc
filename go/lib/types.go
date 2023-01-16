package lib

type BookingData struct {
    dates []Date
    roomSizes []RoomSize
    dailyBookings DailyBookings
}

type DailyBookings map[Date]RoomBookings
type Date = string

type RoomBookings = map[RoomSize][]BookingSlot
type RoomSize = int

type BookingSlot struct {
    from Time
    until Time
    price int
    roomName string
}

type Time struct {
    hour int
    minute int
}


type BookingDataDto struct {
    Dates []DateDto `json:"dates"`
    RoomSizes []RoomSizeDto `json:"roomSizes"`
    DailyBookings DailyBookingsDto `json:"bookings"`
}

type DateDto struct {
    Date Date `json:"date"`
    Weekday string `json:"weekday"`
}

type DailyBookingsDto = map[Date]RoomBookingsDto

type RoomBookingsDto = map[RoomSizeDto][]BookingSlotDto
type RoomSizeDto = string

type BookingSlotDto struct {
    From TimeDto `json:"from"`
    Until TimeDto `json:"until"`
    Price int `json:"price"`
    RoomName string `json:"roomName"`
}

type TimeDto = string
